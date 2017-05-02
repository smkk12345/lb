package com.longbei.appservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.service.api.productservice.IProductBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserLevelMapper;
import com.longbei.appservice.entity.ProductBasic;
import com.longbei.appservice.entity.ProductCart;
import com.longbei.appservice.entity.ProductCategory;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
import com.longbei.appservice.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserLevelMapper userLevelMapper;
	@Autowired
	private IProductBasicService iProductBasicService;

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
				level = userInfo.getGrade() + "";
				totalcoin = userInfo.getTotalcoin();
			}
			
			
			baseResp = iProductBasicService.category(userid, level);
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
				level = userInfo.getGrade() + "";
				baseResp = iProductBasicService.list(userid, cateid, level, starttime, startNo, pageSize);
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
				baseResp = iProductBasicService.getProduct(userid, productid, userLevel.getDiscount());
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
			baseResp = iProductBasicService.addCart(userid, productid, productcount, enabled);
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
			baseResp = iProductBasicService.removeCart(userid, ids);
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
			baseResp = iProductBasicService.clearCart(userid);
		}catch (Exception e){
			logger.error("clearCart userid = {}", userid, e);
		}
		return baseResp;
	}
	

	@Override
	public BaseResp<Object> emptyCart(long userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		Map<String, Object> expandData = new HashMap<>();
		try{
			BaseResp<List<ProductCart>> resp = new BaseResp<>();
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				resp = iProductBasicService.getCart(userid, userLevel.getDiscount(), 0, 1);
			}
//			BaseResp<List<ProductCart>> resp = iProductBasicService.getCart(userid, 0, 1);
			String isempty = "0";
			if(ResultUtil.isSuccess(resp)){
				List<ProductCart> list = resp.getData();
				if(null != list && list.size()>0){
					isempty = "1";
				}
			}
			expandData.put("isempty", isempty);
			baseResp.setExpandData(expandData);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}catch (Exception e){
			logger.error("emptyCart userid = {}", userid, e);
		}
		return baseResp;
	}


	@Override
	public BaseResp<List<ProductCart>> getCart(Long userid, int startNo, int pageSize) {
		BaseResp<List<ProductCart>> baseResp = new BaseResp<>();
		try{
			UserInfo userInfo = userInfoMapper.selectInfoMore(userid);
			if(null != userInfo){
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				baseResp = iProductBasicService.getCart(userid, userLevel.getDiscount(), startNo, pageSize);
//				baseResp = iProductBasicService.getProduct(userid, productid, );
			}
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
			baseResp = iProductBasicService.updateCartProductcount(id, productcount);
		}catch (Exception e){
			logger.error("updateCartProductcount id = {}, productcount = {}", id, productcount, e);
		}
		return baseResp;
	}


	@Override
	public BaseResp<Page<ProductBasic>> selectProductList(ProductBasic productBasic, String startNum, String pageSize) {
		BaseResp<Page<ProductBasic>> baseResp = new BaseResp<Page<ProductBasic>>();
		try {
			baseResp = iProductBasicService.selectProductList(productBasic,startNum,pageSize);
		} catch (Exception e) {
			logger.error("selectProductList error and msg={}",e);
		}
		return baseResp;
	}

@Override
public 	BaseResp<Object> updateProductByProductId(ProductBasic productBasic) {
	BaseResp<Object> baseResp = new BaseResp<Object>();
	try {
			baseResp = iProductBasicService.updateProductByProductId(productBasic);
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
			baseResp = iProductBasicService.insertProduct(productcate, productname,productbriefphotos,
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
			baseResp = iProductBasicService.deleteProductByProductId(productId);
		} catch (Exception e) {
			logger.error("deleteProductByProductId error and msg={}",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<ProductBasic> selectProductByProductId(String productId) {
		BaseResp<ProductBasic> baseResp = new BaseResp<ProductBasic>();
		try {
			baseResp = iProductBasicService.selectProductByProductId(productId);
		}
		catch (Exception e) {
			logger.error("selectProductByProductId error and msg={}",e);
		}
		return baseResp;
	}



}
