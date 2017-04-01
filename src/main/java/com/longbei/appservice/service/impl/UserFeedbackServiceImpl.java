package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserFeedbackMapper;
import com.longbei.appservice.entity.UserFeedback;
import com.longbei.appservice.service.UserFeedbackService;

import java.util.List;

@Service("userFeedbackService")
public class UserFeedbackServiceImpl implements UserFeedbackService {

	@Autowired
	private UserFeedbackMapper userFeedbackMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserFeedbackServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertSelective(UserFeedback record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record={},msg={}",record,e);
		}
		return reseResp;
	}

	private boolean insert(UserFeedback record) {
		int temp = userFeedbackMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public Page<UserFeedback> selectNoFeedbackListWithPage(UserFeedback userFeedback, int pageno, int pagesize) {
		Page<UserFeedback> page = new Page<>(pageno,pagesize);
		try {
			int totalCount = userFeedbackMapper.selectNoFeedbackCount(userFeedback);
			List<UserFeedback> noFeedbackList = userFeedbackMapper.selectNoFeedbackList(userFeedback,pagesize*(pageno-1),pagesize);
			//给每一条反馈userFeedback附加用户昵称和手机号
			for (UserFeedback noFeedback:noFeedbackList) {
				UserInfo userInfo = userInfoMapper.selectByUserid(noFeedback.getUserid());
				noFeedback.setUserInfo(userInfo);
			}
			page.setTotalCount(totalCount);
			page.setList(noFeedbackList);
			return page;
		} catch (Exception e) {
			logger.error("select noFeedback list with page is error:{}",e);
		}
		return page;
	}

	@Override
	public Page<UserFeedback> selectHasFeedbackListWithPage(UserFeedback userFeedback, int pageno, int pagesize) {
		Page<UserFeedback> page = new Page<>(pageno,pagesize);
		try {
			int totalCount = userFeedbackMapper.selectHasFeedbackCount(userFeedback);
			List<UserFeedback> hasFeedbackList = userFeedbackMapper.selectHasFeedbackList(userFeedback,pagesize*(pageno-1),pagesize);
			//给每一条反馈userFeedback附加用户昵称和手机号
			for (UserFeedback hasFeedback:hasFeedbackList) {
				UserInfo userInfo = userInfoMapper.selectByUserid(hasFeedback.getUserid());
				hasFeedback.setUserInfo(userInfo);
			}
			page.setTotalCount(totalCount);
			page.setList(hasFeedbackList);
			return page;
		} catch (Exception e) {
			logger.error("select hasFeedback list with page is error:{}",e);
		}
		return page;
	}

	@Override
	public boolean updateFeedback(UserFeedback userFeedback) {
		try {
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
	public UserFeedback selectUserFeedback(String userid) {
		UserFeedback userFeedback = null;
		try {
			userFeedback = userFeedbackMapper.selectByPrimaryKey(Long.parseLong(userid));
		} catch (NumberFormatException e) {
			logger.error("select userFeedback userid={} is error:{}",userid,e);
		}
		return userFeedback;
	}

}
