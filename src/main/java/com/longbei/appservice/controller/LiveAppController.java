package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.impl.ClassroomServiceImpl;
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
    /**
     * url online/startOnLineRoom
     * 开始直播
     * @param roomid
     * @param courseid
     * @param userid
     * @return
     */
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

}
