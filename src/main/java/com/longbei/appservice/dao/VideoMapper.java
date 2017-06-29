package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Video;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/6/28.
 */
public interface VideoMapper {

    /**
     * 获取视频列表
     * @param map
     * @return
     */
    List<Video> getVideoList(Map<String, Object> map);

    /**
     * 获取视频详情
     * @param videoId
     * @return
     */
    Video getVideo(Integer videoId);

    /**
     * 添加视频
     * @param video
     * @return
     */
    int addVideo(Video video);

    /**
     * 更新视频
     * @param video
     * @return
     */
    int updateVideo(Video video);

    /**
     * 删除视频
     * @param id
     * @return
     */
    int deleteVideo(Integer id);
}
