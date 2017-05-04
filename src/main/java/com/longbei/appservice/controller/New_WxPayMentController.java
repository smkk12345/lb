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

/**
 * @author yinxc
 * 微信支付
 * 2017年3月20日
 * New_WxPayMentController
 */

@RestController
@RequestMapping("/new_wxpay")
public class New_WxPayMentController {

	@Autowired
	private PayService payService;
	
	private static Logger logger = LoggerFactory.getLogger(New_WxPayMentController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/new_wxpay/wxPayMainPage
    * @Description: 微信支付---龙币
    * @param @param userid  
    * @param @param orderid 订单编号
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月20日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/wxPayMainPage")
    public BaseResp<Object> wxPayMainPage(String userid, String orderid) {
		logger.info("userid={},orderid={}",userid,orderid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = payService.wxPayMainPage(Long.parseLong(userid), orderid);
		} catch (Exception e) {
			logger.error("wxPayMainPage userid = {}, orderid = {}", userid, orderid, e);
		}
  		return baseResp;
	}
	
}
