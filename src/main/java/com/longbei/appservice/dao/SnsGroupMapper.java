package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsGroup;

public interface SnsGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsGroup record);

    int insertSelective(SnsGroup record);

    SnsGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsGroup record);

    int updateByPrimaryKey(SnsGroup record);
}