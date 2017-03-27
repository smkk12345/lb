package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Article;

/**
 * 文章相关操作
 *
 * @author luye
 * @create 2017-03-24 下午2:20
 **/
public interface ArticleService {

    /**
     * 添加文章
     * @param article
     * @return
     */
    BaseResp<Object> insertActicle(Article article);


    /**
     * 更新文章
     * @param article
     * @return
     */
    BaseResp<Object> updateActicle(Article article);


    /**
     * 获取文章列表（分页）
     * @param article
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<Article>> selectActicleList(Article article,Integer pageno,Integer pagesize);


}
