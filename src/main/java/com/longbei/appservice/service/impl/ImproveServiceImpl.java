package com.longbei.appservice.service.impl;


import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Perfect;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.constant.Constant_table;
import com.longbei.appservice.common.service.mq.send.QueueMessageSendService;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.ImproveMongoDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import com.netflix.discovery.converters.Auto;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 进步业务操作实现类
 *
 * @author luye
 * @create 2017-01-19 上午11:32
 **/
@Service("improveService")
public class ImproveServiceImpl implements ImproveService{

    private static Logger logger = LoggerFactory.getLogger(ImproveServiceImpl.class);

    @Autowired
    private ImproveMapper improveMapper;
    @Autowired
    private ImproveCircleMapper improveCircleMapper;
    @Autowired
    private ImproveClassroomMapper improveClassroomMapper;
    @Autowired
    private ImproveRankMapper improveRankMapper;
    @Autowired
    private ImproveGoalMapper improveGoalMapper;
    @Autowired
    private QueueMessageSendService queueMessageSendService;
    @Autowired
    private CommentMongoService commentMongoService;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private TimeLineDao timeLineDao;
    @Autowired
    private UserBehaviourService userBehaviourService;
    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private ImpGoalPerdayMapper impGoalPerdayMapper;
    @Autowired
    private UserCollectMapper userCollectMapper;
    @Autowired
    private ImpAllDetailMapper impAllDetailMapper;
    @Autowired
    private ImproveMongoDao improveMongoDao;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private TimeLineDetailDao timeLineDetailDao;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private ImproveTopicMapper improveTopicMapper;
    @Autowired
    private UserImpCoinDetailService userImpCoinDetailService;
    @Autowired
    private CircleMemberService circleMemberService;
    @Autowired
    private RankMembersMapper rankMembersMapper;
    @Autowired
    private ImpAwardMapper impAwardMapper;
//    @Autowired
//    private ClassroomMapper classroomMapper;
    @Autowired
    private UserMsgService userMsgService;
    @Autowired
    private SnsFriendsMapper snsFriendsMapper;
    @Autowired
    private SnsFansMapper snsFansMapper;
    @Autowired
    private RankService rankService;
    @Autowired
    private UserGoalMapper userGoalMapper;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;
    @Autowired
    private RankSortService rankSortService;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private CommentMongoDao commentMongoDao;
    @Autowired
    private CommentLowerMongoDao commentLowerMongoDao;
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private UserCardMapper userCardMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private CircleMembersMapper circleMembersMapper;

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @SuppressWarnings("unchecked")
	@Override
    public BaseResp<Object> insertImprove(String userid, String brief,
                                 String pickey, String filekey,
                                 String businesstype,String businessid, String ptype,
                                 String ispublic, String itype, String pimpid,String picattribute) {

        Improve improve = new Improve();
        improve.setImpid(idGenerateService.getUniqueIdAsLong());
        improve.setUserid(Long.parseLong(userid));
        improve.setBrief(brief);
        improve.setPickey(pickey);
        improve.setSourcekey(filekey);
        improve.setBusinesstype(businesstype);
        improve.setPtype(ptype);
        improve.setIspublic(ispublic);
        improve.setItype(itype);
        improve.setIsmainimp("1");
        improve.setPicattribute(picattribute);
        Date date = new Date();
        improve.setCreatetime(date);
        improve.setUpdatetime(date);
        if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
        }else{
            improve.setBusinessid(Long.parseLong(businessid));
        }

        //添加进步之前的过滤
        BaseResp<Object> baseResp = insertImproveFilter(improve,businesstype);
       if(ResultUtil.fail(baseResp)){
            return baseResp;
        }
        boolean isok = false;
        switch (businesstype){
            case Constant.IMPROVE_SINGLE_TYPE:
                isok = insertImproveSingle(improve);
                break;
            case Constant.IMPROVE_RANK_TYPE:
                isok = insertImproveForRank(improve);
                break;
            case Constant.IMPROVE_CLASSROOM_TYPE:
                improve.setIsresponded("0");
                isok = insertImproveForClassroom(improve);
                break;
            case Constant.IMPROVE_CIRCLE_TYPE:
                isok = insertImproveForCircle(improve);
                break;
            case Constant.IMPROVE_GOAL_TYPE:

                isok = insertImproveForGoal(improve);
                break;
            case Constant.IMPROVE_CLASSROOM_REPLY_TYPE:
                {// 5：教室批复作业
                    improve.setPimpid(Long.parseLong(pimpid));
                    //0 不是批复。1 是批复
                    improve.setIsresponded("1");
                    isok = insertImproveForClassroomReply(improve);
                    
                    //教室批复作业---当成一条主评论信息(学员可以评论老师的批复)
                    Comment comment = new Comment();
         			comment.setContent("批复作业");
         			comment.setCreatetime(new Date());
         			comment.setBusinesstype("5");
         			comment.setBusinessid(businessid);
         			comment.setUserid(userid);
         			comment.setFriendid("0");
         			comment.setImpid(pimpid);
         			commentMongoService.insertComment(comment);
         			
                    ImproveClassroom improveClassroom = improveClassroomMapper.selectByPrimaryKey(Long.parseLong(pimpid));
                    if(null != improveClassroom){
                        //批复完成后添加消息
                    	//1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)  
//                		//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//                		//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
                    	//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
            			//10：榜中  11 圈子中  12 教室中  13:教室批复作业
                    	userMsgService.insertMsg(userid, improveClassroom.getUserid().toString(), improve.getImpid().toString(), 
                    			"13", businessid, "批复作业", "2", "12", "教室作业批复", 0, "", "");
//                        addReplyMsg(improveClassroom.getUserid(), Long.parseLong(businessid), Long.parseLong(userid), improve.getImpid());
                    }
                }
                break;
            default:
                isok = false;
                break;
        }

        //进步发布完成之后
        if(isok && !Constant.IMPROVE_CLASSROOM_REPLY_TYPE.equals(businesstype)){
            try{
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(userid));//此处通过id获取用户信息
                baseResp = userBehaviourService.pointChange(userInfo,"DAILY_ADDIMP",ptype,Constant.USER_IMP_COIN_ADDIMPROVE,improve.getImpid(),0);
                //发布完成之后redis存储i一天数量信息
                String key = Constant.RP_USER_PERDAY+Constant.PERDAY_ADD_IMPROVE+userid+"_"+DateUtils.getDate();
                springJedisDao.increment(key,businesstype,1);
                springJedisDao.expire(key,Constant.CACHE_24X60X60);
                userBehaviourService.userSumInfo(Constant.UserSumType.addedImprove,Long.parseLong(userid),null,0);

                String message = improve.getImpid() +
                        "," + businesstype +
                        "," + improve.getBusinessid() +
                        "," + improve.getUserid() +
                        "," + DateUtils.formatDateTime1(improve.getCreatetime());
                queueMessageSendService.sendAddMessage(Constant.MQACTION_IMPROVE,
                        Constant.MQDOMAIN_IMP_ADD,message);
            }catch (Exception e){
                logger.error("insertImprove userid={},brief={},pickey={},filekey={},businesstype={}," +
                        "businessid={},ptype={},ispublic={},itype={},pimpid={}",
                        userid,brief,pickey, filekey, businesstype,businessid, ptype,ispublic,itype,pimpid, e);
            }

        }
        baseResp.setData(improve.getImpid());
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }
    
    /**
	 * @author yinxc
	 * 批复完成后添加消息
	 * 2017年3月6日
	 * @param businessid 教室业务id
	 * 
	 */
//    private void addReplyMsg(long userid, long businessid, long friendid, long impid){
//    	//mtype  0 系统消息(通知消息，等级提升消息) 
//		//1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)  
//		//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//		//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
//    	UserMsg msg = new UserMsg();
//    	msg.setUserid(userid);
//    	msg.setMsgtype("12");
//    	msg.setMtype("2");
//    	msg.setFriendid(friendid);
//    	msg.setSnsid(impid);
//    	Classroom classroom = classroomMapper.selectByPrimaryKey(businessid);
//    	if(null != classroom){
//    		String remark = Constant.MSG_CLASSROOM_REPLY_MODEL.replace("n", classroom.getClasstitle());
//        	msg.setRemark(remark);
//    	}
//    	//gtype  0 零散 1 目标中 2 榜中 3圈子中 4 教室中
//    	msg.setGtype("4");
//    	msg.setIsdel("0");
//    	msg.setIsread("0");
//    	msg.setCreatetime(new Date());
//    	userMsgMapper.insertSelective(msg);
//    }
    
    /**
	 * @author yinxc
	 * 添加教室批复作业
	 * 2017年3月6日
	 */
    private boolean insertImproveForClassroomReply(Improve improve) {
        int res = 0;
        try {
            res = improveClassroomMapper.insertSelective(improve);
            return res > 0 ? true : false;
        } catch (Exception e) {
            logger.error("insert classroom immprove reply:{}", JSONObject.fromObject(improve).toString(),e);
        }
        return false;
    }
    
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public boolean insertImproveSingle(Improve improve) {

        int res = 0;
        try {
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE);
        } catch (Exception e) {
            logger.error("insert sigle immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res > 0){
            return true;
        }
        return false;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public boolean insertImproveForCircle(Improve improve) {

        int res = 0;
        try {
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_CIRCLE);
        } catch (Exception e) {
            logger.error("insert circle immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res > 0){
            //给该用户在圈子中发表的进步数量加1
            this.circleMemberService.updateCircleMemberIcount(improve.getUserid(),improve.getBusinessid(),1);

            return true;
        }
        return false;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public boolean insertImproveForClassroom(Improve improve) {


        int res = 0;
        try {
            res = improveClassroomMapper.insertSelective(improve);
        } catch (Exception e) {
            logger.error("insert classroom immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res != 0){
            return true;
        }
        return false;
    }
    /**
     *  @author luye
     *  @change lixb 插入进步 修改rank_members中的进步数量 修改主进步为非主进步
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public boolean insertImproveForRank(Improve improve) {
        logger.info("insert improve fro rank rankid={}",improve.getBusinessid());

        int res = 0;
        try {
//            improve.setRankid(idGenerateService.getUniqueIdAsLong());
            res = improveMapper.updateRankMainImprove(improve.getBusinessid(),improve.getUserid());
            logger.info("update rank main improve");
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_RANK);
            logger.info("insert imporve = {} ", JSON.toJSONString(improve));
            logger.info("insert imrpve for rank is result={}",res);
            RankMembers rankMembers = new RankMembers();
            rankMembers.setRankid(improve.getBusinessid());
            rankMembers.setUserid(improve.getUserid());
            rankMembers.setUpdatetime(new Date());
            rankMembers.setIcount(1);
            //更新rankmember中的updatetime
            res = rankMembersMapper.updateRankMemberState(rankMembers);
        } catch (Exception e) {
            logger.error("insert rank immprove:{} is error:{}", "",e);
        }
        if(res != 0){


            String tempnum = springJedisDao.get("rankid"+improve.getBusinessid()+
                    "userid"+improve.getUserid()+DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
            int num = StringUtils.isEmpty(tempnum)?0:Integer.parseInt(tempnum);
            if (num == 0){
                springJedisDao.set("rankid"+improve.getBusinessid()+
                        "userid"+improve.getUserid()+DateUtils.formatDate(new Date(),"yyyy-MM-dd"),"1",1000*60*60*48);
            } else {
                springJedisDao.increment("rankid"+improve.getBusinessid()+
                        "userid"+improve.getUserid()+DateUtils.formatDate(new Date(),"yyyy-MM-dd"),1);
            }
            return true;
        }
        return false;
    }


    private boolean canInsertRankImprovePerday(Long userid,Long rankid,Rank rank){
        String tempnum = springJedisDao.get("rankid"+rankid+"userid"+userid+DateUtils.formatDate(new Date(),"yyyy-MM-dd"));
        int num = StringUtils.isEmpty(tempnum)?0:Integer.parseInt(tempnum);
        if (num < Integer.parseInt(rank.getMaximprovenum())){
            return true;
        }
        return false;
    }

    private boolean canInsertRankImproveTotal(Long userid,Long rankid,Rank rank){
        if(rank.getMaxtotalimprovenum() == null){//如果为空,代表不限制在榜中发表的最大进步数量
            return true;
        }
        RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rank.getRankid(),userid);
        int num = StringUtils.isEmpty(rankMembers.getIcount()+"")?0:rankMembers.getIcount();
        if (num < Integer.parseInt(rank.getMaxtotalimprovenum())){
            return true;
        }
        return false;
    }


    /**
     *  @author luye
     *  @desp  更新目标中进步之后 主进步状态需要更新
     *         目标中的进步条数需要更新
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public boolean insertImproveForGoal(Improve improve) {

        int res = 0;
        try {
            //更新
            res = improveMapper.updateGolaMainImprove(improve.getBusinessid(),improve.getUserid());
            res = userGoalMapper.updateIcount(improve.getBusinessid(),improve.getUserid(),"1");
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_GOAL);
        } catch (Exception e) {
            logger.error("insert goal immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res != 0){
            /**
             * 目标进步发布成功 更新redis中缓存进步id  更新目标进步统计表中的进步id
             *
             */
            updateGoalPerDay(improve);
            return true;
        }
        return false;
    }

    private void updateGoalPerDay(Improve improve){
        long n = springJedisDao.sAdd(Constant.RP_IMPROVE_NDAY+DateUtils.getDate("yyyy-MM-dd")+improve.getBusinessid()
                ,improve.getImpid()+"");
        springJedisDao.expire(Constant.RP_IMPROVE_NDAY+DateUtils.getDate("yyyy-MM-dd")+improve.getBusinessid(),Constant.CACHE_24X60X60X2);
        if(n > 0){
//            boolean exist = impGoalPerdayMapper.selectByUidAndDate();
//            if(exist){ //如果存在 更新
//
//            }else{ //不存在  插入
//
//            }
        }
    }

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public Improve selectImproveByImpid(Long impid,String userid,
                                 String businesstype,String businessid) {

        Improve improve = null;
        try {
            improve = selectImprove(impid,userid,businesstype,businessid,null,null);
        } catch (Exception e) {
            logger.error("select improve is error:",e);
        }
        initImproveInfo(improve,Long.parseLong(userid));
        return improve;
    }

    /**
     * 查询进步核心信息
     * @param impid
     * @param userid
     * @param businesstype
     * @param businessid
     * @return
     */
    @Override
    public Improve selectImproveByImpidMuc(Long impid,String userid,
                                        String businesstype,String businessid) {

        Improve improve = null;
        try {
            improve = selectImprove(impid,userid,businesstype,businessid,null,null);
        } catch (Exception e) {
            logger.error("select improve is error:",e);
        }
        return improve;
    }

    /**
     * 查询公开的 未被删除的进步
     * @param impid
     * @param userid
     * @param businesstype
     * @param businessid
     * @return
     */
    private Improve selectValidateImprove(Long impid,String userid,
                                          String businesstype,String businessid){
        Improve improve = null;
        try {
            improve = selectImprove(impid,userid,businesstype,businessid,"1","1");
        } catch (Exception e) {

        }

        return improve;
    }

    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:57
     *  @update 2017/3/8 下午3:57
     */
    private Improve selectImprove(Long impid,String userid,
                    String businesstype,String businessid, String isdel,String ispublic){
        Improve improve = null;
        try {
            switch (businesstype){
                case Constant.IMPROVE_SINGLE_TYPE:
                    improve = improveMapper.selectByPrimaryKey(impid,null,Constant_table.IMPROVE,isdel,ispublic);
                    break;
                case Constant.IMPROVE_RANK_TYPE:
                    improve = improveMapper.selectByPrimaryKey(impid,businessid,Constant_table.IMPROVE_RANK,isdel,ispublic);
                    break;
                case Constant.IMPROVE_CLASSROOM_TYPE:
                    improve = improveMapper.selectByPrimaryKey(impid,businessid,Constant_table.IMPROVE_CLASSROOM,isdel,ispublic);
                    break;
                case Constant.IMPROVE_CIRCLE_TYPE:
                    improve = improveMapper.selectByPrimaryKey(impid,businessid,Constant_table.IMPROVE_CIRCLE,isdel,ispublic);
                    break;
                case Constant.IMPROVE_GOAL_TYPE:
                    improve = improveMapper.selectByPrimaryKey(impid,businessid,Constant_table.IMPROVE_GOAL,isdel,ispublic);
                    break;
                default:
                    improve = improveMapper.selectByPrimaryKey(impid,null,Constant_table.IMPROVE,isdel,ispublic);
                    break;
            }

        } catch (Exception e) {
            logger.error("select improve by userid:{}" +
                            "id:{} businesstype:{} businessid:{} is error:{}",
                    userid,impid,businesstype,businessid,e);
        }
        return improve;
    }


    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public  BaseResp<List<Improve>> selectRankImproveList(String userid, String rankid,String sift,
                                               String orderby, int pageNo, int pageSize,String lastdate) {
        List<Improve> improves = new ArrayList<>();
        try {
            switch (sift){
                //全部
                case Constant.IMPROVE_LIST_ALL:

                    break;
                //关注
                case Constant.IMPROVE_LIST_FANS:

                    break;
                //好友
                case Constant.IMPROVE_LIST_FRIEND:

                    break;
                //熟人
                case Constant.IMPROVE_LIST_ACQUAINTANCE:

                    break;
                default:
                    break;
            }
            int flowerscore = 10;
            int likescore = 1;
            Rank rank = rankService.selectByRankid(Long.parseLong(rankid));
            if ("1".equals(orderby)){
                if (null != rank){
                    flowerscore = rank.getFlowerscore();
                    likescore = rank.getLikescore();
                }
            }
            improves = improveMapper.selectListByRank(rankid,orderby,
                    flowerscore,likescore,pageNo,pageSize,StringUtils.isBlank(lastdate)?null:lastdate);
            if ("0".equals(orderby)){
                if (null != rank){
                    flowerscore = rank.getFlowerscore();
                    likescore = rank.getLikescore();
                }
            }
            initImproveListOtherInfo(userid,improves);
            initSortInfo(rank,improves);
            if(null == improves){
                improves = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("selectRankImproveList userid:{} rankid:{} is error:{}",userid,rankid,e);
        }
        BaseResp<List<Improve>> improvesList = new BaseResp<>();
        improvesList.setData(improves);
        //获取评论总数
        int taltalCommentNum = 0;
        BaseResp<Integer> resp = commentMongoService.selectCommentCountSum(rankid, "10", null);
        if (ResultUtil.isSuccess(resp)){
            taltalCommentNum = resp.getData();
        }
        improvesList.getExpandData().put("taltalCommentNum",taltalCommentNum);
        return improvesList;
    }

    private void initSortInfo(Rank rank,List<Improve> improves){
        if("1".equals(rank.getIsfinish())){//进行中
            for (Improve improve : improves){
                if(improve == null){
                    continue;
                }
                Long sort = this.springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+rank.getRankid(),String.valueOf(improve.getUserid()));
                improve.setSortnum(sort.intValue());
            }
        }
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<Improve> selectRankImproveListByDate(String userid, String rankid,String sift,String orderby, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {

            switch (sift){
                //全部
                case Constant.IMPROVE_LIST_ALL:

                    break;
                //关注
                case Constant.IMPROVE_LIST_FANS:

                    break;
                //好友
                case Constant.IMPROVE_LIST_FRIEND:

                    break;
                //熟人
                case Constant.IMPROVE_LIST_ACQUAINTANCE:

                    break;
                default:
                    break;
            }

            improves = improveMapper.selectListByBusinessid
                    (rankid, Constant_table.IMPROVE_RANK,"1",null,orderby,null,pageNo,pageSize);
            initImproveListOtherInfo(userid,improves);
            if(null == improves){
                improves = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("selectRankImproveListByDate userid:{} rankid:{} is error:{}",userid,rankid,e);
        }
        return improves;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<Improve> selectCircleImproveList(String userid, String circleid,String sift,String orderby, int pageNo, int pageSize) {
        List<Improve> improves = new ArrayList<Improve>();
        try {
            switch (sift){
                //全部
                case Constant.IMPROVE_LIST_ALL:

                    break;
                //关注
                case Constant.IMPROVE_LIST_FANS:

                    break;
                //好友
                case Constant.IMPROVE_LIST_FRIEND:

                    break;
                //熟人
                case Constant.IMPROVE_LIST_ACQUAINTANCE:

                    break;
                default:
                    break;
            }
            improves = improveMapper.selectListByBusinessid
                    (circleid, Constant_table.IMPROVE_CIRCLE,null,null,orderby,null,pageNo,pageSize);
            initImproveListOtherInfo(userid,improves);
        } catch (Exception e) {
            logger.error("selectCircleImproveList userid:{} circleid:{} is error:{}",userid,circleid,e);
        }
        return improves;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<Improve> selectCircleImproveListByDate(String userid, String circleid,String sift,String orderby, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            switch (sift){
                //全部
                case Constant.IMPROVE_LIST_ALL:

                    break;
                //关注
                case Constant.IMPROVE_LIST_FANS:

                    break;
                //好友
                case Constant.IMPROVE_LIST_FRIEND:

                    break;
                //熟人
                case Constant.IMPROVE_LIST_ACQUAINTANCE:

                    break;
                default:
                    break;
            }
            improves = improveMapper.selectListByBusinessid
                    (circleid, Constant_table.IMPROVE_CIRCLE,"1",null,orderby,null,pageNo,pageSize);
            initImproveListOtherInfo(userid,improves);
        } catch (Exception e) {
            logger.error("selectCircleImproveListByDate userid:{} circleid:{} is error:{}",userid,circleid,e);
        }
        return improves;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<Improve> selectClassroomImproveList(String userid, String classroomid,String sift,String orderby, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            switch (sift){
                //全部
                case Constant.IMPROVE_LIST_ALL:

                    break;
                //关注
                case Constant.IMPROVE_LIST_FANS:

                    break;
                //好友
                case Constant.IMPROVE_LIST_FRIEND:

                    break;
                //熟人
                case Constant.IMPROVE_LIST_ACQUAINTANCE:

                    break;
                default:
                    break;
            }
            improves = improveMapper.selectListByBusinessid
                    (classroomid, Constant_table.IMPROVE_CLASSROOM,null, null, orderby, null,pageNo, pageSize);
            initImproveListOtherInfo(userid,improves);
            replyImp(improves, userid, classroomid);
        } catch (Exception e) {
            logger.error("selectClassroomImproveList userid:{} classroomid:{} is error:{}",userid,classroomid,e);
        }
        return improves;
    }
    
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public List<Improve> selectClassroomImproveListByDate(String userid, String classroomid,String sift,String orderby, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            switch (sift){
                //全部
                case Constant.IMPROVE_LIST_ALL:

                    break;
                //关注
                case Constant.IMPROVE_LIST_FANS:

                    break;
                //好友
                case Constant.IMPROVE_LIST_FRIEND:

                    break;
                //熟人
                case Constant.IMPROVE_LIST_ACQUAINTANCE:

                    break;
                default:
                    break;
            }

            improves = improveMapper.selectListByBusinessid
                    (classroomid, Constant_table.IMPROVE_CLASSROOM, "1",null, orderby, null,pageNo, pageSize);
            initImproveListOtherInfo(userid,improves);
            replyImp(improves, userid, classroomid);
        } catch (Exception e) {
            logger.error("selectClassroomImproveListByDate userid:{} classroomid:{} is error:{}",userid,classroomid,e);
        }
        return improves;
    }

    //批复信息
  	private void replyImp(List<Improve> improves, String userid, String classroomid){
  		Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
//  		List<String> list = new ArrayList<>();
//  		if(null != classroom){
//  			list = userCardMapper.selectUseridByCardid(classroom.getCardid());
//  		}
  		UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
  		if(null != improves && improves.size()>0){
  			for (Improve improve : improves) {
  				String isreply = "0";
  				//获取教室微进步批复作业列表
  				List<ImproveClassroom> replyList = improveClassroomMapper.selectListByBusinessid(improve.getBusinessid(), improve.getImpid());
  				if(null != replyList && replyList.size()>0){
  					//已批复
  					isreply = "1";
  					ImproveClassroom improveClassroom = replyList.get(0);
                    AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(improveClassroom.getUserid()));
                    ReplyImprove replyImprove = new ReplyImprove(improveClassroom.getImpid(), improveClassroom.getItype(), 
                    		improveClassroom.getBrief(), improveClassroom.getPickey(), 
                    		improveClassroom.getUserid(), improveClassroom.getCreatetime());
                    replyImprove.setAppUserMongoEntity(appUserMongo);
  					improve.setReplyImprove(replyImprove);
  				}
  				if(!"1".equals(isreply)){
  					//判断当前用户是否是老师
					if(userCard.getUserid() != Long.parseLong(userid)){
						isreply = "2";
					}
  				}
  				improve.setIsreply(isreply);
  				
  			}
  		}
  	}
  	
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public List<Improve> selectGoalImproveList(String userid, String goalid,String orderby, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            improves = improveMapper.selectListByBusinessid
                    (goalid, Constant_table.IMPROVE_GOAL,null,null,orderby,null,pageNo,pageSize);
            initImproveListOtherInfo(userid,improves);
        } catch (Exception e) {
            logger.error("selectGoalImproveList userid:{} goalid:{} is error:{}",userid,goalid,e);
        }
        return improves;
    }
    
    /**
     *  获取目标中进步Count
     * @param userid
     * @param goalid
     * @return
     */
	@Override
	public int selectCountGoalImprove(String userid, String goalid) {
		// TODO Auto-generated method stub
		return 0;
	}
    
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp<Object> removeImprove(String userid,String improveid,
                                 String businesstype,String businessid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        boolean isok = false;
        try {
        	Improve improves = selectImprove(Long.parseLong(improveid),userid,businesstype,businessid,null,null);
        	if(null != improves){
        		if(!userid.equals(improves.getUserid().toString())){
        			return new BaseResp(Constant.STATUS_SYS_112, Constant.RTNINFO_SYS_112);
        		}
        	}
            switch (businesstype){
                case Constant.IMPROVE_SINGLE_TYPE:
                    baseResp = removeSingleImprove(userid,improveid);
                    break;
                case Constant.IMPROVE_RANK_TYPE:
                    baseResp = removeRankImprove(userid,businessid,improveid);
                    break;
                case Constant.IMPROVE_CLASSROOM_TYPE:
                    baseResp = removeClassroomImprove(userid,businessid,improveid);
                    break;
                case Constant.IMPROVE_CIRCLE_TYPE:
                    baseResp = removeCircleImprove(userid,businessid,improveid);
                    break;
                case Constant.IMPROVE_GOAL_TYPE:
                    baseResp = removeGoalImprove(userid,businessid,improveid);
                    break;
                default:
                    baseResp = removeSingleImprove(userid,improveid);
                    break;
            }
            if (ResultUtil.isSuccess(baseResp)){
                //将收藏了该进步的用户进步状态修改为已删除
                deleteUserCollectImprove("0",improveid);
                //将该进步设为取消推荐
                List<Long> list = new ArrayList<Long>();
                list.add(Long.parseLong(improveid));
//                timeLineDetailDao.updateRecommendImprove(list,businesstype,"0");
                improveMapper.updateImproveRecommend(getTableNameByBusinessType(businesstype),list,"0");

                timeLineDetailDao.deleteImprove(Long.parseLong(improveid),userid);
                Improve improve = selectImproveByImpid(Long.parseLong(improveid),userid,businesstype,businessid);
                userBehaviourService.userSumInfo(Constant.UserSumType.removedImprove,
                        Long.parseLong(userid),improve,0);
            }
        } catch (Exception e) {
            logger.error("remove improve is error:",e);
        }
        return baseResp;
    }

    /**
     * 将收藏了进步的状态修改为已删除
     * @param ctype 0.进步 1.其他
     * @param improveid
     */
    private boolean deleteUserCollectImprove(String ctype, String improveid) {
        int row = this.userCollectMapper.deleteUserCollectImprove(ctype,improveid);
        return true;
    }

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp<Object> removeSingleImprove(String userid, String improveid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        int res = 0;
        try {
            res = improveMapper.remove(userid,improveid);

        } catch (Exception e) {
            logger.error("remove single immprove: improveid:{} userid:{} is error:{}",
                    improveid,userid,e);
        }
        if(res != 0){
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            baseResp = BaseResp.ok();
            return baseResp;
        }
        return baseResp;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp<Object> removeRankImprove(String userid, String rankid, String improveid) {
        //查询该榜单,校验该榜单是否已经结束
        BaseResp<Object> baseResp = new BaseResp<>();
        Rank rank = this.rankMapper.selectRankByRankid(Long.parseLong(rankid));
        if(rank == null){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_613,Constant.RTNINFO_SYS_613);
            return baseResp;
        }
        if(!"0".equals(rank.getIsfinish()) && !"1".equals(rank.getIsfinish())){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_619,Constant.RTNINFO_SYS_619);
            return baseResp;
        }

        int res = 0;
        Improve improve = selectImprove(Long.parseLong(improveid),userid,Constant.IMPROVE_RANK_TYPE,rankid,null,null);
//        Improve improve = selectImproveByImpid(Long.parseLong(improveid),userid,Constant.IMPROVE_RANK_TYPE,rankid);
        try {
            res = improveRankMapper.remove(userid,rankid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: rankid:{} improveid:{} userid:{} is error:{}",
                    rankid,improveid,userid,e);
        }
        if(res != 0){
            //清除数据
            clearDirtyData(improve);
            springJedisDao.increment("rankid"+improve.getBusinessid()+
                    "userid"+improve.getUserid()+DateUtils.formatDate(new Date(),"yyyy-MM-dd"),-1);
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            baseResp = BaseResp.ok();
            return baseResp;
        }
        return baseResp;
    }

    //进步删除之后清理脏数据
    private void clearDirtyData(Improve improve){
        int flower = improve.getFlowers();
        int like = improve.getLikes();
        String tableName = getTableNameByBusinessType(improve.getBusinesstype());
        String sourceTableName = getSourecTableNameByBusinessType(improve.getBusinesstype());
        switch (improve.getBusinesstype()){
            case Constant.IMPROVE_GOAL_TYPE:
                if(improve.getIsmainimp().equals("1")){
                    improveMapper.chooseMainImprove(improve.getBusinessid(),improve.getUserid(),tableName,"businessid");
                }
                //更新赞 花
                improveMapper.afterDelSubImp(improve.getBusinessid(),improve.getUserid(),flower,like,sourceTableName,"goalid");
                break;
            case Constant.IMPROVE_RANK_TYPE:
                if(improve.getIsmainimp().equals("1")){
                    improveMapper.chooseMainImprove(improve.getBusinessid(),improve.getUserid(),tableName,"businessid");
                }
                //更新赞 花 进步条数
                improveMapper.afterDelSubImp(improve.getBusinessid(),improve.getUserid(),flower,like,sourceTableName,"rankid");
                //更新榜中进步条数
//                rankMembersMapper.updateRankImproveCount(improve.getBusinessid(),improve.getUserid(),-1);
                //更新redis中排名by lixb
                rankSortService.updateRankSortScore(improve.getBusinessid(),
                    improve.getUserid(),Constant.OperationType.like,-like);
                rankSortService.updateRankSortScore(improve.getBusinessid(),
                        improve.getUserid(),Constant.OperationType.flower,-flower);
                break;
            default:
                break;
        }
    }


    @Override
    public BaseResp<Object> removeFinishedRankImprove(String userid, String rankid, String improveid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        int res = 0;
        Improve improve = selectImprove(Long.parseLong(improveid),userid,Constant.IMPROVE_RANK_TYPE,rankid,null,null);
        try {
            res = improveRankMapper.remove(userid,rankid,improveid);
        } catch (Exception e) {
            logger.error("remove finishedrank immprove: rankid:{} improveid:{} userid:{} is error:{}",
                    rankid,improveid,userid,e);
        }
        if(res != 0){
            //清除数据
            clearFinishedRankDirtyData(improve);
            springJedisDao.increment("rankid"+improve.getBusinessid()+
                    "userid"+improve.getUserid()+DateUtils.formatDate(new Date(),"yyyy-MM-dd"),-1);
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            //将收藏了该进步的用户进步状态修改为已删除
            deleteUserCollectImprove("0",improveid);
            timeLineDetailDao.deleteImprove(Long.parseLong(improveid),userid);
            userBehaviourService.userSumInfo(Constant.UserSumType.removedImprove,
                    Long.parseLong(userid),improve,0);
            baseResp = BaseResp.ok();

        }

        return baseResp;
    }

    //删除已结束的榜进步之后清理脏数据  不更新榜中排名
    private void clearFinishedRankDirtyData(Improve improve){
        int flower = improve.getFlowers();
        int like = improve.getLikes();
        String tableName = getTableNameByBusinessType(improve.getBusinesstype());
        String sourceTableName = getSourecTableNameByBusinessType(improve.getBusinesstype());
        if(improve.getIsmainimp().equals("1")){
            improveMapper.chooseMainImprove(improve.getBusinessid(),improve.getUserid(),tableName,"businessid");
        }
        //更新赞 花 进步条数
        improveMapper.afterDelSubImp(improve.getBusinessid(),improve.getUserid(),flower,like,sourceTableName,"rankid");
    }

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp<Object> removeCircleImprove(String userid, String circleid, String improveid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        int res = 0;
        try {
            res = improveCircleMapper.remove(userid,circleid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: circleid:{} improveid:{} userid:{} is error:{}",
                    circleid,improveid,userid,e);
        }
        if(res > 0){
            //更改用户在圈子中的进步条数
            this.circleMemberService.updateCircleMemberIcount(Long.parseLong(userid),Long.parseLong(circleid),-1);

            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            baseResp = BaseResp.ok();
            return baseResp;
        }
        return baseResp;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp<Object> removeClassroomImprove(String userid, String classroomid, String improveid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        int res = 0;
        try {
            res = improveClassroomMapper.remove(userid,classroomid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: classroomid:{} improveid:{} userid:{} is error:{}",
                    classroomid,improveid,userid,e);
        }
        if(res != 0){
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            baseResp = BaseResp.ok();
            return baseResp;
        }
        return baseResp;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public BaseResp<Object> removeGoalImprove(String userid, String goalid, String improveid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        int res = 0;
//        Improve improve = selectImproveByImpid(Long.parseLong(improveid),userid,goalid,Constant.IMPROVE_GOAL_TYPE);
        Improve improve = selectImprove((Long.parseLong(improveid)),userid,Constant.IMPROVE_GOAL_TYPE,goalid,null,null);
        try {
            res = improveGoalMapper.remove(userid,goalid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: goalid:{} improveid:{} userid:{} is error:{}",
                    goalid,improveid,userid,e);
        }
        if(res != 0){
            //删除成功之后
            clearDirtyData(improve);
            springJedisDao.sRem(Constant.RP_IMPROVE_NDAY+goalid,improveid+DateUtils.getDate("yyyy-MM-dd"));
            long n = springJedisDao.sCard(Constant.RP_IMPROVE_NDAY+goalid);

            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            baseResp = BaseResp.ok();
            return baseResp;
        }
        return baseResp;
    }


    @Override
    public BaseResp<List<Improve>> selectOtherImproveList(String userid, String targetuserid,
                                                          Date lastdate, int pagesize) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        SnsFriends snsFriends =  snsFriendsMapper.selectByUidAndFid(Long.valueOf(userid),Long.valueOf(targetuserid), "0");
        int ispublic = 2;
        if (null != snsFriends){
            ispublic = 1;
        } else {
            ispublic = 2;
        }  //0 私密 1 好友 2 公开
        try {
            List<Improve> list = selectImproveListByUser(targetuserid,null,
                    Constant.TIMELINE_IMPROVE_SELF,lastdate,pagesize,ispublic);
            AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(targetuserid);
            //获取好友昵称
			String remark = userRelationService.selectRemark(Long.parseLong(userid), Long.parseLong(targetuserid), "0");
			if(!StringUtils.isBlank(remark)){
				appUserMongoEntity.setNickname(remark);
			}
            initUserRelateInfo(Long.parseLong(userid),appUserMongoEntity);
            if (null != list && list.size() != 0){
                for (Improve improve : list){
                    //初始化是否 点赞 送花 送钻 收藏
                    initIsOptionForImprove(userid+"",improve);
                    improve.setAppUserMongoEntity(appUserMongoEntity);
                }
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(list);
        } catch (Exception e) {
            logger.error("select other user improve targetuserid={} list is error:",targetuserid,e);
        }
        return baseResp;
    }

    /**
     * 获取我的进步列表(根据lastdate时间获取当天的进步列表)
     * @param userid  用户id
     * @param ctype  0--广场 1--我的 2--好友，关注，熟人 3-好友 4-关注 5-熟人
     * @param lastdate  最后一条时间
     * @param pagesize  每页显示条数
     * @return
     */
	@Override
	public List<Improve> selectImproveListByUserDate(String userid, String ptype,String ctype, Date searchDate, Date lastdate, int pagesize) {
		List<TimeLine> timeLines = timeLineDao.selectTimeListByUserAndTypeDate(userid,ctype, searchDate,lastdate,pagesize);
        List<Improve> improves = new ArrayList<>();
        for (int i = 0; i < timeLines.size() ; i++){
            TimeLine timeLine = timeLines.get(i);
            TimeLineDetail timeLineDetail = timeLine.getTimeLineDetail();
            Improve improve = new Improve();
            improve.setImpid(timeLineDetail.getImproveId());
            improve.setBrief(timeLineDetail.getBrief());
            improve.setPickey(timeLineDetail.getPhotos());
            improve.setFilekey(timeLineDetail.getFileKey());
            improve.setSourcekey(timeLineDetail.getSourcekey());
            improve.setItype(timeLineDetail.getItype());
            improve.setPicattribute(timeLineDetail.getPicattribute());
            improve.setCreatetime(DateUtils.parseDate(timeLineDetail.getCreatedate()));
            improve.setAppUserMongoEntity(timeLineDetail.getUser());
            improve.setUserid(timeLineDetail.getUser().getUserid());
            improve.setBusinessid(timeLineDetail.getBusinessid());
            if(!StringUtils.isBlank(timeLine.getIspublic()+"")){
                improve.setIspublic(timeLine.getIspublic()+"");
            }else{
                improve.setIspublic("2");
            }

            improve.setBusinesstype(timeLineDetail.getBusinesstype());
            initImproveInfo(improve,Long.parseLong(userid));
            //初始化 赞 花 数量
            improve.setFlowers(timeLineDetail.getFlowers());
           //            initImproveLikeAndFlower(improve);
            //初始化进步用户信息
            initImproveUserInfo(improve,Long.parseLong(userid));
            improves.add(improve);
        }
        return improves;
    }





    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:58
     *  @update 2017/3/8 下午3:58
     */
    @Override
    public List<Improve> selectImproveListByUser(String userid,String ptype,
                                                 String ctype,Date lastdate,int pagesize,Integer ispublic) {

        long l = System.currentTimeMillis();
        List<TimeLine> timeLines = timeLineDao.selectTimeListByUserAndType
                (userid,ptype,ctype,lastdate,pagesize,ispublic);
        long w = System.currentTimeMillis();
        logger.info("select time line time={}",w-l);
        List<Improve> improves = new ArrayList<>();
        Long uid = Long.parseLong(userid);
        String friendids = getFriendIds(uid);
        String funids = getFansIds(uid);
        Map<String, String> map = userRelationService.selectRemarkImpLine(Long.parseLong(userid));
        for (int i = 0; i < timeLines.size() ; i++){
            try {
                TimeLine timeLine = timeLines.get(i);
                TimeLineDetail timeLineDetail = timeLine.getTimeLineDetail();
                if (null == timeLineDetail){
                    continue;
                }
                Improve improve = new Improve();
                improve.setImpid(timeLineDetail.getImproveId());
                improve.setBrief(timeLineDetail.getBrief());
                improve.setPickey(timeLineDetail.getPhotos());
                improve.setFilekey(timeLineDetail.getFileKey());
                improve.setSourcekey(timeLineDetail.getSourcekey());
                improve.setItype(timeLineDetail.getItype());
                improve.setLikes(getLikeFromRedis(String.valueOf(timeLineDetail.getImproveId()),
                        timeLineDetail.getBusinessid()+"",timeLineDetail.getBusinesstype()));
                improve.setFlowers(timeLineDetail.getFlowers());
                improve.setIspublic(timeLineDetail.getIspublic());
                improve.setPicattribute(timeLineDetail.getPicattribute());
                improve.setCreatetime(DateUtils.parseDate(timeLineDetail.getCreatedate()));
                String businessType = timeLine.getBusinesstype();
                if(StringUtils.isBlank(businessType)){
                    improve.setBusinesstype("0");
                }else{
                    improve.setBusinesstype(businessType);
                }
                improve.setBusinessid(timeLine.getBusinessid());
                improve.setPtype(timeLine.getPtype());
                AppUserMongoEntity user = timeLineDetail.getUser();
                //获取好友昵称
                String remark = "";
                if(!map.isEmpty()){
                	remark = map.get(userid + "_" + user.getId() + "_value");
                }
//                String remark = userRelationService.selectRemark(Long.parseLong(userid), Long.parseLong(user.getId()));
                if(StringUtils.isNotBlank(remark)){
                    user.setNickname(remark);
                }
                improve.setAppUserMongoEntity(user);
                if(!Constant.VISITOR_UID.equals(userid)){
                    initUserRelateInfo(uid,timeLineDetail.getUser(),friendids,funids);
                    initImproveInfo(improve,uid);
                }
                //初始化 赞 花 数量
//                initImproveLikeAndFlower(improve);
                improves.add(improve);
            } catch (Exception e) {
                logger.error("select time line userid={} list is error:",userid,e);
            }
        }
        return improves;
    }

    /**
     * 能否发布进步过滤
     * @author:luye
     * @param improve
     * @param improvetype
     * @return
     * @author:luye
     * @date update 02月10日
     */
    private BaseResp insertImproveFilter(Improve improve,String improvetype){
        BaseResp baseResp = new BaseResp();
        switch (improvetype){
            case Constant.IMPROVE_SINGLE_TYPE:
                baseResp.initCodeAndDesp();
                break;
            case Constant.IMPROVE_CIRCLE_TYPE:
                baseResp.initCodeAndDesp();
                break;
            case Constant.IMPROVE_CLASSROOM_TYPE:
                baseResp.initCodeAndDesp();
                break;
            case Constant.IMPROVE_CLASSROOM_REPLY_TYPE:
                baseResp.initCodeAndDesp();
                break;
            case Constant.IMPROVE_GOAL_TYPE:
                UserGoal userGoal = userGoalMapper.selectByGoalId(improve.getBusinessid());
                if (null == userGoal){
                     baseResp.initCodeAndDesp(Constant.STATUS_SYS_58,Constant.RTNINFO_SYS_58);
                }
                if (userGoal.getUserid().longValue() != improve.getUserid().longValue()){
                     baseResp.initCodeAndDesp(Constant.STATUS_SYS_59,Constant.RTNINFO_SYS_59);
                }else {
                    baseResp.initCodeAndDesp();
                }

                break;
            case Constant.IMPROVE_RANK_TYPE: {
                    Rank rank = null;
                    try {
                        logger.info("select rank by rankid={}", improve.getBusinessid());
                        rank = rankMapper.selectRankByRankid(improve.getBusinessid());
                        logger.info("select rank result : {} ", rank);
                    } catch (Exception e) {
                        logger.error("select rank by rankid={} is error:", improve.getBusinessid(), e);
                    }
                    if (null != rank) {
                        logger.info("select rank is not null rank={}", JSON.toJSONString(rank));
                        if (!"1".equals(rank.getIsfinish())) {
                            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_69,Constant.RTNINFO_SYS_69);
                        }
                        if (!canInsertRankImprovePerday(improve.getUserid(), improve.getBusinessid(), rank)) {
                            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_617, Constant.RTNINFO_SYS_617);
                        }
                        if (!canInsertRankImproveTotal(improve.getUserid(), improve.getBusinessid(), rank)) {
                            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_621, Constant.RTNINFO_SYS_621);
                        }
                        return baseResp.initCodeAndDesp();
                    } else {
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_616, Constant.RTNINFO_SYS_616);
                    }
                }
                break;
            default:
                break;
        }
        return baseResp;
    }


    /**
     * 初始化进步附加信息
     * @param improves
     * @author luye
     */
    public void initImproveListOtherInfo(String userid,List<Improve> improves){
        if(null == improves || 0 == improves.size()){
            return;
        }
        for (Improve improve : improves){
            if(improve == null){
                continue;
            }
            improve.setLikes(getLikeFromRedis(String.valueOf(improve.getImpid()),
                    improve.getBusinessid()+"",improve.getBusinesstype()));
            //初始化评论数量
            initImproveCommentInfo(improve);
            //初始化点赞，送花，送钻简略信息
            initLikeFlowerDiamondInfo(improve);
            //初始化进步用户信息
            initImproveUserInfo(improve,(userid != null && !"-1".equals(userid))?Long.parseLong(userid):null);
            //初始化是否 点赞 送花 送钻 收藏
            initIsOptionForImprove(userid,improve);
        }
    }

    /**
     * 初始化用户关系信息
     */
    private void initUserRelateInfo(Long userid,AppUserMongoEntity apuser){
        if(apuser == null || userid == null || userid == -1){
            apuser.setIsfans("0");
            apuser.setIsfriend("0");
            return ;
        }
        if(null != apuser){
        	if(userid.equals(apuser.getUserid())){
                apuser.setIsfans("1");
                apuser.setIsfriend("1");
                return;
            }
        }
        initFriendInfo(userid,apuser);
        initFanInfo(userid,apuser);
    }

    private void initUserRelateInfo(Long userid,AppUserMongoEntity apuser,String friendids,String funids){
        if(userid == null || userid == -1){
            apuser.setIsfans("0");
            apuser.setIsfriend("0");
            return ;
        }
        if(userid.equals(apuser.getUserid())){
            apuser.setIsfans("1");
            apuser.setIsfriend("1");
            return;
        }
        if(StringUtils.isNotEmpty(friendids)){
            if (friendids.contains(String.valueOf(apuser.getUserid()))){
                apuser.setIsfans("1");
            }else{
                apuser.setIsfans("0");
            }
        }
        if(StringUtils.isNotEmpty(funids)){
            if (funids.contains(String.valueOf(apuser.getUserid()))){
                apuser.setIsfans("1");
            }
        }
    }

//    private void initFanInfo(long userid,AppUserMongoEntity apuser){
//        SnsFans snsFans =snsFansMapper.selectByUidAndLikeid(userid,apuser.getUserid());
//        if(null != snsFans){
//            apuser.setIsfans("1");
//        }else{
//            apuser.setIsfans("0");
//        }
//    }
//
//    private void initFriendInfo(Long userid,AppUserMongoEntity apuser){
//        SnsFriends snsFriends =  snsFriendsMapper.selectByUidAndFid(userid,apuser.getUserid());
//        if(null != snsFriends){
//            if(!StringUtils.isBlank(snsFriends.getRemark())){
//                apuser.setNickname(snsFriends.getRemark());
//            }
//            apuser.setIsfriend("1");
//        }else{
//            apuser.setIsfriend("0");
//        }
//    }

    private void initFanInfo(long userid,AppUserMongoEntity apuser){
        apuser.setIsfans("0");
        String fansIds = springJedisDao.get("userFans"+userid);
        if (StringUtils.isBlank(fansIds)){
            List<String> lists = snsFansMapper.selectListidByUid(userid);
            springJedisDao.set("userFans"+userid,JSON.toJSONString(lists),5);
            if (lists.contains(apuser.getUserid())){
                apuser.setIsfans("1");
            }
        } else {
            if (fansIds.contains(String.valueOf(apuser.getUserid()))){
                apuser.setIsfans("1");
            }
        }
    }

    private void initFriendInfo(Long userid,AppUserMongoEntity apuser){
        apuser.setIsfriend("0");
        if(userid == null || userid == -1 || "-1".equals(userid.toString())){
            return ;
        }
        String friendids = springJedisDao.get("userFriend"+userid);
        if (StringUtils.isBlank(friendids)){
            List<String> lists = snsFriendsMapper.selectListidByUid(userid);
            springJedisDao.set("userFriend"+userid,JSON.toJSONString(lists),5);
            if(lists.contains(apuser.getUserid())){
                apuser.setIsfriend("1");
            }
        } else {
            if (friendids.contains(String.valueOf(apuser.getUserid()))){
                apuser.setIsfriend("1");
            }
        }
    }

    /**
     * 获取好友id字符串
     * @param userid
     * @return
     */
    @Override
    public String getFriendIds(Long userid){
        if(userid == null || "-1".equals(userid.toString())){
            return null;
        }
        String friendids = springJedisDao.get("userFriend"+userid);
        if (StringUtils.isBlank(friendids)){
            List<String> lists = snsFriendsMapper.selectListidByUid(userid);
            friendids = JSON.toJSONString(lists);
            springJedisDao.set("userFriend"+userid,friendids,5);

        }
        return friendids;
    }

    /**
     * 获取用户关注列表id字符串
     * @param userid
     * @return
     */
    @Override
    public String getFansIds(Long userid){
        if(userid == null || "-1".equals(userid.toString())){
            return null;
        }
        String fansIds = springJedisDao.get("userFans"+userid);
        if (StringUtils.isBlank(fansIds)){
            List<String> lists = snsFansMapper.selectListidByUid(userid);
            fansIds = JSON.toJSONString(lists);
            springJedisDao.set("userFans"+userid,fansIds,5);

        }
        return fansIds;
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
        //businesstype 微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
        if(improve.getBusinesstype().equals("0")){
            businessid = improve.getImpid().toString();
        }else{
            businessid = improve.getBusinessid().toString();
        }
        String count = springJedisDao.get("comment"+businessid+improve.getBusinesstype()+improve.getImpid().toString());
        if (StringUtils.isBlank(count)){
            BaseResp<Integer> baseResp = commentMongoService.selectCommentCountSum
                    (businessid, improve.getBusinesstype(), improve.getImpid().toString());
            if (ResultUtil.isSuccess(baseResp)){
                count = baseResp.getData()+"";
            } else {
                count = "0";
            }
            springJedisDao.set("comment"+businessid+improve.getBusinesstype()+improve.getImpid().toString(),count,2);
        }
        improve.setCommentnum(Integer.parseInt(count));
    }

    /**
     * 超级话题
     * @param improve
     */
    private void initTopicInfo(Improve improve){
//        List<ImproveTopic> list = improveTopicMapper.selectByImpId(improve.getImpid(),0,4);
//        if(null != list){
//            improve.setImproveTopicList(list);
//        }
    }

    /**
     * 初始化进步中用户信息
     * @param improve
     * @author:luye
     */
    private void initImproveUserInfo(Improve improve,Long userid){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(improve.getUserid()));
        initUserRelateInfo(userid,appUserMongoEntity);
        if(null != appUserMongoEntity){
            if(userid != null && userid != -1 && !"-1".equals(userid+"")){
                //获取好友昵称
                String remark = userRelationService.selectRemark(userid, improve.getUserid(), "0");
                if(StringUtils.isNotBlank(remark)){
                    appUserMongoEntity.setNickname(remark);
                }
            }
            improve.setAppUserMongoEntity(appUserMongoEntity);
        }else{
            improve.setAppUserMongoEntity(new AppUserMongoEntity());
        }
        if ("2".equals(improve.getBusinesstype())){
            initRankImproveTotalLikeAndFlower(improve);
        }
//        improve.setAppUserMongoEntity(appUserMongoEntity);
    }


    private void initRankImproveTotalLikeAndFlower(Improve improve){
        RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId
                (improve.getBusinessid(),improve.getUserid());
        if (null == improve.getAppUserMongoEntity()){
            improve.setAppUserMongoEntity(new AppUserMongoEntity());
        }
        improve.getAppUserMongoEntity().setTotallikes(rankMembers.getLikes());
        improve.getAppUserMongoEntity().setTotalflowers(rankMembers.getFlowers());
    }

    /**
     * 点赞
     * Long impid,String userid,
     * String businesstype,String businessid
     * 点赞每天限制  --- 每天内次只能点一次
     * 进步必须是公开的
     * 不能给自己点赞
     * 取消或者点赞
     * 点赞-----进步赞个数  总赞
     * 点赞对积分的影响
     * 点完赞之后数据返回
     * @author luye
     */
    @Override
    public BaseResp<Object> addlike(final String userid, final String impid, final String businesstype, String businessid){
        BaseResp<Object> baseResp = new BaseResp<>();
        //防止重复提交
        if(isExitsForRedis(impid,userid)){
            return baseResp;
        }
        if ("0".equals(businesstype)){
            businessid = null;
        }
        springJedisDao.set("improve_like_temp_"+impid+userid,"1",1);

        boolean islike = improveMongoDao.exits(String.valueOf(impid),
                userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (islike) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_64,Constant.RTNINFO_SYS_64);
        }

        final Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,null,null);
        final UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
        if(null == improve || null == userInfo){
            return baseResp;
        }
//        if(improve.getUserid() == Long.parseLong(userid)){
//            baseResp.initCodeAndDesp(Constant.STATUS_SYS_13,Constant.RTNINFO_SYS_13);
//            return  baseResp;
//        }
//        baseResp = userBehaviourService.canOperateMore(Long.parseLong(userid),null,Constant.PERDAY_ADD_LIKE);
//        if(!ResultUtil.isSuccess(baseResp)){
//            return baseResp;
//        }
        try{

            //redis
            addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,
                        Constant.IMPROVE_ALL_DETAIL_LIKE,businessid,businesstype);
            //mongo
            addLikeToImproveForMongo(impid,businessid,businesstype,userid,Constant.MONGO_IMPROVE_LFD_OPT_LIKE,
                    userInfo.getAvatar());
            //mysql
            addLikeToImprove(improve,userid,impid,businessid,businesstype);

            final String finalBusinessid = businessid;
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    //更新进步赞数（mysql）


                    if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                        //如果是圈子,则更新circleMember中用户在该圈子中获得的总点赞数
                        circleMemberService.updateCircleMemberInfo(improve.getUserid(), finalBusinessid,1,null,null);
                    }

                    try{
                        userBehaviourService.pointChange(userInfo,"DAILY_LIKE",Constant_Perfect.PERFECT_GAM,null,0,0);
                        userBehaviourService.userSumInfo(Constant.UserSumType.addedLike,Long.parseLong(userid),null,0);
                    }catch (Exception e){
                        logger.error("pointChange or userSumInfo error ",e);
                    }
                    //添加评论消息---点赞
                    //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
                    //10：榜中  11 圈子中  12 教室中  13:教室批复作业
                    if(!userid.equals(improve.getUserid().toString())){
                        userMsgService.insertMsg(userid, improve.getUserid().toString(), impid, businesstype, finalBusinessid,
                                Constant.MSG_LIKE_MODEL, "1", "2", "点赞", 0, "", "");
                    }
                }
            });
            baseResp.getExpandData().put("haslike","1");
            baseResp.getExpandData().put("likes",getLikeFromRedis(improve.getImpid()+"",
                    improve.getBusinessid()+"",improve.getBusinesstype()));
            return baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("addlike error ",e);
        }
        return baseResp;
    }

    /**
	 * @author yinxc
	 * 添加评论消息---点赞
	 * 2017年2月10日
	 */
//	private void insertLikeMsg(String userid, String friendid, String impid, String businesstype, String businessid){
//		UserMsg record = new UserMsg();
//		if(!StringUtils.isBlank(friendid)){
//			record.setUserid(Long.valueOf(friendid));
//		}
//		record.setCreatetime(new Date());
//		record.setFriendid(Long.valueOf(userid));
//		record.setGtype(businesstype);
//		//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
//		record.setMsgtype("2");
//		if(!StringUtils.isBlank(businessid)){
//			record.setSnsid(Long.valueOf(businessid));
//		}else{
//			record.setSnsid(Long.valueOf(impid));
//		}
//		record.setRemark(Constant.MSG_LIKE_MODEL);
//		record.setIsdel("0");
//		record.setIsread("0");
//		// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
//		// 送花 4 送钻石  5:粉丝  等等)
//		record.setMtype("1");
//		try {
//			userMsgMapper.insertSelective(record);
//		} catch (Exception e) {
//			logger.error("insertMsg record = {}", JSONObject.fromObject(record).toString(), e);
//		}
//	}

    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:59
     *  @update 2017/3/8 下午3:59
     */
    @Override
    public BaseResp<Object> cancelLike(final String userid, final String impid, final String businesstype, final String businessid) {
        BaseResp baseResp = new BaseResp();
        //校验impid是否合法
        final Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,null,null);
        if(improve == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = isHaseLikeInfo(userid,impid,businesstype);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        try {
            //redis
            removeLikeToImproveForRedis(impid,userid,businessid,businesstype);
            //mongo
            removeLikeToImproveForMongo(impid,userid,Constant.MONGO_IMPROVE_LFD_OPT_LIKE)  ;
            //mysql
            removeLikeToImprove(improve,userid,impid,businessid,businesstype);
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    //如果是圈子,则更新circleMember中用户在该圈子中获得的总点赞数
                    if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                        circleMemberService.updateCircleMemberInfo(improve.getUserid(),businessid,-1,null,null);
                    }
                    try{
                        //取消赞后，删除消息中的记录    2017-06-05
//                    UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
//                    userBehaviourService.pointChange(userInfo,"DAILY_LIKE","2",null,0,0);
                        userBehaviourService.userSumInfo(Constant.UserSumType.removedLike,Long.parseLong(userid),null,0);
                    }catch (Exception e){
                        logger.error("pointChange,userSumInfo error",e);
                    }
                    userMsgService.deleteLikeCommentMsg(impid, businesstype, businessid, userid);
                }
            });
            baseResp.getExpandData().put("haslike","0");
            int likes = getLikeFromRedis(impid,businessid,businesstype);
            baseResp.getExpandData().put("likes",likes<0?0:likes);
        } catch (Exception e) {
            logger.error("cancel like error:{}",e);
        }
        return baseResp;
    }

    /**
     * 收藏微进步  考虑多次收藏情况  所有插入操作都考虑多次操作
     *
     * @param userid
     * @param impid
     * @param businesstype
     * @return
     */
    @Override
    public BaseResp<Object> collectImp(String userid, String impid, String businesstype,String businessid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            Date date = new Date();
            UserCollect userCollect = new UserCollect();
            userCollect.setUserid(Long.parseLong(userid));
            userCollect.setCid(Long.parseLong(impid));
            userCollect.setCreatetime(date);
            userCollect.setCtype(businesstype);
            userCollect.setUpdatetime(date);
            userCollect.setBusinessid(businessid);
            int n = userCollectMapper.insert(userCollect);
            if(n == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("userCollectMapper.insert error and msg={}",e);
        }
        return baseResp;
    }

    /**
     * 移除关注
     * @param userid
     * @param impid
     * @param buinesstype
     * @return
     */
    @Override
    public BaseResp<Object> removeCollect(String userid, String impid, String buinesstype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            int n = userCollectMapper.removeCollect(Long.parseLong(userid),Long.parseLong(impid),buinesstype);
            if(n > 0){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("userCollectMapper.remove error and msg={}",e);
        }
        return baseResp;
    }

    /**
     * 获取收藏进步列表  不包含榜单 圈子 教室等信息
     * @param userid
     * @param startNum
     * @param pageSize
     * @return
     */
    @Override
    public BaseResp<Object> selectCollect(String userid, int startNum, int pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<UserCollect> list  = userCollectMapper.selectCollect(Long.parseLong(userid),"0",startNum,pageSize);
            if(null == list){
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
            List<Improve> resultList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                UserCollect userCollect = list.get(i);
                Improve improve = selectImproveByImpid(
                        userCollect.getCid(),
                        userCollect.getUserid()+"",
                        userCollect.getCtype(),
                        userCollect.getBusinessid());
                improve.setLikes(getLikeFromRedis(String.valueOf(improve.getImpid()),
                        String.valueOf(improve.getBusinessid()),improve.getBusinesstype()));
                initImproveCommentInfo(improve);
                initImproveUserInfo(improve,Long.parseLong(userid));
                resultList.add(improve);
            }
            baseResp.setData(resultList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectCollect error and msg={}",e);
        }
        return baseResp;
    }

    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:59
     *  @update 2017/3/8 下午3:59
     */
    @Override
    public BaseResp<Object> addFlower(String userid,String friendid, String impid,
                                      int flowernum,String businesstype,String businessid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        //判断龙币是否充足
//        BaseResp baseResp = moneyService.isEnoughLongMoney(userid,flowernum*Constant.FLOWER_PRICE);
//        if (!ResultUtil.isSuccess(baseResp)){
//            return baseResp;
//        }
        if ("0".equals(businesstype)){
            businessid = null;
        }

        Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,null,null);
        UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
        if(null == improve || null == userInfo){
            return baseResp;
        }

        //消耗龙币
//        userMoneyDetailService.insertPublic(Long.parseLong(userid),
//                Constant.USER_MONEY_GIFT,flowernum*Constant.FLOWER_PRICE,-1);

        //扣除龙币成功
        try {
            int res = addImproveAllDetail(userid,impid,businesstype,String.valueOf(flowernum),
                    Constant.IMPROVE_ALL_DETAIL_FLOWER);
            if (res > 0){
                res = improveMapper.updateFlower(impid,flowernum,businessid,
                        getTableNameByBusinessType(businesstype));
            }
            if (res > 0){
                //redis
//                addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,Constant.IMPROVE_ALL_DETAIL_FLOWER);
                timeLineDetailDao.updateImproveFlower(businesstype,Long.valueOf(impid),flowernum);
                addLikeToImproveForMongo(impid,businessid,businesstype,userid,Constant.MONGO_IMPROVE_LFD_OPT_FLOWER,
                        userInfo.getAvatar())  ;
                //赠送龙分操作  UserInfo userInfo,String operateType,String pType)
                //送分  送进步币
                //送花添加消息记录    msg
                String remark = Constant.MSG_FLOWER_MODEL.replace("n", flowernum + "");
                //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
                //10：榜中  11 圈子中  12 教室中  13:教室批复作业
                if(!userid.equals(friendid)){
                	userMsgService.insertMsg(userid, friendid, impid, businesstype, businessid, remark, "1", "3", "送礼物", 0, "", "");
                }
                //用户送花获得龙分
                userBehaviourService.pointChange(userInfo,"DAILY_FLOWER",Constant_Perfect.PERFECT_GAM,null,0,0,flowernum);
//                BaseResp<Object> resp = userBehaviourService.pointChange(userInfo,"DAILY_FLOWERED", Constant_Perfect.PERFECT_GAM,null,0,0);
//                if(ResultUtil.isSuccess(resp)){
//                    int icon = flowernum* Constant_Imp_Icon.DAILY_FLOWERED;
//                    //进步币明细  进步币总数
//                    userImpCoinDetailService.insertPublic(Long.parseLong(friendid),Constant.USER_IMP_COIN_FLOWERD,icon,Long.parseLong(impid),Long.parseLong(userid));
//                }
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
//                insertMsg(userid, friendid, impid, flowernum, businesstype);
                return baseResp;
            }
        } catch (Exception e) {
            logger.error("add flower is error:{}",e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_48,Constant.RTNINFO_SYS_48);
    }
    
    /**
	 * @author yinxc
	 * 添加送花消息
	 * 2017年4月22日
	 */
//	private void insertMsg(String userid, String friendid, String impid,
//            int flowernum, String businesstype){
//		UserMsg record = new UserMsg();
//		record.setUserid(Long.valueOf(friendid));
//		record.setCreatetime(new Date());
//		record.setFriendid(Long.valueOf(userid));
//		record.setGtype(businesstype);
//		//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
//		record.setMsgtype("3");
//		record.setSnsid(Long.valueOf(impid));
//		
//		String remark = Constant.MSG_FLOWER_MODEL.replace("n", flowernum + "");
//		record.setRemark(remark);
//		record.setIsdel("0");
//		record.setIsread("0");
//		// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
//		// 送花 4 送钻石  5:粉丝  等等)
//		record.setMtype("1");
//		record.setNum(flowernum);
//		try {
//			userMsgMapper.insertSelective(record);
//		} catch (Exception e) {
//			logger.error("insertMsg record = {}", JSONObject.fromObject(record).toString(), e);
//		}
//	}
    
    
    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:59
     *  @update 2017/3/8 下午3:59
     */
    @Override
    public BaseResp<Object> addDiamond(String userid,String friendid, String impid,
                                       int diamondnum,String businesstype,String businessid) {
        //判断龙币是否充足
        BaseResp baseResp = moneyService.isEnoughLongMoney(userid,diamondnum*Constant.DIAMOND_PRICE);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        //消耗龙币


        //扣除龙币成功
        try {
            int res = addImproveAllDetail(userid,impid,businesstype,String.valueOf(diamondnum),
                    Constant.IMPROVE_ALL_DETAIL_DIAMOND);
            if (res > 0){
                res = improveMapper.updateDiamond(impid,diamondnum,businessid,
                        getTableNameByBusinessType(businesstype));
            }
            if (res > 0){
                //redis
//                addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,
//                        Constant.IMPROVE_ALL_DETAIL_DIAMOND);

                //赠送龙分操作
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(userid));
                //用户送钻获得龙分
                BaseResp<Object> resp = userBehaviourService.pointChange(userInfo,"DAILY_DIAMOND", Constant_Perfect.PERFECT_GAM,null,0,0);
                if(ResultUtil.isSuccess(resp)){
//                    int icon = diamondnum* Constant_Imp_Icon.DAILY_DIAMONDED;
                    int icon = 0;
                    //进步币明细  进步币总数
                    userImpCoinDetailService.insertPublic(Long.parseLong(friendid),Constant.USER_IMP_COIN_FLOWERD,icon,Long.parseLong(impid),Long.parseLong(userid));
                }

                return resp;
            }
        } catch (Exception e) {
            logger.error("add Diamond is error",e);
        }

        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_49,Constant.RTNINFO_SYS_49);
    }

    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:59
     *  @update 2017/3/8 下午3:59
     */
    @Override
    public BaseResp<List<ImpAllDetail>> selectImproveLFDListByUserid(String userid, String impid, String listtype,
                                                             int pagesize, Date lastdate) {

        BaseResp<List<ImpAllDetail>>  baseResp = new BaseResp<>();
        try {
            List<ImpAllDetail> impAllDetails = impAllDetailMapper.selectList(impid,listtype,pagesize,lastdate);
            for (ImpAllDetail impAllDetail : impAllDetails) {
            	//获取好友昵称
                SnsFriends snsFriends = userRelationService.selectByUidAndFid(Long.parseLong(userid), impAllDetail.getUserid());
//    			String remark = userRelationService.selectRemark(Long.parseLong(userid), impAllDetail.getUserid());
    			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(impAllDetail.getUserid()));
    			if(null != appUserMongoEntity){
                    if (null != snsFriends){
                        appUserMongoEntity.setIsfriend("1");
                        if (!StringUtils.isBlank(snsFriends.getRemark())){
                            appUserMongoEntity.setNickname(snsFriends.getRemark());
                        }
                    }
    				impAllDetail.setAppUser(appUserMongoEntity);
    			}else{
    				impAllDetail.setAppUser(new AppUserMongoEntity());
    			}
//                impAllDetail.setAppUser(userMongoDao.getAppUser(String.valueOf(impAllDetail.getUserid())));
            }
            baseResp = BaseResp.ok();
            baseResp.setData(impAllDetails);
            return baseResp;
        } catch (Exception e) {
            logger.error("select improveLFDList impid={},listtype={}",impid,listtype,e);
        }
        return baseResp;
    }
    
    /**
     *  @author luye
     *  @desp
     *  @create 2017/3/8 下午3:59
     *  @update 2017/3/8 下午3:59
     */
    @Override
    public BaseResp<List<ImpAllDetail>> selectImproveLFDList(String impid, String listtype,
                                                             int pagesize, Date lastdate) {

        BaseResp<List<ImpAllDetail>>  baseResp = new BaseResp<>();
        try {
            List<ImpAllDetail> impAllDetails = impAllDetailMapper.selectList(impid,listtype,pagesize,lastdate);
            for (ImpAllDetail impAllDetail : impAllDetails) {
                impAllDetail.setAppUser(userMongoDao.getAppUser(String.valueOf(impAllDetail.getUserid())));
            }
            baseResp = BaseResp.ok();
            baseResp.setData(impAllDetails);
            return baseResp;
        } catch (Exception e) {
            logger.error("select improveLFDList impid={},listtype={}",impid,listtype,e);
        }
        return baseResp;
    }

    /**
     *  @author luye
     *  @desp 根据进步类型获取 表名字
     *  @create 2017/3/8 下午3:59
     *  @update 2017/3/8 下午3:59
     */
    private String getTableNameByBusinessType(String businesstype){
        String tablename = "";
        switch (businesstype) {
            case Constant.IMPROVE_SINGLE_TYPE:
                tablename =  Constant_table.IMPROVE;
                break;
            case Constant.IMPROVE_RANK_TYPE:
                tablename = Constant_table.IMPROVE_RANK;
                break;
            case Constant.IMPROVE_CLASSROOM_TYPE:
                tablename = Constant_table.IMPROVE_CLASSROOM;
                break;
            case Constant.IMPROVE_CIRCLE_TYPE:
                tablename = Constant_table.IMPROVE_CIRCLE;
                break;
            case Constant.IMPROVE_GOAL_TYPE:
                tablename = Constant_table.IMPROVE_GOAL;
                break;
            default:
                tablename =  Constant_table.IMPROVE;
                break;
        }
        return tablename;
    }

    private String getSourecTableNameByBusinessType(String businesstype){
        String tablename;
        switch (businesstype) {
            case Constant.IMPROVE_RANK_TYPE:
                tablename = Constant_table.RANK_MEMBERS;
                break;
            case Constant.IMPROVE_CLASSROOM_TYPE:
                tablename = Constant_table.CIRCLE_MEMBERS;
                break;
            case Constant.IMPROVE_CIRCLE_TYPE:
                tablename = Constant_table.CIRCLE_MEMBERS;
                break;
            case Constant.IMPROVE_GOAL_TYPE:
                tablename = Constant_table.USER_GOAL;
                break;
            default:
                tablename = null;
                break;
        }
        return tablename;
    }

    /**
     *  @author luye
     *  @desp 是否点过赞
     *  update by lixb 暂时不用  判断是否点过赞 都通过mongodb
     *  @create 2017/3/8 下午4:00
     *  @update 2017/3/8 下午4:00
     */
    private BaseResp isHaseLikeInfo(String userid,String impid,String businesstype){
        BaseResp baseResp = new BaseResp();
        if (hasLikeCache(impid,userid)){
            return BaseResp.ok();
        }
        ImpAllDetail impAllDetail = new ImpAllDetail();
        impAllDetail.setUserid(Long.parseLong(userid));
        impAllDetail.setImpid(Long.parseLong(impid));
        impAllDetail.setBusinesstype(businesstype);
        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_LIKE);
        try {
            List<ImpAllDetail> impAllDetails = impAllDetailMapper.selectOneDetail(impAllDetail);
            if (null != impAllDetails && impAllDetails.size() > 0){
                return baseResp.initCodeAndDesp();
            } else {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_45,Constant.RTNINFO_SYS_45);
            }
        } catch (Exception e) {
            logger.error("can cancel like userid={} impid={} is error:{}",userid,impid,e);
        }
        return baseResp;
    }

    /**
     *  防止重复点赞
     * @param impid
     * @param userid
     * @return
     * @author luye
     */
    private boolean isExitsForRedis(String impid,String userid){
        String result = springJedisDao.get("improve_like_temp_"+impid+userid);
        if (null == result || !"1".equals(result)) {
            return false;
        }
        return true;
    }

    /**
     * 防止重复点赞
     * @param impid
     * @param userid
     * @return
     * @author luye
     */
    private boolean hasLikeCache(String impid,String userid){
        String result = springJedisDao.get(Constant.REDIS_IMPROVE_LIKE + impid + userid);
        if (null == result || !"1".equals(result)){
            return false;
        }
        return true;
    }

    /**
     * 将点赞放入redis
     * @return
     * @author luye
     */
    private void addLikeOrFlowerOrDiamondToImproveForRedis(final String impid,
                                                           String userid,
                                                           String opttype,
                                                           final String businessid,
                                                           final String businesstype){
        Map<String,String> map = new HashMap<>();
        map.put("lfd"+userid,userid);
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
                try {
                    String key = dateFormat.parse(dateFormat.format(new Date())).getTime()+"";
                    springJedisDao.zIncrby(key,impid+","+businessid+","+businesstype,1, (long) (25*60*60));
                } catch (ParseException e) {
                    logger.error("date format is error:",e);
                }
            }
        });
        //将赞保存到redis
        setLikeToRedis(impid,businessid,businesstype,1);
        //添加临时记录
        springJedisDao.set(Constant.REDIS_IMPROVE_LIKE + impid + userid,"1",10*60*60);
        springJedisDao.putAll(Constant.REDIS_IMPROVE_LFD + impid,map,30*24*60*60*1000);
    }


    private void setLikeToRedis(String impid,String businessid,String businesstype,int num){
        if (!springJedisDao.hasKey(Constant.REDIS_IMPROVE_LIKE + impid)){
            //mysql 同步
            Improve improve = selectImprove(Long.parseLong(impid),null,businesstype,businessid,null,null);
            springJedisDao.increment(Constant.REDIS_IMPROVE_LIKE + impid,improve.getLikes());
        }
        springJedisDao.increment(Constant.REDIS_IMPROVE_LIKE + impid,num);
    }


    private int getLikeFromRedis(String impid,String businessid,String businesstype){
        String count = springJedisDao.get(Constant.REDIS_IMPROVE_LIKE + impid);
        if (StringUtils.isBlank(count)){
            Improve improve = selectImprove(Long.parseLong(impid),null,businesstype,businessid,null,null);
            springJedisDao.increment(Constant.REDIS_IMPROVE_LIKE + impid,improve.getLikes());
            return improve.getLikes();
        } else {
            return Integer.parseInt(count);
        }
    }


    /**
     * 取消赞
     * @param impid
     * @param userid
     * @author luye
     */
    private void removeLikeToImproveForRedis(final String impid,
                                             String userid,
                                             final String businessid,
                                             final String businesstype){

        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
                try {
                    String key = dateFormat.parse(dateFormat.format(new Date())).getTime()+"";
                    double score = springJedisDao.zScore(key,impid);
                    if (score > 0){
                        springJedisDao.zIncrby(key,impid+","+businessid+","+businesstype,-1, (long) (24*60*60));
                    }
                } catch (ParseException e) {
                    logger.error("date format is error:",e);
                }
            }
        });

        setLikeToRedis(impid,businessid,businesstype,-1);
        springJedisDao.del("improve_like_temp_"+impid+userid);
        //删除临时记录
        springJedisDao.del(Constant.REDIS_IMPROVE_LIKE + impid + userid);
        springJedisDao.delete(Constant.REDIS_IMPROVE_LFD + impid,"like"+impid);
    }

    /**
     * 将点赞,送花，送钻放入mongo
     * @param opttype 0 - 点赞  1 - 送花  3 - 送钻
     * @return
     * @author luye
     */
    private void addLikeToImproveForMongo(String impid,String businessid,String businesstype,String userid,String opttype,String avatar){
        ImproveLFD improveLFD = new ImproveLFD();
        improveLFD.setImpid(impid);
        improveLFD.setUserid(userid);
        improveLFD.setOpttype(opttype);
        improveLFD.setAvatar(avatar);
//        AppUserMongoEntity user = new AppUserMongoEntity();
//        user.setId(userid);
//        improveLFD.setAppUser(user);
        improveLFD.setCreatetime(new Date());
        improveMongoDao.saveImproveLfd(improveLFD,businessid,businesstype);
    }

    /**
     *  @author luye
     *  @desp 删除 赞 花 钻 明细（mongo）
     *  @create 2017/3/8 下午4:01
     *  @update 2017/3/8 下午4:01
     */
    private void removeLikeToImproveForMongo(String impid,String userid,String opttype){
        ImproveLFD improveLFD = new ImproveLFD();
        improveLFD.setImpid(impid);
        improveLFD.setUserid(userid);
        improveLFD.setOpttype(opttype);
        improveMongoDao.removeImproveLfd(improveLFD);
    }


    /**
     * 点赞mysql操作
     * 目标中点赞
         进步中赞个数+1
         目标中总赞数+1
     榜单中点赞
         进步中赞个数+1
         用户榜单表中的赞总数+1
         排名数据修改
     * @param userid
     * @param impid
     * @param businessid
     * @param businesstype
     * @return
     * @author luye
     */
    private boolean addLikeToImprove(Improve improve,String userid,String impid,String businessid,String businesstype){
        int res = addImproveAllDetail(userid,impid,businesstype,null,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (res <= 0){
            return false;
        }
//        res = updateMemberSumInfo(impid,businesstype,businessid,Constant.IMPROVE_LIKE_ADD,0);
        afterImproveInfoChange(improve,1,Constant.MONGO_IMPROVE_LFD_OPT_LIKE);
        if (res > 0 ){
            return true;
        }
        return false;
    }

    @Override
    public int updateMemberSumInfo(String impid,String businesstype,String businessid,String type,int count){
        String tableName = getTableNameByBusinessType(businesstype);
        int res = 0;
        switch (type){
            case Constant.IMPROVE_LIKE_ADD:
                res = improveMapper.updateLikes(impid,type,businessid,tableName);
                break;
            case Constant.IMPROVE_LIKE_CANCEL:
                res = improveMapper.updateLikes(impid,type,businessid,tableName);
                break;
            case Constant.IMPROVE_FLOWER:
//                res = rankMembersMapper.updateRankImproveCount();
        }
        return res;
    }

    @Override
    public void afterImproveInfoChange(Improve improve,int count,String otype){
        String sourceTableName = getSourecTableNameByBusinessType(improve.getBusinesstype());
        try{
            switch (improve.getBusinesstype()){
                case Constant.IMPROVE_GOAL_TYPE:
                    improveMapper.updateSourceData(improve.getBusinessid(),improve.getUserid(),count,otype,sourceTableName,"goalid");
                    break;
                case Constant.IMPROVE_RANK_TYPE:
                    improveMapper.updateSourceData(improve.getBusinessid(),improve.getUserid(),count,otype,sourceTableName,"rankid");
                    //修改排名信息 Long rankId, Long userId, Constant.OperationType operationType,Integer num
                    Constant.OperationType type =  Constant.OperationType.like;
                    int icount = count;
                    if(otype.equals(Constant.MONGO_IMPROVE_LFD_OPT_LIKE)){
                        if(count>0){
                            type = Constant.OperationType.like;
                        }else{
                            type = Constant.OperationType.cancleLike;
                            icount = -icount;
                        }
                    }else if(otype.equals(Constant.MONGO_IMPROVE_LFD_OPT_FLOWER)){
                        type = Constant.OperationType.flower;
                    }
                    rankSortService.updateRankSortScore(improve.getBusinessid(),
                            improve.getUserid(),type,icount);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            logger.error("afterAddOrRemoveLike error ",e);
        }
    }

    @Override
    public String getFirstPhotos(Improve improve) {
        String photos = "";
        if(null != improve){
            //itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
            if("0".equals(improve.getItype())){
                //0 文字进步   brief --- 说明
                photos = improve.getBrief();
            }else if("1".equals(improve.getItype())){
                //1 图片进步   pickey --- 图片的key
                photos = firstPhotos(improve.getPickey());
            }else{
                //2 视频进步 3 音频进步 4 文件    filekey --- 文件key  视频文件  音频文件 普通文件
                photos = firstPhotos(improve.getPickey());
            }
        }
        return photos;
    }

    @Override
    public int updateSortSource(Long rankId) {
        try{
            improveMapper.updateSortSource(getTableNameByBusinessType(Constant.IMPROVE_RANK_TYPE),rankId);
        }catch (Exception e){
            logger.error("updateSortSource error rankId={}",rankId);
        }
        return 0;
    }

    private String firstPhotos(String photos){
        if(StringUtils.isNotBlank(photos)){
            JSONArray jsonArray = JSONArray.fromObject(photos);
            if(jsonArray.size()>0){
                photos = jsonArray.getString(0);
            }else
                photos = null;
        }
        return photos;
    }

    /**
     *  @author luye
     *  @desp 删除 点赞明细（mysql）
     *  @create 2017/3/8 下午4:03
     *  @update 2017/3/8 下午4:03
     */
    private boolean removeLikeToImprove(Improve improve,String userid,String impid,String businessid,String businesstype){
        int res = removeImproveAllDetail(userid,impid,businesstype,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (res <= 0){
            return false;
        }
//        res = updateMemberSumInfo(impid,businesstype,businessid,Constant.IMPROVE_LIKE_CANCEL,0);
        afterImproveInfoChange(improve,-1,Constant.MONGO_IMPROVE_LFD_OPT_LIKE);
        if (res > 0 ){
            return true;
        }
        return false;
    }

    /**
     * 添加进步全部明细记录（点赞，送花，送钻）
     * @param userid
     * @param impid
     * @param businesstype
     * @param detailtype
     * @return
     * @author luye
     */
    private int addImproveAllDetail(String userid,String impid,String businesstype,String giftnum,String detailtype){
        ImpAllDetail impAllDetail = new ImpAllDetail();
        impAllDetail.setId(idGenerateService.getUniqueIdAsLong());
        impAllDetail.setUserid(Long.parseLong(userid));
        impAllDetail.setImpid(Long.parseLong(impid));
        impAllDetail.setBusinesstype(businesstype);
        impAllDetail.setGiftnum(giftnum);
        impAllDetail.setDetailtype(detailtype);
        impAllDetail.setCreatetime(new Date());
        impAllDetail.setStatus("0");
        int res = 0;
        try {
            res = impAllDetailMapper.insertSelective(impAllDetail);
        } catch (Exception e) {
            logger.error("add improve all detailtype={} is error:{}",detailtype,e);
        }
        return res;
    }

    /**
     *  @author luye
     *  @desp 删除点赞明细
     *  @create 2017/3/8 下午4:04
     *  @update 2017/3/8 下午4:04
     */
    private int removeImproveAllDetail(String userid,String impid,String businesstype,String detailtype){
        ImpAllDetail impAllDetail = new ImpAllDetail();
        impAllDetail.setUserid(Long.parseLong(userid));
        impAllDetail.setImpid(Long.parseLong(impid));
        impAllDetail.setBusinesstype(businesstype);
        impAllDetail.setDetailtype(detailtype);
        int res = 0;
        try {
            res = impAllDetailMapper.deleteByImpAllDetail(impAllDetail);
        } catch (Exception e) {
            logger.error("remove improve like detail is error:{}",e);
        }
        return res;
    }

    /**
     * 初始化进步点赞，送花，送钻简略信息（张三，李四等5人点赞）
     * @param improve
     * @author luye
     */
    private void initLikeFlowerDiamondInfo(Improve improve){
        try{
            if(null != improve){
//                Long count = improveMongoDao.selectTotalCountImproveLFD(String.valueOf(improve.getImpid()));
                Long count = getImproveLFDCount(String.valueOf(improve.getImpid()));
//                List<ImproveLFD> improveLFDs = improveMongoDao.selectImproveLfdList(String.valueOf(improve.getImpid()));
//                for (ImproveLFD improveLFD : improveLFDs){
//                    AppUserMongoEntity appUser = userMongoDao.getAppUser(improveLFD.getUserid());
//                    improveLFD.setAvatar(appUser == null?"":appUser.getAvatar());
//                }
                List<ImproveLFD> improveLFDs = getImproveLFDList(String.valueOf(improve.getImpid()));
                improve.setLfdcount(count);
                improve.setImproveLFDs(improveLFDs);
            }
        }catch (Exception e){
            logger.error("selectImproveLfdList error improve={}",JSONObject.fromObject(improve).toString(),e);
        }
    }


    private Long getImproveLFDCount(String improveid){
        String count = springJedisDao.get("ImpLFD"+improveid);
        if (StringUtils.isBlank(count)){
            count = improveMongoDao.selectTotalCountImproveLFD(improveid)+"";
            springJedisDao.set("ImpLFD"+improveid,count,3);
        }
        return Long.parseLong(count);
    }


    private List<ImproveLFD> getImproveLFDList(String improveid){
        String improveLFDstr = springJedisDao.get("ImpLFDList"+improveid);
        if (StringUtils.isBlank(improveLFDstr)){
            List<ImproveLFD> improveLFDs = improveMongoDao.selectImproveLfdList(improveid);
            for (ImproveLFD improveLFD : improveLFDs){
                AppUserMongoEntity appUser = userMongoDao.getAppUser(improveLFD.getUserid());
                improveLFD.setAvatar(appUser == null?"":appUser.getAvatar());
            }
            improveLFDstr = JSON.toJSONString(improveLFDs);
            springJedisDao.set("ImpLFDList"+improveid,improveLFDstr,2);
            return improveLFDs;
        }
        List<ImproveLFD> list = JSON.parseArray(improveLFDstr,ImproveLFD.class);
        return list;
    }

    /**
     * 是否 点赞 送花 送钻 收藏
     * @param userid
     * @param improve
     * @author luye
     */
    private void initIsOptionForImprove(String userid,Improve improve){
        if(StringUtils.isEmpty(userid) || "-1".equals(userid)){
            improve.setHaslike("0");
            improve.setHasflower("0");
            improve.setHasdiamond("0");
            improve.setHascollect("0");
            return ;
        }
        if (null == improve){
            return;
        }
//        ImpAllDetail impAllDetail = new ImpAllDetail();
//        impAllDetail.setUserid(Long.parseLong(userid));
//        impAllDetail.setImpid(improve.getImpid());
//        impAllDetail.setGtype(improve.getBusinesstype());
//        impAllDetail.setStartno(0);
//        impAllDetail.setPagesize(1);
        //是否点赞
        isLike(userid,improve);
//        boolean islike = improveMongoDao.exits(String.valueOf(improve.getImpid()),
//                userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
//        if (islike) {
//            improve.setHaslike("1");
//        }else
//        {
//            improve.setHaslike("0");
//        }
//        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_LIKE);
//        List<ImpAllDetail> impAllDetailLikes = impAllDetailMapper.selectOneDetail(impAllDetail);
//        if (null != impAllDetailLikes && impAllDetailLikes.size() > 0) {
//            improve.setHaslike("1");
//        }
        //是否送花
        isFlower(userid,improve);
//        boolean isflower = improveMongoDao.exits(String.valueOf(improve.getImpid()),
//                userid,Constant.IMPROVE_ALL_DETAIL_FLOWER);
//        if (isflower) {
//            improve.setHasflower("1");
//        }else{
//            improve.setHasflower("0");
//        }
//        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_FLOWER);
//        List<ImpAllDetail> impAllDetailFlowers = impAllDetailMapper.selectOneDetail(impAllDetail);
//        if (null != impAllDetailFlowers && impAllDetailFlowers.size() > 0) {
//            improve.setHasflower("1");
//        }
        //是否送钻
//        boolean isdiamond = improveMongoDao.exits(String.valueOf(improve.getImpid()),
//                userid,Constant.IMPROVE_ALL_DETAIL_DIAMOND);
//        if (isdiamond) {
//            improve.setHasdiamond("1");
//        }
//        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_DIAMOND);
//        List<ImpAllDetail> impAllDetailDiamonds = impAllDetailMapper.selectOneDetail(impAllDetail);
//        if (null != impAllDetailDiamonds && impAllDetailDiamonds.size() > 0) {
//            improve.setHasdiamond("1");
//        }
        //是否收藏
//        UserCollect userCollect = new UserCollect();
//        userCollect.setUserid(Long.parseLong(userid));
//        userCollect.setCid(improve.getImpid());
//        List<UserCollect> userCollects = userCollectMapper.selectListByUserCollect(userCollect);
//        if (null != userCollects && userCollects.size() > 0 ){
//            improve.setHascollect("1");
//        }
        isCollect(userid,improve);
    }


    private void isLike(String userid,Improve improve){
        String likeListStr = springJedisDao.get("impLikeList"+improve.getImpid());
        if (StringUtils.isBlank(likeListStr)){
            List<ImproveLFDDetail> list = improveMongoDao.getImproveLFDUserIds
                    (String.valueOf(improve.getImpid()),Constant.IMPROVE_ALL_DETAIL_LIKE);
            likeListStr = JSON.toJSONString(list);
            springJedisDao.set("impLikeList"+improve.getImpid(),likeListStr,2);
        }
        if (likeListStr.contains(userid)){
            improve.setHaslike("1");
        } else {
            improve.setHaslike("0");
        }

    }

    private void isFlower(String userid,Improve improve){
        String likeListStr = springJedisDao.get("impFlowerList"+improve.getImpid());
        if (StringUtils.isBlank(likeListStr)){
            List<ImproveLFDDetail> list = improveMongoDao.getImproveLFDUserIds
                    (String.valueOf(improve.getImpid()),Constant.IMPROVE_ALL_DETAIL_FLOWER);
            likeListStr = JSON.toJSONString(list);
            springJedisDao.set("impFlowerList"+improve.getImpid(),likeListStr,2);
        }
        if (likeListStr.contains(userid)){
            improve.setHasflower("1");
        } else {
            improve.setHasflower("0");
        }
    }


    private void isCollect(String userid,Improve improve){

        String collectids = springJedisDao.get("userCollect"+userid);
        improve.setHascollect("0");
        if (StringUtils.isBlank(collectids)){
            List<String> ids = userCollectMapper.selectCollectIdsByUser(userid);
            springJedisDao.set("userCollect"+userid,JSON.toJSONString(ids),5);
            if (ids.contains(String.valueOf(improve.getImpid()))){
                improve.setHascollect("1");
            }
        } else {
            if (collectids.contains(String.valueOf(improve.getImpid()))){
                improve.setHascollect("1");
            }
        }
    }


    /**
     * filekey  type_businessid_name
     * type 0 独立的进步 1 目标中进步 2 榜中进步 3圈中进步 4 教室中进步
     * businessid 业务id 单个进步 0 其他为业务id  如 目标id,榜单id,圈子id
     * name key文件名   workflow 工作流  暂时不用
     * @param key
     * @param pickey
     * @param filekey
     * @return
     */
    @Override
    public BaseResp<Object> updateMedia(String key,String pickey,String filekey,
                                        String workflow,String duration,String picattribute){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(key,filekey)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
//        String oldKey = key;
//        key = key.replace("longbei_mp3/","");
//        key = key.replace("longbei_vido/","");

        String sourceKey = key;
        if(StringUtils.isBlank(duration)){
            duration = null;
        }
        if(workflow.contains("mp3")){
            sourceKey = "longbei_mp3/"+key;
        }else{
            sourceKey = "longbei_vido/"+key;
        }
        String[] filenameArr = key.split("_");
        if(filenameArr.length < 2){
            return baseResp;
        }
        String type = filenameArr[0];
        String businessid = filenameArr[1];
        if(type .equals(Constant.IMPROVE_SINGLE_TYPE)){
            businessid = null;
        }
        if(!workflow.contains("mp3")){
            pickey = "[\""+pickey+"\"]";
        }
        try{
            String tableName = getTableNameByBusinessType(type);
            int n = improveMapper.updateMedia(sourceKey,pickey,filekey,duration,picattribute,businessid,tableName);
            if(n > 0){
                timeLineDetailDao.updateImproveFileKey(sourceKey,pickey,filekey,duration,picattribute);
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("updateMedia error and key={},pickey={},filekey={},duration={},msg={}",key,pickey,filekey,duration,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp select(String userid, String impid, String businesstype,String businessid){
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //Long impid,String userid,
            //String businesstype,String businessid, String isdel,String ispublic
            Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,"0",null);
            if(null != improve){
                initImproveInfo(improve,userid != null?Long.parseLong(userid):null);
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(improve.getUserid()));
                //获取好友昵称
                if (StringUtils.isNotBlank(userid)){
                    String remark = userRelationService.selectRemark(Long.parseLong(userid), improve.getUserid(), "0");
                    if(StringUtils.isNotEmpty(remark)){
                        appUserMongoEntity.setNickname(remark);
                    }
                    initUserRelateInfo(Long.parseLong(userid),appUserMongoEntity);
                }
                improve.setAppUserMongoEntity(appUserMongoEntity);
                //初始化目标，榜单，圈子，教室等信息
                switch(businesstype){
                    case Constant.IMPROVE_SINGLE_TYPE:
                        break;
                    case Constant.IMPROVE_RANK_TYPE:
                        {
                            if("1".equals(improve.getIsbusinessdel())){
                                break;
                            }
                            Rank rank = rankService.selectByRankid(improve.getBusinessid());
                            if (null != rank){
                                int sortnum = 0;
                                RankMembers rankMembers = this.rankMembersMapper.selectByRankIdAndUserId(improve.getBusinessid(),improve.getUserid());
                                if("0".equals(rank.getIsfinish())){

                                }else if(rankMembers != null && "1".equals(rank.getIsfinish())){
                                    long s = this.springJedisDao.zRevRank(Constant.REDIS_RANK_SORT+improve.getBusinessid(),improve.getUserid()+"");
                                    sortnum = Integer.parseInt(s+"");
                                }else{
                                    sortnum = rankMembers.getSortnum();
                                }
                                improve.setBusinessEntity(rank.getPtype(),
                                        rank.getRanktitle(),
                                        rank.getRankinvolved(),
                                        rank.getStarttime(),
                                        rank.getEndtime(),
                                        sortnum,0,
                                        rank.getRankphotos(),
                                        rankMembers.getIcount());
                            }

                        }
                        break;
                    case Constant.IMPROVE_CLASSROOM_TYPE:
                    	//获取教室微进步批复作业列表
                    	List<ImproveClassroom> replyList = improveClassroomMapper.selectListByBusinessid(improve.getBusinessid(), improve.getImpid());
                    	String commentid = "";
                    	String isreply = "0";
                        if(null != replyList && replyList.size()>0){
                            List<CommentLower> lowerlist = new ArrayList<CommentLower>();
//                            for (ImproveClassroom improveClassroom : replyList) {
                            ImproveClassroom improveClassroom = replyList.get(0);
                            AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(improveClassroom.getUserid()));
                            ReplyImprove replyImprove = new ReplyImprove(improveClassroom.getImpid(), improveClassroom.getItype(), 
                            		improveClassroom.getBrief(), improveClassroom.getPickey(), 
                            		improveClassroom.getUserid(), improveClassroom.getCreatetime());
                            replyImprove.setAppUserMongoEntity(appUserMongo);
                			List<Comment> list = commentMongoDao.selectCommentListByItypeid(improve.getImpid().toString(),
                					businessid, "5", null, 0);
                            if(null != list && list.size()>0){
                                    Comment comment = list.get(0);
                                    commentid = comment.getId();
                                    lowerlist = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
                                    //初始化用户信息
                                    initCommentLowerUserInfoList(lowerlist);
//                                }
                                    replyImprove.setLowerlist(lowerlist);
							}
                            isreply = "1";
                    		improve.setReplyImprove(replyImprove);
                    	}
                        improve.setIsreply(isreply);
                    	Classroom classroom = classroomService.selectByClassroomid(improve.getBusinessid());
                    	if (null != classroom){
                    		String teacher = "";
                    		UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
                    		if(null != userCard){
                    			teacher += userCard.getDisplayname();
                    		}
                    		improve.setBusinessEntity(classroom.getPtype(),
                    				classroom.getClasstitle(),
                    				classroom.getClassphotos(),
                    				classroom.getClassinvoloed(),
                    				teacher, commentid);
                    	}
                        break;
                    case Constant.IMPROVE_CIRCLE_TYPE:
                        break;
                    case Constant.IMPROVE_GOAL_TYPE:
                        UserGoal userGoal = userGoalMapper.selectByGoalId(Long.parseLong(businessid));
                        if(null != userGoal){
                            String photos = improve.getPickey();
                            if(!StringUtils.isBlank(photos)){
                                JSONArray jsonArray = JSONArray.fromObject(photos);
                                if(jsonArray.size()>0){
                                    photos = jsonArray.getString(0);
                                }else
                                    photos = null;
                            }
                            improve.setBusinessEntity(userGoal.getPtype(),
                                    userGoal.getGoaltag(),
                                    0,
                                    userGoal.getCreatetime(),
                                    null,
                                    0,
                                    userGoal.getIcount(),photos,userGoal.getIcount());
                        }
                        break;
                    default:
                        break;
                }
                baseResp.setData(improve);
                return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            } else {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_401,Constant.RTNINFO_SYS_401);
            }

        }catch (Exception e){
            logger.error("selectImprove error and impid={},userid={}",impid,userid,e);
        }
        return baseResp;
    }

    /**
     * 初始化消息中用户信息 ------List
     */
    private void initCommentLowerUserInfoList(List<CommentLower> lowers){
        if(null != lowers && lowers.size()>0){
            for (CommentLower commentLower : lowers) {
                if(!StringUtils.hasBlankParams(commentLower.getSeconduserid())){
                    AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(commentLower.getSeconduserid()));
                    commentLower.setSecondNickname(appUserMongoEntity.getNickname());
                }
                if(!StringUtils.hasBlankParams(commentLower.getFirstuserid())){
                    AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(commentLower.getFirstuserid()));
                    commentLower.setFirstNickname(appUserMongo.getNickname());
                }
            }
        }

    }

    /**
     *  @author luye
     *  @desp   初始化 进步附加信息
     *  @create 2017/3/8 下午4:06
     *  @update 2017/3/8 下午4:06
     */
    public void initImproveInfo(Improve improve,Long userid) {

        //初始化赞数
        improve.setLikes(getLikeFromRedis(String.valueOf(improve.getImpid()),
                String.valueOf(improve.getBusinessid()),improve.getBusinesstype()));
//        Long s = System.currentTimeMillis();
        //初始化评论数
        initImproveCommentInfo(improve);
//        long s1 = System.currentTimeMillis();
        //初始化点赞，送花，送钻简略信息
        initLikeFlowerDiamondInfo(improve);
//        long s2 = System.currentTimeMillis();
        //初始化是否 点赞 送花 送钻 收藏
        initIsOptionForImprove(userid != null?userid+"":null,improve);
//        long s3 = System.currentTimeMillis();
        //初始化超级话题列表
        initTopicInfo(improve);
//        long s4 = System.currentTimeMillis();
//        logger.info("init comment time=" + (s1-s) +
//                "; initlikeflowers time = " + (s2-s1) + "; init isoption time=" + (s3-s2) +
//                "");
    }

    /**
     *  @author luye
     *  @desp 初始化 进步 赞和花的数量
     *  @create 2017/3/8 下午4:06
     *  @update 2017/3/8 下午4:06
     */
    public void initImproveLikeAndFlower(Improve improve){
        Long likecount = improveMongoDao.selectCountImproveLF(String.valueOf(improve.getImpid()),
                Constant.MONGO_IMPROVE_LFD_OPT_LIKE);
        Long flowercount = improveMongoDao.selectCountImproveLF(String.valueOf(improve.getImpid()),
                Constant.MONGO_IMPROVE_LFD_OPT_FLOWER);
        improve.setLikes(Integer.valueOf(String.valueOf(likecount)));
        improve.setFlowers(Integer.valueOf(String.valueOf(flowercount)));
    }

    @Override
    public int getPerDayImproveCount(long userid, String businesstype) {
        int result = 0;
        try{
            String key = Constant.RP_USER_PERDAY+Constant.PERDAY_ADD_IMPROVE+userid+"_"+DateUtils.getDate();
            String impCountStr = springJedisDao.getHashValue(key,businesstype);
            if(StringUtils.isBlank(impCountStr)){
            }else{
                result = Integer.parseInt(impCountStr);
            }
        }catch (Exception e){
            logger.error("userid={}",userid,e);
        }
        return result;
    }



    @Override
    public List<Improve> selectSuperTopicImproveList(long userid,String topicid,String orderby, int pageNo, int pageSize) {
        try{
            List<Improve> improves = improveMapper.selectListByBusinessid
                    (topicid, Constant_table.IMPROVE_TOPIC,null,null,orderby,null,pageNo,pageSize);
            if(null ==improves){
                logger.warn("getImproveBytopicid return null");
            }
            for (int i = 0; i <improves.size() ; i++) {
                initImproveInfo(improves.get(i),userid);
            }
            return improves;
        }catch (Exception e){
            logger.error("userid={},topicid={}",userid,e);
        }
        return null;
    }


    /**
     * 领虚拟奖品
     * 领奖是用户领奖
     * @param rankid
     * @param userid
     * @return
     */
    @Override
    public BaseResp<Object> acceptBasicAward(long rankid, long userid) {
        BaseResp<Object> baseResp = new BaseResp<>();
//        RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rankid,userid);
//        if(!canAcceptAward(rankMembers)){
//            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
//        }
        ImpAward impAward = impAwardMapper.selectByRankIdAndUserId(rankid,userid);
        int icon = impAward.getAwardprice();
        //进步币发生变化   龙杯账户减少 用户账户增多  添加事务 写 统一接口



        baseResp.setData(icon);
        baseResp.initCodeAndDesp();
        return baseResp;
    }

    //领实物奖品
    @Override
    public BaseResp<Object> acceptAward(long rankid, long userid, Integer addressId) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
//            RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(rankid,userid);
//            if(!canAcceptAward(rankMembers)) {
//
//            }
        }catch (Exception e){

        }
        return baseResp;
    }


    /**
     * 查询榜中，圈子，教室，目标中的用户所发的进步的列表
     * @param userid  用户id
     * @param businessid  榜，圈子，教室，目标 id
     * @param businesstype 业务类型 榜，圈子，教室，目标
     * @param startno 分页起始条数
     * @param pagesize 分页每页条数
     * @param selectCount 是否查询总数 只有在startno == 0 && selectCount == true时 才会查询总数
     * @return
     *
     * @author luye
     */
    @Override
    public BaseResp<List<Improve>> selectBusinessImproveList(String userid, String businessid,String iscomplain,
                                                       String businesstype, Integer startno, Integer pagesize,boolean selectCount) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        try {
            List<Improve> improves = improveMapper.selectListByBusinessid(businessid, getTableNameByBusinessType(businesstype),
                    null, userid, null, iscomplain, startno, pagesize);

            initImproveListOtherInfo(userid, improves);
            baseResp = BaseResp.ok();
            baseResp.setData(improves);
            if(startno == 0 && selectCount){
                Integer totalcount = improveMapper.selectListTotalcount(businessid, getTableNameByBusinessType(businesstype),
                        null, userid, null, iscomplain);
                baseResp.getExpandData().put("totalcount",totalcount);
            }
        } catch (Exception e) {
            logger.error("select businessi improve list userid={} businessid={} businesstype={} is error:"
                    , userid, businessid, businesstype);
        }
        return baseResp;
    }


    public BaseResp<Object> selectGoalMainImproveList(long userid, int startNum, int pageSize) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<Improve> list = improveMapper.selectGoalMainImproveList(userid,startNum,pageSize);
            for (Improve improve:list) {
                UserGoal userGoal = userGoalMapper.selectByGoalId(improve.getGoalid());
                improve.setIspublic(userGoal.getIspublic());
                improve.setBusinessEntity(userGoal.getPtype(),
                        userGoal.getGoaltag(),
                        0,
                        userGoal.getUpdatetime(),
                        null,
                        0,
                        userGoal.getIcount(),null,userGoal.getIcount());
            }
            baseResp.setData(list);
            return baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("selectGoalMainImproveList userid={},startNum={},pageSize={}",userid,startNum,pageSize,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> delGoal(long goalId, long userId) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            int res = improveMapper.delGoalToImprove(goalId,userId,Constant.IMPROVE_GOAL_TYPE);
            if(res>0){
                timeLineDetailDao.deleteImproveByBusinessid(String.valueOf(goalId),
                        Constant.IMPROVE_GOAL_TYPE,String.valueOf(userId));
                return baseResp.initCodeAndDesp();
            }
            return baseResp.ok();
        }catch (Exception e){
            logger.error("delGoalToImprove goalid={},userid={}",goalId,userId,e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Page<TimeLineDetail>> selectRecommendImproveList(String brief, String usernickname,
                                                                     Date starttime, Integer pageno,Integer pagesize) {
        BaseResp<Page<TimeLineDetail>> baseResp = new BaseResp<>();
        Page<TimeLineDetail> page = new Page<TimeLineDetail>(pageno,pagesize);
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
                    (timeLineDetailDao.selectRecommendImproveCount(brief,userids,
                            starttime)));
            pageno = Page.setPageNo(pageno,totalcount,pagesize);
            List<TimeLineDetail> timeLineDetails = timeLineDetailDao.selectRecommendImproveList
                    (brief,userids,pagesize*(pageno-1),pagesize);
            page.setTotalCount(totalcount);
            page.setList(timeLineDetails);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (NumberFormatException e) {
            logger.error("select recommend improve list from mongo is error:",e);
        }

        return baseResp;
    }

    @Override
    public BaseResp<List<Improve>> selectRecommendImproveList(String userid, Integer startno, Integer pagesize) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        List<Improve> improves = new ArrayList<>();
        try {
            List<TimeLineDetail> timeLineDetails = timeLineDetailDao.selectRecommendImproveList
                    (null,null,startno,pagesize);
            Map<String, String> map = userRelationService.selectRemarkImpLine(userid != null?Long.parseLong(userid):null);
            if (null != timeLineDetails && timeLineDetails.size() != 0){
                for (int i = 0 ; i < timeLineDetails.size() ; i++){
                    TimeLineDetail timeLineDetail = timeLineDetails.get(i);
                    Improve improve = new Improve();
                    improve.setImpid(timeLineDetail.getImproveId());
                    improve.setBrief(timeLineDetail.getBrief());
                    improve.setPickey(timeLineDetail.getPhotos());
                    improve.setFilekey(timeLineDetail.getFileKey());
                    improve.setSourcekey(timeLineDetail.getSourcekey());
                    improve.setBusinessid(timeLineDetail.getBusinessid());
                    improve.setBusinesstype(timeLineDetail.getBusinesstype());
                    improve.setItype(timeLineDetail.getItype());
                    improve.setCreatetime(DateUtils.parseDate(timeLineDetail.getCreatedate()));
//                    improve.setAppUserMongoEntity(timeLineDetail.getUser());
                    AppUserMongoEntity user = timeLineDetail.getUser();
                    //获取好友昵称
                    String remark = "";
                    if(!map.isEmpty()){
                        remark = map.get(userid + "_" + user.getId() + "_value");
                    }
                    if(!StringUtils.isBlank(remark)){
                        user.setNickname(remark);
                    }
                    improve.setAppUserMongoEntity(user);
                    if(!Constant.VISITOR_UID.equals(userid)){
                        initUserRelateInfo(Long.parseLong(userid),timeLineDetail.getUser());
                        improve.setAppUserMongoEntity(timeLineDetail.getUser());
                        initImproveInfo(improve,Long.parseLong(userid));
                    }

                    //初始化 赞 花 数量
//                    initImproveLikeAndFlower(improve);
                    improve.setFlowers(timeLineDetail.getFlowers());
                    improve.setLikes(timeLineDetail.getLikes());
                    improve.setIspublic("2");
                    improves.add(improve);
                }
            }
            baseResp = BaseResp.ok();
            baseResp.setData(improves);
        } catch (Exception e) {
            logger.error("select recommend list is error:",e);
        }

        return baseResp;
    }

    @Override
    public BaseResp<Page<Improve>> selectImproveList(String businesstype,String brief, String usernickname,
                                                     Date starttime,Integer pageno,
                                                     Integer pagesize,String order) {
        BaseResp<Page<Improve>> baseResp = new BaseResp<>();
        Page<Improve> page = new Page<>(pageno,pagesize);
        try {
            int totalcount = 0;
            List<Improve> improves = null;
            List<AppUserMongoEntity> users = new ArrayList<>();
            if (!StringUtils.isBlank(usernickname)){
                AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
                appUserMongoEntity.setNickname(usernickname);
                users = userMongoDao.getAppUsers(appUserMongoEntity);
            }
            if (Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
                totalcount = improveMapper.selectImproveCount(starttime,null,brief,users);
                improves = improveMapper.selectImproveList(starttime,null,brief,users,order,pagesize*(pageno-1),pagesize);
            }
            if (Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
                totalcount = improveMapper.selectGoalImproveCount(starttime,null,brief,users);
                improves = improveMapper.selectGoalImproveList(starttime,null,brief,users,order,pagesize*(pageno-1),pagesize);
            }
            if (Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
                totalcount = improveMapper.selectRankImproveCount(starttime,null,brief,users);
                improves = improveMapper.selectRankImproveList(starttime,null,brief,users,order,pagesize*(pageno-1),pagesize);
            }
            page.setTotalCount(totalcount);
            page.setList(improves);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select improve list for pc is error:",e);
        }

        return baseResp;
    }

    @Override
    public BaseResp<Object> updateImproveRecommentStatus(final String businesstype, final List<Long> impids, String isrecommend) {
        BaseResp baseResp = new BaseResp();
        try {
            timeLineDetailDao.updateRecommendImprove(impids,businesstype,isrecommend);
            improveMapper.updateImproveRecommend(getTableNameByBusinessType(businesstype),impids,isrecommend);
//            //发送消息
//            threadPoolExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    for (Long id : impids){
//                        insertImproveRecommentMsg(businesstype,id);
//                    }
//                }
//            });
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("update improve recommend status is error:",e);
        }
        return baseResp;
    }

    private void insertImproveRecommentMsg(String businesstype,Long impid){
        Improve improve = improveMapper.selectByPrimaryKey
                (impid,null,getTableNameByBusinessType(businesstype),"0","2");
        if (null != improve){
            String remark = "您的进步被设置为推荐进步";
            userMsgService.insertMsg
                    (String.valueOf(improve.getUserid()),"1",String.valueOf(impid),
                            "9",String.valueOf(improve.getBusinessid()),remark,"0","31", "进步被推荐",0, "", "");
        }
    }



    @Override
    public BaseResp updateImproveRecommendSort(Long impid, String businesstype, Integer sort) {
        BaseResp baseResp = new BaseResp();
        try {
            timeLineDetailDao.updateRecommendImproveSort(impid,businesstype,sort);
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("update improve recommend sort is error:", e);
        }
        return baseResp;
    }

    /**
     * 查询用户在business中发表的进步
     * @param curuserid 当前登录用户
     * @param userid 查看的用户id
     * @param businessid 业务id 圈子id
     * @param businesstype 类型 0 未关联 1 目标  2 榜 3 圈子 4教室
     * @param startno
     * @param pagesize
     * @param selectCount 是否查询总数 只有当startno == 0 and selectCount是true时 才查询总数
     *                    如果查询总数 则会在expandData中显示该用户在business中发表的进步总数
     * @return
     */
    @Override
    public BaseResp<List<Improve>> selectListInBusiness(String curuserid, String userid, String businessid, String businesstype, Integer startno, Integer pagesize, boolean selectCount) {
        logger.info("select improve list in business curuserid:{} userid:{} businesstype:{} businessid:{} startNo:{} pageSize:{}",curuserid,userid,businesstype,businessid,startno,pagesize);
        BaseResp<List<Improve>> baseResp = new BaseResp<List<Improve>>();
        try{
            baseResp = selectBusinessImproveList(userid,businessid,null,businesstype,startno,pagesize,false);
            if(ResultUtil.isSuccess(baseResp)) {
                List<Improve> list = baseResp.getData();
                for (int i = 0; i < list.size(); i++) {
                    Improve improve = list.get(i);
                    initImproveInfo(improve,curuserid ==null?null:Long.parseLong(curuserid));
                    initImproveUserInfo(improve,curuserid ==null?null:Long.parseLong(curuserid));
                }
                int count =0;
                if(startno == 0 && selectCount){
                    switch (businesstype) {
                        case Constant.IMPROVE_SINGLE_TYPE:

                            break;
                        case Constant.IMPROVE_RANK_TYPE:
                            RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(Long.parseLong(businessid),Long.parseLong(userid));
                            if(null != rankMembers){
                                count = rankMembers.getIcount();
                            }
                            break;
                        case Constant.IMPROVE_CLASSROOM_TYPE:

                            break;
                        case Constant.IMPROVE_CIRCLE_TYPE:
                            Map<String,Object> resultMap = new HashMap<String,Object>();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("circleId", businessid);
                            map.put("userId", userid);
                            map.put("itype",CircleMembers.normal);
                            //查询该用户是否在该圈子中
                            CircleMembers circleMembers = circleMembersMapper.findCircleMember(map);
                            if (circleMembers != null) {
                                count = circleMembers.getIcount();
                            }
                            break;
                        case Constant.IMPROVE_GOAL_TYPE:

                            break;
                        default:

                            break;
                    }
                }
                baseResp.getExpandData().put("totalCount",count);
            }
            return baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.info("select improve list in business curuserid:{} userid:{} businesstype:{} businessid:{} startNo:{} pageSize:{} errorMsg:{}",curuserid,userid,businesstype,businessid,startno,pagesize,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<Improve>> selectListInRank(String curuserid,String userid, String businessid,
                                                    String businesstype, Integer startno, Integer pagesize) {
        BaseResp<List<Improve>> baseResp = selectBusinessImproveList(userid,businessid,null,businesstype,startno,pagesize,false);
        if(ResultUtil.isSuccess(baseResp)){
//            String remark = userRelationService.selectRemark(Long.parseLong(userid),Long.parseLong(curuserid));
            List<Improve> list = baseResp.getData();
            for (int i = 0; i < list.size(); i++) {
                Improve improve = list.get(i);
                initImproveInfo(improve,curuserid ==null?null:Long.parseLong(curuserid));
//                initUserRelateInfo();
                initImproveUserInfo(improve,curuserid ==null?null:Long.parseLong(curuserid));
//                if (!StringUtils.isBlank(remark)){
//                    improve.getAppUserMongoEntity().setNickname(remark);
//                }
            }
        }
        int currentUserImpCount = 0;
        if(startno == 0){
            RankMembers rankMembers = rankMembersMapper.selectByRankIdAndUserId(Long.parseLong(businessid),Long.parseLong(userid));
            if(null != rankMembers){
                currentUserImpCount = rankMembers.getIcount();
            }
        }
        baseResp.getExpandData().put("currentUserImpCount",currentUserImpCount);
        return baseResp;
    }

    @Override
    public BaseResp<Object> removeImproveFromBusiness(String impid, String businessid, String businesstype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            if (Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
                Rank rank = rankMapper.selectRankByRankid(Long.parseLong(businessid));
                if (rank == null){
                    baseResp.initCodeAndDesp(Constant.STATUS_SYS_613,Constant.RTNINFO_SYS_613);
                    return baseResp;
                }
                int finish = Integer.parseInt(rank.getIsfinish());
                if (finish >= 2){
                    baseResp.initCodeAndDesp(Constant.STATUS_SYS_69,Constant.RTNINFO_SYS_69);
                    return baseResp;
                }
            }
            Improve improve = improveMapper.selectByPrimaryKey(Long.parseLong(impid),businessid,
                    getTableNameByBusinessType(businesstype),"0",null);
            if (null == improve){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_55,Constant.RTNINFO_SYS_55);
                return baseResp;
            }
            int res = improveMapper.removeImproveFromBusiness(getTableNameByBusinessType(businesstype),businessid,impid);
//            UserMsg userMsg = new UserMsg();
//            userMsg.setUserid(improve.getUserid());
//            userMsg.setMtype("0");
//            userMsg.setMsgtype("41");
//            userMsg.setSnsid(Long.parseLong(businessid));
//            userMsg.setRemark("榜中下榜");
//            userMsg.setGtype("2");
//            userMsg.setCreatetime(new Date());
//            userMsgMapper.insertSelective(userMsg);
            //gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
			//10：榜中  11 圈子中  12 教室中  13:教室批复作业

            if (res > 0){
                //清除数据
                clearDirtyData(improve);
                //发送消息
                Rank rank = rankMapper.selectRankByRankid(Long.parseLong(businessid));
				String remark = Constant.MSG_RANKIMP_QUIT_MODEL;
				if(null != rank){
					remark = remark.replace("n", rank.getRanktitle());
				}else{
					remark = remark.replace("n", "");
				}
                userMsgService.insertMsg(improve.getUserid().toString(), "", impid, "10", businessid, 
                		remark, "0", "41", "下榜", 0, "", "");
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("remove improve  impid={} from {} is error:",impid,getTableNameByBusinessType(businesstype),e);
        }
        return baseResp;
    }

    private boolean canAcceptAward(RankMembers rankMembers){
        boolean result = false;
        if(rankMembers.getIswinning().equals("1")&&rankMembers.getAcceptaward().equals("0")){
            result = true;
        }
        return result;
    }

    /**
     * 查询用户对进步献花的总数
     * @param userid
     * @param improveid 
     * @param number
     */
	@Override
	public BaseResp<Object> canGiveFlower(long userid, String improveid, String businesstype, String businessid, String number) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
            if("2".equals(businesstype)){
                //判断榜中的进步是否已删除，已下榜
                Improve improve = selectImproveByImpidMuc(Long.parseLong(improveid), userid + "",
                        businesstype, businessid);
                if(null != improve){
                    if("1".equals(improve.getIsdel())){
                        reseResp.initCodeAndDesp(Constant.STATUS_SYS_401,Constant.RTNINFO_SYS_401);
                        return reseResp;
                    }
                    if("1".equals(improve.getIsbusinessdel())){
                        //下榜的进步送花无限制
                        reseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                        return reseResp;
                    }
                }
            }
			Integer flowerSum = impAllDetailMapper.selectSumByImp(userid, improveid);
			if(flowerSum != null){
				if(flowerSum+Integer.parseInt(number) > Constant_point.DAILY_IMPFLOWER_LIMIT){
					int fnum = Constant_point.DAILY_IMPFLOWER_LIMIT - flowerSum;
					reseResp.initCodeAndDesp(Constant.STATUS_SYS_86, "榜中微进步每人最多只能送"
							+Constant_point.DAILY_IMPFLOWER_LIMIT+"朵花,您当前最大献花数量为" + fnum+"朵花");
				}else{
					reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}else{
				if("2".equals(businesstype)){
					if(Integer.parseInt(number) > Constant_point.DAILY_IMPFLOWER_LIMIT){
						reseResp.initCodeAndDesp(Constant.STATUS_SYS_86, "榜中微进步每人最多只能送"
								+Constant_point.DAILY_IMPFLOWER_LIMIT+"朵花,您当前最大献花数量为"
								+ Constant_point.DAILY_IMPFLOWER_LIMIT+"朵花");
					}else{
						reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
					}
				}else{
					reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}
		} catch (Exception e) {
			logger.error("canGiveFlower userid = {}, improveid = {}, number = {}", userid, improveid, number, e);
		}
		return reseResp;
	}

    @Override
    public BaseResp<List<Improve>> selectRecommendImprove(String userid,Integer startNum, Integer pageSize) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H");
        String key = simpleDateFormat.format(new Date());
        List<Improve> improves = new ArrayList<>();
        Set<String> impids = new HashSet<>();
        String friendids = null;
        String funids = null;
        Long uid = null;
        if(!Constant.VISITOR_UID.equals(userid)){
            uid = Long.parseLong(userid);
            friendids = getFriendIds(uid);
            funids = getFansIds(uid);
        }
        if(!springJedisDao.hasKey(key)){
            if(key.equals("1")){
                key = "24";
            }else {
                int newKey = Integer.parseInt(key) - 1;
                key = String.valueOf(newKey);
            }
        }
        logger.info("selectRecommendImprove userid={},startNum={},pageSize={},key={}",userid,startNum,pageSize,key);
        try {
            impids = springJedisDao.zRevrange(key,startNum,startNum+pageSize);
            for (String impid : impids){
                if (!StringUtils.isBlank(impid)){
                    String []strattr = impid.split(",");
                    if(!springJedisDao.hasKey("reimp"+strattr[0])){
                        Improve improve = selectImprove(Long.parseLong(strattr[0]),null,
                                strattr[2],strattr[1],"0",null);
                        springJedisDao.set("reimp"+strattr[0],JSON.toJSONString(improve),60*61);
                    }
                    Improve improve = JSON.parseObject(springJedisDao.get("reimp"+strattr[0]),Improve.class);
                    if(null == improve){
                        continue;
                    }
                    AppUserMongoEntity appuser = userMongoDao.getAppUser(String.valueOf(improve.getUserid()));
                    improve.setAppUserMongoEntity(appuser);
                    if(!Constant.VISITOR_UID.equals(userid)){
                        initUserRelateInfo(uid,appuser,friendids,funids);
                        initImproveInfo(improve,uid);
                    }
                    improves.add(improve);
                }
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(improves);
        } catch (Exception e) {
            logger.error("select recomment improve list is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp recommendImproveOpt() {

        BaseResp baseResp = new BaseResp();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H");
        String key = simpleDateFormat.format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            Long impkey = dateFormat.parse(dateFormat.format(new Date())).getTime();

            Map<String,Double> map = new HashMap<>();
            if (springJedisDao.hasKey(impkey-60*60*1000+"")){
                map = springJedisDao.zRangeWithScores(impkey-60*60*1000+"",0,-1);
            } else if (springJedisDao.hasKey(impkey.toString())){
                map = springJedisDao.zRangeWithScores(impkey.toString(),0,-1);
            }
            if(null != map&&map.isEmpty()){
                map = springJedisDao.zRangeWithScores(impkey.toString(),0,-1);
            }

            long lastImpKey = impkey-60*60-60*60*24*1000;
            Map<String,Double> lastMap = new HashMap<>();
            if (!springJedisDao.hasKey(String.valueOf(lastImpKey))){
                lastMap = springJedisDao.zRangeWithScores(String.valueOf(lastImpKey),0,-1);

            }
            for (Map.Entry<String,Double> entry : map.entrySet()){
                int iValue = entry.getValue().intValue();
                if(!lastMap.isEmpty()){
                    if(lastMap.containsKey(entry.getKey())){
                        int lastValue = lastMap.get(entry.getKey()).intValue();
                        iValue = iValue-lastValue;
                    }
                }
                springJedisDao.zIncrby(key,entry.getKey(),iValue, (long) (2*60*60));
            }
            logger.info("recommendImproveOpt impkey={},map={},key={}",impkey,
                    JSONObject.fromObject(map).toString(),key);
            baseResp.initCodeAndDesp();
        } catch (ParseException e) {
            logger.error("option recommend improve is error:",e);
        }
        return baseResp;
    }
}
