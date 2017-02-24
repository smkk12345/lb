package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserSchool;

public interface UserSchoolMapper {
    int insertSchool(UserSchool data);
    int deleteSchool(@Param("id") int id,@Param("userid") long userid);
    UserSchool selectSchoolById(@Param("id")int id);
    List<UserSchool> selectSchoolList(@Param("userid") long userid,@Param("startNum") int startNum,@Param("pageSize") int pageSize);
    int updateSchool(UserSchool data);
}