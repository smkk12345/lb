package com.longbei.appservice.service.impl;

import java.text.DecimalFormat;
import java.util.*;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_Perfect;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.service.mq.send.QueueMessageSendService;
import com.longbei.appservice.common.utils.*;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.MsgRedMongDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.*;

import com.longbei.appservice.service.*;
import com.longbei.appservice.service.api.outernetservice.IAlidayuService;
import com.longbei.appservice.service.api.outernetservice.IJPushService;
import com.longbei.appservice.service.api.outernetservice.IRongYunService;
import com.longbei.appservice.service.api.userservice.IUserBasicService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.constant.Constant;

import net.sf.json.JSONObject;
import org.springframework.util.*;

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
	@Autowired
	private UserSettingCommonMapper userSettingCommonMapper;
	@Autowired
	private RankAcceptAwardService rankAcceptAwardService;
	@Autowired
	private IAlidayuService iAlidayuService;
	@Autowired
	private IUserBasicService iUserBasicService;
	@Autowired
	private IRongYunService iRongYunService;
	@Autowired
	private IJPushService ijPushService;
	@Autowired
	private UserPlDetailService userPlDetailService;
	@Autowired
	private UserInterestsService userInterestsService;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private QueueMessageSendService queueMessageSendService;
	@Autowired
	private UserBehaviourService userBehaviourService;
	@Autowired
	private UserImpCoinDetailService userImpCoinDetailService;
	@Autowired
	private JPushService jPushService;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private UserMoneyHintMongoDao userMoneyHintMongoDao;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private UserAccountMapper userAccountMapper;
	@Autowired
	private GroupService groupService;
	@Autowired
	private DeviceIndexMapper deviceIndexMapper;
	@Autowired
	private StatisticService statisticService;
	@Autowired
	private MsgRedMongDao msgRedMongDao;
	@Autowired
	private SysNicknamesMapper sysNicknamesMapper;
	@Autowired
	private SysSensitiveService sysSensitiveService;
	@Autowired
	private ImproveService improveService;

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);



	@Override
	public BaseResp<Integer> isUserMoneyHint(String userid) {
		BaseResp<Integer> reseResp = new BaseResp<Integer>();
		try {
			UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
			int money = 0;
			if(null != userInfo){
				UserMoneyHint userMoneyHint = userMoneyHintMongoDao.selectUserMoneyHintByUid(userid);
				if(null != userMoneyHint){
					money = userInfo.getTotalcoin() - Integer.parseInt(userMoneyHint.getMoney());
					userMoneyHintMongoDao.updateHintAddMoney(userid, money);
				}else{
					//添加
					UserMoneyHint userMoney = new UserMoneyHint();
					userMoney.setDrawtime(DateUtils.formatDateTime1(new Date()));
					userMoney.setMoney(userInfo.getTotalcoin() + "");
					userMoney.setUserid(userid);
					userMoneyHintMongoDao.insertUserMoneyHint(userMoney);
				}
				reseResp.setData(money);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("selectByUserid userid = {}", userid, e);
		}
		return reseResp;
	}


	@Override
	public BaseResp<UserInfo> selectInfoMore(long userid,long lookid) {
		BaseResp<UserInfo> reseResp = new BaseResp<UserInfo>();
		try {
			Map<String, Object> expandData = new HashMap<String, Object>();
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(userInfo == null){
				logger.error("query userInfo null userid:{}",userid);
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
			}
//			if(lookid != userid){
//				//获取好友昵称
//				String remark = userRelationService.selectRemark(lookid, userid);
//				if(!StringUtils.isBlank(remark)){
//					userInfo.setNickname(remark);
//				}
//			}
			//查询用户十全十美的信息列表
			List<UserPlDetail> detailList = userPlDetailMapper.selectUserPerfectListByUserId(userid, 0, 10);
			if(detailList == null || detailList.size() == 0 || detailList.get(0) == null){
				userInfo.setDetailList(new ArrayList<UserPlDetail>());
			}else{
				for (UserPlDetail userPlDetail : detailList) {
					String ptype = userPlDetail.getPtype();
					SysPerfectInfo sysPerfectInfo = sysPerfectInfoMapper.selectPerfectPhotoByPtype(ptype);
					if (null != sysPerfectInfo) {
						userPlDetail.setPhoto(sysPerfectInfo.getPhotos());
					}
				}
				userInfo.setDetailList(detailList);
			}

			//获取用户星级
//			UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
//			expandData.put("userStar", userLevel.getStar());
			//龙级
			expandData.put("grade", userInfo.getGrade());
			//查询粉丝总数
//			int fansCount = snsFansMapper.selectCountFans(userid);
			expandData.put("fansCount", userInfo.getTotalfans());
			
			//查询好友数量
			Integer friendCount = userRelationService.selectFriendsCount(userid);
			expandData.put("friendCount", friendCount);
			//获取用户被赠与的进步花
//			int flowernum = 0;
//			List<UserFlowerDetail> list = userFlowerDetailMapper.selectListByOrigin(userid, "3", 0, 1);
//			if(null != list && list.size()>0){
//				flowernum = userFlowerDetailMapper.selectCountFlower(userid);
//			}
			expandData.put("flowernum", userInfo.getGivedflowers());
			if(lookid != 0){
				if(this.userRelationService.checkIsFriend(lookid,userid)){
					userInfo.setIsfriend("1");
					//获取好友昵称
					String remark = this.userRelationService.getUserRemark(lookid,userid);
					if(StringUtils.isNotEmpty(remark)){
						userInfo.setRemark(remark);
					}
				}
				userInfo.setIsfans(this.userRelationService.checkIsFans(userid,lookid)?"1":"0");

			}
			//判断对话消息是否显示红点    0:不显示   1：显示
			int showMsg =userMsgService.selectCountShowMyByMtype(userid);
			expandData.put("showMsg", showMsg);

			//查询奖品数量----
			Integer awardnum = 0;
			if(lookid != userid){
				//非本人查看
				awardnum = rankAcceptAwardService.userRankAcceptAwardCount(userid, "0");
			}else{
				awardnum = rankAcceptAwardService.userRankAcceptAwardCount(userid, null);
			}
			expandData.put("awardnum", awardnum);
			reseResp.setData(userInfo);
//			expandData.put("detailList", detailList);
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
			UserInterests userInterests = userInterestsService.selectInterests(userid).getData();
			if(null == userInterests || null == userInterests.getPtype()){
				userInfo.setInterestList(new ArrayList<SysPerfectTag>());
			}else{
				String ptypes[] = userInterests.getPtype().split(",");
				List<SysPerfectTag> userTagList = sysPerfectTagMapper.selectUserTagList(ptypes);
				userInfo.setInterestList(userTagList);
			}
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

	@Override
	public UserInfo selectJustInfo(long userid) {
		UserInfo userInfo = null;
		try {
			userInfo = userInfoMapper.selectByUserid(userid);
		} catch (Exception e) {
			logger.error("selectJustUserInfo userid = {}", userid, e);
		}
		return userInfo;
	}


	public BaseResp<Object> register(final Long userid, String username,
									 String nickname, final String inviteuserid,
									 String deviceindex, String devicetype, String avatar) {
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

//		UserInfo userInfo1 = userInfoMapper.getByUserName(inviteuserid);
//		if(!StringUtils.isBlank(inviteuserid)){
//			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(inviteuserid);
//			if(null != appUserMongoEntity){
//				userInfo.setInviteuserid(Long.parseLong(appUserMongoEntity.getId()));
//			}
//		}
		try {
			BaseResp<Object> tokenRtn = iRongYunService.getRYToken(String.valueOf(userid), username, "#");
			if(!ResultUtil.isSuccess(tokenRtn)){
				return reseResp;
			}
			userInfo.setRytoken((String)tokenRtn.getData());
		} catch (Exception e) {
			logger.error("rongCloud getToken error and msg = {}",e);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_02, Constant.RTNINFO_SYS_02);
			return reseResp;
		}
		boolean ri = false;
		UserInfo userInfo1 = null;
		try{
			if (!StringUtils.isBlank(inviteuserid)){
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(inviteuserid);
				if(null != appUserMongoEntity){
					userInfo1 = userInfoMapper.selectByUserid(Long.parseLong(appUserMongoEntity.getId()));
					//给推荐人添加龙币
					userInfo.setInvitecode(inviteFriendIds(userInfo1));
					userInfo.setHandleinvite("0");
				}
			}
			ri = registerInfo(userInfo);
		}catch (Exception e){
			logger.error("registerInfo",e);
		}
		if (ri) {
			//建立好友关系
			if (null != userInfo1){
				BaseResp<Object> insertFriendBaseResp = userRelationService.insertFriend(userid,userInfo1.getUserid());
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(userInfo);
			boolean ro = registerOther(userInfo);
		} else {
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
		}
		return reseResp;
	}

	private BaseResp<Object> registerIndex(String deviceindex,String username){
		BaseResp<Object> baseResp = new BaseResp<>();
		List<DeviceRegister> list = deviceIndexMapper.selectRegisterCountByDevice(deviceindex);
		if(null != list&&list.size()==1){
			DeviceRegister deviceRegister = list.get(0);
			if(deviceRegister.getRegistercount()>=SysRulesCache.behaviorRule.getRegisterdevicelimit()){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_115,Constant.RTNINFO_SYS_115);
			}
		}

		Date date = new Date();
		int n = deviceIndexMapper.updateRegisterCount(deviceindex,
				DateUtils.formatDate(date,"yyyy-MM-dd HH:mm:ss"),username);
		if(n == 0){
			DeviceRegister deviceRegister = new DeviceRegister();
			deviceRegister.setDeviceindex(deviceindex);
			deviceRegister.setLastregistertime(date);
			deviceRegister.setLastusername(username);
			deviceRegister.setRegistercount(1);
			deviceIndexMapper.insert(deviceRegister);
		}
		return baseResp.initCodeAndDesp();
	}

	/**
	 * @Title: initUserPerfectTen
	 * @Description: 初始化用户十全十美信息
	 * @param userid
	 * @return boolean 返回类型
	 * @auther IngaWu
	 * @currentdate:2017年5月3日
	 */
	public boolean initUserPerfectTen(long userid){
		Date date = new Date();
		List<UserPlDetail> userPlDetailList =new ArrayList<>();
		for(Integer i = 0 ;i < 10; i++) {
			UserPlDetail userPlDetail = new UserPlDetail();
			userPlDetail.setUserid(userid);
			userPlDetail.setLeve(1);
			userPlDetail.setPtype(i.toString());
			userPlDetail.setScorce(0);
			userPlDetail.setToplevel("0");
			userPlDetail.setCreatetime(date);
			userPlDetailList.add(userPlDetail);
		}
		Integer n = null;
		try {
			n = userPlDetailService.insertBatchUserPlDetails(userPlDetailList);
		} catch (Exception e) {
			logger.error("initUserPerfectTen error and msg = {}",e);
		}
        if(n > 0){
			return true;
		}
		return false;
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
	private boolean registerOther(final UserInfo userInfo) {
		threadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				//保存到mongodb
				saveUserInfoToMongo(userInfo);
				//保存其他信息,如个人信息等  十全十美数据
				saveUserPointInfo(userInfo);
				initUserCommonMenuInfo(userInfo.getUserid());
				//初始化用户设置
				initUserUserSettingCommon(userInfo.getUserid());
				//初始化用户十全十美等级明细
				initUserPerfectTen(userInfo.getUserid());
				//初始化用户感兴趣的标签
				initUserInterestInfo(userInfo.getUserid());
				//注册获得龙分
				userBehaviourService.pointChange(userInfo,"NEW_REGISTER",Constant_Perfect.PERFECT_GAM,null,0,0);
			}
		});
		return true;
	}


	private boolean initUserInterestInfo(long userid){
		UserInterests userInterests = new UserInterests();
		userInterests.setUserid(String.valueOf(userid));
		Date date = new Date();
		userInterests.setCreatetime(date);
		userInterests.setUpdatetime(date);
		userInterestsMapper.insert(userInterests);
		return true	;
	}


	/**
	 * 初始化用户设置
	 */
	private void initUserUserSettingCommon(long userid){
		List<UserSettingCommon> list = new ArrayList<UserSettingCommon>();
		UserSettingCommon common = new UserSettingCommon(userid, "is_new_fans", "1", "新粉丝", new Date(), new Date());
		UserSettingCommon common2 = new UserSettingCommon(userid, "is_like", "1", "点赞", new Date(), new Date());
		UserSettingCommon common3 = new UserSettingCommon(userid, "is_flower", "1", "送花", new Date(), new Date());
		UserSettingCommon common4 = new UserSettingCommon(userid, "is_comment", "2", "评论设置", new Date(), new Date());
		UserSettingCommon common5 = new UserSettingCommon(userid, "is_nick_search", "1", "允许通过昵称搜到我", new Date(), new Date());
		UserSettingCommon common6 = new UserSettingCommon(userid, "is_phone_search", "1", "允许通过此手机号搜到我", new Date(), new Date());
		UserSettingCommon common7 = new UserSettingCommon(userid, "is_newfriendask", "1", "新好友申请", new Date(), new Date());
		list.add(common);
		list.add(common2);
		list.add(common3);
		list.add(common4);
		list.add(common5);
		list.add(common6);
		list.add(common7);
		userSettingCommonMapper.insertList(list);
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
		userMongoEntity.setCreatetime(DateUtils.formatDateTime1(new Date()));
		userMongoEntity.setUpdatetime(DateUtils.formatDateTime1(new Date()));
		userMongoEntity.setUserid(String.valueOf(userInfo.getUserid()));
		userMongoEntity.setAvatar(userInfo.getAvatar());
		userMongoDao.save(userMongoEntity);
		return true;
	}

	private boolean registerInfo(UserInfo userInfo) {
		userInfo.setCreatetime(new Date());
		userInfo.setTotalcoin(0);
		userInfo.setTotaldiamond(0);
		userInfo.setTotalflower(0);
		userInfo.setTotalmoney(0);
		userInfo.setUpdatetime(new Date());
		if(StringUtils.isBlank(userInfo.getSex())){
			userInfo.setSex("0");
		}
		userInfo.setHcnickname("0");
		userInfo.setPoint(0);
		userInfo.setGrade(1);
		userInfo.setCurpoint(0);
		userInfo.setSchoolcertify("0");
		userInfo.setJobcertify("0");
		userInfo.setIsfashionman("0");
		userInfo.setTotalimp(0);
		userInfo.setTotallikes(0);
		userInfo.setTotalfans(0);
		userInfo.setGivedflowers(0);
		userInfo.setSortno(0);
		int n = userInfoMapper.insertSelective(userInfo);
		return n > 0 ? true : false;
	}

	/* (non-Javadoc)
	 * @see com.longbei.appservice.service.UserService#registerbasic(java.lang.String, java.lang.String)
	 */
	@Override
	public BaseResp<Object> registerbasic(String username, String password,String inviteuserid,
			String deviceindex,String devicetype,String avatar,String nickname) {
		long userid = idGenerateService.getUniqueIdAsLong();

		//验证设备号注册
		if (!StringUtils.isBlank(deviceindex)){
			BaseResp reseResp = registerIndex(deviceindex,username);
			if(ResultUtil.fail(reseResp)){
				return reseResp;
			}
		}


		BaseResp<Object> baseResp = iUserBasicService.add(userid, username, password);
		if(baseResp.getCode() != Constant.STATUS_SYS_00){
			return baseResp;
		}
		String token = (String)baseResp.getData();
		//Long userid,String username, String nickname,String inviteuserid
		//获取唯一昵称
		if(StringUtils.isBlank(nickname)){
			nickname = getRandomNickName();
		}
		nickname = getSingleNickName(nickname);

		baseResp = sysSensitiveService.getSensitiveWordSet(nickname);
		if(!ResultUtil.isSuccess(baseResp)){
			return baseResp;
		}

		if(!StringUtils.hasBlankParams(nickname)){
			if(nickname.length() > 26||nickname.length() < 2){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_911,Constant.RTNINFO_SYS_911);
			}
		}

		baseResp = register(userid,username,nickname,inviteuserid,deviceindex,devicetype,avatar);
		if(ResultUtil.isSuccess(baseResp)){
			baseResp.getExpandData().put("token", token);
			baseResp.getExpandData().put("userid", userid);
			baseResp.getExpandData().put("nickname", nickname);
			//token 放到redis中去
			springJedisDao.set("userid&token&"+userid, token,Constant.APP_TOKEN_EXPIRE);
			//注册成功,当日注册数加1
			statisticService.updateStatistics(Constant.SYS_REGISTER_NUM,1);
		}else{
			//失败之后删除登统中心数据
			iUserBasicService.remove(username);
		}
		return baseResp;
	}

	/**
	 * 获取不重复的用户昵称
	 * @param nickname
	 * @return
	 */
	private String getSingleNickName(String nickname) {
		AppUserMongoEntity app = userMongoDao.getAppUserByNickName(nickname);
		while (null != app ){
			nickname = nickname + RandomUtils.getRandomCode(1,10000);
			app = userMongoDao.getAppUserByNickName(nickname);
		}
		return nickname;
	}

	/* (non-Javadoc)
	 * @see com.longbei.appservice.service.UserService#getByNickName(java.lang.String)
	 */
	@Override
	public UserInfo getByNickName(String nickname) {
		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByNickName(nickname);
		if(null == appUserMongoEntity){
			return null;
		}
		return userInfoMapper.selectByUserid(Long.parseLong(appUserMongoEntity.getId()));
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
//		String randomCode = "0000";

        try {
            String operateName = "注册";
            if (operateType.equals("0")) {//已经注册  直接返回了
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(mobile);
				if(null != appUserMongoEntity){
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_02,Constant.RTNINFO_SYS_02);
				}
                operateName = "注册";
            } else if (operateType.equals("1")) {
                operateName = "修改密码";
            } else if (operateType.equals("2")) {
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(mobile);
//				UserInfo userInfo = userInfoMapper.getByUserName(mobile);
				if(null != appUserMongoEntity){
				}else {
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_04,Constant.RTNINFO_SYS_04);
				}
                operateName = "找回密码";
            } else if (operateType.equals("3")){
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(mobile);
				if(null != appUserMongoEntity){
					baseResp.getExpandData().put("isregistered","1");
				}else {
					baseResp.getExpandData().put("isregistered","0");
				}
				operateName = "绑定手机号";
			}else if (operateType.equals("4")){
				operateName = "安全验证";
			}
			BaseResp<Object> resp = new BaseResp<>();
			resp.initCodeAndDesp();
//			BaseResp resp = iAlidayuService.sendMsg(mobile, randomCode, operateName);
//			if (mobile.contains("136836")){
//				iAlidayuService.sendMsg("13683691417", randomCode, operateName);
//			}
//			else if (mobile.contains("1861207")){
//				iAlidayuService.sendMsg("18612073860", randomCode, operateName);
//			}else if(mobile.contains("1501158")){
//				iAlidayuService.sendMsg("15011587112", randomCode, operateName);
//			}
//			else if (mobile.contains("1501151")){
//				iAlidayuService.sendMsg("15011516059", randomCode, operateName);
//			}
//			else if(mobile.contains("1851128")){
//				iAlidayuService.sendMsg("18511285918", randomCode, operateName);
//			}else{
				resp = iAlidayuService.sendMsg(mobile, randomCode, operateName);
//			}

            if (ResultUtil.isSuccess(resp)) {
				springJedisDao.set(mobile, randomCode, (int)Constant.EXPIRE_USER_RANDOMCODE);
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
                logger.info("向手机  {} 发送验证码 {} 成功", mobile, randomCode);
            } else {
//            		baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
                logger.debug("向手机  {} 发送验证码 {} 失败", mobile, randomCode);
				baseResp = resp;
            }
        } catch (Exception e) {
            logger.error("向手机  {} 发送验证码 {} 时出现异常：{}", mobile, randomCode,e);
        }
		return baseResp;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> smsBatch(List<String> mobiles,String template) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			baseResp = iAlidayuService.sendMsgBatch(mobiles,template);
		} catch (Exception e) {
			logger.error("smsBatch is error",e);
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
			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(mobile);
//			UserInfo userInfo = userInfoMapper.getByUserName(mobile);
			try{
				if(null != appUserMongoEntity){
					userInfoMapper.clearOtherDevice(Long.parseLong(appUserMongoEntity.getId()), deviceindex);
					userInfoMapper.updateIndexDevice(Long.parseLong(appUserMongoEntity.getId()), deviceindex);
				}
//				userInfoMapper.updateDeviceIndexByUserName(userInfo);
			}catch(Exception e){
				logger.error("updateDeviceIndexByUserName error and msg={}",e);
			}
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> checkSmsAndLogin(String mobile, String random, String deviceindex, String devicetype) {
		BaseResp<Object> baseResp = checkSms(mobile, random,deviceindex,devicetype);
		if(ResultUtil.isSuccess(baseResp)){
			String date = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
			springJedisDao.sAdd(deviceindex+date+"login",null,mobile);
		}
		return baseResp;
	}

	public
	BaseResp<Object> checkSms(String mobile, String random){
		BaseResp<Object> baseResp = new BaseResp<>();
		if(random.equals("1688")&&!StringUtils.isMobileNO(mobile)){
			return baseResp.initCodeAndDesp();
		}
		String res = springJedisDao.get(mobile);
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
		if(randomCode.equals("1688")&&!StringUtils.isMobileNO(username)){
			return baseResp.initCodeAndDesp();
		}
		if (res == null) {
			logger.debug("{}  验证码{} 失效", username, randomCode);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_05, Constant.RTNINFO_SYS_05);
		} else if (!randomCode.equals(res)) {
			logger.debug("{}  验证码{} 错误", username, randomCode);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_06, Constant.RTNINFO_SYS_06);
		} else {
			logger.debug("{}  验证码{} 正确", username, randomCode);
			//修改密码
			BaseResp<Object> baseResps = iUserBasicService.updatepwd(username, newpwd, newpwd);
			if(baseResps.getCode() == Constant.STATUS_SYS_00){
				BaseResp<Object> baseResp1 = iUserBasicService.gettoken(username, newpwd);
				if(baseResp1.getCode() == Constant.STATUS_SYS_00){
					String token = (String)baseResp1.getData();
//					baseResp1.getExpandData().put("token", token);
					AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(username);
					if(null != appUserMongoEntity){
						//token 放到redis中去
						springJedisDao.set("userid&token&"+appUserMongoEntity.getId(), token);
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
		BaseResp<Object> baseResp = iUserBasicService.gettoken(username, password);
		BaseResp<UserInfo> returnResp = new BaseResp<>();
		if(baseResp.getCode() == Constant.STATUS_SYS_00){
			String token = (String)baseResp.getData();
			baseResp.getExpandData().put("token", token);
			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(username);
			UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(appUserMongoEntity.getId()));
			if(StringUtils.isBlank(userInfo.getRytoken())){
				try {
					BaseResp<Object> tokenRtn = iRongYunService.getRYToken(userInfo.getUserid()+"", username, "#");
					if(ResultUtil.isSuccess(tokenRtn)){
						userInfo.setRytoken((String)tokenRtn.getData());
						userInfoMapper.updateByUseridSelective(userInfo);
					}
				}catch (Exception e){
					logger.error("userid={},username={} getRYToken error",
							userInfo.getUserid(),userInfo.getUsername(),e);
				}
			}

			returnResp.setData(userInfo);
			returnResp.getExpandData().put("token", token);
			if(null == userInfo){
				return returnResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_04);
			}
			springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
			baseResp =  canAbleLogin(deviceindex,userInfo.getUsername(),userInfo.getUserid());
			if(ResultUtil.fail(baseResp)){
				return returnResp.initCodeAndDesp(baseResp.getCode(),baseResp.getRtnInfo());
			}
			String date = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
			springJedisDao.sAdd(deviceindex+date+"login",null,username);
			addLoginRecord(userInfo.getUsername(),deviceindex);
			returnResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} else  {
			returnResp.initCodeAndDesp(baseResp.getCode(),baseResp.getRtnInfo());
		}
		return returnResp;
	}


	private BaseResp<Object> canAbleLogin(String deviceindex,String username,long userid){
		BaseResp<Object> baseResp = new BaseResp<>();
		if(username.equals("13716832441")){
			return baseResp.initCodeAndDesp();
		}
		//每日次数限制
		if(!canLoginTimesPerDay(deviceindex,username)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_114,Constant.RTNINFO_SYS_114);
		}
		//设备号切换
		baseResp = deviceIndexChange(deviceindex,userid);
		if(ResultUtil.fail(baseResp)){
			return baseResp;
		}
		//帐号冻结
		if(userAccountService.isFreezing(userid))
		{
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_113, Constant.RTNINFO_SYS_113);
		}
		return baseResp.initCodeAndDesp();
	}

	/**
	 * 次数限制
	 * @param deviceindex
	 * @param username
	 * @return
	 */
	private boolean canLoginTimesPerDay(String deviceindex,String username){

		String date = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
		Set<String> tels = springJedisDao.members(deviceindex+date+"login");
		if (tels == null || tels.size() < SysRulesCache.behaviorRule.getChangedeveicelimitperday()){
			return true;
		}
		if(tels.size() == SysRulesCache.behaviorRule.getChangedeveicelimitperday() && tels.contains(username)){
			return true;
		}
//		if(tels.contains(username)){
//			return true;
//		}
		return false;
	}

	/**
	 * 切换帐号登录
	 * @param deviceindex
	 * @param userid
	 * @return
	 */
	private BaseResp<Object> deviceIndexChange(String deviceindex,long userid){
		BaseResp<Object> baseResp = new BaseResp<>();

		List<UserInfo> list = userInfoMapper.getOtherDevice(deviceindex);
		if(null == list){
			if(!StringUtils.isBlank(deviceindex)){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_500,Constant.RTNINFO_SYS_500);
			}
			userInfoMapper.updateIndexDevice(userid,deviceindex);
		}else if(list.size() > 1) {
//			userInfoMapper.clearOtherDevice(userid, deviceindex);
//			userInfoMapper.updateIndexDevice(userid, deviceindex);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_500,Constant.RTNINFO_SYS_500);
		}else if(list.isEmpty()){
			if(!StringUtils.isBlank(deviceindex)){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_500,Constant.RTNINFO_SYS_500);
			}
			userInfoMapper.updateIndexDevice(userid, deviceindex);
		}else {
			UserInfo userInfo = list.get(0);
			if(userInfo.getUserid() == userid){
			}else{
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_500,Constant.RTNINFO_SYS_500);
			}
		}
		return baseResp.initCodeAndDesp();
	}


	/**
	 * 登录成功之后次数修改
	 * @param deviceindex
	 * @param username
	 * @return
	 */
	private boolean addLoginRecord(String deviceindex,String username){
		if(StringUtils.isBlank(deviceindex)){
			return false;
		}
		String date = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
		String loginStr = deviceindex+date+"login";
		long n = springJedisDao.sAdd(loginStr,null,username);
		return true;
	}


	@Override
	public BaseResp<UserInfo> login(String username, String password) {
		BaseResp<UserInfo> baseResp = new BaseResp<>();
		BaseResp<Object> baseRespLogin = null;
		try {
			baseRespLogin = iUserBasicService.gettoken(username, password);
		} catch (Exception e) {
			logger.error("pc login is error:",e);
			return baseResp;
		}
		if (ResultUtil.isSuccess(baseRespLogin)){
			try {
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(username);
				UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(appUserMongoEntity.getId()));
				baseResp.initCodeAndDesp(baseRespLogin.getCode(),baseRespLogin.getRtnInfo());
				baseResp.setData(userInfo);
			} catch (Exception e) {
				logger.error("select userinfo by usernam={} is error:",username,e);
			}
		} else {
			baseResp.initCodeAndDesp(baseRespLogin.getCode(),baseRespLogin.getRtnInfo());
		}
		return baseResp;
	}

	/* smkk
         * @see com.longbei.appservice.service.UserService#registerthird(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
         * 2017年1月17日
         * Object data = baseResp.getData();
			JSONObject jsonObject = JSONObject.fromObject(data);
			String token = (String)jsonObject.get("token");
			baseResp.getExpandData().put("token", token);

			long userid = Long.parseLong((String)jsonObject.get("userid")) ;
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
//			if(userInfo.getDeviceindex().equals(deviceindex)){
				//token 放到redis中去
				springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
         *
         */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> registerthird(String username, String password,
			String utype, String openid,String inviteuserid,String deviceindex,
			String devicetype,String randomcode,String avatar,String nickname) {

		BaseResp<Object> baseResp = iUserBasicService.gettoken(username, password);
		UserInfo userInfo = null;
		//手机号未注册
		if(baseResp.getCode() == Constant.STATUS_SYS_04){
			if(StringUtils.hasBlankParams(password)){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_03, Constant.RTNINFO_SYS_03);
			}
			baseResp = checkSms(username,randomcode);
			if(baseResp.getCode()!=Constant.STATUS_SYS_00){
				return baseResp;
			}

			baseResp = registerbasic(username,password,inviteuserid,deviceindex,devicetype,avatar,nickname);
			//登统中心注册失败  直接返回了
			if(baseResp.getCode() != Constant.STATUS_SYS_00){
				return baseResp;
			}
			//注册成功之后 绑定第三方帐号
			Long suserid = (Long) baseResp.getExpandData().get("userid");
			iUserBasicService.bindingThird(openid, utype, username);
			JSONObject jsonObject = JSONObject.fromObject(baseResp.getData());
			String token = (String)jsonObject.get("token");
			springJedisDao.set("userid&token&"+suserid, token);
			//第三方注册获得龙分
			UserInfo userInfo1 = selectJustInfo(suserid);
			thirdregisterGainPoint(userInfo1,utype);
			userInfo = (UserInfo) baseResp.getData();
		}else{//手机号已经注册
			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(username);
			userInfo = userInfoMapper.selectByUserid(Long.parseLong(appUserMongoEntity.getId()));
			BaseResp baseResp1 =  canAbleLogin(deviceindex,userInfo.getUsername(),userInfo.getUserid());
			if(ResultUtil.fail(baseResp1)){
				baseResp1.setData(userInfo);
				return baseResp1;
			}
			baseResp = iUserBasicService.hasbindingThird(openid, utype, username);
//			long uid = (Long)baseResp.getData();
			if(baseResp.getCode() == Constant.STATUS_SYS_11){
				baseResp.setData(null);
				return baseResp;
			}else{
				//验证码是否正确
				baseResp = checkSms(username,randomcode);
//				if(baseResp.getCode()==Constant.STATUS_SYS_00){
//					//免密码登陆
//					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
//					baseResp = iUserBasicService.gettokenWithoutPwd(username);
//					return baseResp;
//				}
				//密码是否正确
//				BaseResp<Object> baseResp2 = iUserBasicService.gettoken(username, password);
				if(ResultUtil.isSuccess(baseResp)){
					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
					baseResp = iUserBasicService.gettokenWithoutPwd(username);
					JSONObject jsonObject = JSONObject.fromObject(baseResp.getExpandData().get("userBasic"));
					baseResp.getExpandData().put("userid", userInfo.getUserid());
					String token = (String) baseResp.getData();
					baseResp.getExpandData().put("token", token);
					baseResp.setData(userInfo);
					iUserBasicService.bindingThird(openid, utype, username);
					springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
					//第三方注册获得龙分
					thirdregisterGainPoint(userInfo,utype);
//					return baseResp;
				}else{//验证码或者密码错误
//					return baseResp;
				}
			}
		}
//		BaseResp baseResp1 =  canAbleLogin(deviceindex,userInfo.getUsername(),userInfo.getUserid());
//		if(ResultUtil.fail(baseResp1)){
//			baseResp1.setData(userInfo);
//			return baseResp1;
//		}
		addLoginRecord(userInfo.getUsername(),deviceindex);
		return baseResp;
	}

	/*
	 * 第三方注册获得龙分
	 */
	private void thirdregisterGainPoint(UserInfo userInfo,String utype) {
		try {
			switch (utype) {
				case "qq":
					userBehaviourService.pointChange(userInfo,"NEW_LOGIN_QQ",Constant_Perfect.PERFECT_GAM,null,0,0);
					break;
				case "wx":
					userBehaviourService.pointChange(userInfo,"NEW_LOGIN_WX",Constant_Perfect.PERFECT_GAM,null,0,0);
					break;
				case "wb":
					userBehaviourService.pointChange(userInfo,"NEW_LOGIN_WB",Constant_Perfect.PERFECT_GAM,null,0,0);
					break;
				default:
					break;
			}
		}catch (Exception e) {
			logger.error("thirdregisterGainPoint error and msg={}",e);
		}

	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#thirdlogin(java.lang.String, java.lang.String)
	 * 2017年1月17日
	 */
	@Override
	public BaseResp<Object> thirdlogin(String utype, String openid,String deviceindex) {
		BaseResp<Object> baseResp = iUserBasicService.thirdlogin(openid,utype);
		if(baseResp.getCode() == Constant.STATUS_SYS_00){
			Object data = baseResp.getData();
			JSONObject jsonObject = JSONObject.fromObject(data);
			String token = (String)jsonObject.get("token");
			baseResp.getExpandData().put("token", token);

			long userid = Long.parseLong((String)jsonObject.get("userid")) ;
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userid);
			baseResp.setData(userInfo);
			//token 放到redis中去
			springJedisDao.set("userid&token&"+userInfo.getUserid(), token);
			BaseResp baseResp1 =  canAbleLogin(deviceindex,userInfo.getUsername(),userInfo.getUserid());
			if(ResultUtil.fail(baseResp1)){
				return baseResp.initCodeAndDesp(baseResp1.getCode(),baseResp1.getRtnInfo());
			}
			String date = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
			springJedisDao.sAdd(deviceindex+date+"login",null,userInfo.getUsername());
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserService#updateUserInfo(com.longbei.appservice.entity.UserInfo)
	 * 2017年1月17日
	 */
	@Override
	public BaseResp<Object> updateUserInfo(UserInfo newUserInfo) {
		BaseResp<Object> baseResp = new BaseResp<>(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		try {
			UserInfo oldUserInfo = userInfoMapper.selectByUserid(newUserInfo.getUserid());
			if(StringUtils.isNotEmpty(newUserInfo.getNickname()) && !newUserInfo.getNickname().equals(oldUserInfo.getNickname())){
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByNickName(newUserInfo.getNickname());
//				UserInfo infos = userInfoMapper.getByNickName(newUserInfo.getNickname());
				if(null != appUserMongoEntity && !appUserMongoEntity.getId().equals(newUserInfo.getUserid())){
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_16, Constant.RTNINFO_SYS_16);
				}
			}
			if(("1".equals(oldUserInfo.getVcertification()) || "2".equals(oldUserInfo.getVcertification())))
			{
				if(StringUtils.isNotEmpty(newUserInfo.getNickname()) || StringUtils.isNotEmpty(newUserInfo.getBrief())){
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_117, Constant.RTNINFO_SYS_117);
				}
			}
			UserInfo updateUserInfo = compareUserInfo(oldUserInfo,newUserInfo);

			int n = userInfoMapper.updateByUseridSelective(updateUserInfo);
			if(n > 0){
				//BeanUtils.copyProperties(userInfo,userInfo1);
				userMongoDao.updateAppUserMongoEntity(updateUserInfo);
				queueMessageSendService.sendAddMessage(Constant.MQACTION_USERRELATION,
						Constant.MQDOMAIN_USER_UPDATE,String.valueOf(updateUserInfo.getUserid()));
				if(StringUtils.isNotEmpty(updateUserInfo.getNickname()) || StringUtils.isNotEmpty(updateUserInfo.getAvatar())){//修改了昵称或者头像
					updateUserRelevantInfo(updateUserInfo.getUserid(),oldUserInfo.getNickname(),updateUserInfo.getNickname(),updateUserInfo.getAvatar());
				}
			}

		} catch (Exception e) {
			logger.error("updateUserInfo error and msg={}",e);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_01, Constant.RTNINFO_SYS_01);
		}
		return baseResp;
	}

	/**
	 * 更改用户的其他相关信息
	 * @param userId
	 * @param oldNickname
	 * @param nickname
	 * @param avatar
     * @return
     */
	private boolean updateUserRelevantInfo(final Long userId, final String oldNickname, final String nickname, final String avatar){
		if(userId == null || (StringUtils.isEmpty(nickname) && StringUtils.isEmpty(avatar))){
			return false;
		}
		threadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				if(StringUtils.isNotEmpty(nickname)){
					//更改群昵称
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("oldNickname",oldNickname);
					map.put("newNickname",nickname);
					map.put("userId",userId);
					int row = groupService.batchUpdateGroupMemberNickName(map);
				}
				if(StringUtils.isNotEmpty(avatar)){
					//更改群昵称
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("userId",userId);
					map.put("avatar",avatar);
					int row = groupService.batchUpdateGroupMemberNickName(map);
				}
			}
		});
		return true;
	}

	private UserInfo compareUserInfo(UserInfo oldUserInfo,UserInfo newUserInfo){
		UserInfo updateUserInfo = new UserInfo();
		updateUserInfo.setUserid(oldUserInfo.getUserid());
		if(StringUtils.isNotEmpty(newUserInfo.getAvatar()) && !newUserInfo.getAvatar().equals(oldUserInfo.getAvatar())){
			updateUserInfo.setAvatar(newUserInfo.getAvatar());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getNickname()) && !newUserInfo.getNickname().equals(oldUserInfo.getNickname())){
			updateUserInfo.setNickname(newUserInfo.getNickname());
		}
		if(StringUtils.isEmpty(newUserInfo.getRealname()) && StringUtils.isNotEmpty(oldUserInfo.getRealname())){
			updateUserInfo.setRealname("");
		}
		if(StringUtils.isNotEmpty(newUserInfo.getRealname()) && !newUserInfo.getRealname().equals(oldUserInfo.getRealname())){
			updateUserInfo.setRealname(newUserInfo.getRealname());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getSex()) && !newUserInfo.getSex().equals(oldUserInfo.getSex())){
			updateUserInfo.setSex(newUserInfo.getSex());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getCity()) && !newUserInfo.getCity().equals(oldUserInfo.getCity())){
			updateUserInfo.setCity(newUserInfo.getCity());
		}
		if((StringUtils.isEmpty(newUserInfo.getBrief()) && StringUtils.isNotEmpty(oldUserInfo.getBrief()))){
			updateUserInfo.setBrief("");
		}
		if((StringUtils.isNotEmpty(newUserInfo.getBrief()) && !newUserInfo.getBrief().equals(oldUserInfo.getBrief()))){
			updateUserInfo.setBrief(newUserInfo.getBrief());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getArea()) && !newUserInfo.getArea().equals(oldUserInfo.getArea())){
			updateUserInfo.setArea(newUserInfo.getArea());
		}
		if(newUserInfo.getBirthday() != null && (oldUserInfo.getBirthday() == null || oldUserInfo.getBirthday().getTime() != newUserInfo.getBirthday().getTime())){
			updateUserInfo.setBirthday(newUserInfo.getBirthday());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getConstellation()) && !newUserInfo.getConstellation().equals(oldUserInfo.getConstellation())){
			updateUserInfo.setConstellation(newUserInfo.getConstellation());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getBlood()) && !newUserInfo.getBlood().equals(oldUserInfo.getBlood())){
			updateUserInfo.setBlood(newUserInfo.getBlood());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getFeeling()) && !newUserInfo.getFeeling().equals(oldUserInfo.getFeeling())){
			updateUserInfo.setFeeling(newUserInfo.getFeeling());
		}
		if(StringUtils.isNotEmpty(newUserInfo.getVcertification()) && !newUserInfo.getVcertification().equals(oldUserInfo.getVcertification())){
			updateUserInfo.setVcertification(newUserInfo.getVcertification());
		}
		return updateUserInfo;
	}

	@Override
	public BaseResp<Object> changePassword(long userid, String pwd, String newpwd) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userid));
			baseResp = iUserBasicService.updatepwd(appUserMongoEntity.getUsername(),pwd,newpwd);
		}catch (Exception e){
			logger.error("changePassword error userid={},pwd={},newpwd={}",userid
			,pwd,newpwd);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> userlevel(long userid,int grade) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			UserLevel userLevel = userLevelMapper.selectByGrade(grade);
			Map<String,Object> map = new HashedMap();
			map.put("userLevel",userLevel);
			int point = 0;
			Map<String,Object> returnmap = getPointInfoPerDay(userid, point);
			List<String> ist = new ArrayList<String>();
			if(!returnmap.isEmpty()){
				ist = (List<String>) returnmap.get("list");
				point = (Integer) returnmap.get("point");
			}
			List<String> levelDetail = getPointInfoLevel(userid, userLevel);
//			String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
//			String point = springJedisDao.getHashValue(Constant.RP_USER_PERDAY+userid+"_TOTAL",dateStr);
			map.put("pointDetail", ist); //得分明细
			map.put("levelDetail", levelDetail);//
			map.put("todayPoint",point);
			map.put("userPoint", userInfo.getPoint());
			map.put("curPoint",userInfo.getCurpoint());
//			map.put("",);
			baseResp.setData(map);
			return baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("selectByGrade error grade={}",grade,e);
		}
		return baseResp;
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectUserlevel(long userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
			Map<String,Object> map = new HashedMap();
			map.put("userLevel",userLevel);
			int point = 0;
			Map<String,Object> returnmap = getPointInfoPerDay(userid, point);
			List<String> ist = new ArrayList<String>();
			if(!returnmap.isEmpty()){
				ist = (List<String>) returnmap.get("list");
				point = (Integer) returnmap.get("point");
			}
			List<String> levelDetail = getPointInfoLevel(userid, userLevel);
//			String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
//			String point = springJedisDao.getHashValue(Constant.RP_USER_PERDAY+userid+"_TOTAL",dateStr);
			map.put("pointDetail", ist);
			map.put("levelDetail", levelDetail);
			map.put("todayPoint", point);
			map.put("userPoint", userInfo.getPoint());
			map.put("levelprivilegeurl", AppserviceConfig.h5_levelprivilege);
			map.put("curPoint",userInfo.getCurpoint());
			baseResp.setData(map);
			return baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("selectUserlevel userid = {}", userid, e);
		}
		return baseResp;
	}

	/**
	* @Title: getPointInfoLevel
	* @Description: 拼接用户特权
	* @param @param userid
	* @param @return    设定文件
	* @return List<String>    返回类型
	 */
	private List<String> getPointInfoLevel(long userid, UserLevel userLevel){
		List<String> list = new ArrayList<>();
		if(null != userLevel){
			list.add("可以同时加入" + userLevel.getJoinranknum() + "个公开龙榜");
			if(userLevel.getDiscount().intValue() != 1){
				DecimalFormat df = new DecimalFormat("#.0");
				String discount = df.format(userLevel.getDiscount()*10);
				list.add("兑换商城中商品打" + discount + "折兑换(特殊商品除外)");
			}
			list.add("可以发布" + userLevel.getPubrankjoinnum() + "人的公开龙榜");
			list.add("可以同时发布" + userLevel.getPubranknum() + "个公开龙榜");
			list.add("可以发布" + userLevel.getPrirankjoinnum() + "人的定制非公开龙榜");
			list.add("可以同时发布" + userLevel.getPriranknum() + "个定制非公开龙榜");
//			list.add("可以同时发布" + userLevel.getClassroomnum() + "个教室");
		}
		return list;
	}

	private Map<String, Object> getPointInfoPerDay(long userid, int point){
		Map<String, Object> returnmap = new HashMap<String, Object>();
		String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
		String key = Constant.RP_USER_PERDAY+"sum"+userid+dateStr;
		List<String> list = new ArrayList<>();
		if(springJedisDao.hasKey(key)){
			Map<String,String> map = springJedisDao.entries(key);
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()){
				String subKey = iterator.next();
				String value = map.get(subKey);
				String operateType = subKey;
				String disStr = "";
				switch (operateType){
					case "DAILY_LIKE":
						disStr = "点赞+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_FLOWER":
						disStr = "送花+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "NEW_REGISTER":
						disStr = "注册成功+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "NEW_LOGIN_QQ":
						disStr = "绑定QQ成功+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "NEW_LOGIN_WX":
						disStr = "绑定微信成功+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "NEW_LOGIN_WB":
						disStr = "绑定微博成功+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "NEW_CERTIFY_USERCARD":
						disStr = "完成实名认证+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "NEW_USERINFO":
						disStr = "完善个人信息+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_CHECKIN":
						disStr = "签到成功+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_SHARE":
						disStr = "站内分享+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_SHARE_OUT":
						disStr = "站外分享+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "INVITE_LEVEL1":
						disStr = "邀请好友注册+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_ADDFRIEND":
						disStr = "添加好友+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_FUN":
						disStr = "关注他人+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_COMMENT":
						disStr = "与他人评论互动+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_ADDIMP":
						disStr = "发微进步+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_ADDRANK":
						disStr = "加入龙榜+"+value+"分";
						point += Integer.parseInt(value);
						break;
					case "DAILY_ADDCLASSROOM":
						disStr = "加入教室+"+value+"分";
						point += Integer.parseInt(value);
						break;
					default:
						disStr = operateType+value+"分";
						point += Integer.parseInt(value);
						break;
				}
				list.add(disStr);
			}
		}
		returnmap.put("list", list);
		returnmap.put("point", point);
		return returnmap;
	}


//			● 关注他人+XX分，上限+20分/天；
//			● 点赞鼓励他人++XX分，上限+30分/天；
//			● 与他人评论互动+XX分，上限+40分/天；
//			● 发微进步+XX分，上限50分/天；
//			● 加入龙榜+7分；
//			● 加入教室+7分；
//			● 加入圈子+7分；
//			● 公益抽奖+XX分；
	public List<String> getPointOriginate(){
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
//		list.add("关注他人+"+Constant_point.DAILY_FUN+"分"+",上限+"+Constant_point.DAILY_FUN_LIMIT+"分／天");
		list.add("与他人评论互动+"+Constant_point.DAILY_COMMENT+"分"+",上限+"+Constant_point.DAILY_COMMENT_LIMIT+"分／天");
		list.add("发微进步+"+Constant_point.DAILY_ADDIMP+"分"+",上限+"+Constant_point.DAILY_ADDIMP_LIMIT+"分／天");
		list.add("加入龙榜+"+ Constant_point.DAILY_ADDRANK+"分");
		list.add("加入教室+"+ Constant_point.DAILY_ADDCLASSROOM+"分");
		list.add("送花+"+ Constant_point.DAILY_FLOWER+"分");
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
	public BaseResp<Object> selectRandomTagList(String userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		String[] ptypes = null;
		try{
			List<SysPerfectTag> list = sysPerfectTagMapper.selectAll();
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
	public BaseResp<Object> updateNickName(final String userid, String nickname,final String invitecode,String sex,String pl) {
		BaseResp<Object> baseResp = new BaseResp<>();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserid(Long.parseLong(userid));
//		userInfo.setNickname(nickname);
		userInfo.setSex(sex);
		userInfo.setHcnickname("1");
		try {
			//判断邀请人是否是龙杯用户
			if(!StringUtils.hasBlankParams(invitecode)){
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(invitecode);
				final UserInfo info = userInfoMapper.selectByUserid(Long.parseLong(appUserMongoEntity.getId()));
				if(null != info){
					//是龙杯用户,送分.....
					//建立好友关系
					BaseResp<Object> insertFriendBaseResp=userRelationService.insertFriend(Long.parseLong(userid),info.getUserid());
					if(insertFriendBaseResp.getCode() == 0) {
						final AppUserMongoEntity newUser = new AppUserMongoEntity();
						newUser.setUserid(userInfo.getUserid() + "");
						newUser.setNickname(userInfo.getNickname());
						newUser.setUsername(userInfo.getUsername());
						newUser.setRemark(userInfo.getRemark());
						newUser.setAvatar(userInfo.getAvatar());
						newUser.setSex(userInfo.getSex());
						threadPoolTaskExecutor.execute(new Runnable() {
							@Override
							public void run() {
								//JPUSH通知用户
								JSONObject pushMessage = new JSONObject();
								pushMessage.put("status", "消息标识");
								pushMessage.put("userid", info.getUserid());
								pushMessage.put("content", "加好友通知");
								pushMessage.put("msgid", userid);
								pushMessage.put("tag", Constant.JPUSH_TAG_COUNT_1004);
								pushMessage.put("userInfo", newUser);
								ijPushService.messagePush(info.getUserid()+"", "加好友成功通知", "加好友成功通知", pushMessage.toString());
							}
						});
					}
					//邀请好友获得龙分龙币 给推荐人添加龙分 给推荐人添加龙币
					userBehaviourService.pointChange(info,"INVITE_LEVEL1",Constant_Perfect.PERFECT_GAM,null,0,0);
					//给推荐人添加龙币
					userInfo.setInvitecode(inviteFriendIds(info));
					userInfo.setHandleinvite("0");
				}else{
					return baseResp.initCodeAndDesp(Constant.STATUS_SYS_15, Constant.RTNINFO_SYS_15);
				}
			}
			int temp = userInfoMapper.updateByUseridSelective(userInfo);

			if(temp > 0){
				if (SysRulesCache.behaviorRule.getRegistercoins() > 0){
					userImpCoinDetailService.insertPublic(Long.parseLong(userid),"14",
							SysRulesCache.behaviorRule.getRegistercoins(),0,null);
				}
				//更新信息到mongodb
				UserInfo userInfo1 = userInfoMapper.selectByUserid(Long.parseLong(userid));
				BeanUtils.copyProperties(userInfo,userInfo1);
				userMongoDao.updateAppUserMongoEntity(userInfo1);
				queueMessageSendService.sendAddMessage(Constant.MQACTION_USERRELATION,
						Constant.MQDOMAIN_USER_UPDATE,userid);
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByUseridSelective error and msg={}",e);
		}
		return baseResp;
	}



	private String inviteFriendIds(UserInfo userInfo){
		String inviteUserIds = "";
		Long userid = userInfo.getUserid();
		inviteUserIds += userid;
		String ids = userInfo.getInvitecode();
		if (!StringUtils.isBlank(ids)){
			inviteUserIds += "," + ids;
		}
		String []inviteIdsArr = inviteUserIds.split(",");
		if (inviteIdsArr.length > 5){
			inviteUserIds = inviteIdsArr[0] + "," +
							inviteIdsArr[1] + "," +
							inviteIdsArr[2] + "," +
							inviteIdsArr[3] + "," +
							inviteIdsArr[4];
		}
		return inviteUserIds;
	}



	@Override
	public BaseResp<Page<UserInfo>> selectUserList(UserInfo userInfo,String validateidcard, String order, String ordersc, Integer pageno, Integer pagesize) {
		BaseResp<Page<UserInfo>> baseResp = new BaseResp<>();
		Page<UserInfo> page = new Page<>(pageno,pagesize);
		try {
			int totalcount = userInfoMapper.selectCount(userInfo,validateidcard);
			Integer startno = null;
			if (null != pageno && -1 != pageno){
				startno = pagesize*(pageno-1);
			}
			List<UserInfo> userInfos = userInfoMapper.selectList(userInfo,validateidcard,order,ordersc,startno,pagesize);
			for(int i=0;i<userInfos.size();i++){
				BaseResp<UserAccount> baseResp1 = userAccountService.selectUserAccountByUserId(userInfos.get(i).getUserid());
				UserAccount userAccount = baseResp1.getData();
				if(null!= userAccount ) {
					if (!userAccountService.isFreezing(userInfos.get(i).getUserid()))
					{
						userAccount.setStatus("0");
						userAccountMapper.updateUserAccountByUserId(userAccount);
						userInfos.get(i).setFreezestatus("0");
					}else{
						userInfos.get(i).setFreezestatus(userAccount.getStatus());
					}
				}
			}
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
		if(StringUtils.isBlank(userInfo.getBrief()) && StringUtils.isBlank(userInfo.getVcertification()) && StringUtils.isNotEmpty(userInfo.getNickname())){
			//官方认证，检查认证用户名是否重复
			UserInfo userInfo1= userInfoMapper.selectByUserid(userInfo.getUserid());
			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByNickName(userInfo.getNickname());
			if(null != appUserMongoEntity && !appUserMongoEntity.getNickname().equals(userInfo1.getNickname())){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_16, Constant.RTNINFO_SYS_16);
			}else {
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		}
		try {
			if ("0".equals(userInfo.getIsfashionman())){
				userInfo.setDownfashionmantime(new Date());
				userInfo.setSortno(0);
			}
			if ("1".equals(userInfo.getIsfashionman())){
				userInfo.setUpfashionmantime(new Date());
			}
			int res = userInfoMapper.updateByUseridSelective(userInfo);
			if (res > 0){
				if ("1".equals(userInfo.getIsfashionman())){
					String remark = "你被选为达人";
					userMsgService.insertMsg(Constant.SQUARE_USER_ID,String.valueOf(userInfo.getUserid()),null,"9",null,
							remark,"0","30", "选为达人",0, "", "");
					this.jPushService.pushMessage("消息标识",userInfo.getUserid()+"","设置为达人",
							"恭喜，您被选为龙杯达人！","",Constant.JPUSH_TAG_COUNT_1304);
				}
				if ("0".equals(userInfo.getIsfashionman())){
					String remark = "你被取消达人";
					userMsgService.insertMsg(Constant.SQUARE_USER_ID,String.valueOf(userInfo.getUserid()),null,"9",null,
							remark,"0","30", "取消达人",0, "", "");
				}
				if(StringUtils.isNotBlank(userInfo.getVcertification()) ) {
					   UserInfo userInfo2= userInfoMapper.selectByUserid(userInfo.getUserid());
						//同步更改的用户信息
						UserInfo updateUserInfo = compareUserInfo(userInfo2, userInfo);
						userMongoDao.updateAppUserMongoEntity(updateUserInfo);
						queueMessageSendService.sendAddMessage(Constant.MQACTION_USERRELATION,
								Constant.MQDOMAIN_USER_UPDATE, String.valueOf(updateUserInfo.getUserid()));
						if (StringUtils.isNotEmpty(updateUserInfo.getNickname()) || StringUtils.isNotEmpty(updateUserInfo.getAvatar())) {
							updateUserRelevantInfo(updateUserInfo.getUserid(), userInfo2.getNickname(), updateUserInfo.getNickname(), updateUserInfo.getAvatar());
						}
					if ("1".equals(userInfo.getVcertification())) {
						String remark = "你被授予龙杯名人认证";
						userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(userInfo.getUserid()), null, "9", null,
								remark, "0", "53", "授予龙杯名人认证", 0, "", "");
						this.jPushService.pushMessage("消息标识", userInfo.getUserid() + "", "授予龙杯名人认证",
								"恭喜，您被授予龙杯名人认证！", "", Constant.JPUSH_TAG_COUNT_1304);
					}
					if ("2".equals(userInfo.getVcertification())) {
						String remark = "你被授予龙杯Star认证";
						userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(userInfo.getUserid()), null, "9", null,
								remark, "0", "56", "授予龙杯Star认证", 0, "", "");
						this.jPushService.pushMessage("消息标识", userInfo.getUserid() + "", "授予龙杯Star认证",
								"恭喜，您被授予龙杯Star认证！", "", Constant.JPUSH_TAG_COUNT_1305);
					}
					if ("0".equals(userInfo.getVcertification())) {
						if ("1".equals(userInfo2.getVcertification())) {
							String remark = "你被取消龙杯名人认证";
							userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(userInfo.getUserid()), null, "9", null,
									remark, "0", "53", "取消龙杯名人认证", 0, "", "");
						}
						if ("2".equals(userInfo2.getVcertification())) {
							String remark = "你被取消龙杯Star认证";
							userMsgService.insertMsg(Constant.SQUARE_USER_ID, String.valueOf(userInfo.getUserid()), null, "9", null,
									remark, "0", "56", "取消龙杯Star认证", 0, "", "");
						}
					}
				}
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

	@Override
	public BaseResp<Object> updateBg(long userid, String bg) {
		BaseResp<Object> baseResp = new BaseResp<>();
		int n = userInfoMapper.updateBg(userid,bg);
		if(n == 1){
			baseResp.initCodeAndDesp();
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> thirdbinding(String userid, String utype, String opendid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = selectJustInfo(Long.parseLong(userid));
			baseResp = iUserBasicService.bindingThird(opendid,utype,userInfo.getUsername());
			if (ResultUtil.isSuccess(baseResp)){
				//第三方绑定获得龙分
				thirdregisterGainPoint(userInfo,utype);
			}else{
				baseResp.setData(null);
			}
		}catch (Exception e){
			logger.error("thirdbinding error userid={},utype={},opendid={}",userid,utype,opendid);
		}
		return baseResp;
	}

	/*
	 * @param userid 消息推送者id
     * @param friendid 消息接受者id
     * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
	 *									22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *									25:订单发货N天后自动确认收货    26：实名认证审核结果
	 *									27:工作认证审核结果      28：学历认证审核结果
	 *									29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *									32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙杯名人认证 54: 教室成员退出教室 55：转让群主权限 56：被授予龙杯Star认证
	 *									40：订单已取消 41 榜中进步下榜)
	 *				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)
	 *				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
	 *						14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 *
	 */
	@Override
	public BaseResp<Object> sendMessagesBatch(String friendid, String[] userids, String businesstype,
										String businessid, String remark,String title) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			baseResp = userMsgService.sendMessagesBatch(friendid,userids,businesstype,businessid,remark,title);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseResp;
	}

	/**
	 * 用户注册初始化用户显示菜单
	 */
	private void initUserCommonMenuInfo(Long userid){
//		List<UserSettingMenu> list = userSettingMenuMapper.selectDefaultMenu();
	}

	@Override
	public BaseResp updateTotalmoneyByUserid(long userid, Integer totalPrice){
		BaseResp baseResp = new BaseResp();
		try {
//			int res = userInfoMapper.updateTotalmoneyByUserid(userid,totalmoney);
//			if(res>0){
				if(null != totalPrice && totalPrice != 0){
					//添加一条消耗龙币的记录
					userMoneyDetailService.insertPublic(userid, "3", totalPrice, 0);
				}
				baseResp.initCodeAndDesp();
//			}
		} catch (Exception e) {
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,Constant.RTNINFO_SYS_01);
			logger.error("update Totalmoney By Userid {} is error",userid,e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> afterShareSuccess(Long userid,String sharePlatform){
		BaseResp<Object> baseResp = new BaseResp<>();
		try{

			UserInfo userInfo = selectJustInfo(userid);
			if("0".equals(sharePlatform) || "1".equals(sharePlatform)) {
				   //站内分享成功获得龙分
				userBehaviourService.pointChange(userInfo,"DAILY_SHARE", Constant_Perfect.PERFECT_GAM,null, 0, 0);
			}else {//站外分享成功获得龙分龙币
				userBehaviourService.pointChange(userInfo,"DAILY_SHARE_OUT",Constant_Perfect.PERFECT_GAM,"2",0,0);
			}
			baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("afterShareSuccess and userid={},sharePlatform={}", userid,sharePlatform,e);
		}
		return baseResp;
	}

	@Override
	public BaseResp selectInviteCoinsDetail(String userid) {
		BaseResp baseResp = new BaseResp();
		Map<String,Object> map = new HashedMap();
		map.put("improvenum",SysRulesCache.behaviorRule.getInviteimprovenum());
		map.put("invitecoin",SysRulesCache.behaviorRule.getFriendregisterimpcoins());
		map.put("maxlevel",5);
		map.put("inviteawardinfo",createInviteAwardInfo());
		map.put("inviteurl",ShortUrlUtils.getShortUrl("http://www.baidu.com"));
		map.put("invitetitle","我正在玩“龙杯”，推荐给你!");
		map.put("invitecontent","总是设立美好的目标，但也总是光说不练，那么神仙也帮不了你！来龙杯，我们一起进步！还有海量进步币等你来拿。");
		map.put("inviteruleurl","http://www.baidu.com");
		//判断邀请所获收益是否显示红点    0:不显示   1：显示
		MsgRed msgRed = msgRedMongDao.getMsgRed(String.valueOf(userid),"0","62");
		if (null != msgRed){
			map.put("invitecoinnew",msgRed.getRemark());
		} else {
			map.put("invitecoinnew",0);
		}
		UserInfo userInfo = null;
		try {
			userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
			map.put("userinfo",userInfo);
			msgRedMongDao.deleteMsgRed(userid,"0","62");
		} catch (Exception e) {
			e.printStackTrace();
		}
		baseResp.initCodeAndDesp();
		baseResp.setData(map);
		return baseResp;
	}

	@Override
	public BaseResp insertInviteCode(String userid, String invitecode) {
		BaseResp baseResp = new BaseResp();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserid(Long.parseLong(userid));
		try {
			AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUserByUserName(invitecode);
			if (null != appUserMongoEntity){
				UserInfo info = userInfoMapper.selectByUserid(Long.parseLong(userid));
				userInfo.setInvitecode(String.valueOf(appUserMongoEntity.getUserid()));
				userInfo.setHandleinvite("0");
				userInfoMapper.updateByUseridSelective(userInfo);
//				if (null != info && info.getTotalimp() >= SysRulesCache.behaviorRule.getInviteimprovenum()){
//					info.setInvitecode(String.valueOf(appUserMongoEntity.getUserid()));
//					info.setHandleinvite("0");
//					improveService.inviteCoinsHandle(info);
//				}
				baseResp.initCodeAndDesp();
			}
		} catch (Exception e) {
			logger.error("insert invite code userid:{} invitecode:{} is error:",userid,invitecode,e);
		}
		return baseResp;
	}

	private List<String> createInviteAwardInfo(){
		List<String> list = new ArrayList<>();
		for (int i = 1 ; i <= 5 ; i++ ){
			String str = String.valueOf(getImproveCoin(i));
			list.add(str);
		}
		return list;
	}


	private int getImproveCoin(int level){
		int icoinnum = 0;
		switch (level){
			case 1 :
				icoinnum = Constant_Imp_Icon.INVITE_LEVEL1;
				break;
			case 2 :
				icoinnum = Constant_Imp_Icon.INVITE_LEVEL2;
				break;
			case 3 :
				icoinnum = Constant_Imp_Icon.INVITE_LEVEL3;
				break;
			case 4 :
				icoinnum = Constant_Imp_Icon.INVITE_LEVEL4;
				break;
			case 5 :
				icoinnum = Constant_Imp_Icon.INVITE_LEVEL5;
				break;
			default:
				break;
		}
		return icoinnum;
	}


	public String getRandomNickName() {
		SysNicknames sysNicknames = sysNicknamesMapper.getRankNicknames();
		return sysNicknames.getNickname();
	}


}
