package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ClassroomCourses;

public interface ClassroomCoursesService {

	/**
	 * @author yinxc
	 * 获取课程列表(未删除,转码成功的)
	 * 2017年3月1日
	 * param classroomid 教室id
	 */
	BaseResp<Object> selectListByClassroomid(long classroomid, int startNum, int endNum);
	
	ClassroomCourses selectByid(Integer id);
	
	/**
	 * @author yinxc
	 * 获取当前教室的默认课程信息
	 * 2017年3月1日
	 * param classroomid 教室id
	 * isdefault 是否 默认   1 默认封面  0 非默认
	 */
	ClassroomCourses selectIsdefaultByClassroomid(long classroomid);
	
	/**
	 * @author yinxc
	 * 修改课程的默认状态
	 * 2017年3月1日
	 * param id  课程id
	 * param classroomid 教室id
	 * param isdefault 是否 默认   1 默认封面  0 非默认
	 * ClassroomCoursesMapper
	 */
	BaseResp<Object> updateIsdefaultByid(Integer id, long classroomid, String isdefault);
	
	/**
	 * @author yinxc
	 * 删除课程信息(假删)
	 * 2017年3月1日
	 * param id  课程id
	 * isdel  0 — 未删除    1 —删除 
	 */
	BaseResp<Object> updateIsdel(Integer id);
	
}
