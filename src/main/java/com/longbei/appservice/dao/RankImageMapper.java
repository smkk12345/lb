package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;

public interface RankImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankImage record);

    int insertSelective(RankImage record);

    Rank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankImage record);

    int updateByPrimaryKey(RankImage record);
}