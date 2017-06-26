package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomeRecommend;
import com.longbei.appservice.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 页面管理
 *
 * @author luye
 * @create 2017-03-22 下午4:45
 **/
@RestController
@RequestMapping(value = "/api/page")
public class PageApiController {

    private static Logger logger = LoggerFactory.getLogger(PageApiController.class);

    @Autowired
    private PageService pageService;

    /**
     * 添加轮播图
     * @param homePicture
     * @return
     * @author luye
     */
    @RequestMapping(value = "inserthomepic")
    public BaseResp<Object> insertHomePage(@RequestBody HomePicture homePicture){
        logger.info("homePicture:{}", JSON.toJSONString(homePicture));
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.insertHomePage(homePicture);
        } catch (Exception e) {
            logger.error("insert home pic is error:",e);
        }
        return baseResp;
    }

    /**
     * 编辑轮播图
     * @param homePicture
     * @return
     * @author luye
     */
    @RequestMapping(value = "udpatehomepic")
    public BaseResp<Object> udpateHomePage(@RequestBody HomePicture homePicture){
        logger.info("homePicture:{}", JSON.toJSONString(homePicture));
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.udpateHomePage(homePicture);
        } catch (Exception e) {
            logger.error("update home pic is error:",e);
        }
        return baseResp;
    }

    /**
     * 查询轮播图详情
     * @param id
     * @return
     * @author luye
     */
    @RequestMapping(value = "homepicdetail/{id}")
    public BaseResp<HomePicture> selectHomePageDetail(@PathVariable("id") String id){
        logger.info("id={}", id);
        BaseResp<HomePicture> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(id)){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = pageService.selectHomePageDetail(Integer.parseInt(id));
        } catch (Exception e) {
            logger.error("select home pic id={} is error:",id,e);
        }
        return baseResp;
    }

    /**
     * 查询轮播图列表
     * @param homePicture
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    @RequestMapping(value = "homepiclist")
    public BaseResp<Page<HomePicture>> selectHomePageList(@RequestBody HomePicture homePicture,
                                                          String pageno, String pagesize){
        logger.info("homePicture:{},pageno={},pagesize={}", JSON.toJSONString(homePicture),pageno,pagesize);
        BaseResp<Page<HomePicture>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = pageService.selectHomePageList(homePicture,Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select home pic list is error:",e);
        }
        return baseResp;
    }

    /**
     * 添加首页推荐
     * @param homeRecommend
     * @return
     */
    @RequestMapping(value = "insertrecommend")
    public BaseResp<Object> insertHomeRecommend(@RequestBody HomeRecommend homeRecommend){
        logger.info("homeRecommend:{}", JSON.toJSONString(homeRecommend));
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == homeRecommend){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = pageService.insertHomeRecommend(homeRecommend);
        } catch (Exception e) {
            logger.error("insert into homerecommend is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取首页推荐列表（分页）pc
     * @param homeRecommend
     * @param pageno
     * @param pagesize
     * @return
     */
    @RequestMapping(value = "recommendlist")
    public BaseResp<Page<HomeRecommend>> selectHomeRecommendList(@RequestBody HomeRecommend homeRecommend,
                                                          String pageno,String pagesize){
        logger.info("homeRecommend:{},pageno={},pagesize={}", JSON.toJSONString(homeRecommend),pageno,pagesize);
        BaseResp<Page<HomeRecommend>> baseResp = new BaseResp<>();
        if (StringUtils.isBlank(pageno)){
            pageno = "1";
        }
        if (StringUtils.isBlank(pagesize)){
            pagesize = Constant.DEFAULT_PAGE_SIZE;
        }
        try {
            baseResp = pageService.selectHomeRecommendList(homeRecommend,Integer.parseInt(pageno),Integer.parseInt(pagesize));
        } catch (Exception e) {
            logger.error("select home recommend list is error:",e);
        }
        return baseResp;
    }

    /**
     * 更新首页推荐
     * @param homeRecommend
     * @return
     */
    @RequestMapping(value = "updaterecommend")
    public BaseResp<Object> updateHomeRecommend(@RequestBody HomeRecommend homeRecommend){
        logger.info("homeRecommend:{}", JSON.toJSONString(homeRecommend));
        BaseResp<Object> baseResp = new BaseResp<>();
        if (null == homeRecommend){
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = pageService.updateHomeRecommend(homeRecommend);
        } catch (Exception e) {
            logger.error("update into homerecommend is error:",e);
        }
        return baseResp;
    }

    /**
     * 保存或更新一键发布背景图
     * @param pic
     * @return
     */
    @RequestMapping("updatepublishbg")
    public BaseResp<Object> saveOrUpdatePublishBg(String pic){
        logger.info("pic={}",pic);
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.saveOrUpdatePublishBg(pic);
        } catch (Exception e) {
            logger.error("save or update publishbg is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取一键发布背景图
     * @return
     */
    @RequestMapping("publishbgdetail")
    public BaseResp<String> selectPublishBg(){
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.selectPublishBg();
        } catch (Exception e) {
            logger.error("select publishbg is error:",e);
        }
        return baseResp;
    }

    /**
     * 保存或更新注册协议
     * @param regpro
     * @return
     */
    @RequestMapping("updateregpro")
    public BaseResp<Object> saveOrUpdateRegisterProtocol(String regpro){
        logger.info("regpro={}",regpro);
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.saveOrUpdateRegisterProtocol(regpro);
        } catch (Exception e) {
            logger.error("save or update register protocol is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取注册协议
     * @return
     */
    @RequestMapping("regprodetail")
    public BaseResp<String> selectRegisterProtocol(){
        BaseResp<String> baseResp = new BaseResp<>();
        try {
            baseResp = pageService.selectRegisterProtocol();
        } catch (Exception e) {
            logger.error("select register protocol is error:",e);
        }
        return baseResp;
    }
}
