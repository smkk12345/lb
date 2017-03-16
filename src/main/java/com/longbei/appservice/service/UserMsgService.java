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
	BaseResp<Object> deleteByid(Integer id, long userid);
	
	/**
	 * @author yinxc
	 * 删除消息
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 2017年2月7日
	 */
	BaseResp<Object> deleteByUserid(long userid, String mtype);
	
	/**
	 * @author yinxc
	 * 删除用户类型消息(mtype,msgtype)
	 * 2017年2月7日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	BaseResp<Object> deleteByMtypeAndMsgtype(long userid, String mtype, String msgtype);
	
	/**
	 * @author yinxc
	 * 清空点赞等类型消息
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 2017年2月10日
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
	 * 2017年3月8日
	 * return_type
	 * UserMsgService
	 */
	BaseResp<Object> selectByUserid(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 获取消息列表信息(对话消息-----除赞消息,粉丝消息)
	 * 2017年2月7日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	BaseResp<Object> selectExceptList(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月7日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	BaseResp<Object> selectLikeList(long userid, String msgtype, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月7日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	BaseResp<Object> selectOtherList(long userid, String mtype, String msgtype, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息id列表信息(对话消息----未读)
	 * 2017年2月7日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
    List<String> selectIdByMsgtypeList(long userid, String msgtype);
	
	/**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同类型消息Count(对话消息-----已读，未读消息)
	 * 2017年2月8日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * isread 可为null  查全部
	 */
	int selectCountByType(long userid, String mtype, String msgtype, String isread);
	
	/**
	 * @author yinxc
	 * 获取"我的"页面对话消息---红点是否显示
	 * 2017年2月8日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * isread 可为null  查全部
	 * return_type  0:不显示   1：显示
	 */
	int selectShowMyByMtype(long userid);
	
	/**
	 * @author yinxc
	 * 修改消息信息
	 * 2017年2月7日
	 */
	BaseResp<Object> updateByid(UserMsg record);
	
	/**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 */
	BaseResp<Object> updateIsreadByid(Integer id);
	
	/**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	BaseResp<Object> updateIsreadByUserid(long userid, String mtype, String msgtype);

	/**
	 * 批量插入用户信息 userMsg中存储指定的信息,只是接收人的用户id不一样
	 * @param userIdList 接收人的用户Id
	 * @param userMsg 消息实体
     * @return
     */
	boolean batchInsertUserMsg(List<Long> userIdList,UserMsg userMsg);

	/**
	 * 查询圈子的验证消息
	 * @param circleId
	 * @param userId
     * @return
     */
	UserMsg findCircleNoticeMsg(Long circleId, Long userId);
}
