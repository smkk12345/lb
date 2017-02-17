package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.SysLongbeiinfo;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统设置相关
 * 公司相关  app版本更新相关 敏感词相关
 * Created by smkk on 17/2/17.
 */
@Controller
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
    @ResponseBody
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
}
