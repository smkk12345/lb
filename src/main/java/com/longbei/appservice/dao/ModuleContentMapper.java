package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ModuleContent;

import java.util.List;

public interface ModuleContentMapper {

    int deleteByPrimaryKey(Integer id);

    int deleteBySemmodid(Long id);

    int insert(ModuleContent record);

    int insertSelective(ModuleContent record);

    int insertBatch(List<ModuleContent> moduleContents);

    ModuleContent selectByPrimaryKey(Integer id);

    List<ModuleContent> selectBySemmodid(String semmodid);

    int updateByPrimaryKeySelective(ModuleContent record);

    int updateByPrimaryKey(ModuleContent record);

}