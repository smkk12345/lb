package com.longbei.appservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ProductBasic;
import com.longbei.appservice.entity.ProductCart;
import com.longbei.appservice.entity.ProductCategory;
import com.longbei.appservice.service.ProductService;
//import com.longbei.appservice.service.ProductCategoryService;

@RestController
@RequestMapping(value = "product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
//	@Autowired
//	private ProductCategoryService productCategoryService;

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
    public BaseResp<List<ProductCategory>> category(String userid) {
		logger.info("category userid = {}", userid);
		BaseResp<List<ProductCategory>> baseResp = new BaseResp<>();
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
    * @param @param starttime 上架时间    为null查全部  
    * @param @param startNum   pageSize
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
  	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list")
    public BaseResp<List<ProductBasic>> list(String userid, String cateid, String starttime, Integer startNum, Integer pageSize) {
		logger.info("selectCategoryList cateid = {}, userid = {}, starttime = {}, startNum = {}, pageSize = {}",
				cateid, userid, starttime, startNum, pageSize);
		BaseResp<List<ProductBasic>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, cateid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNum){
  				sNo = startNum.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			if(cateid.indexOf(".0") != -1){
  				cateid = cateid.substring(0, cateid.length()-2);
  			}
  			baseResp = productService.list(Long.parseLong(userid), Long.parseLong(cateid), starttime, sNo, sSize);
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
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @desc  Map: isred 购物车是否显示红点     0：不显示     1：显示
    * @currentdate:2017年3月13日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/getProduct")
    public BaseResp<ProductBasic> getProduct(String userid, String productid) {
		logger.info("userid = {},productid = {}", userid,productid);
		BaseResp<ProductBasic> baseResp = new BaseResp<>();
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
		logger.info("userid = {}, productid = {}, productcount = {}, enabled = {}", userid, productid, productcount, enabled);
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
		logger.info("userid = {}, ids = {}", userid, ids);
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
		logger.info("userid = {}", userid);
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
    * @param @param startNum  pageSize
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings({ "unchecked"})
  	@RequestMapping(value = "/getCart")
    public BaseResp<List<ProductCart>> getCart(String userid, Integer startNum, Integer pageSize) {
		logger.info("userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize);
		BaseResp<List<ProductCart>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNum){
  				sNo = startNum.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			baseResp = productService.getCart(Long.parseLong(userid), sNo, sSize);
		} catch (Exception e) {
			logger.error("getCart userid = {}, startNum = {}, pageSize = {}",
					userid, startNum, pageSize, e);
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
		logger.info("id = {}, productcount = {}", id, productcount);
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
	
	/**
    * @Title: http://ip:port/app_service/product/emptyCart
    * @Description: 购物车是否为空
    * @param @param userid 用户id
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @desc 	Map:  isempty  0:为空     1：有商品
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings({ "unchecked"})
  	@RequestMapping(value = "/emptyCart")
    public BaseResp<Object> emptyCart(String userid) {
		logger.info("userid = {}", userid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = productService.emptyCart(Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("emptyCart userid = {}", userid, e);
		}
  		return baseResp;
	}

}
