package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankAcceptAward;

public interface RankAcceptAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankAcceptAward record);

    int insertSelective(RankAcceptAward record);

    RankAcceptAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankAcceptAward record);

    int updateByPrimaryKey(RankAcceptAward record);
}