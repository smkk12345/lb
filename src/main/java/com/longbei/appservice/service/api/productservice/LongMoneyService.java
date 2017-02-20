package com.longbei.appservice.service.api.productservice;

import com.longbei.appservice.common.BaseResp;

/**
 * 龙币相关操作
 *
 * @author luye
 * @create 2017-02-20 下午4:06
 **/
public interface LongMoneyService {

    /**
     * 龙币消耗
     * @param userid  用户id
     * @param count   消耗数量
     * @param consumetype  消耗类型  0 -- 买花 1--买钻
     * @return
     */
    BaseResp<Object> consumeLongMoney(String userid,int count,int consumetype);


}
