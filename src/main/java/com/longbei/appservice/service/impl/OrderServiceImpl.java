package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.entity.UserAddress;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.OrderService;
import com.longbei.appservice.service.UserAddressService;
import com.longbei.appservice.service.api.HttpClient;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserLevelMapper userLevelMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserAddressService userAddressService;
	
	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Override
	public BaseResp<Object> create(Long userid, String productidss, String numberss, String addressid, 
			String impiconprice, String moneyprice, String paytype, String prices, String otype,
			String remark) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserAddress userAddress = userAddressService.selectByPrimaryKey(Integer.parseInt(addressid));
				//省市+详细地址
				String address = userAddress.getRegion() + userAddress.getAddress();
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				
				baseResp = HttpClient.productBasicService.create(userid, userInfo.getUsername(), productidss, numberss, address, 
						userAddress.getReceiver(), userAddress.getMobile(), impiconprice, moneyprice, paytype, prices, otype, 
						remark, userLevel.getDiscount().toString());
			}
		}catch (Exception e){
			logger.error("create userid = {}, productidss= {}, numberss = {}, prices = {}", 
					userid, productidss, numberss, prices, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<List<ProductOrders>> list(Long userid, String orderstatus, int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<>();
		try{
			if(StringUtils.isBlank(orderstatus) || "null".equals(orderstatus)){
				orderstatus = "";
			}
			baseResp = HttpClient.productBasicService.list(userid, orderstatus, startNo, pageSize);
		}catch (Exception e){
			logger.error("list userid = {}, orderstatus = {}, startNo = {}, pageSize = {}", 
					userid, orderstatus, startNo, pageSize, e);
		}
		return baseResp;
	}
	
	

	@Override
	public BaseResp<ProductOrders> get(Long userid, String orderid) {
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
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
	public BaseResp<List<ProductOrders>> adminlist(String orderstatus, int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
		try{
			if(StringUtils.isBlank(orderstatus) || "null".equals(orderstatus)){
				orderstatus = null;
			}
			baseResp = HttpClient.productBasicService.adminlist(orderstatus, startNo, pageSize);
		}catch (Exception e){
			logger.error("adminlist orderstatus = {}, startNo = {}, pageSize = {}", 
					orderstatus, startNo, pageSize, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<ProductOrders> adminget(Long userid, String orderid) {
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.adminget(userid, orderid);
			if (!ResultUtil.isSuccess(baseResp)){
	            return null;
	        }
	        ProductOrders productOrders = baseResp.getData();
	        if(null != productOrders){
	        	initMsgUserInfoByUserid(productOrders);
	        }
	        baseResp.setData(productOrders);
	        baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
			logger.error("updateOrdersRemark orderid = {}, remark = {}", 
					orderid, remark, e);
		}
		return baseResp;
	}

	/**
	 * @author yinxc
	 * 获取用户不同的订单状态的总数
     * @param @param userid 
     * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
     * 						为null   则查全部 
	 * 2017年3月22日
	 */
	@Override
	public BaseResp<Integer> selectCountOrders(String orderstatus) {
		BaseResp<Integer> baseResp = new BaseResp<Integer>();
		try{
			baseResp = HttpClient.productBasicService.selectCountOrders(orderstatus);
		}catch (Exception e){
			logger.error("selectCountOrders orderstatus = {}", 
					orderstatus, e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<Integer> selectCountSearchOrders(String orderstatus, String ordernum, String username,
			String screatetime, String ecreatetime) {
		BaseResp<Integer> baseResp = new BaseResp<Integer>();
		try{
			baseResp = HttpClient.productBasicService.selectCountSearchOrders(orderstatus, ordernum, username, 
					screatetime, ecreatetime);
		}catch (Exception e){
			logger.error("selectCountOrders orderstatus = {}", 
					orderstatus, e);
		}
		return baseResp;
	}
	
	

	@Override
	public BaseResp<List<ProductOrders>> searchList(String orderstatus, String ordernum, String username,
			String screatetime, String ecreatetime, int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
		try{
			if(StringUtils.isBlank(orderstatus) || "null".equals(orderstatus)){
				orderstatus = null;
			}
			baseResp = HttpClient.productBasicService.searchList(orderstatus, ordernum, username, 
					screatetime, ecreatetime, startNo, pageSize);
		}catch (Exception e){
			logger.error("adminlist orderstatus = {}, startNo = {}, pageSize = {}", 
					orderstatus, startNo, pageSize, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<List<ProductOrders>> exceptionlist(int startNo, int pageSize) {
		BaseResp<List<ProductOrders>> baseResp = new BaseResp<List<ProductOrders>>();
		try{
			baseResp = HttpClient.productBasicService.exceptionlist(startNo, pageSize);
		}catch (Exception e){
			logger.error("exceptionlist startNo = {}, pageSize = {}", 
					startNo, pageSize, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateDeliver(String orderid, String logisticscode, String logisticscompany) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.updateDeliver(orderid, logisticscode, logisticscompany);
		}catch (Exception e){
			logger.error("updateDeliver orderid = {}, logisticscode={}, logisticscompany={}", 
					orderid, logisticscode, logisticscompany, e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<Integer> selectCountException() {
		BaseResp<Integer> baseResp = new BaseResp<Integer>();
		try{
			baseResp = HttpClient.productBasicService.selectCountException();
		}catch (Exception e){
			logger.error("selectCountException ", e);
		}
		return baseResp;
	}

	/**
     * 初始化消息中用户信息 ------Userid
     */
    private void initMsgUserInfoByUserid(ProductOrders productOrders){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(productOrders.getUserid()));
        productOrders.setAppUserMongoEntity(appUserMongoEntity);
    }

    
}
