package com.longbei.appservice.service;

import java.util.List;

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
	BaseResp<ImpComplaints> insertSelective(ImpComplaints record, long friendid);
	
	
	
	
	//----------------------------------adminservice调用------------------------------------------------
	
	/** 
	* @Title: selectImpComplaints 
	* @Description: 查询投诉列表
	* @param @param startNo
	* @param @param pageSize
	* @param @return    设定文件 
	* @return BaseResp<List<ImpComplaints>>    返回类型 
	*/
	BaseResp<List<ImpComplaints>> selectImpComplaints(int startNo, int pageSize); 
	
	
	
}
