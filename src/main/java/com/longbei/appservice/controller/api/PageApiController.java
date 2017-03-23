package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.HomePicture;
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

}
