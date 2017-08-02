package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ImproveLikesMapper;
import com.longbei.appservice.dao.ImproveTopicMapper;
import com.longbei.appservice.dao.SuperTopicMapper;
import com.longbei.appservice.dao.TimeLineDetailDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.ImproveTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("improveTopicService")
public class ImproveTopicServiceImpl implements ImproveTopicService{

    private static Logger logger = LoggerFactory.getLogger(ImproveTopicServiceImpl.class);

    @Autowired
    private ImproveTopicMapper improveTopicMapper;
    @Autowired
    private ImproveTopicService improveTopicService;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private TimeLineDetailDao timeLineDetailDao;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private SuperTopicMapper superTopicMapper;
    @Autowired
    private ImproveService improveService;
    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private ImproveLikesMapper improveLikesMapper;

    @Override
    public BaseResp<Page<TimeLineDetail>> selectImpTopicList(String topicId,String businesstype,String isTopic,String brief, String usernickname, Integer startNum, Integer pageSize) {
        BaseResp<Page<TimeLineDetail>> baseResp = new BaseResp<>();
        Page<TimeLineDetail> page = new Page<TimeLineDetail>(startNum/pageSize+1,pageSize);
        try {
            List<String> userids = new ArrayList<>();
            if (!StringUtils.isBlank(usernickname)){
                AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
                appUserMongoEntity.setNickname(usernickname);
                List<AppUserMongoEntity> users = userMongoDao.getAppUsers(appUserMongoEntity);
                for (AppUserMongoEntity user : users){
                    userids.add(user.getId());
                }
            }
            int totalcount = Integer.parseInt(String.valueOf
                        (timeLineDetailDao.selectImpTopicListCount(topicId, businesstype, isTopic, brief, userids)));
            List<TimeLineDetail> timeLineDetails = timeLineDetailDao.selectImpTopicList
                        (topicId, businesstype, isTopic, brief, userids, startNum, pageSize);
            Page.setPageNo(startNum / pageSize + 1, totalcount, pageSize);
            page.setTotalCount(totalcount);
            page.setList(timeLineDetails);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select recommend improve list from mongo is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Page<ImproveTopic>> selectListFromImpTopic(String topicId, String businesstype, String isTopic, String brief, String nickname, Integer startNum, Integer pageSize) {
        BaseResp<Page<ImproveTopic>> baseResp = new BaseResp<>();
        Page<ImproveTopic> page = new Page<ImproveTopic>(startNum/pageSize+1,pageSize);
        try {
                ImproveTopic improveTopic1 =new ImproveTopic();
                improveTopic1.setSupertopicid(Long.parseLong(topicId));
                if(StringUtils.isNotBlank(businesstype)) {
                    improveTopic1.setBusinesstype(Long.parseLong(businesstype));
                }
                if(StringUtils.isNotBlank(businesstype)) {
                    improveTopic1.setBusinesstype(Long.parseLong(businesstype));
                }
                if(StringUtils.isNotBlank(isTopic)) {
                    improveTopic1.setIsdel(isTopic);
                }
                int totalcount = improveTopicMapper.selectImproveTopicListCount(improveTopic1,nickname);
                Page.setPageNo(startNum/pageSize+1,totalcount,pageSize);
                List<ImproveTopic> improveTopicList = new ArrayList<ImproveTopic>();
                improveTopicList = improveTopicMapper.selectImproveTopicList(improveTopic1,nickname,startNum,pageSize);
                if(null == improveTopicList){
                    return baseResp.initCodeAndDesp();
                }
                for (int i = 0; i < improveTopicList.size(); i++) {
                    ImproveTopic improveTopic = improveTopicList.get(i);
                    TimeLineDetail timeLineDetail = timeLineDetailDao.selectTimeLineByImpid(improveTopicList.get(i).getImpid());
                    Improve improve = improveService.selectImproveByImpidMuc(improveTopic.getImpid(),null,improveTopic.getBusinesstype()+"",improveTopic.getBusinessid()+"");
                    improve.setAppUserMongoEntity(timeLineDetail.getUser());
                    Date createtime = DateUtils.formatDate(improveTopicList.get(i).getCreatetime(), "yyyy-MM-dd HH:mm:ss");
                    improveTopicList.get(i).setCreatetime(DateUtils.formatDateTime1(createtime));
                    //初始化赞数
                    improve.setLikes(getLikeFromRedis(String.valueOf(improve.getImpid())));
                    improveTopicList.get(i).setImprove(improve);
                }
                page.setTotalCount(totalcount);
                page.setList(improveTopicList);
                baseResp = BaseResp.ok();
                baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectListFromImpTopic is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateImproveTopicStatus(String topicId,final String businesstype, final List<Long> impids, String isTopic) {
        BaseResp baseResp = new BaseResp();
        try {
              if(StringUtils.isBlank(isTopic)) {
                 return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
              }
              if ("1".equals(isTopic)) {
                  for (int i = 0; i < impids.size(); i++) {
                      ImproveTopic improveTopic = improveTopicMapper.selectImproveTopicByImpId(impids.get(i));
                      if (null == improveTopic) {
                          ImproveTopic improveTopic1=new ImproveTopic();
                          TimeLineDetail timeLineDetail = timeLineDetailDao.selectTimeLineByImpid(impids.get(i));
                          improveTopic1.setSupertopicid(Long.parseLong(topicId));
                          improveTopic1.setImpid(impids.get(i));
                          improveTopic1.setBusinesstype(Long.parseLong(timeLineDetail.getBusinesstype()));
                          improveTopic1.setBusinessid(timeLineDetail.getBusinessid());
                          improveTopic1.setGtype(timeLineDetail.getItype());
                          improveTopic1.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                          improveTopic1.setIsdel(isTopic);
                          improveTopic1.setSort(0);
                          try {
                              int n = improveTopicMapper.insertImproveTopic(improveTopic1);
                              if(n >= 1){//逐条更新monggo状态和话题统计进步数量
                                  List<Long> impid =new ArrayList<>();
                                  impid.add(impids.get(i));
                                  timeLineDetailDao.updateImproveTopicStatus(impid,businesstype,isTopic);
                                  superTopicMapper.updateImpcount(Long.parseLong(topicId),1);
                              }
                          } catch (Exception e) {
                              logger.error("insertImproveTopic error and msg={}", e);
                          }
                      }else {
                          ImproveTopic improveTopic2=new ImproveTopic();
                          improveTopic2.setImpid(improveTopic.getImpid());
                          improveTopic2.setIsdel(isTopic);
                          improveTopic2.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                          improveTopic2.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                          try {
                              int n = improveTopicMapper.updateImproveTopicByImpId(improveTopic2);
                              if(n >= 1){
                                  if(n >= 1){//逐条更新monggo状态和话题统计进步数量
                                      List<Long> impid =new ArrayList<>();
                                      impid.add(improveTopic2.getImpid());
                                      timeLineDetailDao.updateImproveTopicStatus(impid,businesstype,isTopic);
                                      superTopicMapper.updateImpcount(Long.parseLong(topicId),1);
                                  }
                              }
                          } catch (Exception e) {
                              logger.error("updateImproveTopicByImpId error and msg={}",e);
                          }
                      }
                  }
              } else if ("0".equals(isTopic)){
                  int n= improveTopicMapper.updateImpTopicStatusByImpId(impids,isTopic);
                  if(n >= impids.size()) {
                      timeLineDetailDao.updateImproveTopicStatus(impids, businesstype, isTopic);
                      superTopicMapper.updateImpcount(Long.parseLong(topicId), -(impids.size()));
                  }
              }
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("updateImproveTopicStatus is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp updateImproveTopicSort(Long impid, String businesstype, Integer topicSort) {
        BaseResp baseResp = new BaseResp();
        try {
            ImproveTopic improveTopic2=new ImproveTopic();
            improveTopic2.setImpid(impid);
            improveTopic2.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
            improveTopic2.setSort(topicSort);
            int n = improveTopicMapper.updateImproveTopicByImpId(improveTopic2);
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("updateImproveTopicSort is error:", e);
        }
        return baseResp;
    }

    private int getLikeFromRedis(String impid){
        String count = springJedisDao.get(Constant.REDIS_IMPROVE_LIKE + impid);
        if (StringUtils.isBlank(count)){
            ImproveLikes improveLikes = improveLikesMapper.selectByimpid(impid);
            springJedisDao.increment(Constant.REDIS_IMPROVE_LIKE + impid,improveLikes==null?0:improveLikes.getLikes());
            return improveLikes==null?0:improveLikes.getLikes();
        } else {
            return Integer.parseInt(count);
        }
    }
}
