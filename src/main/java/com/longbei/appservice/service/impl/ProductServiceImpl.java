package com.longbei.appservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.entity.ProductBasic;
import com.longbei.appservice.entity.ProductCart;
import com.longbei.appservice.entity.ProductCategory;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.ProductService;
import com.longbei.appservice.service.api.HttpClient;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserLevelMapper userLevelMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public BaseResp<List<ProductCategory>> category(Long userid) {
		BaseResp<List<ProductCategory>> baseResp = new BaseResp<>();
		try{
			String level = "";
			UserInfo userInfo = getLevel(userid);
			Integer totalcoin = 0;
			Map<String, Object> expandData = new HashMap<>();
			if(null != userInfo){
				level = userInfo.getGrade().toString();
				totalcoin = userInfo.getTotalcoin();
			}
			
			
			baseResp = HttpClient.productBasicService.category(userid, level);
			expandData.put("totalcoin", totalcoin);
			baseResp.setExpandData(expandData);
		}catch (Exception e){
			logger.error("selectCategoryList userid = {}", userid, e);
		}
		return baseResp;
	}
	
	
	
	private UserInfo getLevel(long userid){
		UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
		if(null != userInfo){
			return userInfo;
		}
		return null;
	}

	@Override
	public BaseResp<List<ProductBasic>> list(Long userid, Long cateid, String starttime, int startNo,
			int pageSize) {
		BaseResp<List<ProductBasic>> baseResp = new BaseResp<>();
		try{
			String level = "";
			UserInfo userInfo = getLevel(userid);
			if(null != userInfo){
				level = userInfo.getGrade().toString();
				baseResp = HttpClient.productBasicService.list(userid, cateid, level, starttime, startNo, pageSize);
			}
		}catch (Exception e){
			logger.error("selectCategoryList cateid = {}, userid = {}, starttime = {}, startNo = {}, pageSize = {}", 
					cateid, userid, starttime, startNo, pageSize, e);
		}
		return baseResp;
	}
	
	@Override
	public BaseResp<ProductBasic> selectProduct(Long userid, String productid) {
		BaseResp<ProductBasic> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				baseResp = HttpClient.productBasicService.getProduct(userid, productid, userLevel.getDiscount());
			}
		}catch (Exception e){
			logger.error("selectProduct userid = {}, productid = {}",userid, productid, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> addCart(Long userid, String productid, int productcount, String enabled) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.addCart(userid, productid, productcount, enabled);
		}catch (Exception e){
			logger.error("addCart userid = {}, productid = {}, productcount = {}, enabled = {}", 
					userid, productid, productcount, enabled, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> removeCart(Long userid, String ids) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.removeCart(userid, ids);
		}catch (Exception e){
			logger.error("removeCart userid = {}, ids = {}", 
					userid, ids, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> clearCart(Long userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.clearCart(userid);
		}catch (Exception e){
			logger.error("clearCart userid = {}", userid, e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<List<ProductCart>> getCart(Long userid, int startNo, int pageSize) {
		BaseResp<List<ProductCart>> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.getCart(userid, startNo, pageSize);
		}catch (Exception e){
			logger.error("getCart userid = {}, startNo = {}, pageSize = {}", 
					userid, startNo, pageSize, e);
		}
		return baseResp;
	}

	
	
	@Override
	public BaseResp<Object> updateCartProductcount(int id, int productcount) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			baseResp = HttpClient.productBasicService.updateCartProductcount(id, productcount);
		}catch (Exception e){
			logger.error("updateCartProductcount id = {}, productcount = {}", id, productcount, e);
		}
		return baseResp;
	}


	@Override
	public BaseResp<Object> selectProductList(String productId,String productcate,String productname,String enabled,String productpoint,String productpoint1,
											  String startNum,String pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = HttpClient.productBasicService.selectProductList(productId,productcate,productname,enabled,productpoint,productpoint1,startNum,pageSize);
		} catch (Exception e) {
			logger.error("selectProductList error and msg={}",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> updateProductByProductId(String productId,String productcate,String productname,String productbriefphotos,
													 String productprice,String productpoint, String lowimpicon, String productbrief,String enabled,String productdetail) {
		BaseResp<Object> baseResp = new BaseResp<Object>();

		try {
			baseResp = HttpClient.productBasicService.updateProductByProductId(productId,productcate, productname,productbriefphotos, productprice,
					productpoint,lowimpicon,productbrief,enabled,productdetail);
		} catch (Exception e) {
			logger.error("updateProductByProductId error and msg={}",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> insertProduct(String productcate,String productname,String productbriefphotos,
										  String productprice,String productpoint, String lowimpicon, String productbrief,String enabled,String productdetail) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = HttpClient.productBasicService.insertProduct(productcate, productname,productbriefphotos,
					productprice, productpoint,lowimpicon,productbrief,enabled,productdetail);
		} catch (Exception e) {
			logger.error("updateProductByProductId error and msg={}",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> deleteProductByProductId(String productId) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = HttpClient.productBasicService.deleteProductByProductId(productId);
		} catch (Exception e) {
			logger.error("deleteProductByProductId error and msg={}",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> selectProductByProductId(String productId) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = HttpClient.productBasicService.selectProductByProductId(productId);
		}
		catch (Exception e) {
			logger.error("selectProductByProductId error and msg={}",e);
		}
		return baseResp;
	}

}
