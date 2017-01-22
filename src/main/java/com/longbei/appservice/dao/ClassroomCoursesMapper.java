package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ClassroomCourses;

public interface ClassroomCoursesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomCourses record);

    int insertSelective(ClassroomCourses record);

    ClassroomCourses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomCourses record);

    int updateByPrimaryKey(ClassroomCourses record);
}