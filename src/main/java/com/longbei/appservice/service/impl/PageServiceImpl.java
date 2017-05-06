package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.HomePictureMapper;
import com.longbei.appservice.dao.HomeRecommendMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.SysCommonMapper;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomeRecommend;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.SysCommon;
import com.longbei.appservice.service.PageService;
import com.longbei.appservice.service.RankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 页面相关操作
 *
 * @author luye
 * @create 2017-03-22 下午4:55
 **/
@Service
public class PageServiceImpl implements PageService{

    private static Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private HomePictureMapper homePictureMapper;
    @Autowired
    private HomeRecommendMapper homeRecommendMapper;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private SysCommonMapper sysCommonMapper;

    @Override
    public BaseResp<Object> insertHomePage(HomePicture homePicture) {
        BaseResp baseResp = new BaseResp();
        try {
            homePicture.setCreatetime(new Date());
            int res = homePictureMapper.insertSelective(homePicture);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("insert into homepicture is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> udpateHomePage(HomePicture homePicture) {
        BaseResp baseResp = new BaseResp();
        try {
            if ("1".equals(homePicture.getIsup())){
                homePicture.setUptime(new Date());
            }
            if ("0".equals(homePicture.getIsup())){
                homePicture.setDowntime(new Date());
            }
            homePicture.setUpdatetime(new Date());
            int res = homePictureMapper.updateByPrimaryKeySelective(homePicture);
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update homepicture is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<HomePicture> selectHomePageDetail(Integer id) {
        BaseResp<HomePicture> baseResp = new BaseResp();
        try {
            HomePicture homePicture = homePictureMapper.selectByPrimaryKey(id);
            baseResp = BaseResp.ok();
            baseResp.setData(homePicture);
        } catch (Exception e) {
            logger.error("select homepicture is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Page<HomePicture>> selectHomePageList(HomePicture homePicture, Integer pageno, Integer pagesize) {

        BaseResp<Page<HomePicture>> baseResp = new BaseResp<>();
        Page<HomePicture> page = new Page<>(pageno,pagesize);

        try {
            int totalcount = homePictureMapper.selectCount(homePicture);
            List<HomePicture> homePictures = homePictureMapper.selectList(homePicture,(pageno-1)*pagesize,pagesize);
            baseResp = BaseResp.ok();
            page.setTotalCount(totalcount);
            page.setList(homePictures);
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select home pic list is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<List<HomePicture>> selectHomePicList(String type) {
        BaseResp<List<HomePicture>> baseResp = new BaseResp<>();
        try {
            HomePicture homePicture = new HomePicture();
            homePicture.setIsup("1");
            homePicture.setType(type);
            List<HomePicture> homePictures = homePictureMapper.selectList(homePicture,null,null);
            baseResp = BaseResp.ok();
            baseResp.setData(homePictures);
        } catch (Exception e) {
            logger.error("select home pic list for app is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertHomeRecommend(HomeRecommend homeRecommend) {
        BaseResp baseResp = new BaseResp();
        try {
            homeRecommend.setCreatetime(new Date());
            homeRecommendMapper.insertBantch(homeRecommend);
            List<Long> list = homeRecommend.getBusinessids();
            if (null != list && list.size()!=0){
                for (Long lo: list){
                    Rank rank = new Rank();
                    rank.setRankid(lo);
                    rank.setIshomerecommend("1");
                    rankMapper.updateSymbolByRankId(rank);
                }
            }
            baseResp = BaseResp.ok();
        } catch (Exception e) {
            logger.error("insert into homerecommend is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Page<HomeRecommend>> selectHomeRecommendList(HomeRecommend homeRecommend,
                                                                 Integer pageno, Integer pagesize) {
        BaseResp<Page<HomeRecommend>> baseResp = new BaseResp<>();
        Page<HomeRecommend> page = new Page<>(pageno,pagesize);

        try {
            int totalcount = homeRecommendMapper.selectCount(homeRecommend);
            List<HomeRecommend> homeRecommends = homeRecommendMapper.selectList(homeRecommend,pagesize*(pageno-1),pagesize);
            for (HomeRecommend homeRecommend1 : homeRecommends){
                Rank rank = rankMapper.selectRankByRankid(homeRecommend1.getBusinessid());
                homeRecommend1.setRank(rank);
            }
            page.setTotalCount(totalcount);
            page.setList(homeRecommends);
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select home recommend list is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<HomeRecommend>> selectHomeRecommendList(Integer startno, Integer pagesize) {
        BaseResp<List<HomeRecommend>> baseResp = new BaseResp<>();
        HomeRecommend homeRecommend = new HomeRecommend();
        try {
            List<HomeRecommend> homeRecommends = homeRecommendMapper.selectList(homeRecommend,startno,pagesize);
            for (HomeRecommend homeRecommend1 : homeRecommends){
                Rank rank = rankMapper.selectRankByRankid(homeRecommend1.getBusinessid());
                homeRecommend1.setRank(rank);
            }
            baseResp = BaseResp.ok();
            baseResp.setData(homeRecommends);
        } catch (Exception e) {
            logger.error("select homeRecmment list is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateHomeRecommend(HomeRecommend homeRecommend) {
        BaseResp baseResp = new BaseResp();
        try {
            int res = homeRecommendMapper.updateByPrimaryKeySelective(homeRecommend);
            if (res > 0){
                if ("1".equals(homeRecommend.getIsdel())){
                    Rank rank = new Rank();
                    rank.setRankid(homeRecommend.getBusinessid());
                    rank.setIshomerecommend("0");
                    rankMapper.updateSymbolByRankId(rank);
                }
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("update homerecommend is error:",e);
        }
        return baseResp;
    }


    @Override
    public BaseResp<Object> saveOrUpdatePublishBg(String pickey) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            SysCommon sysCommon = sysCommonMapper.selectByKey(Constant.PUBLISH_BG_KEY);
            SysCommon sysCommon1 = new SysCommon();
            sysCommon1.setKey(Constant.PUBLISH_BG_KEY);
            sysCommon1.setInfo(pickey);
            int res = 0;
            if (null == sysCommon){
                res = sysCommonMapper.insertSelective(sysCommon1);
            } else {
                res = sysCommonMapper.updateByKey(sysCommon1);
            }
            if (res > 0){
                baseResp = BaseResp.ok();
            }
        } catch (Exception e) {
            logger.error("save or update publisbg is error:",e);
        }

        return baseResp;
    }

    @Override
    public BaseResp<String> selectPublishBg() {
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            SysCommon sysCommon = sysCommonMapper.selectByKey(Constant.PUBLISH_BG_KEY);
            if (null != sysCommon){
                baseResp = BaseResp.ok();
                baseResp.setData(sysCommon.getInfo());
            }
        } catch (Exception e) {
            logger.error("select publishbg is error:",e);
        }
        return baseResp;
    }
}
