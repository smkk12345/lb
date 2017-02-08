package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ImpComplaints;

public interface ImpComplaintsService {

	/**
	 * @author yinxc
	 * 添加投诉信息
	 * 2017年2月7日
	 * return_type
	 * ImpComplaintsService
	 */
	BaseResp<Object> insertSelective(ImpComplaints record);
	
}
