package com.longbei.appservice.service;

import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserMsg;

public interface UserMsgService {
	
	/**
	 * @author yinxc
	 * 添加消息封装
	 * @param userid 消息推送者id
	 * @param friendid 消息接受者id
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
	 *									22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *									25:订单发货N天后自动确认收货    26：实名认证审核结果
	 *									27:工作认证审核结果      28：学历认证审核结果
	 *									29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *									32：创建的龙榜/教室/圈子被选中推荐  
	 *									40：订单已取消 41 榜中进步下榜)
	 *				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)
	 *				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
	 *						14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 2017年4月26日
	 */
	BaseResp<Object> insertMsg(String userid, String friendid, String impid, 
			String businesstype, String businessid, String remark, 
			String mtype, String msgtype, String title, int num, 
			String commentid, String commentlowerid);

	/**
	 * 消息批量插入
	 * @param friendid
	 * @param userids
	 * @param businesstype
	 * @param businessid
	 * @param remark
	 * @param title
	 * @return
	 */
	BaseResp<Object> sendMessagesBatch(String friendid , String[] userids,
					  String businesstype, String businessid, String remark, String title);

	/**
	 * 和上面的方法差不多 就是多了一个href参数，图省事不想改了 先这样吧 陆也
	 * @param userid
	 * @param friendid
	 * @param impid
	 * @param businesstype
	 * @param businessid
	 * @param remark
	 * @param mtype
	 * @param msgtype
	 * @param title
	 * @param num
	 * @param commentid
	 * @param commentlowerid
	 * @param href
	 * @return
	 * luye
	 */
	BaseResp<Object> insertMsg(String userid, String friendid, String impid,
							   String businesstype, String businessid, String remark,
							   String mtype, String msgtype, String title, int num,
							   String commentid, String commentlowerid,String href);
	
	/**
	 * 删除评论信息
	 */
	int deleteCommentMsg(String impid, String businesstype, String businessid,  
			String commentid, String commentlowerid);

	/**
	 * 删除关注信息
	 */
	int deleteCommentMsgLike(String userid, String friendid);

	/**
	 * 删除赞信息
	 */
	int deleteLikeCommentMsg(String impid, String businesstype, String businessid,
						 String userid);

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
	 * @param msgtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
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
//	BaseResp<Object> insertSelective(UserMsg record);
	
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
	 */
	BaseResp<Object> selectExceptList(long userid, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月7日
	 * @param msgtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
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
	 * @param msgtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
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
	Map<String,Object> selectCountByType(long userid, String mtype, String msgtype, String isread);
	
	/**
	 * @author yinxc
	 * 获取"我的"页面对话消息---红点是否显示---不用
	 * 2017年2月8日
	 * mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * isread 可为null  查全部
	 * return_type 返回未读消息的总数量 以及未读消息的最大消息时间
	 */
	Map<String,Object> selectShowMy(long userid);
	
	/**
	 * @author yinxc
	 * 获取"我的"页面对话消息---红点是否显示
	 * 2017年2月8日
	 *  mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
	 *										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	 *										27:工作认证审核结果      28：学历认证审核结果   
	 *										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *										32：创建的龙榜/教室/圈子被选中推荐)  
	 * 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * isread 可为null  查全部
	 * return_type 返回未读消息的总数量 以及未读消息的最大消息时间
	 */
	Map<String,Object> selectShowMyByMtype(long userid);

	/**
	 * 获取是否显示红点 0.不显示 1.显示红点
	 * @param userid
	 * @return
     */
	int selectCountShowMyByMtype(long userid);
	
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
	BaseResp<Object> updateIsreadByid(Integer id, long userid);
	
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
	 * 批量插入消息
	 * @param userMsgList
	 * @return
     */
	int batchInsertUserMsg(List<UserMsg> userMsgList);

	/**
	 * 查询圈子的验证消息
	 * @param circleId
	 * @param userId
     * @return
     */
	UserMsg findCircleNoticeMsg(Long circleId, Long userId);

	/**
	 * 查看是否有同一个类型的信息
	 * @param userId 接收消息的用户id
	 * @param msgType 消息类型
	 * @param snsId 业务id
	 * @param gType
     * @return
     */
	int findSameTypeMessage(Long userId, String msgType, Long snsId, String gType);

	/**
	 * 更改消息的已读状态
	 * @param userId 接受消息的用户id
	 * @param msgType 消息类型
	 * @param snsId 业务id
	 * @param gType
	 * @param remark 备注
     * @return
     */
	int updateUserMsgStatus(Long userId, String msgType, Long snsId, String gType,String remark,Boolean updateCreatetime);

	/**
	 * 发送消息
	 * @param isOnly 消息是否要求唯一
	 * @param userId 接受消息 用户id
	 * @param friendId 发送消息
	 * @param mType 消息类型 0.系统消息 1.对话消息 2.@我消息
	 * @param msgType
	 * @param snsId 业务id
	 * @param remark 备注
     * @param gType 0 零散 1 目标中 2 榜中 3圈子中 4 教室中 5.龙群
	 * @param updateCreateTime 是否更新createtime true代表更新
     * @return
     */
	boolean sendMessage(boolean isOnly,Long userId,Long friendId,String mType,String msgType,Long snsId,String remark,String gType,Boolean updateCreateTime);

	/**
	 * 获取添加好友申请的消息数量 和最大的createTime
	 * @param userid
	 * @return
     */
	Map<String,Object> selectAddFriendAskMsg(long userid);
	
	/**
	 * 获取添加好友申请的消息数量 和最大的createTime
	 * @param userid
	 * @return
     */
	Map<String,Object> selectAddFriendAskMsgDate(long userid);
	
	
	int updateByPrimaryKeySelective(UserMsg record);
}
