package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserGiftSortable;

public interface UserGiftSortableMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGiftSortable record);

    int insertSelective(UserGiftSortable record);

    UserGiftSortable selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGiftSortable record);

    int updateByPrimaryKey(UserGiftSortable record);
}