package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpLikeDetail;

public interface ImpLikeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImpLikeDetail record);

    int insertSelective(ImpLikeDetail record);

    ImpLikeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImpLikeDetail record);

    int updateByPrimaryKey(ImpLikeDetail record);
}