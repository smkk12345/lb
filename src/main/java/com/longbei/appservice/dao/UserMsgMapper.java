package com.longbei.appservice.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserMsg;

public interface UserMsgMapper {
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("userid") long userid);
    
    /**
	 * @author yinxc
	 * 删除消息
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * return_type
	 * UserMsgService
	 */
    int deleteByUserid(@Param("userid") long userid, @Param("mtype") String mtype);
    
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
    int deleteByMtypeAndMsgtype(@Param("userid") long userid, @Param("mtype") String mtype, @Param("msgtype") String msgtype);
    
    /**
	 * @author yinxc
	 * 清空点赞消息
	 * 2017年2月10日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * return_type
	 * UserMsgMapper
	 */
    int deleteByLikeUserid(@Param("userid") long userid, @Param("msgtype") String msgtype);

    int insert(UserMsg record);

    int insertSelective(UserMsg record);

    UserMsg selectByPrimaryKey(Integer id);
    
    /**
	 * @author yinxc
	 * 获取消息信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
//	List<UserMsg> selectByGtypeAndSnsid(@Param("gtype") String gtype, @Param("snsid") Long snsid);
    
    /**
	 * @author yinxc
	 * 获取消息列表信息(系统消息)
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
    List<UserMsg> selectByUserid(@Param("userid") long userid, @Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取消息列表信息(对话消息除赞消息,粉丝消息)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * return_type
	 * UserMsgService
	 */
    List<UserMsg> selectExceptList(@Param("userid") long userid, @Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同mtype类型消息列表信息
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * return_type
	 * UserMsgService
	 */
    List<UserMsg> selectOtherList(@Param("userid") long userid, @Param("mtype") String mtype, @Param("msgtype") String msgtype, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
    List<UserMsg> selectLikeList(@Param("userid") long userid, @Param("msgtype") String msgtype, @Param("startNum") int startNum, 
    		@Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 根据msgtype获取消息id列表信息(对话消息----未读)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * UserMsgService
	 */
    List<String> selectIdByMsgtypeList(@Param("userid") long userid, @Param("msgtype") String msgtype);

    /**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同类型消息Count(对话消息-----已读，未读消息  0 未读  1 已读)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type
	 * UserMsgService
	 */
    int selectCountByType(@Param("userid") long userid, @Param("mtype") String mtype, @Param("msgtype") String msgtype, 
    		@Param("isread") String isread);
    
    /**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同类型消息List(对话消息-----已读，未读消息  0 未读  1 已读)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 */
    List<UserMsg> selectListByMtypeAndMsgtype(@Param("userid") long userid, @Param("mtype") String mtype, @Param("msgtype") String msgtype, 
    		@Param("isread") String isread);
    
    /**
	 * @author yinxc
	 * 获取"我的"页面对话消息---红点是否显示
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type  0:不显示   1：显示
	 */
//    List<UserMsg> selectShowMyByMtype(@Param("userid") long userid, @Param("mtype") String mtype, @Param("userid") String fanstype, 
//    		@Param("liketype") String liketype, @Param("flowertype") String flowertype, 
//    		@Param("diamondtype") String diamondtype, @Param("commenttype") String commenttype, 
//    		@Param("isread") String isread);
    
    int updateByPrimaryKeySelective(UserMsg record);

    int updateByPrimaryKey(UserMsg record);
    
    /**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
    int updateIsreadByid(@Param("id") Integer id, @Param("userid") long userid);
    
    /**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * return_type
	 * UserMsgService
	 */
    int updateIsreadByUserid(@Param("userid") long userid, @Param("mtype") String mtype, @Param("msgtype") String msgtype);

	/**
	 * 批量插入用户 消息
	 * @param map
	 * @return
     */
	int batchInsertUserMsg(Map<String, Object> map);

	/**
	 * 查询成员加圈子的圈主验证消息
	 * @param circleId
	 * @param userId
     * @return
     */
	UserMsg findCircleNoticeMsg(@Param("circleId") Long circleId,@Param("userId") Long userId);

	/**
	 * 查看是否有同一个类型的信息
	 * @param userId 接收消息的用户id
	 * @param msgType 消息类型
	 * @param snsId 业务id
	 * @param gType
	 * @return
	 */
	int findSameTypeMessage(@Param("userId")Long userId,@Param("msgType") String msgType,@Param("snsId") Long snsId,@Param("gType") String gType);

	/**
	 * 更改消息的已读状态
	 * @param userId 接受消息的用户id
	 * @param msgType 消息类型
	 * @param snsId 业务id
	 * @param gType
	 * @return
	 */
	int updateUserMsgStatus(@Param("userId") Long userId,@Param("msgType") String msgType,@Param("snsId") Long snsId,@Param("gType") String gType);

	/**
	 * 批量插入消息
	 * @param userMsgList
	 * @return
     */
	int batchInsertUserMsgList(List<UserMsg> userMsgList);
}