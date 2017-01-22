package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInterests record);

    int insertSelective(UserInterests record);

    UserInterests selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInterests record);

    int updateByPrimaryKey(UserInterests record);
}