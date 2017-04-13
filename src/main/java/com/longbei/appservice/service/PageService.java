package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomeRecommend;

import java.util.List;

/**
 * 页面相关操作
 *
 * @author luye
 * @create 2017-03-22 下午4:47
 **/
public interface PageService {

    /**
     * 添加轮播图
     * @param homePicture
     * @return
     * @author luye
     */
    BaseResp<Object> insertHomePage(HomePicture homePicture);

    /**
     * 编辑轮播图
     * @param homePicture
     * @return
     * @author luye
     */
    BaseResp<Object> udpateHomePage(HomePicture homePicture);


    /**
     * 查询轮播图详情
     * @param id
     * @return
     * @author luye
     */
    BaseResp<HomePicture> selectHomePageDetail(Integer id);


    /**
     * 查询轮播图列表
     * @param homePicture
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    BaseResp<Page<HomePicture>> selectHomePageList(HomePicture homePicture,Integer pageno,Integer pagesize);


    /**
     * 添加首页推荐
     * @param homeRecommend
     * @return
     */
    BaseResp<Object> insertHomeRecommend(HomeRecommend homeRecommend);


    /**
     * 获取首页推荐列表（分页）pc
     * @param homeRecommend
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<HomeRecommend>> selectHomeRecommendList(HomeRecommend homeRecommend,Integer pageno,Integer pagesize);


    /**
     * 获取首页推荐列表（分页）app
     * @param startno
     * @param pagesize
     * @return
     */
    BaseResp<List<HomeRecommend>> selectHomeRecommendList(Integer startno, Integer pagesize);


    /**
     * 更新首页推荐
     * @param homeRecommend
     * @return
     */
    BaseResp<Object> updateHomeRecommend(HomeRecommend homeRecommend);








}
