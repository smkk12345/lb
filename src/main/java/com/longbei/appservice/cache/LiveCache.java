package com.longbei.appservice.cache;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.RedisCacheNames;
import com.longbei.appservice.dao.LiveGiftMapper;
import com.longbei.appservice.entity.LiveGift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixb on 2017/9/7.
 */
@Service
public class LiveCache {

    private static Logger logger = LoggerFactory.getLogger(LiveCache.class);

    @Autowired
    LiveGiftMapper liveGiftMapper;

    @Cacheable(cacheNames = RedisCacheNames._LIVE,key="'selectList'")
    public List<LiveGift> selectList(Integer startNum, Integer endNum) {
        List<LiveGift> list = new ArrayList<>();
        try{
            list = liveGiftMapper.selectList(startNum,endNum);
        }catch (Exception e){
            logger.error("selectList error and startNum={},endNum={}",startNum,endNum,e);
            throw e;
        }
        return list;
    }
}
