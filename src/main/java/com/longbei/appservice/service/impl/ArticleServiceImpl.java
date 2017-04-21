package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ArticleMapper;
import com.longbei.appservice.entity.Article;
import com.longbei.appservice.entity.Award;
import com.longbei.appservice.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 文章相关操作
 *
 * @author luye
 * @create 2017-03-24 下午2:26
 **/
@Service
public class ArticleServiceImpl implements ArticleService{

    private static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);


    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public boolean insertArticle(Article article) {
        try {
            article.setCreatetime(new Date());
            article.setUpdatetime(new Date());
            int res = articleMapper.insertSelective(article);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("insert article is error:{}",e);
        }
        return false;
    }

    @Override
    public BaseResp<Article> getArticle(String articleid) {
        BaseResp<Article> baseResp = new BaseResp<Article>();
        Article article = new Article();
        try {
            article = articleMapper.selectByPrimaryKey(Integer.parseInt(articleid));
            baseResp.setData(article);
        } catch (NumberFormatException e) {
            logger.error("select article articleid={} is error:{}",articleid,e);
        }
        return baseResp;
    }

    @Override
    public boolean updateArticle(Article article) {
        try {
            article.setUpdatetime(new Date());
            int res = articleMapper.updateByPrimaryKeySelective(article);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("update article is error:{}",e);
        }
        return false;
    }

    @Override
    public BaseResp<Page<Article>> selectArticleListWithPage(Article article, Integer pageno, Integer pagesize) {
        BaseResp<Page<Article>> baseResp = new BaseResp<>();
        Page<Article> page = new Page<>(pageno,pagesize);
        try {
            int totalCount = articleMapper.selectArticleCount(article);
            List<Article> articles = articleMapper.selectArticleList(article,pagesize*(pageno-1),pagesize);
            page.setTotalCount(totalCount);
            page.setList(articles);
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select article list with page is error:{}",e);
        }
        return baseResp;
    }

}
