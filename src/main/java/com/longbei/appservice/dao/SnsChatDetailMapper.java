package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsChatDetail;

public interface SnsChatDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsChatDetail record);

    int insertSelective(SnsChatDetail record);

    SnsChatDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsChatDetail record);

    int updateByPrimaryKey(SnsChatDetail record);
}