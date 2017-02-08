package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserMsg;

public interface UserMsgMapper {
    int deleteByPrimaryKey(Integer id);
    
    /**
	 * @author yinxc
	 * 删除消息(假删)
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等)
	 * return_type
	 * UserMsgService
	 */
    int deleteByUserid(@Param("userid") long userid, @Param("mtype") String mtype);

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

    int updateByPrimaryKeySelective(UserMsg record);

    int updateByPrimaryKey(UserMsg record);
    
    /**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * return_type
	 * UserMsgService
	 */
    int updateIsreadByid(@Param("id") Integer id);
    
    /**
	 * @author yinxc
	 * 修改消息已读状态信息
	 * 2017年2月7日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等)
	 * return_type
	 * UserMsgService
	 */
    int updateIsreadByUserid(@Param("userid") long userid, @Param("mtype") String mtype);
    
}