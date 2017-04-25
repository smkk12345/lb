package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.entity.Article;
import com.longbei.appservice.entity.ArticleBusiness;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文章接口
 * Created by mchaolee on 17/4/1.
 */
@RestController
@RequestMapping(value = "api/article")
public class ArticleApiController {

    private static Logger logger = LoggerFactory.getLogger(ArticleApiController.class);

    @Autowired
    private ArticleService articleService;

    /**
     * 文章列表
     */
    @RequestMapping(value = "list/{pageno}/{pagesize}")
    public BaseResp<Page<Article>> getArticleListWithPage(@RequestBody Article article,
                                                                  @PathVariable("pageno") String pageno,
                                                                  @PathVariable("pagesize") String pagesize){
        BaseResp<Page<Article>> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(pageno)){
            pageno = "1";
        }
        if (StringUtils.isEmpty(pagesize)){
            pageno = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = articleService.selectArticleListWithPage(article,Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (NumberFormatException e) {
            logger.error("get article list with page is error:{}",e);
        }
        return baseResp;
    }


    @RequestMapping(value = "get/{articleid}")
    public BaseResp<Article> getArticle(@PathVariable("articleid") String articleid){
        BaseResp<Article> baseResp = new BaseResp<>();
        if (StringUtils.isEmpty(articleid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = articleService.getArticle(articleid);
        } catch (NumberFormatException e) {
            logger.error("get article  is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "add")
    public BaseResp addArticle(@RequestBody Article article){
        BaseResp baseResp = new BaseResp<>();
        try {
            boolean flag = articleService.insertArticle(article);
            if (flag){
                baseResp = BaseResp.ok();
                baseResp.setData(article.getId());
            }
        } catch (NumberFormatException e) {
            logger.error("add article  is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "update")
    public BaseResp updateArticle(@RequestBody Article article){
        BaseResp baseResp = new BaseResp();
        try {
            boolean flag = articleService.updateArticle(article);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("update article  is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "addBusiness")
    public BaseResp addBusiness(@RequestBody ArticleBusiness articleBusiness){
        BaseResp baseResp = new BaseResp<>();
        try {
            boolean flag = articleService.insertArticleBusiness(articleBusiness);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("add articleBusiness  is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "getBusiness/{articleid}")
    BaseResp<List<ArticleBusiness>> getArticleBussiness(@PathVariable("articleid") String articleid){
        BaseResp<List<ArticleBusiness>> baseResp = new BaseResp<>();

        if (StringUtils.isEmpty(articleid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = articleService.selectArticleBusinessList(articleid);

        } catch (NumberFormatException e) {
            logger.error("get articleBusiness list is error:{}",e);
        }
        return baseResp;
    }

    @RequestMapping(value = "deleteBusiness/{articleid}")
    BaseResp deleteArticleBusinessByArticleId(@PathVariable("articleid") String articleid){
        BaseResp baseResp = new BaseResp<>();
        try {
            boolean flag = articleService.deleteArticleBusinessByArticleId(articleid);
            if (flag){
                baseResp = BaseResp.ok();
            }
        } catch (NumberFormatException e) {
            logger.error("delete articleBusiness  is error:{}",e);
        }
        return baseResp;
    }



}
