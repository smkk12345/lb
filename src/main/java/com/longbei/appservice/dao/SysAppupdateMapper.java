package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysAppupdate;

public interface SysAppupdateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAppupdate record);

    int insertSelective(SysAppupdate record);

    SysAppupdate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAppupdate record);

    int updateByPrimaryKey(SysAppupdate record);
}