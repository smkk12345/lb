package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankMembers;

public interface RankMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankMembers record);

    int insertSelective(RankMembers record);

    RankMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankMembers record);

    int updateByPrimaryKey(RankMembers record);
}