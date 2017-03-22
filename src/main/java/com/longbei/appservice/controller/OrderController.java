package com.longbei.appservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.OrderService;
import com.longbei.appservice.service.UserFlowerDetailService;

@RestController
@RequestMapping(value = "order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserFlowerDetailService userFlowerDetailService;
	
	private static Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/order/create
    * @Description: 提交购物车订单
    * @param @param userid  
    * @param @param productidss 商品id(多个商品以,分割)
    * @param @param numberss 购买数量(多个以,分割)
    * @param @param address 收件人地址
    * @param @param receiver 收件人
    * @param @param mobile 收件人手机
    * @param @param impiconprice 成交价格---进步币 
    * @param @param moneyprice 成交价格---龙币
    * @param @param paytype 支付方式 0：龙币支付 1：微信支付 2：支付宝支付
    * @param @param prices 商品价格，以逗号隔开
    * @param @param otype 订单类型。0 龙币 1 进步币 2 混排
    * @param @param remark 备注
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月17日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/create")
    public BaseResp<Object> create(String userid, String productidss, String numberss, String prices, 
    		String address, String receiver, String mobile, String impiconprice, String moneyprice, String paytype, 
    		String otype, String remark) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, productidss, numberss, address, receiver, mobile, 
  				impiconprice, paytype, prices, otype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.create(Long.parseLong(userid), productidss, numberss, address, receiver, 
  					mobile, impiconprice, moneyprice, paytype, prices, otype, remark);
		} catch (Exception e) {
			logger.error("create userid = {}, productidss = {}, numberss = {}, impiconprice = {}, moneyprice = {}", 
					userid, productidss, numberss, impiconprice, moneyprice, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/list
    * @Description: 我的订单列表
    * @param @param userid 
    * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	* 						为null   则查全部 
    * @param @param startNo  pageSize
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月16日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/list")
    public BaseResp<Object> list(String userid, String orderstatus, Integer startNo, Integer pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);;
		if(null == startNo){
			sNo = startNo.intValue();
		}
		if(null == pageSize){
			sSize = pageSize.intValue();
		}
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.list(Long.parseLong(userid), orderstatus, sNo, sSize);
		} catch (Exception e) {
			logger.error("list userid = {}, orderstatus = {}, startNo = {}, pageSize = {}", 
					userid, orderstatus, startNo, pageSize, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/get
    * @Description: 订单详情
    * @param @param userid 
    * @param @param orderid 订单业务id  
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月17日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/get")
    public BaseResp<Object> get(String userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.get(Long.parseLong(userid), orderid);
		} catch (Exception e) {
			logger.error("get userid = {}, orderid = {}", userid, orderid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/exchange
    * @Description: 再次兑换
    * @param @param userid 
    * @param @param orderid 订单业务id 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月16日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/exchange")
    public BaseResp<Object> exchange(String userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.exchange(Long.parseLong(userid), orderid);
		} catch (Exception e) {
			logger.error("exchange userid = {}, orderid = {}", userid, orderid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/updateOrderStatus
    * @Description: 修改订单状态
    * @param @param userid 
    * @param @param orderid 订单业务id 
    * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月16日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/updateOrderStatus")
    public BaseResp<Object> updateOrderStatus(String userid, String orderid, String orderstatus) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid, orderstatus)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			//orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成 
  			baseResp = orderService.updateOrderStatus(Long.parseLong(userid), orderid, orderstatus);
		} catch (Exception e) {
			logger.error("updateOrderStatus userid = {}, orderid = {}, orderstatus= {}", 
					userid, orderid, orderstatus, e);
		}
  		return baseResp;
	}
	
	
	/**
    * @Title: http://ip:port/app_service/order/getUserInfoCoin
    * @Description: 获取用户龙币，进步币以及龙币兑换进步币比例
    * @param @param userid 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  Map: 
    * 				totalmoney---龙币总数
    * 				totalcoin---进步币总数
    * 				moneytocoin---龙币兑换进步币比例
    * @currentdate:2017年3月21日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/getUserInfoCoin")
    public BaseResp<Object> getUserInfoCoin(String userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userFlowerDetailService.selectUserInfoByUserid(Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("getUserInfoCoin userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/moneyExchangeFlower
    * @Description: 用户龙币兑换鲜花
    * @param @param userid 
    * @param @param number 鲜花数量
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  Data: 添加的鲜花记录
    * @desc  Map: 
    * 				totalmoney---龙币总数
    * 				totalcoin---进步币总数
    * 				totalflower---鲜花总数  
    * @currentdate:2017年3月21日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/moneyExchangeFlower")
    public BaseResp<Object> moneyExchangeFlower(String userid, String number) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, number)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userFlowerDetailService.moneyExchangeFlower(Long.parseLong(userid), Integer.parseInt(number));
		} catch (Exception e) {
			logger.error("moneyExchangeFlower userid = {}, number = {}", userid, number, e);
		}
  		return baseResp;
	}
	
	

}
