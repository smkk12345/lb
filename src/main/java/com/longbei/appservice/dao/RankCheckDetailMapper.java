package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankCheckDetail;

public interface RankCheckDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankCheckDetail record);

    int insertSelective(RankCheckDetail record);

    RankCheckDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankCheckDetail record);

    int updateByPrimaryKey(RankCheckDetail record);
}