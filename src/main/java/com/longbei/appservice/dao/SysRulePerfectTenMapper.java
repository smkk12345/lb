package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysRulePerfectTen;

public interface SysRulePerfectTenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRulePerfectTen record);

    int insertSelective(SysRulePerfectTen record);

    SysRulePerfectTen selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRulePerfectTen record);

    int updateByPrimaryKey(SysRulePerfectTen record);
}