package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.UserAddressMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.service.OrderService;
import com.longbei.appservice.service.UserImpCoinDetailService;
import com.longbei.appservice.service.UserMoneyDetailService;
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
	private UserAddressMapper userAddressMapper;
	@Autowired
	private UserImpCoinDetailService userImpCoinDetailService;
	@Autowired
	private UserMoneyDetailService userMoneyDetailService;
	@Autowired
	private UserMsgMapper userMsgMapper;

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	
	/**
	 * @author yinxc
	 * 购买龙币---生成订单
	 * @param userid 用户id
	 * @param number 购买的龙币数量
	 * @auther yinxc
     * @desc  
     * @currentdate:2017年4月7日
	 */
	@Override
	public BaseResp<ProductOrders> buyMoney(long userid, Integer number) {
		BaseResp<ProductOrders> baseResp = new BaseResp<>();
		try{
			//获取用户手机号
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				baseResp = HttpClient.productBasicService.buyMoney(userid, number, userInfo.getUsername());
			}
		}catch (Exception e){
			logger.error("buyMoney userid = {}, number = {}", userid, number, e);
		}
		return baseResp;
	}


    /**
	 * @author yinxc
	 * 购物车结算(用户龙币，进步币兑换商品)
	 * 2017年4月5日
	 * @param orderid 订单业务id
	 */
	@Override
	public BaseResp<Object> buyOrder(long userid, String orderid, Integer impiconprice, Integer moneyprice) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			//判断用户进步币，龙币是否够用
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				if(userInfo.getTotalcoin() < impiconprice){
					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_24);
					return baseResp;
				}
				if(userInfo.getTotalmoney() < moneyprice){
					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_23);
					return baseResp;
				}
				baseResp = HttpClient.productBasicService.buyOrder(userid, orderid);
				if(ResultUtil.isSuccess(baseResp)){
					//调用product_service成功后    扣除进步币，龙币   
					if(moneyprice != 0){
						//origin ： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
						// 					3：设榜单    4：赞助榜单    5：赞助教室 
						userMoneyDetailService.insertPublic(userid, "2", moneyprice, 0);
					}else{
						baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
					}
					if(impiconprice != 0){
						// 7:兑换商品
						userImpCoinDetailService.insertPublic(userid, "7",
								impiconprice, Long.parseLong(orderid), 0l);
					}
				}
			}
		}catch (Exception e){
			logger.error("buyOrder userid = {}, orderid = {}", userid, orderid, e);
		}
		return baseResp;
	}
	
	/**
	 * @author yinxc
	 * 添加龙币明细
	 * 2017年4月5日
	 * @param baseResp 
	 * @param origin ： 来源   0:充值  购买     1：购买礼物(花,钻)  2:兑换商品时抵用进步币
	 * 					3：设榜单    4：赞助榜单    5：赞助教室 
	 */
//	private void insertMoney(Integer moneyprice, Long userid, String origin){
//		//数量
//		userMoneyDetailService.insertPublic(userid, origin, moneyprice, 0);
//	}

	@Override
	public BaseResp<Object> create(Long userid, String productidss, String numberss, String addressid, 
			String impiconprice, String moneyprice, String paytype, String prices, String otype,
			String remark) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserAddress userAddress = userAddressMapper.selectByPrimaryKey(Integer.parseInt(addressid));
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
			if(ResultUtil.isSuccess(baseResp)){
				//orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成    4：已取消(需要返还用户龙币和进步币)
				if("4".equals(orderstatus)){
					//取消订单---返回当前订单用户花销的进步币以及龙币
	  				BaseResp<ProductOrders> resResp = adminget(userid, orderid);
	  				if(ResultUtil.isSuccess(resResp)){
	  					ProductOrders productOrders = resResp.getData();
	  					if(null != productOrders){
							if(productOrders.getMoneyprice() != 0){
								userMoneyDetailService.insertPublic(userid, "6", productOrders.getMoneyprice().intValue(), 0);
							}
							if(productOrders.getImpiconprice() != 0){
								userImpCoinDetailService.insertPublic(userid, "11",
										productOrders.getImpiconprice().intValue(), Long.parseLong(productOrders.getOrderid()), 0l);
							}
							//推送消息
			  				//msgtype 40：订单已取消
			  				//gtype   0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统
			  				//mtype 0 系统消息
			  				insertMsg(userid, "40", productOrders.getOrderid(), "订单已取消", "7", "0");
	  					}
	  				}
	  				
				}
  				
  			}
		}catch (Exception e){
			logger.error("updateOrderStatus userid = {}, orderid = {}, orderstatus= {}", 
					userid, orderid, orderstatus, e);
		}
		return baseResp;
	}
	
	
	
	//--------------------------------adminservice调用-------------------------------------
	
	/** 
	* @Title: insertMsg 
	* @Description: 添加消息 
	* @param @param userid
	* @param @param msgtype
	* @param @param snsid
	* @param @param remark
	* @param @param gtype
	* @param @param mtype    设定文件 
	* @return void    返回类型 
	*/
	private void insertMsg(long userid, String msgtype, String snsid, String remark, String gtype, String mtype){
		//推送消息
		UserMsg userMsg = new UserMsg();
		userMsg.setFriendid(0l);
		userMsg.setUserid(userid);
		userMsg.setMsgtype(msgtype);
		userMsg.setSnsid(Long.parseLong(snsid));
		userMsg.setRemark(remark);
		userMsg.setIsdel("0");
		userMsg.setIsread("0");
		userMsg.setCreatetime(new Date());
		//gtype   0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统
		userMsg.setGtype(gtype);
		//mtype 0 系统消息  1 对话消息  2:@我消息
		userMsg.setMtype(mtype);
		userMsgMapper.insertSelective(userMsg);
	}

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
	public BaseResp<Object> updateOrdersIsexception(long userid, String orderid) {
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
	public BaseResp<Object> updateDeliver(long userid, String orderid, String logisticscode, String logisticscompany) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.updateDeliver(orderid, logisticscode, logisticscompany);
			BaseResp<ProductOrders> resResp = adminget(userid, orderid);
			if(ResultUtil.isSuccess(resResp)){
				ProductOrders productOrders = resResp.getData();
				if(null != productOrders){
					//推送消息
	  				//msgtype 24：订单已发货
	  				//gtype   0:零散 1:目标中 2:榜中  3:圈子中 4.教室中 5:龙群  6:龙级  7:订单  8:认证 9：系统
	  				//mtype 0 系统消息
	  				insertMsg(userid, "24", productOrders.getOrderid(), "订单已发货", "7", "0");
				}
			}
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

	@Override
	public BaseResp<UserAddress> selectAddress(long userid) {
		
		BaseResp<UserAddress> baseResp = new BaseResp<>();
		try{
			UserAddress userAddress = userAddressMapper.selectDefaultAddressByUserid(userid);
			if(null == userAddress){
				//获取用户最新添加的一条
				List<UserAddress> list = userAddressMapper.selectByUserId(userid, 0, 1);
				if(null != list && list.size()>0){
					userAddress = list.get(0);
				}
			}
			baseResp.setData(userAddress);
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			Map<String, Object> expandData = new HashMap<>();
			if(null != userInfo){
				expandData.put("totalmoney", userInfo.getTotalmoney());
				expandData.put("totalcoin", userInfo.getTotalcoin());
			}
			expandData.put("moneytocoin", AppserviceConfig.moneytocoin);
			expandData.put("flowertocoin", AppserviceConfig.flowertocoin);
			expandData.put("moneytoflower", AppserviceConfig.moneytoflower);
			baseResp.setExpandData(expandData);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}catch (Exception e){
			logger.error("selectCountException ", e);
		}
		return baseResp;
	}
	
	


	/**
	 * 系统自动确认收货
	 * @param currentDate
	 * @return
     */
	@Override
	public BaseResp<Object> autoConfirmReceipt(Date currentDate) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try{
			Date beforeDate = DateUtils.getBeforeDateTime(currentDate,Constant.AUTO_CONFIRM_RECEIPT);
			BaseResp<List<ProductOrders>> baseOrderList = HttpClient.productBasicService.selectAutoReceiptOrder(beforeDate.getTime());
			if(baseOrderList.getCode() != 0){
				return baseResp;
			}
			List<ProductOrders> orderList = baseOrderList.getData();
			if(orderList == null || orderList.size() == 0){
				return baseResp.ok();
			}

			for(ProductOrders productOrders:orderList){
				UserMsg userMsg = new UserMsg();
				userMsg.setCreatetime(new Date());
				userMsg.setUpdatetime(new Date());
				userMsg.setIsdel("0");
				userMsg.setIsread("0");
				userMsg.setRemark("您的订单:"+productOrders.getOrderid()+",由于长时间未确认收货,已由系统自动确认收货!");
				userMsg.setMtype("0");
				userMsg.setMsgtype("25");
				userMsg.setGtype("7");
				userMsg.setSnsid(Long.parseLong(productOrders.getOrderid()));
				userMsg.setUserid(Long.parseLong(productOrders.getUserid()));
				userMsg.setFriendid(new Long(Constant.SQUARE_USER_ID));
				this.userMsgMapper.insertSelective(userMsg);
			}

			//修改订单状态
			BaseResp<Object> updateResp = HttpClient.productBasicService.updateOrderAutoConfirmReceipt(beforeDate.getTime());

			return baseResp.ok();
		}catch(Exception e){
			logger.error("order auto confirm receipt error currentDate:{} msg:{}",currentDate,e);
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
