package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysMedal;

public interface SysMedalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysMedal record);

    int insertSelective(SysMedal record);

    SysMedal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysMedal record);

    int updateByPrimaryKey(SysMedal record);
}