package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysLongbeiinfo;

public interface SysLongbeiinfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLongbeiinfo record);

    int insertSelective(SysLongbeiinfo record);

    SysLongbeiinfo selectByPrimaryKey(Integer id);

    SysLongbeiinfo selectDefault();

    int updateByPrimaryKeySelective(SysLongbeiinfo record);

    int updateByPrimaryKey(SysLongbeiinfo record);
}