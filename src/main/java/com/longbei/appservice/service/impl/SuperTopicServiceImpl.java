package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ImproveTopicMapper;
import com.longbei.appservice.dao.SnsFansMapper;
import com.longbei.appservice.dao.SnsFriendsMapper;
import com.longbei.appservice.dao.SuperTopicMapper;
import com.longbei.appservice.dao.UserCollectMapper;
import com.longbei.appservice.dao.mongo.dao.ImproveMongoDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveLFD;
import com.longbei.appservice.entity.ImproveTopic;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.entity.SnsFriends;
import com.longbei.appservice.entity.SuperTopic;
import com.longbei.appservice.entity.UserCollect;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.SuperTopicService;
import com.longbei.appservice.service.UserRelationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 超级话题服务
 * Created by lixb on 2017/3/1.
 */
@Service
public class SuperTopicServiceImpl implements SuperTopicService {

    private static Logger logger = LoggerFactory.getLogger(SuperTopicServiceImpl.class);

    @Autowired
    private SuperTopicMapper superTopicMapper;
    @Autowired
    private ImproveService improveService;
    @Autowired
    private ImproveTopicMapper improveTopicMapper;
    @Autowired
    private CommentMongoService commentMongoService;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private UserCollectMapper userCollectMapper;
    @Autowired
    private ImproveMongoDao improveMongoDao;
    @Autowired
    private SnsFriendsMapper snsFriendsMapper;
    @Autowired
    private SnsFansMapper snsFansMapper;
    @Autowired
    private UserRelationService userRelationService;

    /**
     * 获取超级话题列表
     * @param startNum
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<Object> selectSuerTopicList(int startNum, int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<SuperTopic> list = superTopicMapper.selectList(startNum,endNum);
            baseResp.setData(list);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("startNum={},endNum={}",startNum,endNum,e);
        }
        return baseResp;
    }

    /**
     * 查询超级话题下的进步列表
     * @param topicId
     * @param startNum
     * @param endNum
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<Object> selectImprovesByTopicId(long userid,long topicId, int startNum, int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<Improve> list = new ArrayList<>();
            List<ImproveTopic> improveTopics = improveTopicMapper.selectByTopicId(topicId,startNum,endNum);
            if(null == improveTopics){
                return baseResp.initCodeAndDesp();
            }
            for (int i = 0; i < improveTopics.size(); i++) {
                ImproveTopic improveTopic = improveTopics.get(i);
                //Long impid,String userid,String businesstype,String businessid
                Improve improve = improveService.selectImproveByImpid(improveTopic.getImpid(),userid+"",improveTopic.getBusinesstype()+"",improveTopic.getBusinessid()+"");
                initImproveListOtherInfo(userid, improve);
                list.add(improve);
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(list);
        }catch (Exception e){
            logger.error("topicId={},startNum={},endNum={}",topicId,startNum,endNum,e);
        }
        return baseResp;
    }
    
    /**
     * 初始化进步附加信息
     * @param improve
     * @author luye
     */
    public void initImproveListOtherInfo(long userid, Improve improve){
        if(null == improve){
            return;
        }
        //初始化评论数量
        initImproveCommentInfo(improve);
        //初始化进步用户信息
        initImproveUserInfo(improve, userid);
        //初始化点赞，送花，送钻简略信息
        initLikeFlowerDiamondInfo(improve);
        //初始化是否 点赞 送花 送钻 收藏
        initIsOptionForImprove(String.valueOf(userid), improve);
    }
    
    /**
     * 向improve中的评论数赋值
     * @param improve
     * @author:luye
     */
    private void initImproveCommentInfo(Improve improve){

        if (null == improve){
            return;
        }
        //对进步的评论数赋值
        String businessid = "";
        if(StringUtils.isBlank(improve.getBusinessid().toString()) || improve.getBusinessid() == 0){
        	businessid = improve.getImpid().toString();
        }else{
        	businessid = improve.getBusinessid().toString();
        }
        BaseResp<Integer> baseResp = commentMongoService.selectCommentCountSum
                        (businessid, improve.getBusinesstype());
        if (ResultUtil.isSuccess(baseResp)){
            improve.setCommentnum(baseResp.getData());
        } else {
            improve.setCommentnum(0);
        }

    }
    
    /**
     * 初始化进步中用户信息
     * @param improve
     * @author:luye
     */
    private void initImproveUserInfo(Improve improve,Long userid){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(improve.getUserid()));
        //获取好友昵称
        String remark = userRelationService.selectRemark(userid, improve.getUserid());
        if(null != appUserMongoEntity){
            if(!StringUtils.isBlank(remark)){
                appUserMongoEntity.setNickname(remark);
            }
            improve.setAppUserMongoEntity(appUserMongoEntity);
        }else{
            improve.setAppUserMongoEntity(new AppUserMongoEntity());
        }
        initUserRelateInfo(userid,appUserMongoEntity);
//        improve.setAppUserMongoEntity(appUserMongoEntity);
    }
    
    /**
     * 初始化用户关系信息
     */
    private void initUserRelateInfo(Long userid,AppUserMongoEntity apuser){
        if(userid == null){
            apuser.setIsfans("0");
            apuser.setIsfriend("0");
            return ;
        }
        if(userid == apuser.getUserid()){
            return;
        }
        initFriendInfo(userid,apuser);
        initFanInfo(userid,apuser);
    }

    private void initFanInfo(long userid,AppUserMongoEntity apuser){
        SnsFans snsFans =snsFansMapper.selectByUidAndLikeid(userid,apuser.getUserid());
        if(null != snsFans){
            apuser.setIsfans("1");
        }else{
            apuser.setIsfans("0");
        }
    }

    private void initFriendInfo(Long userid,AppUserMongoEntity apuser){
        SnsFriends snsFriends =  snsFriendsMapper.selectByUidAndFid(userid,apuser.getUserid());
        if(null != snsFriends){
            if(!StringUtils.isBlank(snsFriends.getRemark())){
                apuser.setNickname(snsFriends.getRemark());
            }
            apuser.setIsfriend("1");
        }else{
            apuser.setIsfriend("0");
        }
    }
    
    /**
     * 初始化进步点赞，送花，送钻简略信息（张三，李四等5人点赞）
     * @param improve
     * @author luye
     */
    private void initLikeFlowerDiamondInfo(Improve improve){
        Long count = improveMongoDao.selectTotalCountImproveLFD(String.valueOf(improve.getImpid()));
        List<ImproveLFD> improveLFDs = improveMongoDao.selectImproveLfdList(String.valueOf(improve.getImpid()));
        improve.setLfdcount(count);
        improve.setImproveLFDs(improveLFDs);
    }

    /**
     * 是否 点赞 送花 送钻 收藏
     * @param userid
     * @param improve
     * @author luye
     */
    private void initIsOptionForImprove(String userid,Improve improve){
        //是否点赞
        boolean islike = improveMongoDao.exits(String.valueOf(improve.getImpid()),
                userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (islike) {
            improve.setHaslike("1");
        }
        //是否送花
        boolean isflower = improveMongoDao.exits(String.valueOf(improve.getImpid()),
                userid,Constant.IMPROVE_ALL_DETAIL_FLOWER);
        if (isflower) {
            improve.setHasflower("1");
        }
        //是否送钻
        boolean isdiamond = improveMongoDao.exits(String.valueOf(improve.getImpid()),
                userid,Constant.IMPROVE_ALL_DETAIL_DIAMOND);
        if (isdiamond) {
            improve.setHasdiamond("1");
        }
        //是否收藏
        UserCollect userCollect = new UserCollect();
        userCollect.setUserid(Long.parseLong(userid));
        userCollect.setCid(improve.getImpid());
        List<UserCollect> userCollects = userCollectMapper.selectListByUserCollect(userCollect);
        if (null != userCollects && userCollects.size() > 0 ){
            improve.setHascollect("1");
        }
    }

}
