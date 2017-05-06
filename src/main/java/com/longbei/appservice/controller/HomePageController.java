package com.longbei.appservice.controller;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页
 *
 * @author luye
 * @create 2017-04-26 下午3:10
 **/
@RestController
@RequestMapping(value = "home")
public class HomePageController {

    private static Logger logger = LoggerFactory.getLogger(HomePageController.class);

    @Autowired
    private PageService pageService;

    /**
     * 获取首页轮播图
     * @url http://ip:port/app_service/home/homepics
     * 轮播图类型 0 - 首页轮播图。1 - 商城轮播图
     * @param userid 用户id
     * @return
     */
    @RequestMapping(value = "homepics")
    public BaseResp<List<HomePicture>> getHomePicList(String userid){
        logger.info("user userid={} get home pic list",userid);
        BaseResp<List<HomePicture>> baseResp = new BaseResp<>();
        try {
            String type = "0";
            baseResp = pageService.selectHomePicList(type);
        } catch (Exception e) {
            logger.error("user userid={} get home pic list is error:",userid,e
            );
        }
        return baseResp;
    }

    /**
     * 获取商城轮播图
     * @url http://ip:port/app_service/home/productpics
     * 轮播图类型 0 - 首页轮播图。1 - 商城轮播图
     * @param userid 用户id
     * @return
     */
    @RequestMapping(value = "productpics")
    public BaseResp<List<HomePicture>> getProductPicList(String userid){
        logger.info("user userid={} get product pic list",userid);
        BaseResp<List<HomePicture>> baseResp = new BaseResp<>();
        try {
            String type ="1";
            baseResp = pageService.selectHomePicList(type);
        } catch (Exception e) {
            logger.error("user userid={} get product pic list is error:",userid,e
            );
        }
        return baseResp;
    }

    /**
     * 获取一键发布背景图
     * @param userid 用户id
     * @return
     */
    @RequestMapping(value = "publishbg")
    public BaseResp<String> getPublishBg(String userid){
        logger.info("user userid={} get publishbg",userid);
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.selectPublishBg();
        } catch (Exception e) {
            logger.error("user userid={} get publishbg is error:",userid,e);
        }
        return baseResp;
    }




}
