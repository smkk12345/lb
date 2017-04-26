package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysCommon;
import org.apache.ibatis.annotations.Param;

public interface SysCommonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysCommon record);

    int insertSelective(SysCommon record);

    SysCommon selectByPrimaryKey(Integer id);

    SysCommon selectByKey(@Param("key") String key);

    int updateByPrimaryKeySelective(SysCommon record);

    int updateByKey(SysCommon record);

    int updateByPrimaryKey(SysCommon record);
}