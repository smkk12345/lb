package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserLevel;

import java.util.List;

public interface UserLevelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLevel record);

    int insertSelective(UserLevel record);

    UserLevel selectByPrimaryKey(Long id);

    List<UserLevel> selectAll();

    int updateByPrimaryKeySelective(UserLevel record);

    int updateByPrimaryKey(UserLevel record);
}