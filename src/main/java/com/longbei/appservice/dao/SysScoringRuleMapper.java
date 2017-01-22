package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysScoringRule;

public interface SysScoringRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysScoringRule record);

    int insertSelective(SysScoringRule record);

    SysScoringRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysScoringRule record);

    int updateByPrimaryKey(SysScoringRule record);
}