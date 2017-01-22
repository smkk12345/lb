package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Circle;

public interface CircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);
}