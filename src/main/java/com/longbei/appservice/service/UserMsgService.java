package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserMsg;

public interface UserMsgService {

	/**
	 * @author yinxc
	 * 删除消息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> deleteByid(Integer id);
	
	/**
	 * @author yinxc
	 * 删除消息
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等)
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> deleteByUserid(long userid, String mtype);
	
	/**
	 * @author yinxc
	 * 添加消息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> insertSelective(UserMsg record);
	
	/**
	 * @author yinxc
	 * 获取消息信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	UserMsg selectByid(Integer id);
	
	/**
	 * @author yinxc
	 * 获取消息信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
//	List<UserMsg> selectByGtypeAndSnsid(String gtype, Long snsid);
	
	/**
	 * @author yinxc
	 * 获取消息列表信息(系统消息)
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> selectByUserid(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 修改消息信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> updateByid(UserMsg record);
	
	/**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> updateIsreadByid(Integer id);
	
	/**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等)
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> updateIsreadByUserid(long userid, String mtype);
	
}
