package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ClassroomClassnotice;

public interface ClassroomClassnoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomClassnotice record);

    int insertSelective(ClassroomClassnotice record);

    ClassroomClassnotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomClassnotice record);

    int updateByPrimaryKey(ClassroomClassnotice record);
}