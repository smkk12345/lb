package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserBusinessConcern;

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
    int deleteUserBusinessConcern(Long userid, Integer businessType, Long businessId);
}
