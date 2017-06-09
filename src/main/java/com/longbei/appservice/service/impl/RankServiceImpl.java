package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.NumberUtil;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.CodeDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 榜单操作接口实现类
 *
 * @author luye
 * @create 2017-01-20 下午3:29
 **/
@Service("rankService")
public class RankServiceImpl extends BaseServiceImpl implements RankService{

    private static Logger logger = LoggerFactory.getLogger(RankServiceImpl.class);

    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private RankImageMapper rankImageMapper;
    @Autowired
    private RankCheckDetailMapper rankCheckDetailMapper;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private RankAwardMapper rankAwardMapper;
    @Autowired
    private AwardMapper awardMapper;
    @Autowired
    private RankAwardReleaseMapper rankAwardReleaseMapper;
    @Autowired
    private RankMembersMapper rankMembersMapper;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserIdcardService userIdcardService;
    @Autowired
    private ImproveRankMapper improveRankMapper;
    @Autowired
    private RankSortService rankSortService;
    @Autowired
    private UserImpCoinDetailService userImpCoinDetailService;
    @Autowired
    private CodeDao codeDao;
    @Autowired
    private UserBusinessConcernMapper userBusinessConcernMapper;
    @Autowired
    private SnsFansMapper snsFansMapper;
    @Autowired
    private RankAcceptAwardService rankAcceptAwardService;
    @Autowired
    private UserBehaviourService userBehaviourService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private RankAcceptAwardMapper rankAcceptAwardMapper;
    @Autowired
    private UserMsgService userMsgService;
    @Autowired
    private SnsFriendsMapper snsFriendsMapper;
    @Autowired
    private DictAreaMapper dictAreaMapper;
    @Autowired
    private CommentMongoService commonMongoService;
    @Autowired
    private HomeRecommendMapper homeRecommendMapper;
    @Autowired
    private RankCardMapper rankCardMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AwardService awardService;
    @Autowired
    private CommentMongoService commentMongoService;
    @Autowired
    private ImproveService improveService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JPushService jPushService;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp insertRank(RankImage rankImage) {
        BaseResp baseResp = new BaseResp();
        rankImage.setRankid(idGenerateService.getUniqueIdAsLong());
        rankImage.setCreatetime(new Date());

        int res = 0;
        try {
            res = rankImageMapper.insertSelective(rankImage);
            if(res>0){
                baseResp.initCodeAndDesp();
                baseResp.setData(rankImage.getRankid());
                if (Constant.RANK_SOURCE_TYPE_1.equals(rankImage.getSourcetype().trim())){
                    insertPCRankAward(String.valueOf(rankImage.getRankid()),rankImage.getRankAwards());
                }else {
                    insertRankAward(String.valueOf(rankImage.getRankid()),rankImage.getRankAwards());
                }
            }
        } catch (Exception e) {
            logger.error("insert rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return baseResp;
    }

    @Override
    public boolean updateRankImageSymbol(RankImage rankImage) {
        int res = 0;
        try {
            res = rankImageMapper.updateSymbolByRankId(rankImage);
            if (res > 0) {
                if (Constant.RANK_ISAUTO_TIME.equals(rankImage.getIsauto())){
                    //定时任务
                }
            }
        } catch (Exception e) {
            logger.error("update rank image symbol is error:",e);
        }
        return res>0;
    }

    @Override
    public boolean updateRankSymbol(Rank rank) {
        int res = 0;
        try {
            res = rankMapper.updateSymbolByRankId(rank);
            if("1".equals(rank.getIsdel())){
            	rank = rankMapper.selectRankByRankid(rank.getRankid());
            	if(null != rank){
            		//sourcetype   来源类型。0 运营端创建   1  app 2   商户
            		if(Constant.RANK_SOURCE_TYPE_1.equals(rank.getSourcetype())){
            			userMsgService.insertMsg(Constant.SQUARE_USER_ID, rank.getCreateuserid(),
            	        		"", "10", rank.getRankid().toString(), "发布榜单审核未通过", "2", "49", "榜关闭", 0, "", "");
            		}
            	}
            }
            if("5".equals(rank.getIsfinish())){
//            	rank = rankMapper.selectRankByRankid(rank.getRankid());
            	//发送获奖消息
	         	try {
	               	sendRankEndUserMsg(rank);
	           	} catch (Exception e) {
	             	logger.error("send rank rankid={}",rank.getRankid(),e);
	           	}
            }
        } catch (Exception e) {
            logger.error("updateRankSymbol rank = {}", JSON.toJSON(rank).toString(), e);
        }
        return res>0;
    }

    private boolean insertRankAward(String rankid, List<RankAward> rankAwards){
        if (null != rankAwards){
            Date date = new Date();
            for (RankAward rankAward:rankAwards){
                rankAward.setRankid(rankid);
                rankAward.setCreatetime(date);
            }
            try {
                int res = rankAwardMapper.insertBatch(rankAwards);
                return true;
            } catch (Exception e) {
                logger.error("insert rank award rankid={} is error:",rankid,e);
            }
        }
        return false;
    }

    /**
     * pc端发榜insertPCRankAward
     * @param rankid
     * @param rankAwards
     * @return
     */
    private boolean insertPCRankAward(String rankid, List<RankAward> rankAwards){
        if (null != rankAwards){
            Date date = new Date();
            for (RankAward rankAward:rankAwards){
                Award award = rankAward.getAward();
                try{
                    award.setUpdatetime(date);
                    award.setCreatetime(date);
                    awardService.insertAward(award);
                    rankAward.setAwardid(award.getId().toString());
                    rankAward.setRankid(rankid);
                    rankAward.setCreatetime(date);
                }catch (Exception e){
                    logger.error("insertPCRankAward={} and msg={}",
                            JSONObject.fromObject(award).toString(),e);
                }
            }
            try {
                int res = rankAwardMapper.insertBatch(rankAwards);
                return true;
            } catch (Exception e) {
                logger.error("insert rank award rankid={} is error:",rankid,e);
            }
        }
        return false;
    }

    /**
     *  @author luye
     *  @desp
     *  @create 2017/1/23 下午6:12
     *  @update 2017/1/23 下午6:12
     */
    @Override
    public boolean updateRankImage(RankImage rankImage) {
        int res = 0;
        try {
            res = rankImageMapper.updateByPrimaryKeySelective(rankImage);
            rankAwardMapper.deleteByRankid(String.valueOf(rankImage.getRankid()));
            if (Constant.RANK_SOURCE_TYPE_1.equals(rankImage.getSourcetype())){
                insertPCRankAward(String.valueOf(rankImage.getRankid()),rankImage.getRankAwards());
            }else {
                insertRankAward(String.valueOf(rankImage.getRankid()),rankImage.getRankAwards());
            }
        } catch (Exception e) {
            logger.error("update rank:{} is error:{}", JSONObject.fromObject(rankImage),e);
        }
        return res != 0;
    }

    @Override
    public BaseResp<RankImage> selectRankImage(String rankimageid) {
        BaseResp<RankImage> baseResp = new BaseResp<>();
        try {
            RankImage rankImage = rankImageMapper.selectByRankImageId(rankimageid);
            if (null == rankImage){
                return baseResp;
            }
            rankImage.setRankAwards(selectRankAwardByRankid(rankimageid));
            baseResp = BaseResp.ok();
            baseResp.setData(rankImage);
            return baseResp;
        } catch (Exception e) {
            logger.error("select rank image by rankimageid={} is error:{}",rankimageid,e);
        }
        return BaseResp.fail();
    }

    private List<RankAward> selectRankAwardByRankid(String rankiamgeid){
        List<RankAward> rankAwards = rankAwardMapper.selectListByRankid(rankiamgeid);
        for (RankAward rankAward : rankAwards){
            if (!StringUtils.isEmpty(rankAward.getAwardid())){
                Award award = awardMapper.selectByPrimaryKey(Long.parseLong(rankAward.getAwardid()));
                rankAward.setAward(award);
            }
        }
        return rankAwards;
    }

    private BaseResp publishRankImage(RankImage rankImage){
        logger.info("publish rank image : {}", com.alibaba.fastjson.JSON.toJSONString(rankImage));
        BaseResp baseResp = new BaseResp();
        String rankImageId = rankImage.getRankid()+"";
        rankImage.setRankAwards(selectRankAwardByRankid(rankImageId));
        if (!Constant.RANKIMAGE_STATUS_4.equals(rankImage.getCheckstatus())){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_60, Constant.RTNINFO_SYS_60);
        }
        Rank rank = new Rank();
        try {
            BeanUtils.copyProperties(rank,rankImage);
        } catch (Exception e) {
            logger.error("copy rankimage to rank is error:{}",e);
        }
        try {
            rank.setAutotime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
            rank.setIsup("1");
            rank.setIsfinish(null);
            Rank rank1 = rankMapper.selectRankByRankid(rankImage.getRankid());
            int res = 0;
            boolean flag = updateRankAwardRelease(rankImageId);
            if (flag){
                if (null != rank1){
                    res = rankMapper.updateByPrimaryKeySelective(rank);
                } else {
                    // 定制榜，添加参榜口令joinCode
                    if (!"0".equals(rankImage.getRanktype())){
                        rank.setJoincode(codeDao.getCode(null));
                    }
                    Date starttime = rank.getStarttime();
                    if (new Date().getTime() >= starttime.getTime()){
                        rank.setIsfinish("1");
                    }
                    logger.warn("rank image info : {}", com.alibaba.fastjson.JSON.toJSONString(rankImage));
                    logger.warn("rank info : {}", com.alibaba.fastjson.JSON.toJSONString(rank));
                    res = rankMapper.insertSelective(rank);
                }
                if (res > 0){
                    RankImage rm = new RankImage();
                    rm.setRankid(Long.valueOf(rankImageId));
                    rm.setIsup(Constant.RANK_ISUP_YES);
                    rankImageMapper.updateSymbolByRankId(rm);
                    return BaseResp.ok();
                }
            }

        } catch (Exception e) {
            logger.error("publish rank rankid={} is error:",rankImageId,e);
        }
        return BaseResp.fail();
    }

    @Override
    public BaseResp publishRankImage(String rankImageid) {
        RankImage rankImage = rankImageMapper.selectByRankImageId(rankImageid);
        return publishRankImage(rankImage);
    }

    @Override
    public BaseResp<String> selectOwnRankIdsList(String userid) {
        BaseResp<String> baseResp = new BaseResp<>();
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        parameterMap.put("userId",userid);
        List<RankMembers> rankMembers = this.rankMembersMapper.selectRankMembers(parameterMap);
        List<String> list = new ArrayList<>();
        if(rankMembers != null && rankMembers.size() > 0){
            for(RankMembers rankMembers1:rankMembers){
                list.add(String.valueOf(rankMembers1.getRankid()));
            }
        }
        String ids = StringUtils.join(list.toArray(),",");
        baseResp.initCodeAndDesp();
        baseResp.setData(null);
        baseResp.getExpandData().put("ids",ids);
        return baseResp;
    }

    private boolean deleteRankAwardRelease(String rankid){
        boolean flag = true;
        try {
            rankAwardReleaseMapper.deleteByRankid(rankid);
        } catch (Exception e) {
            flag = false;
            logger.error("delete rankaward release is error:",e);
        }
        return flag;
    }

    private boolean updateRankAwardRelease(String rankimageid){
        logger.info("update rank award release by rankid={}",rankimageid);
        List<RankAward> awards = null;
        try {
            awards = rankAwardMapper.selectListByRankid(rankimageid);
        } catch (Exception e) {
            logger.error("select rankaward by rankid id={} is error:",rankimageid,e);
            return false;
        }
        boolean flag = deleteRankAwardRelease(rankimageid);
        if (flag){
            List<RankAwardRelease> rankAwardReleases = new ArrayList<>();
            for (RankAward rankAward : awards){
                RankAwardRelease rankAwardRelease = new RankAwardRelease();
                try {
                    BeanUtils.copyProperties(rankAwardRelease,rankAward);

                } catch (Exception e) {
                    logger.warn("copy rankaward to rankawardrelease is error:",e);
                    return false;
                }
                try {
                    rankAwardReleaseMapper.insertRankAwardRe(rankAwardRelease);
                } catch (Exception e) {
                    logger.error("insert batch rankawardrelease is error:",e);
                }
            }
            return true;

        }
        return false;
    }

    @Override
    public Page<RankImage> selectRankImageList(RankImage rankImage,int pageno, int pagesize) {
        Page<RankImage> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = rankImageMapper.selectListCount(rankImage);
            pageno = Page.setPageNo(pageno,totalcount,pagesize);
            List<RankImage> rankImages = rankImageMapper.selectListWithPage(rankImage,(pageno-1)*pagesize,pagesize);
            for(RankImage rankImage1: rankImages){
                //pc端发榜,榜单审核意见列表
                if(Constant.RANK_SOURCE_TYPE_1.equals(rankImage1.getSourcetype())){
                    List<RankCheckDetail> list = rankCheckDetailMapper.selectList(String.valueOf(rankImage1.getRankid()));
                    rankImage1.setRankCheckDetails(list);
                }
            }
            page.setTotalCount(totalcount);
            page.setList(rankImages);
        } catch (Exception e) {
            logger.error("select rank image list is error:",e);
        }
        return page;
    }

    @Override
    public Page<Rank> selectRankList(Rank rank, int pageno, int pagesize,Boolean showAward) {
        Page<Rank> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = rankMapper.selectListCount(rank);
            pageno = Page.setPageNo(pageno,totalcount,pagesize);
            List<Rank> ranks = selectRankListByRank(rank,pageno,pagesize,showAward);
            page.setTotalCount(totalcount);
            page.setList(ranks);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return page;
    }

    /**
     * 获取榜单列表 （带人数、评论数排序）
     * @param rank
     * @param pageno
     * @param pagesize
     * @return
     * @author IngaWu
     */
    @Override
    public Page<Rank> selectRankList2(Rank rank, int pageno, int pagesize,String orderByInvolved) {
        Page<Rank> page = new Page<>(pageno,pagesize);
        try {
            rank.setIsdel("0");
            int totalcount = rankMapper.selectListCount(rank);
            pageno = Page.setPageNo(pageno,totalcount,pagesize);
            List<Rank> ranks = rankMapper.selectListWithPage2(rank,(pageno-1)*pagesize,pagesize,orderByInvolved);
            for (Rank rank1 : ranks){
                BaseResp<Integer> baseResp = commentMongoService.selectCommentCountSum(String.valueOf(rank1.getRankid()),"10",null);
                if (ResultUtil.isSuccess(baseResp)){
                    rank1.setCommentCount(baseResp.getData());
                }
                String icount = rankMembersMapper.getRankImproveCount
                        (String.valueOf(rank1.getRankid()))==null?"0":rankMembersMapper.getRankImproveCount(String.valueOf(rank1.getRankid()));
                rank1.setIcount(Integer.parseInt(icount));
                //pc端发榜：createUserId转nickname显示
                if(Constant.RANK_SOURCE_TYPE_1.equals(rank1.getSourcetype())){
                    AppUserMongoEntity appUser = userMongoDao.getAppUser(String.valueOf(rank1.getCreateuserid()));
                    rank1.setCreateuserid(appUser.getNickname());
                }
            }
            page.setTotalCount(totalcount);
            page.setList(ranks);
        } catch (Exception e) {
            logger.error("select rank list2 for adminservice is error:",e);
        }
        return page;
    }

    private List<Rank> selectRankListByRank(Rank rank, int pageno, int pagesize, Boolean showAward){
        try{
            List<Rank> ranks = rankMapper.selectListWithPage(rank,(pageno-1)*pagesize,pagesize);
            if(showAward != null && showAward && ranks != null && ranks.size() > 0){
                for(Rank rank1:ranks){
                    List<RankAwardRelease> awardList = this.rankAwardReleaseMapper.findRankAward(rank.getRankid());
                    if(awardList != null && awardList.size() > 0){
                        rank.setRankAwards(awardList);
                    }
                    BaseResp<Integer> baseResp = commentMongoService.selectCommentCountSum(String.valueOf(rank1.getRankid()),"2",null);
                    if (ResultUtil.isSuccess(baseResp)){
                        rank1.setCommentCount(baseResp.getData());
                    }
                    rank1.setIcount(Integer.parseInt(rankMembersMapper.getRankImproveCount(String.valueOf(rank1.getRankid()))));
                }
            }
            return ranks;
        }catch (Exception e){
            logger.error("selectListWithPage",e);
        }
        return null;
    }

    @Override
    public BaseResp<List<Rank>> selectRankListForApp(long userid,Integer startNum,Integer pageSize) {
        BaseResp<List<Rank>> baseResp = new BaseResp<>();
        try{
//            List<Rank> rankList = new ArrayList<Rank>();
//            HomeRecommend homeRecommend = new HomeRecommend();
//            homeRecommend.setIsdel("0");
//            homeRecommend.setRecommendtype(0);
//            List<HomeRecommend> homeRecommendList = homeRecommendMapper.selectList(homeRecommend,startNum,pageSize);

            Map<String,Object> map = new HashMap<String,Object>();
            map.put("isrecommend",1);
            map.put("isdel",0);
            map.put("orderByType","recommend");
            map.put("startNum",startNum);
            map.put("pageSize",pageSize);
            List<Rank> homeRecommendRankList = this.rankMapper.selectRankList(map);
            if(homeRecommendRankList != null && homeRecommendRankList.size() > 0){
                for(Rank rank: homeRecommendRankList){
                    if(!Constant.VISITOR_UID.equals(String.valueOf(userid))){
                        RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rank.getRankid(),userid);
                        if(null != rankMembers){
                            rank.setHasjoin("1");
                        }
                    }
                    List<RankAwardRelease> awardList = this.rankAwardReleaseMapper.findRankAward(rank.getRankid());
                    if(awardList != null && awardList.size() > 0){
                        rank.setRankAwards(awardList);
                    }
                }
            }
            baseResp.setData(homeRecommendRankList);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select rank list for app error startNum:{} pageSize:{}",startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 初始化榜单的奖品列表
     * @param rank
     * @return
     */
    public Rank initRankAward(Rank rank){
        try{
            List<RankAwardRelease> awardList = this.rankAwardReleaseMapper.findRankAward(rank.getRankid());
            if(awardList != null && awardList.size() > 0){
                if (!StringUtils.isBlank(awardList.get(0).getAwardid())){
                    awardList.get(0).setAward(awardMapper.selectByPrimaryKey(Long.parseLong(awardList.get(0).getAwardid())));
                }
                rank.setRankAwards(awardList);
            }
        }catch(Exception e){
            logger.error("setAward error msg:{}",e);
        }
        return rank;
    }

    @Override
    public BaseResp<Object> selectRankListByCondition(Long userid,String rankTitle,String codeword, String pType, String rankscope,
                                                      Integer status, String lastDate,Integer startNo, Integer pageSize,Boolean showAward) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
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
                map.put("orderByType","starttimeDesc");
            }else if(status == 2){//未开始
                map.put("isfinish","0");
                map.put("orderByType","starttimeAsc");
            }else if(status == 3){//已结束
                map.put("isfinish","2");
                map.put("orderByType","endtime");
            }
            if(status != 0 && StringUtils.isNotEmpty(lastDate)){
                Date tempLastDate = DateUtils.parseDate(lastDate);
                map.put("lastDate",tempLastDate);
            }
            map.put("startNum",startNo);
            map.put("sstatus",status);
            map.put("ispublic","0");
            map.put("isdel","0");
            map.put("pageSize",pageSize);
            int totalCount = 0;
            if(StringUtils.isNotEmpty(rankTitle) && startNo == 0){
                 totalCount = rankMapper.selectRankListCount(map);
            }
            List<Rank> ranks = new ArrayList<Rank>();
            if(StringUtils.isNotEmpty(rankTitle) && startNo == 0 && totalCount == 0){
                ranks = new ArrayList<Rank>();
            }else{
                ranks =rankMapper.selectRankList(map);
            }
            if(ranks != null && ranks.size() > 0){
                for(Rank rank1:ranks){
                    rank1.setHasjoin("0");
                    if(userid != null && !Constant.VISITOR_UID.equals(String.valueOf(userid))){
                        RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rank1.getRankid(),userid);
                        if(null != rankMembers && rankMembers.getStatus() == 1){
                            rank1.setHasjoin("1");
                        }
                    }
                    if(showAward != null && showAward){
                        initRankAward(rank1);
                    }
                    if (Constant.RANK_TYEP_APP.equals(rank1.getRanktype())){
                        rank1.setAppUserMongoEntity(this.userMongoDao.getAppUser(rank1.getCreateuserid()+""));
                    }
                    //初始化榜主名片
                    if(StringUtils.isNotEmpty(rankTitle) && rank1.getRankcardid() != null){
                        RankCard rankCard = this.rankCardMapper.selectByPrimaryKey(Integer.parseInt(rank1.getRankcardid()));
                        rank1.setRankCard(rankCard);
                    }

                    if(rank1.getRankinvolved() >= rank1.getRanklimite()){
                        //获取可以挤掉的用户数量
                        int removeCount = getSureRemoveRankMemberCount(rank1.getRankid());
                        if(removeCount > 0){
                            rank1.setRankinvolved(rank1.getRankinvolved()-removeCount);
                        }
                    }
                }
            }
            baseResp.setData(ranks);
            baseResp.getExpandData().put("totalCount",totalCount);
            //如果是按照口令搜索,则返回用户是否可以入榜
            if(StringUtils.isNotEmpty(codeword) && ranks != null && ranks.size() > 0){
                if(userid != null && !Constant.VISITOR_UID.equals(String.valueOf(userid))){
                    int userRankMemberStatus = getInsertUserRankMemberStatus(userid,ranks.get(0));
                    baseResp.getExpandData().put("userRankMemberStatus",userRankMemberStatus);
                }else{
                    baseResp.getExpandData().put("userRankMemberStatus",0);
                }
            }
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("select rank list for adminservice is error:",e);
        }
        return baseResp;
    }

    /**
     * 查询和自己相关的榜单
     * @param searchType 1.我参与的 2.我关注的 3.我创建的
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> selectownRank(Long userId,Integer searchType,
                                          Integer startNum, Integer pageSize,String opttype) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            if(searchType == 1){//我参与的
                Map<String,Object> parameterMap = new HashMap<String,Object>();
                parameterMap.put("userId",userId);
                parameterMap.put("status","1");
                parameterMap.put("orderType","updateTimeDesc");
                parameterMap.put("startNum",startNum);
                parameterMap.put("pageSize",pageSize);
                List<RankMembers> rankMembers = this.rankMembersMapper.selectRankMembers(parameterMap);
                List<Rank> marching = new ArrayList<Rank>();
                List<Rank> finish = new ArrayList<Rank>();
                List<Rank> nostart = new ArrayList<Rank>();
                if(rankMembers != null && rankMembers.size() > 0){
                    for(RankMembers rankMembers1:rankMembers){
                        Rank rank = this.rankMapper.selectRankByRankid(rankMembers1.getRankid());
                        if(rank == null){
                            continue;
                        }
                        rank.setHasjoin("1");
                        initRankAward(rank);
                        if("0".equals(rank.getIsfinish())){//未开始
                            nostart.add(rank);
                        }else if("1".equals(rank.getIsfinish())){//进行中
                            marching.add(rank);
                        }else{
                            if (StringUtils.isBlank(opttype)){
                                finish.add(rank);
                            }
                        }
                    }
                }
                List<Rank> resultList = new LinkedList<Rank>();
                resultList.addAll(marching);
                resultList.addAll(nostart);
                resultList.addAll(finish);
                baseResp.setData(resultList);
            }else if(searchType == 2){//我关注的
                List<Rank> rankList = new ArrayList<Rank>();
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("userId",userId);
                map.put("businessType","2");
                map.put("orderType","idDesc");
                map.put("startNum",startNum);
                map.put("pageSize",pageSize);
                List<UserBusinessConcern> concernList = this.userBusinessConcernMapper.findUserBusinessConcernList(map);
                if(concernList != null && concernList.size() > 0){
                    for(UserBusinessConcern userBusinessConcern:concernList){
                        Rank rank = this.rankMapper.selectRankByRankid(userBusinessConcern.getBusinessid());
                        if(rank != null){
                            RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rank.getRankid(),userId);
                            if(null != rankMembers){
                                rank.setHasjoin("1");
                            }
                            initRankAward(rank);
                            rankList.add(rank);
                        }
                    }
                }
                baseResp.setData(rankList);
            }else if(searchType == 3){//我创建的
                Map<String,Object> parameterMap = new HashMap<String,Object>();
                parameterMap.put("createuserid",userId);
                parameterMap.put("status","1");
                parameterMap.put("isdel","0");
                parameterMap.put("startNum",startNum);
                parameterMap.put("pageSize",pageSize);
                List<Rank> createList = this.rankMapper.selectRankList(parameterMap);
                for(Rank rank1:createList){
                    RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rank1.getRankid(),userId);
                    if(null != rankMembers){
                        rank1.setHasjoin("1");
                    }
                }
                baseResp.setData(createList);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select own rank list error searchType:{} startNum:{} pageSize:{}",searchType,startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询榜单列表
     * @param rankId
     * @return
     */
    @Override
    public BaseResp<List<RankAwardRelease>> selectRankAward(Long rankId) {
        BaseResp<List<RankAwardRelease>> baseResp = new BaseResp<List<RankAwardRelease>>();
        try{
            List<RankAwardRelease> rankAwardList = selectRankAwardByRankidRelease(rankId+"");
            baseResp.setData(rankAwardList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select rank award error rankId:{}",rankId);
            printException(e);
        }
        return baseResp;
    }

    @Override
    public boolean deleteRankImage(String rankimageid) {
        int res = 0;
        try {
            res = rankImageMapper.deleteByRankImageId(rankimageid);
        } catch (Exception e) {
            logger.error("delete rank image by rankimageid={} is error:{}",rankimageid,e);
        }
        return res > 0;
    }

    @Override
    public boolean updateSponsornumAndSponsormoney(long rankid ) {
        int res = 0;
        try {
            res = rankMapper.updateSponsornumAndSponsormoney(rankid);
        } catch (Exception e) {
            logger.error("updateSponsornumAndSponsormoney error:{}", e);
        }
        return res > 0;
    }

    @Override
    public BaseResp checkRankImage(RankCheckDetail rankCheckDetail) {
        BaseResp baseResp = new BaseResp();
        RankImage rankImage = new RankImage();
        rankImage.setRankid(rankCheckDetail.getRankid());
        rankImage.setCheckstatus(rankCheckDetail.getCheckstatus());
        rankCheckDetail.setCreatetime(new Date());
        boolean flag = updateRankImageSymbol(rankImage);
        if (flag) {
            int res = 0;
            try {
                res = rankCheckDetailMapper.insertSelective(rankCheckDetail);
            } catch (Exception e) {
                logger.error("insert rank check detail is error:{}",e);
            }
            if (res > 0){
                optionAfterCheck(rankCheckDetail);
                return BaseResp.ok();
            }
        }
        return baseResp;
    }

    /**
     * 判断是否发布榜单
     */
    private void optionAfterCheck(RankCheckDetail rankCheckDetail){
        //审核通过
        BaseResp<RankImage> baseResp = selectRankImage(String.valueOf(rankCheckDetail.getRankid()));
        RankImage rankImage = baseResp.getData();
        if (Constant.RANKIMAGE_STATUS_4.equals(rankCheckDetail.getCheckstatus())){
            if(ResultUtil.isSuccess(baseResp)){
                if (Constant.RANK_ISAUTO_YES.equals(rankImage.getIsauto())){
                    publishRankImage(String.valueOf(rankCheckDetail.getRankid()));
                }
            }
        }else{
            //pc榜单审核不通过,返还龙币
            if (Constant.RANK_SOURCE_TYPE_1.equals(rankImage.getSourcetype())){
                //发榜扣除龙币
                Double tempPrice = 0.0;
                List<RankAward> awardList = rankImage.getRankAwards();
                for(RankAward rankAward:awardList){
                    int coins = (int)(rankAward.getAward().getAwardprice()*100);//奖励进步币数
                    Double nums = rankAward.getAwardrate();//奖励人数
                    tempPrice += coins * nums;
                }
                int totalPrice = (int)(Math.ceil(tempPrice/10));//进步币折换成龙币:除10向上取整
                //用户剩余龙币
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(rankImage.getCreateuserid()));
                int totalMoney = userInfo.getTotalmoney();
                //返还后龙币数
                int remainMoney = totalMoney + totalPrice;
                userService.updateTotalmoneyByUserid(Long.parseLong(rankImage.getCreateuserid()),remainMoney,0);
                //添加龙币返还记录
                userMoneyDetailService.insertPublic(Long.parseLong(rankImage.getCreateuserid()), "7", totalPrice, 0);
            }
        }

        //审核不通过且不可以修改 运营端直接删除;pc端不删除
        if (Constant.RANKIMAGE_STATUS_3.equals(rankCheckDetail.getCheckstatus())){
            if (!Constant.RANK_SOURCE_TYPE_1.equals(rankImage.getSourcetype())){
                deleteRankImage(String.valueOf(rankCheckDetail.getRankid()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public BaseResp<Object> selectRankByRankid(long rankid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            Rank rank = rankMapper.selectRankByRankid(rankid);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            baseResp.setData(rank);
        } catch (Exception e) {
            logger.error("selectRankByRankid error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public Rank selectByRankid(long rankid) {
        try {
            Rank rank = rankMapper.selectRankByRankid(rankid);
            return rank;
        } catch (Exception e) {
            logger.error("selectRankByRankid error and msg={}",e);
        }
        return null;
    }

    /**
     * 用户加入榜单
     * @param userId 用户id
     * @param rankId 榜单id
     * @param codeword 口令
     * @return
     */
    @Transactional
    @Override
    public BaseResp<Object> insertRankMember(Long userId, Long rankId, String codeword) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if (rank == null) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
            }
            if("0".equals(rank.getIsfinish())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_68,Constant.RTNINFO_SYS_68);
            }
            if (!"1".equals(rank.getIsfinish()) || "1".equals(rank.getIsdel())) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_63, Constant.RTNINFO_SYS_63);
            }
            if (!DateUtils.compare(rank.getEndtime(), new Date())) {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_69,Constant.RTNINFO_SYS_69);
            }
            if(StringUtils.isNotEmpty(rank.getJoinlastday()) && !"0".equals(rank.getJoinlastday())){
                int day = Integer.parseInt(rank.getJoinlastday());
                Date lastDate = DateUtils.getBeforeDay(rank.getEndtime(),day);
                if(DateUtils.compare(new Date(),lastDate)){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_62,Constant.RTNINFO_SYS_62);
                }
            }
            //查看口令是否正确
            if(("1".equals(rank.getRanktype()) || "1".equals(rank.getIspublic())) && (StringUtils.isEmpty(codeword) || !codeword.equals(rank.getJoincode()))){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_61,Constant.RTNINFO_SYS_61);
            }
            if("1".equals(rank.getIsrealname())){
                UserIdcard userIdCard = this.userIdcardService.selectByUserid(userId+"");
                if(!"2".equals(userIdCard.getValidateidcard())){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_96,Constant.RTNINFO_SYS_96);
                }
            }
            UserInfo userInfo = this.userInfoMapper.selectByUserid(userId);
            if("1".equals(rank.getIslonglevel())){
                if(rank.getLonglevel() != null && (userInfo.getGrade() < Integer.parseInt(rank.getLonglevel()))){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_97,Constant.RTNINFO_SYS_97);
                }
            }
            if(rank.getRankinvolved() >= rank.getRanklimite()){
                //查询是否有可以挤走的榜成员
                int count = getSureRemoveRankMemberCount(rankId);
                if(count < 1){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_610,Constant.RTNINFO_SYS_610);
                }
                //如果人数已满,且有可挤走的人,而且rankMember入榜不需要进行榜主审核则将N小时未发榜的人移除
                if(count > 0 && rank.getNeedConfirm() != null && !rank.getNeedConfirm()){
                    boolean removeFlag = removeOverTimeRankMember(rankId);
                    if(!removeFlag){
                        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_610,Constant.RTNINFO_SYS_610);
                    }
                }
            }
//            BaseResp<Object> baseResp1 = userBehaviourService.hasPrivilege(userInfo,Constant.PrivilegeType.joinranknum,null);
//            if(!ResultUtil.isSuccess(baseResp1)){
//                return baseResp1;
//            }
            //校验用户是否已经在榜单中
            RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rankId, userId);
            if(rankMembers != null && rankMembers.getStatus() == 0 ){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_93,Constant.RTNINFO_SYS_93);
            }else if(rankMembers != null && rankMembers.getStatus() == 1){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_98,Constant.RTNINFO_SYS_98);
            }else if(rankMembers != null && rankMembers.getStatus() == 3){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_99,Constant.RTNINFO_SYS_99);
            }else if(rankMembers != null && rankMembers.getStatus() == 2){
                //修改数据
                //1.修改用户的状态为入榜
                int status = 0;
                if(rank.getNeedConfirm() != null && !rank.getNeedConfirm()){
                    status = 1;
                }
                boolean initRankMember = true;
                Map<String,Object> map = new HashMap<>();
                map.put("userId",userId);
                map.put("rankId",rankId);
                map.put("status",status);
                map.put("initRankMember",initRankMember);
                int updateRankMemberRow = this.rankMembersMapper.updateRank(map);

                //2.如果直接入榜 不需要审核的话,则修改入榜人数
                if(status == 1 && updateRankMemberRow > 0){
                    boolean updateRankMemberCount = updateRankMemberCount(rankId,1);

                }


                if (Constant.RANK_TYEP_APP.equals(rank.getRanktype())){
                    //TODO 发送消息给榜主 直接参榜以及需要验证是否都需要发消息
                    String remark = "有新用户申请加入您创建的龙榜\""+rank.getRanktitle()+"\",赶快去处理吧!";
                    //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
					//10：榜中  11 圈子中  12 教室中  13:教室批复作业
                    boolean sendMsgFlag = sendUserMsg(true,Long.parseLong(rank.getCreateuserid()),userId,"17",rank.getRankid(),remark,"10");

                }

                //初始化redis的排名
                boolean redisInitFlag = initRedisRankSort(rank,userId);

                Map<String,Object> expandData = new HashMap<String,Object>();
                expandData.put("status",status);
                baseResp.setData(expandData);

                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,status==1?"入榜成功":"入榜申请已提交,正在等待榜主审核");
            }


            RankMembers rankMember = new RankMembers();
            rankMember.setCreatetime(new Date());
            rankMember.setFlowers(0);
            rankMember.setIcount(0);
            rankMember.setUserid(userId);
            rankMember.setRankid(rankId);
            rankMember.setUpdatetime(new Date());
            rankMember.setLikes(0);
            rankMember.setIsfashionman("0");
            if(rank.getNeedConfirm() != null && rank.getNeedConfirm()){
                rankMember.setStatus(0);
            }else{
                rankMember.setStatus(1);
            }
            int row = rankMembersMapper.insertSelective(rankMember);
            if(row > 0 && Constant.RANK_TYEP_APP.equals(rank.getRanktype())){
                // 发送消息给榜主
                String remark = "有新用户申请加入您创建的龙榜\"" + rank.getRanktitle() + "\",赶快去处理吧!";
                try {
                    boolean sendMsgFlag = sendUserMsg(true, Long.parseLong(rank.getCreateuserid()), userId, "17", rank.getRankid(), remark, "10");
                } catch (Exception e) {
                    logger.error("sendUserMsg error createuserid={},userid={},rankid={},remark={}",rank.getCreateuserid(), userId, rank.getRankid(), remark, e);
                }
            }
            if(row > 0 && rankMember.getStatus() == 1){
                boolean updateRankFlag = updateRankMemberCount(rankId,1);
                //往reids中放入初始化的排名值
                boolean initRedisFlag = initRedisRankSort(rank,userId);
                //参榜成功获得龙分
                userBehaviourService.pointChange(userInfo,"DAILY_ADDRANK",rank.getPtype(),null,0,0);
            }
            Map<String,Object> expandData = new HashMap<String,Object>();
            expandData.put("status",rankMember.getStatus());
            baseResp.setExpandData(expandData);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,rankMember.getStatus() == 1?"入榜成功":"入榜申请已提交,正在等待榜主审核");
        } catch (Exception e) {
            logger.error("insert rankMemeber error rankId:{} userId:{} error:",rankId,userId,e);
            //从redis中删除该用户
            printExceptionAndRollBackTransaction(e);
        }
        return baseResp;
    }

    /**
     * 给用户发送关于榜中 @我 消息
     * @param isOnly 消息是否唯一
     * @param userId 接收消息的用户id
     * @param friendId 朋友id
     * @param msgType 消息类型
     * @param snsId 业务id
     * @param remark
     * @param gType
     * @return
     */
    private Boolean sendUserMsg(boolean isOnly,Long userId,Long friendId,String msgType,Long snsId,String remark,String gType){
        if(isOnly){
            //先查询是否有该榜单的@我 消息
            int count = this.userMsgService.findSameTypeMessage(userId,msgType,snsId,gType);
            if(count > 0){
                //直接更改已读状态
                int updateRow = this.userMsgService.updateUserMsgStatus(userId,msgType,snsId,gType,remark,null);
                if(updateRow > 0){
                    return true;
                }
            }
        }
        //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
        BaseResp<Object> insertResult = userMsgService.insertMsg(friendId.toString(), userId.toString(),
        		"", gType,
        		snsId+"", remark, "2", msgType, "用户申请加入您创建的龙榜", 0, "", "");
        if(insertResult.getCode() == 0){
            return true;
        }
        return false;
    }

    /**
     * 移除超时未发进步的榜成员 只移除一个成员
     * @param rankId 榜id
     * @return
     */
    private boolean removeOverTimeRankMember(Long rankId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rankId",rankId);
        map.put("lastUpdateTime",DateUtils.formatDate(DateUtils.getBeforeDateTime(new Date(),RankMembers.maxHour*60),"yyyy-MM-dd HH:mm:ss"));

        //查询出可以挤走的用户id
        Long userid = this.rankMembersMapper.removeOverTimeRankMemberUserId(map);
        if(userid == null){
            return false;
        }
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("rankId",rankId);
        paraMap.put("userId",userid);
        paraMap.put("status",2);
        paraMap.put("updateTime",new Date());

        int row = this.rankMembersMapper.updateRank(paraMap);
        if(row > 0){
            //入榜人数减1
            boolean updateRankMemberCount = updateRankMemberCount(rankId,-1);
            //删除reids中榜单的该用户排名
            boolean redisRemoveFlag = springJedisDao.zRem(Constant.REDIS_RANK_SORT+rankId,userid+"");
            String remark = "由于您加入榜后,长时间未在榜中发微进步,已被其他新成员挤下榜,如您还想继续参榜,请到帮详情页重新参榜!";
            //给该用户发送系统消息,通知该用户被挤走了
            userMsgService.insertMsg(Constant.SQUARE_USER_ID, userid.toString(),null, "10",
                    rankId.toString(), remark, "2", "44", "榜中成员下榜", 0, "", "");

            return updateRankMemberCount;
        }
        return false;
    }

    /**
     * 获取可以挤走的榜成员数量
     * @param rankId
     * @return
     */
    private int getSureRemoveRankMemberCount(Long rankId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rankId",rankId);
        map.put("lastUpdateTime",DateUtils.getBeforeDateTime(new Date(),RankMembers.maxHour*60));

        return this.rankMembersMapper.getSureRemoveRankMemberCount(map);
    }

    /**
     * 退榜
     * @param userId
     * @param rankId
     * @return
     */
    @Override
    public BaseResp<Object> removeRankMember(Long userId, Long rankId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Rank rank = rankMapper.selectRankByRankid(rankId);
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(userId.equals(rank.getCreateuserid())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_612,Constant.RTNINFO_SYS_612);
            }
            if(rank.getIsfinish().equals("0")||rank.getIsfinish().equals("1")){
                //1.更改rankMember的状态
                Map<String,Object> updateMap = new HashMap<String,Object>();
                updateMap.put("rankId",rankId);
                updateMap.put("userId",userId);
                updateMap.put("status","2");
                updateMap.put("isfashionman","0");
                updateMap.put("updateTime",new Date());
                int updateRow = this.rankMembersMapper.updateRank(updateMap);
                if(updateRow < 1){
                    return baseResp.fail("退榜失败");
                }
                //2.更改用户在该榜单中发布的进步的状态
                int removeRow = improveRankMapper.updateImproveRanStatus(userId+"",rankId+"",null,"1");

                //3.更改参榜人数
                boolean updateRankFlag = updateRankMemberCount(rankId,-1);
                //4.删除reids中榜单的该用户排名
                boolean redisRemoveFlag = springJedisDao.zRem(Constant.REDIS_RANK_SORT+rankId,userId+"");
                return baseResp.ok("退榜成功");
            }else {
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_70,Constant.RTNINFO_SYS_70);
            }
        }catch(Exception e){
            logger.error("remove RankMember error userId:{} rankId:{}",userId,rankId);
            printException(e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> removeRankMember(RankMembers rankMembers,String opttype) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if(rankMembers.getUserid().equals(rank.getCreateuserid())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_612,Constant.RTNINFO_SYS_612);
            }
            rankMembers.setIcount(0);
            rankMembers.setIsfashionman("0");
            int updateRow = this.rankMembersMapper.updateRankMemberState(rankMembers);
            if(updateRow < 1){
                return baseResp.fail("退榜失败");
            }
            //添加消息---榜@我通知
            //sourcetype 来源类型。0 运营端创建   1  b端创建 2 app用户创建。
			String remark = Constant.MSG_RANKIMP_QUIT_MODEL;
			if(null != rank){
				remark = remark.replace("n", rank.getRanktitle());
			}else{
				remark = remark.replace("n", "");
			}
//            String remark = Constant.MSG_QUITRANK_MODEL.replace("n", rank.getRanktitle());
            if("0".equals(rank.getSourcetype())){
            	//mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
								//22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
								//25:订单发货N天后自动确认收货    26：实名认证审核结果
								//27:工作认证审核结果      28：学历认证审核结果
								//29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
								//32：创建的龙榜/教室/圈子被选中推荐
								//40：订单已取消 41 榜中进步下榜
								// 42.榜单公告更新   43:后台反馈回复消息 )
				//1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)
				//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
				//					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  44: 榜中成员下榜)
                if ("1".equals(opttype)){
                	String rem = Constant.MSG_RANK_CLOSS_MODEL;
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID, rankMembers.getUserid().toString(),
                            "", "10",
                            rankMembers.getRankid().toString(), rem, "2", "46", "榜关闭", 0, "", "");
                } else {
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID, rankMembers.getUserid().toString(),
                            "", "10",
                            rankMembers.getRankid().toString(), remark, "2", "44", "榜中成员下榜", 0, "", "");
                }

            }
            if("2".equals(rank.getSourcetype())){
                if ("1".equals(opttype)){
                	String rem = Constant.MSG_RANK_CLOSS_MODEL;
                    userMsgService.insertMsg(rank.getCreateuserid().toString(), rankMembers.getUserid().toString(),
                            "", "10",
                            rankMembers.getRankid().toString(), rem, "2", "46", "榜关闭", 0, "", "");
                } else {
                    userMsgService.insertMsg(rank.getCreateuserid().toString(), rankMembers.getUserid().toString(),
                            "", "10",
                            rankMembers.getRankid().toString(), remark, "2", "44", "榜中成员下榜", 0, "", "");
                }

            }

            //2.更改用户在该榜单中发布的进步的状态
            int removeRow = improveRankMapper.updateImproveRanStatus(rankMembers.getUserid()+"",
                    rankMembers.getRankid()+"",null,"1");
            //3.更改参榜人数
            boolean updateRankFlag = updateRankMemberCount(rankMembers.getRankid(),-1);
            //4.删除reids中榜单的该用户排名
            boolean redisRemoveFlag = springJedisDao.zRem(Constant.REDIS_RANK_SORT+rankMembers.getRankid(),
                    rankMembers.getUserid()+"");
            return baseResp.ok("退榜成功");

        } catch (Exception e) {
            logger.error("remove RankMember error userId:{} rankId:{}",rankMembers.getUserid(),rankMembers.getRankid());
        }
        return baseResp;
    }


    @Override
    public BaseResp<Object> closeRank(String rankid){
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            RankMembers rankMembers = new RankMembers();
            rankMembers.setRankid(Long.parseLong(rankid));
            //获取榜单全部成员列表
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,null,null);
            for(RankMembers rankMember : rankMemberses){
                try {
                    removeRankMember(rankMember,"1");
                }catch (Exception e){
                    logger.error("remove RankMember:{} error when close Rank rankId:{}",rankMember.getUserid(),rankid);
                    return baseResp.fail("关闭榜单失败");
                }
            }
        }catch (Exception e){
            logger.error("close RankMember error rankId:{}",rankid);
        }
        return baseResp.ok("关闭榜单成功");

    }


    @Override
    public BaseResp<Object> setIsfishionman(RankMembers rankMembers) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            if("1".equals(rankMembers.getIsfashionman())){
                rankMembers.setUpfashionmantime(new Date());
            }
            if ("0".equals(rankMembers.getIsfashionman())){
                rankMembers.setDownfashionmantime(new Date());
            }
            rankMembers.setIcount(0);
            int res = rankMembersMapper.updateRankMemberState(rankMembers);
            if (res > 0){
                if("1".equals(rankMembers.getIsfashionman())){
                    String remark = "你被选为榜单达人";
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID,String.valueOf(rankMembers.getUserid()),null,"9",
                            String.valueOf(rankMembers.getRankid()),remark,"0","47", "选为榜单达人",0, "", "");
                }
                if ("0".equals(rankMembers.getIsfashionman())){
                    String remark = "你被取消榜单达人";
                    userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(rankMembers.getUserid()),null,"9",
                            String.valueOf(rankMembers.getRankid()),remark,"0","47", "取消榜单达人",0, "", "");
                }
                return BaseResp.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateRankMemberCheckStatus(RankMembers rankMembers) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            if ("3".equals(rankMembers.getCheckstatus())){
                if (!canCheckPass(rankMembers)){
                    baseResp.initCodeAndDesp(Constant.STATUS_SYS_618,Constant.RTNINFO_SYS_618);
                    return baseResp;
                }
            }
            rankMembers.setIcount(0);
            int res = rankMembersMapper.updateRankMemberState(rankMembers);
            if (res > 0){
                return BaseResp.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResp;
    }


    private boolean canCheckPass(RankMembers rankMembers){
        Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
        //榜单参与人数
        int rankmnum = rank.getRankinvolved();
        //榜单设置奖品数
        int awardcount = getRankAwardCount(String.valueOf(rankMembers.getRankid()));
        //审核通过数
        RankMembers rankMembers1 = new RankMembers();
        rankMembers1.setRankid(rankMembers.getRankid());
        rankMembers1.setCheckstatus("3");
        int okcount = rankMembersMapper.selectCount(rankMembers1);
        if (okcount < awardcount){
            return true;
        }
        return false;
    }




    /**
     * 更改用户的参榜申请
     * @param userIds 用户id 数组
     * @param rankId 榜单id
     * @param status 要处理的结果
     * @return
     */
    @Override
    public BaseResp<Object> auditRankMember(Long[] userIds, Long rankId, Integer status) {
        BaseResp<Object> baseResp =new BaseResp<>();
        try{
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            Map<String,Object> updateMap = new HashMap<String,Object>();
            updateMap.put("rankId",rankId);
            updateMap.put("status",status);
            updateMap.put("initRankMember",false);
            updateMap.put("updateTime",new Date());

            List<Long> userIdList = new ArrayList<Long>();
            boolean flag = true;
            //牵扯到是否需要往redis中初始化用户排名,以及批量处理申请时,可能会超过人数限制,所以用了循环
            for(Long userId:userIds){
                RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
                if(rankMembers == null || status == rankMembers.getStatus()){
                    continue;
                }
                if(status == 1 && (rank.getRankinvolved()+1) > rank.getRanklimite()){
                    baseResp.initCodeAndDesp(Constant.STATUS_SYS_92,Constant.RTNINFO_SYS_92);
                    flag = false;
                    break;
                }
                //1.修改rankMember中的状态
                updateMap.put("userId",userId);
                int updateRow = this.rankMembersMapper.updateRank(updateMap);
                if(updateRow < 1){
                    continue;
                }
                //2.参榜人数加1
                boolean updateRankMemberCountFlag = updateRankMemberCount(rankId,1);
                if(updateRankMemberCountFlag){
                    rank.setRankinvolved(rank.getRankinvolved()+1);
                }
                //3.初始化redis的用户排名
                boolean redisFlag = initRedisRankSort(rank,userId);
                userIdList.add(userId);

                //4.参榜成功获得龙分
                UserInfo userInfo = userService.selectJustInfo(userId);
                userBehaviourService.pointChange(userInfo,"DAILY_ADDRANK",rank.getPtype(),null,0,0);
            }
            if(userIdList.size() > 0){
                String remark = null;
                if(status == 1){
                    remark = "您提交的加入龙榜\""+rank.getRanktitle()+"\"申请,已被审核通过,快去看看吧!";
                }else{
                    remark = "抱歉,您提交的加入龙榜\""+rank.getRanktitle()+"\"申请,已被榜主拒绝!";
                }
                UserMsg userMsg = new UserMsg();
                userMsg.setFriendid(Long.parseLong(rank.getCreateuserid()));
                userMsg.setMsgtype("17");
//                userMsg.setSnsid(rank.getRankid());
                userMsg.setRemark(remark);
                //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
				//10：榜中  11 圈子中  12 教室中  13:教室批复作业
                userMsg.setGtype("10");
                userMsg.setGtypeid(rank.getRankid());
                userMsg.setMtype("2");//@我消息 榜的消息 都属于@我消息
                userMsg.setCreatetime(new Date());
                userMsg.setIsdel("0");
                userMsg.setIsread("0");
                userMsg.setTitle("加入请求审批结果");
                boolean insertUserMsg = this.userMsgService.batchInsertUserMsg(userIdList,userMsg);
            }
            if(flag){
                return baseResp.ok();
            }
            return baseResp;
        }catch(Exception e){
            logger.error("audit rankMemeber error userIds:{} rankId:{} status:{}",userIds,rankId,status);
            printException(e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> submitRankMemberCheckResultPreview(String rankid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        if (!isSubRankMemberCheckResult(rankid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_54,Constant.RTNINFO_SYS_54);
        }
        try {
            //发布奖品
            publishRankAward(rankid);
            return BaseResp.ok();
        } catch (Exception e) {
            logger.error("submint rank member checkresult rankid={} is error:",rankid,e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Object> submitRankMemberCheckResult(Rank rank,boolean isUpdateRank,boolean isGtAwardCount) {
        BaseResp<Object>  baseResp = submitRankMemberCheckResultPreview(String.valueOf(rank.getRankid()));
        if (ResultUtil.isSuccess(baseResp)){
            if(isUpdateRank){
                Rank rank1 = rankMapper.selectRankByRankid(rank.getRankid());
                //防止获奖名单重复提交
                if ("5".equals(rank1.getIsfinish())){
                    return baseResp;
                }
                int res = rankMapper.updateSymbolByRankId(rank);
            }
            //添加中奖名单信息
            insertRankAcceptAwardInfo(String.valueOf(rank.getRankid()));
            //发送获奖消息
//            try {
//                sendRankEndUserMsg(rank);
//            } catch (Exception e) {
//                logger.error("send rank rankid={} end msg is error:",rank.getRankid(),e);
//            }
            return baseResp;
        }
        return BaseResp.fail();
    }

    @Override
    public BaseResp<Page<RankMembers>> rankMemberCheckResultPreview(RankMembers rankMembers, Integer pageNo, Integer pageSize) {
        Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
        BaseResp baseResp = new BaseResp();
        if (null != rank && "3".equals(rank.getIsfinish())){
            baseResp = BaseResp.ok();
        }else{
            baseResp = submitRankMemberCheckResultPreview(String.valueOf(rankMembers.getRankid()));
        }

        if (ResultUtil.isSuccess(baseResp)) {
            rankMembers.setCheckstatus("-1");
            BaseResp<Page<RankMembers>> pageBaseResp = selectRankMemberList(rankMembers, pageNo, pageSize);
            return pageBaseResp;
        }
        return baseResp;
    }

    /**
     * 获取用户在榜中的排名
     * @param userId
     * @param rankId
     * @return
     */
    @Override
    public Map<String,Object> getUserSortNumAndImproveCount(Long userId, Long rankId) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try{
            int sortNum = 0;
            int improveCount = 0;
            //查询Rank
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if(rank == null){
                resultMap.put("sortnum",sortNum);
                resultMap.put("improveCount",improveCount);
                return resultMap;
            }
            RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
            if(rankMembers == null || rankMembers.getStatus() != 1){
                resultMap.put("sortnum",sortNum);
                resultMap.put("improveCount",improveCount);
                return resultMap;
            }
            if("0".equals(rank.getIsfinish())){
                resultMap.put("sortnum",sortNum);
                resultMap.put("improveCount",improveCount);
                return resultMap;
            }else if("1".equals(rank.getIsfinish())){
                long sort = this.springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+rankId,userId+"");
                sortNum = Integer.parseInt(sort+"");
            }else{
                sortNum = rankMembers.getSortnum();
            }
            improveCount = rankMembers.getIcount();
            resultMap.put("sortnum",sortNum);
            resultMap.put("improveCount",improveCount);
            return resultMap;
        }catch(Exception e){
            logger.error("get user sortnum error userId:{} rankId:{}",userId,rankId);
            printException(e);
        }
        resultMap.put("sortnum",0);
        resultMap.put("improveCount",0);
        return resultMap;
    }

    /**
     * 查询用户在榜单中的排名
     * @param rankId 榜单id
     * @param userId 用户id
     * @return
     */
    @Override
    public BaseResp<Object> ownRankSort(Long rankId, Long userId,Long currentUserid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //查询Rank
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
            if(rankMembers == null || rankMembers.getStatus() != 1){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_910,Constant.RTNINFO_SYS_910);
            }
            if("0".equals(rank.getIsfinish())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_68,Constant.RTNINFO_SYS_68);
            }else if("1".equals(rank.getIsfinish())){
                long sort = this.springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+rankId,userId+"");
                rankMembers.setSortnum(Integer.parseInt(sort+""));
            }else{
                rankMembers.setLikes(rankMembers.getRankfinishlikes());
                rankMembers.setFlowers(rankMembers.getRankfinishflower());
            }

            AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(userId+"");
            if(currentUserid != null && !currentUserid.equals(userId)){
            	//获取好友昵称
    			appUserMongoEntity.setNickname(this.friendService.getNickName(currentUserid,userId));
//                appUserMongoEntity.setNickname(this.friendService.getNickName(currentUserid,userId));
            }
            rankMembers.setAppUserMongoEntity(appUserMongoEntity);

            Map<String,Object> resultMap = new HashMap<String,Object>();
            if("5".equals(rank.getIsfinish()) && "1".equals(rankMembers.getIswinning())){
                if("3".equals(rankMembers.getCheckstatus())){
                    if("0".equals(rankMembers.getAcceptaward())){
                        rankMembers.setIswinning("1");//已中奖 未领取
                    }else{
                        RankAcceptAward rankAcceptAward = rankAcceptAwardMapper.selectByRankIdAndUserid(
                                String.valueOf(rankId),String.valueOf(userId));
                        if(null != rankAcceptAward){
                            // status 状态 0 - 未领取 1 - 已领取 2 - 已经发货 待确认（实物 物流） 3 - 已完成 4 - 已经失效
                            if(rankAcceptAward.getStatus().intValue() == 2){
                                rankMembers.setIswinning("5");//已中奖 已经发货
                            }else if(rankAcceptAward.getStatus().intValue() == 3){
                                rankMembers.setIswinning("6");//已中奖 已经确认收货
                            }else {
                                rankMembers.setIswinning("3");//已中奖 且已领奖 未发货
                            }
                        }else{
                            rankMembers.setIswinning("3");//已中奖 且已领奖 未发货
                        }
                    }
                }else if("0".equals(rankMembers.getCheckstatus())){
                    rankMembers.setIswinning("4");//已中奖 人工审核中
                }else{
                    rankMembers.setIswinning("2");//审核未通过
                }
                RankAward rankAward = this.rankAwardMapper.selectRankAwardByRankIdAndAwardId(rankId,Long.parseLong(rankMembers.getRankAward().getAwardid()));
                rankMembers.setRankAward(rankAward);
            }else if("1".equals(rank.getIsfinish())){
                String sn = rank.getMinimprovenum();
                if(StringUtils.isBlank(sn)){
                }else{
                    int n = (Integer.parseInt(sn) - rankMembers.getIcount());
                    if(n>0){
                        rankMembers.setCheckresult("根据榜规则，至少还需要发"+n+"条微进步！");
                    }
                }
            }else if(!"0".equals(rank.getIsfinish()) && "1".equals(rankMembers.getIswinning())){
                rankMembers.setIswinning("4");
            }else if(!"0".equals(rank.getIsfinish()) && !"5".equals(rank.getIsfinish()) && "0".equals(rankMembers.getIswinning())){
                rankMembers.setIswinning("4");
            }

            baseResp.setData(rankMembers);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("own rank sort error rankId:{} userId:{}",rankId,userId);
            printException(e);
        }

        return baseResp;
    }


    @Override
    public BaseResp<List<RankCheckDetail>> selectRankCheckDetailList(String rankid) {
        BaseResp<List<RankCheckDetail>> baseResp = new BaseResp<>();
        try {
            List<RankCheckDetail> list = rankCheckDetailMapper.selectList(rankid);
            baseResp = BaseResp.ok();
            baseResp.setData(list);
        } catch (Exception e) {
            logger.error("select rank rankid={} checkdetail is error:",rankid,e);
        }
        return baseResp;
    }

    /**
     * 更改榜单的加榜验证 或 公告
     * @param rankId
     * @param userid 当前登录用户id
     * @param needConfirm 加榜是否需要验证 该参数不可与notice参数同事传入
     * @param notice 公告内容
     * @param noticeUser 更改公告是否需要通知用户
     * @return
     */
    @Override
    public BaseResp<Object> updateRankInfo(Long rankId, Long userid, Boolean needConfirm, final String notice, Boolean noticeUser) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            final Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if(rank == null || !(userid+"").equals(rank.getCreateuserid())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_615,Constant.RTNINFO_SYS_615);
            }
            Rank updateRank = new Rank();
            updateRank.setRankid(rank.getRankid());
            if(needConfirm != null){
                updateRank.setNeedConfirm(needConfirm);
            }else if(StringUtils.isNotEmpty(notice)){
                updateRank.setNotice(notice);
            }
            updateRank.setUpdatetime(new Date());
            int row = this.rankMapper.updateSymbolByRankId(updateRank);
            if(row > 0 && StringUtils.isNotEmpty(notice) && noticeUser != null && noticeUser){
                rankNoticeMessage(rankId,notice);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("update rank info error rankId:{} userid:{} needConfirm:{} notice:{} noticeUser:{}",rankId,userid,needConfirm,notice,noticeUser);
            printException(e);
        }
        return baseResp;
    }


    private void rankNoticeMessage(Long rankId,final String  notice){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rankId",rankId);
        map.put("status","1");
        final List<RankMembers> rankMembersList= this.rankMembersMapper.selectRankMembers(map);
        final Rank rank = this.rankMapper.selectRankByRankid(rankId);
        if(rankMembersList != null && rankMembersList.size() > 0){
            //通知所有用户
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        String messageContent = "\"" + rank.getRanktitle() + "\"龙榜更新了公告:"+notice;
                        List<Long> userIdList = new ArrayList<Long>();
                        for(RankMembers rankMembers:rankMembersList){
                            userIdList.add(rankMembers.getUserid());
                        }
                        UserMsg userMsg = new UserMsg();
                        //sourcetype   来源类型。0 运营端创建   1  app 2   商户
                		if(Constant.RANK_SOURCE_TYPE_1.equals(rank.getSourcetype())){
                        	userMsg.setFriendid(Long.parseLong(rank.getCreateuserid()));
                        }else{
                        	userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
                        }
                        userMsg.setMtype("2");//系统消息
                        userMsg.setMsgtype("42");
                        userMsg.setSnsid(rank.getRankid());
                        userMsg.setGtypeid(rank.getRankid());
                        userMsg.setRemark(messageContent);
                        userMsg.setGtype("10");
                        userMsg.setCreatetime(new Date());
                        userMsg.setUpdatetime(new Date());
                        userMsg.setIsdel("0");
                        userMsg.setIsread("0");
                        userMsg.setTitle("龙榜公告更新");
                        userMsgService.batchInsertUserMsg(userIdList,userMsg);
                    }catch (Exception e){
                        logger.error("update rank info notice user error msg:{}",e);
                    }
                }
            });
        }
    }

//    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public BaseResp<Object> insertNotice(Rank rank, String noticetype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            rank.setUpdatetime(new Date());
            int row = this.rankMapper.updateSymbolByRankId(rank);
            if(row > 0 && "1".equals(noticetype)){
                rankNoticeMessage(rank.getRankid(),rank.getNotice());
            }
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("insert rank notice is error:",e);
        }
        return baseResp;
    }

    /**
     * 查询榜主名片详情
     * @param rankCardId
     * @return
     */
    @Override
    public BaseResp<Object> rankCardDetail(String rankCardId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            RankCard rankCard = this.rankCardMapper.selectByPrimaryKey(Integer.parseInt(rankCardId));
            baseResp.setData(rankCard);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select rankCard detail error rankCardId:{}",rankCardId);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 榜单的成员排名列表
     * @param rankId 榜单id
     * @param sortType 排序的类型
     * @param startNum 开始下标
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public BaseResp<Object> rankMemberSort(Long userId,Long rankId, Integer sortType, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            List<RankMembers> userList = new ArrayList<RankMembers>();

            if(sortType == 0 && "1".equals(rank.getIsfinish())){
                Integer endNum = startNum + pageSize - 1;
                Set<String> userIdList  = this.springJedisDao.zRevrange(Constant.REDIS_RANK_SORT+rankId,startNum,endNum);
                if(userIdList != null && userIdList.size() > 0){
                    int i = 0;
                    for(String tempUserId:userIdList){
                        RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,Long.parseLong(tempUserId));
                        if(rankMembers == null){
                            continue;
                        }
                        rankMembers.setSortnum(startNum+i+1);
                        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(tempUserId+"");
                        if(userId != null && !Constant.VISITOR_UID.equals(userId + "")){
                        	//获取好友昵称
        					String remark = userRelationService.selectRemark(userId, rankMembers.getUserid(), "0");
        					if(!StringUtils.isBlank(remark)){
        						appUserMongoEntity.setNickname(remark);
        					}
                        }
                        rankMembers.setAppUserMongoEntity(appUserMongoEntity);
                        rankMembers.setRankAward(new RankAward());
                        userList.add(rankMembers);
                        i++;
                    }
                }
            }else{
                userList = this.rankMembersMapper.selectUserSort(rankId,sortType,"1".equals(rank.getIsfinish())?0:1,startNum,pageSize);
                if(userList != null && userList.size() > 0){
                    int i = 0;
                    for (RankMembers rankMember:userList){
                        rankMember.setSortnum(startNum + i +1);
                        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(rankMember.getUserid()+"");
                        if(userId != null){
                            appUserMongoEntity.setNickname(this.friendService.getNickName(userId,Long.parseLong(rankMember.getUserid()+"")));
                        }
                        rankMember.setAppUserMongoEntity(appUserMongoEntity);

                        //获取奖
                        if("5".equals(rank.getIsfinish()) && "1".equals(rankMember.getIswinning())){
                            if("3".equals(rankMember.getCheckstatus())){
                                if("0".equals(rankMember.getAcceptaward())){
                                    rankMember.setIswinning("1");//已中奖 未领取
                                }else{
                                    RankAcceptAward rankAcceptAward = rankAcceptAwardMapper.selectByRankIdAndUserid(
                                            String.valueOf(rankId),String.valueOf(rankMember.getUserid()));
                                    if(null != rankAcceptAward){
                                        // status 状态 0 - 未领取 1 - 已领取 2 - 已经发货 待确认（实物 物流） 3 - 已完成 4 - 已经失效
                                        if(rankAcceptAward.getStatus().intValue() == 2){
                                            rankMember.setIswinning("5");//已中奖 已经发货
                                        }else if(rankAcceptAward.getStatus().intValue() == 3){
                                            rankMember.setIswinning("6");//已中奖 已经确认收货
                                        }else {
                                            rankMember.setIswinning("3");//已中奖 且已领奖 未发货
                                        }
                                    }else{
                                        rankMember.setIswinning("3");//已中奖 且已领奖 未发货
                                    }
                                }
                                if(sortType == 0){
                                    RankAward rankAward = this.rankAwardMapper.selectRankAwardByRankIdAndAwardId(rankId,Long.parseLong(rankMember.getRankAward().getAwardid()));
                                    rankAward.setAward(this.awardMapper.selectByPrimaryKey(Long.parseLong(rankAward.getAwardid())));
                                    rankMember.setRankAward(rankAward);
                                }
                            }else if("0".equals(rankMember.getCheckstatus())){
                                rankMember.setIswinning("4");//已中奖 人工审核中
                            }else{
                                rankMember.setIswinning("2");//审核未通过
                            }
                        }else{
                            rankMember.setRankAward(new RankAward());
                        }

                        i++;
                    }
                }
            }

            baseResp.setData(userList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("rank member sort list error rankId:{} sortType:{} startNum:{} pageSize:{}",rankId,sortType,startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 获取榜中的达人
     * @param rankId 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> selectFashionMan(Long userId,Long rankId, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("rankId",rankId);
            parameterMap.put("isFashionMan","1");
            parameterMap.put("status","1");
            parameterMap.put("startNum",startNum);
            parameterMap.put("pageSize",pageSize);
            List<RankMembers> rankMembersList = this.rankMembersMapper.selectRankMembers(parameterMap);

            List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
            if(rankMembersList != null && rankMembersList.size() > 0){
                for (RankMembers rankMembers : rankMembersList) {
                    AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(rankMembers.getUserid()+"");
                    if(appUserMongoEntity == null){
                        continue;
                    }
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("userid",appUserMongoEntity.getUserid());
                    map.put("avatar",appUserMongoEntity.getAvatar());


                    //判断是否是好友
                    if(!Constant.VISITOR_UID.equals(userId+"")){
                        SnsFans snsFans = this.snsFansMapper.selectByUidAndLikeid(userId,rankMembers.getUserid());
                        if(snsFans != null){
                            map.put("isfans","1");
                        }else{
                            map.put("isfans","0");
                        }
                        SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userId,rankMembers.getUserid(), "0");
                        if(snsFriends != null){
                            map.put("isfriend","1");
                        }else{
                            map.put("isfriend","0");
                        }
                        if(userId == null){
                            map.put("usernickname",appUserMongoEntity.getNickname());
                        }else{
                            map.put("usernickname",this.friendService.getNickName(userId,rankMembers.getUserid()));
                        }
                        if(userId == null){
                            map.put("isfans","0");
                            resultList.add(map);
                            continue;
                        }
                        if(userId.longValue() == rankMembers.getUserid().longValue()){
                            map.put("isfans","1");
                            resultList.add(map);
                            continue;
                        }
                    }else{
                        map.put("usernickname",appUserMongoEntity.getNickname());
                    }

                    resultList.add(map);
                }
            }
            baseResp.setData(resultList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select rank fashionMain error rankId:{} startNum:{} pageSize:{}",rankId,startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**\
     * 用户领奖
     * @param userId 用户id
     * @param rankId 榜单id
     * @return
     */
    @Transactional
    @Override
    public BaseResp<Object> acceptAward(Long userId, Long rankId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try{
            Rank rank = this.rankMapper.selectRankByRankid(rankId);
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            RankMembers rankMember = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
            if(rankMember == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if((!"1".equals(rankMember.getIswinning())) || (rankMember != null && rankMember.getRankAward().getAwardid() == null)){//判断是否获奖
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_66,Constant.RTNINFO_SYS_66);
            }
            //查询该用户获得的奖
            Award award = this.awardMapper.selectAwardAndAwardClassify(Long.parseLong(rankMember.getRankAward().getAwardid()));
            if(award == null){
                logger.error("query award null awardId:{}",rankMember.getAwardid());
                return baseResp.fail("系统异常");
            }
            RankAcceptAward rankAcceptAward = this.rankAcceptAwardMapper.selectByRankIdAndUserid(rankId+"",userId+"");
            if(rankAcceptAward == null){
                logger.error("query rankAcceptAward null userId:{} rankId:{}",userId,rankId);
                return baseResp.fail("系统异常");
            }

            Map<String,Object> rankResultMap = new HashMap<String,Object>();
            rankResultMap.put("ranktitle",rank.getRanktitle());
            rankResultMap.put("ptype",rank.getPtype());
            resultMap.put("rank",rankResultMap);

            Integer acceptAwardStatus = Integer.parseInt(rankMember.getAcceptaward());
            String explain = "";//前台显示的说明文案
            if(award.getAwardClassify().getClassifytype() == 0){//进步币
                explain = "进步币可在\"我的钱包\"中查看";
            }else if(award.getAwardClassify().getClassifytype() == 1){//红包
                if(acceptAwardStatus < 2){//0 未领奖 1 领奖 2 发货 3签收
                    explain = "添加龙杯小编微信:15816987854\n发送你的领奖码给小编,领取微信红包";
                }else{
                    explain = "你已领奖成功";
                }
            }else if(award.getAwardClassify().getClassifytype() == 3){//实物
                if(acceptAwardStatus < 2){//0 未领奖 1 领奖 2 发货 3签收
                    explain = "奖品会在20个工作日内发放,\n记得领取哦";
                }else if(acceptAwardStatus == 2){
                    explain = "奖品已经发放,正在飞奔到你的手中";
                }else if(acceptAwardStatus == 3){
                    explain = "你已领奖成功";
                }
            }
            baseResp.getExpandData().put("explain",explain);
            //获取用户获得的奖项信息
            RankAwardRelease rankAwardRelease = this.rankAwardReleaseMapper.selectByRankIdAndAwardId(rankId+"",rankMember.getRankAward().getAwardid()+"");
            rankMember.getRankAward().setAwardnickname(rankAwardRelease.getAwardnickname());

            //已领奖
            if(rankMember.getAcceptaward() != null && acceptAwardStatus > 0){
                Map<String,Object> rankAcceptAwardMap = new HashMap<String,Object>();
                rankAcceptAwardMap.put("receiverid",rankAcceptAward.getId());
                if(award.getAwardClassify().getClassifytype() == 3){//实物
                    rankAcceptAwardMap.put("reciverusername",rankAcceptAward.getReciverusername());//收货人姓名
                    rankAcceptAwardMap.put("reciveruseraddr",rankAcceptAward.getReciveruseraddr());//收货人地址
                    rankAcceptAwardMap.put("reciverusertel",rankAcceptAward.getReciverusertel());//收货人电话
                }
                rankAcceptAwardMap.put("reciverstatus",rankAcceptAward.getStatus());// 1 领奖 2 发货 3签收
                rankMember.setReceivecode(rankAcceptAward.getReceivecode());
                resultMap.put("rankAcceptAward",rankAcceptAwardMap);
                resultMap.put("award",award);
                resultMap.put("rankMember",rankMember);
                baseResp.setData(resultMap);

                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                return baseResp;
            }
            //实物 未领奖
            if(acceptAwardStatus == 0 && award.getAwardClassify().getClassifytype() == 3){
                //获取用户的默认收货地址
                UserAddress userAddress = this.userAddressService.selectDefaultAddressByUserid(userId);
                if(userAddress != null){
                    Map<String,Object> rankAcceptAwardMap = new HashMap<String,Object>();
                    rankAcceptAwardMap.put("receiverid",rankAcceptAward.getId());
                    rankAcceptAwardMap.put("userAddressId",userAddress.getId());
                    rankAcceptAwardMap.put("reciverusername",userAddress.getReceiver());
                    rankAcceptAwardMap.put("reciveruseraddr",userAddress.getRegion()+userAddress.getAddress());
                    rankAcceptAwardMap.put("reciverusertel",userAddress.getMobile());
                    rankAcceptAwardMap.put("reciverstatus",rankAcceptAward.getStatus());// 1 领奖 2 发货 3签收
                    resultMap.put("rankAcceptAward",rankAcceptAwardMap);
                }
            }

            RankAcceptAward newRankAcceptAward = new RankAcceptAward();
            newRankAcceptAward.setRankid(rankId);
            newRankAcceptAward.setUserid(userId);
            if(award.getAwardClassify().getClassifytype() < 3){//除实物以外的
                newRankAcceptAward.setAcceptdate(new Date());
                newRankAcceptAward.setStatus(1);

                rankMember.setAcceptaward("1");
            }

            if(award.getAwardClassify().getClassifytype() == 0){//进步币
                //加进步币
                BaseResp<Object> baseResp1 = userImpCoinDetailService.insertPublic(userId,"4",(int) (award.getAwardprice()*Constant.RMB_POINT),rankId,null);
                newRankAcceptAward.setPublishawardtype("0");

                if(baseResp1.getCode() == 0){
                    //更改用户的领奖状态
                    Map<String,Object> parameterMap = new HashMap<String,Object>();
                    parameterMap.put("userId",userId);
                    parameterMap.put("rankId",rankId);
                    parameterMap.put("acceptaward","3");
                    int updateRow = this.rankMembersMapper.updateRank(parameterMap);
                    rankMember.setAcceptaward("3");

                    newRankAcceptAward.setStatus(3);
                }
            }else if(award.getAwardClassify().getClassifytype() == 1){//红包
                //更改用户的领奖状态
                Map<String,Object> parameterMap = new HashMap<String,Object>();
                parameterMap.put("userId",userId);
                parameterMap.put("rankId",rankId);
                parameterMap.put("acceptaward","1");
                int updateRow = this.rankMembersMapper.updateRank(parameterMap);
            }
            if(newRankAcceptAward.getAcceptdate() != null){
                BaseResp<Object> updateRankAcceptAward = this.rankAcceptAwardService.updateRankAcceptAward(newRankAcceptAward);
            }

            resultMap.put("award",award);
            resultMap.put("rankMember",rankMember);

            baseResp.setData(resultMap);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            return baseResp;
        }catch(Exception e){
            logger.error("user accept award error userId:{} rankId:{}",userId,rankId);
            printExceptionAndRollBackTransaction(e);
        }

        return baseResp;
    }

    /**
     * 领取实物奖品
     * @param userId 用户id
     * @param rankId 榜单id
     * @param userAddressId 收货地址id
     * @return
     */
    @Override
    public BaseResp<Object> acceptRealAward(Long userId, Long rankId, Long userAddressId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            RankMembers rankMember = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userId);
            if(rankMember == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            if((rankMember != null && !"1".equals(rankMember.getIswinning())) || (rankMember != null && rankMember.getRankAward().getAwardid() == null)){//判断是否获奖
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_66,Constant.RTNINFO_SYS_66);
            }else if(rankMember != null && rankMember.getAcceptaward() != null && !"0".equals(rankMember.getAcceptaward())){//已经领过奖了
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_65,Constant.RTNINFO_SYS_65);
            }
            //查询该用户获得的奖
            Award award = this.awardMapper.selectAwardAndAwardClassify(Long.parseLong(rankMember.getRankAward().getAwardid()));
            if(award == null){
                logger.error("query award null awardId:{}",rankMember.getAwardid());
                return baseResp.fail("系统异常");
            }
            if(award.getAwardClassify().getClassifytype() < 3){//不是实物奖品
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_611,Constant.RTNINFO_SYS_611);
            }
            UserAddress userAddress = this.userAddressService.selectByPrimaryKey(userId, userAddressId);
            if(userAddress == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }

            RankAcceptAward rankAcceptAward = new RankAcceptAward();
            rankAcceptAward.setRankid(rankId);
            rankAcceptAward.setUserid(userId);
            rankAcceptAward.setAcceptdate(new Date());//领奖时间
            rankAcceptAward.setStatus(1);//状态 已领奖
            rankAcceptAward.setReciverusername(userAddress.getReceiver());//收货人
            rankAcceptAward.setReciveruseraddr(userAddress.getRegion() + userAddress.getAddress());//收货地址
            rankAcceptAward.setReciverusertel(userAddress.getMobile());//收货人电话

            BaseResp<Object> updateRankAcceptAward = this.rankAcceptAwardService.updateRankAcceptAward(rankAcceptAward);
            if(updateRankAcceptAward.getCode() == 0){
                //更改用户的领奖状态
                Map<String,Object> parameterMap = new HashMap<String,Object>();
                parameterMap.put("userId",userId);
                parameterMap.put("rankId",rankId);
                parameterMap.put("acceptaward","1");
                int updateRow = this.rankMembersMapper.updateRank(parameterMap);
                if(updateRow > 0){
                    return baseResp.ok();
                }
            }
            return baseResp.fail("操作失败!");
        }catch(Exception e){
            logger.error("accept real award error userid:{} rankId:{} userAddressId:{}",userId,rankId,userAddressId);
            printExceptionAndRollBackTransaction(e);
        }
        return baseResp;
    }

    /**
     * 查看单个用户在榜中的信息
     * @param userid 用户id
     * @param currentUserId 当前登录用户id
     * @return
     */
    @Override
    public BaseResp<Object> selectRankMebmerDetail(Long userid,Long rankId, Long currentUserId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(userid+"");
            //获取好友昵称
            String remark = userRelationService.selectRemark(currentUserId, userid, "0");
            if(!StringUtils.isBlank(remark)){
                appUserMongoEntity.setNickname(remark);
            }
            if(appUserMongoEntity == null) return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            Map<String,Object> resultMap = new HashMap<String,Object>();
            RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankId,userid);
            if(rankMembers == null || rankMembers.getStatus() != 1) return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            Long sortNum = null;
            if(rankMembers.getSortnum() == null||rankMembers.getSortnum() == 0){
                sortNum = this.springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+rankId,userid+"");
            }else{
                sortNum = new Long(rankMembers.getSortnum());
            }
            if(currentUserId != null){
                SnsFans snsFans = this.snsFansMapper.selectByUidAndLikeid(currentUserId,userid);
                if(snsFans != null){
                    resultMap.put("isfans",1);
                }else{
                    resultMap.put("isfans",0);
                }
                SnsFriends snsFriends = this.snsFriendsMapper.selectByUidAndFid(currentUserId, userid, "0");
                if(snsFriends != null && snsFriends.getIsdel() == 0){
                    resultMap.put("isFriends",1);
                }else{
                    resultMap.put("isFriends",0);
                }
            }else{
                resultMap.put("isfans",0);
                resultMap.put("isFriends",0);
            }

            resultMap.put("likes",rankMembers.getLikes());
            resultMap.put("flowers",rankMembers.getFlowers());
            resultMap.put("nickname",appUserMongoEntity.getNickname());
            resultMap.put("avatar",appUserMongoEntity.getAvatar());
            resultMap.put("userid",appUserMongoEntity.getUserid());
            resultMap.put("sortnum",sortNum);
            resultMap.put("icount",rankMembers.getIcount());
            Rank rank = rankMapper.selectRankByRankid(rankId);
            resultMap.put("ranktitle",rank.getRanktitle());
            resultMap.put("isfinish",rank.getIsfinish());
            resultMap.put("endtime",DateUtils.formatDate(rank.getEndtime(),"yyyy-MM-dd HH:mm:ss"));
            resultMap.put("starttime",DateUtils.formatDate(rank.getStarttime(),"yyyy-MM-dd HH:mm:ss"));
            baseResp.setData(resultMap);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select rankMember detail error userid:{} currentUserId:{}",userid,currentUserId);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询中奖的用户
     * @return
     */
    @Override
    public BaseResp<Object> selectWinningRankAward(Long userid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
            List<RankMembers> winningRankAwardList = this.rankMembersMapper.selectWinningRankAward();
            if(winningRankAwardList != null && winningRankAwardList.size() > 0){
                for(RankMembers rankMembers: winningRankAwardList){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("rankid",rankMembers.getRankid());

                    Award award = this.awardMapper.selectByPrimaryKey(Long.parseLong(rankMembers.getRankAward().getAwardid()));
                    map.put("awardnickname",award.getAwardtitle());
                    map.put("nickname",this.friendService.getNickName(userid,rankMembers.getUserid()));
                    resultList.add(map);
                }
            }
            baseResp.setData(resultList);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select winning rankAward error");
            printException(e);
        }
        return baseResp;
    }

    /**
     * 通知关注榜单的用户 榜单已开始
     * @param rank
     * @return
     */
    @Override
    public BaseResp<Object> noticeFollowRankUser(Rank rank) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            List<Long> userIdList = new ArrayList<Long>();
                //通过rank查看关注的用户
                Map<String,Object> paraMap = new HashMap<String,Object>();
                paraMap.put("businessId",rank.getRankid());
                paraMap.put("businessType","2");
                List<UserBusinessConcern> userList = this.userBusinessConcernMapper.findConcernUserList(paraMap);
                for(UserBusinessConcern userBusinessConcern:userList){
                    userIdList.add(userBusinessConcern.getUserid());
                }
                if(userIdList.size() == 0){
                    return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                }
                UserMsg userMsg = new UserMsg();
                userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
                userMsg.setMtype("0");
                userMsg.setMsgtype("20");
//                userMsg.setSnsid(rank.getRankid());
                userMsg.setGtypeid(rank.getRankid());
                userMsg.setRemark("您关注的榜已经开始了,快去参榜吧!");
                //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
				//10：榜中  11 圈子中  12 教室中  13:教室批复作业
                userMsg.setGtype("10");
                userMsg.setMtype("0");//系统消息
                userMsg.setCreatetime(new Date());
                userMsg.setUpdatetime(new Date());
                userMsg.setIsdel("0");
                userMsg.setIsread("0");
                userMsg.setTitle("龙榜关注开榜通知");
                this.userMsgService.batchInsertUserMsg(userIdList,userMsg);
            return baseResp.ok();
        }catch(Exception e){
            logger.error("notice follow rank user error rank:{}", com.alibaba.fastjson.JSON.toJSONString(rank));
            printException(e);
        }
        return baseResp;
    }

    /**
     * 将已开始的榜单置为已开始
     * @param currentDate
     * @return
     */
    @Override
    public BaseResp<Object> handleStartRank(Date currentDate) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("currentDate",DateUtils.formatDate(currentDate,"yyyy-MM-dd HH:mm:ss"));
            List<Rank> lists = this.rankMapper.selectStartRank(map);
            if (null == lists || lists.size() == 0){
                return BaseResp.ok();
            }
            for (Rank rank : lists){
                Rank torank = new Rank();
                torank.setRankid(rank.getRankid());
                torank.setIsfinish("1");
                int row = rankMapper.updateSymbolByRankId(torank);
                if (row > 0){
                    noticeFollowRankUser(rank);
                }
            }
            logger.info("handleStartRank currentDate={},",currentDate);
            return baseResp.ok();
        }catch(Exception e){
            logger.error("handle start rank error currentDate:{]",currentDate);
        }
        return baseResp.fail();
    }

    /**
     * 榜单奖品自动确认收货
     * @param currentDate
     * @return
     */
    @Override
    public BaseResp<Object> rankAwardConfirmReceipt(Date currentDate) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            //查询待确认收货的
            List<RankAcceptAward> rankAcceptAwardList = this.rankAcceptAwardService.selectAutoConfirmReceiptRankAward(currentDate);
            if(rankAcceptAwardList == null || rankAcceptAwardList.size() == 0){
                return baseResp.ok();
            }
            //2017-04-27  不用推消息(已确认)
//            for(RankAcceptAward rankAcceptAward:rankAcceptAwardList){
                //将状态改成已确认收货,发消息给该用户
//                UserMsg userMsg = new UserMsg();
//                userMsg.setCreatetime(new Date());
//                userMsg.setUpdatetime(new Date());
//                userMsg.setUserid(rankAcceptAward.getUserid());
//                userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
//                userMsg.setMtype("0");//系统消息
//                userMsg.setMsgtype("25");
//                userMsg.setSnsid(rankAcceptAward.getRankid());
//                userMsg.setGtype("2");
//                userMsg.setIsdel("0");
//                userMsg.setIsread("0");
//                userMsg.setRemark("由于您长时间未确认收货,系统已为您自动确认收货!");
//                BaseResp baseResp1 = this.userMsgService.insertSelective(userMsg);
//            	String remark = "由于您长时间未确认收货,系统已为您自动确认收货!";
            	//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
				//10：榜中  11 圈子中  12 教室中  13:教室批复作业
//            	userMsgService.insertMsg(Constant.SQUARE_USER_ID, rankAcceptAward.getUserid().toString(),
//            			"", "10", rankAcceptAward.getRankid().toString(),
//            			remark, "0", "25", 0);
//            }
            //系统同意修改确认收货状态
            int row = this.rankAcceptAwardService.updateRankAwardStatus(currentDate);
            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("rank award confirm receipt error currentDate:{}",currentDate);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询榜单地区
     * @return
     */
    @Override
    public BaseResp<Object> selectRankArea() {
        //查询榜单的所有地区
        List<Long> areaIdList = this.rankMapper.selectRankArea();
        List<DictArea> areaList = this.dictAreaMapper.findAreaListById(areaIdList);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        baseResp.setData(areaList);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    /**
     * 用户手动确认收货
     * @param userid 用户id
     * @param rankId 榜单id
     * @return
     */
    @Override
    public BaseResp<Object> userRankAwardConfirmReceipt(Long userid, Long rankId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            //查询是否有该用户的获奖信息
            RankAcceptAward rankAcceptAward = this.rankAcceptAwardMapper.selectByRankIdAndUserid(rankId+"",userid+"");
            if(rankAcceptAward == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,"未获取到获奖信息");
            }
            if(!"2".equals(rankAcceptAward.getStatus()+"")){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,"未获取到获奖信息");
            }
            //修改订单状态为已收货
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",rankAcceptAward.getId());
            map.put("newStatus","3");
            int row = this.rankAcceptAwardMapper.updateRankAwardStatus(map);

            //修改rankMember状态为已完成
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("userId",userid);
            parameterMap.put("rankId",rankId);
            parameterMap.put("acceptaward","3");
            int updateRow = this.rankMembersMapper.updateRank(parameterMap);

            if(row > 0){
                return baseResp.ok();
            }
        }catch(Exception e){
            logger.error("user rank award confirm receipt error userid:{} rankId:{}",userid,rankId);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 结束榜单
     * @param currentTime
     * @return
     */
    @Override
    public BaseResp<Object> endRank(Date currentTime) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("currentTime",currentTime);
            map.put("isfinish",1);
            //查询结束的榜单
            List<Rank> rankList = this.rankMapper.selectWillEndRank(map);
            if(rankList == null || rankList.size() == 0){
                return baseResp.ok();
            }
            for(Rank rank:rankList){
                boolean flag =this.rankSortService.checkRankEnd(rank);
                if(!flag){
                    continue;
                }
                if(rank.getIscheck() != null && !"0".equals(rank.getIscheck())){
                    continue;
                }
            }
        }catch(Exception e){
            logger.error("end rank error currentTime;{}",currentTime);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 榜单结束 通知用户榜结束 包含给中奖用户发消息 给未中奖用户发消息
     * 该接口将不再校验榜单是否已结束,所以调用该接口前请确认该榜单已结束
     * @param rankId
     * @return
     */
    @Override
    public BaseResp<Object> rankEndNotice(Long rankId) {
        Rank rank = this.rankMapper.selectRankByRankid(rankId);
        return sendRankEndUserMsg(rank);
    }

    @Override
    public BaseResp<Object> rankEndNotice(Rank rank) {
        return sendRankEndUserMsg(rank);
    }

    /**
     * 删除进步时,1.修改用户在榜中发布的进步总条数,
     *          2.删除用户获得的赞和花影响的排名
     * @param rankId 榜单id
     * @param userId 用户id
     * @param likes 赞总数
     * @param flowers 花总数
     * @return
     */
    @Override
    public BaseResp<Object> updateUserRankMemberByImprove(Long rankId, Long userId, int likes, int flowers) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            //修改rankMember的用户进步条数
            RankMembers rankMembers = new RankMembers();
            rankMembers.setRankid(rankId);
            rankMembers.setUserid(userId);
            rankMembers.setIcount(-1);
            int row = this.rankMembersMapper.updateRankMemberState(rankMembers);
            if(row < 1) return baseResp;

            if(likes > 0){
                this.rankSortService.updateRankSortScore(rankId,userId, Constant.OperationType.cancleLike,likes);
            }
            if(flowers > 0){
                this.rankSortService.updateRankSortScore(rankId,userId, Constant.OperationType.flower,-flowers);
            }
            return baseResp.ok();
        }catch(Exception e){
            logger.error("update user rankMember by improve error rankId:{} userId:{} likes:{} flowers:{}",rankId,userId,likes,flowers);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 发布榜单
     * @param currentDateTime 系统当前时间
     * @return
     */
    @Override
    public BaseResp<Object> publishRank(Date currentDateTime) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            //查询哪些需要发布
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("checkStatus",Constant.RANKIMAGE_STATUS_4);//审核通过的
            map.put("isAuto",Constant.RANK_ISAUTO_TIME);//定时发布
            map.put("currentTime",currentDateTime);
            List<RankImage> rankImageList = this.rankImageMapper.selectPublishRank(map);
            if(rankImageList == null || rankImageList.size() == 0){
                return baseResp;
            }
            for(RankImage rankImage:rankImageList){
                BaseResp baseResp1 = this.publishRankImage(rankImage);
            }
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("publish rank error currentDateTime:{}",currentDateTime);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 获取榜单的获奖用户
     * @param rankid 榜单id
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> getWinningRankAwardUser(Long rankid,Long userid, Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            List<RankMembers> rankMembersList = this.rankMembersMapper.getWinningRankAwardUser(rankid,startNum,pageSize);
            if(rankMembersList == null || rankMembersList.size() == 0){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_07);
            }

            //查看该用户的领奖状态
            int showBtn = 0;//未登录/不在榜中

            for(RankMembers rankMembers:rankMembersList){
            	AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(rankMembers.getUserid()+"");
            	//获取好友昵称
                if (userid != null && userid != -1 && !Constant.VISITOR_UID.equals(userid)){
                    appUserMongoEntity.setNickname(this.friendService.getNickName(userid,rankMembers.getUserid()));
                    if(userid != null && userid.equals(rankMembers.getUserid())){
                        if(!"1".equals(rankMembers.getIswinning())){
                            showBtn = 2;//在榜中 未中奖
                        }else if("0".equals(rankMembers.getAcceptaward())){
                            showBtn = 3;//中奖 但未领取
                        }else{
                            showBtn = 4;//中奖 且已领奖
                        }
                    }
                }
                rankMembers.setAppUserMongoEntity(appUserMongoEntity);
                rankMembers.setAwardlevel(rankMembers.getRankAward().getAwardlevel());
            }
            baseResp.setData(rankMembersList);

            Map<String,Object> expandMap = new HashMap<String,Object>();
            expandMap.put("showBtn",showBtn);
            baseResp.setExpandData(expandMap);

            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("get winning rank award user error rankid:{} startNum:{} pageSize:{}",rankid,startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    private BaseResp<Object> sendRankEndUserMsg(Rank rank){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("rankId",rank.getRankid());
            map.put("status",1);
            List<RankMembers> rankMemberses = this.rankMembersMapper.selectRankMembers(map);
            if(rankMemberses == null || rankMemberses.size() == 0){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
            rank = rankMapper.selectRankByRankid(rank.getRankid());
            List<UserMsg> winningUserMsgList = new ArrayList<UserMsg>();
            List<Long> noWinningUserIdList = new ArrayList<Long>();
            for(RankMembers rankMember:rankMemberses){
                if("1".equals(rankMember.getIswinning()) && "3".equals(rankMember.getCheckstatus())){
                    UserMsg userMsg = createWinningUserMsg(rank,rankMember.getUserid(),
                            Long.parseLong(rankMember.getRankAward().getAwardid()));
                    if(userMsg != null) winningUserMsgList.add(userMsg);
                    continue;
                }
                noWinningUserIdList.add(rankMember.getUserid());
            }
            if(winningUserMsgList.size() > 0){
                int row = this.userMsgService.batchInsertUserMsg(winningUserMsgList);
                pushMsgToRankMember(winningUserMsgList,String.valueOf(rank.getRankid()));
            }
            if(noWinningUserIdList.size() > 0){
                UserMsg userMsg = createNoWinningUserMsg(rank);
                Boolean flag = this.userMsgService.batchInsertUserMsg(noWinningUserIdList,userMsg);
            }
            return baseResp.ok();
        }catch(Exception e){
            logger.error("rank end notice error rankId:{}",rank.getRankid());
            printException(e);
        }
        return baseResp;
    }

    private boolean pushMsgToRankMember(final List<UserMsg> list,final String rankid){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    UserMsg us = list.get(i);
                    //String status,String userId,String title,String content,String msgid,String tag
                    jPushService.pushMessage("消息标识",us.getUserid()+"","获奖消息",us.getRemark(),rankid,
                            Constant.JPUSH_TAG_COUNT_1502);

                }
            }
        });
        return true;
    }

    /**
     * 生成中奖用户的usermsg
     * @param rank 榜单
     * @param userId 用户id
     * @awardId
     * @return
     */
    private UserMsg createWinningUserMsg(Rank rank,Long userId,Long awardId){
        //查询用户中的奖项
        RankAward rankAward = this.rankAwardMapper.selectRankAwardByRankIdAndAwardId(rank.getRankid(),awardId);
        if(rankAward == null){
            return null;
        }
        String remark = "";
        if(null != rank){
        	if(!StringUtils.isBlank(rank.getRanktitle())){
        		remark = "恭喜您在龙榜'"+rank.getRanktitle()+"'中获得"+rankAward.getAwardnickname()+"，快去领奖吧！";
        	}else{
        		Rank rank1 = rankMapper.selectRankByRankid(rank.getRankid());
    	    	if(null != rank1){
    	    		remark = "恭喜您在龙榜'"+rank1.getRanktitle()+"'中获得"+rankAward.getAwardnickname()+"，快去领奖吧！";
    	    	}else{
    	    		remark = "恭喜您在龙榜中获得"+rankAward.getAwardnickname()+"，快去领奖吧！";
    	    	}
        	}
        }else{
        	remark = "恭喜您在龙榜中获得"+rankAward.getAwardnickname()+"，快去领奖吧！";
        }
        UserMsg userMsg = new UserMsg();
        userMsg.setUserid(userId);
        userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
        //mtype 0 系统消息     1 对话消息   2:@我消息   用户中奖消息在@我      未中奖消息在通知消息
        userMsg.setMtype("2");
        userMsg.setMsgtype("34");
        //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
        userMsg.setGtype("10");
        userMsg.setGtypeid(rank.getRankid());
        userMsg.setIsdel("0");
        userMsg.setIsread("0");
        userMsg.setCreatetime(new Date());
        userMsg.setUpdatetime(new Date());
//        userMsg.setSnsid(rank.getRankid());
        userMsg.setRemark(remark);
        userMsg.setTitle("加入的龙榜获奖通知");
        return userMsg;
    }

    /**
     * 生成rankMember未中奖的userMsg
     * @param rank
     * @return
     */
    public UserMsg createNoWinningUserMsg(Rank rank){
    	String remark = "";
    	if(null != rank && !StringUtils.isBlank(rank.getRankid().toString())){
    		if(!StringUtils.isBlank(rank.getRanktitle())){
    	    	remark = "您参与的龙榜\""+rank.getRanktitle()+"\"已结束,中奖名单已公布,快去围观吧！";
    	    }else{
    	    	Rank rank1 = rankMapper.selectRankByRankid(rank.getRankid());
    	    	if(null != rank1){
    	    		remark = "您参与的龙榜\""+rank1.getRanktitle()+"\"已结束,中奖名单已公布,快去围观吧！";
    	    	}else{
    	    		remark = "您参与的龙榜已结束,中奖名单已公布,快去围观吧！";	
    	    	}
    	    }
    	}else{
    		remark = "您参与的龙榜已结束,中奖名单已公布,快去围观吧！";
    	}
        UserMsg userMsg = new UserMsg();
        userMsg.setFriendid(Long.parseLong(Constant.SQUARE_USER_ID));
        //mtype 0 系统消息     1 对话消息   2:@我消息      用户中奖消息在@我      未中奖消息在通知消息
        userMsg.setMtype("0");
        userMsg.setMsgtype("22");
        //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
        userMsg.setGtype("10");
        userMsg.setIsdel("0");
        userMsg.setIsread("0");
        userMsg.setCreatetime(new Date());
        userMsg.setUpdatetime(new Date());
//        userMsg.setSnsid(rank.getRankid());
        userMsg.setGtypeid(rank.getRankid());
        userMsg.setRemark(remark);
        userMsg.setTitle("加入的龙榜结榜未获奖");
        return userMsg;
    }


    /**
     * 获取榜单的获奖公示
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> rankAwardList(Long userId,Integer startNum, Integer pageSize) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
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
                    resultMap.put("endtime",DateUtils.formatDate(rank.getEndtime()));
                    resultMap.put("rankinvolved",rank.getRankinvolved());//参与人数
                    resultMap.put("rankphotos",rank.getRankphotos());//榜单图片
                    initAwardResultMap(resultMap,rank.getRankid(),userId,true);
                    resultList.add(resultMap);
                }
                baseResp.setData(resultList);
            }
            baseResp.getExpandData().put("shareurl", AppserviceConfig.h5_share_rank_award);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("rank award list error startNum:{} pageSize:{}",startNum,pageSize);
            printException(e);
        }
        return baseResp;
    }

    /**
     * 查询单个榜单的获奖公示
     * @param rankid
     * @return
     */
    @Override
    public BaseResp<Object> onlyRankAward(Long rankid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            Rank rank = this.rankMapper.selectRankByRankid(rankid);
            Map<String,Object> resultMap = new HashMap<String,Object>();
            if(rank != null && "5".equals(rank.getIsfinish()) && "0".equals(rank.getIsdel())){
                resultMap.put("rankid",rank.getRankid());
                resultMap.put("ranktitle",rank.getRanktitle());
                resultMap.put("endtime",DateUtils.formatDate(rank.getEndtime()));
                resultMap.put("rankinvolved",rank.getRankinvolved());//参与人数
                resultMap.put("rankphotos",rank.getRankphotos());//榜单图片
                initAwardResultMap(resultMap,rank.getRankid(),null,false);
            }
            baseResp.setData(resultMap);
            baseResp.getExpandData().put("shareurl", AppserviceConfig.h5_share_rank_award);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select onlyRankAward error rankid:{}",rankid);
            printException(e);
        }
        return baseResp;
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
                if(searchUserNickName){
                    awardMap.put("usernickname",this.friendService.getNickName(userId,rankAwardRelease.getUserid()));
                }
                awardMap.put("awardcount",rankAwardRelease.getAwardcount());
                awardList.add(awardMap);
                rankAwardCount += rankAwardRelease.getAwardcount();
            }
            resultMap.put("rankawardcount",rankAwardCount);
            resultMap.put("rankawardList",awardList);
        }
    }

    /**
     * 获取榜单获奖详情
     * @param rankid
     * @return
     */
    @Override
    public BaseResp<Object> rankAwardDetail(Long rankid,Long userid) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Map<String,Object> resultMap = new HashMap<String,Object>();
            Rank rank = this.rankMapper.selectRankByRankid(rankid);
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }else if("0".equals(rank.getIsfinish())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_68,Constant.RTNINFO_SYS_68);
            }else if("1".equals(rank.getIsfinish())){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_67,Constant.RTNINFO_SYS_67);
            }
            resultMap.put("rankid",rank.getRankid());
            resultMap.put("ranktitle",rank.getRanktitle());
            resultMap.put("endtime",DateUtils.formatDate(rank.getEndtime()));
            resultMap.put("rankinvolved",rank.getRankinvolved());//参与人数
            resultMap.put("rankImage",rank.getRankphotos());

            List<Map<String,Object>> awardList = new ArrayList<Map<String,Object>>();//用于存放一等奖 二等奖
            //查询所有的中奖用户 按照中奖等级排列
            List<RankMembers> rankMemberses = this.rankMembersMapper.getWinningRankAwardUser(rankid,null,null);
            if(rankMemberses != null && rankMemberses.size() > 0){
                int awardLevel = -1;
                int awardCount = 0;
                Map<String,Object> awardMap = new HashMap<String,Object>();//用于存放 一等奖的详情
                List<Map<String,Object>> awards = new ArrayList<Map<String,Object>>();//用于存放一等奖中哪些用户中奖
                for(RankMembers rankMembers:rankMemberses){
                    if(awardLevel != rankMembers.getRankAward().getAwardlevel().intValue()){
                        if(awards.size() > 0){
                            awardMap.put("awardcount",awardCount);
                            awardMap.put("awardMembers",awards);
                            awardList.add(awardMap);
                            awardMap = new HashMap<String,Object>();
                            awards = new ArrayList<Map<String,Object>>();
                            awardCount = 0;
                            awardLevel = rankMembers.getRankAward().getAwardlevel().intValue();
                        }else{
                            awardLevel = rankMembers.getRankAward().getAwardlevel().intValue();
                        }
                        Award award = this.awardMapper.selectByPrimaryKey(Long.parseLong(rankMembers.getRankAward().getAwardid()));
                        RankAwardRelease rankAwardRelease = this.rankAwardReleaseMapper.selectByRankIdAndAwardId(rankid+"",rankMembers.getRankAward().getAwardid()+"");
                        awardMap.put("awardtitle",award.getAwardtitle());
                        awardMap.put("awardnickname", rankAwardRelease.getAwardnickname());
                        awardMap.put("awardphotos",award.getAwardphotos());
                        awardMap.put("awardprice",award.getAwardprice());
                    }
                    AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(rankMembers.getUserid()+"");

                    Map<String,Object> userDetail = new HashMap<String,Object>();
                    if(appUserMongoEntity != null){
                        userDetail.put("usernickname",appUserMongoEntity.getNickname());//用户昵称
                        userDetail.put("avatar",appUserMongoEntity.getAvatar());
                    }
                    userDetail.put("likes",rankMembers.getLikes());//赞的数量
                    userDetail.put("flowers",rankMembers.getFlowers());//花的数量
                    awards.add(userDetail);
                    awardCount ++;
                }
                awardMap.put("awardcount",awardCount);
                awardMap.put("awardMembers",awards);
                awardList.add(awardMap);

//            List<RankAwardRelease> rankAwardList = this.rankMembersMapper.rankMemberAwardList(rankid);
//            if(rankAwardList != null && rankAwardList.size() > 0){
//                Integer awardLevel = -1;
//                Integer awardCount = 0;
//                Map<String,Object> awardMap = new HashMap<String,Object>();//用于存放 一等奖的详情
//                List<Map<String,Object>> awards = new ArrayList<Map<String,Object>>();//用于存放一等奖中哪些用户中奖
//                for(RankAwardRelease rankAwardRelease:rankAwardList){
//                    if(awardLevel != rankAwardRelease.getAwardlevel()){
//                        if(awards.size() > 0){
//                            awardMap.put("awardcount",awardCount);
//                            awardMap.put("awardMembers",awards);
//                            awardList.add(awardMap);
//                            awardMap = new HashMap<String,Object>();
//                            awards = new ArrayList<Map<String,Object>>();
//                            awardCount = 0;
//                            awardLevel = rankAwardRelease.getAwardlevel();
//                        }
//                        awardMap.put("awardtitle",rankAwardRelease.getAward().getAwardtitle());
//                        awardMap.put("awardnickname", rankAwardRelease.getAwardnickname());
//                        awardMap.put("awardphotos",rankAwardRelease.getAward().getAwardphotos());
//                        awardMap.put("awardprice",rankAwardRelease.getAward().getAwardprice());
//                    }
//                    AppUserMongoEntity appUserMongoEntity = this.userMongoDao.getAppUser(rankAwardRelease.getUserid()+"");
//                    RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rankid,rankAwardRelease.getUserid());
//
//                    Map<String,Object> userDetail = new HashMap<String,Object>();
//                    if(appUserMongoEntity != null){
//                        userDetail.put("usernickname",appUserMongoEntity.getNickname());//用户昵称
//                        userDetail.put("avatar",appUserMongoEntity.getAvatar());
//                    }
//                    userDetail.put("likes",rankMembers.getLikes());//赞的数量
//                    userDetail.put("flowers",rankMembers.getFlowers());//花的数量
//                    awards.add(userDetail);
//                    awardCount ++;
//                }
//                awardMap.put("awardcount",awardCount);
//                awardMap.put("awardMembers",awards);
//                awardList.add(awardMap);
            }
            resultMap.put("awardList",awardList);
            baseResp.setData(resultMap);

            //查看该用户的领奖状态
            int showBtn = 0;//未登录
            if(userid != null){
                RankMembers rankMembers= this.rankMembersMapper.selectByRankIdAndUserId(rankid,userid);
                if(rankMembers == null){
                    showBtn = 1;//已登录 不在榜中
                }
                if(!"1".equals(rankMembers.getIswinning())){
                    showBtn = 2;//在榜中 未中奖
                }else if("0".equals(rankMembers.getAcceptaward())){
                    showBtn = 3;//中奖 但未领取
                }else{
                    showBtn = 4;//中奖 且已领奖
                }
            }
            Map<String,Object> expandMap = new HashMap<String,Object>();
            expandMap.put("showBtn",showBtn);
            baseResp.setExpandData(expandMap);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("rank award detail error rankId:{}",rankid);
            printException(e);
        }
        return baseResp;
    }

    private List<RankAwardRelease> selectRankAwardByRankidRelease(String rankid){
        List<RankAwardRelease> rankAwards = rankAwardReleaseMapper.selectListByRankid(rankid);
        for (RankAwardRelease rankAward : rankAwards){
            if(!StringUtils.isBlank(rankAward.getAwardid())){
                Award award = awardMapper.selectByPrimaryKey(Long.parseLong(rankAward.getAwardid()));
                rankAward.setAward(award != null?award:new Award());
            }
        }
        return rankAwards;
    }

    /**
     * 更改榜中的参榜人数
     * @param rankId 榜id
     * @param count 新增人数
     * @return
     */
    private boolean updateRankMemberCount(Long rankId, int count) {
        int updateRow = this.rankMapper.updateRankMemberCount(rankId,count);
        return updateRow > 0? true:false;
    }

    /**
     * 初始化新入榜的用户排名
     * @param rank 榜单信息
     * @param userId
     * @return
     */
    private boolean initRedisRankSort(Rank rank, Long userId) {
        long nowDate = new Date().getTime();
        double a = nowDate - rank.getCreatetime().getTime();
        double b = rank.getEndtime().getTime() - rank.getCreatetime().getTime();
        double ratio = NumberUtil.round(a/b,5);
        ratio = 1 - ratio;
        return springJedisDao.zAdd(Constant.REDIS_RANK_SORT+rank.getRankid(),userId+"",ratio);
    }


    @Override
    public BaseResp<Rank> selectRankDetailByRankid(Long userId,String rankId,Boolean queryCreateUser,Boolean queryAward) {
        BaseResp<Rank> baseResp = new BaseResp<Rank>();
        try {
            Map<String,Object> resultMap = new HashMap<String,Object>();
            Rank rank = rankMapper.selectRankByRankid(Long.parseLong(rankId));
            if(null != rank && null != rank.getAutotime()) {
                Date autotimeformate = DateUtils.formatDate(rank.getAutotime(), "yyyy-MM-dd HH:mm:ss");
                rank.setAutotime(DateUtils.formatDateTime1(autotimeformate));
            }
            if(rank == null){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            }
            rankSortService.checkRankEnd(rank);
            if(queryCreateUser != null && queryCreateUser){
                if (Constant.RANK_TYEP_APP.equals(rank.getRanktype())){
                    rank.setAppUserMongoEntity(userMongoDao.getAppUser(rank.getCreateuserid()+""));
                }
            }

            //只有当参榜人数满的时候,才获取可以挤掉的榜成员
            if(rank.getRankinvolved() >= rank.getRanklimite()) {
                //获取可以挤掉的用户数量
                int removeCount = getSureRemoveRankMemberCount(Long.parseLong(rankId));
                if (removeCount > 0) {
                    rank.setRankinvolved(rank.getRankinvolved() - removeCount);
                }
            }
            if(queryAward != null && queryAward ){
                rank.setRankAwards(selectRankAwardByRankidRelease(String.valueOf(rankId)));
            }

            if(rank.getRankcardid() != null){
                RankCard rankCard = this.rankCardMapper.selectByPrimaryKey(Integer.parseInt(rank.getRankcardid()));
                if(rankCard != null){
                    rankCard.setRankCardUrl(rankCard.getId().toString());
                    rank.setRankCard(rankCard);
                }
            }

            //pc端发榜
            if(Constant.RANK_SOURCE_TYPE_1.equals(rank.getSourcetype())){
                AppUserMongoEntity appUser = userMongoDao.getAppUser(rank.getCreateuserid());
                rank.setCreateuserid(rank.getCreateuserid());

                //封装pc榜主名片
                if(rank.getRankCard() == null){
                    RankCard rankCard = new RankCard();
                    rankCard.setCreateuserid(rank.getCreateuserid());
                    rankCard.setAdminname(appUser.getNickname());
                    rankCard.setAdminpic(appUser.getAvatar());
                    rankCard.setAdminbrief(appUser.getBrief());
                    //rankCard.setRankCardUrl(null);//pc端没有榜主名片id
                    rank.setRankCard(rankCard);
                }
            }

            int userRankMemberStatus = 0;//可参榜
            //用户是否可参榜
            userRankMemberStatus = getInsertUserRankMemberStatus(userId,rank);
            //是否已经关注榜单
            if(userId != null && !Constant.VISITOR_UID.equals(userId+"")){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("businessType","2");
                map.put("businessId",rankId);
                map.put("userId",userId);
                List<UserBusinessConcern> userBusinessConcern = this.userBusinessConcernMapper.findUserBusinessConcernList(map);
                if(userBusinessConcern.size() > 0){
                    resultMap.put("isfollow",true);
                }else{
                    resultMap.put("isfollow",false);
                }
            }else{
                resultMap.put("isfollow",false);
            }
            resultMap.put("userRankMemberStatus",userRankMemberStatus);

            //加载评论数
            BaseResp<Integer> commentResp = this.commonMongoService.selectCommentCountSum(rankId,"10", "");
            if(commentResp.getCode() == 0){
                resultMap.put("commentCount",commentResp.getData());
                rank.setCommentCount(commentResp.getData());
            }else{
                resultMap.put("commentCount","0");
                rank.setCommentCount(0);
            }

            //计算入榜截止时间
            Date endJoinTime = rank.getEndtime();
            if(StringUtils.isNotEmpty(rank.getJoinlastday()) && !"0".equals(rank.getJoinlastday())){
                endJoinTime = DateUtils.getBeforeDateTime(rank.getEndtime(),Integer.parseInt(rank.getJoinlastday()));
            }
            rank.setEndjointime(endJoinTime);

            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            baseResp.setData(rank);
            baseResp.setExpandData(resultMap);
        } catch (Exception e) {
            logger.error("selectRankByRankid error and msg={}",e);
        }
        return baseResp;
    }

    /**
     * 获取用户是否可参榜的状态
     * @param userId
     * @param rank
     * @return
     */
    private int getInsertUserRankMemberStatus(Long userId, Rank rank) {
        int userRankMemberStatus = 0;
        if("5".equals(rank.getIsfinish())){
            userRankMemberStatus = 4;//榜已结束 查看
        }else if(!"0".equals(rank.getIsfinish()) && !"1".equals(rank.getIsfinish())){
            userRankMemberStatus = 5;//榜单获奖结果审核中
        }
        if(userRankMemberStatus > 0){
            return userRankMemberStatus;
        }
        if(userId == null || Constant.VISITOR_UID.equals(userId+"")){
            return userRankMemberStatus;
        }
        //查看是否已经加入榜单
        RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(rank.getRankid(),userId);
        if(rankMembers != null && rankMembers.getStatus() == 1){
            userRankMemberStatus = 1;//已入榜
        }else if(rankMembers != null && rankMembers.getStatus() == 0){
            userRankMemberStatus = 2;//已入榜 待审核
        }
        if(userRankMemberStatus == 0 && rank.getRankinvolved() >= rank.getRanklimite()){
            userRankMemberStatus = 3;//已满,无法参榜
        }
        return userRankMemberStatus;
    }

    @Override
    public BaseResp<Page<RankMembers>> selectRankMemberList(RankMembers rankMembers, Integer pageNo, Integer pageSize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        Page<RankMembers> page = new Page<>(pageNo,pageSize);
        try {
            AppUserMongoEntity user = rankMembers.getAppUserMongoEntity();
            List<AppUserMongoEntity> users = null;
            if (null != user) {
                users = userMongoDao.getAppUsers(user);
            }
            rankMembers.setAppUserMongoEntities(users);
            int totalcount = rankMembersMapper.selectCount(rankMembers);
            Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,pageSize*(pageNo-1),pageSize);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
                if (null != rankMembers1.getRankAward() && null != rankMembers1.getRankAward().getAwardid()){
                    rankMembers1.getRankAward().setAward(awardMapper.selectByPrimaryKey(Long.parseLong(rankMembers1.getRankAward().getAwardid())));
                }
                if ("1".equals(rankMembers1.getCheckstatus())
                        || "2".equals(rankMembers1.getCheckstatus())) {
                    rankMembers1.setAwardid(-1L);
                    rankMembers1.setAwardlevel(-1);
                    rankMembers1.setRankAward(new RankAward());
                }
                if("1".equals(rank.getIsfinish())){
                    long sortNum = springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+rankMembers1.getRankid(),rankMembers1.getUserid()+"");
                    rankMembers1.setSortnum(Integer.parseInt(sortNum+""));
                }
            }
            page.setTotalCount(totalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rankmembers list rankid={} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Page<RankMembers>> selectRankAllMemberList(UserInfo userInfo, Integer pageno, Integer pagesize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        Page<RankMembers> page = new Page<>(pageno,pagesize);
        try {
            List<UserInfo> userInfos = new ArrayList<>();
            if (!isNullUser(userInfo)){
                userInfos = userInfoMapper.selectList(userInfo,null,null,null,null);
            }
            RankMembers rankMembers = new RankMembers();
            rankMembers.setIsfashionman("1");
            List<AppUserMongoEntity> appUserMongoEntities = new ArrayList<>();
            for (UserInfo userInfo1 : userInfos){
                AppUserMongoEntity user = new AppUserMongoEntity();
                user.setId(String.valueOf(userInfo1.getUserid()));
                appUserMongoEntities.add(user);
            }
            rankMembers.setAppUserMongoEntities(appUserMongoEntities);
            int totalcount = rankMembersMapper.selectCount(rankMembers);
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,"1",pagesize*(pageno-1),pagesize);
            for (RankMembers rankMembers1 : rankMemberses){
                UserInfo userInfo1 = userInfoMapper.selectByPrimaryKey(rankMembers1.getUserid());
                Rank rank = rankMapper.selectRankByRankid(rankMembers1.getRankid());
                rankMembers1.setUserInfo(userInfo1);
                rankMembers1.setRank(rank);
            }
            page.setTotalCount(totalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select all rank member list for pc is error:",e);
        }
        return baseResp;
    }



    @Override
    public BaseResp<Page<RankMembers>> selectRankFashionManList(String rankId, Integer pageno, Integer pagesize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        Page<RankMembers> page = new Page<>(pageno,pagesize);
        try {
            RankMembers rankMembers = new RankMembers();
            rankMembers.setIsfashionman("1");
            rankMembers.setRankid(Long.parseLong(rankId));
            List<AppUserMongoEntity> appUserMongoEntities = new ArrayList<>();
            int totalcount = rankMembersMapper.selectCount(rankMembers);
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,"1",pagesize*(pageno-1),pagesize);
            for (RankMembers rankMembers1 : rankMemberses){
                UserInfo userInfo1 = userInfoMapper.selectByPrimaryKey(rankMembers1.getUserid());
                Rank rank = rankMapper.selectRankByRankid(rankMembers1.getRankid());
                rankMembers1.setUserInfo(userInfo1);
                rankMembers1.setRank(rank);
                AppUserMongoEntity user = new AppUserMongoEntity();
                user.setId(String.valueOf(rankMembers1.getUserid()));
                appUserMongoEntities.add(user);
            }
            rankMembers.setAppUserMongoEntities(appUserMongoEntities);
            page.setTotalCount(totalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectRankFashionManList for pc is error:",e);
        }
        return baseResp;
    }


    private boolean isNullUser(UserInfo userInfo){

        if (!StringUtils.isBlank(userInfo.getNickname())){
            return false;
        }
        if (!StringUtils.isBlank(userInfo.getUsername())){
            return false;
        }
        if (!StringUtils.isBlank(userInfo.getSex())){
            return false;
        }
        if (null != userInfo.getScreatetime()){
            return false;
        }
        if (null != userInfo.getEcreatetime()){
            return false;
        }
        if (0 != userInfo.getSgrade()){
            return false;
        }
        if (0 != userInfo.getEgrade()){
            return false;
        }
        return true;
    }


    @Override
    public BaseResp<Page<RankMembers>> selectRankMemberWaitCheckList(RankMembers rankMembers, Integer pageNo, Integer pageSize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers || null == rankMembers.getRankid()){
            return baseResp;
        }
        Page<RankMembers> page = new Page<>(pageNo,pageSize);
        try {
            int totalcount = selectRankMemberWaitCount(rankMembers);
            AppUserMongoEntity user = rankMembers.getAppUserMongoEntity();
            List<AppUserMongoEntity> users = null;
            if (null != user) {
                users = userMongoDao.getAppUsers(user);
            }
            rankMembers.setAppUserMongoEntities(users);
            List<RankMembers> rankMemberses = rankMembersMapper.selectWaitCheckList
                    (rankMembers,pageSize*(pageNo-1),pageSize,totalcount);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));

            }
            Integer searchTotalcount = rankMembersMapper.selectWaitCheckListCount(rankMembers,totalcount);
            page.setTotalCount(searchTotalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select wait check rankmembers list rankid={} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

    /**
     * 查询榜单待审核成员数
     * @param rankMembers
     * @return
     */
    private int selectRankMemberWaitCount(RankMembers rankMembers){
        Rank rank = rankMapper.selectRankByRankid(rankMembers.getRankid());
        //榜单参与人数
        int rankmnum = rank.getRankinvolved();
        //榜单设置奖品数
        int awardcount = getRankAwardCount(String.valueOf(rankMembers.getRankid()));
        //审核通过数
        int okcount = rankMembersMapper.selectCountByStatusAndCheckstatus(rankMembers.getRankid(),1,"3");
        //未审核数
        int waitcount = rankMembersMapper.selectCountByStatusAndCheckstatus(rankMembers.getRankid(),1,"0");
        if (awardcount > rankmnum){
            awardcount = rankmnum;
        }
        if (waitcount <= awardcount - okcount){
            return waitcount;
        } else {
            return (awardcount - okcount)<0?0:(awardcount - okcount);
        }
    }

    private boolean isSubRankMemberCheckResult(String rankid){
        RankMembers rankMembers = new RankMembers();
        rankMembers.setRankid(Long.parseLong(rankid));

        //榜单设置奖品数
        int awardcount = getRankAwardCount(String.valueOf(rankMembers.getRankid()));
        //审核通过数
        rankMembers.setCheckstatus("3");
        int okcount = rankMembersMapper.selectCount(rankMembers);
        //未审核数
        rankMembers.setCheckstatus("0");
        int waitcount = rankMembersMapper.selectCount(rankMembers);

        //通过数等于奖品数
        if (okcount >= awardcount){
            return true;
        } else if (okcount < awardcount){
            if (waitcount==0){
                return true;
            }
        }

        return false;
    }


    /**
     * 发布奖品
     * @param rankid
     */
    private void publishRankAward(String rankid){
        RankMembers rankMembers = new RankMembers();
        rankMembers.setRankid(Long.parseLong(rankid));

        //获取榜单全部成员列表
        List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,null,null);
        //获得榜单奖品
        List<RankAward> rankAwards = rankAwardMapper.selectListByRankid(rankid);
        int awardcount = 0;
        int tempcount = 0;
        for(int i=0;i<rankAwards.size();i++){
            RankAward rankAward = rankAwards.get(i);
            for (int j = tempcount ; j < rankMemberses.size();j++){
                if (awardcount >= rankAward.getAwardrate()){
                    break;
                }
                RankMembers rkmember = rankMemberses.get(j);
                if ("3".equals(rkmember.getCheckstatus())){
                    awardcount++;
                    tempcount++;
                    rankMembers.setUserid(rkmember.getUserid());
                    rankMembers.setIswinning("1");
                    String awardcode = codeDao.getCode(null);
                    rankMembers.setReceivecode(awardcode);
                    RankAward rankAward1 = new RankAward();
                    rankAward1.setAwardlevel(i+1);
                    rankAward1.setAwardid(rankAward.getAwardid());
                    rankMembers.setRankAward(rankAward1);
                    rankMembersMapper.updateRankMemberState(rankMembers);
                }
                if ("1".equals(rkmember.getCheckstatus())
                        || "2".equals(rkmember.getCheckstatus())){
                    tempcount++;
                    //rankMembers延用了前面的值，且无法更新
//                    rankMembers.setUserid(rkmember.getUserid());
//                    rankMembers.setIswinning("2");
                    rkmember.setIswinning("2");
                    rkmember.setIcount(0);//不改变进步数
//                    rankMembersMapper.updateRankMemberState(rankMembers);
                    rankMembersMapper.updateRankMemberState(rkmember);
                }
            }
            awardcount = 0;
        }

    }


    private boolean insertRankAcceptAwardInfo(String rankid){

        RankMembers rankMembers = new RankMembers();
        rankMembers.setRankid(Long.parseLong(rankid));
        Rank rank = rankMapper.selectRankByRankid(Long.parseLong(rankid));
        List<RankAcceptAward> rankAcceptAwards = new ArrayList<>();
        //获取榜单全部成员列表
        List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,null,null);

        for (int j = 0 ; j < rankMemberses.size();j++){
            RankMembers rkmember = rankMemberses.get(j);
            if ("3".equals(rkmember.getCheckstatus()) && "1".equals(rkmember.getIswinning())){
                //生成获奖订单
                RankAcceptAward rankAcceptAward = new RankAcceptAward();
                rankAcceptAward.setRankid(Long.parseLong(rankid));
                rankAcceptAward.setUserid(rkmember.getUserid());
                rankAcceptAward.setReceivecode(rkmember.getReceivecode());
                if(null == rkmember.getRankAward() || rkmember.getRankAward().getAwardid() == null){
                    continue;
//                    rkmember.setRankAward(rankAwardMapper.selectRankAwardByRankIdAndAwardId(rkmember.getRankid(),rkmember.getAwardid()));
                }
                rankAcceptAward.setAwardlevel(rkmember.getRankAward().getAwardlevel());
                rankAcceptAward.setAwardid(Long.parseLong(rkmember.getRankAward().getAwardid()));
                rankAcceptAward.setSortnum(rkmember.getSortnum());
                rankAcceptAward.setAwardnickname(rkmember.getRankAward().getAwardnickname());
                rankAcceptAward.setCreatedate(new Date());
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(rkmember.getUserid()));
                if (null != appUserMongoEntity){
                    rankAcceptAward.setUsername(appUserMongoEntity.getUsername());
                }
                Award award = awardMapper.selectByPrimaryKey(rkmember.getAwardid());
                if (null != award){
                    rankAcceptAward.setAwardcateid(String.valueOf(award.getAwardcateid()));
                }

                if (null != rank){
                    rankAcceptAward.setRanktitle(rank.getRanktitle());
                }
                RankAwardRelease awardRelease = rankAwardReleaseMapper.selectByRankIdAndAwardId(rankid,String.valueOf(rkmember.getAwardid()));
                if (null != awardRelease){
                    rankAcceptAward.setAwardnickname(awardRelease.getAwardnickname());
                }
                rankAcceptAwards.add(rankAcceptAward);
            }
        }

        //添加领奖信息
        try {
            rankAcceptAwardService.insertAcceptAwardInfoBatch(rankAcceptAwards);
            return true;
        } catch (Exception e) {
            logger.error("insert rank rankid={} accept award info is error:",rank.getRankid(),e);
        }
        return false;
    }



    private int getRankAwardCount(String rankid){
        int awardcont = 0;
        try {
            List<RankAward> rankAwards = rankAwardMapper.selectListByRankid(rankid);
            if (null != rankAwards && rankAwards.size() != 0){
                for (RankAward rankAward : rankAwards){
                    awardcont += rankAward.getAwardrate();
                }
            }
        } catch (Exception e) {
            logger.error("get rankaward rankid={} count is error:",rankid,e);
        }
        return awardcont;
    }

    @Override
    public BaseResp<RankMembers> selectRankMemberInfo(String rankid, String userid) {
        BaseResp<RankMembers> baseResp = new BaseResp<>();
        RankMembers rankMembers = new RankMembers();
        try {
            rankMembers.setRankid(Long.parseLong(rankid));
            rankMembers.setUserid(Long.parseLong(userid));
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,null,null);
            if (null != rankMemberses && rankMemberses.size() != 0){
                RankMembers rankMembers1 = rankMemberses.get(0);
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
                baseResp = BaseResp.ok();
                baseResp.setData(rankMemberses.get(0));
            }
        } catch (NumberFormatException e) {
            logger.error("select rankmember info is error:",e);
        }
        return baseResp;
    }
    
    @Override
    public BaseResp<Object> updateStatus(String status,String userid,String rankid,String improveid){
        BaseResp<Object> reseResp = new BaseResp<>();
        try {
        	Rank rank = rankMapper.selectByPrimaryKey(Long.parseLong(rankid));
            //status 0：未处理 1： 删除微进步    2： 下榜微进步  3： 通过其他方式已处理  4: 已忽略
            if ("1".equals(status)) {
                //删除
                improveService.removeImprove(userid, improveid, "2", rankid);
                Improve improve = improveService.selectImproveByImpidMuc(Long.parseLong(improveid), 
                		userid, "2", rankid);
				String remark = Constant.MSG_IMP_DEL_MODEL;
				if(null != improve){
					if(!StringUtils.isBlank(improve.getBrief())){
						if(improve.getBrief().length()>=20){
							//抓取内容20个字
							String brief = improve.getBrief().substring(0, 20);
							remark = remark.replace("n", "(" + brief + ")");
						}else{
							remark = remark.replace("n", "(" + improve.getBrief() + ")");
						}
					}else{
						remark = remark.replace("n", "");
					}
				}
//                String remark = Constant.MSG_QUITRANK_IMP_MODEL.replace("n", rank.getRanktitle());
            	//mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
				//						22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
				//						25:订单发货N天后自动确认收货    26：实名认证审核结果
				//						27:工作认证审核结果      28：学历认证审核结果
				//						29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
				//						32：创建的龙榜/教室/圈子被选中推荐  
				//						40：订单已取消 41 榜中进步下榜   
				// 						42.榜单公告更新   43:后台反馈回复消息    45:榜中删除成员进步)
            	//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
				//			10：榜中  11 圈子中  12 教室中  13:教室批复作业   14:反馈 15 关注
            	userMsgService.insertMsg(Constant.SQUARE_USER_ID, userid, 
            			improveid, "2", 
            			rankid, remark, "0", "45", "榜中删除成员进步", 0, "", "",AppserviceConfig.h5_helper);
            }
            if ("2".equals(status)) {
                //businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
                //下榜
                improveService.removeImproveFromBusiness(improveid, rankid, "2");
                
				String remark = Constant.MSG_RANKIMP_QUIT_MODEL;
				if(null != rank){
					remark = remark.replace("n", rank.getRanktitle());
				}else{
					remark = remark.replace("n", "");
				}
//            	String remark = Constant.MSG_QUITRANK_QUIT_MODEL.replace("n", rank.getRanktitle());
            	//mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
				//						22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
				//						25:订单发货N天后自动确认收货    26：实名认证审核结果
				//						27:工作认证审核结果      28：学历认证审核结果
				//						29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
				//						32：创建的龙榜/教室/圈子被选中推荐  
				//						40：订单已取消 41 榜中进步下榜   
				// 						42.榜单公告更新   43:后台反馈回复消息    45:榜中删除成员进步)
            	//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
				//			10：榜中  11 圈子中  12 教室中  13:教室批复作业   14:反馈 15 关注
            	userMsgService.insertMsg(Constant.SQUARE_USER_ID, userid, 
            			improveid, "2", 
            			rankid, remark, "0", "41", "榜中进步下榜", 0, "", "",AppserviceConfig.h5_helper);
                
            }
            reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        } catch (Exception e) {
            logger.error("remove rankImprove status={},userid={} ,rankid={},improveid={} ",status,userid,rankid,improveid);
        }
        return reseResp;
    }

    @Override
    public BaseResp<List<RankMembers>> selectRankMemberListForApp(String rankid, Integer startNo, Integer pageSize) {
        BaseResp<List<RankMembers>> baseResp = new BaseResp<>();
        RankMembers rankMembers = new RankMembers();
        try {
            rankMembers.setRankid(Long.parseLong(rankid));
            List<RankMembers> rankMemberses = rankMembersMapper.selectList(rankMembers,null,startNo,pageSize);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
            }
            baseResp = BaseResp.ok();
            baseResp.setData(rankMemberses);
        } catch (NumberFormatException e) {
            logger.error("select rankmember list rankid={} is error:",rankid,e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Page<RankMembers>> selectRankAcceptAwardListPage(RankMembers rankMembers, Integer pageno, Integer pagesize) {
        BaseResp<Page<RankMembers>> baseResp = new BaseResp<>();
        if (null == rankMembers){
            return baseResp;
        }
        Page<RankMembers> page = new Page<>(pageno,pagesize);
        try {
            //搜索用户
            AppUserMongoEntity user = rankMembers.getAppUserMongoEntity();
            List<AppUserMongoEntity> users = null;
            if (null != user) {
                users = userMongoDao.getAppUsers(user);
            }
            rankMembers.setAppUserMongoEntities(users);


            if (null != rankMembers.getRankAward()){

                //搜索榜单
                Rank ranksearch = rankMembers.getRankAward().getRank();
                if (null != ranksearch){
                    List<Rank> ranks = rankMapper.selectListWithPage(ranksearch,null,null);
                    rankMembers.setRanks(ranks);
                }

                //搜索类目
                Award award = rankMembers.getRankAward().getAward();
                if (null != award){
                    List<Award> awards = awardMapper.selectAwardList(award,null,null);
                    rankMembers.setAwards(awards);
                }

            }


            int totalcount = rankMembersMapper.selectCount(rankMembers);
            List<RankMembers> rankMemberses = rankMembersMapper.selectRankAcceptAwardList(rankMembers,pagesize*(pageno-1),pagesize);
            for (RankMembers rankMembers1 : rankMemberses){
                rankMembers1.setAppUserMongoEntity(userMongoDao.getAppUser(String.valueOf(rankMembers1.getUserid())));
                if (null != rankMembers1.getRankAward() && null != rankMembers1.getRankAward().getAwardid()){
                    rankMembers1.getRankAward().setRank(rankMapper.selectRankByRankid(rankMembers1.getRankid()));
                    rankMembers1.getRankAward().setAward(awardMapper.selectByPrimaryKey(Long.parseLong(rankMembers1.getRankAward().getAwardid())));
                }
            }
            page.setTotalCount(totalcount);
            page.setList(rankMemberses);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rankmembers list rankid={} is error:",rankMembers.getRankid(),e);
        }
        return baseResp;
    }

}
