package com.longbei.appservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.PayService;
import com.longbei.pay.weixin.res.ResponseHandler;

@RestController
@RequestMapping("/notify")
public class NotifyController {

	@Autowired
	private PayService payService;
	
	private static Logger logger = LoggerFactory.getLogger(NotifyController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/notify/verify/ali
    * @Description: 支付宝回调---龙币支付
    * @param @param userid  
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月20日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/verify/ali")
    public BaseResp<Object> ali(String userid, 
			HttpServletRequest request, HttpServletResponse response) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			Map<String, String> resMap = new HashMap<String, String>();

  			resMap.put("notify_time", request.getParameter("notify_time"));
  			resMap.put("notify_type", request.getParameter("notify_type"));
  			resMap.put("notify_id", request.getParameter("notify_id"));
  			resMap.put("sign_type", request.getParameter("sign_type"));
  			resMap.put("sign", request.getParameter("sign"));
  			resMap.put("out_trade_no", request.getParameter("out_trade_no"));
  			resMap.put("subject", request.getParameter("subject"));
  			resMap.put("payment_type", request.getParameter("payment_type"));
  			resMap.put("trade_no", request.getParameter("trade_no"));
  			resMap.put("trade_status", request.getParameter("trade_status"));
  			resMap.put("seller_id", request.getParameter("seller_id"));
  			resMap.put("seller_email", request.getParameter("seller_email"));
  			resMap.put("buyer_id", request.getParameter("buyer_id"));
  			resMap.put("buyer_email", request.getParameter("buyer_email"));
  			resMap.put("total_fee", request.getParameter("total_fee"));
  			resMap.put("body", request.getParameter("body"));
  			
  			//2：购买龙币
  			baseResp = payService.verifyali(Long.parseLong(userid), "2", resMap);
		} catch (Exception e) {
			logger.error("verify/ali userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/notify/verify/wx
    * @Description: 微信回调---龙币支付
    * @param @param userid  
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月20日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/verify/wx")
    public BaseResp<Object> verifywx(String userid, 
			HttpServletRequest request, HttpServletResponse response) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			ResponseHandler resHandler = new ResponseHandler(request, response);
  			//2：购买龙币
  			baseResp = payService.verifywx(Long.parseLong(userid), "2", resHandler);
		} catch (Exception e) {
			logger.error("verifywx userid = {}", userid, e);
		}
  		return baseResp;
	}
	
}
