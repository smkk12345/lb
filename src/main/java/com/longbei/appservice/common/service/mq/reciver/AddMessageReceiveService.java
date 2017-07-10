package com.longbei.appservice.common.service.mq.reciver;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.activemq.ActivemqJmsConsumer;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.MongoUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserRelationService;
import com.longbei.appservice.service.impl.ImproveServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.text.ParseException;
import java.util.*;


/**
 * mq 添加消息接收
 * Created by luye on 2017/1/18.
 */
@Service
public class AddMessageReceiveService implements MessageListener{

    private static Logger logger = LoggerFactory.getLogger(AddMessageReceiveService.class);

    @Autowired
    private ImproveService improveService;
    @Autowired
    private TimeLineDao timeLineDao;
    @Autowired
    private TimeLineDetailDao timeLineDetailDao;
    @Autowired
    private UserRelationService relationService;


    /**
     * 接收mq消息
     * @author luye
     * @param msg 消息内容
     */
//    @JmsListener(destination="${spring.activemq.queue.name.add}")
//    public void receiveMessage(String msg){
//
//        System.out.println("监听接收到的消息是:"+msg);//打印队列内的消息
//        if (StringUtils.isBlank(msg)
//                || msg.indexOf(",") == -1
//                || msg.split(",").length < 5
//                || msg.split(",").length > 5){
//            return;
//        }
//        //id,businesstype,businessid,userid,date
//        String []content = msg.split(",");
//        //提取消息中的内容
//        String id = content[0];
//        String businesstype = content[1];
//        String businessid = content[2];
//        String userid = content[3];
//        String date = content[4];
//
//        Date creatdate = null;
//        try {
//            creatdate = DateUtils.formatDate(date,"yyyy-MM-dd HH:mm:ss");
//        } catch (ParseException e) {
//            creatdate = new Date();
//            logger.error("string:{} to date is error:{}",date,e);
//        }
//
//        //保存时间线详情
//        insertTimeLineDetail(id,businesstype,businessid,userid,creatdate);
//    }


    /**
     * 添加时间线中节点详情
     * @author luye
     * @param id 进步id
     * @param businesstype 业务类型
     * @param businessid 业务id
     * @param userid 用户id
     * @param date 创建时间
     */
    private void insertTimeLineDetail(String id,String businesstype,String businessid,String userid,Date date){

        Improve improve = improveService.selectImproveByImpid(Long.parseLong(id),userid,businesstype,businessid);
        AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
        appUserMongoEntity.setId(userid);
        TimeLineDetail timeLineDetail = new TimeLineDetail();
        timeLineDetail.setUser(appUserMongoEntity);
        timeLineDetail.setImproveId(Long.parseLong(id));
        timeLineDetail.setBrief(improve.getBrief());
        timeLineDetail.setPhotos(improve.getPickey());
        timeLineDetail.setFileKey(improve.getFilekey());
        timeLineDetail.setSourcekey(improve.getSourcekey());
        timeLineDetail.setItype(improve.getItype());
        timeLineDetail.setBusinesstype(improve.getBusinesstype());
        timeLineDetail.setBusinessid(improve.getBusinessid());
        timeLineDetail.setIspublic(improve.getIspublic());
        timeLineDetail.setCreatedate(date);
        timeLineDetail.setDuration(improve.getDuration());
        timeLineDetail.setPicattribute(improve.getPicattribute());
        //保存详情
        timeLineDetailDao.save(timeLineDetail);
        //保存时间线
        insertTimeLine(userid,timeLineDetail,improve,"",date);

    }


    /**
     * 添加时间线
     * @author luye
     * @param userid 用户id
     * @param timeLineDetail 节点详情
     * @param remark 备注
     * @param createdate 创建日期
     */
    private void insertTimeLine(String userid,TimeLineDetail timeLineDetail,Improve improve,String remark,Date createdate){

        TimeLine timeLine = new TimeLine();
        timeLine.setTimeLineDetail(timeLineDetail);
        timeLine.setRemark(remark);
        timeLine.setCreatedate(createdate);
        timeLine.setBusinesstype(improve.getBusinesstype());
        timeLine.setPtype(improve.getPtype());
        timeLine.setBusinessid(improve.getBusinessid());
        timeLine.setIspublic(improve.getIspublic()==null?2:Integer.parseInt(improve.getIspublic()));
        switch (improve.getIspublic()){
            case Constant.IMPROVE_ISPUBLIC_2:
                //广场
                insertTimeLinePublic(timeLine);
                //我的
                insertTimeLineSelf(timeLine,userid);
                //动态
                insertTimeLineDyn(timeLine,userid);
                //好友
                insertTimeLineFriend(timeLine,userid);
                //关注
                insertTimeLineAttr(timeLine,userid);
                //熟人
                insertTimeLineAcq(timeLine,userid);
                break;
            case Constant.IMPROVE_ISPUBLIC_0:
                //
                insertTimeLineSelf(timeLine,userid);
                break;
            default:
                //我的
                insertTimeLineSelf(timeLine,userid);
                //动态
                insertTimeLineDyn(timeLine,userid);
                //好友
                insertTimeLineFriend(timeLine,userid);
                break;
        }
    }

    /**
     * 广场
     * @author luye
     * @param timeLine 时间线信息
     */
    private void insertTimeLinePublic(TimeLine timeLine){
        timeLine.setId(MongoUtils.UUID());
        timeLine.setUserid(Constant.SQUARE_USER_ID);
//        timeLine.setCtype("0");
        timeLineDao.save(timeLine,Constant.TIMELINE_IMPROVE_SQUARE_COLLECTION);
    }
    /**
     * 我的
     * @author luye
     * @param timeLine 时间线信息
     */
    private void insertTimeLineSelf(TimeLine timeLine,String userid){
        timeLine.setId(MongoUtils.UUID());
        timeLine.setUserid(userid);
//        timeLine.setCtype("1");
        timeLineDao.save(timeLine,Constant.TIMELINE_IMPROVE_SELF_COLLECTION);
    }

    /**
     * 动态
     * @author luye
     * @param timeLine 时间线信息
     */
    private void insertTimeLineDyn(TimeLine timeLine,String userid){
        Set<String> friendids = null;
        Set<String> funids = null;
        friendids = this.relationService.getFriendIds(userid);
        funids = this.relationService.getFansIds(userid);
        friendids.addAll(funids);
       for (String fid : friendids){
           timeLine.setId(MongoUtils.UUID());
           timeLine.setUserid(fid);
           timeLineDao.save(timeLine,Constant.TIMELINE_IMPROVE_ALL_COLLECTION);
       }
    }

    /**
     * 好友
     * @author luye
     * @param timeLine 时间线信息
     */
    private void insertTimeLineFriend(TimeLine timeLine,String userid){
        Set<String> friendids = null;
        friendids = this.relationService.getFriendIds(userid);
        for (String friendid : friendids) {
            timeLine.setId(MongoUtils.UUID());
            timeLine.setUserid(friendid);
            timeLineDao.save(timeLine,Constant.TIMELINE_IMPROVE_FRIEND_COLLECTION);
        }
    }
    /**
     * 关注
     * @author luye
     * @param timeLine 时间线信息
     */
    private void insertTimeLineAttr(TimeLine timeLine,String userid){
        Set<String> beFunIds = null;
        beFunIds = this.relationService.getBeFansedIds(userid);
        for (String funid : beFunIds) {
            timeLine.setId(MongoUtils.UUID());
            timeLine.setUserid(funid);
            timeLineDao.save(timeLine,Constant.TIMELINE_IMPROVE_ATTR_COLLECTION);
        }
    }
    /**
     * 熟人
     * @author luye
     * @param timeLine 时间线信息
     */
    private void insertTimeLineAcq(TimeLine timeLine,String userid){

    }

    @Override
    public void onMessage(Message message) {
        try{
            String action = message.getStringProperty("action");
            if(action.equals(Constant.MQACTION_IMPROVE)){
                String domainName = message.getStringProperty("domainName");
                String msg = message.getStringProperty("ids");
                logger.info("onMessage sucess and msg={}",msg);
                //TODO 连续二次重复的split没必要， 太浪费了； 消息大的话一堆的临时对象
                String []content = msg.split(",");
                if (StringUtils.isBlank(msg)
                        || msg.indexOf(",") == -1
                        || content.length < 5
                        || content.length > 5){
                    return;
                }
                //提取消息中的内容
                String id = content[0];
                String businesstype = content[1];
                String businessid = content[2];
                String userid = content[3];
                String date = content[4];
                Date creatdate = null;
                try {
                    creatdate = DateUtils.formatDate(date,"yyyy-MM-dd HH:mm:ss");
                } catch (ParseException e) {
                    creatdate = new Date();
                    //TODO ,logger参数 试用错误
                    logger.error("string:{} to date is error:{}",date,e);
                }
                //保存时间线详情
                insertTimeLineDetail(id,businesstype,businessid,userid,creatdate);
                //好友关系
            }else if(action.equals(Constant.MQACTION_USERRELATION)){
                String domainName = message.getStringProperty("domainName");
                String msg = message.getStringProperty("ids");
                if(domainName.equals(Constant.MQDOMAIN_USER_UPDATE)){//更新用户所有好友的信息流
                    relationService.syncUserRelationInfo(msg);
                }else if(domainName.equals(Constant.MQDOMAIN_USER_ADDFRIEND)){
                    String[] sArr = msg.split("&");
                    String uid = sArr[0];
                    String friendid = sArr[1];
                    relationService.syncUserRelationInfo(uid,friendid);
                    relationService.syncUserRelationInfo(friendid,uid);
                }else if(domainName.equals(Constant.MQDOMAIN_USER_ADDFUN)){
                    String[] sArr = msg.split("&");
                    String uid = sArr[0];
                    String friendid = sArr[1];
                    relationService.syncUserRelationInfo(uid,friendid);
                }else if(domainName.equals(Constant.MQDOMAIN_USER_REMOVEFRIEND)){
                    String[] sArr = msg.split("&");
                    String uid = sArr[0];
                    String friendid = sArr[1];
                    relationService.syncUserRelationInfo(uid,friendid);
                }else if(domainName.equals(Constant.MQDOMAIN_USER_REMOVEFUN)){
                    String[] sArr = msg.split("&");
                    String uid = sArr[0];
                    String friendid = sArr[1];
                    relationService.syncUserRelationInfo(uid,friendid);
                }
            }
        }catch (Exception e){
            logger.error("",message.toString());
        }

    }

}
