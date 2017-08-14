package com.longbei.appservice.dao;

import com.longbei.appservice.entity.MediaResourceDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangyongzhi 17/8/11.
 */
public interface MediaResourceDetailMapper {

    /**
     * 批量插入资源库 详情列表
     * @param mediaResourceDetailList
     * @return
     */
    int batchInsertMediaResourceDetail(List<MediaResourceDetail> mediaResourceDetailList);

    /**
     * 获取资源详情列表
     * @param mediaresourceid
     * @return
     */
    List<String> findMediaResourceDetailList(@Param("mediaresourceid") Integer mediaresourceid);
}
