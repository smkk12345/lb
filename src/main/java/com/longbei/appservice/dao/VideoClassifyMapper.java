package com.longbei.appservice.dao;

import com.longbei.appservice.entity.VideoClassify;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/6/28.
 */
public interface VideoClassifyMapper {

    /**
     * 获取视频分类的列表
     */
    List<VideoClassify> getVideoClassifyList(Map<String, Object> map);

    /**
     * 添加视频分类
     * @param videoClassify
     * @return
     */
    int insertVideoClassify(VideoClassify videoClassify);

    /**
     * 获取视频分类
     * @param id
     * @return
     */
    VideoClassify getVideoClassify(Integer id);

    /**
     * 删除视频分类
     * @param id
     * @return
     */
    int deleteVideoClassify(Integer id);

    /**
     * 编辑视频分类
     * @param videoClassify
     * @return
     */
    int updateVideoClassify(VideoClassify videoClassify);
}
