package com.longbei.appservice.service.impl;


import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.persistence.CustomizedPropertyConfigurer;
import com.longbei.appservice.common.service.mq.send.QueueMessageSendService;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

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
    private ImproveTopicMapper improveTopicMapper;
    @Autowired
    private QueueMessageSendService queueMessageSendService;

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
        if(Constant.IMPROVE_TOPIC_TYPE.equals(businesstype)){
            improve.setBusinessid(Long.parseLong(businessid));
            ImproveTopic improveTopic = new ImproveTopic();
            try {
                BeanUtils.copyProperties(improveTopic,improve);
            } catch (Exception e) {
                logger.error("bean properties copy is error:{}",e);
            }
            isok = insertImproveForTopic(improveTopic);
        }
        return isok;
    }

    @Override
    public boolean insertImproveSingle(Improve improve) {
        int res = 0;
        try {
            res = improveMapper.insertSelective(improve);
        } catch (Exception e) {
            logger.error("insert sigle immprove:{} is error:{}", JSONObject.fromObject(improve).toString(),e);
        }
        if(res != 0){
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_ADD_NAME),
//                    "message");
            return true;
        }
        return false;
    }

    @Override
    public boolean insertImproveForCircle(ImproveCircle improveCircle) {
        int res = 0;
        try {
            res = improveCircleMapper.insertSelective(improveCircle);
        } catch (Exception e) {
            logger.error("insert circle immprove:{} is error:{}", JSONObject.fromObject(improveCircle).toString(),e);
        }
        if(res != 0){
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_ADD_NAME),
//                    "message");
            return true;
        }
        return false;
    }

    @Override
    public boolean insertImproveForClassroom(ImproveClassroom improveClassroom) {
        int res = 0;
        try {
            res = improveClassroomMapper.insertSelective(improveClassroom);
        } catch (Exception e) {
            logger.error("insert classroom immprove:{} is error:{}", JSONObject.fromObject(improveClassroom).toString(),e);
        }
        if(res != 0){
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_ADD_NAME),
//                    "message");
            return true;
        }
        return false;
    }

    @Override
    public boolean insertImproveForRank(ImproveRank improveRank) {
        int res = 0;
        try {
            res = improveRankMapper.insertSelective(improveRank);
        } catch (Exception e) {
            logger.error("insert rank immprove:{} is error:{}", JSONObject.fromObject(improveRank).toString(),e);
        }
        if(res != 0){
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_ADD_NAME),
//                    "message");
            return true;
        }
        return false;
    }

    @Override
    public boolean insertImproveForTopic(ImproveTopic improveTopic) {
        int res = 0;
        try {
            res = improveTopicMapper.insertSelective(improveTopic);
        } catch (Exception e) {
            logger.error("insert topic immprove:{} is error:{}", JSONObject.fromObject(improveTopic).toString(),e);
        }
        if(res != 0){
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_ADD_NAME),
//                    "message");
            return true;
        }
        return false;
    }

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
        if(Constant.IMPROVE_TOPIC_TYPE.equals(businesstype)){
            isok = removeTopicImprove(userid,businessid,improveid);
        }
        return isok;
    }

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
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_UPDATE_NAME),
//                    "message");
            return true;
        }
        return false;
    }

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
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_UPDATE_NAME),
//                    "message");
            return true;
        }
        return false;
    }

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
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_UPDATE_NAME),
//                    "message");
            return true;
        }
        return false;
    }

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
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_UPDATE_NAME),
//                    "message");
            return true;
        }
        return false;
    }

    @Override
    public boolean removeTopicImprove(String userid, String topicid, String improveid) {
        int res = 0;
        try {
            res = improveTopicMapper.remove(userid,topicid,improveid);
        } catch (Exception e) {
            logger.error("remove rank immprove: topicid:{} improveid:{} userid:{} is error:{}",
                    topicid,improveid,userid,e);
        }
        if(res != 0){
            String message = "";
//            queueMessageSendService.send(
//                    CustomizedPropertyConfigurer.getContextProperty(Constant.QUEUE_UPDATE_NAME),
//                    "message");
            return true;
        }
        return false;
    }
}
