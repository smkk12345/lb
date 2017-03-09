package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserSchoolMapper;
import com.longbei.appservice.entity.UserSchool;
import com.longbei.appservice.service.UserSchoolService;

@Service("userSchoolService")
public class UserSchoolServiceImpl implements UserSchoolService {

	@Autowired
	private UserSchoolMapper userSchoolMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserSchoolServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertSchool(long userid,String schoolname,String Department,Date starttime,Date endtime) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserSchool data = new UserSchool();
		data.setUserid(userid);
		data.setSchoolname(schoolname);
		data.setDepartment(Department);
		data.setStarttime(starttime);
		data.setEndtime(endtime);		
		Date date = new Date();
		data.setCreatetime(date);
		data.setUpdatetime(date);
		try {
			int nums = userSchoolMapper.selectCountSchool(userid);
			if(nums<5){
				int m = userSchoolMapper.insertSchool(data);
				if(m == 1){
					baseResp.setData(data);
					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}else{
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_39, Constant.RTNINFO_SYS_39);
			}
		} catch (Exception e) {
			logger.error("insertSchool error and msg={}",e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<Object> deleteSchool(int id,long userid) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			int m = userSchoolMapper.deleteSchool(id,userid);
			if(m == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			 }
		} catch (Exception e) {
			logger.error("deleteSchool error and msg={}",e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<Object> selectSchoolById(int id) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			UserSchool userSchool = userSchoolMapper.selectSchoolById(id);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(userSchool);	
		} catch (Exception e) {
			logger.error("selectSchoolById error and msg={}",e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<Object> selectSchoolList(long userid,int startNum,int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			List<UserSchool> list = userSchoolMapper.selectSchoolList(userid,startNum,pageSize);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);	
		} catch (Exception e) {
			logger.error("selectSchoolList error and msg={}",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateSchool(int id,String schoolname,String Department,Date starttime,Date endtime) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserSchool data = new UserSchool();
		data.setId(id);
		data.setSchoolname(schoolname);
		data.setDepartment(Department);
		data.setStarttime(starttime);
		data.setEndtime(endtime);		
		Date date = new Date();
		data.setUpdatetime(date);
		try {
			int n = userSchoolMapper.updateSchool(data);
			if(n == 1){
				baseResp.setData(data);
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateSchool error and msg={}",e);
		}
		return baseResp;
	}
	
}
