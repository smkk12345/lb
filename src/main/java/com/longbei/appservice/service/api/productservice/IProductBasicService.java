package com.longbei.appservice.service.api.productservice;

import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.*;
import com.longbei.pay.weixin.res.ResponseHandler;

import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("productService")
@RequestMapping("productService")
public interface IProductBasicService {

	/**
	 * @author yinxc
	 * 获得不同商品类目及其子类，id=-1则获取全部类别列表
	 * 2017年3月15日
	 * @param userid 用户id
	 * @param level  用户等级
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/category")
	BaseResp<List<ProductCategory>> category(@RequestParam("userid") Long userid,
											 @RequestParam("level") String level);

	/**
	 * @author yinxc
	 * 获取商品列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param starttime 日期
	 * @param startNo pageSize
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/list")
	BaseResp<List<ProductBasic>> list(@RequestParam("userid") Long userid,
									  @RequestParam("cateid") Long cateid,
									  @RequestParam("level") String level,
									  @RequestParam("starttime") String starttime,
									  @RequestParam("startNo") int startNo,
									  @RequestParam("pageSize") int pageSize);

	/**
	 * @author yinxc
	 * 获取商品详情
	 * 2017年3月14日
	 * @param userid 用户id
	 * @param productid 商品id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/getProduct")
	BaseResp<ProductBasic> getProduct(@RequestParam("userid") Long userid,
									  @RequestParam("productid") String productid,
									  @RequestParam("discount") double discount);

	/**
	 * @author yinxc
	 * 添加商品到购物车
	 * 2017年3月15日
	 * @param userid 用户id
	 * @param productid 商品id
	 * @param productcount 商品数量 （默认 为1）
	 * @param enabled  商品状态  是否下架 0 下架 1 未下架（默认为 1）
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/addCart")
	BaseResp<Object> addCart(@RequestParam("userid") Long userid,
							 @RequestParam("productid") String productid,
							 @RequestParam("productcount") int productcount,
							 @RequestParam("enabled") String enabled);

	/**
	 * @author yinxc
	 * 批量删除购物车
	 * 2017年3月15日
	 * @param userid
	 * @param ids 购物车ids
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/removeCart")
	BaseResp<Object> removeCart(@RequestParam("userid") Long userid,
								@RequestParam("ids") String ids);

	/**
	 * @author yinxc
	 * 根据userid清空购物车信息
	 * 2017年3月15日
	 * @param userid
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/clearCart")
	BaseResp<Object> clearCart(@RequestParam("userid") Long userid);

	/**
	 * 根据状态和用户id获取购物车列表
	 * @author yinxc
	 * @param userid
	 * @param startNo
	 * @param pageSize
	 * 2017年3月15日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/getCart")
	BaseResp<List<ProductCart>> getCart(@RequestParam("userid") Long userid,
										@RequestParam("startNo") int startNo,
										@RequestParam("pageSize") int pageSize);

	/**
	 * @author yinxc
	 * 修改购物车商品数量
	 * 2017年3月15日
	 * @param id 购物车id
	 * @param productcount 商品数量
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/product/updateCartProductcount")
	BaseResp<Object> updateCartProductcount(@RequestParam("id") int id,
											@RequestParam("productcount") int productcount);



	//---------------------------------------------订单接口-------------------------------------------------

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
	 * @param otype 订单类型。0 龙币 1 进步币 2 混排 3 其他
	 * @param remark 备注
	 * @param discount 用户等级所享受的折扣
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/order/create")
	BaseResp<Object> create(@RequestParam("userid") Long userid,
							@RequestParam("username") String username,
							@RequestParam("productidss") String productidss,
							@RequestParam("numberss") String numberss,
							@RequestParam("address") String address,
							@RequestParam("receiver") String receiver,
							@RequestParam("mobile") String mobile,
							@RequestParam("impiconprice") String impiconprice,
							@RequestParam("moneyprice") String moneyprice,
							@RequestParam("paytype") String paytype,
							@RequestParam("prices") String prices,
							@RequestParam("otype") String otype,
							@RequestParam("remark") String remark,
							@RequestParam("discount") String discount);

	/**
	 * 我的订单列表
	 * @author yinxc
	 * @param @param userid
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成
	 * 						为null   则查全部
	 * @param @param startNo  pageSize
	 * 2017年3月17日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/list")
	BaseResp<List<ProductOrders>> list(@RequestParam("userid") Long userid,
									   @RequestParam("orderstatus") String orderstatus,
									   @RequestParam("startNo") int startNo,
									   @RequestParam("pageSize") int pageSize);

	/**
	 * 订单详情
	 * @author yinxc
	 * @param @param orderid 订单业务id
	 * 2017年3月17日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/get")
	BaseResp<ProductOrders> get(@RequestParam("userid") Long userid,
								@RequestParam("orderid") String orderid);

	/**
	 * @author yinxc
	 * 购物车结算(用户龙币，进步币兑换商品)
	 * 2017年4月5日
	 * @param orderid 订单业务id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/buyOrder")
	BaseResp<Object> buyOrder(@RequestParam("userid") Long userid,
							  @RequestParam("orderid") String orderid);

	/**
	 * @author yinxc
	 * 购买龙币---生成订单
	 * @param userid 用户id
	 * @param number 购买的龙币数量
	 * @param paytype 支付方式  0：龙币支付 1：微信支付 2：支付宝支付
	 *                       3:IOS内购测试帐号购买 4：IOS内购正式帐号购买
	 * @auther yinxc
	 * @desc
	 * @currentdate:2017年4月7日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/order/buyMoney")
	BaseResp<ProductOrders> buyMoney(@RequestParam("userid") long userid,
									 @RequestParam("number") Integer number,
									 @RequestParam("username") String username,
									 @RequestParam("paytype") String paytype,
									 @RequestParam("sign") String sign,
									 @RequestParam("price") String price);

	/**
	 * 再次兑换
	 * @author yinxc
	 * @param @param userid
	 * @param @param orderid 订单业务id
	 * @param @param discount 用户等级所享受的折扣
	 * 2017年3月17日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/exchange")
	BaseResp<Object> exchange(@RequestParam("userid") Long userid,
							  @RequestParam("orderid") String orderid,
							  @RequestParam("discount") String discount);

	/**
	 * 修改订单状态
	 * @author yinxc
	 * @param @param userid
	 * @param @param orderid 订单业务id
	 * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成
	 * 2017年3月17日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/updateOrderStatus")
	BaseResp<Object> updateOrderStatus(@RequestParam("userid") Long userid,
									   @RequestParam("orderid") String orderid,
									   @RequestParam("orderstatus") String orderstatus);




	//------------------------支付接口------------------------------------

	/**
	 * 微信支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * 2017年3月20日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/new_wxpay/wxPayMainPage")
	BaseResp<Object> wxPayMainPage(@RequestParam("orderid") String orderid);

	/**
	 * 支付宝支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid
	 * 2017年3月20日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/pay/signature")
	BaseResp<Object> signature(@RequestParam("userid") Long userid,
							   @RequestParam("orderid") String orderid);

	/**
	 * 支付宝支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月20日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/notify/verify/ali")
	BaseResp<Object> verifyali(@RequestParam("orderType") String orderType,
							   Map<String, String> resMap);

	/**
	 * 微信支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月21日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/notify/verify/wx")
	BaseResp<Object> verifywx(@RequestParam("orderType") String orderType,
							  ResponseHandler resHandler);


	/**
	 * @Title: selectProductList
	 * @Description: 按条件查询商品列表
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/selectProductList")
	@Headers("Content-Type: application/json")
	BaseResp<Object> selectProductList(ProductBasic productBasic,
									   @RequestParam("startNum") String startNum,
									   @RequestParam("pageSize") String pageSize);

	/**
	 * @Title: selectListCount
	 * @Description: 查询商品列表总数
	 * @auther IngaWu
	 * @currentdate:2017年3月30日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/selectListCount")
	@Headers("Content-Type: application/json")
	int selectListCount(ProductBasic productBasic);

	/**
	 * @Title: updateProductByProductId
	 * @Description: 编辑商品详情
	 * @param @param productId 商品id
	 * @param @param productcate 商品类目
	 * @param @param productname 商品名称
	 * @param @param productbriefphotos 商品缩略图
	 * @param @param productprice 市场价格
	 * @param @param productpoint 兑换商品所需币
	 * @param @param lowimpicon 最低进步币要求
	 * @param @param productbrief 商品规格
	 * @param @param enabled 商品是否下架 0:已下架  1：未下架
	 * @param @param productdetail 商品详情
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/updateProductByProductId")
	BaseResp<Object> updateProductByProductId(@RequestParam("productId")String productId,
											  @RequestParam("productcate")String productcate,
											  @RequestParam("productname")String productname,
											  @RequestParam("productbriefphotos")String productbriefphotos,
											  @RequestParam("productprice")String productprice,
											  @RequestParam("productpoint")String productpoint,
											  @RequestParam("lowimpicon") String lowimpicon,
											  @RequestParam("productbrief")String productbrief,
											  @RequestParam("enabled")String enabled,
											  @RequestParam("productdetail")String productdetail);

	/**
	 * @Title: insertProduct
	 * @Description: 添加商品
	 * @param @param productcate 商品类目
	 * @param @param productname 商品名称
	 * @param @param productbriefphotos 商品缩略图
	 * @param @param productprice 市场价格
	 * @param @param productpoint 兑换商品所需币
	 * @param @param lowimpicon 最低进步币要求
	 * @param @param productbrief 商品规格
	 * @param @param enabled 商品是否下架 0:已下架  1：未下架
	 * @param @param productdetail 商品详情
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/insertProduct")
	BaseResp<Object> insertProduct(@RequestParam("productcate")String productcate,
								   @RequestParam("productname")String productname,
								   @RequestParam("productbriefphotos")String productbriefphotos,
								   @RequestParam("productprice")String productprice,
								   @RequestParam("productpoint")String productpoint,
								   @RequestParam("lowimpicon")String lowimpicon,
								   @RequestParam("productbrief") String productbrief,
								   @RequestParam("enabled")String enabled,
								   @RequestParam("productdetail")String productdetail);

	/**
	 * @Title: deleteProductByProductId
	 * @Description: 删除商品
	 * @param  @param productId
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/deleteProductByProductId")
	BaseResp<Object> deleteProductByProductId(@RequestParam("productId")String productId);


	/**
	 * @Title: selectProductByProductId
	 * @Description: 通过商品id查看商品详情
	 * @auther IngaWu
	 * @currentdate:2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/selectProductByProductId")
	BaseResp<Object> selectProductByProductId(@RequestParam("productId")String productId);


	//--------------------------------adminservice调用-------------------------------------

	/**
	 * 我的订单列表(所有的)
	 * @author yinxc
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成
	 * 						为null   则查全部
	 * @param @param startNo  pageSize
	 * 2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/adminlist")
	BaseResp<List<ProductOrders>> adminlist(@RequestParam("orderstatus") String orderstatus,
											@RequestParam("startNo") int startNo,
											@RequestParam("pageSize") int pageSize);

	/**
	 * 订单详情
	 * @author yinxc
	 * @param @param userid
	 * @param @param orderid 订单业务id
	 * 2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/adminget")
	BaseResp<ProductOrders> adminget(@RequestParam("userid") Long userid,
									 @RequestParam("orderid") String orderid);

	/**
	 * 设为异常订单
	 * @author yinxc
	 * @param @param orderid 订单业务id
	 * 2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/updateOrdersIsexception")
	BaseResp<Object> updateOrdersIsexception(@RequestParam("orderid") String orderid);

	/**
	 * 取消订单
	 * @author yinxc
	 * @param @param orderid 订单业务id
	 * 2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/updateOrdersIsdel")
	BaseResp<Object> updateOrdersIsdel(@RequestParam("orderid") String orderid);

	/**
	 * @author yinxc
	 * 获取用户不同的订单状态的总数
	 * @param @param userid
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成
	 * 						为null   则查全部
	 * 2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/selectCountOrders")
	BaseResp<Integer> selectCountOrders(@RequestParam("orderstatus") String orderstatus);

	/**
	 * 获取订单搜索的总数
	 * @author yinxc
	 * 2017年3月24日
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成
	 * 						为null   则查全部
	 * @param @param ordernum 订单编号
	 * @param @param username 用户手机号
	 * @param @param screatetime    ecreatetime下单搜索时间段
	 * @param @param startNo pageSize
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/order/selectCountSearchOrders")
	BaseResp<Integer> selectCountSearchOrders(@RequestParam("orderstatus") String orderstatus,
											  @RequestParam("ordernum") String ordernum,
											  @RequestParam("username") String username,
											  @RequestParam("screatetime") String screatetime,
											  @RequestParam("ecreatetime") String ecreatetime);

	/**
	 * 订单搜索
	 * @author yinxc
	 * 2017年3月24日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/order/searchList")
	BaseResp<List<ProductOrders>> searchList(@RequestParam("orderstatus") String orderstatus,
											 @RequestParam("ordernum") String ordernum,
											 @RequestParam("username") String username,
											 @RequestParam("screatetime") String screatetime,
											 @RequestParam("ecreatetime") String ecreatetime,
											 @RequestParam("startNo") int startNo,
											 @RequestParam("pageSize") int pageSize);

	/**
	 * 获取异常订单列表
	 * @author yinxc
	 * 2017年3月24日
	 * @param startNo
	 * @param pageSize
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/exceptionlist")
	BaseResp<List<ProductOrders>> exceptionlist(@RequestParam("startNo") int startNo,
												@RequestParam("pageSize") int pageSize);

	/**
	 * 订单发货
	 * @author yinxc
	 * 2017年3月24日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/order/updateDeliver")
	@Headers("Content-Type: application/json")
	BaseResp<Object> updateDeliver(@RequestParam("orderid") String orderid,
								   @RequestParam("logisticscode") String logisticscode,
								   String logisticscompany);

	/**
	 * @author yinxc
	 * 修改订单备注
	 * @param @param orderid
	 * @param @param remark 备注
	 * 2017年3月22日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/updateOrdersRemark")
	@Headers("Content-Type: application/json")
	BaseResp<Object> updateOrdersRemark(@RequestParam("orderid") String orderid, String remark);


	/**
	 * 获取异常订单总数
	 * @author yinxc
	 * 2017年3月24日
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/api/order/selectCountException")
	BaseResp<Integer> selectCountException();

	/**
	 * 查询需要自动确认
	 * @param beforeDateTime 查询多久之前发货的 时间
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/selectAutoReceiptOrder")
	BaseResp<List<ProductOrders>> selectAutoReceiptOrder(@RequestParam("beforeDateTime")Long beforeDateTime);

	/**
	 * 修改系统自动收货的订单状态
	 * @param beforeDateTime
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/order/updateOrderAutoConfirmReceipt")
	BaseResp<Object> updateOrderAutoConfirmReceipt(@RequestParam("beforeDateTime")Long beforeDateTime);
}
