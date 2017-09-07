package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.MediaResource;
import com.longbei.appservice.entity.MediaResourceDetail;
import com.longbei.appservice.entity.MediaResourceType;

import java.util.List;

/**
 * Created by wangyongzhi 17/7/27.
 */
public interface MediaResourceService {
    /**
     * 查询资源分类列表
     * @return
     */
    BaseResp<List<MediaResourceType>> findMediaResourceTypeList(Long userid);

    /**
     * 查询资源列表
     * @param mediaResource mediaResource 查询的条件
     * @param pageno
     * @param pagesize
     * @return
     */
    BaseResp<Page<MediaResource>> findMediaResourceList(MediaResource mediaResource,String istranscoding, Integer pageno, Integer pagesize);

    /**
     * 添加资源库
     * @param mediaResource
     * @return
     */
    BaseResp<Object> addMediaResource(MediaResource mediaResource);

    /**
     * 获取资源库详情
     * @param id
     * @return
     */
    BaseResp<MediaResource> mediaResourceDetail(Integer id);


    /**
     * 出入图片
     * @param mediaResourceDetailList
     */
    void batchInsertMediaResourceDetail(List<MediaResourceDetail> mediaResourceDetailList);

    /**
     * 更新资源库文件
     * @param mediaResource
     * @return
     */
    BaseResp<Object> updateMediaResource(MediaResource mediaResource);

    /**
     * 转码通知 ,修改文件相关信息
     * @param key
     * @param pickey
     * @param workflow
     * @param duration
     * @param picAttribute
     * @return
     */
    BaseResp<Object> updateMediaResourceInfo(String key, String pickey,String fileKey, String workflow, String duration, String picAttribute);

    /**
     * 添加资源分类
     * @param typename
     * @param userid
     * @return
     */
    BaseResp<Object> addMediaResourceType(String typename, String userid);

    /**
     * 更新媒体资源库
     * @param id
     * @param userid
     * @param typename
     * @return
     */
    BaseResp<Object> updateMediaResourceType(Integer id, String userid, String typename);

    /**
     * 校验用户是否可以继续添加媒体资源分类
     * @param userid
     * @return
     */
    BaseResp<Object> checkUserAddMediaResourceType(String userid);

    /**
     * 删除媒体资源库分类
     * @param id
     * @param userid
     * @return
     */
    BaseResp<Object> deleteMediaResourceType(Integer id, String userid);

    /**
     * 获取资源详情列表
     * @param mediaresourceid
     * @param userid
     * @return
     */
    BaseResp<List<String>> findMediaResourceDetailList(Integer mediaresourceid, Long userid);
}
