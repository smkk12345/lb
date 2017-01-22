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
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> selectCommentListByItypeid(String itypeid, String itype, int startNo,
			int pageSize);
	
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
	
}
