package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lixb on 2017/8/23.
 * 前端app接口
 */
public class LiveAppController{

    private static Logger logger = LoggerFactory.getLogger(LiveAppController.class);

    /**
     * 开启直播间
     * @param roomid
     * @param courseid
     * @param userid
     * @param duration
     * @return
     */
    @RequestMapping(value="startOnLineRoom")
    public BaseResp startOnLineRoom(String roomid, String courseid, String userid, String duration){
        logger.info("startOnLineRoom roomid:{} courseid:{} userid:{} duration:{}",
                roomid,courseid,userid,duration);
        BaseResp baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(roomid,courseid,userid,duration)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        //处理教室开启直播逻辑
        return baseResp.initCodeAndDesp();
    }

    /**
     * 关闭直播间
     * @param roomid
     * @param courseid
     * @param userid
     * @param duration
     * @return
     */
    @RequestMapping(value="closeOnLineRoom")
    public BaseResp closeOnLine(String roomid,String courseid,String userid,String duration){
        logger.info("closeOnLineRoom roomid:{} courseid:{} userid:{} duration:{}",
                roomid,courseid,userid,duration);
        BaseResp baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(roomid,courseid,userid,duration)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        //处理关闭教室直播逻辑
        return baseResp.initCodeAndDesp();
    }

}
