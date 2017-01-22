package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserCheckinDetail;

public interface UserCheckinDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCheckinDetail record);

    int insertSelective(UserCheckinDetail record);

    UserCheckinDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCheckinDetail record);

    int updateByPrimaryKey(UserCheckinDetail record);
}