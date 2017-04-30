package com.longbei.appservice.service.api.productservice;

import com.longbei.appservice.common.BaseResp;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient("productServicewcy")
@RequestMapping("product_service")
public interface IProductCategoryService {
	/**
	 * @Title: selectCategoryByCateId
	 * @Description: 查询单个商品类目详情
	 * @param @param cateId 商品类目id
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/selectCategoryByCateId")
	BaseResp<Object> selectCategoryByCateId(@RequestParam("cateId")String cateId);

	/**
	 * @Title: roductCategories
	 * @Description: 获取商品类目树形集合
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/productCategories")
	BaseResp<Object> productCategories();

	/**
	 * @Title: selectCategory
	 * @Description: 查找类目列表(通过父级类目id)
	 * @param @param parentid 父级类目编号 (通过parentid=1可查全部一级类目id，一级类目id可查其全部二级类目id，后面级别以此类推)
	 * @param @param startNum分页起始值，pageSize每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年3月29日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/selectCategory"
			,produces = MediaType.APPLICATION_JSON_VALUE)
	BaseResp<Object> selectCategory(@RequestParam("parentid") String parentid,
									@RequestParam("startNum") String startNum,
									@RequestParam("pageSize")String pageSize);

	/**
	 * @Title: insertCategory
	 * @Description: 添加商品类目
	 * @param @param catename 商品类目名称
	 * @param @param parentid 商品类目父类id
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/insertCategory")
	BaseResp<Object> insertCategory(@RequestParam("catename") String catename,
									@RequestParam("parentid") String parentid);

	/**
	 * @Title: updateCategoryByCateId
	 * @param @param cateId 商品类目id
	 * @Description: 编辑商品类目的名称或排序号
	 * @param @param catename 商品类目名称
	 * @param @param sort 排序号
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/updateCategoryByCateId")
	BaseResp<Object> updateCategoryByCateId(@RequestParam("cateId")String cateId,
											@RequestParam("catename")String catename,
											@RequestParam("sort")String sort);

	/**
	 * @Title:removeCategoryByCateId
	 * @Description: 删除商品类目
	 * @param @param id 商品类目id
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/api/product/removeCategoryByCateId")
	BaseResp<Object> removeCategoryByCateId(@RequestParam("cateId")String cateId);

}
