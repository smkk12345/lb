package com.longbei.appservice.service.impl;


import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_Perfect;
import com.longbei.appservice.common.constant.Constant_table;
import com.longbei.appservice.common.service.mq.send.QueueMessageSendService;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.ImproveMongoDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;

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
                                 String ispublic, String itype, String pimpid) {

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
        Date date = new Date();
        improve.setCreatetime(date);
        improve.setUpdatetime(date);
        if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
        }else{
            improve.setBusinessid(Long.parseLong(businessid));
        }

        BaseResp<Object> baseResp = new BaseResp<>();
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
                    ImproveClassroom improveClassroom = improveClassroomMapper.selectByPrimaryKey(Long.parseLong(pimpid));
                    if(null != improveClassroom){
                        //批复完成后添加消息
                    	//1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)  
//                		//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//                		//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
                    	//gtype  0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统 
						//10：榜中微进步  11 圈子中微进步  12 教室中微进步  13:教室批复作业
                    	userMsgService.insertMsg(userid, improveClassroom.getUserid().toString(), improve.getImpid().toString(), 
                    			"13", businessid, "批复作业", "2", "12", 0);
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
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(userid));//此处通过id获取用户信息
            baseResp = userBehaviourService.pointChange(userInfo,"DAILY_ADDIMP",ptype, Constant_Perfect.PERFECT_GAM,improve.getImpid(),0);
            //发布完成之后redis存储i一天数量信息
//            springJedisDao.put(Constant.RP_USER_PERDAY+Constant.PERDAY_ADD_IMPROVE+"",businesstype);
            String key = Constant.RP_USER_PERDAY+Constant.PERDAY_ADD_IMPROVE+userid+"_"+DateUtils.getDate();
            springJedisDao.increment(key,businesstype,1);
            springJedisDao.expire(key,Constant.CACHE_24X60X60);
            userBehaviourService.userSumInfo(Constant.UserSumType.addedImprove,Long.parseLong(userid),null,0);
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

        if(!insertImproveFilter(improve.getUserid(),Constant.IMPROVE_SINGLE_TYPE)){
            return false;
        }
        int res = 0;
        try {
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE);
        } catch (Exception e) {
            logger.error("insert sigle immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res != 0){
            //id,businesstype,businessid,userid,date
            String message = improve.getImpid() +
                    "," + Constant.IMPROVE_SINGLE_TYPE +
                    ",-1," + improve.getUserid() +
                    "," + DateUtils.formatDateTime1(improve.getCreatetime());
            queueMessageSendService.sendAddMessage(message);
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

        if(!insertImproveFilter(improve.getUserid(),Constant.IMPROVE_CIRCLE_TYPE)){
            return false;
        }
        int res = 0;
        try {
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_CIRCLE);
        } catch (Exception e) {
            logger.error("insert circle immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res != 0){
            String message = improve.getImpid() +
                    "," + Constant.IMPROVE_CIRCLE_TYPE +
                    "," + improve.getBusinessid() +
                    "," + improve.getUserid() +
                    "," + DateUtils.formatDateTime1(improve.getCreatetime());
            queueMessageSendService.sendAddMessage(message);
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

        if(!insertImproveFilter(improve.getUserid(),Constant.IMPROVE_CLASSROOM_TYPE)){
            return false;
        }
        int res = 0;
        try {
            res = improveClassroomMapper.insertSelective(improve);
        } catch (Exception e) {
            logger.error("insert classroom immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res != 0){
            String message = improve.getImpid() +
                    "," + Constant.IMPROVE_CLASSROOM_TYPE +
                    "," + improve.getBusinessid() +
                    "," + improve.getUserid() +
                    "," + DateUtils.formatDateTime1(improve.getCreatetime());

            queueMessageSendService.sendAddMessage(message);
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

        if(!insertImproveFilter(improve.getUserid(),Constant.IMPROVE_RANK_TYPE)){
            return false;
        }

        int res = 0;
        try {
            //没有
            improve.setRankid(idGenerateService.getUniqueIdAsLong());
            res = improveMapper.updateRankMainImprove(improve.getBusinessid(),improve.getUserid());
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_RANK);
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
            String message = improve.getImpid() +
                    "," + Constant.IMPROVE_RANK_TYPE +
                    "," + improve.getBusinessid() +
                    "," + improve.getUserid() +
                    "," + DateUtils.formatDateTime1(improve.getCreatetime());
            queueMessageSendService.sendAddMessage(message);
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

        if(!insertImproveFilter(improve.getUserid(),Constant.IMPROVE_GOAL_TYPE)){
            return false;
        }
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
            String message = improve.getImpid() +
                    "," + Constant.IMPROVE_GOAL_TYPE +
                    "," + improve.getBusinessid() +
                    "," + improve.getUserid() +
                    "," + DateUtils.formatDateTime1(improve.getCreatetime());

            queueMessageSendService.sendAddMessage(message);
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

        }
        initImproveInfo(improve,Long.parseLong(userid));
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
                    improve = improveMapper.selectByPrimaryKey(impid,businessid,Constant_table.IMPROVE,isdel,ispublic);
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
    public List<Improve> selectRankImproveList(String userid, String rankid,String sift,
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
                    flowerscore,likescore,pageNo,pageSize,lastdate==null?null:DateUtils.parseDate(lastdate));
            initImproveListOtherInfo(userid,improves);
            initSortInfo(rank,improves);
            if(null == improves){
                improves = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("selectRankImproveList userid:{} rankid:{} is error:{}",userid,rankid,e);
        }
        return improves;
    }

    private void initSortInfo(Rank rank,List<Improve> improves){
        if(rank.getIsfinish().equals("1")){//进行中
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
                    (rankid, Constant_table.IMPROVE_RANK,"1",null,orderby,pageNo,pageSize);
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
                    (circleid, Constant_table.IMPROVE_CIRCLE,null,null,orderby,pageNo,pageSize);
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
                    (circleid, Constant_table.IMPROVE_CIRCLE,"1",null,orderby,pageNo,pageSize);
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
                    (classroomid, Constant_table.IMPROVE_CLASSROOM,null, null, orderby, pageNo, pageSize);
            initImproveListOtherInfo(userid,improves);
            replyImp(improves);
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
                    (classroomid, Constant_table.IMPROVE_CLASSROOM, "1",null, orderby, pageNo, pageSize);
            initImproveListOtherInfo(userid,improves);
            replyImp(improves);
        } catch (Exception e) {
            logger.error("selectClassroomImproveListByDate userid:{} classroomid:{} is error:{}",userid,classroomid,e);
        }
        return improves;
    }

    //批复信息
  	private void replyImp(List<Improve> improves){
  		if(null != improves && improves.size()>0){
  			String isreply = "0";
  			for (Improve improve : improves) {
  				//获取教室微进步批复作业列表
  				List<ImproveClassroom> replyList = improveClassroomMapper.selectListByBusinessid(improve.getBusinessid(), improve.getImpid());
  				if(null != replyList && replyList.size()>0){
  					//已批复
  					isreply = "1";
  				}
  				improve.setIsreply(isreply);
  				improve.setReplyList(replyList);
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
                    (goalid, Constant_table.IMPROVE_GOAL,null,null,orderby,pageNo,pageSize);
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
    public boolean removeImprove(String userid,String improveid,
                                 String businesstype,String businessid) {

        boolean isok = false;
        try {
            switch (businesstype){
                case Constant.IMPROVE_SINGLE_TYPE:
                    isok = removeSingleImprove(userid,improveid);
                    break;
                case Constant.IMPROVE_RANK_TYPE:
                    isok = removeRankImprove(userid,businessid,improveid);
                    break;
                case Constant.IMPROVE_CLASSROOM_TYPE:
                    isok = removeClassroomImprove(userid,businessid,improveid);
                    break;
                case Constant.IMPROVE_CIRCLE_TYPE:
                    isok = removeCircleImprove(userid,businessid,improveid);
                    break;
                case Constant.IMPROVE_GOAL_TYPE:
                    isok = removeGoalImprove(userid,businessid,improveid);
                    break;
                default:
                    isok = removeSingleImprove(userid,improveid);
                    break;
            }
            if (isok){
                //将收藏了该进步的用户进步状态修改为已删除
                deleteUserCollectImprove("0",improveid);

                timeLineDetailDao.deleteImprove(Long.parseLong(improveid),userid);
                Improve improve = selectImproveByImpid(Long.parseLong(improveid),userid,businesstype,businessid);
                userBehaviourService.userSumInfo(Constant.UserSumType.removedImprove,
                        Long.parseLong(userid),improve,0);
            }
        } catch (Exception e) {
            logger.error("remove improve is error:",e);
        }
        return isok;
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
    public boolean removeSingleImprove(String userid, String improveid) {

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
            return true;
        }
        return false;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public boolean removeRankImprove(String userid, String rankid, String improveid) {
        int res = 0;
        Improve improve = selectImproveByImpid(Long.parseLong(improveid),userid,Constant.IMPROVE_RANK_TYPE,rankid);
        try {
            res = improveRankMapper.remove(userid,rankid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: rankid:{} improveid:{} userid:{} is error:{}",
                    rankid,improveid,userid,e);
        }
        if(res != 0){
            //清除数据
            clearDirtyData(improve);
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            return true;
        }
        return false;
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
                    improveMapper.chooseMainImprove(improve.getBusinessid(),improve.getUserid(),tableName,"goalid");
                }
                //更新赞 花
                improveMapper.afterDelSubImp(improve.getBusinessid(),improve.getUserid(),flower,like,sourceTableName,"goalid");
                break;
            case Constant.IMPROVE_RANK_TYPE:
                if(improve.getIsmainimp().equals("1")){
                    improveMapper.chooseMainImprove(improve.getBusinessid(),improve.getUserid(),tableName,"rankid");
                }
                //更新赞 花 进步条数
                improveMapper.afterDelSubImp(improve.getBusinessid(),improve.getUserid(),flower,like,sourceTableName,"rankid");
                //更新redis中排名by lixb
                rankSortService.updateRankSortScore(improve.getBusinessid(),
                    improve.getUserid(),Constant.OperationType.like,like);
                rankSortService.updateRankSortScore(improve.getBusinessid(),
                        improve.getUserid(),Constant.OperationType.flower,flower);
                break;
            default:
                break;
        }
    }

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public boolean removeCircleImprove(String userid, String circleid, String improveid) {
        int res = 0;
        try {
            res = improveCircleMapper.remove(userid,circleid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: circleid:{} improveid:{} userid:{} is error:{}",
                    circleid,improveid,userid,e);
        }
        if(res != 0){
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            return true;
        }
        return false;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public boolean removeClassroomImprove(String userid, String classroomid, String improveid) {
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
            return true;
        }
        return false;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public boolean removeGoalImprove(String userid, String goalid, String improveid) {
        int res = 0;
        Improve improve = selectImproveByImpid(Long.parseLong(improveid),userid,goalid,Constant.IMPROVE_GOAL_TYPE);
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
            return true;
        }
        return false;
    }


    @Override
    public BaseResp<List<Improve>> selectOtherImproveList(String userid, String targetuserid, Date lastdate, int pagesize) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        try {
            List<Improve> list = selectImproveListByUser(targetuserid,null,Constant.TIMELINE_IMPROVE_SELF,lastdate,pagesize);
            if (null != list && list.size() != 0){
                for (Improve improve : list){
                    //初始化是否 点赞 送花 送钻 收藏
                    initIsOptionForImprove(userid+"",improve);
                }
            }
            baseResp = BaseResp.ok();
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
	public List<Improve> selectImproveListByUserDate(String userid, String ptype,String ctype, Date lastdate, int pagesize) {
		List<TimeLine> timeLines = timeLineDao.selectTimeListByUserAndTypeDate(userid,ctype,lastdate,pagesize);
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
            improve.setCreatetime(DateUtils.parseDate(timeLineDetail.getCreatedate()));
            improve.setAppUserMongoEntity(timeLineDetail.getUser());
            improve.setUserid(timeLineDetail.getUser().getUserid());
            improve.setBusinessid(timeLineDetail.getBusinessid());
            improve.setBusinesstype(timeLineDetail.getBusinesstype());
            initImproveInfo(improve,Long.parseLong(userid));
            //初始化 赞 花 数量
            initImproveLikeAndFlower(improve);
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
    public List<Improve> selectImproveListByUser(String userid,String ptype,String ctype,Date lastdate,int pagesize) {

        List<TimeLine> timeLines = timeLineDao.selectTimeListByUserAndType
                (userid,ptype,ctype,lastdate,pagesize);
        List<Improve> improves = new ArrayList<>();

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
                improve.setCreatetime(DateUtils.parseDate(timeLineDetail.getCreatedate()));
                String businessType = timeLine.getBusinesstype();
                if(StringUtils.isBlank(businessType)){
                    improve.setBusinesstype("0");
                }else{
                    improve.setBusinesstype(businessType);
                }
                improve.setBusinessid(timeLine.getBusinessid());
                improve.setPtype(timeLine.getPtype());

//            AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(timeLine.getUserid());
                initUserRelateInfo(Long.parseLong(userid),timeLineDetail.getUser());
                improve.setAppUserMongoEntity(timeLineDetail.getUser());

                initImproveInfo(improve,Long.parseLong(userid));
                //初始化 赞 花 数量
                initImproveLikeAndFlower(improve);
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
     * @param userid
     * @param improvetype
     * @return
     * @author:luye
     * @date update 02月10日
     */
    private boolean insertImproveFilter(Long userid,String improvetype){
        switch (improvetype){
            case Constant.IMPROVE_SINGLE_TYPE:
                break;
            case Constant.IMPROVE_CIRCLE_TYPE:
                break;
            case Constant.IMPROVE_CLASSROOM_TYPE:
                break;
            case Constant.IMPROVE_GOAL_TYPE:
                break;
            case Constant.IMPROVE_RANK_TYPE:
                break;
            default:
                break;
        }
        return true;
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
            //初始化评论数量
            initImproveCommentInfo(improve);
            //初始化进步用户信息
            initImproveUserInfo(improve,userid != null?Long.parseLong(userid):null);
            //初始化点赞，送花，送钻简略信息
            initLikeFlowerDiamondInfo(improve);
            //初始化是否 点赞 送花 送钻 收藏
            initIsOptionForImprove(userid,improve);
        }
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
        BaseResp<Integer> baseResp = commentMongoService.selectCommentCountSum
                        (businessid, improve.getBusinesstype(), improve.getImpid().toString());
        if (ResultUtil.isSuccess(baseResp)){
            improve.setCommentnum(baseResp.getData());
        } else {
            improve.setCommentnum(0);
        }

    }

    /**
     * 超级话题
     * @param improve
     */
    private void initTopicInfo(Improve improve){
        List<ImproveTopic> list = improveTopicMapper.selectByImpId(improve.getImpid(),0,4);
        if(null != list){
            improve.setImproveTopicList(list);
        }
    }

    /**
     * 初始化进步中用户信息
     * @param improve
     * @author:luye
     */
    private void initImproveUserInfo(Improve improve,Long userid){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(improve.getUserid()));
        if(null != appUserMongoEntity){
            if(userid != null){
                //获取好友昵称
                String remark = userRelationService.selectRemark(userid, improve.getUserid());
                if(!StringUtils.isBlank(remark)){
                    appUserMongoEntity.setNickname(remark);
                }
            }
            improve.setAppUserMongoEntity(appUserMongoEntity);
        }else{
            improve.setAppUserMongoEntity(new AppUserMongoEntity());
        }
        initUserRelateInfo(userid,appUserMongoEntity);
//        improve.setAppUserMongoEntity(appUserMongoEntity);
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
    public BaseResp<Object> addlike(String userid,String impid,String businesstype,String businessid){
        BaseResp<Object> baseResp = new BaseResp<>();
        //防止重复提交
        if(isExitsForRedis(impid,userid)){
            return baseResp;
        }
        springJedisDao.set("improve_like_temp_"+impid+userid,"1",1);

        boolean islike = improveMongoDao.exits(String.valueOf(impid),
                userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (islike) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_64,Constant.RTNINFO_SYS_64);
        }

        Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,null,null);
        UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
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
            //mysql相关操作
            boolean flag = addLikeToImprove(improve,userid,impid,businessid,businesstype);
            if (flag){
                //redis
                addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
                //mongo
                addLikeToImproveForMongo(impid,businessid,businesstype,userid,Constant.MONGO_IMPROVE_LFD_OPT_LIKE,
                        userInfo.getAvatar())  ;

                //如果是圈子,则更新circleMember中用户在该圈子中获得的总点赞数
                if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                   circleMemberService.updateCircleMemberInfo(improve.getUserid(),businessid,1,null,null);
                }

                try{
                    userBehaviourService.pointChange(userInfo,"DAILY_LIKE",improve.getPtype(),null,0,0);
                    userBehaviourService.userSumInfo(Constant.UserSumType.addedLike,Long.parseLong(userid),null,0);
                }catch (Exception e){
                    logger.error("pointChange or userSumInfo error ",e);
                }

            }
            //添加评论消息---点赞
            String gtype = "";
            //gtype; // 0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统 
			//10：榜中微进步  11 圈子中微进步  12 教室中微进步  13:教室批复作业
            if("2".equals(businesstype)){
            	gtype = "10";
            }else if("3".equals(businesstype)){
            	gtype = "11";
            }else if("4".equals(businesstype)){
            	gtype = "12";
            }else{
            	gtype = businesstype;
            }
            userMsgService.insertMsg(userid, improve.getUserid().toString(), impid, gtype, businessid, 
            		Constant.MSG_LIKE_MODEL, "1", "2", 0);
//            insertLikeMsg(userid, improve.getUserid().toString(), impid, businesstype, businessid);
            baseResp.getExpandData().put("haslike","1");
            baseResp.getExpandData().put("likes",improve.getLikes()+1);
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
    public BaseResp<Object> cancelLike(String userid, String impid, String businesstype, String businessid) {
        BaseResp baseResp = new BaseResp();
        //校验impid是否合法
        Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,null,null);
        if(improve == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = isHaseLikeInfo(userid,impid,businesstype);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        try {
            boolean flag = removeLikeToImprove(improve,userid,impid,businessid,businesstype);
            if (flag){
                //redis
                removeLikeToImproveForRedis(impid,userid);
                //mongo
                removeLikeToImproveForMongo(impid,userid,Constant.MONGO_IMPROVE_LFD_OPT_LIKE)  ;
                //如果是圈子,则更新circleMember中用户在该圈子中获得的总点赞数
                if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                    circleMemberService.updateCircleMemberInfo(improve.getUserid(),businessid,-1,null,null);
                }
                try{
//                    UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
//                    userBehaviourService.pointChange(userInfo,"DAILY_LIKE",improve.getPtype(),null,0,0);
                    userBehaviourService.userSumInfo(Constant.UserSumType.removedLike,Long.parseLong(userid),null,0);
                }catch (Exception e){
                    logger.error("pointChange,userSumInfo error",e);
                }

            }
            baseResp.getExpandData().put("haslike","0");
            int likes = improve.getLikes()-1;
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
        //判断龙币是否充足
        BaseResp baseResp = moneyService.isEnoughLongMoney(userid,flowernum*Constant.FLOWER_PRICE);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }

        //消耗龙币
        userMoneyDetailService.insertPublic(Long.parseLong(userid),
                Constant.USER_MONEY_GIFT,flowernum*Constant.FLOWER_PRICE,-1);

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
                addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,Constant.IMPROVE_ALL_DETAIL_FLOWER);

                //赠送龙分操作  UserInfo userInfo,String operateType,String pType)
                //送分  送进步币
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(userid));
                //用户龙分变化
                BaseResp<Object> resp = userBehaviourService.pointChange(userInfo,"DAILY_FLOWERED", Constant_Perfect.PERFECT_GAM,null,0,0);
                if(ResultUtil.isSuccess(resp)){
                    int icon = flowernum* Constant_Imp_Icon.DAILY_FLOWERED;
                    //进步币明细  进步币总数
                    userImpCoinDetailService.insertPublic(Long.parseLong(friendid),Constant.USER_IMP_COIN_FLOWERD,icon,Long.parseLong(impid),Long.parseLong(userid));
                }
                //送花添加消息记录    msg
                String remark = Constant.MSG_FLOWER_MODEL.replace("n", flowernum + "");
                String gtype = "";
                //gtype; // 0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统 
    			//10：榜中微进步  11 圈子中微进步  12 教室中微进步  13:教室批复作业
                if("2".equals(businesstype)){
                	gtype = "10";
                }else if("3".equals(businesstype)){
                	gtype = "11";
                }else if("4".equals(businesstype)){
                	gtype = "12";
                }else{
                	gtype = businesstype;
                }
                userMsgService.insertMsg(userid, friendid, impid, gtype, businessid, remark, "1", "3", 0);
//                insertMsg(userid, friendid, impid, flowernum, businesstype);
                return resp;
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
                addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,Constant.IMPROVE_ALL_DETAIL_DIAMOND);

                //赠送龙分操作
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(userid));
                //用户龙分变化
                BaseResp<Object> resp = userBehaviourService.pointChange(userInfo,"DAILY_DIAMOND", Constant_Perfect.PERFECT_GAM,null,0,0);
                if(ResultUtil.isSuccess(resp)){
                    int icon = diamondnum* Constant_Imp_Icon.DAILY_DIAMONDED;
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
        impAllDetail.setGtype(businesstype);
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
    private void addLikeOrFlowerOrDiamondToImproveForRedis(String impid,String userid,String opttype){
        Map<String,String> map = new HashMap<>();
//        switch (opttype) {
//            case Constant.IMPROVE_ALL_DETAIL_LIKE:
//                map.put("like"+userid,userid);
//                break;
//            case Constant.IMPROVE_ALL_DETAIL_FLOWER:
//                map.put("flower"+userid,userid);
//                break;
//            case Constant.IMPROVE_ALL_DETAIL_DIAMOND:
//                map.put("diamond"+userid,userid);
//                break;
//            default:
//                break;
//        }
        map.put("lfd"+userid,userid);
        //添加临时记录
        springJedisDao.set(Constant.REDIS_IMPROVE_LIKE + impid + userid,"1",10*60*60);
        springJedisDao.putAll(Constant.REDIS_IMPROVE_LFD + impid,map,30*24*60*60*1000);
    }

    /**
     * 取消赞
     * @param impid
     * @param userid
     * @author luye
     */
    private void removeLikeToImproveForRedis(String impid,String userid){
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
        String tableName = getTableNameByBusinessType(businesstype);
        res = improveMapper.updateLikes(impid,Constant.IMPROVE_LIKE_ADD,businessid,tableName);
        afterAddOrRemoveLike(improve,1,Constant.MONGO_IMPROVE_LFD_OPT_LIKE);
        if (res > 0 ){
            return true;
        }
        return false;
    }

    private void afterAddOrRemoveLike(Improve improve,int count,String otype){
        String sourceTableName = getSourecTableNameByBusinessType(improve.getBusinesstype());
        try{
            switch (improve.getBusinesstype()){
                case Constant.IMPROVE_GOAL_TYPE:
                    improveMapper.updateSourceLike(improve.getBusinessid(),improve.getUserid(),count,otype,sourceTableName,"goalid");
                    break;
                case Constant.IMPROVE_RANK_TYPE:
                    improveMapper.updateSourceLike(improve.getBusinessid(),improve.getUserid(),count,otype,sourceTableName,"rankid");
                    //修改排名信息 Long rankId, Long userId, Constant.OperationType operationType,Integer num
                    if(count>0){
                        rankSortService.updateRankSortScore(improve.getBusinessid(),
                                improve.getUserid(),Constant.OperationType.like,count);
                    }else{
                        rankSortService.updateRankSortScore(improve.getBusinessid(),
                                improve.getUserid(),Constant.OperationType.cancleLike,-count);
                    }

                    break;
                default:

                    break;
            }
        }catch (Exception e){
            logger.error("afterAddOrRemoveLike error ",e);
        }
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
        res = improveMapper.updateLikes(impid,Constant.IMPROVE_LIKE_CANCEL,businessid,getTableNameByBusinessType(businesstype));
        afterAddOrRemoveLike(improve,-1,Constant.MONGO_IMPROVE_LFD_OPT_LIKE);
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
        impAllDetail.setGtype(businesstype);
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
        impAllDetail.setGtype(businesstype);
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
        if(StringUtils.isEmpty(userid)){
            improve.setHaslike("0");
            improve.setHasflower("0");
            improve.setHasdiamond("0");
            improve.setHascollect("0");
            return ;
        }
//        ImpAllDetail impAllDetail = new ImpAllDetail();
//        impAllDetail.setUserid(Long.parseLong(userid));
//        impAllDetail.setImpid(improve.getImpid());
//        impAllDetail.setGtype(improve.getBusinesstype());
//        impAllDetail.setStartno(0);
//        impAllDetail.setPagesize(1);
        //是否点赞
        boolean islike = improveMongoDao.exits(String.valueOf(improve.getImpid()),
                userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (islike) {
            improve.setHaslike("1");
        }
//        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_LIKE);
//        List<ImpAllDetail> impAllDetailLikes = impAllDetailMapper.selectOneDetail(impAllDetail);
//        if (null != impAllDetailLikes && impAllDetailLikes.size() > 0) {
//            improve.setHaslike("1");
//        }
        //是否送花
        boolean isflower = improveMongoDao.exits(String.valueOf(improve.getImpid()),
                userid,Constant.IMPROVE_ALL_DETAIL_FLOWER);
        if (isflower) {
            improve.setHasflower("1");
        }
//        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_FLOWER);
//        List<ImpAllDetail> impAllDetailFlowers = impAllDetailMapper.selectOneDetail(impAllDetail);
//        if (null != impAllDetailFlowers && impAllDetailFlowers.size() > 0) {
//            improve.setHasflower("1");
//        }
        //是否送钻
        boolean isdiamond = improveMongoDao.exits(String.valueOf(improve.getImpid()),
                userid,Constant.IMPROVE_ALL_DETAIL_DIAMOND);
        if (isdiamond) {
            improve.setHasdiamond("1");
        }
//        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_DIAMOND);
//        List<ImpAllDetail> impAllDetailDiamonds = impAllDetailMapper.selectOneDetail(impAllDetail);
//        if (null != impAllDetailDiamonds && impAllDetailDiamonds.size() > 0) {
//            improve.setHasdiamond("1");
//        }
        //是否收藏
        UserCollect userCollect = new UserCollect();
        userCollect.setUserid(Long.parseLong(userid));
        userCollect.setCid(improve.getImpid());
        List<UserCollect> userCollects = userCollectMapper.selectListByUserCollect(userCollect);
        if (null != userCollects && userCollects.size() > 0 ){
            improve.setHascollect("1");
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
    public BaseResp<Object> updateMedia(String key,String pickey,String filekey,String workflow){
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(key,filekey)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
//        String oldKey = key;
//        key = key.replace("longbei_mp3/","");
//        key = key.replace("longbei_vido/","");
        String sourceKey = key;
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
            int n = improveMapper.updateMedia(sourceKey,pickey,filekey,businessid,tableName);
            if(n > 0){
                timeLineDetailDao.updateImproveFileKey(sourceKey,pickey,filekey);
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("updateMedia error and key={},pickey={},filekey={},msg={}",key,pickey,filekey,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp select(String userid, String impid, String businesstype,String businessid){
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            //Long impid,String userid,
            //String businesstype,String businessid, String isdel,String ispublic
            Improve improve = selectImprove(Long.parseLong(impid),null,businesstype,businessid,null,null);
            if(null != improve){
                initImproveInfo(improve,userid != null?Long.parseLong(userid):null);
                AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(improve.getUserid()));
                improve.setAppUserMongoEntity(appUserMongoEntity);
                initUserRelateInfo(improve.getUserid(),appUserMongoEntity);
                //初始化目标，榜单，圈子，教室等信息
                switch(businesstype){
                    case Constant.IMPROVE_SINGLE_TYPE:
                        break;
                    case Constant.IMPROVE_RANK_TYPE:
                        {
                            Rank rank = rankService.selectByRankid(improve.getBusinessid());
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
                        break;
                    case Constant.IMPROVE_CLASSROOM_TYPE:
                        break;
                    case Constant.IMPROVE_CIRCLE_TYPE:
                        break;
                    case Constant.IMPROVE_GOAL_TYPE:
                        UserGoal userGoal = userGoalMapper.selectByGoalId(Long.parseLong(businessid));
                        improve.setBusinessEntity(userGoal.getPtype(),
                                userGoal.getGoaltag(),
                                0,
                                userGoal.getUpdatetime(),
                                null,
                                0,
                                userGoal.getIcount(),null,userGoal.getIcount());
                        break;
                    default:
                        break;
                }
                baseResp.setData(improve);
            }
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectImprove error and impid={},userid={}",impid,userid,e);
        }
        return baseResp;
    }

    @Override
    public List<Improve> findCircleMemberImprove(Long circleId, Long userId,Long currentUserId, Integer startNo, Integer pageSize) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("circleId",circleId);
        map.put("userId",userId);
        map.put("startNo",startNo);
        map.put("pageSize",pageSize);
        List<Improve> improveCircleList = improveMapper.findCircleMemberImprove(map);
        initImproveListOtherInfo(currentUserId.toString(),improveCircleList);
        return improveCircleList;
    }

    /**
     *  @author luye
     *  @desp   初始化 进步附加信息
     *  @create 2017/3/8 下午4:06
     *  @update 2017/3/8 下午4:06
     */
    public void initImproveInfo(Improve improve,Long userid) {
        //初始化评论数
        initImproveCommentInfo(improve);
        //初始化点赞，送花，送钻简略信息
        initLikeFlowerDiamondInfo(improve);
        //初始化是否 点赞 送花 送钻 收藏
        initIsOptionForImprove(userid != null?userid+"":null,improve);
        //初始化超级话题列表
        initTopicInfo(improve);
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
                    (topicid, Constant_table.IMPROVE_TOPIC,null,null,orderby,pageNo,pageSize);
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
     * @return
     *
     * @author luye
     */
    @Override
    public BaseResp<List<Improve>> selectBusinessImproveList(String userid, String businessid,
                                                       String businesstype, Integer startno, Integer pagesize) {
        BaseResp<List<Improve>> baseResp = new BaseResp<>();
        try {
            List<Improve> improves = improveMapper.selectListByBusinessid(businessid, getTableNameByBusinessType(businesstype),
                    null, userid, null, startno, pagesize);
            initImproveListOtherInfo(userid, improves);
            baseResp = BaseResp.ok();
            baseResp.setData(improves);
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
                return baseResp.initCodeAndDesp();
            }
        }catch (Exception e){
            logger.error("delGoalToImprove goalid={},userid={}",goalId,userId,e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Page<TimeLineDetail>> selectRecommendImproveList(String brief, String usernickname,
                                                                     Date starttime, Integer pageno,Integer pagesize) {
        BaseResp<Page<TimeLineDetail>> baseResp = new BaseResp<>();
        Page<TimeLineDetail> page = new Page<>();
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
                    improve.setAppUserMongoEntity(timeLineDetail.getUser());

                    initImproveInfo(improve,Long.parseLong(userid));
                    //初始化 赞 花 数量
                    initImproveLikeAndFlower(improve);
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
    public BaseResp<Object> updateImproveRecommentStatus(String businesstype, List<Long> impids, String isrecommend) {
        BaseResp baseResp = new BaseResp();
        try {
            timeLineDetailDao.updateRecommendImprove(impids,businesstype,isrecommend);
            improveMapper.updateImproveRecommend(getTableNameByBusinessType(businesstype),impids,isrecommend);
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("update improve recommend status is error:",e);
        }
        return baseResp;
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
    @Override
    public BaseResp<List<Improve>> selectListInRank(String curuserid,String userid, String businessid, String businesstype, Integer startno, Integer pagesize) {
        BaseResp<List<Improve>> baseResp = selectBusinessImproveList(userid,businessid,businesstype,startno,pagesize);
        if(ResultUtil.isSuccess(baseResp)){
            List<Improve> list = baseResp.getData();
            for (int i = 0; i < list.size(); i++) {
                Improve improve = list.get(i);
                initImproveInfo(improve,curuserid ==null?null:Long.parseLong(curuserid));
//                initUserRelateInfo();
                initImproveUserInfo(improve,curuserid ==null?null:Long.parseLong(curuserid));
            }
        }
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
                    getSourecTableNameByBusinessType(businesstype),"0",null);
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
            userMsgService.insertMsg(improve.getUserid().toString(), "", impid, "2", businessid, "榜中下榜", "0", "41", 0);
            if (res > 0){
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


}
