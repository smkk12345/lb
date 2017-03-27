package com.longbei.appservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.service.ProductService;
import com.longbei.appservice.service.ProductCategoryService;

@RestController
@RequestMapping(value = "product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	private static Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/product/category
    * @Description: 获取商品类别
    * @param @param userid  
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @desc  map：totalcoin：进步币数量
    * @currentdate:2017年3月13日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/category")
    public BaseResp<Object> category(String userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.category(Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("category userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/list
    * @Description: 获取商品列表
    * @param @param userid 用户id
    * @param @param cateid 商品类别id(获取全部的时候 cateid=1)
    * @param @param starttime
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/list")
    public BaseResp<Object> list(String userid, String cateid, String starttime, int startNo, int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, cateid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.list(Long.parseLong(userid), cateid, starttime, startNo, pageSize);
		} catch (Exception e) {
			logger.error("list userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/getProduct
    * @Description: 获取商品详情
    * @param @param userid 用户id
    * @param @param productid 商品id
    * @param @param discount 用户等级所享受的折扣
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月13日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/getProduct")
    public BaseResp<Object> getProduct(String userid, String productid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, productid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.selectProduct(Long.parseLong(userid), productid);
		} catch (Exception e) {
			logger.error("getProduct userid = {}, productid = {}", userid, productid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/addCart
    * @Description: 添加商品到购物车
    * @param @param userid 用户id
    * @param @param productid 商品id
    * @param @param productcount 商品数量 （默认 为1）
    * @param @param enabled  商品状态  是否下架 0 下架 1 未下架（默认为 1）
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/addCart")
    public BaseResp<Object> addCart(String userid, String productid, String productcount, String enabled) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, productid, productcount, enabled)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.addCart(Long.parseLong(userid), productid, Integer.parseInt(productcount), enabled);
		} catch (Exception e) {
			logger.error("addCart userid = {}, productid = {}, productcount = {}, enabled = {}", 
					userid, productid, productcount, enabled, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/removeCart
    * @Description: 将商品从购物车中移除
    * @param @param userid 用户id
    * @param @param ids 购物车id  (多个以','隔开)
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/removeCart")
    public BaseResp<Object> removeCart(String userid, String ids) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, ids)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.removeCart(Long.parseLong(userid), ids);
		} catch (Exception e) {
			logger.error("removeCart userid = {}, ids = {}", 
					userid, ids, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/clearCart
    * @Description: 清空购物车
    * @param @param userid 用户id
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/clearCart")
    public BaseResp<Object> clearCart(String userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.clearCart(Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("clearCart userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/getCart
    * @Description: 获取购物车
    * @param @param userid 用户id
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/getCart")
    public BaseResp<Object> getCart(String userid, int startNo, int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.getCart(Long.parseLong(userid), startNo, pageSize);
		} catch (Exception e) {
			logger.error("getCart userid = {}, startNo = {}, pageSize = {}", 
					userid, startNo, pageSize, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/product/updateCartProductcount
    * @Description: 修改购物车中商品的数量
    * @param @param id 购物车中该商品所在位置id
    * @param @param productcount 商品的数量
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/updateCartProductcount")
    public BaseResp<Object> updateCartProductcount(String id, String productcount) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
  		if (StringUtils.hasBlankParams(id, productcount)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.updateCartProductcount(Integer.parseInt(id), Integer.parseInt(productcount));
		} catch (Exception e) {
			logger.error("updateCartProductcount id = {}, productcount = {}", 
					id, productcount, e);
		}
  		return baseResp;
	}

}
