package com.longbei.appservice.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	 * @author yinxc
	 * 收货地址管理
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list")
	@ResponseBody
	public BaseResp<Object> list(HttpServletRequest request, HttpServletResponse response) {
		String userid = request.getParameter("userid");
		String startNumStr = request.getParameter("startNum");
		String endNumStr = request.getParameter("endNum");
		int pageNo = 0;
		if (!StringUtils.isBlank(startNumStr)) {
			pageNo = Integer.parseInt(startNumStr);
		}
		int pageSize = 0;
		if (!StringUtils.isBlank(endNumStr)) {
			pageSize = Integer.parseInt(endNumStr);
		}
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userid)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userAddressService.selectByUserId(userid, pageNo, pageSize);
		} catch (Exception e) {
			logger.error("list error and msg = {}", e);
		}
		return baseResp;
	}
	
	/**
	 * @author yinxc
	 * 添加收货地址
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add")
	@ResponseBody
	public BaseResp<Object> add(HttpServletRequest request, HttpServletResponse response) {
		String userid = request.getParameter("userid");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String region = request.getParameter("region");
		String addr = request.getParameter("address");
		
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userid)||StringUtils.isBlank(addr)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		UserAddress record = new UserAddress(Long.parseLong(userid), region, addr, "1", mobile, receiver, "0", new Date(), new Date());
		try {
			baseResp = userAddressService.insertSelective(record);
		} catch (Exception e) {
			logger.error("add userid = {}, msg = {}", userid, e);
		}
		return baseResp;
	}
	
	/**
	 * @author yinxc
	 * 修改收货地址
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "update")
	@ResponseBody
	public BaseResp<Object> update(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String userid = request.getParameter("userid");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String region = request.getParameter("region");
		String addr = request.getParameter("address");
		
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(id,userid,addr)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		UserAddress record = new UserAddress(Long.parseLong(userid), region, addr, "1", mobile, receiver, "0", new Date(), new Date());
		record.setId(Integer.parseInt(id));
		try {
			baseResp = userAddressService.updateByPrimaryKeySelective(record);
		} catch (Exception e) {
			logger.error("update id = {}, userid = {}, msg = {}", id, userid, e);
		}
		return baseResp;
	}
	
	/**
	 * @author yinxc
	 * 修改收货地址默认地址
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "updateIsdefault")
	@ResponseBody
	public BaseResp<Object> updateIsdefault(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String userid = request.getParameter("userid");
		String isdefault = request.getParameter("isdefault");
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(id)||StringUtils.isBlank(isdefault)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		
		try {
			baseResp = userAddressService.updateIsdefaultByAddressId(id, userid, isdefault);
		} catch (Exception e) {
			logger.error("updateIsdefault id = {}, isdefault = {}, msg = {}", id, isdefault, e);
		}
		return baseResp;
	}
	
	/**
	 * @author yinxc
	 * 删除收货地址(假删)
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "remove")
	@ResponseBody
	public BaseResp<Object> remove(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(id)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userAddressService.removeIsdel(id);
		} catch (Exception e) {
			logger.error("remove id = {}, msg = {}", id, e);
		}
		return baseResp;
	}
	
}
