package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.service.MoneyService;
import org.springframework.stereotype.Service;

/**
 * 钱
 *
 * @author luye
 * @create 2017-02-20 下午4:22
 **/
@Service
public class MoneyServiceImpl implements MoneyService{

    @Override
    public BaseResp isEnoughLongMoney(String userid, int consumenum) {
        return BaseResp.ok();
    }


}
