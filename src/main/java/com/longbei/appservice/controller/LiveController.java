package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.LiveGift;
import com.longbei.appservice.service.impl.LiveGiftServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lixb on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/live")
public class LiveController {

    private static Logger logger = LoggerFactory.getLogger(LiveController.class);

    @Autowired
    private LiveGiftServiceImpl liveGiftService;


    /**
     *
     * @param startnum
     * @param endnum
     * @return
     */
    @SuppressWarnings("unchecked")
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
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
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
