package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * Created by wangyongzhi on 17/2/28.
 */
public interface CircleService {

    /**
     * 根据圈子名称查询相关圈子
     * @param circleName 圈子名称
     * @return
     */
    BaseResp<Object> relevantCircle(String circleName,Integer startNo,Integer pageSize);

    /**
     * 新建兴趣圈
     * @param circleTitle
     * @param circlephotos
     * @param circlebrief
     * @return
     */
    BaseResp<Object> insertCircle(String userId,String circleTitle, String circlephotos, String circlebrief,
                                  Integer circleinvoloed,String ptype,Boolean ispublic,Boolean needconfirm,Boolean creategoup);

    /**
     * 校验兴趣圈名是否可用
     * @param circleTitle
     * @return
     */
    boolean checkCircleTitle(String circleTitle);
}
