package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ClassroomMembers;

public interface ClassroomMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomMembers record);

    int insertSelective(ClassroomMembers record);

    ClassroomMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomMembers record);

    int updateByPrimaryKey(ClassroomMembers record);
}