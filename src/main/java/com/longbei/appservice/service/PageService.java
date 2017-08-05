package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.HomePicture;
import com.longbei.appservice.entity.HomePoster;
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
     * 查询首页---启动页列表
     * @param homePoster
     * @param pageno
     * @param pagesize
     * @author yinxc
     */
    BaseResp<Page<HomePoster>> homeposterlist(HomePoster homePoster,Integer pageno,Integer pagesize);
    
    /**
     * 添加首页---启动页
     * @param homePoster
     * @author yinxc
     */
    BaseResp<Object> insertHomePoster(HomePoster homePoster);
    
    /**
     * 修改首页---启动页
     * @param homePoster
     * @author yinxc
     */
    BaseResp<Object> editPoster(HomePoster homePoster);
    
    /**
     * 查询首页---启动页信息
     * @author yinxc
     */
    BaseResp<HomePoster> selectHomePoster(String id);
    
    
    /**
     * 启动页上下线修改
     * @author yinxc
     */
    BaseResp<Object> updateIsup(String isup, String id);
    
    /**
     * 删除启动页
     * @author yinxc
     */
    BaseResp<Object> updateIsdel(String id);



    /**
     * 查询轮播图列表(app)
     * @return
     * @author luye
     */
    BaseResp<List<HomePicture>> selectHomePicList(String type);


    /**
     * 添加首页推荐
     * @param homeRecommend
     * @return
     * @author luye
     */
    BaseResp<Object> insertHomeRecommend(HomeRecommend homeRecommend);


    /**
     * 获取首页推荐列表（分页）pc
     * @param homeRecommend
     * @param pageno
     * @param pagesize
     * @return
     * @author luye
     */
    BaseResp<Page<HomeRecommend>> selectHomeRecommendList(HomeRecommend homeRecommend,Integer pageno,Integer pagesize);


    /**
     * 获取首页推荐列表（分页）app
     * @param startno
     * @param pagesize
     * @return
     * @author luye
     */
    BaseResp<List<HomeRecommend>> selectHomeRecommendList(Integer startno, Integer pagesize);


    /**
     * 更新首页推荐
     * @param homeRecommend
     * @return
     * @author luye
     */
    BaseResp<Object> updateHomeRecommend(HomeRecommend homeRecommend);


    /**
     * 保存或更新一键发布背景图
     * @param pickey
     * @return
     */
    BaseResp<Object> saveOrUpdatePublishBg(String pickey);

    /**
     * 获取一键发布背景图
     * @return
     */
    BaseResp<String> selectPublishBg();

    /**
     * 保存或更新注册协议
     * @param regPro
     * @return
     */
    BaseResp<Object> saveOrUpdateRegisterProtocol(String regPro);

    /**
     * 获取注册协议
     * @return
     */
    BaseResp<String> selectRegisterProtocol();


}
