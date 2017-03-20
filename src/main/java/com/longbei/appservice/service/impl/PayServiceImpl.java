package com.longbei.appservice.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.service.PayService;
import com.longbei.appservice.service.api.HttpClient;

@Service("payService")
public class PayServiceImpl implements PayService {
	
	
	private static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

	
	/**
	 * 微信支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	@Override
	public BaseResp<Object> wxPayMainPage(Long userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.wxPayMainPage(orderid);
			//购买成功后，添加龙币----
			
		}catch (Exception e){
			logger.error("wxPayMainPage orderid = {}", orderid, e);
		}
		return baseResp;
	}

	
	
	/**
	 * 支付宝支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	@Override
	public BaseResp<Object> signature(Long userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.signature(userid, orderid);
		}catch (Exception e){
			logger.error("signature userid = {}, orderid = {}", userid, orderid, e);
		}
		return baseResp;
	}


	/**
	 * 支付宝支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月20日
	 */
	@Override
	public BaseResp<Object> ali(Long userid, String orderType, Map<String, String> resMap) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.ali(orderType, resMap);
			//购买成功后，添加龙币----
			if (ResultUtil.isSuccess(baseResp)){
				
	        }
		}catch (Exception e){
			logger.error("ali userid = {}, orderType = {}", userid, orderType, e);
		}
		return baseResp;
	}

}
