package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.controller.AppUserController;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户详情接口
 * Created by yinxc on 2017/3/13.
 */
@RestController
@RequestMapping(value = "api/user")
public class AppUserApiController {

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(AppUserApiController.class);


    @SuppressWarnings("unchecked")
	@RequestMapping(value = "userDetail")
    public BaseResp<UserInfo> userDetail(String userid){
        BaseResp<UserInfo> baseResp = new BaseResp<UserInfo>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userService.selectInfoMore(Long.parseLong(userid));
        } catch (Exception e) {
            logger.error("userDetail userid = {}", userid, e);
        }
        return baseResp;
    }

}
