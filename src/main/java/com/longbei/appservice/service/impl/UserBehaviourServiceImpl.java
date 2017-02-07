package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.redis.SpringJedisDao;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.service.UserBehaviourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by smkk on 17/2/7.
 */
@Service
public class UserBehaviourServiceImpl implements UserBehaviourService {

    @Autowired
    private SpringJedisDao springJedisDao;
    private static Logger logger = LoggerFactory.getLogger(UserBehaviourServiceImpl.class);

    @Override
    public BaseResp<Object> canOperateMore(long userid,UserInfo userInfo, String operateType) {

        BaseResp<Object> baseResp = new BaseResp<>();
        String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        try{
            switch (operateType){
                case Constant.PERDAY_CHECK_IN:
                    int checkIn = getHashValueFromCache(getPerKey(userInfo.getUserid()), dateStr+Constant.PERDAY_CHECK_IN);
                    if (checkIn == 0){//为空,可以签到
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                    }
                    break;
                //发进步
                case Constant.PERDAY_ADD_IMPROVE:
                    int addImproveCount = getHashValueFromCache(getPerKey(userInfo.getUserid()),dateStr+Constant.PERDAY_ADD_IMPROVE);
                    if(addImproveCount < SysRulesCache.sysRules.getMaximprove()){
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                    }
                    break;
                //点赞
                case Constant.PERDAY_ADD_LIKE:
                    int addLikeCount = getHashValueFromCache(getPerKey(userInfo.getUserid()),dateStr+Constant.PERDAY_ADD_LIKE);
                    int maxCount = SysRulesCache.sysRules.getLevellike()*userInfo.getGrade()+
                            SysRulesCache.sysRules.getLimitlike();
                    if(addLikeCount < maxCount){
                        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
                    }
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            logger.error("canOperateMore error userid={},operateType={},msg={}",userid,operateType,e);
        }
        return baseResp;
    }

    private int getHashValueFromCache(String key,String hashKey){
        Object o = springJedisDao.getHashValue(key,hashKey);
        if(null != o){
            try{
                return (int)o;
            }catch(Exception e){
                logger.debug("(int)o msg={}",e);
                return 0;
            }
        }
        return 0;
    }

    @Override
    public BaseResp<Object> levelUp(long userid, int iPoint,int pType) {
        UserInfo userInfo = null;//此处通过id获取用户信息
        return levelUp(userInfo,iPoint,pType);
    }

    /**
     * 是否升级以及升级之后的操作
     * @param userInfo
     * @param iPoint
     * @return
     */
    @Override
    public BaseResp<Object> levelUp(UserInfo userInfo, int iPoint,int pType) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            String dateStr = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
            String key = getPerKey(userInfo.getUserid());
            boolean hasKey = springJedisDao.hasKey(key,dateStr+Constant.PERDAY_POINT);
            if(hasKey){
                int point = getHashValueFromCache(key,dateStr+Constant.PERDAY_POINT);
                int leftPoint = point - iPoint;
                if(point > 0 && leftPoint > 0){//未升级
                    springJedisDao.increment(key,dateStr+Constant.PERDAY_POINT,-iPoint);
                }else{//升级
                    levelUpAsyn(userInfo,iPoint);
                }
            }else{
                int point = userInfo.getPoint();
                int leftPoint = point - iPoint;
                if(point > 0 && leftPoint > 0){
                    springJedisDao.put(key,dateStr+Constant.PERDAY_POINT,leftPoint+"");
                    springJedisDao.expire(key,Constant.CACHE_24X60X60);
                }else{//升级
                    levelUpAsyn(userInfo,iPoint);
                }
            }
            getHashValueFromCache(getPerKey(userInfo.getUserid()),dateStr+Constant.PERDAY_POINT);
        }catch(Exception e){
            logger.error("levelUp error and msg = {}",e);
        }
        return baseResp;
    }

    private String getPerKey(long userid){
        return Constant.RP_USER_PERDAY+userid;
    }
    //异步线程跑升级
    private void levelUpAsyn(UserInfo userInfo,int leftPoint){
        try{
            //等级升级  update UserInfo
            //插入一条 等级升级消息
            //推送一条信息
        }catch (Exception e){
            logger.error("levelUpAsyn error and msg = {}",e);
        }
    }


}
