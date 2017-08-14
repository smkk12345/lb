package com.longbei.appservice.dao;

import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.entity.MediaResourceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/7/27.
 */
public interface MediaResourceMapper {

    int findMediaResourceCount(@Param("mediaResource") MediaResource mediaResource,@Param("istranscoding") String istranscoding);

    /**
     * 查询资源库列表
     * @param mediaResource
     * @param startno
     * @param pagesize
     * @return
     */
    List<MediaResource> findMediaResourceList(@Param("mediaResource")MediaResource mediaResource,@Param("istranscoding")String istranscoding,@Param("startno") Integer startno,@Param("pagesize") Integer pagesize);

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

    /**
     * 更新文件资料
     * @param map
     * @return
     */
    int updateMedia(Map<String, Object> map);

    /**
     * 将资源分类mediaResourceTypeId下的资源分类id都改成空
     * @param mediaResourceTypeId
     * @return
     */
    int updateMediaResourceTypeIsNull(@Param("mediaResourceTypeId") Integer mediaResourceTypeId);
}
