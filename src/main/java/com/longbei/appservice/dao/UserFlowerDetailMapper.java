package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserFlowerDetail;

public interface UserFlowerDetailMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(UserFlowerDetail record);

    int insertSelective(UserFlowerDetail record);

    UserFlowerDetail selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(UserFlowerDetail record);

    int updateByPrimaryKey(UserFlowerDetail record);
}