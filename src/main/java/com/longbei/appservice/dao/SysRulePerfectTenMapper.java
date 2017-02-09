package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysRulePerfectTen;
import java.util.List;

public interface SysRulePerfectTenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRulePerfectTen record);

    int insertSelective(SysRulePerfectTen record);

    List<SysRulePerfectTen> selectAll();

    int updateByPrimaryKeySelective(SysRulePerfectTen record);

    int updateByPrimaryKey(SysRulePerfectTen record);
}