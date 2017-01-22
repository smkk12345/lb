package com.longbei.appservice.dao;

import com.longbei.appservice.entity.PutFashionman;

public interface PutFashionmanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PutFashionman record);

    int insertSelective(PutFashionman record);

    PutFashionman selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PutFashionman record);

    int updateByPrimaryKey(PutFashionman record);
}