package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.service.ProductCategoryService;
import com.longbei.appservice.service.api.productservice.IProductBasicService;
import com.longbei.appservice.service.api.productservice.IProductCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productCategoryService")
public class ProductCategoryServiceImpl implements ProductCategoryService {


	@Autowired
	private IProductCategoryService iProductCategoryService;
	
	private static Logger logger = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

	@Override
	public BaseResp<Object> selectCategoryByCateId(String cateId) {
		BaseResp<Object> baseResp = iProductCategoryService.selectCategoryByCateId(cateId);
		return baseResp;
	}

	@Override
	public BaseResp<Object> productCategories(){
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			baseResp = iProductCategoryService.productCategories();
		} catch (Exception e) {
			logger.error("select product category list is error:",e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> selectCategory(String parentid,String startNum,String pageSize){
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			baseResp = iProductCategoryService.selectCategory(parentid,startNum,pageSize);
		} catch (Exception e) {
			logger.error("select product category list is error:",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertCategory(String catename,String parentid) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = iProductCategoryService.insertCategory(catename,parentid);
		} catch (Exception e) {
			logger.error("insertCategory error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateCategoryByCateId(String cateId,String catename,String sort) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = iProductCategoryService.updateCategoryByCateId(cateId,catename,sort);
		} catch (Exception e) {
			logger.error("updateCategoryByCateId error and msg={}",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> removeCategoryByCateId(String cateId) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = iProductCategoryService.removeCategoryByCateId(cateId);
		} catch (Exception e) {
			logger.error("removeCategoryByCateId error and msg={}",e);
		}
		return baseResp;
	}
}
