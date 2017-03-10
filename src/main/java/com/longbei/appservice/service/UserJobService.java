package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;

public interface UserJobService {
	/**
	* @Title: insertJob
	* @Description: 添加工作经历
    * @param @param userid      
    * @param @param companyname所在公司
    * @param @param department所在部门
    * @param @param location工作所在地
    * @param @param starttime工作起始时间
	* @param @param endtime工作结束时间
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/
	BaseResp<Object> insertJob(long userid,String companyname,String department,String location,String starttime,Date endtime);
	/**
	* @Title: deleteJob
	* @Description: 删除工作经历（通过id和userid删除）
    * @param @param id      
    * @param @param userid 
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/
	BaseResp<Object> deleteJob(int id,long userid);
	/**
	* @Title: selectJobById
	* @Description: 查询工作经历
    * @param @param id      
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/	
	BaseResp<Object> selectJobById(int id);
	/**
	* @Title: selectJobList
	* @Description: 查看工作经历列表
    * @param @param userid     
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/		
	BaseResp<Object> selectJobList(long userid,int startNum,int pageSize);
	/**
	* @Title: insertJob
	* @Description: 修改工作经历
    * @param @param id      
    * @param @param companyname所在公司
    * @param @param department所在部门
    * @param @param location工作所在地
    * @param @param starttime工作起始时间
	* @param @param endtime工作结束时间
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/
	BaseResp<Object> updateJob(int id,String companyname,String department,String location,String starttime,Date endtime);
}
