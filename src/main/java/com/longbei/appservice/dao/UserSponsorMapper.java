package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserSponsor;

public interface UserSponsorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSponsor record);

    int insertSelective(UserSponsor record);

    UserSponsor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSponsor record);

    int updateByPrimaryKey(UserSponsor record);
}