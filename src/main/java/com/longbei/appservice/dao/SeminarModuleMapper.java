package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SeminarModule;

import java.util.List;

public interface SeminarModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBySeminarid(Long seminarid);

    int insert(SeminarModule record);

    int insertSelective(SeminarModule record);

    SeminarModule selectByPrimaryKey(Integer id);

    List<SeminarModule> selectList(Long seminarid);

    int updateByPrimaryKeySelective(SeminarModule record);

    int updateByPrimaryKey(SeminarModule record);
}