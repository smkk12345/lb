package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserCheckinInfo;

public interface UserCheckinInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCheckinInfo record);

    int insertSelective(UserCheckinInfo record);

    UserCheckinInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCheckinInfo record);

    int updateByPrimaryKey(UserCheckinInfo record);
}