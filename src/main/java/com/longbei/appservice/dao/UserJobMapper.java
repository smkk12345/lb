package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserJob;

public interface UserJobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserJob record);

    int insertSelective(UserJob record);

    UserJob selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserJob record);

    int updateByPrimaryKey(UserJob record);
}