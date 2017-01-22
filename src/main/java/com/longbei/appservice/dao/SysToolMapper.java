package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysTool;

public interface SysToolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysTool record);

    int insertSelective(SysTool record);

    SysTool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysTool record);

    int updateByPrimaryKey(SysTool record);
}