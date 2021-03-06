package com.longbei.appservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.entity.UserAddress;

public interface OrderService {
	
	/**
	 * @author yinxc
	 * 购买龙币---生成订单
	 * @param userid 用户id
	 * @param number 购买的龙币数量
	 * @param paytype 支付方式  0：龙币支付 1：微信支付 2：支付宝支付
     *                       3:IOS内购测试帐号购买 4：IOS内购正式帐号购买
	 * @param price 真实价格
	 * @auther yinxc
     * @desc  
     * @currentdate:2017年4月7日
	 */
	BaseResp<ProductOrders> buyMoney(long userid, Integer number, String paytype, String price);

	/**
	 * 判断pc端购买龙币是否支付成功
	 * @param ordernum
	 * @return
	 */
	boolean isSuccessPay(String ordernum);
	
	/**
	 * @author yinxc
	 * 购物车结算(用户龙币，进步币兑换商品)
	 * 2017年4月5日
	 * @param orderid 订单业务id
	 * @param ptype 0:Android 1：IOS
	 */
	BaseResp<Object> buyOrder(long userid, String orderid, Integer impiconprice, Integer moneyprice, String ptype);
	
	BaseResp<UserAddress> selectAddress(long userid);
	
	/**
	 * @author yinxc
	 * 提交购物车订单
	 * 2017年3月17日
	 * @param userid 用户id
     * @param productidss 商品id(多个商品以,分割)
     * @param numberss 购买数量(多个以,分割)
     * @param address 收件人地址
     * @param receiver 收件人
     * @param mobile 收件人手机
     * @param impiconprice 成交价格---进步币 
     * @param moneyprice 成交价格---龙币
     * @param paytype 支付方式 0：龙币支付 1：微信支付 2：支付宝支付
     * @param prices 商品价格，以逗号隔开
     * @param otype 订单类型。0 龙币 1 进步币 2 混排
     * @param remark 备注
     * @param discount 用户等级所享受的折扣
	 */
	 BaseResp<ProductOrders> create(Long userid, String productidss,
			 String numberss, String addressid, 
			 String impiconprice, String moneyprice, String paytype, String prices, 
			 String otype, String remark, String ptype);
	 
	 /**
	 * 我的订单列表
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	 * 						为null   则查全部 
	 * @param @param startNo  pageSize
	 * 2017年3月17日
	 */
	 BaseResp<List<ProductOrders>> list(Long userid, String orderstatus, 
			 int startNo, int pageSize);
	 
	 /**
	 * 订单详情
	 * @author yinxc
	 * @param @param orderid 订单业务id  
	 * 2017年3月17日
	 */
	 BaseResp<ProductOrders> get(Long userid, String orderid);
	 
	 /**
	 * 再次兑换
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderid 订单业务id 
	 * @param @param discount 用户等级所享受的折扣 
	 * 2017年3月17日
	 */
	 BaseResp<Object> exchange(Long userid, String orderid);
	 
	 /**
	 * 修改订单状态
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderid 订单业务id 
	 * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成 
	 * 2017年3月17日
	 */
	 BaseResp<Object> updateOrderStatus(Long userid, String orderid, 
			 String orderstatus);
	 
	 
	//--------------------------------adminservice调用-------------------------------------
	 
	 /**
	 * 兑换商品订单列表(所有的)
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	 * 						为null   则查全部 
	 * @param @param startNo  pageSize
	 * 2017年3月22日
	 */
	 BaseResp<List<ProductOrders>> adminConsumeList(String orderstatus, int startNo, int pageSize);

	/**
	 * 根据orderstatus查询订单列表数量
	 * @title selectConsumeOrderListNum
	 */
	BaseResp<Object> selectConsumeOrderListNum(String orderstatus);

	 /**
	 * 订单详情
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderid 订单业务id  
	 * 2017年3月22日
	 */
	 BaseResp<ProductOrders> adminget(Long userid, String orderid);
	 
	 /**
	 * 设为异常订单
	 * @author yinxc
	 * @param @param orderid 订单业务id 
	 * 2017年3月22日
	 */
	 BaseResp<Object> updateOrdersIsexception(long userid, String orderid);
	 
	 /**
	 * 取消订单
	 * @author yinxc
	 * @param @param orderid 订单业务id 
	 * 2017年3月22日
	 */
	 BaseResp<Object> updateOrdersIsdel(String orderid);
	 
	 /**
	 * @author yinxc
	 * 修改订单备注
     * @param @param orderid 
     * @param @param remark 备注
	 * 2017年3月22日
	 */
	 BaseResp<Object> updateOrdersRemark(String orderid, String remark);
	 
	 /**
	 * @author yinxc
	 * 获取用户不同的订单状态的总数
     * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
     * 						为null   则查全部 
	 * 2017年3月22日
	 */
	 BaseResp<Integer> selectCountOrders(String orderstatus);
	 
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
	BaseResp<Integer> selectCountSearchOrders(String orderstatus, String ordernum, 
    		String username, String screatetime, String ecreatetime);
	 
	 /**
	 * 订单搜索
	 * @author yinxc
	 * 2017年3月24日
	 */
	 BaseResp<List<ProductOrders>> searchList(String orderstatus, String ordernum, 
			 String username, String screatetime, String ecreatetime, 
			 int startNo, int pageSize);
	 
	 /**
	 * 获取异常订单列表
	 * @author yinxc
	 * 2017年3月24日
	 * @param startNo 
	 * @param pageSize 
	 */
	 BaseResp<List<ProductOrders>> exceptionlist(int startNo, int pageSize);
    
     /**
	 * 订单发货
	 * @author yinxc
	 * 2017年3月24日
	 */
	 BaseResp<Object> updateDeliver(long userid, String orderid, String logisticscode, String logisticscompany);
	 
	 /**
	 * 获取异常订单总数
	 * @author yinxc
	 * 2017年3月24日
	 */
	 BaseResp<Integer> selectCountException();

	/**
	 * 系统自动确认收货
	 * @param currentDate
	 * @return
     */
	BaseResp<Object> autoConfirmReceipt(Date currentDate);

	/**
	 * 微信扫码支付
	 * @param price
	 * @param remark 备注
	 * @param orderid
	 * @param ip
     * @return
     */
	BaseResp<Object> weixinSaoMa(String price, String remark, String orderid, String ip,String userid);

	/**
	 * 支付宝扫码支付
	 * @param price
	 * @param ordernum
     * @return
     */
	BaseResp<Object> aliPaySaoMa(String price, String ordernum,String userid);



	/**
	 * 获取指定用户订单数量
	 * @param userid
	 * @return
	 */
	BaseResp<Integer> selectTotalOrderNum(String userid);

	/**
	 * 获取用户订单进步币总数
	 * @param userid
	 * @return
	 */
	BaseResp<Integer> selectTotalOrderCoinNum(String userid);



}
