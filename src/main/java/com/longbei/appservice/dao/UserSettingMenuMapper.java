package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserSettingMenu;

public interface UserSettingMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSettingMenu record);

    int insertSelective(UserSettingMenu record);

    UserSettingMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSettingMenu record);

    int updateByPrimaryKey(UserSettingMenu record);
}