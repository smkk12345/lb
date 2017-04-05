package com.longbei.appservice.service.impl;

import java.util.*;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.NickNameUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;

import com.longbei.appservice.service.UserRelationService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.nativejdbc.OracleJdbc4NativeJdbcExtractor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.UserMsgService;
import com.longbei.appservice.service.UserService;
import com.longbei.appservice.service.api.HttpClient;

import io.rong.models.TokenReslut;
import net.sf.json.JSONObject;

/**
 * 创建时间：2015-1-27 下午5:22:59
 * @author smkk
 * @version 2.2
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private IdGenerateService idGenerateService;

	@Autowired
	private SpringJedisDao springJedisDao;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserJobMapper userJobMapper;
	@Autowired
	private UserSchoolMapper userSchoolMapper;
	@Autowired
	private UserInterestsMapper userInterestsMapper;
	@Autowired
	private UserPlDetailMapper userPlDetailMapper;
	@Autowired
	private SysPerfectInfoMapper sysPerfectInfoMapper;
	@Autowired
	private SnsFansMapper snsFansMapper;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private UserLevelMapper userLevelMapper;
	@Autowired
	private SysPerfectTagMapper sysPerfectTagMapper;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private SysPerfectDefineMapper sysPerfectDefineMapper;
	@Autowired
	private UserSettingMenuMapper userSettingMenuMapper;
	
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	
	

	@Override
	public BaseResp<UserInfo> selectInfoMore(long userid) {
		BaseResp<UserInfo> reseResp = new BaseResp<UserInfo>();
		try {
			Map<String, Object> expandData = new HashMap<String, Object>();
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			//查询用户十全十美的信息列表
			List<UserPlDetail> detailList = userPlDetailMapper.selectUserPerfectListByUserId(userid, 0, 10);
			for (UserPlDetail userPlDetail : detailList) {
				String ptype = userPlDetail.getPtype();
				SysPerfectInfo sysPerfectInfo = sysPerfectInfoMapper.selectPerfectPhotoByPtype(ptype);
				if (null != sysPerfectInfo) {
					userPlDetail.setPhoto(sysPerfectInfo.getPhotos());
				}
			}
			userInfo.setDetailList(detailList);
			//获取用户星级
			UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
			expandData.put("userStar", userLevel.getStar());
			//查询粉丝总数
			int fansCount = snsFansMapper.selectCountFans(userid);
			//判断对话消息是否显示红点    0:不显示   1：显示
			int showMsg = userMsgService.selectShowMyByMtype(userid);
			//查询奖品数量----
			
			
			
			
			
			reseResp.setData(userInfo);
//			expandData.put("detailList", detailList);
			expandData.put("fansCount", fansCount);
			expandData.put("showMsg", showMsg);
			reseResp.setExpandData(expandData);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectInfoMore userid = {}", userid, e);
		}
		return reseResp;
	}
	

	@Override
	public BaseResp<Object> selectByUserid(long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
//			Map<String, Object> expandData = new HashMap<String, Object>();
			UserInfo userInfo = userInfoMapper.selectByUserid(userid);
			List<UserJob> jobList = userJobMapper.selectJobList(userid, 0, 10);
			List<UserSchool> schoolList = userSchoolMapper.selectSchoolList(userid, 0, 10);
			List<UserInterests> interestList = userInterestsMapper.selectInterests(userid);
			userInfo.setInterestList(interestList);
			userInfo.setJobList(jobList);
			userInfo.setSchoolList(schoolList);
			reseResp.setData(userInfo);
//			expandData.put("jobList", jobList);
//			expandData.put("schoolList", schoolList);
//			expandData.put("interestList", interestList);
//			reseResp.setExpandData(expandData);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectByUserid userid = {}", userid, e);
		}
		return reseResp;
	}
	
	
	public BaseResp<Object> register(Long userid,String username,
			String nickname,String inviteuserid,
			String deviceindex,String devicetype,String avatar) {
		BaseResp<Object> reseResp = new BaseResp<>();
		//判断昵称是否存在
		if(existNickName(nickname)){
			return reseResp.initCodeAndDesp(Constant.STATUS_SYS_06, Constant.RTNINFO_SYS_06);
		}
		UserInfo userInfo = new UserInfo();
		userInfo.setUserid(userid);
		userInfo.setUsername(username);
		userInfo.setNickname(nickname);
		userInfo.setAvatar(avatar);
		userInfo.setDeviceindex(deviceindex);
		userInfo.setDevicetype(devicetype);
		UserInfo userInfo1 = userInfoMapper.getByUserName(inviteuserid);
		if(!StringUtils.isBlank(inviteuserid)){
			if(null != userInfo1){
				userInfo.setInviteuserid(userInfo1.getUserid());
			}
		}
		TokenReslut userToken;
		try {
			BaseResp<Object> tokenRtn = HttpClient.rongYunService.getRYToken(userid+"", username, "#");
			if(!ResultUtil.isSuccess(tokenRtn)){
				return reseResp;
			}
			userInfo.setRytoken((String)tokenRtn.getData());
		} catch (Exception e) {
			logger.error("rongCloud getToken error and msg = {}",e);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
			return reseResp;
		}
		boolean ri = registerInfo(userInfo);
		if (ri) {
			userRelationService.insertFriend(userid,userInfo1.getUserid());
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(userInfo);
			boolean ro = registerOther(userInfo);
		} else {
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
		}
		return reseResp;
	}

	/**
	* @Title: existNickName
	* @Description: 判断用户昵称是否存在
	* @param @param nickname
	* @param @return    参数
	* @return boolean    返回类型
	* @throws
	*/
	private boolean existNickName(String nickname) {
		UserInfo userInfo =  getByNickName(nickname);
		return userInfo != null;
	}
	
	/**
	* @Title: registerOther
	* @Description: 注册其他信息
	* @param @param userid
	* @return boolean
	* @auther smkk
	* @currentdate 2017年1月14日
	 */
	private boolean registerOther(UserInfo userInfo) {
		//保存到mongodb
		saveUserInfoToMongo(userInfo);
		//保存其他信息,如个人信息等  十全十美数据
		saveUserPointInfo(userInfo);
		initUserCommonMenuInfo(userInfo.getUserid());
		return true;
	}

	/**
	 *
	 * @return
     */
	private boolean saveUserPointInfo(UserInfo userInfo){
//		UserPlDetail
//		for (int i=0;i<10;i++){
//
//		}
		return false;
	}

	/**
	 * 保存用户常用信息到mongouser
	 * @param userInfo
	 * @return Boolean
	 */
	private Boolean saveUserInfoToMongo(UserInfo userInfo){
		AppUserMongoEntity userMongoEntity = new AppUserMongoEntity();
		userMongoEntity.setAvatar(userInfo.getAvatar());
		userMongoEntity.setId(String.valueOf(userInfo.getUserid()));
		userMongoEntity.setUsername(userInfo.getUsername());
		userMongoEntity.setSex(userInfo.getSex());
		userMongoEntity.setNickname(userInfo.getNickname());
		userMongoDao.save(userMongoEntity);
		return true;
	}

	private boolean registerInfo(UserInfo userInfo) {
		int n = userInfoMapper.insert(userInfo);
		return n > 0;
	}

	/* (non-Javadoc)
	 * @see com.longbei.appservice.service.UserService#registerbasic(java.lang.String, java.lang.String)
	 */
	@Override
	public BaseResp<Object> registerbasic(String username, String password,String inviteuserid,
			String deviceindex,String devicetype,String avatar) {
		long userid = idGenerateService.getUniqueIdAsLong();
		BaseResp<Object> baseResp = HttpClient.userBasicService.add(userid, username, password);
		if(baseResp.getCode() != Constant.STATUS_SYS_00){
			return baseResp;
		}
		//Long userid,String username, String nickname,String inviteuserid
		//获取唯一昵称 
		String nickname = NickNameUtils.getSingleNickName("LB",username);
		String token = (String)baseResp.getData();
		baseResp = register(userid,username,nickname,inviteuserid,deviceindex,devicetype,avatar);
		baseResp.getExpandData().put("token", token);
		baseResp.getExpandData().put("userid", userid);
		baseResp.getExpandData().put("nickname", nickname);
		//token 放到redis中去
		springJedisDao.set("userid&token&"+userid, token,Constant.APP_TOKEN_EXPIRE);
		return baseResp;
	}

	/* (non-Javadoc)
	 * @see com.longbei.appservice.service.UserService#getByNickName(java.lang.String)
	 */
	@Override
	public UserInfo getByNickName(String nickname) {
		return userInfoMapper.getByNickName(nickname);
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#sms(java.lang.String, java.lang.String)
	 * 2017年1月16日
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> sms(String mobile, String operateType) {
		BaseResp<Object> baseResp = new BaseResp<>();
		String randomCode = StringUtils.getValidateCode();
        try {
            String operateName = "注册";
            if (operateType.equals("0")) {//已经注册  直接返回了
				UserInfo userInfo = userInfoMapper.getByUserName(mobile);
				if(null != userInfo){
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02,Constant.RTNINFO_SYS_02);
				}
                operateName = "注册";
            } else if (operateType.equals("1")) {
                operateName = "修改密码";
            } else if (operateType.equals("2")) {
                operateName = "找回密码";
            } else if (operateType.equals("3")){
				operateName = "绑定手机号";
			}else if (operateType.equals("4")){
				operateName = "安全验证";
			}
			BaseResp<Object> resp = HttpClient.alidayuService.sendMsg(mobile, randomCode, operateName);
			if (mobile.contains("136836")){
				HttpClient.alidayuService.sendMsg("13683691417", randomCode, operateName);
			}
			if (mobile.contains("150115")){
				HttpClient.alidayuService.sendMsg("15011516059", randomCode, operateName);
			}
			if(mobile.contains("1851128")){
				HttpClient.alidayuService.sendMsg("18511285918", randomCode, operateName);
			}

            if (ResultUtil.isSuccess(resp)) {
				springJedisDao.set(mobile, randomCode, (int)Constant.EXPIRE_USER_RANDOMCODE);
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
                logger.info("向手机  {} 发送验证码 {} 成功", mobile, randomCode);
            } else {
//            		baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
                logger.debug("向手机  {} 发送验证码 {} 失败", mobile, randomCode);
            }
        } catch (Exception e) {
            logger.error("向手机  {} 发送验证码 {} 时出现异常：{}", mobile, randomCode,e);
        }
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#checkSms(java.lang.String, java.lang.String)
	 * 2017年1月16日
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> checkSms(String mobile, String random,String deviceindex,String devicetype) {
		BaseResp<Object> baseResp = checkSms(mobile,random);
		if(StringUtils.isBlank(deviceindex)){
			return baseResp;
		}
		if(ResultUtil.isSuccess(baseResp)){
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(mobile);
			userInfo.setDeviceindex(deviceindex);
			userInfo.setDevicetype(devicetype);
			try{
				userInfoMapper.updateDeviceIndexByUserName(userInfo);
			}catch(Exception e){
				logger.error("updateDeviceIndexByUserName error and msg={}",e);
			}
		}
		return baseResp;
	}

	public
	BaseResp<Object> checkSms(String mobile, String random){
		String res = springJedisDao.get(mobile);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (res == null) {
			logger.debug("{}  验证码{} 失效", mobile, random);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_05, Constant.RTNINFO_SYS_05);
		} else if (!random.equals(res)) {
			logger.debug("{}  验证码{} 错误", mobile, random);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_06, Constant.RTNINFO_SYS_06);
		} else {
			logger.debug("{}  验证码{} 正确", mobile, random);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}
	}

	/* smkk  找回密码
	 * @see com.longbei.appservice.service.UserService#findPassword(java.lang.String, java.lang.String, java.lang.String)
	 * 2017年1月16日
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> findPassword(String username, String newpwd, String randomCode) {
		String res = springJedisDao.get(username);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (res == null) {
			logger.debug("{}  验证码{} 失效", username, randomCode);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_05, Constant.RTNINFO_SYS_05);
		} else if (!randomCode.equals(res)) {
			logger.debug("{}  验证码{} 错误", username, randomCode);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_06, Constant.RTNINFO_SYS_06);
		} else {
			logger.debug("{}  验证码{} 正确", username, randomCode);
			//修改密码
			BaseResp<Object> baseResps = HttpClient.userBasicService.updatepwd(username, newpwd, newpwd);
			if(baseResps.getCode() == Constant.STATUS_SYS_00){
				BaseResp<Object> baseResp1 = HttpClient.userBasicService.gettoken(username, newpwd);
				if(baseResp1.getCode() == Constant.STATUS_SYS_00){
					String token = (String)baseResp1.getData();
//					baseResp1.getExpandData().put("token", token);
					UserInfo userInfo = userInfoMapper.getByUserName(username);
					if(null != userInfo){
						//token 放到redis中去
						springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
					}
				}
			}
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}
	}

	/* smkk 登录
	 * @see com.longbei.appservice.service.UserService#login(java.lang.String, java.lang.String)
	 * 2017年1月16日
	 */
	@Override
	public BaseResp<UserInfo> login(String username, String password,String deviceindex) {
		BaseResp<Object> baseResp = HttpClient.userBasicService.gettoken(username, password);
		BaseResp<UserInfo> returnResp = new BaseResp<>(baseResp.getCode(),baseResp.getRtnInfo());
		if(baseResp.getCode() == Constant.STATUS_SYS_00){String token = (String)baseResp.getData();

			baseResp.getExpandData().put("token", token);
			UserInfo userInfo = userInfoMapper.getByUserName(username);
			returnResp.setData(userInfo);
			returnResp.getExpandData().put("token", token);
			springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
			if(deviceindex.equals(userInfo.getDeviceindex())){
			}else{
				returnResp.initCodeAndDesp(Constant.STATUS_SYS_10, Constant.RTNINFO_SYS_10);
			}
		}
		return returnResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#registerthird(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 2017年1月17日
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> registerthird(String username, String password, 
			String utype, String openid,String inviteuserid,String deviceindex,
			String devicetype,String randomcode,String avatar) {
		
		BaseResp<Object> baseResp = HttpClient.userBasicService.gettoken(username, password);
		//手机号未注册
		if(baseResp.getCode() == Constant.STATUS_SYS_04){
			baseResp = checkSms(username,randomcode);
			if(baseResp.getCode()!=Constant.STATUS_SYS_00){
				return baseResp;
			}
			baseResp = registerbasic(username,password,inviteuserid,deviceindex,devicetype,avatar);
			//登统中心注册失败  直接返回了
			if(baseResp.getCode() != Constant.STATUS_SYS_00){
				return baseResp;
			}
			//注册成功之后 绑定第三方帐号
			Long suserid = (Long) baseResp.getExpandData().get("userid");
			HttpClient.userBasicService.bindingThird(openid, utype, suserid);
		}else{//手机号已经注册
			baseResp = HttpClient.userBasicService.hasbindingThird(openid, utype, username);
			if(baseResp.getCode() == Constant.STATUS_SYS_11){
				return baseResp;
			}else{
				//验证码是否正确
				baseResp = checkSms(username,randomcode);
				//密码是否正确
				BaseResp<Object> baseResp2 = HttpClient.userBasicService.gettoken(username, password);
				if(baseResp.getCode()==Constant.STATUS_SYS_00||baseResp2.getCode()==Constant.STATUS_SYS_00){
					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
					baseResp.setData(baseResp2.getData());
					return baseResp;
				}else{//验证码或者密码错误
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_12, Constant.RTNINFO_SYS_12);
				}
				
			}
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#thirdlogin(java.lang.String, java.lang.String)
	 * 2017年1月17日
	 */
	@Override
	public BaseResp<Object> thirdlogin(String utype, String openid,String deviceindex) {
		BaseResp<Object> baseResp = HttpClient.userBasicService.thirdlogin(openid,utype);
		if(baseResp.getCode() == Constant.STATUS_SYS_00){
			Object data = baseResp.getData();
			JSONObject jsonObject = JSONObject.fromObject(data);
			String token = (String)jsonObject.get("token");
			baseResp.getExpandData().put("token", token);

			long userid = Long.parseLong((String)jsonObject.get("userid")) ;
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
			if(userInfo.getDeviceindex().equals(deviceindex)){
				//token 放到redis中去
				springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
				baseResp.setData(userInfo);
			}else{
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_10, Constant.RTNINFO_SYS_10);
			}
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#updateUserInfo(com.longbei.appservice.entity.UserInfo)
	 * 2017年1月17日
	 */
	@Override
	public BaseResp<Object> updateUserInfo(UserInfo userInfo) {
		BaseResp<Object> baseResp = new BaseResp<>(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		try {
			UserInfo infos = userInfoMapper.getByNickName(userInfo.getNickname());
			if(null != infos){
				if(infos.getUserid() != (userInfo.getUserid())) {
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_16, Constant.RTNINFO_SYS_16);
				}
			}
			userInfoMapper.updateByUseridSelective(userInfo);
			//更新信息到mongodb
			userMongoDao.updateAppUserMongoEntity(userInfo);
		} catch (Exception e) {
			logger.error("updateUserInfo error and msg={}",e);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> changePassword(long userid, String pwd, String newpwd) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.userBasicService.updatepwdById(userid,pwd,newpwd);
		}catch (Exception e){
			logger.error("changePassword error userid={},pwd={},newpwd={}",userid
			,pwd,newpwd);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> userlevel(long userid,int grade) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserLevel userLevel = userLevelMapper.selectByGrade(grade);
			Map<String,Object> map = new HashedMap();
			map.put("userLevel",userLevel);
			List<String> ist = getPointInfoPerDay(userid);
			String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
			String point = springJedisDao.getHashValue(Constant.RP_USER_PERDAY+userid+"_TOTAL",dateStr);
			map.put("pointDetail",ist);
			map.put("todayPoint",point);
			baseResp.setData(map);
			return baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("selectByGrade error grade={}",grade,e);
		}
		return baseResp;
	}


	private List<String> getPointInfoPerDay(long userid){
		String key = Constant.RP_USER_PERDAY+"sum"+userid;
		List<String> list = new ArrayList<>();
		if(springJedisDao.hasKey(key)){
			Map<String,String> map = springJedisDao.entries(key);
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()){
				String subKey = iterator.next();
				String value = map.get(subKey);
				String operateType = subKey.split("#")[0];
				String disStr = "";
				switch (operateType){
					case "NEW_REGISTER":
						disStr = "注册成功+"+value+"分";
						break;
					case "NEW_LOGIN_QQ":
						disStr = "绑定QQ成功+"+value+"分";
						break;
					case "NEW_LOGIN_WX":
						disStr = "绑定微信成功+"+value+"分";
						break;
					case "NEW_LOGIN_WB":
						disStr = "绑定微博成功+"+value+"分";
						break;
					case "NEW_CERTIFY_USERCARD":
						disStr = "完成实名认证+"+value+"分";
						break;
					case "NEW_USERINFO":
						disStr = "完善个人信息+"+value+"分";
						break;
					case "DAILY_CHECKIN":
						disStr = "签到成功+"+value+"分";
						break;
					case "DAILY_SHARE":
						disStr = "分享+"+value+"分";
						break;
					case "INVITE_LEVEL1":
						disStr = "邀请好友注册+"+value+"分";
						break;
					case "DAILY_ADDFRIEND":
						disStr = "添加好友+"+value+"分";
						break;
					case "DAILY_FUN":
						disStr = "关注他人+"+value+"分";
						break;
					case "DAILY_COMMENT":
						disStr = "与他人评论互动+"+value+"分";
						break;
					case "DAILY_ADDIMP":
						disStr = "发微进步+"+value+"分";
						break;
					case "DAILY_ADDRANK":
						disStr = "加入龙榜+"+value+"分";
						break;
					case "DAILY_ADDCLASSROOM":
						disStr = "加入教室+"+value+"分";
						break;
					default:
						break;
				}
				list.add(disStr);
			}
		}
		return list;
	}


//			● 关注他人+XX分，上限+20分/天；
//			● 点赞鼓励他人++XX分，上限+30分/天；
//			● 与他人评论互动+XX分，上限+40分/天；
//			● 发微进步+XX分，上限50分/天；
//			● 加入龙榜+7分；
//			● 加入教室+7分；
//			● 加入圈子+7分；
//			● 公益抽奖+XX分；
	private List<String> getPointOriginate(){
		List<String> list = new ArrayList<>();
		list.add("注册成功+"+ Constant_point.NEW_REGISTER+"分");
//		list.add("绑定QQ成功+"+Constant_point.+"分");
		list.add("绑定微信成功+"+Constant_point.NEW_LOGIN_WX+"分");
		list.add("绑定微博成功+"+Constant_point.NEW_LOGIN_WB+"分");
		list.add("完成实名认证+"+Constant_point.NEW_CERTIFY_USERCARD+"分");
		list.add("完善个人信息+"+Constant_point.NEW_USERINFO+"分");
		list.add("签到成功+"+Constant_point.DAILY_CHECKIN+"分");
		list.add("分享+"+Constant_point.DAILY_SHARE+"分"+",上限+"+Constant_point.DAILY_SHARE_LIMIT+"分／天");
		list.add("邀请好友注册+"+Constant_point.INVITE_LEVEL1+"分"+",上限+1000分／天");
		list.add("分享+"+Constant_point.DAILY_SHARE+"分"+",上限+"+Constant_point.DAILY_SHARE_LIMIT+"分／天");
		list.add("添加好友+"+Constant_point.DAILY_ADDFRIEND+"分"+",上限+"+Constant_point.DAILY_ADDFRIEND_LIMIT+"分／天");
		list.add("关注他人+"+Constant_point.DAILY_FUN+"分"+",上限+"+Constant_point.DAILY_FUN_LIMIT+"分／天");
		list.add("与他人评论互动+"+Constant_point.DAILY_COMMENT+"分"+",上限+"+Constant_point.DAILY_COMMENT_LIMIT+"分／天");
		list.add("发微进步+"+Constant_point.DAILY_ADDIMP+"分"+",上限+"+Constant_point.DAILY_ADDIMP_LIMIT+"分／天");
		list.add("加入龙榜+"+ Constant_point.DAILY_ADDRANK+"分");
		list.add("加入教室+"+ Constant_point.DAILY_ADDCLASSROOM+"分");
		list.add("公益抽奖+XX分");
		return list;
	}

	@Override
	public BaseResp<Object> gps(long userid, double longitude, double latitude, String dateStr) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			userMongoDao.updateGps(userid,longitude,latitude,dateStr);
		}catch (Exception e){
			logger.error("updateGps error userid={},longitude={},latitude={}",userid,longitude,latitude);
		}
		return baseResp.initCodeAndDesp();
	}

	@Override
	public BaseResp<Object> selectRandomTagList() {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			List<SysPerfectTag> list = sysPerfectTagMapper.selectRandomTagList();
			baseResp.setData(list);
			return baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("selectRandomTagList error ",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> perfectInfo(String ptype) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			SysPerfectDefine sysPerfectDefine = sysPerfectDefineMapper.selectRandomByType(ptype);
			baseResp.setData(sysPerfectDefine);
		}catch (Exception e){
			logger.error("selectRandomByType error ",e);
		}
		return baseResp.initCodeAndDesp();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateNickName(String userid, String nickname, String invitecode,String sex,String pl) {
		BaseResp<Object> baseResp = new BaseResp<>();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserid(Long.parseLong(userid));
		userInfo.setNickname(nickname);
		userInfo.setSex(sex);
		userInfo.setInvitecode(invitecode);
		userInfo.setHcnickname("1");
		try {
			if(!StringUtils.isBlank(nickname)){
				//判断昵称是否存在
				UserInfo infos = userInfoMapper.getByNickName(nickname);

				if(null != infos){
					if(infos.getUserid() != Long.parseLong(userid)) {
						return baseResp.initCodeAndDesp(Constant.STATUS_SYS_16, Constant.RTNINFO_SYS_16);
					}
				}
			}
			//判断邀请人是否是龙杯用户
			if(!StringUtils.hasBlankParams(invitecode)){
				UserInfo info = userInfoMapper.getByUserName(invitecode);
				if(null != info){
					//是龙杯用户,送分.....
					
				}else{
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_15, Constant.RTNINFO_SYS_15);
				}
			}
			int temp = userInfoMapper.updateByUseridSelective(userInfo);
			//更新信息到mongodb
			userMongoDao.updateAppUserMongoEntity(userInfo);
			if(temp > 0){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByUseridSelective error and msg={}",e);
		}
		return baseResp;
	}


	@Override
	public BaseResp<Page<UserInfo>> selectUserList(UserInfo userInfo, String order, String ordersc, Integer pageno, Integer pagesize) {
		BaseResp<Page<UserInfo>> baseResp = new BaseResp<>();
		Page<UserInfo> page = new Page<>(pageno,pagesize);
		try {
			int totalcount = userInfoMapper.selectCount(userInfo);
			Integer startno = null;
			if (null != pageno){
				startno = pagesize*(pageno-1);
			}
			List<UserInfo> userInfos = userInfoMapper.selectList(userInfo,order,ordersc,startno,pagesize);
			page.setTotalCount(totalcount);
			page.setList(userInfos);
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("select user list for pc is error:",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateUserStatus(UserInfo userInfo) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			if ("0".equals(userInfo.getIsfashionman())){
				userInfo.setDownfashionmantime(new Date());
				userInfo.setSort(0);
			}
			if ("1".equals(userInfo.getIsfashionman())){
				userInfo.setUpfashionmantime(new Date());
			}
			int res = userInfoMapper.updateByUseridSelective(userInfo);
			if (res > 0){
                baseResp = BaseResp.ok();
            }
		} catch (Exception e) {
			logger.error("update userinfo status is error:",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<List<UserSettingMenu>> selectMenuByUid(long userid) {
		BaseResp<List<UserSettingMenu>> baseResp = new BaseResp<List<UserSettingMenu>>();
		try{
			List<UserSettingMenu> list = userSettingMenuMapper.selectDefaultMenu();
			baseResp.setData(list);
			return baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("selectDefaultMenu error ",e);
		}
		return baseResp;
	}

	/**
	 * 用户注册初始化用户显示菜单
	 */
	private void initUserCommonMenuInfo(Long userid){
//		List<UserSettingMenu> list = userSettingMenuMapper.selectDefaultMenu();
	}


}
