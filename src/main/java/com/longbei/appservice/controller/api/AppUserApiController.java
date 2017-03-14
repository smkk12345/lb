package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.UserLevelService;
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
    @Autowired
    private UserLevelService userLevelService;

    private static Logger logger = LoggerFactory.getLogger(AppUserApiController.class);


    /**
	 * @author yinxc
	 * 获取用户信息---屏蔽私密的字段token
	 * 2017年3月14日
	 * @param userid
	 */
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
    
    /**
	 * @author yinxc
	 * 获取用户等级详情信息---用户享受的折扣
	 * 2017年3月14日
	 * @param userid
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "selectUserLevel")
    public BaseResp<UserLevel> selectUserLevel(String userid){
        BaseResp<UserLevel> baseResp = new BaseResp<UserLevel>();
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userLevelService.selectByUserid(Long.parseLong(userid));
        } catch (Exception e) {
            logger.error("selectUserLevel userid = {}", userid, e);
        }
        return baseResp;
    }

}
