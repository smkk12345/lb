package com.longbei.appservice.service;

import java.util.Date;
import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ProductOrders;
import com.longbei.appservice.entity.UserAddress;

public interface OrderService {
	
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
	 BaseResp<Object> create(Long userid, String productidss, 
			 String numberss, String addressid, 
			 String impiconprice, String moneyprice, String paytype, String prices, 
			 String otype, String remark);
	 
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
	 * 我的订单列表(所有的)
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	 * 						为null   则查全部 
	 * @param @param startNo  pageSize
	 * 2017年3月22日
	 */
	 BaseResp<List<ProductOrders>> adminlist(String orderstatus, int startNo, int pageSize);
	 
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
}
