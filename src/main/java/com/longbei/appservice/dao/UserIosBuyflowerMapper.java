package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserIosBuyflower;

public interface UserIosBuyflowerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserIosBuyflower record);

    int insertSelective(UserIosBuyflower record);

    UserIosBuyflower selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserIosBuyflower record);

    int updateByPrimaryKey(UserIosBuyflower record);
}