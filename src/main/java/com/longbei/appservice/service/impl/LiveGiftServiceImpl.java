package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.LiveGiftDetailMapper;
import com.longbei.appservice.dao.LiveGiftMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.LiveGift;
import com.longbei.appservice.entity.LiveGiftDetail;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.LiveGiftService;
import com.longbei.appservice.service.UserMoneyDetailService;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by lixb on 2017/8/11.
 */
@Service
public class LiveGiftServiceImpl implements LiveGiftService {

    @Autowired
    private LiveGiftMapper liveGiftMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private LiveGiftDetailMapper liveGiftDetailMapper;
    @Autowired
    private UserMoneyDetailService userMoneyDetailService;

    private static Logger logger = LoggerFactory.getLogger(LiveGiftServiceImpl.class);

    @Override
    public BaseResp<List<LiveGift>> selectList(Integer startNum, Integer endNum) {
        BaseResp<List<LiveGift>> baseResp = new BaseResp<>();
        try{
            List<LiveGift> list = liveGiftMapper.selectList(startNum,endNum);
            baseResp.setData(list);
            baseResp.initCodeAndDesp();
        }catch (Exception e){
            logger.error("selectList error and startNum={},endNum={}",startNum,endNum,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> giveGift(long giftId, long fromUid, int num, long toUId,
                                     long businessid,String businesstype) {
        BaseResp<Object> baseResp = new BaseResp<>();
        LiveGift liveGift = liveGiftMapper.selectById(giftId);
        UserInfo userInfo = userInfoMapper.selectByUserid(fromUid);
        if(null == liveGift||null == userInfo){
            return baseResp;
        }
        if(hasEnoughMoney(liveGift,userInfo,num)){
            //扣除龙币 生成记录
            //1，兑换礼物 2，送礼物
            //插入一条明细
            int n = insertLiveGiftDetail(fromUid,toUId,num,liveGift,businessid,businesstype);
            if(n > 0){ //String origin, int number, long friendid
                int giveMoney = liveGift.getPrice()*num;
                int receiveMoney = giveMoney*(1-Constant.TAKEPERCENTAGE.intValue()/100);
                userMoneyDetailService.insertPublic(userInfo,"13",-giveMoney,toUId);
                userMoneyDetailService.insertPublic(toUId,"14",receiveMoney,fromUid);
            }
            baseResp.initCodeAndDesp();
            baseResp.setData(userInfo.getTotalmoney()-num*liveGift.getPrice());
        }else {
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_1301, Constant.RTNINFO_SYS_1301);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<LiveGiftDetail>> selectOwnGiftList(long userid, Integer startNum, Integer endNum) {
        return null;
    }

    @Override
    public BaseResp<List<LiveGiftDetail>> selectGiftList(long userid, Integer startNum, Integer endNum) {
        return null;
    }

    /**
     * 判断用户是否有足够的龙币支付礼物
     * @param liveGift
     * @param userInfo
     * @param num
     * @return
     */
    private boolean hasEnoughMoney(LiveGift liveGift,
                                   UserInfo userInfo,
                                   int num){
        if(userInfo.getTotalmoney() > liveGift.getPrice()*num){
            return true;
        }
        return false;
    }


    private int insertLiveGiftDetail(long fromuid,long touid
                                     ,int num,LiveGift liveGift,
                                     Long businessid,
                                     String businesstype){
        int result = 0;
        LiveGiftDetail liveGiftDetail = new LiveGiftDetail(fromuid,
                touid,liveGift.getGiftid(),liveGift.getTitle(),
                num,businessid,businesstype);
        Date date = new Date();
        liveGiftDetail.setCreatetime(date);
        liveGiftDetail.setUpdatetime(date);
        try{
            result = liveGiftDetailMapper.insertGiftDetail(liveGiftDetail);
        }catch (Exception e){
            logger.error("insertLiveGiftDetail error",e);
        }
        return result;
    }



}
