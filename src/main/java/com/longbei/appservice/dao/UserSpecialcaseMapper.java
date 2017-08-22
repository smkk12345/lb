package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserSpecialcase;

public interface UserSpecialcaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSpecialcase record);

    int insertSelective(UserSpecialcase record);

    UserSpecialcase selectUserSpecialcase();

    int updateUserSpecialcase(UserSpecialcase record);

    int updateByPrimaryKey(UserSpecialcase record);
}