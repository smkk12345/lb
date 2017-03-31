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
    public BaseResp<List<ProductCategory>> category(String userid) {
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
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
  	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list")
    public BaseResp<List<ProductBasic>> list(String userid, String cateid, String starttime, Integer startNo, Integer pageSize) {
		BaseResp<List<ProductBasic>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, cateid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNo){
  				sNo = startNo.intValue();
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
    * @param @param startNo  pageSize
    * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年3月15日
	*/
	@SuppressWarnings({ "unchecked"})
  	@RequestMapping(value = "/getCart")
    public BaseResp<List<ProductCart>> getCart(String userid, Integer startNo, Integer pageSize) {
		BaseResp<List<ProductCart>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNo){
  				sNo = startNo.intValue();
  			}
  			if(null != pageSize){
  				sSize = pageSize.intValue();
  			}
  			baseResp = productService.getCart(Long.parseLong(userid), sNo, sSize);
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


	/**
	 * @Title: http://ip:port/app_service/product/selectCategoryByCateId
	 * @Description: 查询单个商品类目详情
	 * @param @param CateId 商品类目id
	 * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectCategoryByCateId")
	public BaseResp<Object> selectCategoryByCateId(String cateId) {
		logger.info("selectCategoryByCateId and cateId={}",cateId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(cateId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productCategoryService.selectCategoryByCateId(cateId);
		} catch (Exception e) {
			logger.error("selectCategoryByCateId and cateId={}",cateId, e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/productCategories
	 * @Description: 获取商品类目树形集合
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(value = "/productCategories")
	public BaseResp<Object> productCategories() {
		try {
			return productCategoryService.productCategories();
		} catch (Exception e) {
			logger.error("productCategories error",e);
		}
		return null;
	}

	/**
	 * @Title: http://ip:port/app_service/product/insertCategory
	 * @Description: 添加商品类目
	 * @param @param catename 商品类目名称
	 * @param @param parentid 商品类目父类id（根类目id为1）
	 * @param @param 正确返回 code 0，  -7为 参数错误，未知错误返回相应状态码
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertCategory")
	public BaseResp<Object> insertCategory(String catename,String parentid) {
		logger.info("insertCategory and catename={},parentid={}",catename,parentid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(parentid,catename)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			return productCategoryService.insertCategory(catename,parentid);
		} catch (Exception e) {
			logger.error("insertCategory and catename={}",catename,parentid,e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/updateCategoryByCateId
	 * @Description: 编辑商品类目的名称或排序号
	 * @param @param cateId 商品类目id
	 * @param @param catename 商品类目名称
	 * @param @param sort 排序号
	 * @param @param 正确返回data(商品类目父类名称)和code 0
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateCategoryByCateId")
	public BaseResp<Object> updateCategoryByCateId(String cateId,String catename,String sort) {
		logger.info("updateCategoryByCateId and cateId={},catename={},sort={}",cateId,catename,sort);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(cateId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productCategoryService.updateCategoryByCateId(cateId,catename,sort);
			return baseResp;
		} catch (Exception e) {
			logger.error("updateCategoryByCateId and cateId={},catename={},sort={}",cateId,catename,sort,e);
		}
		return baseResp;
	}

	/**
	 * @Title:  http://ip:port/app_service/product/deleteCategoryByCateId
	 * @Description: 删除商品类目
	 * @param @param cateId 商品类目id
	 * @param @param 正确返回 code 0 错误返回相应code 和 描述
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteCategoryByCateId")
	public BaseResp<Object> deleteCategoryByCateId(String cateId) {
		logger.info("deleteCategoryByCateId and cateId={}",cateId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(cateId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productCategoryService.deleteCategoryByCateId(cateId);
			return baseResp;
		} catch (Exception e) {
			logger.error("deleteCategoryByCateId and cateId={}",cateId,e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/selectProductList
	 * @Description: 按条件查询商品列表
	 * @param @param productId 商品id
	 * @param @param productcate 商品类目
	 * @param @param productname 商品名称
	 * @param @param enabled 商品是否下架 0:已下架  1：未下架
	 * @param @param productpoint 兑换商品所需币
	 * @param @param productpoint1 兑换商品所需币1
	 * @param @param startNum分页起始值
	 * @param @param pageSize每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年3月19日
	 */
	@RequestMapping(value = "/selectProductList")
	public BaseResp<Object> selectProductList(String productId,String productcate,String productname,String enabled,String productpoint,String productpoint1,
											  String startNum,String pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			logger.info("selectProductList and productId={},productcate={},productname={},enabled={},productpoint={},productpoint1={},startNum={},pageSize={}",
					productId,productcate,productname,enabled,productpoint,productpoint1,startNum,pageSize);
			baseResp = productService.selectProductList(productId,productcate,productname,enabled,productpoint,productpoint1,startNum,pageSize);
			return baseResp;
		} catch (Exception e) {
			logger.error("selectProductList and productId={},productcate={},productname={},enabled={},productpoint={},productpoint1={},startNum={},pageSize={}",
					productId,productcate,productname,enabled,productpoint,productpoint1,startNum,pageSize,e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/updateProductByProductId
	 * @Description: 编辑商品详情
	 * @param @param productId 商品id
	 * @param @param productcate 商品类目
	 * @param @param productname 商品名称
	 * @param @param productbriefphotos 商品缩略图
	 * @param @param productprice 市场价格
	 * @param @param productpoint 兑换商品所需币
	 * @param @param lowimpicon 最低进步币要求
	 * @param @param productbrief 商品规格
	 * @param @param enabled 商品是否下架 0:已下架  1：未下架
	 * @param @param productdetail 商品详情
	 * @param @param 正确返回 code 0 错误返回相应code 和 描述
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateProductByProductId")
	public BaseResp<Object> updateProductByProductId(String productId,String productcate,String productname,String productbriefphotos,
													 String productprice,String productpoint, String lowimpicon, String productbrief,String enabled,String productdetail) {
		logger.info("updateProductByProductId and productId={},productcate={},productname={},productbriefphotos={}, productprice={}," +
						"productpoint={},lowimpicon={},productbrief={},enabled={},productdetail={}",
				productId,productcate, productname,productbriefphotos, productprice,
				productpoint,lowimpicon,productbrief,enabled,productdetail);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(productId,productcate, productname,productbriefphotos, productprice,
				productpoint,lowimpicon,productbrief,enabled,productdetail)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productService.updateProductByProductId(productId,productcate, productname,productbriefphotos,
					productprice, productpoint,lowimpicon,productbrief,enabled,productdetail);
			return baseResp;
		} catch (Exception e) {
			logger.error("updateProductByProductId and productId={},productcate={},productname={},productbriefphotos={}, productprice={}," +
							"productpoint={},lowimpicon={},productbrief={},enabled={},productdetail={}",
					productId,productcate, productname,productbriefphotos, productprice,
					productpoint,lowimpicon,productbrief,enabled,productdetail,e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/insertProduct
	 * @Description: 添加商品
	 * @param @param productcate 商品类目
	 * @param @param productname 商品名称
	 * @param @param productbriefphotos 商品缩略图
	 * @param @param productprice 市场价格
	 * @param @param productpoint 兑换商品所需币
	 * @param @param lowimpicon 最低进步币要求
	 * @param @param productbrief 商品规格
	 * @param @param enabled 商品是否下架 0:已下架  1：未下架
	 * @param @param productdetail 商品详情
	 * @param @param 正确返回 code 0 错误返回相应code 和 描述
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertProduct")
	public BaseResp<Object> insertProduct(String productcate,String productname,String productbriefphotos,
										  String productprice,String productpoint, String lowimpicon, String productbrief,String enabled,String productdetail) {
		logger.info("insertProduct and productcate={},productname={},productbriefphotos={}, productprice={}," +
						"productpoint={},lowimpicon={},productbrief={},enabled={},productdetail={}",
				productcate, productname,productbriefphotos, productprice,
				productpoint,lowimpicon,productbrief,enabled,productdetail);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(productcate, productname,productbriefphotos, productprice,
				productpoint,lowimpicon,productbrief,enabled,productdetail)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productService.insertProduct(productcate, productname,productbriefphotos,
					productprice, productpoint,lowimpicon,productbrief,enabled,productdetail);
			return baseResp;
		} catch (Exception e) {
			logger.error("insertProduct and productcate={},productname={},productbriefphotos={}, productprice={}," +
							"productpoint={},lowimpicon={},productbrief={},enabled={},productdetail={}",
					productcate, productname,productbriefphotos, productprice,
					productpoint,lowimpicon,productbrief,enabled,productdetail,e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/deleteProductByProductId
	 * @Description: 删除商品
	 * @param  @param productId 商品id
	 * @param @param code 0
	 * @auther IngaWu
	 * @currentdate:2017年3月20日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteProductByProductId")
	public BaseResp<Object> deleteProductByProductId(String productId) {
		logger.info("deleteProductByProductId and productId={}",productId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(productId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productService.deleteProductByProductId(productId);
			return baseResp;
		} catch (Exception e) {
			logger.error("deleteProductByProductId and productId={}",productId,e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/product/selectProductByProductId
	 * @Description: 通过商品id查看商品详情
	 * @param  @param productId 商品id
	 * @param @param code 0
	 * @auther IngaWu
	 * @currentdate:2017年3月22日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectProductByProductId")
	public BaseResp<Object> selectProductByProductId(String productId) {
		logger.info("selectProductByProductId and productId={}",productId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(productId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = productService.selectProductByProductId(productId);
			return baseResp;
		} catch (Exception e) {
			logger.error("selectProductByProductId and productId={}",productId,e);
		}
		return baseResp;
	}

}
