package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserSettingTool;

public interface UserSettingToolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSettingTool record);

    int insertSelective(UserSettingTool record);

    UserSettingTool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSettingTool record);

    int updateByPrimaryKey(UserSettingTool record);
}