package com.longbei.appservice.dao;

import com.longbei.appservice.entity.MediaResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangyongzhi 17/7/27.
 */
public interface MediaResourceMapper {

    int findMediaResourceCount(@Param("mediaResource") MediaResource mediaResource);

    /**
     * 查询资源库列表
     * @param mediaResource
     * @param startno
     * @param pagesize
     * @return
     */
    List<MediaResource> findMediaResourceList(@Param("mediaResource")MediaResource mediaResource,@Param("startno") Integer startno,@Param("pagesize") Integer pagesize);

    /**
     * 添加资源数据
     * @param mediaResource
     * @return
     */
    int insertMediaResource(@Param("mediaResource")MediaResource mediaResource);

    /**
     * 获取资源数据详情
     * @param id
     * @return
     */
    MediaResource getMediaResourceDetail(@Param("id")Integer id);

    /**
     * 更新资源库数据
     * @param mediaResource
     * @return
     */
    int updateMediaResource(@Param("mediaResource")MediaResource mediaResource);
}
