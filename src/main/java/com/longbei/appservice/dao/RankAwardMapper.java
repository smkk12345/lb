package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankAward;

public interface RankAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankAward record);

    int insertSelective(RankAward record);

    RankAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankAward record);

    int updateByPrimaryKey(RankAward record);
}