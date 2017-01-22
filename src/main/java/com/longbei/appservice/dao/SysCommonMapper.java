package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysCommon;

public interface SysCommonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysCommon record);

    int insertSelective(SysCommon record);

    SysCommon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysCommon record);

    int updateByPrimaryKey(SysCommon record);
}