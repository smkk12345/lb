package com.longbei.appservice.service.impl;

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

@Service("userFeedbackService")
public class UserFeedbackServiceImpl implements UserFeedbackService {

	@Autowired
	private UserFeedbackMapper userFeedbackMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	
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
