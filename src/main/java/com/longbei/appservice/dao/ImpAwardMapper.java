package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpAward;

public interface ImpAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImpAward record);

    int insertSelective(ImpAward record);

    ImpAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImpAward record);

    int updateByPrimaryKey(ImpAward record);
}