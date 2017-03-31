package com.longbei.appservice.service.api.productservice;

import com.longbei.appservice.common.BaseResp;

import com.longbei.appservice.entity.ProductOrderInfo;
import feign.Param;
import feign.RequestLine;

import java.util.List;
import java.util.Map;

public interface IProductCategoryService {
    /**
     * @Title: selectCategoryByCateId
     * @Description: 查询单个商品类目详情
     * @param @param cateId 商品类目id
     * @auther IngaWu
     * @currentdate:2017年3月19日
     */
    @RequestLine("GET /product/selectCategoryByCateId?cateId={cateId}")
    BaseResp<Object> selectCategoryByCateId(@Param("cateId")String cateId);

	/**
	 * @Title: roductCategories
	 * @Description: 获取商品类目树形集合
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestLine("GET /product/productCategories")
	BaseResp<Object> productCategories();

	/**
	 * @Title: selectCategory
	 * @Description: 查找类目列表(通过父级类目id)
	 * @param @param parentid 父级类目编号 (通过parentid=1可查全部一级类目id，一级类目id可查其全部二级类目id，后面级别以此类推)
	 * @param @param startNum分页起始值，pageSize每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年3月29日
	 */
	@RequestLine("GET /product/selectCategory?parentid={parentid}&startNum={startNum}&pageSize={pageSize}")
	BaseResp<Object> selectCategory(@Param("parentid") String parentid,@Param("startNum") String startNum,@Param("pageSize")String pageSize);

	/**
	 * @Title: insertCategory
	 * @Description: 添加商品类目
	 * @param @param catename 商品类目名称
	 * @param @param parentid 商品类目父类id
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
    @RequestLine("GET /product/insertCategory?catename={catename}&parentid={parentid}")
	BaseResp<Object> insertCategory(@Param("catename") String catename,
									@Param("parentid") String parentid);

    /**
     * @Title: updateCategoryByCateId
     * @param @param cateId 商品类目id
     * @Description: 编辑商品类目的名称或排序号
     * @param @param catename 商品类目名称
     * @param @param sort 排序号
     * @auther IngaWu
     * @currentdate:2017年3月19日
     */
    @RequestLine("GET /product/updateCategoryByCateId?cateId={cateId}&catename={catename}&sort={sort}")
    BaseResp<Object> updateCategoryByCateId(@Param("cateId")String cateId,@Param("catename")String catename,@Param("sort")String sort);

    /**
     * @Title:deleteCategoryByCateId
     * @Description: 删除商品类目
     * @param @param id 商品类目id
     * @auther IngaWu
     * @currentdate:2017年3月19日
     */
    @RequestLine("GET /product/deleteCategoryByCateId?cateId={cateId}")
    BaseResp<Object> deleteCategoryByCateId(@Param("cateId")String cateId);

}
