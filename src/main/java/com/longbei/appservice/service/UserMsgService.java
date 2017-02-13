package com.longbei.appservice.service;

import java.util.List;

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
	 * 删除用户类型消息(mtype,msgtype)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> deleteByMtypeAndMsgtype(long userid, String mtype, String msgtype);
	
	/**
	 * @author yinxc
	 * 清空点赞等类型消息
	 * msgtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等)
	 * 2017年2月10日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> deleteByLikeUserid(long userid, String msgtype);
	
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
	 * 获取消息列表信息(对话消息-----除赞消息,粉丝消息)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> selectExceptList(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> selectLikeList(long userid, String msgtype, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	BaseResp<Object> selectOtherList(long userid, String mtype, String msgtype, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息id列表信息(对话消息----未读)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * return_type
	 * UserMsgService
	 */
    List<String> selectIdByMsgtypeList(long userid, String msgtype);
	
	/**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同类型消息Count(对话消息-----已读，未读消息)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type
	 * UserMsgService
	 */
	int selectCountByType(long userid, String mtype, String msgtype, String isread);
	
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
	BaseResp<Object> updateIsreadByUserid(long userid, String mtype, String msgtype);
	
}
