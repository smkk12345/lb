package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserLotteryDetail;

public interface UserLotteryDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLotteryDetail record);

    int insertSelective(UserLotteryDetail record);

    UserLotteryDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLotteryDetail record);

    int updateByPrimaryKey(UserLotteryDetail record);
}