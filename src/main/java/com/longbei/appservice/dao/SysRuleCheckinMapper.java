package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysRuleCheckin;
import java.util.List;

public interface SysRuleCheckinMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRuleCheckin record);

    int insertSelective(SysRuleCheckin record);

    List<SysRuleCheckin> selectAll();

    int updateByPrimaryKeySelective(SysRuleCheckin record);

    int updateByPrimaryKey(SysRuleCheckin record);
}