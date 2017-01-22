package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserSchool;

public interface UserSchoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSchool record);

    int insertSelective(UserSchool record);

    UserSchool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSchool record);

    int updateByPrimaryKey(UserSchool record);
}