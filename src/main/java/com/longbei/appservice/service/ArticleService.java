package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Article;

/**
 * 文章相关操作
 *
 * @author lichaochao
 * @create 2017-04-21
 **/
public interface ArticleService {

    /**
     * 添加文章
     * @param article
     * @return
     */
    boolean insertArticle(Article article);


    /**
     * 获取文章
     * @param articleid
     * @return
     */
    BaseResp<Article> getArticle(String articleid);


    /**
     * 更新文章
     * @param article
     * @return
     */
    boolean updateArticle(Article article);


    /**
     * 获取文章列表（分页）
     * @param article
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<Article>> selectArticleListWithPage(Article article,Integer pageno,Integer pagesize);


}
