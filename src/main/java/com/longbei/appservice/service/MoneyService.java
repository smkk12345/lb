package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * 钱相关
 *
 * @author luye
 * @create 2017-02-20 下午4:19
 **/
public interface MoneyService {

    /**
     * 龙币是否足够
     * @param userid
     * @param consumenum
     * @return
     */
    BaseResp isEnoughLongMoney(String userid,int consumenum);

}
