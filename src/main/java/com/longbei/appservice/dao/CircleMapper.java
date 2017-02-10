package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Circle;

public interface CircleMapper {
    int deleteByPrimaryKey(long circleid);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(long circleid);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);
}