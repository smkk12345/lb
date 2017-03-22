package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.OrderService;
import com.longbei.appservice.service.api.HttpClient;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserLevelMapper userLevelMapper;
	
	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public BaseResp<Object> create(Long userid, String productidss, String numberss, String address, String receiver,
			String mobile, String impiconprice, String moneyprice, String paytype, String prices, String otype,
			String remark) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				baseResp = HttpClient.productBasicService.create(userid, productidss, numberss, address, 
						receiver, mobile, impiconprice, moneyprice, paytype, prices, otype, 
						remark, userLevel.getDiscount().toString());
			}
		}catch (Exception e){
			logger.error("create userid = {}, productidss= {}, numberss = {}, prices = {}", 
					userid, productidss, numberss, prices, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> list(Long userid, String orderstatus, int startNo, int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.list(userid, orderstatus, startNo, pageSize);
		}catch (Exception e){
			logger.error("list userid = {}, orderstatus = {}, startNo = {}, pageSize = {}", 
					userid, orderstatus, startNo, pageSize, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> get(Long userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.get(userid, orderid);
		}catch (Exception e){
			logger.error("get userid = {}, orderid = {}", userid, orderid, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> exchange(Long userid, String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				baseResp = HttpClient.productBasicService.exchange(userid, orderid, userLevel.getDiscount().toString());
			}
		}catch (Exception e){
			logger.error("exchange userid = {}, orderid = {}", userid, orderid, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateOrderStatus(Long userid, String orderid, String orderstatus) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.updateOrderStatus(userid, orderid, orderstatus);
		}catch (Exception e){
			logger.error("updateOrderStatus userid = {}, orderid = {}, orderstatus= {}", 
					userid, orderid, orderstatus, e);
		}
		return baseResp;
	}
	
	
	
	//--------------------------------adminservice调用-------------------------------------

	@Override
	public BaseResp<Object> adminlist(Long userid, String orderstatus, int startNo, int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			if(StringUtils.isBlank(orderstatus) || "null".equals(orderstatus)){
				orderstatus = null;
			}
			baseResp = HttpClient.productBasicService.adminlist(userid, orderstatus, startNo, pageSize);
		}catch (Exception e){
			logger.error("adminlist userid = {}, orderstatus = {}, startNo = {}, pageSize = {}", 
					userid, orderstatus, startNo, pageSize, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<ProductOrders> adminget(Long userid, String orderid) {
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.adminget(userid, orderid);
		}catch (Exception e){
			logger.error("adminget userid = {}, orderid = {}", 
					userid, orderid, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateOrdersIsexception(String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.updateOrdersIsexception(orderid);
		}catch (Exception e){
			logger.error("updateOrdersIsexception orderid = {}", orderid, e);
		}
		return baseResp;
	}

	/**
	 * 取消订单
	 * @author yinxc
	 * @param @param orderid 订单业务id 
	 * 2017年3月22日
	 */
	@Override
	public BaseResp<Object> updateOrdersIsdel(String orderid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			//取消需要返还龙币，进步币
			
			baseResp = HttpClient.productBasicService.updateOrdersIsdel(orderid);
		}catch (Exception e){
			logger.error("updateOrdersIsdel orderid = {}", orderid, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateOrdersRemark(String orderid, String remark) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.updateOrdersRemark(orderid, remark);
		}catch (Exception e){
			logger.error("updateOrdersRemark orderid = {}, remark= {}", 
					orderid, remark, e);
		}
		return baseResp;
	}

}
