package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.SysPerfectInfoMapper;
import com.longbei.appservice.entity.SysPerfectInfo ;
import com.longbei.appservice.service.SysPerfectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("sysPerfectInfoService")
public class SysPerfectInfoServiceImpl implements SysPerfectInfoService {

	@Autowired
	private SysPerfectInfoMapper sysPerfectInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(SysPerfectInfoServiceImpl.class);


	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectPerfectInfoById(int id) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			SysPerfectInfo sysPerfectInfo = sysPerfectInfoMapper.selectPerfectInfoById(id);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(sysPerfectInfo);
		} catch (Exception e) {
			logger.error("selectPerfectInfoById error and msg={}",e);
		}
		return baseResp;
	}
	
}
