package com.longbei.appservice.dao;

import com.longbei.appservice.entity.MediaResourceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wangyongzhi 17/7/27.
 */
public interface MediaResourceTypeMapper {

    /**
     * 查询资源库分类列表
     * @return
     */
    List<MediaResourceType> findMediaResourceTypeList(@Param("userid")Long userid);

    /**
     * 添加媒体资源库分类
     * @param mediaResourceType
     * @return
     */
    int addMediaResourceType(@Param("mediaResourceType") MediaResourceType mediaResourceType);

    /**
     * 更新媒体资源库
     * @param mediaResourceType
     * @return
     */
    int updateMediaResourceType(@Param("mediaResourceType") MediaResourceType mediaResourceType);

    /**
     * 获取媒体资源库分类的类型数量
     * @param userid
     * @return
     */
    int getUserMediaResourceTypeCount(@Param("userid")String userid);

    /**
     * 删除媒体资源库分类
     * @param id
     * @param userid
     * @return
     */
    int deleteMediaResourceType(@Param("id")Integer id,@Param("userid") String userid);

    /**
     * 更新媒体资源库分类下的资源数量
     * @param resourcetypeid
     * @param count
     * @return
     */
    int updateMediaResourceTypeCount(@Param("resourcetypeid")Integer resourcetypeid,@Param("count") int count);
}
