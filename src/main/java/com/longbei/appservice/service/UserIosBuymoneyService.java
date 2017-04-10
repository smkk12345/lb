package com.longbei.appservice.service;

import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserIosBuymoney;

public interface UserIosBuymoneyService {

	/**
    * @Title: selectMoneyAllList 
    * @Description: IOS购买龙币菜单
    * @param @return    设定文件 
    * @return BaseResp<List<UserIosBuymoney>>    返回类型
     */
	BaseResp<List<UserIosBuymoney>> selectMoneyAllList();

	BaseResp<Map<String,String>> buyIOSMoney(long userid, String productid, String payloadData);
	
}
