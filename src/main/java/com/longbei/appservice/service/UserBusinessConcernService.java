package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * Created by wangyongzhi 17/3/21.
 */
public interface UserBusinessConcernService extends BaseService{

    /**
     * 增加关注
     * @param userid
     * @param businessType
     * @param businessId
     * @return
     */
    BaseResp<Object> insertUserBusinessConcern(Long userid, Integer businessType, Long businessId);

    /**
     * 取消关注
     * @param userid
     * @param businessType
     * @param businessId
     * @return
     */
    BaseResp<Object> deleteUserBusinessConcern(Long userid, Integer businessType, Long businessId);
}
