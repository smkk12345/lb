package com.longbei.appservice.service.api.productservice;


import com.longbei.appservice.common.BaseResp;

import feign.Param;
import feign.RequestLine;

public interface IProductBasicService {
	
	/**
	 * @author yinxc
	 * 获得不同商品类目及其子类，id=-1则获取全部类别列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param level  用户等级
	 */
	 @RequestLine("GET /product/category?userid={userid}&cateid={cateid}&level={level}")
	 BaseResp<Object> category(@Param("userid") Long userid, @Param("cateid") String cateid, 
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
	 BaseResp<Object> list(@Param("userid") Long userid, @Param("cateid") String cateid, @Param("level") String level, 
			 @Param("starttime") String starttime, @Param("startNo") int startNo, @Param("pageSize") int pageSize);
	 
	 /**
	 * @author yinxc
	 * 获取商品详情
	 * 2017年3月14日
	 * @param userid 用户id
	 * @param productid 商品id
	 */
	 @RequestLine("GET /product/getProduct?userid={userid}&productid={productid}&discount={discount}")
	 BaseResp<Object> getProduct(@Param("userid") Long userid, @Param("productid") String productid, 
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
	 BaseResp<Object> getCart(@Param("userid") Long userid, @Param("startNo") int startNo, @Param("pageSize") int pageSize);
	 
	 /**
	 * @author yinxc
	 * 修改购物车商品数量
	 * 2017年3月15日
	 * @param id 购物车id
	 * @param productcount 商品数量
	 */
	 @RequestLine("GET /product/updateCartProductcount?id={id}&productcount={productcount}")
	 BaseResp<Object> updateCartProductcount(@Param("id") int id, @Param("productcount") int productcount);
	 
	 
	 
}
