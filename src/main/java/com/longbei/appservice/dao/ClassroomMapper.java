package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Classroom;

public interface ClassroomMapper {
    int deleteByPrimaryKey(long classroomid);

    int insert(Classroom record);

    int insertSelective(Classroom record);

    Classroom selectByPrimaryKey(long classroomid);

    int updateByPrimaryKeySelective(Classroom record);

    int updateByPrimaryKey(Classroom record);
}