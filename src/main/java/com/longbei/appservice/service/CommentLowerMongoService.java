package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.CommentLower;

public interface CommentLowerMongoService {

	/**
	 * @author yinxc
	 * 添加子评论
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> insertCommentLower(CommentLower commentLower);
	
	/**
	 * @author yinxc
	 * 根据主评论id获取子评论列表
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	List<CommentLower> selectCommentLowerListByCommentid(String commentid);
	
	/**
	 * @author yinxc
	 * 根据id获取子评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	CommentLower selectCommentLowerByid(String id);
	
	/**
	 * @author yinxc
	 * 获取子评论条数
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	long selectCountLowerByCommentid(String commentid);
	
	/**
	 * @author yinxc
	 * 删除子评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> deleteLowerByCommentid(String commentid);
	
	/**
	 * @author yinxc
	 * 删除子评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> deleteCommentLower(String id);
	
}
