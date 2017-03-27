package com.longbei.appservice.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.entity.UserAddress;
import com.longbei.appservice.service.UserAddressService;

/**
 * @author yinxc
 * 收货地址
 * 2017年1月19日
 * return_type
 */
@Controller
@RequestMapping(value = "/userAddress")
public class UserAddressController extends BaseController {

	@Autowired
	private UserAddressService userAddressService;
	
	private static Logger logger = LoggerFactory.getLogger(UserAddressController.class);
	
	/**
    * @Title: http://ip:port/app_service/userAddress/list
    * @Description: 收货地址管理
    * @param @param userid
	* @param @param startNum
    * @param @param endNum
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月16日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list")
	@ResponseBody
	public BaseResp<Object> list(String userid, Integer startNum, Integer endNum) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userid)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
  			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
  			if(null != startNum){
  				sNo = startNum.intValue();
  			}
  			if(null != endNum){
  				sSize = endNum.intValue();
  			}
			baseResp = userAddressService.selectByUserId(Long.parseLong(userid), sNo, sSize);
		} catch (Exception e) {
			logger.error("list userid = {}", userid, e);
		}
		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userAddress/add
    * @Description: 添加收货地址
    * @param @param userid
	* @param @param receiver
	* @param @param mobile
	* @param @param region
	* @param @param address
    * @param @param isdefault  是否 默认   1 默认收货地址  0 非默认
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月16日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add")
	@ResponseBody
	public BaseResp<Object> add(@RequestParam("userid") String userid, String receiver, String mobile, 
			String region, String address, String isdefault) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(userid, receiver, mobile, region, address, isdefault)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		UserAddress record = new UserAddress(Long.parseLong(userid), region, address, isdefault, mobile, receiver, "0", new Date(), new Date());
		try {
			baseResp = userAddressService.insertSelective(record);
		} catch (Exception e) {
			logger.error("add userid = {}, msg = {}", userid, e);
		}
		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userAddress/update
    * @Description: 修改收货地址
    * @param @param userid
    * @param @param id
	* @param @param receiver
	* @param @param mobile
	* @param @param region
	* @param @param address
    * @param @param isdefault  是否 默认   1 默认收货地址  0 非默认
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月16日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "update")
	@ResponseBody
	public BaseResp<Object> update(@RequestParam("userid") String userid, String id, String receiver, 
			String mobile, String region, String address, String isdefault) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(userid, receiver, mobile, region, address, isdefault, id)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		UserAddress record = new UserAddress(Long.parseLong(userid), region, address, isdefault, mobile, receiver, "0", new Date(), new Date());
		record.setId(Integer.parseInt(id));
		try {
			baseResp = userAddressService.updateByPrimaryKeySelective(record);
		} catch (Exception e) {
			logger.error("update id = {}, userid = {}, msg = {}", id, userid, e);
		}
		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userAddress/updateIsdefault
    * @Description: 修改收货地址默认地址
    * @param @param userid
    * @param @param id
    * @param @param isdefault  是否 默认   1 默认收货地址  0 非默认
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月16日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "updateIsdefault")
	@ResponseBody
	public BaseResp<Object> updateIsdefault(@RequestParam("userid") String userid, String id, String isdefault) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(userid, id, isdefault)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		
		try {
			baseResp = userAddressService.updateIsdefaultByAddressId(id, Long.parseLong(userid), isdefault);
		} catch (Exception e) {
			logger.error("updateIsdefault id = {}, isdefault = {}, msg = {}", id, isdefault, e);
		}
		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userAddress/remove
    * @Description: 删除收货地址(假删)
    * @param @param id
    * @param @param userid
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月16日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "remove")
	@ResponseBody
	public BaseResp<Object> remove(@RequestParam("userid") String userid, @RequestParam("id") String id) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(id)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userAddressService.removeIsdel(Long.parseLong(userid), id);
		} catch (Exception e) {
			logger.error("remove id = {}, msg = {}", id, e);
		}
		return baseResp;
	}
	
}
