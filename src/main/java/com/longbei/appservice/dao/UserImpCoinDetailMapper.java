package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserImpCoinDetail;

public interface UserImpCoinDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserImpCoinDetail record);

    int insertSelective(UserImpCoinDetail record);

    UserImpCoinDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserImpCoinDetail record);

    int updateByPrimaryKey(UserImpCoinDetail record);
}