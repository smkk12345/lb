package com.longbei.appservice.controller.api;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.controller.ProductController;
import com.longbei.appservice.entity.Statistics;
import com.longbei.appservice.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangyongzhi 17/4/17.
 */
@RestController
@RequestMapping(value="/api/statistic")
public class StatisticApiController {

    @Autowired
    private StatisticService statisticService;
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);


    /**
     * 定时任务－统计用户的相关信息 进步/赞/花
     * @param currentTime
     * @return
     */
    @RequestMapping(value="userStatisticImprove")
    public BaseResp<Object> userStatisticImprove(Long currentTime){
        logger.info("userStatisticImprove currentTime = {}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.statisticService.userStatisticImprove(currentTime);
        return baseResp;
    }

    /**
     * 定时任务－每日统计数据: 当日注册数、签到数、点赞数、赠花数、新增微进步数、充值金额／元、未消耗龙币数、未消耗进步币数
     */
    @RequestMapping(value = "sysDailyStatistic")
    public BaseResp<Object> sysDailyStatistic(Long currentTime){
        logger.info("sysDailyStatistic currentTime = {}",currentTime);
        BaseResp<Object> baseResp = new BaseResp<>();
        if (currentTime == null) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        baseResp = this.statisticService.sysDailyStatistic();
        return baseResp;
    }

    /**
     * 查询每日统计数据
     */
    @RequestMapping(value = "select")
    public BaseResp<Statistics> selectStatistics(){
        logger.info("select Statistics");
        BaseResp<Statistics> baseResp = statisticService.selectStatistics();
        return baseResp;
    }

    /**
     * 查询进几天的历史记录
     * @param days 天数
     * @return
     */
    @RequestMapping(value = "listByDate")
    public BaseResp<List<Statistics>> listStatisticsForDays(int days){
        logger.info("listStatisticsForDays: select Statistics list for {} days",days);
        BaseResp<List<Statistics>> listResp = statisticService.listStatisticsForDays(days);
        return listResp;
    }

    /**
     * 查询某统计数据的历史总数:总注册用户数、龙榜总数、龙币充值总数
     * @param field 要查询总数的统计数据字段
     * @return
     */
    @RequestMapping(value = "sumByField")
    private BaseResp<Integer> sumByField(String field) {
        logger.info("sumByField field={}",field);
        BaseResp<Integer> baseResp = statisticService.sumByField(field);
        return baseResp;
    }
}
