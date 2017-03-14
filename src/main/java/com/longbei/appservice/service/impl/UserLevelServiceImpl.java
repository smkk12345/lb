package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.UserLevelService;

@Service("userLevelService")
public class UserLevelServiceImpl implements UserLevelService {
	
	@Autowired
	private UserLevelMapper userLevelMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserLevelServiceImpl.class);

	@Override
	public BaseResp<UserLevel> selectByUserid(long userid) {
		BaseResp<UserLevel> baseResp = new BaseResp<UserLevel>();
		try {
			UserLevel userLevel = null;
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				baseResp.setData(userLevel);
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("selectByUserid userid = {}", userid, e);
		}
		return baseResp;
	}

}
