package com.longbei.appservice.service;

/**
 * Created by wangyongzhi 17/3/2.
 */
public interface CircleMemberService {

    /**
     * 修改用户的某个兴趣圈中的总赞数以及获得的花(在原来的基础上增加)
     * @param userid 用户id
     * @param circleId 圈子id
     * @param likes 增加的赞数
     * @param flowers 增加的花 数量
     * @param diamonds 增加的 钻石 数量
     */
    boolean updateCircleMemberInfo(Long userId, String circleId, Integer likes, Integer flowers,Integer diamonds);
}
