package com.longbei.appservice.service.api.outernetservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longbei.appservice.common.BaseResp;

@FeignClient("outernetService")
@RequestMapping("outernetService")
public interface OrderSercviceApi {

	/**
	 * 微信扫码支付
	 * @param price
	 * @param remark
	 * @param orderid
	 * @param ip
     * @return
     */
	@RequestMapping(value = "new_wxpay/weixinSaoMa")
    BaseResp<Object> weixinSaoMa(@RequestParam("price") String price, 
    		@RequestParam("remark") String remark, @RequestParam("orderid") String orderid,
			@RequestParam("ip")  String ip,@RequestParam("userid")String userid);

	/**
	 * 支付宝扫码支付
	 * @param price
	 * @param orderid
     * @return
     */
	@RequestMapping(value="aliPay/aliSaoMa")
	BaseResp<Object> aliPaySaoMa(@RequestParam("price")String price,@RequestParam("orderid")String orderid,@RequestParam("userid")String userid);
}
