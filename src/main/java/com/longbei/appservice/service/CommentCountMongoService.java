package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.CommentCount;

public interface CommentCountMongoService {

	/**
	 * @author yinxc
	 * 添加评论总数
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	BaseResp<Object> insertCommentCount(CommentCount commentCount);
	
	/**
	 * @author yinxc
	 * 获取评论总数信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	CommentCount selectCommentCountByItypeid(String itypeid, String itype);
	
	/**
	 * @author yinxc
	 * 删除评论总数
	 * 2017年1月21日
	 * return_type
	 * CommentCountMongoDao
	 */
	BaseResp<Object> deleteCommentCount(String id);
	
	/**
	 * @author yinxc
	 * 修改评论总数及点赞总数
	 * 2017年1月21日
	 * return_type
	 * CommentCountMongoDao
	 */
	void updateCommentCount(String itypeid, String itype, String comcount, String likes);
	
}
