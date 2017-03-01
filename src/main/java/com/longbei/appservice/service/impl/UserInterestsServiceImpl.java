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
	public BaseResp<Object> updateInterests(int id,String ptype,String perfectname) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserInterests data = new UserInterests();
		data.setId(id);
		data.setPtype(ptype);
		data.setPerfectname(perfectname);
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

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertInterests(String userid,String ptype,String perfectname) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserInterests data = new UserInterests();
		data.setUserid(userid);
		data.setPtype(ptype);
		data.setPerfectname(perfectname);
		Date date = new Date();
		data.setCreatetime(date);
		data.setUpdatetime(date);
		try {
			int n = userInterestsMapper.insertInterests(data);
			if(n == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateInterests error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> deleteInterests(int id,String userid) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			int m = userInterestsMapper.deleteInterests(id,userid);
			if(m == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteSchool error and msg={}",e);
		}
		return baseResp;
	}
}
