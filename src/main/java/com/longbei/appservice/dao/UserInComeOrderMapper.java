package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserInComeOrder;

public interface UserInComeOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInComeOrder record);

    int insertSelective(UserInComeOrder record);

    UserInComeOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInComeOrder record);

    int updateByPrimaryKey(UserInComeOrder record);
}