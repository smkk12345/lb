package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysPerfectInfo;

public interface SysPerfectInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPerfectInfo record);

    int insertSelective(SysPerfectInfo record);

    SysPerfectInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPerfectInfo record);

    int updateByPrimaryKey(SysPerfectInfo record);
}