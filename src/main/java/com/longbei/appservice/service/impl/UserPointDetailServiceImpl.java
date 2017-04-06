package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.UserPointDetailMapper;
import com.longbei.appservice.entity.UserPointDetail;
import com.longbei.appservice.service.UserPointDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("userPointDetailService")
public class UserPointDetailServiceImpl implements UserPointDetailService {

	@Autowired
	private UserPointDetailMapper userPointDetailMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserPointDetailServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> selectPointListByUseridAndPointtype(long userid,String pointtype,int startNum,int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			List<UserPointDetail> list = userPointDetailMapper.selectPointListByUseridAndPointtype(userid,pointtype,startNum,pageSize);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectPointListByUseridAndPointtype error and msg={}",e);
		}
		return baseResp;
	}

}
