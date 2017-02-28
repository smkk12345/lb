package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.CircleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangyongzhi on 17/2/28.
 */
@RestController
@RequestMapping(value="circle")
public class CircleController {

    @Autowired
    private CircleService circleService;
    private static Logger logger = LoggerFactory.getLogger(CircleController.class);

    /**
     * @url http://ip:port/app_service/circle/relevantCircle
     * @param circleName 圈子名称
     * @param startNo 开始下标
     * @param endNo 结束下标
     * @author wangyongzhi
     * @return
     */
    @RequestMapping(value="relevantCircle")
    public BaseResp<Object> relevantCircle(String circleName,Integer startNo,Integer endNo){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.hasBlankParams(circleName)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        if(startNo == null || startNo < 0){
            startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        Integer pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        if(endNo != null && endNo > startNo){
            pageSize = endNo = startNo;
        }
        logger.info("query relevantCircle circleName:{}, startNo:{},pageSize:{} ",circleName,startNo,pageSize);
        baseResp = circleService.relevantCircle(circleName,startNo,pageSize);
        return baseResp;
    }

    /**
     * @url http://ip:port/app_service/circle/insertCircle
     * @param userId 用户Id
     * @param circleTitle 圈子名称
     * @param circlephotos 圈子图片
     * @param circlebrief 圈子简介
     * @param circleinvoloed 最大圈子人数
     * @param ptype 圈子类型
     * @param ispublic 是否所有人可见
     * @param needconfirm 加圈子是否需要圈主验证
     * @param creategoup 是否同时建龙信群
     * @author wangyongzhi
     * @return
     */
    @RequestMapping(value="insertCircle")
    public BaseResp<Object> insertCircle(String userId,String circleTitle,String circlephotos,String circlebrief,
                                         Integer circleinvoloed,String ptype,Boolean ispublic,Boolean needconfirm,Boolean creategoup){
        logger.info("insert Circle userId:{} circleTitle:{} circlephotos:{} circlebrief:{}",0,circleTitle,circlephotos,circlebrief);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.hasBlankParams(userId,circleTitle,circlephotos,circlebrief,ptype)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
            return baseResp;
        }
        if(ispublic == null){
            ispublic = true;
        }
        if(needconfirm == null){
            needconfirm = true;
        }
        if(creategoup == null){
            creategoup = false;
        }
        //校验兴趣圈名是否可用
        boolean flag = circleService.checkCircleTitle(circleTitle);
        if(flag){
            baseResp = circleService.insertCircle(userId,circleTitle,circlephotos,circlebrief,circleinvoloed,ptype,ispublic,needconfirm,creategoup);
        }else{
            baseResp = baseResp.initCodeAndDesp(Constant.STATUS_SYS_60, Constant.RTNINFO_SYS_60);
        }
        return baseResp;
    }



}
