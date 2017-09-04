package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserBusinessConcern;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/3/21.
 */
public interface UserBusinessConcernMapper {

    /**
     * 添加关注
     * @param userBusinessConcern
     * @return
     */
    int insertUserBusinessConcern(UserBusinessConcern userBusinessConcern);

    /**
     * 删除关注数据
     * @param userid
     * @param businessType
     * @param businessId
     * @return
     */
    int deleteUserBusinessConcern(@Param("userid") Long userid,@Param("businessType") Integer businessType,@Param("businessId") Long businessId);
    
    /**
     * 删除关注数据
     * @param businessType
     * @param businessId
     * @return
     */
    int deleteBusinessConcern(@Param("businessType") Integer businessType,@Param("businessId") Long businessId);

    /**
     * 查询自己的关注列表
     * @param map
     * @return
     */
    List<UserBusinessConcern> findUserBusinessConcernList(Map<String,Object> map);

    /**
     * 查询关注的用户列表
     * @param paraMap
     * @return
     */
    List<UserBusinessConcern> findConcernUserList(Map<String, Object> paraMap);
    
}
