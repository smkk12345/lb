package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.dao.UserInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserFeedbackMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserFeedback;
import com.longbei.appservice.service.UserFeedbackService;

import java.util.Date;
import java.util.List;

@Service("userFeedbackService")
public class UserFeedbackServiceImpl implements UserFeedbackService {

	@Autowired
	private UserFeedbackMapper userFeedbackMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserFeedbackServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertSelective(UserFeedback record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			String username = initUserInfoByFriendid(record.getUserid());
			record.setUsername(username);
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
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
//				UserInfo userInfo = userInfoMapper.selectByUserid(noFeedback.getUserid());
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(feedback.getUserid()));
				feedback.setAppUserMongoEntity(appUserMongoEntity);
//				noFeedback.setUserInfo(userInfo);
			}
			page.setTotalCount(totalCount);
			page.setList(feedbackList);
			return page;
		} catch (Exception e) {
			logger.error("select feedback list with page is error:{}",e);
		}
		return page;
	}

//	@Override
//	public Page<UserFeedback> selectHasFeedbackListWithPage(UserFeedback userFeedback, int pageno, int pagesize) {
//		Page<UserFeedback> page = new Page<>(pageno,pagesize);
//		try {
//			int totalCount = userFeedbackMapper.selectHasFeedbackCount(userFeedback);
//			List<UserFeedback> hasFeedbackList = userFeedbackMapper.selectHasFeedbackList(userFeedback,pagesize*(pageno-1),pagesize);
//			for (UserFeedback hasFeedback:hasFeedbackList) {
//				UserInfo userInfo = userInfoMapper.selectByUserid(hasFeedback.getUserid());
//				hasFeedback.setUserInfo(userInfo);
//			}
//			page.setTotalCount(totalCount);
//			page.setList(hasFeedbackList);
//			return page;
//		} catch (Exception e) {
//			logger.error("select hasFeedback list with page is error:{}",e);
//		}
//		return page;
//	}

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
				return true;
			}
		} catch (Exception e) {
			logger.error("update userFeedback is error:{}",e);
		}
		return false;
	}

	@Override
	public UserFeedback selectUserFeedback(String id) {
		UserFeedback userFeedback = null;
		try {
			userFeedback = userFeedbackMapper.selectByPrimaryKey(Long.parseLong(id));
		} catch (NumberFormatException e) {
			logger.error("select userFeedback id={} is error:{}",id,e);
		}
		return userFeedback;
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

}
