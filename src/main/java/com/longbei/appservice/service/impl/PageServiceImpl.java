package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.HomePictureMapper;
import com.longbei.appservice.dao.HomeRecommendMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.SysCommonMapper;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomeRecommend;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.SysCommon;
import com.longbei.appservice.service.PageService;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private SysSettingService sysSettingService;

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
        List<HomePicture> homePictures = new ArrayList<HomePicture>();
        try {
            if(!"2".equals(type)){
                HomePicture homePicture = new HomePicture();
                homePicture.setIsup("1");
                homePicture.setType(type);
                homePictures = homePictureMapper.selectList(homePicture,null,null);
                for (HomePicture homePicture1 : homePictures){
                    if ("0".equals(homePicture1.getContenttype())){
                        homePicture1.setHref(AppserviceConfig.articleurl + "?articleid=" + homePicture1.getHref());
                    }
                }
            }else if("2".equals(type)){
                HomePicture homePicture = new HomePicture();
                homePicture.setPicname("叶圣陶听说读写能力展示活动简介");
                homePicture.setPhotos("d0dded28-d919-4d9c-b836-672344bede9e");
                homePicture.setContenttype("0");
                homePicture.setIsup("1");
                homePicture.setIsdel(0);
                homePicture.setSort("0");
                homePicture.setHref(AppserviceConfig.articleurl + "?articleid=101");

                HomePicture homePicture2 = new HomePicture();
                homePicture2.setPicname("北京公益基金会");
                homePicture2.setPhotos("b8d75484-33d4-4a42-8b24-4f00ad4e902e");
                homePicture2.setContenttype("0");
                homePicture2.setIsup("1");
                homePicture2.setIsdel(0);
                homePicture2.setSort("0");
                homePicture2.setHref(AppserviceConfig.articleurl + "?articleid=102");

                homePictures.add(0,homePicture);
                homePictures.add(1,homePicture2);
            }
            baseResp.setData(homePictures);
            return baseResp.initCodeAndDesp();
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
            List<HomeRecommend> resultlist = new ArrayList<>();
            for (HomeRecommend homeRecommend1 : homeRecommends){
                Rank rank = rankMapper.selectRankByRankid(homeRecommend1.getBusinessid());
                if (null != rank){
                    homeRecommend1.setRank(rank);
                    resultlist.add(homeRecommend1);
                }

            }
            baseResp = BaseResp.ok();
            baseResp.setData(resultlist);
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
            boolean sueecss = sysSettingService.insertSysCommon(Constant.SYS_COMMON_KEYS.publishbg.toString(),pickey, DateUtils.getDate());
            if (sueecss){
                baseResp.initCodeAndDesp();
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
            SysCommon sysCommon = sysSettingService.getSysCommonByKey(Constant.SYS_COMMON_KEYS.publishbg.toString());
            if (null != sysCommon){
                baseResp.initCodeAndDesp();
                baseResp.setData(sysCommon.getInfo());
            }
        } catch (Exception e) {
            logger.error("select publishbg is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> saveOrUpdateRegisterProtocol(String regPro) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            boolean res = sysSettingService.insertSysCommon(Constant.SYS_COMMON_KEYS.regprotocol.toString(),regPro,DateUtils.getDate());
            if (res){
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("save or update register protocol is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<String> selectRegisterProtocol() {
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            SysCommon sysCommon = sysSettingService.getSysCommonByKey(Constant.SYS_COMMON_KEYS.regprotocol.toString());
            if (null != sysCommon){
                baseResp.initCodeAndDesp();
                baseResp.setData(sysCommon.getInfo());
            }
        } catch (Exception e) {
            logger.error("select register protocol is error:",e);
        }
        return baseResp;
    }
}
