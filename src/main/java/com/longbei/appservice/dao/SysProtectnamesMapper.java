package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysProtectnames;

public interface SysProtectnamesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysProtectnames record);

    int insertSelective(SysProtectnames record);

    SysProtectnames selectProtectnames();

    int updateProtectNames(SysProtectnames record);

    int updateByPrimaryKey(SysProtectnames record);
}