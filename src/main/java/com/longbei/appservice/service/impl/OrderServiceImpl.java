package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
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

}
