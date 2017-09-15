package com.longbei.appservice.cache;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.RankService;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 榜单相关业务缓存
 *
 * @author luye
 * @create 2017-08-30 下午3:19
 **/
@Service
public class RankCache {


    private static Logger logger = LoggerFactory.getLogger(RankCache.class);

    @Autowired
    private RankMembersMapper rankMembersMapper;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private RankAwardReleaseMapper rankAwardReleaseMapper;
    @Autowired
    private RankService rankService;
    @Autowired
    private RankCardMapper rankCardMapper;
    @Autowired
    private UserMongoDao userMongoDao;


//    @Cacheable(cacheNames = RedisCacheNames._RANK_HOME_AWARD,key="'homerankawardlist'")
    public List<Map<String,Object>> selectRankWinningAwardList() throws Exception{
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        try{
            List<RankMembers> winningRankAwardList = this.rankMembersMapper.selectWinningRankAward();
            if(winningRankAwardList != null && winningRankAwardList.size() > 0){
                for(RankMembers rankMembers: winningRankAwardList){
                    Rank rank = this.rankMapper.selectRankByRankid(rankMembers.getRankid());
                    if (rank == null || !"5".equals(rank.getIsfinish())){
                        continue;
                    }
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("rankid",rankMembers.getRankid());
                    map.put("awarduserid",rankMembers.getUserid());
                    Award award = this.awardMapper.selectByPrimaryKey(Long.parseLong(rankMembers.getRankAward().getAwardid()));
                    //ranktype 榜单类型。0—公共榜 1--定制榜  2：定制私密
//                    if("0".equals(rank.getRanktype())){
                    map.put("awardnickname",award.getAwardtitle());
//                    map.put("nickname",this.userRelationService.getUserRemark(userid,rankMembers.getUserid(),true));
                    resultList.add(map);
//                    }
                }
            }
        }catch(Exception e){
            logger.error("selectRankWinningAwardList error:",e);
            throw e;
        }
        return resultList;
    }


//    @Cacheable(cacheNames = RedisCacheNames._RANK_HOME_AWARD,key = "'rankAward'.concat(#startNum).concat('-').concat(#pageSize)")
    public List<Map<String,Object>> selectRankAwardList(Integer startNum, Integer pageSize) throws Exception{
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        try{
            //获取当前结束的榜单
            Map<String,Object> parameterMap = new HashMap<String,Object>();

            List<Rank> finishRankList = this.rankMapper.selectHasAwardRankList(startNum, pageSize);

            //查看结束的榜单的获奖情况
            if(finishRankList != null && finishRankList.size() > 0){
                for(Rank rank:finishRankList){
                    Map<String,Object> resultMap = new HashMap<String,Object>();
                    resultMap.put("rankid",rank.getRankid());
                    resultMap.put("ranktitle",rank.getRanktitle());
                    resultMap.put("endtime", DateUtils.formatDate(rank.getEndtime()));
                    resultMap.put("rankinvolved",rank.getRankinvolved());//参与人数
                    resultMap.put("rankphotos",rank.getRankphotos());//榜单图片
                    resultMap.put("ispublic",rank.getIspublic());//是否公开
                    initAwardResultMap(resultMap,rank.getRankid(),null,true);
                    resultList.add(resultMap);
                }
            }
        }catch(Exception e){
            logger.error("rank award list error startNum:{} pageSize:{}",startNum,pageSize);
            throw e;
        }
        return resultList;
    }

//    @Cacheable(cacheNames = RedisCacheNames._RANK_LIST,key = "#startno + '&' + #pagesize"
//            ,condition="#status == 0")
    public Map<String,Object> selectRankListByCondition(String rankTitle,
                                                      String codeword, String pType,
                                                      String rankscope,
                                                      Integer status, String lastDate,
                                                        Integer startNo, Integer pageSize, Boolean showAward){
        Map<String,Object> resultmap = new HashedMap();
        List<Rank> ranks = new ArrayList<Rank>();
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            if(StringUtils.isNotEmpty(rankTitle)){
                map.put("ranktitle",rankTitle);
            }
            if(StringUtils.isNotEmpty(codeword)){
                map.put("codeword",codeword);
            }
            if(StringUtils.isNotEmpty(pType) && !"-1".equals(pType)){
                map.put("ptype",pType);
            }
            if(StringUtils.isNotEmpty(rankscope) && !"0".equals(rankscope) && !"-1".equals(rankscope)){
                map.put("rankscope",rankscope);
            }
            if(status == 0){//推荐的
                map.put("isrecommend","1");
                map.put("orderByType","recommend");
            }else if(status == 1){//进行中的
                map.put("isfinish","1");
                map.put("minEndDate",new Date());
                lastDate = null;
                map.put("orderByType","starttimeDesc");
            }else if(status == 2){//未开始
                map.put("isfinish","0");
                map.put("orderByType","starttimeAsc");
            }else if(status == 3){//已结束
                map.put("isfinish","2");
                map.put("orderByType","endtime");
            }
//            if(status != 0 && StringUtils.isNotEmpty(lastDate)){
//                Date tempLastDate = DateUtils.parseDate(lastDate);
//                map.put("lastDate",tempLastDate);
//            }
            map.put("startNum",startNo);
            map.put("sstatus",status);
            map.put("ispublic","0");
            map.put("isdel","0");
            map.put("pageSize",pageSize);
            int totalCount = 0;
            logger.info("selectRankListCount before map={}", JSONObject.fromObject(map).toString());
            if(StringUtils.isNotEmpty(rankTitle) && startNo == 0){
                totalCount = rankMapper.selectRankListCount(map);
            }

            if(StringUtils.isNotEmpty(rankTitle) && startNo == 0 && totalCount == 0){
                ranks = new ArrayList<Rank>();
            }else{
                ranks =rankMapper.selectRankList(map);
            }
            logger.info("selectRankListCountis={},selectRankList.size={}",totalCount,ranks.size());
            if(ranks != null && ranks.size() > 0){
                for(Rank rank1:ranks){
                    if(showAward != null && showAward){
                        rankService.initRankAward(rank1);
                    }
                    if (Constant.RANK_TYEP_APP.equals(rank1.getRanktype())){
//                        rank1.setAppUserMongoEntity(this.userMongoDao.getAppUser(rank1.getCreateuserid()+""));
                    }
                    //初始化榜主名片
                    if(StringUtils.isNotEmpty(rankTitle)){
                        if(rank1.getRankcardid() != null){
                            RankCard rankCard = this.rankCardMapper.selectByPrimaryKey(Integer.parseInt(rank1.getRankcardid()));
                            rank1.setRankCard(rankCard);
                        }else{
                            if (Constant.RANK_SOURCE_TYPE_1.equals(rank1.getSourcetype())){
                                //web发榜
                                RankCard rankCard = new RankCard();
                                AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(rank1.getCreateuserid()+"");
                                rankCard.setAdminname(appUserMongoEntity.getNickname());
                                rankCard.setAdminpic(appUserMongoEntity.getAvatar());
                                rank1.setRankCard(rankCard);
                            }
                        }

                    }

                    if(rank1.getRankinvolved() >= rank1.getRanklimite()){
                        //获取可以挤掉的用户数量
                        int removeCount = rankService.getSureRemoveRankMemberCount(rank1.getRankid());
                        if(removeCount > 0){
                            rank1.setRankinvolved(rank1.getRankinvolved()-removeCount);
                        }
                    }
                    rank1.setJoincode(null);
                }
            }
            resultmap.put("totalcount",totalCount);
            resultmap.put("ranklist",ranks);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }

        return resultmap;

    }


    private void initAwardResultMap(Map<String,Object> resultMap,Long rankid,Long userId,boolean searchUserNickName){
        List<RankAwardRelease> rankAwardList = this.rankMembersMapper.selectAwardMemberList(rankid);

        if(rankAwardList != null && rankAwardList.size() > 0){
            List<Map<String,Object>> awardList = new ArrayList<Map<String,Object>>();
            int rankAwardCount = 0;//整个榜单的获奖总数
            for(RankAwardRelease rankAwardRelease:rankAwardList){
                if(StringUtils.isBlank(rankAwardRelease.getAwardid())){
                    continue;
                }

                Map<String,Object> awardMap = new HashMap<String,Object>();
                Award award = awardMapper.selectByPrimaryKey(Long.parseLong(rankAwardRelease.getAwardid()));
                if(null != award){
                    awardMap.put("awardtitle",award.getAwardtitle());
                    awardMap.put("awardlevel",award.getAwardlevel());
                    awardMap.put("awardphotos",award.getAwardphotos());
                    awardMap.put("awardprice",award.getAwardprice());
                }
                RankAwardRelease tempRankAwardRelease = this.rankAwardReleaseMapper.selectByRankIdAndAwardId(rankid+"",rankAwardRelease.getAwardid());
                awardMap.put("nickname",tempRankAwardRelease.getAwardnickname());
                awardMap.put("awarduserid",rankAwardRelease.getUserid());
                awardMap.put("awardcount",rankAwardRelease.getAwardcount());
                awardList.add(awardMap);
                rankAwardCount += rankAwardRelease.getAwardcount();
            }
            resultMap.put("rankawardcount",rankAwardCount);
            resultMap.put("rankawardList",awardList);

        }
    }


}
