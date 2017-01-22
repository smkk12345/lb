package com.longbei.appservice.dao;

import com.longbei.appservice.entity.CircleMembers;

public interface CircleMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleMembers record);

    int insertSelective(CircleMembers record);

    CircleMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleMembers record);

    int updateByPrimaryKey(CircleMembers record);
}