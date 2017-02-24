package com.longbei.appservice.controller;

import java.util.ArrayList;
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
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.entity.UserFeedback;
import com.longbei.appservice.entity.UserIdcard;
import com.longbei.appservice.entity.UserInfo;
<<<<<<< HEAD
import com.longbei.appservice.entity.UserInterests;
import com.longbei.appservice.service.DictAreaService;
=======
import com.longbei.appservice.service.UserCheckinDetailService;
>>>>>>> 7203875b6bd7e20f9a80b0d51ef2bd9054fb1d39
import com.longbei.appservice.service.UserFeedbackService;
import com.longbei.appservice.service.UserIdcardService;
import com.longbei.appservice.service.UserInterestsService;
import com.longbei.appservice.service.UserJobService;
import com.longbei.appservice.service.UserSchoolService;
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
    @Autowired
    private UserSchoolService userSchoolService;
    @Autowired
    private UserJobService userJobService;
    @Autowired
<<<<<<< HEAD
    private DictAreaService dictAreaService;
    @Autowired
    private UserInterestsService userInterestsService;
=======
    private UserCheckinDetailService userCheckinDetailService;
>>>>>>> 7203875b6bd7e20f9a80b0d51ef2bd9054fb1d39

    
    
    private static Logger logger = LoggerFactory.getLogger(AppUserController.class);
    
    
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
     public BaseResp<Object> checkinDate(@RequestParam("userid") String userid, @RequestParam("yearmonth") String yearmonth) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, yearmonth)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = userCheckinDetailService.selectDetailListByYearmonth(Long.parseLong(userid), Integer.parseInt(yearmonth));
         } catch (Exception e) {
             logger.error("init userid = {}, msg = {}", userid, e);
         }
  		return baseResp;
     }
    
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
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
 		//一些其他的逻辑
 		
 		try {
 			baseResp = userCheckinDetailService.selectIsCheckIn(Long.parseLong(userid));
        } catch (Exception e) {
            logger.error("init userid = {}, msg = {}", userid, e);
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
    * @param @param request userid avatar头像    username手机号    nickname昵称     realname真名   sex性别  
    * @param @param city所在城市   area所在区域   brief个人简介   birthday生日   constellation星座   blood血型   feeling感情状态
    * @param @param code 0
    * @auther smkk
    * @currentdate:2017年2月23日
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.GET)
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
            logger.error("updateUserInfo error and msg = {}", e);
        }
        return baseResp;
    }
    
    
    /**
     * @Title: http://ip:port/appservice/user/selectInterests
     * @Description: 获取用户感兴趣的标签列表
     * @param userid
     * @param code 0
     * @auther smkk
     * @currentdate:2017年2月23日
     */
    @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/selectInterests")
     @ResponseBody
     public BaseResp<Object> selectInterests(String userid) {
     	logger.info("selectInterests and userid={}",userid);
     	BaseResp<Object> baseResp = new BaseResp<>();
     	if(StringUtils.isBlank(userid)){
     		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
     	}  
     	try {
     		baseResp.ok();
     		baseResp.setData(new ArrayList<UserInterests>(){{
 		        add(new UserInterests("-1","全部"));
 		        add(new UserInterests("0","学习"));
 		        add(new UserInterests("1","运动"));
 		        add(new UserInterests("2","社交"));
 		        add(new UserInterests("3","艺术"));
 		        add(new UserInterests("4","生活"));
 		        add(new UserInterests("5","公益"));
 		        add(new UserInterests("6","文字"));
 		        add(new UserInterests("7","劳动"));
 		        add(new UserInterests("8","修养"));
 		        add(new UserInterests("9","健康"));
     		}});
     		return baseResp;	
 		} catch (Exception e) {
 			logger.error("selectInterests error and msg={}",e);
 		}
     	return baseResp;
     }
    
    /**
     * @Title: http://ip:port/appservice/user/updateInterests
     * @Description: 更改用户的兴趣信息
     * @param userid
     * @param code 0
     * @auther IngaWu
     * @currentdate:2017年2月23日
     */   
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateInterests")
    @ResponseBody
    public BaseResp<Object> updateInterests(String id,String ptype,String perfectname) {
    	logger.info("updateInterests and id={},ptype={},perfectname={}",id,ptype,perfectname);
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.isBlank(id)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userInterestsService.updateInterests(Integer.parseInt(id),ptype,perfectname);
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("updateInterests error and msg={}",e);
		}
    	return baseResp;
    }   
    
    /**
     * @Title: http://ip:port/appservice/user/selectCityList
     * @Description: 查找城市信息 
     * @param pid 父级城市编号 (通过pid=null可查中国全部省份，通过省份可查其全部市，通过市可查其全部县)
     * @param code 0
     * @auther IngaWu
     * @currentdate:2017年2月23日
     */     
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectCityList")
    @ResponseBody
    public BaseResp<Object> selectCityList(String pid) {
    	logger.info("selectCityList and pid={}",pid);
    	BaseResp<Object> baseResp = new BaseResp<>();

    	try {
    		baseResp = dictAreaService.selectCityList(pid);
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectCityList error and msg={}",e);
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
    @RequestMapping(value = "/addFeedback")
    @ResponseBody
    public BaseResp<Object> addFeedback(@RequestParam("userid") String userid, String content, String photos) {
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if (StringUtils.hasBlankParams(userid, content, photos)) {
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
	public BaseResp<Object> userSafety(@RequestParam("userid") String userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
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
			//是否验证了身份证号码 0  是未提交信息 1  是验证中 2 验证通过 3  验证不通过
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
    
    //--------------------教育经历start-------------------------------  
    
    /**
     * @Title: http://ip:port/app_service/user/insertSchool
     * @Description: 添加教育经历
     * @param @param userid schoolname学校名称 Department院系 starttime教育起始时间 endtime教育结束时间
     * @param 正确返回 code 0 错误返回相应code 和 描述
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
    		baseResp = userSchoolService.insertSchool(Long.parseLong(userid),schoolname,Department,DateUtils.parseDate(starttime),DateUtils.parseDate(endtime));
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("insertSchool error and msg={}",e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/deleteSchool
     * @Description: 删除教育经历
     * @param @param id userid
     * @param 正确返回 code 0 错误返回相应code 和 描述
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
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("deleteSchool error and msg={}",e);
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
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectSchoolById error and msg={}",e);
		}
    	return baseResp;
    }

    
    /**
     * @Title: http://ip:port/app_service/user/selectSchoolList
     * @Description: 查看教育经历列表
     * @param @param userid 
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */  
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectSchoolList")
    @ResponseBody
    public BaseResp<Object> selectSchoolList(String userid) {
    	logger.info("selectSchoolList and userid={}",userid);
    	BaseResp<Object> baseResp = new BaseResp<>();
        int startNum = 0;
        int pageSize = 15;
    	if(StringUtils.isBlank(userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userSchoolService.selectSchoolList(Long.parseLong(userid),startNum,pageSize);
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectSchoolList error and msg={}",e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/updateSchool 
     * @Description: 更改教育经历
     * @param @param id schoolname学校名称 Department院系 starttime教育起始时间 endtime教育结束时间
     * @param 正确返回 code 0 错误返回相应code 和 描述
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
    		baseResp = userSchoolService.updateSchool(Integer.parseInt(id),schoolname,Department,DateUtils.parseDate(starttime),DateUtils.parseDate(endtime));
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("updateSchool error and msg={}",e);
		}
    	return baseResp;
    }
    
    //--------------------教育经历end--------------------------------- 
    //--------------------工作经历start-------------------------------
    
    /**
     * @Title: http://ip:port/app_service/user/insertJob
     * @Description: 添加工作经历
     * @param @param userid companyname所在公司 department所在部门 location工作所在地  starttime工作起始时间 endtime工作结束时间
     * @param 正确返回 code 0 错误返回相应code 和 描述
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
    		baseResp = userJobService.insertJob(Long.parseLong(userid),companyname,department,location,DateUtils.parseDate(starttime),DateUtils.parseDate(endtime));
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("insertJob error and msg={}",e);
		}
    	return baseResp;
    }
       
    /**
     * @Title: http://ip:port/app_service/user/deleteJob
     * @Description: 删除工作经历
     * @param @param id userid
     * @param 正确返回 code 0 错误返回相应code 和 描述
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
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("deleteJob error and msg={}",e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/selectJobList
     * @Description: 查看工作经历列表
     * @param @param userid 
     * @auther IngaWu
     * @currentdate:2017年2月22日
      */      
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectJobList")
    @ResponseBody
    public BaseResp<Object> selectJobList(String userid) {
    	logger.info("selectJobList and userid={}",userid);
    	BaseResp<Object> baseResp = new BaseResp<>();
        int startNum = 0;
        int pageSize = 15;
    	if(StringUtils.isBlank(userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
    	}  
    	try {
    		baseResp = userJobService.selectJobList(Long.parseLong(userid),startNum,pageSize);
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectJobList error and msg={}",e);
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
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("selectJobById error and msg={}",e);
		}
    	return baseResp;
    }
    
    /**
     * @Title: http://ip:port/app_service/user/updateJob 
     * @Description: 更改工作经历
     * @param @param id companyname所在公司 department所在部门 location工作所在地  starttime工作起始时间 endtime工作结束时间
     * @param 正确返回 code 0 错误返回相应code 和 描述
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
    		baseResp = userJobService.updateJob(Integer.parseInt(id),companyname,department,location,DateUtils.parseDate(starttime),DateUtils.parseDate(endtime));
    		if(baseResp.getCode() == Constant.STATUS_SYS_00){
    			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
    		}
    		return baseResp;	
		} catch (Exception e) {
			logger.error("updateJob error and msg={}",e);
		}
    	return baseResp;
    }
    //--------------------工作经历end-------------------------------     
    
}