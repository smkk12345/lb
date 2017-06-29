package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Video;
import com.longbei.appservice.entity.VideoClassify;

import java.util.List;

/**
 * Created by wangyongzhi 17/6/28.
 */
public interface VideoService {

    /**
     * 获取视频分类的列表
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<List<VideoClassify>> getVideoClassifyList(Integer startNum, Integer pageSize);

    /**
     * 添加视频分类
     * @param title
     * @param brief
     * @param bannerurl
     * @param styleflag
     * @return
     */
    BaseResp<Object> addVideoClassify(String title, String brief, String bannerurl, Integer styleflag);

    /**
     * 获取视频分类
     * @param id
     * @return
     */
    BaseResp<Object> getVideoClassify(Integer id);

    /**
     * 删除
     * @param id
     * @return
     */
    BaseResp<Object> deleteVideoClassify(Integer id);

    /**
     * 编辑视频分类
     * @param id
     * @param title
     * @param brief
     * @param bannerurl
     * @param styleflag
     * @return
     */
    BaseResp<Object> editVideoClassify(Integer id, String title, String brief, String bannerurl, Integer styleflag);

    /**
     * 获取视频列表
     * @param videoClassifyId
     * @param startNum
     * @param pageSize
     * @return
     */
    BaseResp<List<Video>> getVideoList(Integer videoClassifyId, Integer startNum, Integer pageSize);

    /**
     * 获取视频详情
     * @param videoId
     * @return
     */
    BaseResp<Video> getVideo(Integer videoId);

    /**
     * 添加视频
     * @param title
     * @param brief
     * @param screenpthoto
     * @param videourl
     * @param isshow
     * @param sortnum
     * @param videoClassifyId
     * @return
     */
    BaseResp<Object> addVideo(String title, String brief, String screenpthoto, String videourl, Boolean isshow, Integer sortnum, Integer videoClassifyId);

    /**
     * 更新视频
     * @param title
     * @param brief
     * @param screenpthoto
     * @param videourl
     * @param isshow
     * @param sortnum
     * @param videoClassifyId
     * @return
     */
    BaseResp<Object> updateVideo(Integer id,String title, String brief, String screenpthoto, String videourl, Boolean isshow, Integer sortnum, Integer videoClassifyId);

    /**
     * 删除视频
     * @param id
     * @return
     */
    BaseResp<Object> deleteVideo(Integer id);
}
