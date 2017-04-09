package com.longbei.appservice.service.api.productservice;

import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.*;
import com.longbei.pay.weixin.res.ResponseHandler;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface IProductBasicService {
	
	/**
	 * @author yinxc
	 * 获得不同商品类目及其子类，id=-1则获取全部类别列表
	 * 2017年3月15日
	 * @param userid 用户id
	 * @param level  用户等级
	 */
	 @RequestLine("GET /product/category?userid={userid}&level={level}")
	 BaseResp<List<ProductCategory>> category(@Param("userid") Long userid,
			 @Param("level") String level);

	 /**
	 * @author yinxc
	 * 获取商品列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param starttime 日期
	 * @param startNo pageSize
	 */
	 @RequestLine("GET /product/list?userid={userid}&cateid={cateid}&level={level}&starttime={starttime}&startNo={startNo}&pageSize={pageSize}")
	 BaseResp<List<ProductBasic>> list(@Param("userid") Long userid, @Param("cateid") Long cateid, @Param("level") String level,
			 @Param("starttime") String starttime, @Param("startNo") int startNo, @Param("pageSize") int pageSize);

	 /**
	 * @author yinxc
	 * 获取商品详情
	 * 2017年3月14日
	 * @param userid 用户id
	 * @param productid 商品id
	 */
	 @RequestLine("GET /product/getProduct?userid={userid}&productid={productid}&discount={discount}")
	 BaseResp<ProductBasic> getProduct(@Param("userid") Long userid, @Param("productid") String productid,
			 @Param("discount") double discount);

	 /**
	 * @author yinxc
	 * 添加商品到购物车
	 * 2017年3月15日
	 * @param userid 用户id
     * @param productid 商品id
     * @param productcount 商品数量 （默认 为1）
     * @param enabled  商品状态  是否下架 0 下架 1 未下架（默认为 1）
	 */
	 @RequestLine("GET /product/addCart?userid={userid}&productid={productid}&productcount={productcount}&enabled={enabled}")
	 BaseResp<Object> addCart(@Param("userid") Long userid, @Param("productid") String productid,
			 @Param("productcount") int productcount, @Param("enabled") String enabled);

	 /**
	 * @author yinxc
	 * 批量删除购物车
	 * 2017年3月15日
	 * @param userid
	 * @param ids 购物车ids
	 */
	 @RequestLine("GET /product/removeCart?userid={userid}&ids={ids}")
	 BaseResp<Object> removeCart(@Param("userid") Long userid, @Param("ids") String ids);

	 /**
	 * @author yinxc
	 * 根据userid清空购物车信息
	 * 2017年3月15日
	 * @param userid
	 */
	 @RequestLine("GET /product/clearCart?userid={userid}")
	 BaseResp<Object> clearCart(@Param("userid") Long userid);

	 /**
	 * 根据状态和用户id获取购物车列表
	 * @author yinxc
	 * @param userid
	 * @param startNo
	 * @param pageSize
	 * 2017年3月15日
	 */
	 @RequestLine("GET /product/getCart?userid={userid}&startNo={startNo}&pageSize={pageSize}")
	 BaseResp<List<ProductCart>> getCart(@Param("userid") Long userid, @Param("startNo") int startNo, @Param("pageSize") int pageSize);

	 /**
	 * @author yinxc
	 * 修改购物车商品数量
	 * 2017年3月15日
	 * @param id 购物车id
	 * @param productcount 商品数量
	 */
	 @RequestLine("GET /product/updateCartProductcount?id={id}&productcount={productcount}")
	 BaseResp<Object> updateCartProductcount(@Param("id") int id, @Param("productcount") int productcount);
	 
	 
	 
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
	 @RequestLine("POST /order/create?userid={userid}&username={username}&productidss={productidss}&numberss={numberss}&address={address}&receiver={receiver}&mobile={mobile}&impiconprice={impiconprice}&moneyprice={moneyprice}&paytype={paytype}&prices={prices}&otype={otype}&remark={remark}&discount={discount}")
	 BaseResp<Object> create(@Param("userid") Long userid, @Param("username") String username, 
			 @Param("productidss") String productidss, 
			 @Param("numberss") String numberss, @Param("address") String address, 
			 @Param("receiver") String receiver, @Param("mobile") String mobile, 
			 @Param("impiconprice") String impiconprice, @Param("moneyprice") String moneyprice, 
			 @Param("paytype") String paytype, @Param("prices") String prices, 
			 @Param("otype") String otype, @Param("remark") String remark, 
			 @Param("discount") String discount);
	 
	 /**
	 * 我的订单列表
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	 * 						为null   则查全部 
	 * @param @param startNo  pageSize
	 * 2017年3月17日
	 */
	 @RequestLine("GET /order/list?userid={userid}&orderstatus={orderstatus}&startNo={startNo}&pageSize={pageSize}")
	 BaseResp<List<ProductOrders>> list(@Param("userid") Long userid, @Param("orderstatus") String orderstatus, 
			 @Param("startNo") int startNo, @Param("pageSize") int pageSize);
	 
	 /**
	 * 订单详情
	 * @author yinxc
	 * @param @param orderid 订单业务id  
	 * 2017年3月17日
	 */
	 @RequestLine("GET /order/get?userid={userid}&orderid={orderid}")
	 BaseResp<ProductOrders> get(@Param("userid") Long userid, @Param("orderid") String orderid);
	 
	 /**
	 * @author yinxc
	 * 购物车结算(用户龙币，进步币兑换商品)
	 * 2017年4月5日
	 * @param orderid 订单业务id
	 */
	 @RequestLine("GET /order/buyOrder?userid={userid}&orderid={orderid}")
	 BaseResp<Object> buyOrder(@Param("userid") Long userid, @Param("orderid") String orderid);
	 
	 /**
	 * @author yinxc
	 * 购买龙币---生成订单
	 * @param userid 用户id
	 * @param number 购买的龙币数量
	 * @auther yinxc
     * @desc  
     * @currentdate:2017年4月7日
	 */
	 @RequestLine("POST /order/buyMoney?userid={userid}&number={number}&username={username}")
	 BaseResp<ProductOrders> buyMoney(@Param("userid") long userid, @Param("number") Integer number, 
			 @Param("username") String username);
	 
	 /**
	 * 再次兑换
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderid 订单业务id 
	 * @param @param discount 用户等级所享受的折扣 
	 * 2017年3月17日
	 */
	 @RequestLine("GET /order/exchange?userid={userid}&orderid={orderid}&discount={discount}")
	 BaseResp<Object> exchange(@Param("userid") Long userid, @Param("orderid") String orderid, @Param("discount") String discount);
	 
	 /**
	 * 修改订单状态
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderid 订单业务id 
	 * @param @param orderstatus  订单状态   0：待付款   1：待发货   2：待收货  3：已完成 
	 * 2017年3月17日
	 */
	 @RequestLine("GET /order/updateOrderStatus?userid={userid}&orderid={orderid}&orderstatus={orderstatus}")
	 BaseResp<Object> updateOrderStatus(@Param("userid") Long userid, @Param("orderid") String orderid, 
			 @Param("orderstatus") String orderstatus);
	 
	 
	 
	 
	 //------------------------支付接口------------------------------------
	 
	 /**
	 * 微信支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * 2017年3月20日
	 */
	 @RequestLine("POST /new_wxpay/wxPayMainPage?orderid={orderid}")
	 BaseResp<Object> wxPayMainPage(@Param("orderid") String orderid);
	 
	 /**
	 * 支付宝支付
	 * @author yinxc
	 * @param @param orderid 订单编号
	 * @param @param userid 
	 * 2017年3月20日
	 */
	 @RequestLine("POST /pay/signature?userid={userid}&orderid={orderid}")
	 BaseResp<Object> signature(@Param("userid") Long userid, @Param("orderid") String orderid);
	 
	 /**
	 * 支付宝支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月20日
	 */
	 @RequestLine("POST /notify/verify/ali?orderType={orderType}")
	 BaseResp<Object> verifyali(@Param("orderType") String orderType, Map<String, String> resMap);
	 
	 /**
	 * 微信支付回调
	 * @author yinxc
	 * @param @param orderType 2：购买龙币
	 * 2017年3月21日
	 */
	 @RequestLine("POST /notify/verify/wx?orderType={orderType}")
	 BaseResp<Object> verifywx(@Param("orderType") String orderType, ResponseHandler resHandler);


	/**
	 * @Title: selectProductList
	 * @Description: 按条件查询商品列表
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestLine("POST /api/product/selectProductList?startNum={startNum}&pageSize={pageSize}")
	@Headers("Content-Type: application/json")
	BaseResp<Object> selectProductList(ProductBasic productBasic,
									   @Param("startNum") String startNum,
									   @Param("pageSize") String pageSize);

	/**
	 * @Title: selectListCount
	 * @Description: 查询商品列表总数
	 * @auther IngaWu
	 * @currentdate:2017年3月30日
	 */
	@RequestLine("POST /api/product/selectListCount")
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
	@RequestLine("POST /api/product/updateProductByProductId?productId={productId}&productcate={productcate}&productname={productname}&productbriefphotos={productbriefphotos}&productprice={productprice}&productpoint={productpoint}&lowimpicon={lowimpicon}&productbrief={productbrief}&enabled={enabled}&productdetail={productdetail}")
	BaseResp<Object> updateProductByProductId(@Param("productId")String productId,@Param("productcate")String productcate,@Param("productname")String productname,@Param("productbriefphotos")String productbriefphotos,
											  @Param("productprice")String productprice,@Param("productpoint")String productpoint,@Param("lowimpicon") String lowimpicon, @Param("productbrief")String productbrief,@Param("enabled")String enabled,@Param("productdetail")String productdetail);

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
	@RequestLine("POST /api/product/insertProduct?productcate={productcate}&productname={productname}&productbriefphotos={productbriefphotos}&productprice={productprice}&productpoint={productpoint}&lowimpicon={lowimpicon}&productbrief={productbrief}&enabled={enabled}&productdetail={productdetail}")
	BaseResp<Object> insertProduct(@Param("productcate")String productcate,@Param("productname")String productname,@Param("productbriefphotos")String productbriefphotos,
								   @Param("productprice")String productprice,@Param("productpoint")String productpoint, @Param("lowimpicon")String lowimpicon,
								   @Param("productbrief") String productbrief,@Param("enabled")String enabled,@Param("productdetail")String productdetail);

	/**
	 * @Title: deleteProductByProductId
	 * @Description: 删除商品
	 * @param  @param productId
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@RequestLine("POST /api/product/deleteProductByProductId?productId={productId}")
	BaseResp<Object> deleteProductByProductId(@Param("productId")String productId);


	/**
	 * @Title: selectProductByProductId
	 * @Description: 通过商品id查看商品详情
	 * @auther IngaWu
	 * @currentdate:2017年3月22日
	 */
	@RequestLine("POST /api/product/selectProductByProductId?productId={productId}")
	BaseResp<Object> selectProductByProductId(@Param("productId")String productId);
	 
	 
	 //--------------------------------adminservice调用-------------------------------------
	 
	 /**
	 * 我的订单列表(所有的)
	 * @author yinxc
	 * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
	 * 						为null   则查全部 
	 * @param @param startNo  pageSize
	 * 2017年3月22日
	 */
	 @RequestLine("GET /api/order/adminlist?orderstatus={orderstatus}&startNo={startNo}&pageSize={pageSize}")
	 BaseResp<List<ProductOrders>> adminlist(@Param("orderstatus") String orderstatus, 
			 @Param("startNo") int startNo, @Param("pageSize") int pageSize);
	 
	 /**
	 * 订单详情
	 * @author yinxc
	 * @param @param userid 
	 * @param @param orderid 订单业务id  
	 * 2017年3月22日
	 */
	 @RequestLine("GET /api/order/adminget?userid={userid}&orderid={orderid}")
	 BaseResp<ProductOrders> adminget(@Param("userid") Long userid, @Param("orderid") String orderid);
	 
	 /**
	 * 设为异常订单
	 * @author yinxc
	 * @param @param orderid 订单业务id 
	 * 2017年3月22日
	 */
	 @RequestLine("GET /api/order/updateOrdersIsexception?orderid={orderid}")
	 BaseResp<Object> updateOrdersIsexception(@Param("orderid") String orderid);
	 
	 /**
	 * 取消订单
	 * @author yinxc
	 * @param @param orderid 订单业务id 
	 * 2017年3月22日
	 */
	 @RequestLine("GET /api/order/updateOrdersIsdel?orderid={orderid}")
	 BaseResp<Object> updateOrdersIsdel(@Param("orderid") String orderid);
	 
	 /**
	 * @author yinxc
	 * 获取用户不同的订单状态的总数
     * @param @param userid 
     * @param @param orderstatus 订单状态   0：待付款   1：待发货   2：待收货  3：已完成    
     * 						为null   则查全部 
	 * 2017年3月22日
	 */
	 @RequestLine("GET /api/order/selectCountOrders?orderstatus={orderstatus}")
	 BaseResp<Integer> selectCountOrders(@Param("orderstatus") String orderstatus);
	 
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
	 @RequestLine("POST /api/order/selectCountSearchOrders?orderstatus={orderstatus}&ordernum={ordernum}&username={username}&screatetime={screatetime}&ecreatetime={ecreatetime}")
	 BaseResp<Integer> selectCountSearchOrders(@Param("orderstatus") String orderstatus, @Param("ordernum") String ordernum, 
			 @Param("username") String username, @Param("screatetime") String screatetime, @Param("ecreatetime") String ecreatetime);
	 
	 /**
	 * 订单搜索
	 * @author yinxc
	 * 2017年3月24日
	 */
	 @RequestLine("POST /api/order/searchList?orderstatus={orderstatus}&ordernum={ordernum}&username={username}&screatetime={screatetime}&ecreatetime={ecreatetime}&startNo={startNo}&pageSize={pageSize}")
	 BaseResp<List<ProductOrders>> searchList(@Param("orderstatus") String orderstatus, @Param("ordernum") String ordernum, 
			 @Param("username") String username, @Param("screatetime") String screatetime, @Param("ecreatetime") String ecreatetime, 
			 @Param("startNo") int startNo, @Param("pageSize") int pageSize);
	 
	 /**
	 * 获取异常订单列表
	 * @author yinxc
	 * 2017年3月24日
	 * @param startNo 
	 * @param pageSize 
	 */
	 @RequestLine("GET /api/order/exceptionlist?startNo={startNo}&pageSize={pageSize}")
	 BaseResp<List<ProductOrders>> exceptionlist(@Param("startNo") int startNo, @Param("pageSize") int pageSize);
    
     /**
	 * 订单发货
	 * @author yinxc
	 * 2017年3月24日
	 */
	 @RequestLine("POST /api/order/updateDeliver?orderid={orderid}&logisticscode={logisticscode}")
	 @Headers("Content-Type: application/json")
	 BaseResp<Object> updateDeliver(@Param("orderid") String orderid, @Param("logisticscode") String logisticscode, 
			 String logisticscompany);
	 
	 /**
	 * @author yinxc
	 * 修改订单备注
     * @param @param orderid 
     * @param @param remark 备注
	 * 2017年3月22日
	 */
	 @RequestLine("GET /api/order/updateOrdersRemark?orderid={orderid}")
	 @Headers("Content-Type: application/json")
	 BaseResp<Object> updateOrdersRemark(@Param("orderid") String orderid, String remark);
		 
	 
	 /**
	 * 获取异常订单总数
	 * @author yinxc
	 * 2017年3月24日
	 */
	 @RequestLine("GET /api/order/selectCountException")
	 BaseResp<Integer> selectCountException();

	/**
	 * 查询需要自动确认
	 * @param beforeDateTime 查询多久之前发货的 时间
	 * @return
	 */
	@RequestLine("GET /order/selectAutoReceiptOrder?beforeDateTime={beforeDateTime}")
	BaseResp<List<ProductOrders>> selectAutoReceiptOrder(@Param("beforeDateTime")Long beforeDateTime);

	/**
	 * 修改系统自动收货的订单状态
	 * @param beforeDateTime
	 * @return
     */
	@RequestLine("GET /order/updateOrderAutoConfirmReceipt?beforeDateTime={beforeDateTime}")
	BaseResp<Object> updateOrderAutoConfirmReceipt(@Param("beforeDateTime")Long beforeDateTime);
}
