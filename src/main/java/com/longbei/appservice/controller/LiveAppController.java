package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.LiveGift;
import com.longbei.appservice.service.LiveGiftService;
import com.longbei.appservice.service.impl.ClassroomServiceImpl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lixb on 2017/8/23.
 * 前端app接口
 */
@RestController
@RequestMapping(value = "/online")
public class LiveAppController{

    private static Logger logger = LoggerFactory.getLogger(LiveAppController.class);

    @Autowired
    private ClassroomServiceImpl classroomService;
//    @Autowired
//    private LiveInfoMongoService liveInfoMongoService;
    @Autowired
    private LiveGiftService liveGiftService;
    
    /**
     * url online/startOnLineRoom
     * 开始直播
     * @param roomid
     * @param courseid
     * @param userid
     * @return
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="startOnLineRoom")
    public BaseResp startOnLineRoom(String roomid, String courseid, String userid){
        logger.info("startOnLineRoom roomid:{} courseid:{} userid:{}",
                roomid,courseid,userid);
        BaseResp baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(roomid,courseid,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        //处理教室开启直播逻辑
        baseResp = classroomService.updateOnlineStatus(roomid,courseid,userid,"1");
        return baseResp;
    }

    /**
     * url online/closeOnLineRoom
     * 关闭直播间 结束直播
     * @param roomid
     * @param courseid
     * @param userid
     * @return
     */
    @RequestMapping(value="closeOnLineRoom")
    public BaseResp closeOnLine(String roomid,String courseid,String userid){
        logger.info("closeOnLineRoom roomid:{} courseid:{} userid:{}",
                roomid,courseid,userid);
        BaseResp baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(roomid,courseid,userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        //处理关闭教室直播逻辑
        baseResp = classroomService.updateOnlineStatus(roomid,courseid,userid,"2");

        return baseResp.initCodeAndDesp();
    }

    @RequestMapping(value = "giftList")
    public BaseResp<List<LiveGift>> giftList(String startnum, String endnum) {
        logger.info("giftList startNum={},endNum={}",startnum,endnum);
        BaseResp<List<LiveGift>> baseResp = new BaseResp<>();
        int startn = 0;
        if(!StringUtils.isBlank(startnum)){
            startn = Integer.parseInt(startnum);
        }
        int endn = 15;
        if(!StringUtils.isBlank(endnum)){
            endn = Integer.parseInt(endnum);
        }
        try {
            baseResp = liveGiftService.selectList(startn,endn);
        } catch (Exception e) {
            logger.error("giftList startNum={},endNum={}",startnum,endnum,e);
        }
        return baseResp;
    }

    /**
     * 送礼物
     * @param giftid
     * @param fromuid
     * @param num
     * @param touid
     * @param classroomid 教室id
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "giveGift")
    public BaseResp<Object> giveGift(String giftid,
                                       String fromuid,
                                       String num,
                                       String touid,
                                       String classroomid) {
        logger.info("giveGift giftId={},fromUid={},num={},toUId={}" +
                "",giftid,fromuid,num,touid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(giftid,fromuid,num,touid,classroomid)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        try {
            baseResp = liveGiftService.giveGift(
                    Long.parseLong(giftid),
                    Long.parseLong(fromuid),
                    Integer.parseInt(num),
                    Long.parseLong(touid),
                    Long.parseLong(classroomid),
                    Constant.IMPROVE_CLASSROOM_TYPE);
        } catch (Exception e) {
            logger.error("giveGift giftId={},fromUid={},num={},toUId={}",giftid,fromuid,num,touid,e);
        }
        return baseResp;
    }

}
