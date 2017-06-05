package com.longbei.appservice.service.api.outernetservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.longbei.appservice.common.BaseResp;

@FeignClient("outernetServiceWYZ")
@RequestMapping("outernetService")
public interface OrderSercviceApi {

	@RequestMapping(value = "new_wxpay/weixinSaoMa")
    BaseResp<Object> weixinSaoMa(@RequestParam("price") String price, 
    		@RequestParam("remark") String remark, @RequestParam("orderid") String orderid, 
    		@RequestParam("notifyURL") String notifyURL, @RequestParam("ip")  String ip);
	
}
