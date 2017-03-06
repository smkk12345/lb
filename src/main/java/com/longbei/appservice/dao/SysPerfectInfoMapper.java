package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysPerfectInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysPerfectInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysPerfectInfo record);

    int insertSelective(SysPerfectInfo record);

    SysPerfectInfo selectPerfectInfoByPtype(String ptype);

    SysPerfectInfo selectPerfectPhotoByPtype(@Param("ptype")String ptype);

    int updateByPrimaryKeySelective(SysPerfectInfo record);

    int updateByPrimaryKey(SysPerfectInfo record);
}