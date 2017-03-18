package com.longbei.appservice.dao;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserCard;

public interface UserCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCard record);

    int insertSelective(UserCard record);

    UserCard selectByCardid(@Param("cardid") long cardid);

    int updateByPrimaryKeySelective(UserCard record);

    int updateByPrimaryKey(UserCard record);
    
}