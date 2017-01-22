package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsAcquaintances;

public interface SnsAcquaintancesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsAcquaintances record);

    int insertSelective(SnsAcquaintances record);

    SnsAcquaintances selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsAcquaintances record);

    int updateByPrimaryKey(SnsAcquaintances record);
}