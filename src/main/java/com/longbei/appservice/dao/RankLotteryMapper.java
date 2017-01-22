package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankLottery;

public interface RankLotteryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankLottery record);

    int insertSelective(RankLottery record);
}