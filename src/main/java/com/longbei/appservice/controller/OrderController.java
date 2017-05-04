package com.longbei.appservice.controller;

import java.util.List;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.entity.UserAddress;
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
	 * @Title: http://ip:port/app_service/order/buyMoney
	 * @Description: 购买龙币---生成订单
	 * @param userid 用户id
	 * @param number 购买的龙币数量
	 * @param paytype 支付方式  0：龙币支付 1：微信支付 2：支付宝支付
     *                       3:IOS内购测试帐号购买 4：IOS内购正式帐号购买
	 * @auther yinxc
     * @desc  
     * @currentdate:2017年4月7日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/buyMoney", method = RequestMethod.POST)
	public BaseResp<ProductOrders> buyMoney(String userid, String number, String paytype) {
		logger.info(userid + "购买 " + number + " 朵龙币，订单生成中....");
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, number, paytype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			//人民币兑换龙币比例       
  		    double yuantomoney = AppserviceConfig.yuantomoney;
  		    //price 获取真实价格    已分为单位
  		    double minute = Integer.parseInt(number)*yuantomoney*100;
  		    String price = minute + "";
  			int num = 0;
  			if(!"3".equals(paytype) && !"4".equals(paytype)){
  				num = getMoneyNum(Integer.parseInt(number));
  			}else{
  				num = Integer.parseInt(number);
  			}
  			baseResp = orderService.buyMoney(Long.parseLong(userid), num, paytype, price);
			logger.info("buyMoney success and baseResp={}", JSONObject.fromObject(baseResp).toString());
		} catch (Exception e) {
			logger.error("buyMoney userid = {}, number = {}, paytype = {}", 
					userid, number, paytype, e);
		}
  		return baseResp;
	}
	
	private int getMoneyNum(int num){
		 long xs = num/100 - 1;
        double a = (xs*0.145) + 1;
        long lastNum = (long)(num/100);
        lastNum = lastNum*100;
        Double giveFlower = lastNum * (a/100);
        long giveNum = Math.round(giveFlower);
        return giveNum>0?num+Integer.parseInt(giveNum+""):num;
	}
	
	
	/**
    * @Title: http://ip:port/app_service/order/buyOrder
    * @Description: 购物车结算(用户龙币，进步币兑换商品)
    * @param @param userid 
    * @param @param orderid 订单业务id 
    * @param @param impiconprice 成交价格---进步币
    * @param @param moneyprice 成交价格---龙币       若没有,传0
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年4月5日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/buyOrder")
    public BaseResp<Object> buyOrder(String userid, String orderid, String impiconprice, String moneyprice) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid, impiconprice)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
			if(StringUtils.isBlank(moneyprice) || "0".equals(moneyprice)){
  				moneyprice = "0";
  			}else{
				if(moneyprice.indexOf(".0") != -1){
					moneyprice = moneyprice.substring(0, moneyprice.length()-2);
				}
			}
			if(impiconprice.indexOf(".0") != -1){
				impiconprice = impiconprice.substring(0, impiconprice.length()-2);
			}
  			baseResp = orderService.buyOrder(Long.parseLong(userid), orderid,
					Integer.parseInt(impiconprice), Integer.parseInt(moneyprice));
		} catch (Exception e) {
			logger.error("buyOrder userid = {}, orderid = {}, impiconprice = {}, moneyprice = {}", 
					userid, orderid, impiconprice, moneyprice, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/selectAddressIsdefault
    * @Description: 获取订单默认收货地址
    * @param @param userid 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc   Map: 
    * 				totalmoney---龙币总数
    * 				totalcoin---进步币总数
    * 				moneytocoin---龙币兑换进步币比例 1:10
    * 				flowertocoin---花兑换进步币比例  1:10
    * 				flowertomoney---花兑换龙币比例 1:1 
    *  				yuantomoney---人民币兑换龙币比例 1:10
    * @currentdate:2017年3月31日
	*/
	@SuppressWarnings({ "unchecked" })
  	@RequestMapping(value = "/selectAddressIsdefault")
    public BaseResp<UserAddress> selectAddressIsdefault(String userid) {
		BaseResp<UserAddress> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.selectAddress(Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("selectAddressIsdefault userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/create
    * @Description: 提交购物车订单
    * @param @param userid  
    * @param @param productidss 商品id(多个商品以,分割)
    * @param @param numberss 购买数量(多个以,分割)
    * @param @param addressid 收货地址id
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
    public BaseResp<ProductOrders> create(String userid, String productidss, String numberss, String prices,
    		String addressid, String impiconprice, String moneyprice, String paytype, 
    		String otype, String remark) {
		logger.info("create userid = {}, productidss= {}, numberss = {}, addressid = {}, prices = {}, impiconprice = {},"
				+ " moneyprice = {}, remark = {}", 
				userid, productidss, numberss, addressid, impiconprice, moneyprice, prices, remark);
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, productidss, numberss, addressid, 
  				impiconprice, paytype, prices, otype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.create(Long.parseLong(userid), productidss, numberss, addressid, 
  					impiconprice, moneyprice, paytype, prices, otype, remark);
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
    * @param @param startNum  pageSize
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月16日
	*/
	@SuppressWarnings({ "unchecked" })
  	@RequestMapping(value = "/list")
    public BaseResp<List<ProductOrders>> list(String userid, String orderstatus, Integer startNum, Integer pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<>();
		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNum){
			sNo = startNum.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.list(Long.parseLong(userid), orderstatus, sNo, sSize);
		} catch (Exception e) {
			logger.error("list userid = {}, orderstatus = {}, startNum = {}, pageSize = {}",
					userid, orderstatus, startNum, pageSize, e);
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
    public BaseResp<ProductOrders> get(String userid, String orderid) {
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
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
    * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成  4:已取消
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
  			//orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成  4:已取消
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
    * 				moneytocoin---龙币兑换进步币比例 1:10
    * 				flowertocoin---花兑换进步币比例  1:10
    * 				flowertomoney---花兑换龙币比例 1:1
    * 				yuantomoney---人民币兑换龙币比例 1:10
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
    * @Title: http://ip:port/app_service/order/exchangeFlower
    * @Description: 用户兑换鲜花并赠送
    * @param @param userid 赠送人id
    * @param @param friendid被赠送人id
    * @param @param number 鲜花数量
    * @param @param improveid    进步id
    * @param @param businessid  各类型对应的id
    * @param @param businesstype  类型    0 零散进步   1 目标进步    2 榜  3圈子 4 教室
    * @param @param payType 1:龙币兑换  2：进步币兑换
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
  	@RequestMapping(value = "/exchangeFlower")
    public BaseResp<Object> exchangeFlower(String userid, String number, String friendid, 
    		String improveid, String businesstype, String businessid, String payType) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, number, friendid, improveid, businesstype, payType)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			//payType 1:龙币兑换  2：进步币兑换
  			if("1".equals(payType)){
  				baseResp = userFlowerDetailService.moneyExchangeFlower(Long.parseLong(userid), Integer.parseInt(number), 
  	  					friendid, improveid, businesstype, businessid);
  			}else{
  				baseResp = userFlowerDetailService.coinExchangeFlower(Long.parseLong(userid), Integer.parseInt(number), 
  	  					friendid, improveid, businesstype, businessid);
  			}
		} catch (Exception e) {
			logger.error("moneyExchangeFlower userid = {}, number = {}", userid, number, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/order/coinExchangeFlower
    * @Description: 用户进步币兑换鲜花并赠送
    * @param @param userid 赠送人id
    * @param @param friendid被赠送人id
    * @param @param number 鲜花数量
    * @param @param improveid    进步id
    * @param @param businessid  各类型对应的id
    * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  Data: 添加的鲜花记录
    * @desc  Map: 
    * 				totalmoney---龙币总数
    * 				totalcoin---进步币总数
    * 				totalflower---鲜花总数  
    * @currentdate:2017年3月28日
	*/
//	@SuppressWarnings("unchecked")
//  	@RequestMapping(value = "/coinExchangeFlower")
//    public BaseResp<Object> coinExchangeFlower(String userid, String number, String friendid,
//    		String improveid, String businesstype, String businessid) {
//		BaseResp<Object> baseResp = new BaseResp<>();
//  		if (StringUtils.hasBlankParams(userid, number)) {
//  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
//  		}
//  		try {
//  			baseResp = userFlowerDetailService.coinExchangeFlower(Long.parseLong(userid), Integer.parseInt(number),
//  					friendid, improveid, businesstype, businessid);
//		} catch (Exception e) {
//			logger.error("coinExchangeFlower userid = {}, number = {}", userid, number, e);
//		}
//  		return baseResp;
//	}
	

}
