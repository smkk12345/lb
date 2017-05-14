package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.UserFeedback;

public interface UserFeedbackService {

	/**
	 * @author yinxc
	 * 添加意见反馈
	 * 2017年1月19日
	 * return_type
	 */
	BaseResp<Object> insertSelective(UserFeedback record);

	/**
	 * * 意见反馈列表
	 * Created by mchaolee on 17/4/1.
	 */
	Page<UserFeedback> selectFeedbackListWithPage(UserFeedback userFeedback,int pageno,int pagesize);

	/**
	 * 处理意见反馈
	 * @param userFeedback
	 * @return
	 */
	boolean updateFeedback(UserFeedback userFeedback);

	/**
	 * 意见反馈详情
	 * @param id
	 * @return
	 */
	BaseResp<UserFeedback> selectUserFeedback(String id, long userid);
	
	/**
	 * 意见反馈详情
	 * @param id
	 * @return
	 */
	BaseResp<UserFeedback> selectUserFeedbackById(String id);




}
