package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Module;

import java.util.List;

public interface ModuleMapper {

    int deleteByPrimaryKey(String  key);

    int insert(Module record);

    int insertSelective(Module record);

    Module selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(Module record);

    List<Module> selectList();

    int updateByPrimaryKey(Module record);
}