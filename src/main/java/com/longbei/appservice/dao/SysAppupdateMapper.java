package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysAppupdate;

import java.util.List;

public interface SysAppupdateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAppupdate record);

    int insertSelective(SysAppupdate record);

    SysAppupdate selectByPrimaryKey(Integer id);

    SysAppupdate selectRecentByKey(String ttype);

    int updateByPrimaryKeySelective(SysAppupdate record);

    int updateByPrimaryKey(SysAppupdate record);

    /**
     * 查询版本更新的列表
     * @return
     */
    List<SysAppupdate> findSysAppUpdateList();
}