package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Statistics;

/**
 * Created by wangyongzhi 17/4/17.
 */
public interface StatisticService extends BaseService {

    /**
     * 定时任务－统计用户每天的进步 花 赞
     * @param currentTime
     * @return
     */
    BaseResp<Object> userStatisticImprove(Long currentTime);

    /**
     * 定时任务－每日统计数据: 当日注册数、签到数、点赞数、赠花数、新增微进步数、充值金额／元、未消耗龙币数、未消耗进步币数
     */
    BaseResp<Object> sysDailyStatistic();

    /**
     * 查询每日统计数据
     * @return
     */
    BaseResp<Statistics> selectStatistics();
}
