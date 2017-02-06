package com.longbei.appservice.service.impl;


import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.service.mq.send.QueueMessageSendService;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
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

    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public boolean insertImprove(String userid, String brief,
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

        boolean isok = false;
        if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
            isok = insertImproveSingle(improve);
        }
        if(Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
            improve.setBusinessid(Long.parseLong(businessid));
            ImproveRank improveRank = new ImproveRank();
            try {
                BeanUtils.copyProperties(improveRank,improve);
            } catch (Exception e) {
                logger.error("bean properties copy is error:{}",e);
            }
            isok = insertImproveForRank(improveRank);
        }
        if(Constant.IMPROVE_CLASSROOM_TYPE.equals(businesstype)){
            improve.setBusinessid(Long.parseLong(businessid));
            ImproveClassroom improveClassroom = new ImproveClassroom();
            try {
                BeanUtils.copyProperties(improveClassroom,improve);
            } catch (Exception e) {
                logger.error("bean properties copy is error:{}",e);
            }
            isok = insertImproveForClassroom(improveClassroom);
        }
        if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
            improve.setBusinessid(Long.parseLong(businessid));
            ImproveCircle improveCircle = new ImproveCircle();
            try {
                BeanUtils.copyProperties(improveCircle,improve);
            } catch (Exception e) {
                logger.error("bean properties copy is error:{}",e);
            }
            isok = insertImproveForCircle(improveCircle);
        }
        if(Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
            improve.setBusinessid(Long.parseLong(businessid));
            ImproveGoal improveGoal = new ImproveGoal();
            try {
                BeanUtils.copyProperties(improveGoal,improve);
            } catch (Exception e) {
                logger.error("bean properties copy is error:{}",e);
            }
            isok = insertImproveForGoal(improveGoal);
        }
        return isok;
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
            res = improveMapper.insertSelective(improve);
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
    public boolean insertImproveForCircle(ImproveCircle improveCircle) {

        if(!insertImproveFilter(improveCircle.getUserid(),Constant.IMPROVE_CIRCLE_TYPE)){
            return false;
        }
        int res = 0;
        try {
            res = improveCircleMapper.insertSelective(improveCircle);
        } catch (Exception e) {
            logger.error("insert circle immprove:{} is error:{}", JSONObject.fromObject(improveCircle).toString(),e);
        }
        if(res != 0){
            String message = improveCircle.getImpid() +
                    "," + Constant.IMPROVE_CIRCLE_TYPE +
                    "," + improveCircle.getBusinessid() +
                    "," + improveCircle.getUserid() +
                    "," + DateUtils.formatDateTime1(improveCircle.getCreatetime());
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
    public boolean insertImproveForClassroom(ImproveClassroom improveClassroom) {

        if(!insertImproveFilter(improveClassroom.getUserid(),Constant.IMPROVE_CLASSROOM_TYPE)){
            return false;
        }
        int res = 0;
        try {
            res = improveClassroomMapper.insertSelective(improveClassroom);
        } catch (Exception e) {
            logger.error("insert classroom immprove:{} is error:{}", JSONObject.fromObject(improveClassroom).toString(),e);
        }
        if(res != 0){
            String message = improveClassroom.getImpid() +
                    "," + Constant.IMPROVE_CLASSROOM_TYPE +
                    "," + improveClassroom.getBusinessid() +
                    "," + improveClassroom.getUserid() +
                    "," + DateUtils.formatDateTime1(improveClassroom.getCreatetime());

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
    public boolean insertImproveForRank(ImproveRank improveRank) {

        if(!insertImproveFilter(improveRank.getUserid(),Constant.IMPROVE_RANK_TYPE)){
            return false;
        }

        int res = 0;
        try {
            res = improveRankMapper.insertSelective(improveRank);
        } catch (Exception e) {
            logger.error("insert rank immprove:{} is error:{}", JSONObject.fromObject(improveRank).toString(),e);
        }
        if(res != 0){
            String message = improveRank.getImpid() +
                    "," + Constant.IMPROVE_RANK_TYPE +
                    "," + improveRank.getBusinessid() +
                    "," + improveRank.getUserid() +
                    "," + DateUtils.formatDateTime1(improveRank.getCreatetime());
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
    public boolean insertImproveForGoal(ImproveGoal improveGoal) {

        if(!insertImproveFilter(improveGoal.getUserid(),Constant.IMPROVE_GOAL_TYPE)){
            return false;
        }

        int res = 0;
        try {
            res = improveGoalMapper.insertSelective(improveGoal);
        } catch (Exception e) {
            logger.error("insert goal immprove:{} is error:{}", JSONObject.fromObject(improveGoal).toString(),e);
        }
        if(res != 0){
            String message = improveGoal.getImpid() +
                    "," + Constant.IMPROVE_GOAL_TYPE +
                    "," + improveGoal.getBusinessid() +
                    "," + improveGoal.getUserid() +
                    "," + DateUtils.formatDateTime1(improveGoal.getCreatetime());

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
    public Improve selectImproveByImpid(Long impid,String userid,
                                 String businesstype,String businessid) {

        Improve improve = null;
        try {
            if(Constant.IMPROVE_SINGLE_TYPE.equals(businesstype)){
                improve = improveMapper.selectByPrimaryKey(impid);
            }
            if(Constant.IMPROVE_RANK_TYPE.equals(businesstype)){
                improve = improveRankMapper.selectByPrimaryKey(impid);
            }
            if(Constant.IMPROVE_CLASSROOM_TYPE.equals(businesstype)){
                improve = improveClassroomMapper.selectByPrimaryKey(impid);
            }
            if(Constant.IMPROVE_CIRCLE_TYPE.equals(businesstype)){
                improve = improveCircleMapper.selectByPrimaryKey(impid);
            }
            if(Constant.IMPROVE_GOAL_TYPE.equals(businesstype)){
                improve = improveGoalMapper.selectByPrimaryKey(impid);
            }
        } catch (Exception e) {
            logger.error("select improve by userid:{}" +
                    "id:{} businesstype:{} businessid:{}",
                    userid,impid,businesstype,businessid);
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
    public List<ImproveRank> selectRankImproveList(String userid, String rankid, int pageNo, int pageSize) {
        List<ImproveRank> improveRanks = null;
        try {
            improveRanks = improveRankMapper.selectByRankId(rankid,null);
            if(null == improveRanks){
                improveRanks = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("selectRankImproveList userid:{} rankid:{} is error:{}",userid,rankid,e);
        }
        return improveRanks;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<ImproveRank> selectRankImproveListByDate(String userid, String rankid, int pageNo, int pageSize) {
        List<ImproveRank> improveRanks = null;
        try {
            improveRanks = improveRankMapper.selectByRankId(rankid,"1");
            if(null == improveRanks){
                improveRanks = new ArrayList<>();
            }
        } catch (Exception e) {
            logger.error("selectRankImproveListByDate userid:{} rankid:{} is error:{}",userid,rankid,e);
        }
        return improveRanks;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<ImproveCircle> selectCircleImproveList(String userid, String circleid, int pageNo, int pageSize) {
        List<ImproveCircle> improveCircles = null;
        try {
            improveCircles = improveCircleMapper.selectByCircleId(circleid,null);
        } catch (Exception e) {
            logger.error("selectCircleImproveList userid:{} circleid:{} is error:{}",userid,circleid,e);
        }
        return improveCircles;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<ImproveCircle> selectCircleImproveListByDate(String userid, String circleid, int pageNo, int pageSize) {
        List<ImproveCircle> improveCircles = null;
        try {
            improveCircles = improveCircleMapper.selectByCircleId(circleid,"1");
        } catch (Exception e) {
            logger.error("selectCircleImproveListByDate userid:{} circleid:{} is error:{}",userid,circleid,e);
        }
        return improveCircles;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:54
     *  @update 2017/1/23 下午4:54
     */
    @Override
    public List<ImproveClassroom> selectClassroomImproveList(String userid, String classroomid, int pageNo, int pageSize) {
        List<ImproveClassroom> improveClassrooms = null;
        try {
            improveClassrooms = improveClassroomMapper.selectByClassroomId(classroomid,null);
        } catch (Exception e) {
            logger.error("selectClassroomImproveList userid:{} classroomid:{} is error:{}",userid,classroomid,e);
        }
        return improveClassrooms;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public List<ImproveClassroom> selectClassroomImproveListByDate(String userid, String classroomid, int pageNo, int pageSize) {
        List<ImproveClassroom> improveClassrooms = null;
        try {
            improveClassrooms = improveClassroomMapper.selectByClassroomId(classroomid,"1");
        } catch (Exception e) {
            logger.error("selectClassroomImproveListByDate userid:{} classroomid:{} is error:{}",userid,classroomid,e);
        }
        return improveClassrooms;
    }
    /**
     *  @author luye
     *  @desp 
     *  @create 2017/1/23 下午4:55
     *  @update 2017/1/23 下午4:55
     */
    @Override
    public List<ImproveGoal> selectGoalImproveList(String userid, String goalid, int pageNo, int pageSize) {
        List<ImproveGoal> improveGoals = null;
        try {
            improveGoals = improveGoalMapper.selectByGoalId(goalid);
        } catch (Exception e) {
            logger.error("selectGoalImproveList userid:{} goalid:{} is error:{}",userid,goalid,e);
        }
        return improveGoals;
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
            String message = "updatetest";
            queueMessageSendService.sendUpdateMessage(message);
            return true;
        }
        return false;
    }

    /**
     * 能否发布进步过滤
     * @author:luye
     * @param userid
     * @param improvetype
     * @return
     * @author:luye
     */
    private boolean insertImproveFilter(Long userid,String improvetype){
        return true;
    }


    /**
     * 向improve中的赞，献花，钻石，评论数赋值
     * @param improve
     * @author:luye
     */
    private void initImproveAttachInfo(Improve improve){



    }

    /**
     * 初始化进步中用户信息
     * @param improve
     * @author:luye
     */
    private void initImproveUserInfo(Improve improve){

    }


    




}
