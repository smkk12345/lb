package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserPlDetail;

public interface UserPlDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPlDetail record);

    int insertSelective(UserPlDetail record);

    UserPlDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPlDetail record);

    int updateByPrimaryKey(UserPlDetail record);
}