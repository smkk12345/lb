package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.ClassroomCourses;

public interface ClassroomCoursesService {

	/**
	 * @author yinxc
	 * 获取课程列表(未删除,转码成功的)
	 * 2017年3月1日
	 * param classroomid 教室id
	 */
	BaseResp<List<ClassroomCourses>> selectListByClassroomid(long classroomid, int startNum, int endNum);
	
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
	BaseResp<Object> updateIsdel(long classroomid, Integer id);
	
	

	
	//---------------------------------admin调用------------------------------------------
	
	/**
    * @Description: 获取教室课程列表
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年7月4日
	*/
	BaseResp<Page<ClassroomCourses>> selectPcSearchCroomCoursesList(ClassroomCourses classroomCourses, int startNum, int endNum);
	
	/**
    * @Description: 获取课程信息列表---开始年月日查询
    * @param classroomid 教室id
    * @param daytime 开始年月日 用于方便查询
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年8月26日
	*/
	BaseResp<List<ClassroomCourses>> selectDaytimeCoursesListByCid(long classroomid, String daytime, int startNum, int endNum);
	
	/*
     * 获取课程信息---最近直播
     * classroomid 教室id
     */
	ClassroomCourses selectTeachingCoursesListByCid(long classroomid);
	
	/**
	 * @author yinxc
	 * 修改课程排序
	 * 2017年7月12日
	 * param id  课程id
	 * param classroomid 教室id
	 * param coursesort 课程序号
	 */
	BaseResp<Object> updateSortByid(Integer id, long classroomid, Integer coursesort);
	
	/**
     * @Description: 发布教室课程
     * @param @param classroomid 教室id
     * @param @param id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月25日
 	*/
	BaseResp<Object> updateIsup(Integer id, long classroomid);
	
	BaseResp<Object> saveCourses(ClassroomCourses classroomCourses);
	
	BaseResp<Object> editCourses(ClassroomCourses classroomCourses);
	
	/**
     * @Description: 获取教室课程
     * @param @param classroomid 教室id
     * @param @param id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月12日
 	*/
	BaseResp<ClassroomCourses> selectCourses(long classroomid, Integer id);
	
}
