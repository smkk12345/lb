package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ArticleBusiness;

public interface ArticleBusinessMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticleBusiness record);

    int insertSelective(ArticleBusiness record);

    ArticleBusiness selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleBusiness record);

    int updateByPrimaryKey(ArticleBusiness record);
}