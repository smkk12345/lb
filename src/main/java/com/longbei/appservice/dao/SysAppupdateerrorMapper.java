package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysAppupdateerror;

public interface SysAppupdateerrorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAppupdateerror record);

    int insertSelective(SysAppupdateerror record);

    SysAppupdateerror selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAppupdateerror record);

    int updateByPrimaryKey(SysAppupdateerror record);
}