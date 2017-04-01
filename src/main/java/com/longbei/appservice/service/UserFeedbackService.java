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
	 * * 未处理意见反馈列表
	 * Created by mchaolee on 17/4/1.
	 */
	Page<UserFeedback> selectNoFeedbackListWithPage(UserFeedback userFeedback,int pageno,int pagesize);

	/**
	 * * 已处理意见反馈列表
	 * Created by mchaolee on 17/4/1.
	 */
	Page<UserFeedback> selectHasFeedbackListWithPage(UserFeedback userFeedback,int pageno,int pagesize);


	/**
	 * 处理意见反馈
	 * @param userFeedback
	 * @return
	 */
	boolean updateFeedback(UserFeedback userFeedback);

	/**
	 * 意见反馈详情
	 * @param userid
	 * @return
	 */
	UserFeedback selectUserFeedback(String userid);


}
