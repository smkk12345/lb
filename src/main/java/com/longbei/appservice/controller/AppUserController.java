package com.longbei.appservice.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.entity.UserFeedback;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserFeedbackService;
import com.longbei.appservice.service.UserIdcardService;
import com.longbei.appservice.service.UserService;

/**
 * @author smkk
 */
@Controller
@RequestMapping(value = "/user")
public class AppUserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFeedbackService userFeedbackService;
    @Autowired
    private UserIdcardService userIdcardService;

    private static Logger logger = LoggerFactory.getLogger(AppUserController.class);
    
    
    /**
    * @Title: http://ip:port/appservice/user/registerbasic
    * @Description: 注册登统中心，appservice  注册分两步  1步 注册  2步 修改昵称
    * @param @param request
	* @param @param response
    * @param @param username password inviteuserid randomCode
    * @param @param 正确返回 code 0 并返回用户信息，融云token，apptoken ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther smkk
    * @currentdate:2017年1月16日
	*/
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/registerbasic")
    @ResponseBody
    public BaseResp<Object> registerbasic(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String inviteuserid = request.getParameter("inviteuserid");
        String randomCode = request.getParameter("randomCode");
        String deviceindex = request.getParameter("deviceindex");
        String devicetype = request.getParameter("devicetype");
        logger.info("register params username={},password={}", username, password);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(username, password, randomCode,deviceindex)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        baseResp = userService.checkSms(username, randomCode,deviceindex,devicetype);
        if (baseResp.getCode() != Constant.STATUS_SYS_00) {
            return baseResp;
        }
        try {
            return userService.registerbasic(username, password, inviteuserid,deviceindex,devicetype,null);
        } catch (Exception e) {
            logger.error("register error and msg = {}", e);
        }
        return baseResp;
    }

    /**
     * http://ip:port/appservice/user/sms
     * 短信验证
     *  operateType 0 注册 1 修改密码 2 找回密码
     * @param response 
     * @return 正确返回 code 0 错误返回相应code 和 描述
     */
    @RequestMapping(value = "/sms")
    @ResponseBody
    public BaseResp<Object> sms(HttpServletRequest request, HttpServletResponse response) {
        String mobile = request.getParameter("mobile");
        String operateType = request.getParameter("operateType");
        operateType = operateType == null ? "0" : operateType;
        logger.info("sms and mobile={},operateType={}", mobile, operateType);
        if(StringUtils.isBlank(mobile)){
        		return new BaseResp<>(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return userService.sms(mobile, operateType);
        } catch (Exception e) {
            logger.error(" sms error and msg={}", e);
        }
        return new BaseResp<>();
    }

    /**
     * url http://ip:port/app_service/user/checkSms
     * @param @param mobile
     * @param @param random  deviceindex
     * @Title: checkSms
     * @Description: 验证
     * @auther smkk
     * @currentdate:2017年1月16日
     */
    @ResponseBody
    @RequestMapping(value = "/checkSms")
    public BaseResp<Object> checkSms(String mobile, String random,String deviceindex,String devicetype) {
        logger.info("checkSms params mobile={},random={}", mobile, random);
        if(StringUtils.hasBlankParams(mobile,random,deviceindex)){
    			return new BaseResp<>(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return userService.checkSms(mobile, random,deviceindex,devicetype);
        } catch (Exception e) {
            logger.error("checkSms error and msg={}", e);
        }
        return new BaseResp<>();
    }

    /**
    * @Title:  http://ip:port/appservice/user/findPassword
    * @Description: 密码找回
    * @param @param request  username newpassword  randomCode
    * @param @param response
    * @param @return 正确返回 code 0 错误返回相应code 和 描述
    * @auther smkk
    * @currentdate:2017年1月16日
    */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/findPassword")
    @ResponseBody
    public BaseResp<Object> findPassword(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String newpwd = request.getParameter("newpassword");
        String randomCode = request.getParameter("randomCode");
        logger.info("findPassword username={},newpwd={},randomCode={}", username, newpwd, randomCode);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(username, newpwd, randomCode)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
			return userService.findPassword(username,newpwd,randomCode);
		} catch (Exception e) {
			logger.error("findPassword error and msg={}",e);
		}
        return baseResp;
    }

    /**
     * @Title:  http://ip:port/app_service/user/changePassword
     * @param request userid  password  newpassword
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public BaseResp<Object> changePassword(HttpServletRequest request, HttpServletResponse response) {
        String userid = request.getParameter("userid");
        String password = request.getParameter("password");
        String newpwd = request.getParameter("newpassword");
        logger.info("changePassword userid={},password={},newpassword={}", userid, password, newpwd);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(userid, newpwd, password)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return userService.changePassword(Long.parseLong(userid),password,newpwd);
        } catch (Exception e) {
            logger.error("changePassword error and msg={}",e);
        }
        return baseResp;
    }


    /**
    * @Title: http://ip:port/appservice/user/login
    * @Description: 登录
    * @param @param username password
    * @param @param 成功返回code 0 和各种token userinfo  失败返回相应非0状态码和描述信息
    * @auther smkk
    * @currentdate:2017年1月16日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/login")
    @ResponseBody
    public BaseResp<UserInfo> login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String deviceindex = request.getParameter("deviceindex");
        logger.info("login username={},password={}", username, password);
        BaseResp<UserInfo> baseResp = new BaseResp<>(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
        if (StringUtils.hasBlankParams(username, password,deviceindex)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return userService.login(username, password,deviceindex);
        } catch (Exception e) {
            logger.error("login error and msg={}", e);
        }
        return baseResp;
    }
    /**
    * @Title: http://ip:port/appservice/user/updateUserInfo
    * @Description: 更新用户信息  头像 昵称 性别 一句话简介  等等信息
    * @param @param request  avatar  nickname  userid sex brief
    * @param @param code 0
    * @auther smkk
    * @currentdate:2017年1月19日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<Object> updateUserInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResp<Object> baseResp = new BaseResp<>();
        String avatar = request.getParameter("avatar");
        String nickname = request.getParameter("nickname");
        String userid = request.getParameter("userid");
        String sex = request.getParameter("sex");
        String brief = request.getParameter("brief");
        logger.info("updateUserInfo params avatar={},nickname={},userid={},sex={}"
                , avatar, nickname, userid, sex);
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            UserInfo userInfo = new UserInfo(Long.parseLong(userid), nickname, avatar, sex);
            userInfo.setHcnickname("1");
            userInfo.setBrief(brief);
            return userService.updateUserInfo(userInfo);
        } catch (Exception e) {
            logger.error("updateUserInfo error and msg = {}", e);
        }
        return baseResp;
    }
    
    
    /**  http://ip:port/appservice/user/updateNickName
     * @param @param userid
     * @param @param nickname  sex   pl   isJump(boolean)
     * @param @param invitecode  邀请人手机号
     * @Title: thirdupdate
     * @Description: 第三方注册后，修改推荐人手机号及昵称，昵称数据库去重
     * @auther yinxc
     * @currentdate:2017年1月21日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateNickName")
    @ResponseBody
    public BaseResp<Object> updateNickName(@RequestParam("userid") String userid, @RequestParam("nickname") String nickname,
    		String inviteusername,boolean isJump,String sex,String pl) {
		//必传参数 userid nickname  isJump 0
		BaseResp<Object> baseResp = new BaseResp<>(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
		if(StringUtils.hasBlankParams(userid,nickname)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
            if(isJump){
                baseResp = userService.updateNickName(userid, "", "","","");
            }else{
    		    baseResp = userService.updateNickName(userid, nickname, inviteusername,sex,pl);
            }
		} catch (Exception e) {
			logger.error("thirdupdate error and msg = {}",e);
		}
        return baseResp;
    }
    
    //--------------------第三方登录，注册start-------------------------------
    
    /**  user/thirdregister
     * @param @param request  utype  openid password  username avatar randomcode 
     * inviteusername deviceindex  devicetype
     * @param @param response
     * @Title: thirdregister
     * @Description: 第三方注册, 传递
     * @auther smkk
     * @currentdate:2017年1月17日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdregister")
    @ResponseBody
    public BaseResp<Object> thirdregister(HttpServletRequest request, HttpServletResponse response) {
    		String utype = request.getParameter("utype");
    		String openid = request.getParameter("openid");
    		String password = request.getParameter("password");
    		String username = request.getParameter("username");
    		String avatar = request.getParameter("avatar");
    		String sex = request.getParameter("sex");
    		String randomcode = request.getParameter("randomcode");
    		String inviteuserid = request.getParameter("inviteusername");
    		String deviceindex = request.getParameter("deviceindex");
        String devicetype = request.getParameter("devicetype");
    		//必传参数  类型 openid 密码 手机号  随机码
    		BaseResp<Object> baseResp = new BaseResp<>(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    		if(StringUtils.hasBlankParams(utype,openid,password,username,randomcode,deviceindex)){
    			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
    		}
    		//判断 手机号是否已经注册  未注册  是下面逻辑
    		//已经注册 判断是否绑定当前第三方  绑定 提示去登录   未绑定 判断验证码或者密码有一个对 就进入
    		try {
        		baseResp = userService.registerthird(username,password,utype,openid,inviteuserid,
        				deviceindex,devicetype,randomcode,avatar);	
		} catch (Exception e) {
			logger.error("registerthird error and msg = {}",e);
		}
    		return baseResp;
    }

    /** user/thirdlogin
     * @param @param request  utype  openid deviceindex
     * @param @param response
     * @Title: thirdlogin
     * @Description: 第三方登录  直接拿 openid
     * @auther smkk
     * @currentdate:2017年1月17日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdlogin", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<Object> thirdlogin(HttpServletRequest request, HttpServletResponse response) {
        String utype = request.getParameter("utype");
        String openid = request.getParameter("openid");
        String deviceindex = request.getParameter("deviceindex");
        logger.info("thirdlogin utype={},openid={}", utype, openid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(utype, openid,deviceindex)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            return userService.thirdlogin(utype, openid,deviceindex);
        } catch (Exception e) {
            logger.error("login error and msg={}", e);
        }
        return baseResp;
    }
    //--------------------第三方登录，注册end-------------------------------

	/**
     * @param @param  request
     * @param @param  response
     * @param @return
     * @Title: addFeedback
     * @Description: 添加意见反馈
     * @auther yinxc
     * @currentdate:2017年1月19日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/addFeedback", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<Object> addFeedback(@RequestParam("userid") String userid, @RequestParam("content") String content,
    			@RequestParam("photos") String photos) {
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
    	try {
    		UserFeedback record = new UserFeedback(Long.parseLong(userid), content, photos, new Date(), "0");
    		baseResp = userFeedbackService.insertSelective(record);
        } catch (Exception e) {
            logger.error("login error and msg={}", e);
        }
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/appservice/user/applyIdCardValidate
     * @Description: 申请身份证验证，移动端保存身份证验证信息到数据库
     * @param @param request  avatar  nickname  userid sex brief
     * @param @param code 0
     * @auther yinxc
     * @currentdate:2017年1月20日
      */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/applyIdCardValidate", method = RequestMethod.POST)
	@ResponseBody
	public BaseResp<Object> applyIdCardValidate(@RequestParam("userid") String userid,
			@RequestParam("realname") String realname, @RequestParam("idcard") String idcard,
			@RequestParam("idcardimage") String idcardimage) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, realname, idcard, idcardimage)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			// 修改userInfo表真实姓名
			UserInfo userInfo = new UserInfo();
			userInfo.setRealname(realname);
			userInfo.setUserid(Long.parseLong(userid));
			userService.updateUserInfo(userInfo);

			UserIdcard record = new UserIdcard();
			record.setIdcard(idcard);
			record.setIdcardimage(idcardimage);
			record.setValidateidcard("1");
			record.setUserid(Long.parseLong(userid));
			record.setApplydate(new Date());
			// 添加身份证验证 先判断是否验证过，已验证修改
			baseResp = userIdcardService.insert(record);
		} catch (Exception e) {
			logger.error("applyIdCardValidate userid={},msg={}", userid, e);
		}
		return baseResp;
	}
    /**
    * @Title: checkIn
    * @Description: 用户签到 每天只可以签到一次 ［签到送龙分（龙分影响龙级） 随机送龙币］
    * @param @param userid
    * @auther smkk
    * @currentdate:2017年1月22日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkIn", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<Object> checkIn(@RequestParam("userid") Long userid) {
    		BaseResp<Object> baseResp = new BaseResp<>();
    		
    		return baseResp;
    }


}