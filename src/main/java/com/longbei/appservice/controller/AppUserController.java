package com.longbei.appservice.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
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
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;


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
    @Autowired
    private UserSchoolService userSchoolService;
    @Autowired
    private UserJobService userJobService;
    @Autowired
    private DictAreaService dictAreaService;
    @Autowired
    private UserInterestsService userInterestsService;
    @Autowired
    private UserCheckinDetailService userCheckinDetailService;
    @Autowired
    private UserCertifyService userCertifyService;
    @Autowired
    private UserSponsorService userSponsorService;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;
    @Autowired
    private RankService rankService;
    @Autowired
    private SysPerfectInfoService sysPerfectInfoService;
    @Autowired
    private UserPlDetailService userPlDetailService;
    @Autowired
    private SysSensitiveService sysSensitiveService;
    @Autowired
    private UserPointDetailService userPointDetailService;

    private static Logger logger = LoggerFactory.getLogger(AppUserController.class);

    /**
     * @Title: http://ip:port/app_service/user/infoMore
     * @Description: 个人中心
     * @param @param userid 当前登录id
     * @param @param friendid
     * @param @param 正确返回 code 0，验证码不对，参数错误，未知错误返回相应状态码
     * @return  data:userInfo
     * 			Map :detailList---用户十全十美的信息列表
     * 				 fansCount---粉丝总数
     * 				 showMsg---消息是否显示红点    0:不显示   1：显示	
     * 				 userStar---星级
     * @auther yinxc
     * @currentdate:2017年3月9日
     */
   	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "infoMore")
    @ResponseBody
    public BaseResp<UserInfo> infoMore(String userid, String friendid) {
        logger.info("userid={},friendid={}",userid,friendid);
        BaseResp<UserInfo> baseResp = new BaseResp<UserInfo>();
   		if (StringUtils.hasBlankParams(userid, friendid)) {
        	return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = userService.selectInfoMore(Long.parseLong(friendid),Long.parseLong(userid));
        } catch (Exception e) {
        	logger.error("infoMore userid = {}, friendid = {}", userid, friendid, e);
        }
   		return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/info
     * @Description: 个人资料
     * @param @param userid 当前登录id
     * @param @param friendid
     * @auther yinxc
     * @currentdate:2017年3月8日
     */
   	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "info")
    @ResponseBody
    public BaseResp<Object> info(String userid, String friendid) {
        logger.info("userid={},friendid={}",userid,friendid);
        BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, friendid)) {
        	return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = userService.selectByUserid(Long.parseLong(friendid));
        } catch (Exception e) {
        	logger.error("info userid = {}, friendid = {}", userid, friendid, e);
        }
   		return baseResp;
    }
    
    
    /**
    * @Title: http://ip:port/appservice/user/checkinDate
    * @Description: 用户每月签到详情及搜索
    * @param @param userid
    * @param @param yearmonth  格式为：201702...
    * @auther yinxc
    * @currentdate:2017年2月23日
    */
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "checkinDate")
    @ResponseBody
    public BaseResp<Object> checkinDate(String userid, String yearmonth) {
        logger.info("userid={},yearmonth={}",userid,yearmonth);
        BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, yearmonth)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = userCheckinDetailService.selectDetailListByYearmonth(Long.parseLong(userid), yearmonth);
        } catch (Exception e) {
            logger.error("checkinDate userid = {}, yearmonth = {}", userid, yearmonth, e);
        }
  		return baseResp;
    }
  	
  	
  	/**
     * @Title: http://ip:port/appservice/user/sysRuleCheckin
     * @Description: 用户每月签到规则
     * @auther yinxc
     * @currentdate:2017年4月13日
     */
//  	@RequestMapping(value = "sysRuleCheckin")
//     @ResponseBody
//     public BaseResp<Object> sysRuleCheckin() {
//   		BaseResp<Object> baseResp = new BaseResp<>();
//   		try {
//   			baseResp = userCheckinDetailService.sysRuleCheckin();
//   		} catch (Exception e) {
//   			logger.error("sysRuleCheckin ", e);
//   		}
//   		return baseResp;
//     }
  	
    
    /**
    * @Title: http://ip:port/appservice/user/init
    * @Description: 用户初始化(签到等)
    * @param @param userid
    * @auther yinxc
    * @currentdate:2017年2月23日
    */
 	@SuppressWarnings("unchecked")
	@RequestMapping(value = "init")
    @ResponseBody
    public BaseResp<Object> init(@RequestParam("userid") String userid) {
        logger.info("userid={}",userid);
        BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
 		//一些其他的逻辑
 		
 		try {
 			baseResp = userCheckinDetailService.selectIsCheckIn(Long.parseLong(userid));
            baseResp.getExpandData().put("hasnewmsg","1");
            baseResp.getExpandData().put("hasnewaskfriend","1");
        } catch (Exception e) {
            logger.error("init userid = {} ", userid, e);
        }
 		return baseResp;
    }
    
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
        logger.info("registerbasic params username={},password={},inviteuserid={},randomCode={},deviceindex={},devicetype={}", username, password,inviteuserid,randomCode,deviceindex,devicetype);
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
            logger.error("registerbasic error and msg = {}", e);
        }
        return baseResp;
    }

    /**
     * http://ip:port/appservice/user/sms
     * 短信验证
     *  operateType 0 注册 1 修改密码 2 找回密码 3 第三方绑定手机号 4 安全验证
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
        logger.info("checkSms params mobile={},random={},deviceindex={},devicetype={}", mobile, random,deviceindex,devicetype);
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
     * @Description: 修改密码
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
        logger.info("login username={},password={},deviceindex={}", username, password,deviceindex);
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
     * @Description: 更新用户信息  头像 昵称(去重) 性别 一句话简介  等等信息
     * @param @param request userid avatar头像    username手机号
     * @param @param nickname昵称     realname真名   sex性别
     * @param @param city所在城市   area所在区域   brief个人简介   birthday生日
     * @param @param constellation星座   blood血型   feeling感情状态
     * @param @param code 0
     * @auther smkk
     * @currentdate:2017年2月23日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseResp<Object> updateUserInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResp<Object> baseResp = new BaseResp<>();
        String userid = request.getParameter("userid");
        String avatar = request.getParameter("avatar");
        String username = request.getParameter("username");
        String nickname = request.getParameter("nickname");
        String realname = request.getParameter("realname");
        String sex = request.getParameter("sex");
        String city = request.getParameter("city");
        String area = request.getParameter("area");
        String brief = request.getParameter("brief");
        String birthday = request.getParameter("birthday");
        String constellation = request.getParameter("constellation");
        String blood = request.getParameter("blood");
        String feeling = request.getParameter("feeling");        
        
        logger.info("updateUserInfo params userid={},avatar={},username={},nickname={},realname={},sex={},city={},area={},brief={},birthday={},constellation={},blood={},feeling={}"
                , userid,avatar,username,nickname,realname,sex,city,area,brief,birthday,constellation,blood,feeling);
        if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if(!StringUtils.hasBlankParams(nickname)){
            baseResp = sysSensitiveService.getSensitiveWordSet(nickname);
            if(!ResultUtil.isSuccess(baseResp)){
                return baseResp;
            }
        }
        try {
            UserInfo userInfo = new UserInfo(Long.parseLong(userid), nickname, avatar, sex);
            userInfo.setUsername(username); 
            userInfo.setHcnickname("1");
            userInfo.setRealname(realname); 
            userInfo.setCity(city); 
            userInfo.setArea(area); 
            userInfo.setBrief(brief); 
            userInfo.setBirthday(DateUtils.parseDate(birthday)); 
            userInfo.setConstellation(constellation); 
            userInfo.setBlood(blood);  
            userInfo.setFeeling(feeling);
    		Date date = new Date();
    		userInfo.setUpdatetime(date);
            return userService.updateUserInfo(userInfo);
        } catch (Exception e) {
            logger.error("updateUserInfo params userid={},avatar={},username={},nickname={},realname={},sex={},city={},area={},brief={},birthday={},constellation={},blood={},feeling={}"
                    , userid,avatar,username,nickname,realname,sex,city,area,brief,birthday,constellation,blood,feeling, e);
        }
        return baseResp;
    }

    
    /**  http://ip:port/appservice/user/updateNickName
     * @param @param userid
     * @param @param nickname  sex   pl   isJump(boolean)
     * @param @param inviteusername  邀请人手机号
     * @Title: thirdupdate
     * @Description: 第三方注册后，修改推荐人手机号及昵称，昵称数据库去重
     * @auther yinxc
     * @currentdate:2017年1月21日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateNickName")
    @ResponseBody
    public BaseResp<Object> updateNickName(String userid, String nickname,
    		String inviteusername,boolean isJump,String sex,String pl) {
        logger.info("userid={},nickname={},inviteusername={},isJump={},sex={},pl={}", userid, nickname,inviteusername,isJump, sex,inviteusername);
        //必传参数 userid nickname  isJump 0
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(userid, nickname)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
        if(StringUtils.hasBlankParams(nickname)){
            baseResp = sysSensitiveService.getSensitiveWordSet(nickname);
            if(!ResultUtil.isSuccess(baseResp)){
                return baseResp;
            }
        }
		try {
            if(isJump){
                baseResp = userService.updateNickName(userid, "", "","","");
            }else{
    		    baseResp = userService.updateNickName(userid, nickname, inviteusername,sex,pl);
            }
		} catch (Exception e) {
			logger.error("updateNickName error and msg = {}",e);
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
            logger.info("login utype={},openid={},password={},username={},avatar={},sex={},randomcode={},inviteuserid={},deviceindex={},devicetype={}",
                utype, openid,password,username,avatar,sex,randomcode,inviteuserid,deviceindex,devicetype);

        //必传参数  类型 openid 密码 手机号  随机码
    		BaseResp<Object> baseResp = new BaseResp<>(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
    		if(StringUtils.hasBlankParams(utype,openid,username,randomcode,deviceindex)){
    			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
    		}
    		//判断 手机号是否已经注册  未注册  是下面逻辑
    		//已经注册 判断是否绑定当前第三方  绑定 提示去登录   未绑定 判断验证码或者密码有一个对 就进入
    		try {
        		baseResp = userService.registerthird(username,password,utype,openid,inviteuserid,
        				deviceindex,devicetype,randomcode,avatar);	
		} catch (Exception e) {
			logger.error("thirdregister error and msg = {}",e);
		}
    		return baseResp;
    }

    /**
     * user/thirdbinding
     * 绑定第三方帐号
     * @param userid  必传
     * @param utype   必传
     * @param openid  必传
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/thirdbinding")
    @ResponseBody
    public BaseResp<Object> thirdbinding(String userid,String utype,String openid) {
        logger.info("thirdbinding userid={},utype={},opendi={}",userid,utype,openid);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,utype,openid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return userService.thirdbinding(userid,utype,openid);
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
        logger.info("thirdlogin utype={},openid={},deviceindex={}", utype, openid,deviceindex);
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
    @RequestMapping(value = "/addFeedback")
    @ResponseBody
    public BaseResp<Object> addFeedback(@RequestParam("userid") String userid, String content, String photos) {
        logger.info("addFeedback userid={},content={},photos={}", userid, content,photos);
        BaseResp<Object> baseResp = new BaseResp<>();
    	if (StringUtils.hasBlankParams(userid, content, photos)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        if(StringUtils.hasBlankParams(content)){
            baseResp = sysSensitiveService.getSensitiveWordSet(content);
            if(!ResultUtil.isSuccess(baseResp)){
                return baseResp;
            }
        }
    	try {
    		UserFeedback record = new UserFeedback(Long.parseLong(userid), content, photos, new Date(), "0");
    		baseResp = userFeedbackService.insertSelective(record);
        } catch (Exception e) {
            logger.error("addFeedback userid={},content={},photos={}", userid, content,photos, e);
        }
    	return baseResp;
    }

    /**
     * @param id
     * @Title: getFeedback
     * @Description: 获取意见反馈信息
     * @auther yinxc
     * @currentdate:2017年5月14日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getFeedback")
    @ResponseBody
    public BaseResp<UserFeedback> getFeedback(@RequestParam("id") String id) {
        logger.info("getFeedback id = {}", id);
        BaseResp<UserFeedback> baseResp = new BaseResp<>();
        if (StringUtils.hasBlankParams(id)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userFeedbackService.selectUserFeedback(id);
        } catch (Exception e) {
            logger.error("getFeedback id = {}", id, e);
        }
        return baseResp;
    }
    
    /**
     * @Title: http://ip:port/appservice/user/userSafety
     * @Description: 帐号与安全(获取身份验证状态)
     * @Description: validateidcard   是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过
     * @Description: realname  真实姓名
     * @param @param userid
     * @param @param code 0
     * @auther yinxc
     * @currentdate:2017年2月24日
      */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/userSafety")
	@ResponseBody
	public BaseResp<UserIdcard> userSafety(@RequestParam("userid") String userid) {
        logger.info("userid={}", userid);
        BaseResp<UserIdcard> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userIdcardService.userSafety(Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("applyIdCardValidate userid={},msg={}", userid, e);
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
        logger.info("userid={},realname={},idcard={},idcardimage={}", userid, realname,idcard,idcardimage);
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
			//是否验证了身份证号码 0  为验证 1  验证通过 2 验证不通过
			record.setValidateidcard("1");
			record.setUserid(Long.parseLong(userid));
			record.setApplydate(new Date());
            record.setRealname(realname);
			// 添加身份证验证 先判断是否验证过，已验证修改
			baseResp = userIdcardService.insert(record);
		} catch (Exception e) {
			logger.error("applyIdCardValidate userid={},msg={}", userid, e);
		}
		return baseResp;
	}
    
    //--------------------教育经历start-------------------------------
    /**
     * @Title: http://ip:port/app_service/user/insertSchool
     * @Description: 添加教育经历
     * @param @param userid schoolname学校名称 Department院系 starttime教育起始时间 endtime教育结束时间
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertSchool")
    @ResponseBody
    public BaseResp<Object> insertSchool(String userid,String schoolname,String Department,String starttime,String endtime) {
    	logger.info("insertSchool and userid={},schoolname={},Department={},starttime={},endtime={}", userid,schoolname,Department,starttime,endtime);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(userid,schoolname,Department)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userSchoolService.insertSchool(Long.parseLong(userid),schoolname,Department,starttime,DateUtils.parseDate(endtime));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("insertSchool and userid={},schoolname={},Department={},starttime={},endtime={}", userid,schoolname,Department,starttime,endtime,e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/deleteSchool
     * @Description: 删除教育经历
     * @param @param id userid
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteSchool")
    @ResponseBody
    public BaseResp<Object> deleteSchool(String id,String userid) {
    	logger.info("deleteSchool and id={},userid={}",id,userid);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(id,userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userSchoolService.deleteSchool(Integer.parseInt(id),Long.parseLong(userid));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("deleteSchool and id={},userid={}",id,userid,e);
		}
    	return baseResp;
    }
   
    /**
     * @Title: http://ip:port/app_service/user/selectSchoolById
     * @Description: 查询教育经历
     * @param @param id
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */  
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectSchoolById")
    @ResponseBody
    public BaseResp<Object> selectSchoolById(String id) {
    	logger.info("selectSchoolById and id={}",id);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(id)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userSchoolService.selectSchoolById(Integer.parseInt(id));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectSchoolById and id={}",id,e);
		}
    	return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectSchoolList
     * @Description: 查看教育经历列表
     * @param @param userid  startNum分页起始值 pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */  
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectSchoolList")
    @ResponseBody
    public BaseResp<Object> selectSchoolList(String userid,String startNum,String pageSize) {
    	logger.info("selectSchoolList and userid={},startNum={},pageSize={}",userid,startNum,pageSize);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}
        if (StringUtils.isBlank(startNum)){
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
    		baseResp = userSchoolService.selectSchoolList(Long.parseLong(userid),Integer.parseInt(startNum),Integer.parseInt(pageSize));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectSchoolList and userid={},startNum={},pageSize={}",userid,startNum,pageSize,e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/updateSchool 
     * @Description: 更改教育经历
     * @param @param id schoolname学校名称 Department院系 starttime教育起始时间 endtime教育结束时间
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateSchool")
    @ResponseBody
    public BaseResp<Object> updateSchool(String id,String schoolname,String Department,String starttime,String endtime) {
    	logger.info("updateSchool and id={},schoolname={},Department={},starttime={},endtime={}",id,schoolname,Department,starttime,endtime);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(id)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userSchoolService.updateSchool(Integer.parseInt(id),schoolname,Department,starttime,DateUtils.parseDate(endtime));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("updateSchool and id={},schoolname={},Department={},starttime={},endtime={}",id,schoolname,Department,starttime,endtime,e);
		}
    	return baseResp;
    }
    
    //--------------------教育经历end--------------------------------- 
    //--------------------工作经历start-------------------------------
    
    /**
     * @Title: http://ip:port/app_service/user/insertJob
     * @Description: 添加工作经历
     * @param @param userid companyname所在公司 department所在部门 location工作所在地  starttime工作起始时间 endtime工作结束时间
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */  
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertJob")
    @ResponseBody
    public BaseResp<Object> insertJob(String userid,String companyname,String department,String location,String starttime,String endtime) {
    	logger.info("insertJob and userid={},companyname={},department={},location={},starttime={},endtime={}",userid,companyname,department,location,starttime,endtime);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(userid,companyname,department,location)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userJobService.insertJob(Long.parseLong(userid),companyname,department,location,starttime,DateUtils.parseDate(endtime));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("insertJob and userid={},companyname={},department={},location={},starttime={},endtime={}",userid,companyname,department,location,starttime,endtime,e);
		}
    	return baseResp;
    }
       
    /**
     * @Title: http://ip:port/app_service/user/deleteJob
     * @Description: 删除工作经历
     * @param @param id userid
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */ 
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteJob")
    @ResponseBody
    public BaseResp<Object> deleteJob(String id,String userid) {
    	logger.info("deleteJob and id={},userid={}",id,userid);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(id,userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userJobService.deleteJob(Integer.parseInt(id),Long.parseLong(userid));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("deleteJob and id={},userid={}",id,userid,e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/selectJobList
     * @Description: 查看工作经历列表
     * @param @param userid startNum分页起始值 pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */      
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectJobList")
    @ResponseBody
    public BaseResp<Object> selectJobList(String userid,String startNum,String pageSize) {
    	logger.info("selectJobList and userid={},startNum={},pageSize={}",userid,startNum,pageSize);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}
        if (StringUtils.isBlank(startNum)){
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
    	try {
    		baseResp = userJobService.selectJobList(Long.parseLong(userid),Integer.parseInt(startNum),Integer.parseInt(pageSize));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectJobList and userid={},startNum={},pageSize={}",userid,startNum,pageSize,e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/selectJobById
     * @Description: 查询工作经历
     * @param @param id
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */     
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectJobById")
    @ResponseBody
    public BaseResp<Object> selectJobById(String id) {
    	logger.info("selectJobById and id={}",id);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(id)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userJobService.selectJobById(Integer.parseInt(id));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectJobById and id={}",id,e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/updateJob 
     * @Description: 更改工作经历
     * @param @param id companyname所在公司 department所在部门 location工作所在地  starttime工作起始时间 endtime工作结束时间
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */ 
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateJob")
    @ResponseBody
    public BaseResp<Object> updateJob(String id,String companyname,String department,String location,String starttime,String endtime) {
    	logger.info("updateJob and id={},companyname={},department={},location={},starttime={},endtime={}",id,companyname,department,location,starttime,endtime);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(id)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userJobService.updateJob(Integer.parseInt(id),companyname,department,location,starttime,DateUtils.parseDate(endtime));
    		return baseResp;	
		} catch (Exception e) {
			logger.error("updateJob and id={},companyname={},department={},location={},starttime={},endtime={}",id,companyname,department,location,starttime,endtime,e);
		}
    	return baseResp;
    }
    //--------------------工作经历end-------------------------------
    //--------------------用户兴趣标签start-------------------------------
    /**
     * @Title: http://ip:port/appservice/user/selectInterests
     * @Description: 获取用户感兴趣的标签列表
     * @param @param userid
     * @auther smkk
     * @currentdate:2017年2月23日
     */
    @SuppressWarnings({ "unchecked", "static-access", "serial" })
    @RequestMapping(value = "/selectInterests")
    @ResponseBody
    public BaseResp<Object> selectInterests(String userid) {
        logger.info("selectInterests and userid={}",userid);
        BaseResp<Object> baseResp = new BaseResp<>();
//        if(StringUtils.isBlank(userid)){
//            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
//        }
        try {
            baseResp = userService.selectRandomTagList(userid);
            return baseResp.initCodeAndDesp();
        } catch (Exception e) {
            logger.error("selectInterests and userid={}",userid,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/appservice/user/updateInterests
     * @Description: 更改兴趣标签信息
     * @param userid 用户id
     * @param ids 兴趣标签id(String字符串类型，用逗号隔开)
     * @param @param code 0
     * @auther IngaWu
     * @currentdate:2017年2月24日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateInterests")
    @ResponseBody
    public BaseResp<Object> updateInterests(String userid,String ids) {
        logger.info("updateInterests and userid={},ids={}",userid,ids);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,ids)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            return userInterestsService.updateInterests(userid,ids);
        } catch (Exception e) {
            logger.error("updateInterests and userid={},ids={}",userid,ids,e);
        }
        return baseResp;
    }

    //--------------------用户兴趣标签end-------------------------------
    /**
     * @Title: http://ip:port/appservice/user/selectCityList
     * @Description: 查找城市信息
     * @param @param pid 父级城市编号 (通过pid=null可查中国全部省份，通过省份可查其全部市，通过市可查其全部县)
     * @param @param startNum分页起始值，pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年2月23日
     */
    @RequestMapping(value = "/selectCityList")
    @ResponseBody
    public BaseResp<List<DictArea>> selectCityList(String pid,String startNum,String pageSize) {
        logger.info("selectCityList and pid={},startNum={},pageSize={}",pid,startNum,pageSize);
        BaseResp<List<DictArea>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(startNum)){
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = dictAreaService.selectCityList(pid,Integer.parseInt(startNum),Integer.parseInt(pageSize));
            return baseResp;
        } catch (Exception e) {
            logger.error("selectCityList and pid={},startNum={},pageSize={}",pid,startNum,pageSize,e);
        }
        return baseResp;
    }
    //--------------------教育经历和工作经历认证start-------------------------------
    /**
     * @Title: http://ip:port/app_service/user/applyCertify
     * @Description: 申请教育经历和工作经历认证
     * @param id id为空值时添加认证，有id时修改认证
     * @param userid
     * @param @param  ctype认证类型 0学历 1工作经历
     * @param @param photes图片信息
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/applyCertify")
    @ResponseBody
    public BaseResp<Object> applyCertify(String id,String userid,String ctype,String photes) {
        logger.info("applyCertify and userid={},ctype={},photes={}",userid,ctype,photes);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,ctype,photes)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            if(!StringUtils.isBlank(id)){
                return  userCertifyService.updateApplyCertify(Integer.parseInt(id),ctype,photes);
            }
            else {
                return userCertifyService.insertCertify(Long.parseLong(userid),ctype,photes);
            }
        } catch (Exception e) {
            logger.error("applyCertify and userid={},ctype={},photes={}",userid,ctype,photes,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectCertifyById
     * @Description: 查询认证
     * @param @param id
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectCertifyById")
    @ResponseBody
    public BaseResp<Object> selectCertifyById(String id) {
        logger.info("selectCertifyById and id={}", id);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(id)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userCertifyService.selectCertifyById(Integer.parseInt(id));
            return baseResp;
        } catch (Exception e) {
            logger.error("selectCertifyById and id={}", id, e);
        }
        return baseResp;
    }
    //--------------------教育经历和工作经历认证end-------------------------------
    //--------------------榜单赞助start-------------------------------
    /**
     * @Title: http://ip:port/app_service/user/insertSponsor
     * @Description: 添加赞助
     * @param @param userid 赞助人 userid为空值时添加一条记录，有userid时追加原有记录赞助的龙币数量
     * @param @param number 赞助龙币数量, bid榜单id, ptype赞助类型 0龙币 1进步币
     * @param @param 正确返回 code 0 错误返回相应code 和 描述
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/insertSponsor")
    @ResponseBody
    public BaseResp<Object> insertSponsor(String userid,String number,String bid,String ptype) {
        logger.info("insertSponsor and userid={},number={},bid={},ptype={}",userid,number,bid,ptype);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,number,bid,ptype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            UserSponsor userSponsor = userSponsorService.selectByUseridAndBid(Long.parseLong(userid),Long.parseLong(bid));
            if(null != userSponsor){
                return userSponsorService.updateSponsor(Long.parseLong(userid),Integer.parseInt(number),Long.parseLong(bid),ptype);
            }else {
                return userSponsorService.insertSponsor(Long.parseLong(userid), Integer.parseInt(number), Long.parseLong(bid), ptype);
            }
        } catch (Exception e) {
            logger.error("insertSponsor and userid={},number={},bid={},ptype={}",userid,number,bid,ptype,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectSponsorList
     * @Description: 查询赞助记录列表
     * @param @param bid榜单 startNum分页起始值 pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectSponsorList")
    @ResponseBody
    public BaseResp<Object> selectSponsorList(String bid,String startNum,String pageSize) {
        logger.info("selectSponsorList and bid={},startNum={},pageSize={}",bid,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(startNum)){
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = userSponsorService.selectSponsorList(Long.parseLong(bid),Integer.parseInt(startNum),Integer.parseInt(pageSize));
            return baseResp;
        } catch (Exception e) {
            logger.error("selectSponsorList and bid={},startNum={},pageSize={}",bid,startNum,pageSize,e);
        }
        return baseResp;
    }
    //--------------------榜单赞助end-------------------------------

    /**
     * @Title: http://ip:port/app_service/user/updateSponsornumAndSponsormoney
     * @Description: 更新赞助的榜单人数和龙币数量
     * @param @param id
     * @auther IngaWu
     * @currentdate:2017年2月27日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/updateSponsornumAndSponsormoney")
    @ResponseBody
    public BaseResp<Object> updateSponsornumAndSponsormoney(String rankid) {
        logger.info("updateSponsornumAndSponsormoney and rankid={}",rankid);
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            boolean n = rankService.updateSponsornumAndSponsormoney(Long.parseLong(rankid));
            if (n)
            {
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00 );
            }
        } catch (Exception e) {
            logger.error("updateSponsornumAndSponsormoney and rankid={}",rankid,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectPerfectInfoByPtype
     * @Description: 查询单个十全十美类型的信息
     * @param @param ptype十全十美类型 0 全部 1学习 等
     * @auther IngaWu
     * @currentdate:2017年3月6日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectPerfectInfoByPtype")
    @ResponseBody
    public BaseResp<Object> selectPerfectInfoByPtype(String ptype) {
        logger.info("selectPerfectInfoByPtype and ptype={}",ptype);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(ptype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = sysPerfectInfoService.selectPerfectInfoByPtype(ptype);
            return baseResp;
        } catch (Exception e) {
            logger.error("selectPerfectInfoByPtype and ptype={}",ptype,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectNowscoreAndDiffByUseridAndPtype
     * @Description: 查询用户现在的分数和距离升级的差分
     * @param userid 用户id
     * @param  ptype 十全十美类型 0 全部 1学习 等
     * @return nowscore 现在的分数
     * @return diff 距离升级的差分
     * @auther IngaWu
     * @currentdate:2017年3月6日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectNowscoreAndDiffByUseridAndPtype")
    @ResponseBody
    public BaseResp<Object> selectNowscoreAndDiffByUseridAndPtype(String userid,String ptype) {
        logger.info("selectNowscoreAndDiffByUseridAndPtype and userid={}，ptype={} ",userid,ptype);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,ptype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = userPlDetailService.selectNowscoreAndDiffByUseridAndPtype(Long.parseLong(userid), ptype);
            return baseResp;
        } catch (Exception e) {
            logger.error("selectNowscoreAndDiffByUseridAndPtype and userid={}，ptype={} ",userid,ptype,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectUserPerfectListByUserId
     * @Description: 查询用户十全十美的信息列表（十全十美图片、等级、分数等）
     * @param @param userid  startNum分页起始值 pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年3月6日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectUserPerfectListByUserId")
    @ResponseBody
    public BaseResp<Object> selectUserPerfectListByUserId(String userid,String startNum,String pageSize) {
        logger.info("selectUserPerfectListByUserId and userid={},startNum={},pageSize={}",userid,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(startNum)){
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = userPlDetailService.selectUserPerfectListByUserId(Long.parseLong(userid),Integer.parseInt(startNum),Integer.parseInt(pageSize));
            return baseResp;
        } catch (Exception e) {
            logger.error("selectUserPerfectListByUserId and userid={},startNum={},pageSize={}",userid,startNum,pageSize,e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/readCityTxt
     * @Description: 读取城市信息txt,若没有则新建
     * @auther IngaWu
     * @currentdate:2017年3月9日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/readCityTxt")
    @ResponseBody
    public BaseResp<Object> readCityTxt() {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = dictAreaService.readCityTxt();
            baseResp.initCodeAndDesp();
            return baseResp;
        } catch (Exception e) {
            logger.error("readCityTxt error and msg={}",e);
        }
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/userlevel
     * @Description: 获取用户龙级信息   userid 用户的id
     * @auther lixb
     * @currentdate:2017年3月16日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/userlevel")
    @ResponseBody
    public BaseResp<Object> userlevel(String userid) {
        logger.info("userid={}",userid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(StringUtils.hasBlankParams(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = userService.selectUserlevel(Long.parseLong(userid));
        return baseResp;
    }

    /**
     * 保存用户位置 http://server_ip:port/app_service/user/gps
     *
     * @param userid    ：用户id
     * @param longitude ：位置经度字符串
     * @param latitude  ：位置纬度字符串
     * @return ：成功返回{"status":"0", "rtnInfo":""}，失败返回对应代码
     * @method ：POST
     */
    @RequestMapping(value = "/gps")
    @ResponseBody
    public BaseResp<Object> gps(String userid,String longitude,String latitude) {
        BaseResp<Object> baseResp = new BaseResp<>();
        logger.info("gps Info longitude={}, latitude={}", longitude, latitude);
        if(StringUtils.hasBlankParams(userid,longitude,latitude)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        String dateStr = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
        baseResp = userService.gps(Long.parseLong(userid), Double.parseDouble(longitude), Double.parseDouble(latitude), dateStr);
        return baseResp;
    }

    /**
     * http://server_ip:port/app_service/user/perfectInfo
     * 十全十美说明信息
     * @param ptype
     * @return
     */
    @RequestMapping(value = "/perfectInfo")
    @ResponseBody
    public BaseResp<Object> perfectInfo(String ptype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        logger.info("perfectInfo ptype={}", ptype);
        if(StringUtils.hasBlankParams(ptype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = userService.perfectInfo(ptype);
        return baseResp;
    }

    /**
     * @Title: http://ip:port/app_service/user/selectPointListByUseridAndPtype
     * @Description: 获取用户发进步积分列表
     * @param @param userid 用户id
     * @param @param ptype 积分类型
     * @param @param startNum分页起始值
     * @param @param pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年4月5日
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/selectPointListByUseridAndPtype")
    @ResponseBody
    public BaseResp<Object> selectPointListByUseridAndPtype(String userid,String ptype,String startNum,String pageSize) {
        logger.info("selectPointListByUseridAndPtype and userid={},pointtype={},startNum={},pageSize={}",userid,ptype,startNum,pageSize);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid,ptype)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isBlank(startNum)){
            startNum = Constant.DEFAULT_START_NO;
        }
        if (StringUtils.isBlank(pageSize)){
            pageSize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = userPointDetailService.selectPointListByUseridAndPtype(Long.parseLong(userid),ptype,Integer.parseInt(startNum),Integer.parseInt(pageSize));
            return baseResp;
        } catch (Exception e) {
            logger.error("selectPointListByUseridAndPtype and userid={},pointtype={},startNum={},pageSize={}",userid,ptype,startNum,pageSize,e);
        }
        return baseResp;
    }

    /**
     * http://server_ip:port/app_service/user/usermenus
     * @param userid
     * @return
     */
    @RequestMapping(value = "/usermenus")
    @ResponseBody
    public BaseResp<List<UserSettingMenu>> usermenulist(String userid) {
        BaseResp<List<UserSettingMenu>> baseResp = new BaseResp<>();
        logger.info("usermenulist userid={}", userid);
        if(StringUtils.hasBlankParams(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return userService.selectMenuByUid(Long.parseLong(userid));
    }

    /**
     * http://server_ip:port/app_service/user/updateBg
     * @param userid   用户id
     * @param bg  新的背景图片
     * @return
     */
    @RequestMapping(value = "/updateBg")
    @ResponseBody
    public BaseResp<Object> updateBg(String userid,String bg) {
        BaseResp<List<UserSettingMenu>> baseResp = new BaseResp<>();
        logger.info("updateBg userid={},bg = {}", userid,bg);
        if(StringUtils.hasBlankParams(userid,bg)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return userService.updateBg(Long.parseLong(userid),bg);
    }

}