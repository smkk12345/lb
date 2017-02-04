package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.CommentLikes;

public interface CommentLikesMongoService {

	/**
	 * @author yinxc
	 * 添加主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	BaseResp<Object> insertCommentLikes(CommentLikes commentLikes);
	
	/**
	 * @author yinxc
	 * 查询主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	CommentLikes selectCommentLikesByCommentid(String commentid, String userid);
	
	/**
	 * @author yinxc
	 * 删除主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	BaseResp<Object> deleteCommentLikes(String id);
	
	/**
	 * @author yinxc
	 * 删除主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	BaseResp<Object> deleteCommentLikesByCommentidAndFriendid(String commentid, String friendid);
	
	/**
	 * @author yinxc
	 * 删除主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	BaseResp<Object> deleteCommentLikesByCommentid(String commentid);
	
}
