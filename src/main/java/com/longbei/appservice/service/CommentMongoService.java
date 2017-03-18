package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.Comment;

public interface CommentMongoService {

	/**
	 * @author yinxc
	 * 添加主评论
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> insertComment(Comment comment);
	
	/**
	 * @author yinxc
	 * 根据评论类型及类型id获取当前类型的评论列表(分页)
	 * 2017年1月21日
	 * @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param itypeid  各类型对应的id
	 * CommentMongoDao
	 */
	BaseResp<Object> selectCommentListByItypeid(String itypeid, String itype, int startNo,
			int pageSize);
	
	/**
	 * @author yinxc
	 * 根据评论类型及类型id获取当前类型的评论列表(分页)
	 * 2017年1月21日
	 * @param friendid  当前访问者id
	 * @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param itypeid  各类型对应的id
	 * CommentMongoDao
	 */
	BaseResp<Object> selectCommentListByItypeidAndFriendid(String friendid, String itypeid, String itype, int startNo,
			int pageSize);
	
	/**
	 * @author yinxc
	 * 根据评论类型及类型id获取当前类型的热门评论列表
	 * 2017年1月21日
	 * @param friendid  当前访问者id
	 * @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param itypeid  各类型对应的id
	 * CommentMongoDao
	 */
	BaseResp<Object> selectCommentHotListByItypeidAndFid(String friendid, String itypeid, String itype);
	
	/**
	 * @author yinxc
	 * 根据id获取主评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	Comment selectCommentByid(String id);
	
	/**
	 * @author yinxc
	 * 删除主评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> deleteComment(String id);
	
	/**
	 * @author yinxc
	 * 查看评论总数
	 * 2017年1月21日
	 * @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param itypeid  各类型对应的id
	 * CommentMongoDao
	 */
	BaseResp<Integer> selectCommentCountSum(String itypeid, String itype);
	
}
