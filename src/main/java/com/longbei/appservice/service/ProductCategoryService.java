package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

public interface ProductCategoryService {

    /**
     * @Title: selectCategoryByCateId
     * @Description: 查询单个商品类目详情
     * @param @param cateId 商品类目id
     * @auther IngaWu
     * @currentdate:2017年3月19日
     */
    BaseResp<Object> selectCategoryByCateId(String cateId);

	/**
	 * @Title: roductCategories
	 * @Description: 获取商品类目树形集合
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	BaseResp<Object> productCategories();

	/**
	 * @Title: insertCategory
	 * @Description: 添加商品类目
	 * @param @param catename 商品类目名称
	 * @param @param parentid 商品类目父类id
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	BaseResp<Object> insertCategory(String catename, String parentid);

    /**
     * @Title: updateCategoryByCateId
     * @param @param cateId 商品类目id
     * @Description: 编辑商品类目的名称或排序号
     * @param @param catename 商品类目名称
     * @param @param sort 排序号
     * @auther IngaWu
     * @currentdate:2017年3月19日
     */
    BaseResp<Object> updateCategoryByCateId(String cateId,String catename,String sort);

    /**
     * @Title:deleteCategoryByCateId
     * @Description: 删除商品类目
     * @param @param id 商品类目id
     * @auther IngaWu
     * @currentdate:2017年3月19日
     */
    BaseResp<Object> deleteCategoryByCateId(String cateId);
}
