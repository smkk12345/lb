package com.longbei.appservice.dao;

import com.longbei.appservice.entity.PutRules;

public interface PutRulesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PutRules record);

    int insertSelective(PutRules record);
}