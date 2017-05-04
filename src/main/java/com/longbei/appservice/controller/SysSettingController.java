package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.SysAppupdate;
import com.longbei.appservice.entity.SysLongbeiinfo;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系统设置相关
 * 公司相关  app版本更新相关 敏感词相关
 * Created by smkk on 17/2/17.
 */
@RestController
@RequestMapping(value = "/syssetting")
public class SysSettingController {

    @Autowired
    private SysSettingService sysSettingService;

    private static Logger logger = LoggerFactory.getLogger(SysSettingController.class);

    /**
     * http://localhost:9090/app_service/syssetting/cinfo
     * @return
     */
    @RequestMapping(value = "cinfo")
    public BaseResp<Object> init() {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = sysSettingService.selectCompanyInfo();
        }catch (Exception e) {
            logger.error("");
        }
        //初始化操作
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        return baseResp;
    }

    /**
     *  lixb
     *  http://localhost:9090/app_service/syssetting/upgrade
     * @Title: upgrade
     * @Description: app更新接口
     * @param @param ttype     客户端类型 （0：android；1：ios）
     * @param @param version   客户端当前版本号
     * @param @return
     * @return Map<String,String> 返回值类型
     * @throws
     */
    @RequestMapping(value = "/upgrade")
    public BaseResp<Object> upgrade(String ttype,String version) {
        logger.info("upgrade ttype={},version={}",ttype,version);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(ttype,version)){
           return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = sysSettingService.upGrade(ttype,version);
        }catch (Exception e){
            logger.error("upgrade ttype={},version={}",ttype,version,e);
        }
        return baseResp;
    }

    /**
     * lixb
     * http://localhost:9090/app_service/syssetting/downloadApp
     * appType IOS 或者 安卓
     *下载跳转
     */
    @RequestMapping("downloadApp")
    public String testdownload(String appType) {
        logger.info("appTypee={}",appType);
        if(org.apache.commons.lang3.StringUtils.isBlank(appType)){
            return "";
        }
        SysAppupdate appUpdate = sysSettingService.selectRecentByKey(appType);
        return "redirect:" + appUpdate.getUrl();
    }

    /**
     * @api http://localhost:9090/app_service/syssetting/getShareInfo
     * 获取分享的相关信息
     * @return
     */
    @RequestMapping(value="getShareInfo")
    public BaseResp<Object> getShareInfo(){
        return this.sysSettingService.getShareInfo();
    }

}
