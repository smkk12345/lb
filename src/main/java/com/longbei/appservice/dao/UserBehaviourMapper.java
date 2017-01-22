package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserBehaviour;

public interface UserBehaviourMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBehaviour record);

    int insertSelective(UserBehaviour record);

    UserBehaviour selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBehaviour record);

    int updateByPrimaryKey(UserBehaviour record);
}