package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserFeedbackMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserFeedback;
import com.longbei.appservice.service.UserFeedbackService;
import com.longbei.appservice.service.UserMsgService;
import com.longbei.appservice.service.UserRelationService;

import java.util.Date;
import java.util.List;

@Service("userFeedbackService")
public class UserFeedbackServiceImpl implements UserFeedbackService {

	@Autowired
	private UserFeedbackMapper userFeedbackMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private UserRelationService userRelationService;
	
	private static Logger logger = LoggerFactory.getLogger(UserFeedbackServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertSelective(UserFeedback record) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			String username = initUserInfoByFriendid(record.getUserid());
			record.setUsername(username);
			boolean temp = insert(record);
			if (temp) {
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return baseResp;
	}

	private boolean insert(UserFeedback record) {
		int temp = userFeedbackMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public Page<UserFeedback> selectFeedbackListWithPage(UserFeedback userFeedback, int pageno, int pagesize) {
		Page<UserFeedback> page = new Page<>(pageno,pagesize);
		try {
			int totalCount = userFeedbackMapper.selectFeedbackCount(userFeedback);
			List<UserFeedback> feedbackList = userFeedbackMapper.selectFeedbackList(userFeedback,pagesize*(pageno-1),pagesize);
			for (UserFeedback feedback:feedbackList) {
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(feedback.getUserid()));
				feedback.setAppUserMongoEntity(appUserMongoEntity);
			}
			page.setTotalCount(totalCount);
			page.setList(feedbackList);
			return page;
		} catch (Exception e) {
			logger.error("select feedback list with page is error:{}",e);
		}
		return page;
	}

	@Override
	public BaseResp<Object> selectFeedbackListNum(UserFeedback userFeedback){
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			int totalcount = userFeedbackMapper.selectFeedbackCount(userFeedback);
			baseResp.setData(totalcount);
			baseResp.initCodeAndDesp();
		} catch (Exception e) {
			logger.error("selectFeedbackListNum for adminservice and userFeedback ={}", JSON.toJSONString(userFeedback), e);
		}
		return  baseResp;
	}

	@Override
	public boolean updateFeedback(UserFeedback userFeedback) {
		if(StringUtils.isBlank(userFeedback.getCheckoption())){
			userFeedback.setCheckoption("忽略");
			userFeedback.setStatus("1");
		}else{
			userFeedback.setStatus("2");
		}
		try {
			userFeedback.setDealtime(new Date());
			int res = userFeedbackMapper.updateByPrimaryKeySelective(userFeedback);
			if (res > 0){
				userFeedback = userFeedbackMapper.selectByPrimaryKey(userFeedback.getId());
				if("2".equals(userFeedback.getStatus())){
					//后台回复反馈信息   添加通知消息
					userMsgService.insertMsg(Constant.SQUARE_USER_ID, 
							userFeedback.getUserid().toString(), "", "14", userFeedback.getId().toString(), 
							userFeedback.getCheckoption(), "0", "43", "用户反馈被回复", 0, "", "");
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("update userFeedback is error:{}",e);
		}
		return false;
	}

	@Override
	public BaseResp<UserFeedback> selectUserFeedback(String id, long userid) {
		BaseResp<UserFeedback> baseResp = new BaseResp<>();
		UserFeedback userFeedback = null;
		try {
			userFeedback = userFeedbackMapper.selectByPrimaryKey(Long.parseLong(id));
			initMsgUserInfoByFriendid(userFeedback, userid);
			AppUserMongoEntity appUserMongoEntitys = new AppUserMongoEntity();
			appUserMongoEntitys.setId(Constant.SQUARE_USER_ID);
			appUserMongoEntitys.setUserid(Constant.SQUARE_USER_ID);
			appUserMongoEntitys.setNickname(Constant.MSG_LONGBEI_NICKNAME);
			appUserMongoEntitys.setAvatar(Constant.MSG_LONGBEI_DIFAULT_AVATAR);
			userFeedback.setAppUserMongoEntityLongbei(appUserMongoEntitys);
			baseResp.setData(userFeedback);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectUserFeedback id={}, userid = {}", id, userid, e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<UserFeedback> selectUserFeedbackById(String id) {
		BaseResp<UserFeedback> baseResp = new BaseResp<>();
		UserFeedback userFeedback = null;
		try {
			userFeedback = userFeedbackMapper.selectByPrimaryKey(Long.parseLong(id));
			baseResp.setData(userFeedback);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectUserFeedbackById id={}", id, e);
		}
		return baseResp;
	}

	
	//------------------------公用方法，初始化用户信息------------------------------------------
	/**
     * 初始化用户信息 ------Friendid
     */
    private String initUserInfoByFriendid(long friendid){
		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(friendid));
		if(null != appUserMongoEntity){
			return appUserMongoEntity.getUsername();
		}
        return "";
    }
    
  //------------------------公用方法，初始化消息中用户信息------------------------------------------
  	/**
   * 初始化消息中用户信息 ------Friendid
   */
  private void initMsgUserInfoByFriendid(UserFeedback userFeedback, long userid){
  	if(!StringUtils.hasBlankParams(userFeedback.getUserid().toString())){
  		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userFeedback.getUserid()));
		if(null != appUserMongoEntity){
			this.userRelationService.updateFriendRemark(userid,appUserMongoEntity);
			userFeedback.setAppUserMongoEntity(appUserMongoEntity);
		}else{
			userFeedback.setAppUserMongoEntity(new AppUserMongoEntity());
		}
  	}
  }

}
