package com.longbei.appservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.PayService;

@RestController
@RequestMapping("/pay")
public class ZhiFuBaoController {

	@Autowired
	private PayService payService;
	
	private static Logger logger = LoggerFactory.getLogger(ZhiFuBaoController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/pay/signature
    * @Description: 支付宝签名---龙币支付
    * @param @param userid  
    * @param @param orderid 订单编号
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月20日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/signature")
    public BaseResp<Object> signature(String userid, String orderid) {
		logger.info("userid={},orderid={}",userid,orderid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = payService.signature(Long.parseLong(userid), orderid);
		} catch (Exception e) {
			logger.error("signature userid = {}, orderid = {}", userid, orderid, e);
		}
  		return baseResp;
	}
	
	
}
