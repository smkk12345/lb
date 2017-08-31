package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.syscache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.utils.ShortUrlUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.config.AppserviceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by smkk on 17/2/9.
 */
@Controller
@RequestMapping(value = "/init")
public class SysInitController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(SysInitController.class);
    /**
     * http://ip:port/app_service/init
     * @return
     */
    @RequestMapping(value = "")
    @ResponseBody
    public BaseResp<Object> init() {
        BaseResp<Object> baseResp = new BaseResp<>();
        try
        {
            //系统规则
            baseResp.getExpandData().put("sysRules", SysRulesCache.behaviorRule);
            //十全十美菜单
            baseResp.getExpandData().put("perfectmenus",SysRulesCache.perfectTenList);
            //oss路径
            if(StringUtils.isBlank(AppserviceConfig.oss_media)){
                baseResp.getExpandData().put("osspath", "http://longbei-test-media-out.oss-cn-beijing.aliyuncs.com/");
            }else{
                baseResp.getExpandData().put("osspath", AppserviceConfig.oss_media);
            }
            //帮助中心url
            baseResp.getExpandData().put("helperurl",AppserviceConfig.h5_helper);
            //协议
            baseResp.getExpandData().put("agreementurl",AppserviceConfig.h5_agreementurl);

            baseResp.getExpandData().put("invitepoint", Constant_point.INVITE_LEVEL1);
            baseResp.getExpandData().put("inviteimp", Constant_Imp_Icon.INVITE_LEVEL1);

            baseResp.getExpandData().put("registerurl",
                    ShortUrlUtils.getShortUrl(AppserviceConfig.h5_invite));
            baseResp.getExpandData().put("pcurl",AppserviceConfig.pcurl);

            baseResp.getExpandData().put("classosspath", AppserviceConfig.oss_media);

            baseResp.getExpandData().put("livestart",30);
            baseResp.getExpandData().put("liveend",30);

        }catch (Exception e){
            logger.error("initerror",e);
        }
        //多媒体前缀
//        baseResp.getExpandData().put("mediapath",Constant.OSS_MEDIA);
        //初始化操作
        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        return baseResp;
    }

}
