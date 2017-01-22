package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveGoal;

public interface ImproveGoalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveGoal record);

    int insertSelective(ImproveGoal record);

    ImproveGoal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImproveGoal record);

    int updateByPrimaryKey(ImproveGoal record);
}