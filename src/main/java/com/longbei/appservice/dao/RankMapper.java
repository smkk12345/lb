package com.longbei.appservice.dao;
import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.Rank;

public interface RankMapper {
    int deleteByPrimaryKey(Integer rankid);

    int insert(Rank record);

    int insertSelective(Rank record);

    Rank selectByPrimaryKey(Long rankid);

    int updateByPrimaryKeySelective(Rank record);

    int updateByPrimaryKey(Rank record);

    int updateSponsornumAndSponsormoney(@Param("rankid")Long rankid);

    Rank selectRankByRankid(@Param("rankid")Long rankid);
}