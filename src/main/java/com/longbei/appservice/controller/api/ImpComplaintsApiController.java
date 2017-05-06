package com.longbei.appservice.controller.api;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ImpComplaints;
import com.longbei.appservice.service.ImpComplaintsService;

@RestController
@RequestMapping(value = "/api/complaints")
public class ImpComplaintsApiController {

	@Autowired
	private ImpComplaintsService impComplaintsService;
	
	private static Logger logger = LoggerFactory.getLogger(ImpComplaintsApiController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/api/complaints/selectListByStatus
    * @Description: 根据status查询投诉列表
    * @param @param status 0：未处理  1: 已处理
    * @param @param startNo  pageSize
    * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @desc  
    * @currentdate:2017年3月30日
	*/
  	@RequestMapping(value = "/selectListByStatus")
    public BaseResp<Page<ImpComplaints>> selectListByStatus(String status, int startNo, int pageSize) {
  		Page.initPageNoAndPageSize(startNo + "", pageSize + "");
		BaseResp<Page<ImpComplaints>> baseResp = new BaseResp<>();
  		try {
  			Page<ImpComplaints> page = impComplaintsService.selectListByStatus(status, startNo, pageSize);
  			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
  			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("selectListByStatus status = {}, startNo = {}, pageSize = {}", 
					status, startNo, pageSize, e);
		}
  		return baseResp;
	}
  	
  	/**
     * @Title: http://ip:port/app_service/api/complaints/searchList
     * @Description: 搜索 
     * @param @param status 0：未处理  1: 已处理
     * @param @param username
     * @param @param businesstype
     * @param @param sdealtime
     * @param @param edealtime
     * @param @param startNo
     * @param @param pageSize
     * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @desc  
     * @currentdate:2017年3月30日
 	*/
   	@RequestMapping(value = "/searchList")
     public BaseResp<Page<ImpComplaints>> searchList(String status, String username, String businesstype, 
     		String sdealtime, String edealtime, String startNo, String pageSize) {
   		Page.initPageNoAndPageSize(startNo, pageSize);
 		BaseResp<Page<ImpComplaints>> baseResp = new BaseResp<>();
   		try {
   			Page<ImpComplaints> page = impComplaintsService.searchList(status, username, businesstype, 
   					sdealtime, edealtime, Integer.parseInt(startNo), 
   					Integer.parseInt(pageSize));
   			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
   			baseResp.setData(page);
 		} catch (Exception e) {
 			logger.error("searchList status = {}, username = {}, businesstype = {}", 
            		status, username, businesstype, e);
 		}
   		return baseResp;
 	}
   	
   	/**
     * @Title: http://ip:port/app_service/api/complaints/updateStatus
     * @Description: 投诉处理 
     * @param @param status 0：未处理  1: 已处理
     * @param @param checkoption
     * @param @param dealuser 处理人
     * @param @param id 业务id
     * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @desc  
     * @currentdate:2017年3月30日
 	*/
   	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateStatus")
    public BaseResp<Object> updateStatus(String id, String status, 
    		String dealuser, String checkoption) {
   		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(id, status)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = impComplaintsService.updateImpComplaintsByStatus(Long.parseLong(id), status, 
  					new Date(), dealuser, checkoption);
		} catch (Exception e) {
			logger.error("updateStatus id = {}, status = {}, dealuser = {}, checkoption = {}", 
					id, status, dealuser, checkoption, e);
		}
  		return baseResp;
 	}
   	
   	/**
     * @Title: http://ip:port/app_service/api/complaints/selectDetail
     * @Description: 获取投诉详情 
     * @param @param id 业务id
     * @param @param 正确返回 code 0， -7为 参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @desc  
     * @currentdate:2017年3月30日
 	*/
   	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectDetail")
    public BaseResp<ImpComplaints> selectDetail(String id) {
   		BaseResp<ImpComplaints> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(id)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = impComplaintsService.selectDetail(Long.parseLong(id));
		} catch (Exception e) {
			logger.error("selectDetail id = {}", id, e);
		}
  		return baseResp;
 	}
	
}
