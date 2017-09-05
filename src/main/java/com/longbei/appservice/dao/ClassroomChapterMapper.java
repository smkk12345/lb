package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ClassroomChapter;

public interface ClassroomChapterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomChapter record);

    int insertSelective(ClassroomChapter record);

    ClassroomChapter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomChapter record);

    int updateByPrimaryKey(ClassroomChapter record);
}