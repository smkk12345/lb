package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ImpComplaintsMapper;
import com.longbei.appservice.entity.ImpComplaints;
import com.longbei.appservice.service.ImpComplaintsService;

@Service("impComplaintsService")
public class ImpComplaintsServiceImpl implements ImpComplaintsService {

	@Autowired
	private ImpComplaintsMapper impComplaintsMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ImpComplaintsServiceImpl.class);
	
	@Override
	public BaseResp<ImpComplaints> insertSelective(ImpComplaints record) {
		BaseResp<ImpComplaints> reseResp = new BaseResp<>();
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
	
	private boolean insert(ImpComplaints record){
		int temp = impComplaintsMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<List<ImpComplaints>> selectImpComplaints(int startNo, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
