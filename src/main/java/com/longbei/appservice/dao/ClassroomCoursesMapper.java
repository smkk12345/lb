package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.ClassroomCourses;

public interface ClassroomCoursesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomCourses record);

    int insertSelective(ClassroomCourses record);

    ClassroomCourses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomCourses record);

    int updateByPrimaryKey(ClassroomCourses record);
    
    /**
	 * @author yinxc
	 * 获取课程列表(未删除,转码成功的)---排序coursesort课程序号desc
	 * 2017年3月1日
	 * param classroomid 教室id
	 */
    List<ClassroomCourses> selectListByClassroomid(@Param("classroomid") long classroomid, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取课程列表(未删除,转码成功的)---排序创建时间desc
	 * 2017年6月9日
	 * param classroomid 教室id
	 */
    List<ClassroomCourses> selectCroomidOrderByCtime(@Param("classroomid") long classroomid, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取当前教室的默认课程信息
	 * 2017年3月1日
	 * param classroomid 教室id
	 * isdefault 是否 默认   1 默认封面  0 非默认
	 */
    ClassroomCourses selectIsdefaultByClassroomid(@Param("classroomid") long classroomid);
    
    /**
	 * @author yinxc
	 * 用户课程列表都设为非默认状态
	 * 2017年3月1日
	 * param classroomid 教室id
	 * param createuserid 创建人id
	 * isdefault 是否 默认   1 默认封面  0 非默认
	 */
    int updateIsdefaultByClassroomid(@Param("classroomid") long classroomid);
    
    /**
	 * @author yinxc
	 * 修改课程的默认状态
	 * 2017年3月1日
	 * param id  课程id
	 * param isdefault 是否 默认   1 默认封面  0 非默认
	 * ClassroomCoursesMapper
	 */
    int updateIsdefaultByid(@Param("id") Integer id, @Param("isdefault") String isdefault);
    
    /**
	 * @author yinxc
	 * 删除课程信息(假删)
	 * 2017年3月1日
	 * param id  课程id
	 * isdel  0 — 未删除    1 —删除 
	 */
    int updateIsdel(@Param("classroomid") long classroomid, @Param("id") Integer id);
    
    /**
	 * @author yinxc
	 * 修改课程排序
	 * 2017年7月12日
	 * param id  课程id
	 * param classroomid 教室id
	 * param coursesort 课程序号
	 */
    Integer updateSortByid(@Param("classroomid") long classroomid, 
    		@Param("id") Integer id, @Param("coursesort") Integer coursesort);
    
    /**
	 * @author yinxc
	 * 获取当前教室课程总数
	 * 2017年3月3日
	 * param classroomid 教室id
	 */
    Integer selectCountCourses(@Param("classroomid") long classroomid);
    
    /*
     * 获取Count
     */
    Integer selectSearchCount(@Param("classroomCourses") ClassroomCourses classroomCourses);
    
    /*
     * 获取课程信息列表
     */
    List<ClassroomCourses> selectSearchList(@Param("classroomCourses") ClassroomCourses classroomCourses, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    
}