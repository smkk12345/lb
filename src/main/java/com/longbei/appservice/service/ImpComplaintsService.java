package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
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
	* @Title: selectListByStatus 
	* @Description: 根据status查询投诉列表
	* @param @param status
	* @param @param startNo
	* @param @param pageSize
	* @param @return    设定文件 
	* @return Page<ImpComplaints>    返回类型 
	*/
	Page<ImpComplaints> selectListByStatus(String status, int startNo, int pageSize); 
	
	/**
    * @Title: searchList 
    * @Description: 搜索 
    * @param @param status
    * @param @param username
    * @param @param businesstype
    * @param @param sdealtime
    * @param @param edealtime
    * @param @param startNo
    * @param @param pageSize
    * @param @return    设定文件 
    * @return Page<ImpComplaints>    返回类型
     */
	Page<ImpComplaints> searchList(String status, String username, String businesstype, 
    		String sdealtime, String edealtime, int startNo, int pageSize);
	
	/**
    * @Title: updateImpComplaintsByStatus 
    * @Description: 投诉处理
    * @param @param status
    * @param @param dealtime
    * @param @param checkoption
    * @param @param dealuser 处理人
    * @param @param id 业务id
    * @param @return    设定文件 
    * @return BaseResp<Object>    返回类型
     */
	BaseResp<Object> updateImpComplaintsByStatus(long id, String status, Date dealtime, String dealuser, 
    		String checkoption);
	
	/**
	* @Title: selectDetail 
	* @Description: 获取投诉详情
	* @param @param id
	* @param @return    设定文件 
	* @return BaseResp<ImpComplaints>    返回类型
	 */
	BaseResp<ImpComplaints> selectDetail(long id);
	
}
