package com.longbei.appservice.service.impl;


import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_table;
import com.longbei.appservice.common.service.mq.send.QueueMessageSendService;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserBehaviourService;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    private UserBehaviourService userBehaviourService;
    @Autowired
    private SpringJedisDao springJedisDao;
    @Autowired
    private ImpGoalPerdayMapper impGoalPerdayMapper;

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public BaseResp<Object> insertImprove(String userid, String brief,
                                 String pickey, String filekey,
                                 String businesstype,String businessid, String ptype,
                                 String ispublic, String itype) {

        Improve improve = new Improve();

        improve.setImpid(new Date().getTime());
        improve.setUserid(Long.parseLong(userid));
        improve.setBrief(brief);
        improve.setPickey(pickey);
        improve.setFilekey(filekey);
        improve.setBusinesstype(businesstype);
        improve.setPtype(ptype);
        improve.setIspublic(ispublic);
        improve.setItype(itype);
        improve.setCreatetime(new Date());
        improve.setUpdatetime(new Date());
        if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
//
        }else{
            improve.setBusinessid(Long.parseLong(businessid));
        }
        BaseResp<Object> baseResp = new BaseResp<>();
        boolean isok = false;
        if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
            isok = insertImproveSingle(improve);
        }
        if(Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
            isok = insertImproveForRank(improve);
        }
        if(Constant.IMPROVE_CLASSROOM_TYPE.equals(businesstype)){
            isok = insertImproveForClassroom(improve);
        }
        if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
            isok = insertImproveForCircle(improve);
        }
        if(Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
            isok = insertImproveForGoal(improve);
        }
        //进步发布完成之后
        if(isok){
            userBehaviourService.levelUp(Long.parseLong(userid), SysRulesCache.sysRules.getAddimprove(),ptype);
        }
        baseResp.setData(improve.getImpid());
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
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
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_CLASSROOM);
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
            res = improveMapper.insertSelective(improve,Constant_table.IMPROVE_RANK);
        } catch (Exception e) {
            logger.error("insert rank immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
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
     *  @desp 
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
            if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE);
            }
            if(Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_RANK);
            }
            if(Constant.IMPROVE_CLASSROOM_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_CLASSROOM);
            }
            if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_CIRCLE);
            }
            if(Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_GOAL);
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
    public List<Improve> selectRankImproveList(String userid, String rankid,String sift, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            //全部
            if (Constant.IMPROVE_LIST_ALL.equals(sift)) {

            }
            //关注
            if (Constant.IMPROVE_LIST_FANS.equals(sift)){

            }
            //好友
            if (Constant.IMPROVE_LIST_FRIEND.equals(sift)){

            }
            //熟人
            if (Constant.IMPROVE_LIST_ACQUAINTANCE.equals(sift)){

            }
            improves = improveMapper.selectListByBusinessid
                    (rankid, Constant_table.IMPROVE_RANK,null,pageNo,pageSize);
            initImproveListOtherInfo(improves);
            if(null == improves){
                improves = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("selectRankImproveList userid:{} rankid:{} is error:{}",userid,rankid,e);
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
    public List<Improve> selectRankImproveListByDate(String userid, String rankid,String sift, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            //全部
            if (Constant.IMPROVE_LIST_ALL.equals(sift)) {

            }
            //关注
            if (Constant.IMPROVE_LIST_FANS.equals(sift)){

            }
            //好友
            if (Constant.IMPROVE_LIST_FRIEND.equals(sift)){

            }
            //熟人
            if (Constant.IMPROVE_LIST_ACQUAINTANCE.equals(sift)){

            }
            improves = improveMapper.selectListByBusinessid
                    (rankid, Constant_table.IMPROVE_RANK,"1",pageNo,pageSize);
            initImproveListOtherInfo(improves);
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
    public List<Improve> selectCircleImproveList(String userid, String circleid,String sift, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            //全部
            if (Constant.IMPROVE_LIST_ALL.equals(sift)) {

            }
            //关注
            if (Constant.IMPROVE_LIST_FANS.equals(sift)){

            }
            //好友
            if (Constant.IMPROVE_LIST_FRIEND.equals(sift)){

            }
            //熟人
            if (Constant.IMPROVE_LIST_ACQUAINTANCE.equals(sift)){

            }
            improves = improveMapper.selectListByBusinessid
                    (circleid, Constant_table.IMPROVE_CIRCLE,null,pageNo,pageSize);
            initImproveListOtherInfo(improves);
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
    public List<Improve> selectCircleImproveListByDate(String userid, String circleid,String sift, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            //全部
            if (Constant.IMPROVE_LIST_ALL.equals(sift)) {
            }
            //关注
            if (Constant.IMPROVE_LIST_FANS.equals(sift)){

            }
            //好友
            if (Constant.IMPROVE_LIST_FRIEND.equals(sift)){

            }
            //熟人
            if (Constant.IMPROVE_LIST_ACQUAINTANCE.equals(sift)){

            }
            improves = improveMapper.selectListByBusinessid
                    (circleid, Constant_table.IMPROVE_CIRCLE,"1",pageNo,pageSize);
            initImproveListOtherInfo(improves);
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
    public List<Improve> selectClassroomImproveList(String userid, String classroomid,String sift, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            //全部
            if (Constant.IMPROVE_LIST_ALL.equals(sift)) {
            }
            //关注
            if (Constant.IMPROVE_LIST_FANS.equals(sift)){

            }
            //好友
            if (Constant.IMPROVE_LIST_FRIEND.equals(sift)){

            }
            //熟人
            if (Constant.IMPROVE_LIST_ACQUAINTANCE.equals(sift)){

            }
            improves = improveMapper.selectListByBusinessid
                    (classroomid, Constant_table.IMPROVE_CLASSROOM,null,pageNo,pageSize);
            initImproveListOtherInfo(improves);
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
    public List<Improve> selectClassroomImproveListByDate(String userid, String classroomid,String sift, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            //全部
            if (Constant.IMPROVE_LIST_ALL.equals(sift)) {
            }
            //关注
            if (Constant.IMPROVE_LIST_FANS.equals(sift)){

            }
            //好友
            if (Constant.IMPROVE_LIST_FRIEND.equals(sift)){

            }
            //熟人
            if (Constant.IMPROVE_LIST_ACQUAINTANCE.equals(sift)){

            }

            improves = improveMapper.selectListByBusinessid
                    (classroomid, Constant_table.IMPROVE_CLASSROOM,"1",pageNo,pageSize);
            initImproveListOtherInfo(improves);
        } catch (Exception e) {
            logger.error("selectClassroomImproveListByDate userid:{} classroomid:{} is error:{}",userid,classroomid,e);
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
    public List<Improve> selectGoalImproveList(String userid, String goalid, int pageNo, int pageSize) {
        List<Improve> improves = null;
        try {
            improves = improveMapper.selectListByBusinessid
                    (goalid, Constant_table.IMPROVE_GOAL,null,pageNo,pageSize);
            initImproveListOtherInfo(improves);
        } catch (Exception e) {
            logger.error("selectGoalImproveList userid:{} goalid:{} is error:{}",userid,goalid,e);
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
    public boolean removeImprove(String userid,String improveid,
                                 String businesstype,String businessid) {

        boolean isok = false;
        if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
            isok = removeSingleImprove(userid,improveid);
        }
        if(Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
            isok = removeRankImprove(userid,businessid,improveid);
        }
        if(Constant.IMPROVE_CLASSROOM_TYPE.equals(businesstype)){
            isok = removeClassroomImprove(userid,businessid,improveid);
        }
        if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
            isok = removeCircleImprove(userid,businessid,improveid);
        }
        if(Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
            isok = removeGoalImprove(userid,businessid,improveid);
        }
        return isok;
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
        try {
            res = improveRankMapper.remove(userid,rankid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: rankid:{} improveid:{} userid:{} is error:{}",
                    rankid,improveid,userid,e);
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
        try {
            res = improveGoalMapper.remove(userid,goalid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: goalid:{} improveid:{} userid:{} is error:{}",
                    goalid,improveid,userid,e);
        }
        if(res != 0){
            //删除成功之后
            springJedisDao.sRem(Constant.RP_IMPROVE_NDAY+goalid,improveid+DateUtils.getDate("yyyy-MM-dd"));
            long n = springJedisDao.sCard(Constant.RP_IMPROVE_NDAY+goalid);

            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            return true;
        }
        return false;
    }

    @Override
    public List<Improve> selectImproveListByUser(String userid,Date lastdate,int pagesize) {


        List<TimeLine> timeLines = timeLineDao.selectTimeListByUserAndType
                (userid,Constant.TIMELINE_IMPROVE_SELF,lastdate,pagesize);

        List<Improve> improves = new ArrayList<>();

        for (int i = 0; i < timeLines.size() ; i++){
            TimeLine timeLine = timeLines.get(i);
            TimeLineDetail timeLineDetail = timeLine.getTimeLineDetail();
            Improve improve = new Improve();
            improve.setImpid(timeLineDetail.getImproveId());

        }
        return null;
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


    private void initImproveListOtherInfo(List<Improve> improves){
        if(null == improves || 0 == improves.size()){
            return;
        }
        for (Improve improve : improves){

            initImproveAttachInfo(improve);
            initImproveUserInfo(improve);

        }
    }


    /**
     * 向improve中的赞，献花，钻石，评论数赋值
     * @param improve
     * @author:luye
     */
    private void initImproveAttachInfo(Improve improve){

        //对进步的评论数赋值
        BaseResp<Object> baseResp = commentMongoService.selectCommentCountSum
                        (String.valueOf(improve.getId()),Constant.COMMENT_SINGLE_TYPE);
        if (ResultUtil.isSuccess(baseResp)){
            improve.setCommentnum((Integer)baseResp.getData());
        } else {
            improve.setCommentnum(0);
        }

        //对进步的赞数赋值


        //对进步的鲜花数赋值


        //对进步的钻石数赋值


    }

    /**
     * 初始化进步中用户信息
     * @param improve
     * @author:luye
     */
    private void initImproveUserInfo(Improve improve){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(improve.getUserid()));
        improve.setAppUserMongoEntity(appUserMongoEntity);
    }







}
