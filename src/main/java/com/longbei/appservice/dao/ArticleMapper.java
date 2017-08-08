package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);

    int selectArticleCount(@Param("article") Article article);

    List<Article> selectArticleList(@Param("article") Article article,
                                              @Param("startno") Integer startno, @Param("pagesize") Integer pageszie);

    int updatePageViewById(@Param("id") String id);

}