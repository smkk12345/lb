package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserGiftDetail;

public interface UserGiftDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGiftDetail record);

    int insertSelective(UserGiftDetail record);

    UserGiftDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGiftDetail record);

    int updateByPrimaryKey(UserGiftDetail record);
}