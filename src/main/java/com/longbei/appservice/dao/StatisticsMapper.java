package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Statistics;

import java.util.List;

public interface StatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Statistics record);

    int insertSelective(Statistics record);

    Statistics selectByPrimaryKey(Integer id);

    List<Statistics> listByStartDate(Statistics record);

    int updateByPrimaryKeySelective(Statistics record);

    int updateByPrimaryKey(Statistics record);
}