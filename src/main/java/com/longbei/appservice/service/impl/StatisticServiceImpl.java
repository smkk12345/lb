package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.TimeLineDetailDao;
import com.longbei.appservice.entity.TimeLineDetail;
import com.longbei.appservice.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by wangyongzhi 17/4/17.
 */
@Service("statisticService")
public class StatisticServiceImpl extends BaseServiceImpl implements StatisticService {
    @Autowired
    private TimeLineDetailDao timeLineDetailDao;

    /**
     * 统计用户每天的进步 花 赞
     * @param currentTime
     * @return
     */
    @Override
    public BaseResp<Object> userStatisticImprove(Long currentTime) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
            Date startDate = DateUtils.getDateStart(DateUtils.getBeforeDay(new Date(),1));
            Date endDate = DateUtils.getDateEnd(startDate);
            //查询用户发的进步数量
            List<Object> userImproveList = this.timeLineDetailDao.getUserImproveStatistic(startDate,endDate);

        }catch(Exception e){

        }

        return null;
    }
}
