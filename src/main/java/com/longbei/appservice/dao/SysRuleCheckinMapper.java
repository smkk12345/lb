package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysRuleCheckin;

public interface SysRuleCheckinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRuleCheckin record);

    int insertSelective(SysRuleCheckin record);

    SysRuleCheckin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRuleCheckin record);

    int updateByPrimaryKey(SysRuleCheckin record);
}