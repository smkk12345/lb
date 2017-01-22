package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsGroupMembers;

public interface SnsGroupMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsGroupMembers record);

    int insertSelective(SnsGroupMembers record);

    SnsGroupMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsGroupMembers record);

    int updateByPrimaryKey(SnsGroupMembers record);
}