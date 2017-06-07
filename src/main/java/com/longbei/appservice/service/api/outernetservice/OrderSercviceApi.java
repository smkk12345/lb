package com.longbei.appservice.service.api.outernetservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longbei.appservice.common.BaseResp;

@FeignClient("outernetServiceWYZ")
@RequestMapping("outernetService")
public interface OrderSercviceApi {

	/**
	 * 微信扫码支付
	 * @param price
	 * @param remark
	 * @param orderid
	 * @param notifyURL
	 * @param ip
     * @return
     */
	@RequestMapping(value = "new_wxpay/weixinSaoMa")
    BaseResp<Object> weixinSaoMa(@RequestParam("price") String price, 
    		@RequestParam("remark") String remark, @RequestParam("orderid") String orderid, 
    		@RequestParam("notifyURL") String notifyURL, @RequestParam("ip")  String ip);

	/**
	 * 支付宝扫码支付
	 * @param price
	 * @param ordernum
     * @return
     */
	@RequestMapping(value="aliPay/aliSaoMa")
	BaseResp<Object> aliPaySaoMa(@RequestParam("price")String price,@RequestParam("ordernum")String ordernum);
}
