package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserPointDetail;

public interface UserPointDetailMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(UserPointDetail record);

    int insertSelective(UserPointDetail record);

    UserPointDetail selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(UserPointDetail record);

    int updateByPrimaryKey(UserPointDetail record);
}