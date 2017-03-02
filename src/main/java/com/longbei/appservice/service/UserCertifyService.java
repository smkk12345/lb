package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

import com.longbei.appservice.entity.UserCertify;

import java.util.Date;

public interface UserCertifyService {

	/**
	 * @Title: applyCertify
	 * @Description: 申请教育经历和工作经历认证
	 * @param @param id
	 * @param @param userid
	 * @param @param ctype认证类型 0学历 1工作经历
	 * @param @param photes图片信息
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	BaseResp<Object> insertCertify(long userid,String ctype,String photes);
	/**
	 * @Title: selectCertifyById
	 * @Description: 查询认证
	 * @param @param id
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	BaseResp<Object> selectCertifyById(int id);
	/**
	 * @Title: applyCertify
	 * @Description: 修改教育经历和工作经历认证
	 * @param @param id
	 * @param @param userid
	 * @param @param ctype认证类型 0学历 1工作经历
	 * @param @param photes图片信息
	 * @param @return
	 * @auther IngaWu
	 * @currentdate:2017年2月27日
	 */
	BaseResp<Object> updateApplyCertify(int id,String ctype,String photes);

}
