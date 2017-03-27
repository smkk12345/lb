package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Article;
import com.longbei.appservice.service.ArticleService;
import org.springframework.stereotype.Service;

/**
 * 文章相关操作
 *
 * @author luye
 * @create 2017-03-24 下午2:26
 **/
@Service
public class ArticleServiceImpl implements ArticleService{

    @Override
    public BaseResp<Object> insertActicle(Article article) {
        return null;
    }

    @Override
    public BaseResp<Object> updateActicle(Article article) {
        return null;
    }

    @Override
    public BaseResp<Page<Article>> selectActicleList(Article article, Integer pageno, Integer pagesize) {
        return null;
    }
}
