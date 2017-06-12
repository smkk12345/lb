package com.longbei.appservice.controller.api;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.service.OrderService;

import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "/api/order")
public class OrderApiController {

	@Autowired
	private OrderService orderService;
	
	private static Logger logger = LoggerFactory.getLogger(OrderApiController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/api/order/adminConsumeList
    * @Description: 兑换商品订单列表(所有的)
    * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	* 						为null   则查全部 
    * @param @param startNo  pageSize
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
  	@RequestMapping(value = "/adminConsumeList")
    public BaseResp<List<ProductOrders>> adminConsumeList(String orderstatus, int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
  		try {
  			baseResp = orderService.adminConsumeList(orderstatus, startNo, pageSize);
		} catch (Exception e) {
			logger.error("adminConsumeList orderstatus = {}, startNo = {}, pageSize = {}",
					orderstatus, startNo, pageSize, e);
		}
  		return baseResp;
	}

  	/**
     * @Title: http://ip:port/app_service/api/order/searchList
     * @Description: 订单搜索
     * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
 	 * 						为null   则查全部 
     * @param @param ordernum 订单编号
     * @param @param username 用户手机号
     * @param @param screatetime    ecreatetime下单搜索时间段
     * @param @param startNo pageSize
     * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @desc  
     * @currentdate:2017年3月16日
 	*/
  	@RequestMapping(value = "/searchList", method = RequestMethod.POST)
    public BaseResp<List<ProductOrders>> searchList(String orderstatus, String ordernum, 
    		String username, String screatetime, String ecreatetime, 
    		int startNo, int pageSize) {
  		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
  		try {
  			baseResp = orderService.searchList(orderstatus, ordernum, username, 
  					screatetime, ecreatetime, startNo, pageSize);
		} catch (Exception e) {
			logger.error("searchList orderstatus = {}, startNo = {}, pageSize = {}", 
					orderstatus, startNo, pageSize, e);
		}
  		return baseResp;
	}
  	
	/**
    * @Title: http://ip:port/app_service/order/adminget
    * @Description: 订单详情
    * @param @param userid 
    * @param @param orderid 订单业务id  
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/adminget")
    public BaseResp<ProductOrders> adminget(String userid, String orderid) {
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.adminget(Long.parseLong(userid), orderid);
		} catch (Exception e) {
			logger.error("adminget userid = {}, orderid = {}", userid, orderid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/api/order/updateOrdersIsexception
    * @Description: 设为异常订单
    * @param @param orderid 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/updateOrdersIsexception")
    public BaseResp<Object> updateOrdersIsexception(String userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			//isexception   0 正常 1 异常订单
  			baseResp = orderService.updateOrdersIsexception(Long.parseLong(userid), orderid);
		} catch (Exception e) {
			logger.error("updateOrdersIsexception orderid = {}", orderid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/api/order/updateOrdersIsdel
    * @Description: 取消订单
    * @param @param orderid 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/updateOrdersIsdel")
    public BaseResp<Object> updateOrdersIsdel(String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			//isdel   是否取消订单   0：不取消 1:取消
  			baseResp = orderService.updateOrdersIsdel(orderid);
		} catch (Exception e) {
			logger.error("updateOrdersIsdel orderid = {}", orderid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/api/order/updateOrdersRemark
    * @Description: 修改订单备注
    * @param @param orderid 
    * @param @param remark 备注
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/updateOrdersRemark")
    public BaseResp<Object> updateOrdersRemark(String orderid, String remark) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(orderid, remark)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.updateOrdersRemark(orderid, remark);
		} catch (Exception e) {
			logger.error("updateOrdersRemark orderid = {}, remark = {}", orderid, remark, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/api/order/updateOrderStatus
    * @Description: 修改订单状态
    * @param @param userid 
    * @param @param orderid 订单业务id 
    * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成   4：已取消(需要返还用户龙币和进步币)
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
  			//orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成    4：已取消(需要返还用户龙币和进步币)
  			baseResp = orderService.updateOrderStatus(Long.parseLong(userid), orderid, orderstatus);
		} catch (Exception e) {
			logger.error("updateOrderStatus userid = {}, orderid = {}, orderstatus= {}", 
					userid, orderid, orderstatus, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/api/order/selectCountOrders
    * @Description: 获取用户不同的订单状态的总数
    * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
    * 						为null   则查全部 
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
  	@RequestMapping(value = "/selectCountOrders")
    public BaseResp<Integer> selectCountOrders(String orderstatus) {
		BaseResp<Integer> baseResp = new BaseResp<>();
  		try {
  			baseResp = orderService.selectCountOrders(orderstatus);
		} catch (Exception e) {
			logger.error("selectCountOrders orderstatus = {}", orderstatus, e);
		}
  		return baseResp;
	}
  	
  	/**
	 * 获取订单搜索的总数
	 * @author yinxc
	 * 2017年3月24日
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
 	 * 						为null   则查全部 
     * @param @param ordernum 订单编号
     * @param @param username 用户手机号
     * @param @param screatetime    ecreatetime下单搜索时间段
	 */
  	@RequestMapping(value = "/selectCountSearchOrders")
    public BaseResp<Integer> selectCountSearchOrders(String orderstatus, String ordernum, 
    		String username, String screatetime, String ecreatetime) {
		BaseResp<Integer> baseResp = new BaseResp<>();
  		try {
  			baseResp = orderService.selectCountSearchOrders(orderstatus, ordernum, username, screatetime, ecreatetime);
		} catch (Exception e) {
			logger.error("selectCountSearchOrders orderstatus = {}, ordernum = {}", orderstatus, ordernum, e);
		}
  		return baseResp;
	}

	/**
    * @Title: http://ip:port/app_service/api/order/updateDeliver
    * @Description: 订单发货
    * @param @param orderid 订单业务id 
    * @param @param logisticscode 物流编号
    * @param @param logisticscompany 物流公司
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月24日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/updateDeliver", method = RequestMethod.POST)
    public BaseResp<Object> updateDeliver(String userid, String orderid, String logisticscode, String logisticscompany) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
  		if (StringUtils.hasBlankParams(userid, orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = orderService.updateDeliver(Long.parseLong(userid), orderid, logisticscode, logisticscompany);
		} catch (Exception e) {
			logger.error("updateDeliver orderid = {}, logisticscode={}, logisticscompany={}", 
					orderid, logisticscode, logisticscompany, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/api/order/exceptionlist
    * @Description: 获取异常订单列表
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月24日
	*/
  	@RequestMapping(value = "/exceptionlist")
    public BaseResp<List<ProductOrders>> exceptionlist(int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
  		try {
  			baseResp = orderService.exceptionlist(startNo, pageSize);
		} catch (Exception e) {
			logger.error("exceptionlist startNo = {}, pageSize = {}", 
					startNo, pageSize, e);
		}
  		return baseResp;
	}
  	
  	/**
     * @Title: http://ip:port/product_service/api/order/selectCountException
     * @Description: 获取异常订单总数
     * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @desc  
     * @currentdate:2017年3月16日
 	*/
   	@RequestMapping(value = "/selectCountException")
     public BaseResp<Integer> selectCountException() {
 		BaseResp<Integer> baseResp = new BaseResp<Integer>();
   		try {
   			baseResp = orderService.selectCountException();
 		} catch (Exception e) {
 			logger.error("selectCountException", e);
 		}
   		return baseResp;
 	}

	/**
	 * 系统自动确认收货
	 * @param currentTime
	 * @return
     */
	@RequestMapping(value="autoConfirmReceipt")
	public BaseResp<Object> autoConfirmReceipt(Long currentTime){
		BaseResp<Object> baseResp = new BaseResp<Object>();
		if(currentTime == null){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		Date currentDate = new Date(currentTime);
		baseResp = this.orderService.autoConfirmReceipt(currentDate);
		return baseResp;
	}
	
	
	
	
	//-----------------------web_service调用----------------------------
	/**
	 * @Title: http://ip:port/app_service/api/order/buyMoney
	 * @Description: 购买龙币---生成订单
	 * @param userid 用户id
	 * @param number 购买的龙币数量
	 * @param paytype 支付方式  0：龙币支付 1：微信支付 2：支付宝支付
     *                       3:IOS内购测试帐号购买 4：IOS内购正式帐号购买
     *                       5:web_sercvice购买
     * @param btype  0:支付宝   1 微信支付   
	 * @auther yinxc
     * @desc  
     * @currentdate:2017年4月7日
	 */
	@RequestMapping(value = "/buyMoney", method = RequestMethod.POST)
	public BaseResp<Object> buyMoney(String userid, String number, String paytype, String btype, 
			HttpServletRequest request) {
		logger.info(userid + "购买 " + number + " 龙币，订单生成中....");
		BaseResp<Object> resResp = new BaseResp<>();
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
  			BaseResp<ProductOrders> baseResp = new BaseResp<>();
  			baseResp = orderService.buyMoney(Long.parseLong(userid), num, paytype, price);
  			if(ResultUtil.isSuccess(baseResp)){
  				ProductOrders orders = baseResp.getData();
  				//btype  0:支付宝   1 微信支付   
  				
  				if("1".equals(btype)){
  					String ip = request.getRemoteAddr();
  					resResp = orderService.weixinSaoMa(price, "longbi", orders.getOrdernum(), ip,orders.getUserid());
  					return resResp;
  				}else if("0".equals(btype)){
  					resResp = orderService.aliPaySaoMa(price,orders.getOrdernum(),orders.getUserid());
					return resResp;
  				}
  			}
			logger.info("buyMoney success and baseResp={}", JSONObject.fromObject(baseResp).toString());
		} catch (Exception e) {
			logger.error("buyMoney userid = {}, number = {}, paytype = {}", 
					userid, number, paytype, e);
		}
  		return resResp;
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
  	
}
