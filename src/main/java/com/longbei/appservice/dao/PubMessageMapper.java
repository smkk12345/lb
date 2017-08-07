package com.longbei.appservice.dao;

import com.longbei.appservice.entity.PubMessage;

public interface PubMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PubMessage record);

    int insertSelective(PubMessage record);

    PubMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PubMessage record);

    int updateByPrimaryKey(PubMessage record);
}