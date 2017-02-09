package com.longbei.appservice.service.impl;

import java.util.UUID;

import com.longbei.appservice.common.utils.RequestUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserPlDetail;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.expand.AlidayuSmsUtils;
import com.longbei.appservice.common.expand.RongCloudProxy;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserService;
import com.longbei.appservice.service.api.FeignApiProxy;

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
	
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@SuppressWarnings("unchecked")
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
		if(!StringUtils.isBlank(inviteuserid)){
			userInfo.setInviteuserid(Long.parseLong(inviteuserid));
		}
		TokenReslut userToken;
		try {
			userToken = RongCloudProxy.rongCloud.user.getToken(userid+"", username, "#");
			if(null != userToken){
				JSONObject jsonObject = JSONObject.fromObject(userToken);
				userInfo.setRytoken(jsonObject.getString("token"));
			}
		} catch (Exception e) {
			logger.error("rongCloud getToken error and msg = {}",e);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
			return reseResp;
		}
		boolean ri = registerInfo(userInfo);
		if (ri) {
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
		BaseResp<Object> baseResp = FeignApiProxy.userBasicService.add(userid, username, password);
		if(baseResp.getCode() != Constant.STATUS_SYS_00){
			return baseResp;
		}
		//Long userid,String username, String nickname,String inviteuserid
		//获取唯一昵称 
		String nickname = "lb_"+UUID.randomUUID();
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
        String rtn = "";
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
            rtn = AlidayuSmsUtils.sendMsgValidate(mobile, randomCode, operateName);

            if (StringUtils.isBlank(rtn)) {
            		baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            		springJedisDao.set(mobile, randomCode, (int)Constant.EXPIRE_USER_RANDOMCODE);
                logger.info("向手机  {} 发送验证码 {} 成功", mobile, randomCode);
            } else {
            		baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
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

	private BaseResp<Object> checkSms(String mobile, String random){
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
			BaseResp<Object> baseResps = FeignApiProxy.userBasicService.updatepwd(username, newpwd, newpwd);
			if(baseResps.getCode() == Constant.STATUS_SYS_00){
				BaseResp<Object> baseResp1 = FeignApiProxy.userBasicService.gettoken(username, newpwd);
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
		BaseResp<Object> baseResp = FeignApiProxy.userBasicService.gettoken(username, password);
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
		
		BaseResp<Object> baseResp = FeignApiProxy.userBasicService.gettoken(username, password);
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
			FeignApiProxy.userBasicService.bindingThird(openid, utype, suserid);
		}else{//手机号已经注册
			baseResp = FeignApiProxy.userBasicService.hasbindingThird(openid, utype, username);
			if(baseResp.getCode() == Constant.STATUS_SYS_11){
				return baseResp;
			}else{
				//验证码是否正确
				baseResp = checkSms(username,randomcode);
				//密码是否正确
				BaseResp<Object> baseResp2 = FeignApiProxy.userBasicService.gettoken(username, password);
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
		BaseResp<Object> baseResp = FeignApiProxy.userBasicService.thirdlogin(openid,utype);
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
			userInfoMapper.updateByUseridSelective(userInfo);
			//更新信息到mongodb
			userMongoDao.updateAppUserMongoEntity(userInfo);
		} catch (Exception e) {
			logger.error("updateUserInfo error and msg={}",e);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
		}
		return baseResp;
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
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_16, Constant.RTNINFO_SYS_16);
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
	
	
	
	

}
