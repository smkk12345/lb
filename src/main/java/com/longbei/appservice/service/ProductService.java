package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

public interface ProductService {
	
	/**
	 * @author yinxc
	 * 获得不同商品类目及其子类，id=-1则获取全部类别列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param level  用户等级
	 */
	BaseResp<Object> category(Long userid, String cateid);
	
	/**
	 * @author yinxc
	 * 获取商品列表
	 * 2017年3月15日
	 * @param cateid 分类id
	 * @param userid 用户id
	 * @param starttime 日期
	 * @param startNo pageSize
	 */
	 BaseResp<Object> list(Long userid, String cateid, 
			 String starttime, int startNo, int pageSize);

	 /**
	 * @author yinxc
	 * 获取商品详情
	 * 2017年3月14日
	 * @param userid 用户id
	 * @param productid 商品id
	 */
	 BaseResp<Object> selectProduct(Long userid, String productid);
	 
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
	 BaseResp<Object> getCart(Long userid, int startNo, int pageSize);
	 
	 /**
	 * @author yinxc
	 * 修改购物车商品数量
	 * 2017年3月15日
	 * @param id 购物车id
	 * @param productcount 商品数量
	 */
	 BaseResp<Object> updateCartProductcount(int id, int productcount);
		 
}
