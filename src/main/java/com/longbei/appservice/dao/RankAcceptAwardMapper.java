package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankAcceptAward;

import java.util.List;

public interface RankAcceptAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankAcceptAward record);

    int insertBatch(List<RankAcceptAward> acceptAwards);

    int insertSelective(RankAcceptAward record);

    RankAcceptAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankAcceptAward record);

    int updateByPrimaryKey(RankAcceptAward record);
}