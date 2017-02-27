package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserSchool;

public interface UserSchoolService {
	/**
	* @Title: insertSchool
	* @Description: 添加教育经历
    * @param @param userid      
    * @param @param schoolname学校名称
    * @param @param Department院系
    * @param @param starttime教育起始时间
	* @param @param endtime教育结束时间
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/
	BaseResp<Object> insertSchool(long userid,String schoolname,String Department,Date starttime,Date endtime);
	/**
	* @Title: deleteSchool
	* @Description: 删除教育经历（通过id和userid删除）
    * @param @param id      
    * @param @param userid 
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/
	BaseResp<Object> deleteSchool(int id,long userid);
	/**
	* @Title: selectSchoolById
	* @Description: 查询教育经历
    * @param @param id      
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/	
	BaseResp<Object> selectSchoolById(int id);
	/**
	* @Title: selectSchoolList
	* @Description: 查看教育经历列表
    * @param @param userid     
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/		
	BaseResp<Object> selectSchoolList(long userid,int startNum,int pageSize);
	/**
	* @Title: updateSchool
	* @Description: 修改教育经历
    * @param @param userid      
    * @param @param schoolname学校名称
    * @param @param Department院系
    * @param @param starttime教育起始时间
	* @param @param endtime教育结束时间
	* @param @return
	* @auther IngaWu
	* @currentdate:2017年2月22日
	*/
	BaseResp<Object> updateSchool(int id,String schoolname,String Department,Date starttime,Date endtime);
}
