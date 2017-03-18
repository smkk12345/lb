package com.longbei.appservice.service;

/**
 * Created by wangyongzhi 17/3/7.
 */
public interface FansService extends BaseService {

    /**
     * 校验fansId是否关注了userId
     * @param userId
     * @param fansId
     * @return
     */
    boolean checkIsFans(Long userId,Long fansId);
}
