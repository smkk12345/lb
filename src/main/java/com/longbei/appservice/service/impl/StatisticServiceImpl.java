package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.RedisDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.ImpAllDetailMapper;
import com.longbei.appservice.dao.TimeLineDetailDao;
import com.longbei.appservice.dao.UserImproveStatisticMapper;
import com.longbei.appservice.entity.ImpAllDetail;
import com.longbei.appservice.entity.UserImproveStatistic;
import com.longbei.appservice.service.StatisticService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangyongzhi 17/4/17.
 */
@Service("statisticService")
public class StatisticServiceImpl extends BaseServiceImpl implements StatisticService {
    @Autowired
    private TimeLineDetailDao timeLineDetailDao;
    @Autowired
    private UserImproveStatisticMapper userImproveStatisticMapper;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private ImpAllDetailMapper impAllDetailMapper;

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
            String currentDay = DateUtils.formatDate(startDate);
            //查询用户发的进步数量
            List<UserImproveStatistic> userImproveList = this.timeLineDetailDao.getUserImproveStatistic(startDate,endDate);
            //批量 存入数据库
            if(userImproveList.size() > 0){
                int row=this.userImproveStatisticMapper.batchInsertUserImproveStatistic(userImproveList);
                for(UserImproveStatistic userImproveStatistic:userImproveList){
                    String key = userImproveStatistic.getUserid()+"_"+userImproveStatistic.getCurrentday()+"_"+"statisticImprove";
                    this.redisDao.set(key,"1",2*60*60);//用户的统计存放两个小时,标识该用户已经有当天的统计数据了
                }
            }
            List<UserImproveStatistic> insertUserImproveStatisticList = new ArrayList<UserImproveStatistic>();
            //查询当天的用户点赞统计
            List<ImpAllDetail> improveAllDetailList = this.impAllDetailMapper.userStatistic(startDate,endDate);
            if(improveAllDetailList != null && improveAllDetailList.size() > 0){
                int listSize = improveAllDetailList.size();
                for(int i=0;i<listSize;i++){
                    ImpAllDetail impAllDetail = improveAllDetailList.get(i);
                    ImpAllDetail nextAllDetail = null;
                    if((i+1) < listSize && impAllDetail.getUserid().equals(improveAllDetailList.get(i+1).getUserid())){
                        nextAllDetail = improveAllDetailList.get(i+1);
                    }
                    //判断key是否已经存在
                    String key = impAllDetail.getUserid()+"_"+currentDay+"_"+"statisticImprove";
                    if(this.redisDao.exists(key)){
                        //update
                        Map<String,Object> map = new HashMap<String,Object>();
                        map.put("userid",impAllDetail.getUserid());
                        map.put("currentday",currentDay);
                        if("0".equals(impAllDetail.getDetailtype())){//点赞
                            map.put("likecount",impAllDetail.getGiftnum());
                            if(nextAllDetail != null){
                                map.put("flowercount",nextAllDetail.getGiftnum());
                            }
                        }else if("1".equals(impAllDetail.getDetailtype())){
                            map.put("flowercount",impAllDetail.getGiftnum());
                        }
                        int updateRow = this.userImproveStatisticMapper.updateUserImproveStatic(map);
                    }else{
                        //insert
                        UserImproveStatistic userImproveStatistic = new UserImproveStatistic();
                        userImproveStatistic.setUserid(impAllDetail.getUserid());
                        userImproveStatistic.setCurrentday(currentDay);
                        if("0".equals(impAllDetail.getDetailtype())){//点赞
                            userImproveStatistic.setLikecount(Integer.parseInt(impAllDetail.getGiftnum()));
                            if(nextAllDetail != null){
                                userImproveStatistic.setFlowercount(Integer.parseInt(nextAllDetail.getGiftnum()));
                            }
                        }else if("1".equals(impAllDetail.getDetailtype())){
                            userImproveStatistic.setFlowercount(Integer.parseInt(impAllDetail.getGiftnum()));
                        }
                        insertUserImproveStatisticList.add(userImproveStatistic);
                    }

                    if(nextAllDetail != null){
                        i++;
                    }
                }
                if(insertUserImproveStatisticList.size() > 0){
                    this.userImproveStatisticMapper.batchInsertUserImproveStatistic(insertUserImproveStatisticList);
                }
            }
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            e.printStackTrace();
        }

        return baseResp;
    }
}
