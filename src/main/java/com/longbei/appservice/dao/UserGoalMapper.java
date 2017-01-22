package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserGoal;

public interface UserGoalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGoal record);

    int insertSelective(UserGoal record);

    UserGoal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGoal record);

    int updateByPrimaryKey(UserGoal record);
}