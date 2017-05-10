package com.longbei.appservice.dao;

import com.longbei.appservice.entity.BehaviorRule;

public interface BehaviorRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BehaviorRule record);

    int insertSelective(BehaviorRule record);

    BehaviorRule selectOne();

    int updateByPrimaryKeySelective(BehaviorRule record);

    int updateByPrimaryKey(BehaviorRule record);
}