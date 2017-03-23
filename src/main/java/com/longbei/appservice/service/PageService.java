package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.HomePicture;

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


}
