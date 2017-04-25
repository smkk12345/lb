package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ArticleBusiness;

import java.util.List;

public interface ArticleBusinessMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteArticleBusinessByArticleId(String articleid);

    int insert(ArticleBusiness record);

    int insertSelective(ArticleBusiness record);

    ArticleBusiness selectByPrimaryKey(Integer id);

    List<ArticleBusiness> selectArticleBusinessList(String articleid);

    int updateByPrimaryKeySelective(ArticleBusiness record);

    int updateByPrimaryKey(ArticleBusiness record);
}