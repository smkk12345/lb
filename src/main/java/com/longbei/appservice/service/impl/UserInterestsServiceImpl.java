package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserInterestsMapper;
import com.longbei.appservice.entity.UserInterests;
import com.longbei.appservice.service.UserInterestsService;

@Service("userInterestsService")
public class UserInterestsServiceImpl implements UserInterestsService {

	@Autowired
	private UserInterestsMapper userInterestsMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserInterestsServiceImpl.class);
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateInterests(String userid,String ids) {
		BaseResp<Object> baseResp = new BaseResp<Object>();

		UserInterests data = new UserInterests();
		data.setUserid(userid);
		data.setPtype(ids);
		Date date = new Date();
		data.setUpdatetime(date);
		try {
			int n = userInterestsMapper.updateInterests(data);
			if(n == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateInterests error and msg={}",e);
		}
		return baseResp;
	}


}
