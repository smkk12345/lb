package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.CommentCountMongoDao;
import com.longbei.appservice.dao.CommentLikesMongoDao;
import com.longbei.appservice.entity.CommentLikes;
import com.longbei.appservice.service.CommentLikesMongoService;

@Service("commentLikesMongoService")
public class CommentLikesMongoServiceImpl implements CommentLikesMongoService {

	@Autowired
	private CommentLikesMongoDao commentLikesMongoDao;
	@Autowired
	private CommentCountMongoDao commentCountMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLikesMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertCommentLikes(CommentLikes commentLikes) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//判断是否已点赞
			CommentLikes likes = commentLikesMongoDao.selectCommentLikesByCommentid(commentLikes.getCommentid(), commentLikes.getFriendid());
			if(null != likes){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_22, Constant.RTNINFO_SYS_22);
			}else{
				insert(commentLikes);
				//修改CommentCount点赞数量+1
				otherAddLikes(commentLikes.getCommentid());
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertCommentLikes commentLikes = {}", commentLikes, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 修改CommentCount点赞数量   ------点赞
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoServiceImpl
	 */
	private void otherAddLikes(String commentid){
		commentCountMongoDao.updateCommentAddLikes(commentid);
	}
	
	private void insert(CommentLikes commentLikes){
		commentLikesMongoDao.insertCommentLikes(commentLikes);;
	}

	@Override
	public CommentLikes selectCommentLikesByCommentid(String commentid, String friendid) {
		CommentLikes commentLikes = null;
		try {
			commentLikes = commentLikesMongoDao.selectCommentLikesByCommentid(commentid, friendid);
		} catch (Exception e) {
			logger.error("selectCommentLikesByCommentid commentid = {}, friendid = {}", commentid, friendid, e);
		}
		return commentLikes;
	}

	@Override
	public BaseResp<Object> deleteCommentLikes(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			delete(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLikes id = {}", id, e);
		}
		return reseResp;
	}
	
	private void delete(String id){
		commentLikesMongoDao.deleteCommentLikes(id);;
	}
	
	@Override
	public BaseResp<Object> deleteCommentLikesByCommentidAndFriendid(String commentid, String friendid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteByfriendid(commentid, friendid);
			//修改CommentCount点赞数量-1   取消点赞
			otherDecreaseLikes(commentid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLikesByCommentidAndFriendid commentid = {}, friendid = {}", commentid, friendid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 修改CommentCount点赞数量   ------取消点赞
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoServiceImpl
	 */
	private void otherDecreaseLikes(String commentid){
		commentCountMongoDao.updateCommentDecreaseLikes(commentid);
	}
	
	private void deleteByfriendid(String commentid, String friendid){
		commentLikesMongoDao.deleteCommentLikesByCommentidAndFriendid(commentid, friendid);
	}

	@Override
	public BaseResp<Object> deleteCommentLikesByCommentid(String commentid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteBycommentid(commentid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLikesByCommentid commentid = {}", commentid, e);
		}
		return reseResp;
	}
	
	private void deleteBycommentid(String commentid){
		commentLikesMongoDao.deleteCommentLikesByCommentid(commentid);
	}

}
