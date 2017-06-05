package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.ArticleBusinessMapper;
import com.longbei.appservice.dao.ArticleMapper;
import com.longbei.appservice.entity.Article;
import com.longbei.appservice.entity.ArticleBusiness;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankAwardRelease;
import com.longbei.appservice.service.ArticleService;
import com.longbei.appservice.service.RankService;
import org.apache.tools.ant.taskdefs.Concat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private ArticleBusinessMapper articleBusinessMapper;

    @Autowired
    private RankService rankService;

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
//            List<ArticleBusiness> articleBusinesses =  articleBusinessMapper.selectArticleBusinessList(articleid);
//            for (ArticleBusiness articleBusiness : articleBusinesses){
//                Rank rank = rankService.selectByRankid(articleBusiness.getBusinessid());
//                articleBusiness.setRank(rank);
//            }
//            article.setArticleBusinesses(articleBusinesses);
            baseResp = BaseResp.ok();
            baseResp.setData(article);
        } catch (NumberFormatException e) {
            logger.error("select article articleid={} is error:{}",articleid,e);
        }
        return baseResp;
    }



    @Override
    public BaseResp<Article> getArticleH5(String articleid) {
        BaseResp<Article> baseResp = new BaseResp<Article>();
        Article article = new Article();
        try {
            article = articleMapper.selectByPrimaryKey(Integer.parseInt(articleid));
            if (null == article){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_620,Constant.RTNINFO_SYS_620);
                return baseResp;
            }
            List<ArticleBusiness> articleBusinesses =  articleBusinessMapper.selectArticleBusinessList(articleid);
            for (ArticleBusiness articleBusiness : articleBusinesses){
                Rank rank = rankService.selectByRankid(articleBusiness.getBusinessid());
                if(rank == null){
                    continue;
                }
                BaseResp<List<RankAwardRelease>> baseResp1 = rankService.selectRankAward(rank.getRankid());
                if (ResultUtil.isSuccess(baseResp1)){
                    List<RankAwardRelease> rankAwardRelease = baseResp1.getData();
                    List<RankAwardRelease> rkaward = new ArrayList<>();
                    for (RankAwardRelease rankAwardRelease1 : rankAwardRelease){
                        rkaward.add(rankAwardRelease1);
                        break;
                    }
                    rank.setRankAwards(rkaward);
                }
                articleBusiness.setRank(rank);
            }
            article.setArticleBusinesses(articleBusinesses);
            baseResp = BaseResp.ok();
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

    @Override
    public boolean insertArticleBusiness(ArticleBusiness articleBusiness) {
        try {
            articleBusiness.setCreatetime(new Date());
            articleBusiness.setUpdatetime(new Date());
            int res = articleBusinessMapper.insertSelective(articleBusiness);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("insert articleBusiness is error:{}",e);
        }
        return false;
    }

    @Override
    public BaseResp<List<ArticleBusiness>> selectArticleBusinessList(String articleid) {
        BaseResp<List<ArticleBusiness>> baseResp = new BaseResp<>();
        List<ArticleBusiness> businessList = new ArrayList<>();
        try {
            businessList = articleBusinessMapper.selectArticleBusinessList(articleid);
            baseResp.setData(businessList);
        } catch (Exception e) {
            logger.error("select articleBusiness list is error:{}",e);
        }
        return baseResp;
    }

    @Override
    public boolean deleteArticleBusinessByArticleId(String articleid) {
        try {
            int res = articleBusinessMapper.deleteArticleBusinessByArticleId(articleid);
            if (res > 0){
                return true;
            }
        } catch (Exception e) {
            logger.error("update article is error:{}",e);
        }
        return false;
    }

}
