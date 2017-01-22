package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserMoneyDetail;

public interface UserMoneyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMoneyDetail record);

    int insertSelective(UserMoneyDetail record);

    UserMoneyDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMoneyDetail record);

    int updateByPrimaryKey(UserMoneyDetail record);
}