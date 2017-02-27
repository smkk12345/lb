package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserJobMapper;
import com.longbei.appservice.entity.UserJob;
import com.longbei.appservice.entity.UserSchool;
import com.longbei.appservice.service.UserJobService;

@Service("userJobService")
public class UserJobServiceImpl implements UserJobService {

	@Autowired
	private UserJobMapper userJobMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserJobServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertJob(long userid,String companyname,String department,String location,Date starttime,Date endtime) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserJob data = new UserJob();
		data.setUserid(userid);
		data.setCompanyname(companyname);
		data.setDepartment(department);
		data.setLocation(location);		
		data.setStarttime(starttime);
		data.setEndtime(endtime);		
		Date date = new Date();
		data.setCreatetime(date);
		data.setUpdatetime(date);
		try {
			int m = userJobMapper.insertJob(data);
			if(m == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			 }
		} catch (Exception e) {
			logger.error("insertJob error and msg={}",e);
		}
		return baseResp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> deleteJob(int id,long userid) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			int m = userJobMapper.deleteJob(id,userid);
			if(m == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			 }
		} catch (Exception e) {
			logger.error("deleteJob error and msg={}",e);
		}
		return baseResp;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectJobById(int id) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			UserJob userJob = userJobMapper.selectJobById(id);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(userJob);	
		} catch (Exception e) {
			logger.error("selectJobById error and msg={}",e);
		}
		return baseResp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectJobList(long userid,int startNum,int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			List<UserJob> list = userJobMapper.selectJobList(userid,startNum,pageSize);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);	
		} catch (Exception e) {
			logger.error("selectJobList error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateJob(int id,String companyname,String department,String location,Date starttime,Date endtime) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserJob data = new UserJob();
		data.setId(id);
		data.setCompanyname(companyname);
		data.setDepartment(department);
		data.setLocation(location);		
		data.setStarttime(starttime);
		data.setEndtime(endtime);		
		Date date = new Date();
		data.setUpdatetime(date);
		try {
			int n = userJobMapper.updateJob(data);
			if(n == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateJob error and msg={}",e);
		}
		return baseResp;
	}
	
}
