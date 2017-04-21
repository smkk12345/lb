package com.longbei.appservice.service;

import java.util.Map;

import com.longbei.appservice.common.BaseResp;
import com.longbei.pay.weixin.res.ResponseHandler;

public interface PayService {
	
	/**
	 * 微信支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	 BaseResp<Object> wxPayMainPage(Long userid, String orderid);
	 
	 
	 /**
	 * 支付宝支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	 BaseResp<Object> signature(Long userid, String orderid);
	 
	 /**
	 * 支付宝支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月20日
	 */
	 BaseResp<Object> verifyali(Long userid, String orderType, Map<String, String> resMap);
	 
	 /**
	 * 微信支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月21日
	 */
	 BaseResp<Object> verifywx(Long userid, String orderType, String price, ResponseHandler resHandler);
	
	 void testWx();
}
