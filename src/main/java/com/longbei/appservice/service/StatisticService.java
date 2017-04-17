package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * Created by wangyongzhi 17/4/17.
 */
public interface StatisticService extends BaseService {

    /**
     * 统计用户每天的进步 花 赞
     * @param currentTime
     * @return
     */
    BaseResp<Object> userStatisticImprove(Long currentTime);
}
