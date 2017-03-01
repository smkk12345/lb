package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserCertifyMapper;
import com.longbei.appservice.entity.UserCertify;
import com.longbei.appservice.service.UserCertifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("userCertifyService")
public class UserCertifyServiceImpl implements UserCertifyService {

	@Autowired
	private UserCertifyMapper userCertifyMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserCertifyServiceImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertCertify(long userid,String ctype,String photes) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserCertify data = new UserCertify();
		data.setUserid(userid);
		data.setCtype(ctype);
		data.setPhotes(photes);
		Date date = new Date();
		data.setCreatetime(date);
		data.setUpdatetime(date);
		try {
			int m = userCertifyMapper.insertCertify(data);
			if(m == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			 }
		} catch (Exception e) {
			logger.error("insertCertify error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectCertifyById(int id) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			UserCertify userCertify = userCertifyMapper.selectCertifyById(id);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(userCertify);
		} catch (Exception e) {
			logger.error("selectCertifyById error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateApplyCertify(int id,String ctype,String photes) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		UserCertify data = new UserCertify();
		data.setId(id);
		data.setCtype(ctype);
		data.setPhotes(photes);
		Date date = new Date();
		data.setUpdatetime(date);
		try {
			int n = userCertifyMapper.updateApplyCertify(data);
			if(n == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateCertify error and msg={}",e);
		}
		return baseResp;
	}



}
