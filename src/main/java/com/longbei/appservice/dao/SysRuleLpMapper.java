package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysRuleLp;

public interface SysRuleLpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRuleLp record);

    int insertSelective(SysRuleLp record);

    SysRuleLp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRuleLp record);

    int updateByPrimaryKey(SysRuleLp record);
}