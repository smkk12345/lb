package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpGoalPerday;

public interface ImpGoalPerdayMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImpGoalPerday record);

    int insertSelective(ImpGoalPerday record);

    ImpGoalPerday selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImpGoalPerday record);

    int updateByPrimaryKey(ImpGoalPerday record);
}