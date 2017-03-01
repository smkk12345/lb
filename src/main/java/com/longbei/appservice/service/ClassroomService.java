package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Classroom;

public interface ClassroomService {

	BaseResp<Object> insertClassroom(Classroom record);
	
	Classroom selectByClassroomid(long classroomid);
	
	BaseResp<Object> updateByClassroomid(Classroom record);
	
	/**
	 * @author yinxc
	 * 获取教室信息
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectListByUserid(long userid, String ptype, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取我加入的教室信息List
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectInsertByUserid(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据ptype获取教室信息(通用方法)
	 * param ptype:十全十美类型
	 * param keyword:关键字搜索    可为null
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	BaseResp<Object> selectListByPtype(String ptype, String keyword, int startNum, int endNum);
	
}
