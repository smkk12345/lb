package com.longbei.appservice.dao;

import com.longbei.appservice.entity.MediaResourceType;

import java.util.List;

/**
 * Created by wangyongzhi 17/7/27.
 */
public interface MediaResourceTypeMapper {

    /**
     * 查询资源库分类列表
     * @return
     */
    List<MediaResourceType> findMediaResourceTypeList();
}
