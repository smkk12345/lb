package com.longbei.appservice.service.impl;


import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
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
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.MoneyService;
import com.longbei.appservice.service.UserBehaviourService;
import net.sf.json.JSONObject;
import org.apache.catalina.User;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.immutable.Stream;

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
//        improve.setFilekey(filekey);
        improve.setSourcekey(filekey);
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
//            userBehaviourService.levelUp(Long.parseLong(userid), SysRulesCache.sysRules.getAddimprove(),ptype);
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
            improve = selectImprove(impid,userid,businesstype,businessid,null,null);
        } catch (Exception e) {

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

    private Improve selectImprove(Long impid,String userid,
                    String businesstype,String businessid, String isdel,String ispublic){
        Improve improve = null;
        try {
            if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE,isdel,ispublic);
            }
            if(Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_RANK,isdel,ispublic);
            }
            if(Constant.IMPROVE_CLASSROOM_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_CLASSROOM,isdel,ispublic);
            }
            if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_CIRCLE,isdel,ispublic);
            }
            if(Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid,Constant_table.IMPROVE_GOAL,isdel,ispublic);
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
            initImproveListOtherInfo(userid,improves);
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
            initImproveListOtherInfo(userid,improves);
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
            initImproveListOtherInfo(userid,improves);
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
            initImproveListOtherInfo(userid,improves);
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
    public List<Improve> selectImproveListByUser(String userid,String ctype,Date lastdate,int pagesize) {

        List<TimeLine> timeLines = timeLineDao.selectTimeListByUserAndType
                (userid,ctype,lastdate,pagesize);
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
            //初始化赞，花，钻数量
            initImproveAttachInfo(improve);
            //初始化点赞，送花，送钻简略信息
            initLikeFlowerDiamondInfo(improve);
            //初始化是否 点赞 送花 送钻 收藏
            initIsOptionForImprove(userid,improve);
            improves.add(improve);
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
     */
    private void initImproveListOtherInfo(String userid,List<Improve> improves){
        if(null == improves || 0 == improves.size()){
            return;
        }
        for (Improve improve : improves){
            //初始化赞，花，钻数量
            initImproveAttachInfo(improve);
            //初始化进步用户信息
            initImproveUserInfo(improve);
            //初始化点赞，送花，送钻简略信息
            initLikeFlowerDiamondInfo(improve);
            //初始化是否 点赞 送花 送钻 收藏
            initIsOptionForImprove(userid,improve);
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
     */
    @Override
    public BaseResp<Object> addlike(String userid,String impid,String businesstype,String businessid){

        BaseResp<Object> baseResp = new BaseResp<>();
        //防止重复提交
        if(isExitsForRedis(impid,userid)){
            return baseResp;
        }
        Improve improve = selectImprove(Long.parseLong(impid),userid,businesstype,businessid,null,null);
        AppUserMongoEntity userMongoEntity = userMongoDao.getAppUser(userid);
        if(null == improve||null == userMongoEntity){
            return baseResp;
        }
        if(improve.getUserid().equals(userid)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_13,Constant.RTNINFO_SYS_13);
            return  baseResp;
        }
//        baseResp = userBehaviourService.canOperateMore(Long.parseLong(userid),null,Constant.PERDAY_ADD_LIKE);
//        if(!ResultUtil.isSuccess(baseResp)){
//            return baseResp;
//        }
        try{
            //mysql相关操作
            boolean flag = addLikeToImprove(userid,impid,businessid,businesstype);
            if (flag){
                //redis
                addLikeOrFlowerOrDiamondToImproveForRedis(impid,userid,Constant.IMPROVE_ALL_DETAIL_LIKE);
                //mongo
                addLikeToImproveForMongo(impid,userid,Constant.MONGO_IMPROVE_LFD_OPT_LIKE)  ;
            }
            return BaseResp.ok();
        }catch (Exception e){
            logger.error("addlike error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> cancelLike(String userid, String impid, String businesstype, String businessid) {
        BaseResp baseResp = canCancelLike(userid,impid,businesstype);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }
        try {
            boolean flag = removeListToImprove(userid,impid,businessid,businesstype);
            if (flag){
                //redis
                removeLikeToImproveForRedis(impid,userid);
                //mongo
                removeLikeToImproveForMongo(impid,userid,Constant.MONGO_IMPROVE_LFD_OPT_LIKE)  ;
            }
            return BaseResp.ok();
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
            if(n == 1){
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
     * @param endNum
     * @return
     */
    @Override
    public BaseResp<Object> selectCollect(String userid, int startNum, int endNum) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            List<UserCollect> list  = userCollectMapper.selectCollect(Long.parseLong(userid),startNum,endNum);
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
                //合法  再做初始化
                if(improve.getIsdel().equals("1")&&improve.getIspublic().equals("1")){
                    initImproveAttachInfo(improve);
                    initImproveUserInfo(improve);
                }
                resultList.add(improve);
            }
            baseResp.setData(resultList);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectCollect error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> addFlower(String userid, String impid,
                                      int flowernum,String businesstype,String businessid) {
        //判断龙币是否充足
        BaseResp baseResp = moneyService.isEnoughLongMoney(userid,flowernum*Constant.FLOWER_PRICE);
        if (!ResultUtil.isSuccess(baseResp)){
            return baseResp;
        }

        //消耗龙币

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

                //赠送龙分操作



                return BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("add flower is error:{}",e);
        }
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_48,Constant.RTNINFO_SYS_48);
    }

    @Override
    public BaseResp<Object> addDiamond(String userid, String impid,
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



                return BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("add Diamond is error:{}",e);
        }

        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_49,Constant.RTNINFO_SYS_49);
    }


    @Override
    public BaseResp<List<ImpAllDetail>> selectImproveLFDList(String impid, String listtype,
                                                             int pagesize, Date lastdate) {

        BaseResp<List<ImpAllDetail>>  baseResp = new BaseResp<>();
        try {
            List<ImpAllDetail> impAllDetails = impAllDetailMapper.selectList(impid,listtype,pagesize,lastdate);
            for (ImpAllDetail impAllDetail : impAllDetails) {
                impAllDetail.setAppUser(userMongoDao.findById(String.valueOf(impAllDetail.getUserid())));
            }
            baseResp = BaseResp.ok();
            baseResp.setData(impAllDetails);
            return baseResp;
        } catch (Exception e) {
            logger.error("select improveLFDList is error:{}",e);
        }
        return baseResp;
    }

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


    private BaseResp canCancelLike(String userid,String impid,String businesstype){
        BaseResp baseResp = new BaseResp();
        ImpAllDetail impAllDetail = new ImpAllDetail();
        impAllDetail.setUserid(Long.parseLong(userid));
        impAllDetail.setImpid(Long.parseLong(impid));
        impAllDetail.setGtype(businesstype);
        impAllDetail.setDetailtype(Constant.IMPROVE_ALL_DETAIL_LIKE);
        try {
            List<ImpAllDetail> impAllDetails = impAllDetailMapper.selectOneDetail(impAllDetail);
            if (null != impAllDetails && impAllDetails.size() > 0){
                return BaseResp.ok();
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
     */
    private boolean isExitsForRedis(String impid,String userid){
        String result = springJedisDao.get("improve_like_temp_"+impid+userid);
        if (null == result || !"1".equals(result)) {
            return false;
        }
        return true;
    }

    /**
     * 将点赞放入redis
     * @return
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
        springJedisDao.set("improve_like_temp_"+impid+userid,"1",1);
        springJedisDao.putAll(Constant.REDIS_IMPROVE_LFD + impid,map,30*24*60*60*1000);
    }

    /**
     * 取消赞
     * @param impid
     * @param userid
     */
    private void removeLikeToImproveForRedis(String impid,String userid){
        springJedisDao.del("improve_like_temp_"+impid+userid);
        springJedisDao.delete(Constant.REDIS_IMPROVE_LFD + impid,"like"+impid);
    }

    /**
     * 将点赞,送花，送钻放入mongo
     * @param opttype 0 - 点赞  1 - 送花  3 - 送钻
     * @return
     */
    private void addLikeToImproveForMongo(String impid,String userid,String opttype){
        ImproveLFD improveLFD = new ImproveLFD();
        improveLFD.setImpid(impid);
        improveLFD.setUserid(userid);
        improveLFD.setOpttype(opttype);
        AppUserMongoEntity user = new AppUserMongoEntity();
        user.setId(userid);
        improveLFD.setAppUser(user);
        improveLFD.setCreatetime(new Date());
        improveMongoDao.saveImproveLfd(improveLFD);
    }

    private void removeLikeToImproveForMongo(String impid,String userid,String opttype){
        ImproveLFD improveLFD = new ImproveLFD();
        improveLFD.setImpid(impid);
        improveLFD.setUserid(userid);
        improveLFD.setOpttype(opttype);
        improveMongoDao.removeImproveLfd(improveLFD);
    }


    /**
     * 点赞mysql操作
     * @param userid
     * @param impid
     * @param businessid
     * @param businesstype
     * @return
     */
    private boolean addLikeToImprove(String userid,String impid,String businessid,String businesstype){
        int res = addImproveAllDetail(userid,impid,businesstype,null,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (res <= 0){
            return false;
        }
        res = improveMapper.updateLikes(impid,Constant.IMPROVE_LIKE_ADD,businessid,getTableNameByBusinessType(businesstype));
        if (res > 0 ){
            return true;
        }
        return false;
    }


    private boolean removeListToImprove(String userid,String impid,String businessid,String businesstype){
        int res = removeImproveAllDetail(userid,impid,businesstype,Constant.IMPROVE_ALL_DETAIL_LIKE);
        if (res <= 0){
            return false;
        }
        res = improveMapper.updateLikes(impid,Constant.IMPROVE_LIKE_CANCEL,businessid,getTableNameByBusinessType(businesstype));
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
        int res = 0;
        try {
            res = impAllDetailMapper.insertSelective(impAllDetail);
        } catch (Exception e) {
            logger.error("add improve all detailtype={} is error:{}",detailtype,e);
        }
        return res;
    }

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
     */
    private void initLikeFlowerDiamondInfo(Improve improve){
        Long count = improveMongoDao.selectCountImproveLFD(String.valueOf(improve.getImpid()));
        List<ImproveLFD> improveLFDs = improveMongoDao.selectImproveLfdList(String.valueOf(improve.getImpid()));
        improve.setLfdcount(count);
        improve.setImproveLFDs(improveLFDs);
    }


    private void initIsOptionForImprove(String userid,Improve improve){

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
        String[] filenameArr = key.split("_");
        if(filenameArr.length < 2){
            return baseResp;
        }
        String type = filenameArr[0];
        String businessid = filenameArr[1];
        if(type .equals(Constant.IMPROVE_SINGLE_TYPE)){
            businessid = null;
        }
        try{
            String tableName = getTableNameByBusinessType(type);
            int n = improveMapper.updateMedia(key,pickey,filekey,businessid,tableName);
            if(n > 0){
                timeLineDetailDao.updateImproveFileKey(key,pickey,filekey);
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
            }
        }catch (Exception e){
            logger.error("updateMedia error and key={},pickey={},filekey={},msg={}",key,pickey,filekey,e);
        }
        return baseResp;
    }

}
