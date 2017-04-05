package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ProductBasic;
import com.longbei.appservice.entity.ProductCart;
import com.longbei.appservice.entity.ProductCategory;

public interface ProductService {
	
	/**
	 * @author yinxc
	 * 获得不同商品类目及其子类，id=-1则获取全部类别列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param level  用户等级
	 */
	BaseResp<List<ProductCategory>> category(Long userid);
	
	/**
	 * @author yinxc
	 * 获取商品列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param starttime 日期
	 * @param startNo pageSize
	 */
	 BaseResp<List<ProductBasic>> list(Long userid, Long cateid, 
			 String starttime, int startNo, int pageSize);

	 /**
	 * @author yinxc
	 * 获取商品详情
	 * 2017年3月14日
	 * @param userid 用户id
	 * @param productid 商品id
	 */
	 BaseResp<ProductBasic> selectProduct(Long userid, String productid);
	 
	 /**
	 * @author yinxc
	 * 添加商品到购物车
	 * 2017年3月15日
	 * @param userid 用户id
     * @param productid 商品id
     * @param productcount 商品数量 （默认 为1）
     * @param enabled  商品状态  是否下架 0 下架 1 未下架（默认为 1）
	 */
	 BaseResp<Object> addCart(Long userid, String productid, int productcount, String enabled);
	 
	 /**
	 * @author yinxc
	 * 批量删除购物车
	 * 2017年3月15日
	 * @param userid
	 * @param ids 购物车ids
	 */
	 BaseResp<Object> removeCart(Long userid, String ids);
	 
	 /**
	 * @author yinxc
	 * 根据userid清空购物车信息
	 * 2017年3月15日
	 * @param userid
	 */
	 BaseResp<Object> clearCart(Long userid);
	 
	 /**
	 * 根据状态和用户id获取购物车列表
	 * @author yinxc
	 * @param userid
	 * @param startNo 
	 * @param pageSize 
	 * 2017年3月15日
	 */
	 BaseResp<List<ProductCart>> getCart(Long userid, int startNo, int pageSize);
	 
	 /**
	 * @author yinxc
	 * 修改购物车商品数量
	 * 2017年3月15日
	 * @param id 购物车id
	 * @param productcount 商品数量
	 */
	 BaseResp<Object> updateCartProductcount(int id, int productcount);

	/**
	 * @Title: selectProductList
	 * @Description: 按条件查询商品列表
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	BaseResp<Object> selectProductList(ProductBasic productBasic,String startNum,String pageSize);

	/**
	 * @Title: selectListCount
	 * @Description: 查询商品列表总数
	 * @auther IngaWu
	 * @currentdate:2017年3月30日
	 */
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
	BaseResp<Object> updateProductByProductId(String productId,String productcate,String productname,String productbriefphotos,
											  String productprice,String productpoint, String lowimpicon, String productbrief,String enabled,String productdetail);

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
	BaseResp<Object> insertProduct(String productcate,String productname,String productbriefphotos,
								   String productprice,String productpoint, String lowimpicon, String productbrief,String enabled,String productdetail);

	/**
	 * @Title: deleteProductByProductId
	 * @Description: 删除商品
	 * @param  @param productId
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	BaseResp<Object> deleteProductByProductId(String productId);

	/**
	 * @Title: selectProductByProductId
	 * @Description: 通过商品id查看商品详情
	 * @param  @param productId 商品id
	 * @param @param code 0
	 * @auther IngaWu
	 * @currentdate:2017年3月22日
	 */
	BaseResp<Object> selectProductByProductId(String productId);
}
