package com.longbei.appservice.controller.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.service.OrderService;

@RestController
@RequestMapping(value = "/api/order")
public class OrderApiController {

	@Autowired
	private OrderService orderService;
	
	private static Logger logger = LoggerFactory.getLogger(OrderApiController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/api/order/adminlist
    * @Description: 订单列表(所有的)
    * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	* 						为null   则查全部 
    * @param @param startNo  pageSize
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月22日
	*/
  	@RequestMapping(value = "/adminlist")
    public BaseResp<List<ProductOrders>> adminlist(String orderstatus, int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
  		try {
  			baseResp = orderService.adminlist(orderstatus, startNo, pageSize);
		} catch (Exception e) {
			logger.error("adminlist orderstatus = {}, startNo = {}, pageSize = {}", 
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
    public BaseResp<Object> updateOrdersIsexception(String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(orderid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			//isexception   0 正常 1 异常订单
  			baseResp = orderService.updateOrdersIsexception(orderid);
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
  			baseResp = orderService.updateOrdersIsexception(orderid);
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
    * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成   4：已取消
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
  	
}
