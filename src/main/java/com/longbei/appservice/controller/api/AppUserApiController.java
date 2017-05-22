package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.UserLevelService;
import com.longbei.appservice.service.UserRelationService;
import com.longbei.appservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public BaseResp<UserInfo> login(String username,String password){
        BaseResp<UserInfo> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(username, password)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userService.login(username,password);
        } catch (Exception e) {
            logger.error("pc login is error:",e);
        }
        return baseResp;
    }


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
            baseResp = userService.selectInfoMore(Long.parseLong(userid),0);
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

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/registerbasic")
    @ResponseBody
    public BaseResp<Object> registerbasic(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String inviteuserid = request.getParameter("inviteuserid");
        String randomCode = request.getParameter("randomCode");
        logger.info("registerbasic params username={},password={}", username, password);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(username, password, randomCode)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        baseResp = userService.checkSms(username,randomCode);
        if (baseResp.getCode() != Constant.STATUS_SYS_00) {
            return baseResp;
        }
        try {
            return userService.registerbasic(username, password, inviteuserid,null,null,null);
        } catch (Exception e) {
            logger.error("registerbasic error and msg = {}", e);
        }
        return baseResp;
    }


    /**
     * 获取用户列表
     * @param userInfo
     * @param order
     * @param ordersc
     * @param pageno
     * @param pagesize
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/userlist")
    @ResponseBody
    public BaseResp<Page<UserInfo>> selectUserList(@RequestBody UserInfo userInfo, String order,
                                                   String ordersc, String pageno, String pagesize){
        BaseResp<Page<UserInfo>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(ordersc)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = userService.selectUserList(userInfo,order,ordersc,Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select user list for pc is error:",e);
        }
        return baseResp;

    }

    /**
     * 更新用户状态 达人，封号等
     * @param userInfo
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateuser")
    @ResponseBody
    public BaseResp<Object> updateUserStatus(@RequestBody UserInfo userInfo){
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = userService.updateUserStatus(userInfo);
        } catch (Exception e) {
            logger.error("update user status is error:",e);
        }
        return baseResp;
    }



}
