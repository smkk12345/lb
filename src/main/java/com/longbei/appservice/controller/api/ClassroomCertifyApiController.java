package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ClassroomCertify;
import com.longbei.appservice.service.ClassroomCertifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/roomcertify")
public class ClassroomCertifyApiController {
	@Autowired
	private ClassroomCertifyService classroomCertifyService;

	private static Logger logger = LoggerFactory.getLogger(ClassroomCertifyApiController.class);

	@RequestMapping(value = "selectClassroomCertifyList")
	public BaseResp<Page<ClassroomCertify>> selectClassroomCertifyList(@RequestBody ClassroomCertify classroomCertify, Integer startNum, Integer pageSize){
		logger.info("selectClassroomCertifyList for adminservice classroomCertify:{},startNum={},pageSize={}", JSON.toJSONString(classroomCertify),startNum,pageSize);
		BaseResp<Page<ClassroomCertify>> baseResp = new BaseResp<>();
		if (null == startNum) {
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (null == pageSize) {
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			Page<ClassroomCertify> page  = classroomCertifyService.selectClassroomCertifyList(classroomCertify,startNum,pageSize);
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("selectClassroomCertifyList for adminservice error",e);
		}
		return baseResp;
	}

	/**
	 * 获取老师认证信息列表数量
	 * @param classroomCertify
	 * @return
	 */
	@RequestMapping(value = {"selectClassroomCertifyNum"})
	public BaseResp<Object> selectClassroomCertifyNum(@RequestBody ClassroomCertify classroomCertify){
		logger.info("selectClassroomCertifyNum for adminservice and classroomCertify ={}", JSON.toJSONString(classroomCertify));
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = classroomCertifyService.selectClassroomCertifyNum(classroomCertify);
		} catch (Exception e) {
			logger.error("selectClassroomCertifyNum for adminservice and classroomCertify ={}", JSON.toJSONString(classroomCertify), e);
		}
		return  baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectClassroomCertifyByUserid")
	public BaseResp<ClassroomCertify> selectClassroomCertifyByUserid(String userid) {
		logger.info("selectClassroomCertifyByUserid and userid={}",userid);
		BaseResp<ClassroomCertify> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userid)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomCertifyService.selectClassroomCertifyByUserid(Long.parseLong(userid));
			return baseResp;
		} catch (Exception e) {
			logger.error("selectClassroomCertifyByUserid and userid={}",userid,e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertClassroomCertify")
	public BaseResp<Object> insertClassroomCertify(@RequestBody ClassroomCertify classroomCertify) {
		logger.info("insertClassroomCertify for adminservice classroomCertify:{}", JSON.toJSONString(classroomCertify));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(classroomCertify.getContent())){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomCertifyService.insertClassroomCertify(classroomCertify);
			return baseResp;
		} catch (Exception e) {
			logger.error("insertClassroomCertify for pc classroomCertify:{}", JSON.toJSONString(classroomCertify),e);
		}
		return baseResp;
	}

	@RequestMapping(value = "/updateClassroomCertifyByUserid")
	public BaseResp<Object> updateClassroomCertifyByUserid(@RequestBody ClassroomCertify classroomCertify) {
		logger.info("updateClassroomCertifyByUserid for adminservice classroomCertify:{}", JSON.toJSONString(classroomCertify));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(String.valueOf(classroomCertify.getUserid()))){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomCertifyService.updateClassroomCertifyByUserid(classroomCertify);
		} catch (Exception e) {
			logger.error("updateClassroomCertifyByUserid for adminservice classroomCertify:{}", JSON.toJSONString(classroomCertify),e);

		}
		return baseResp;
	}
}
