package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.CommentLikesMongoDao;
import com.longbei.appservice.entity.CommentLikes;
import com.longbei.appservice.service.CommentLikesMongoService;

@Service("commentLikesMongoService")
public class CommentLikesMongoServiceImpl implements CommentLikesMongoService {

	@Autowired
	private CommentLikesMongoDao commentLikesMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLikesMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertCommentLikes(CommentLikes commentLikes) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(commentLikes);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertCommentLikes commentLikes={},msg={}",commentLikes,e);
		}
		return reseResp;
	}
	
	private void insert(CommentLikes commentLikes){
		commentLikesMongoDao.insertCommentLikes(commentLikes);;
	}

	@Override
	public CommentLikes selectCommentLikesByCommentid(String commentid, String userid) {
		CommentLikes commentLikes = null;
		try {
			commentLikes = commentLikesMongoDao.selectCommentLikesByCommentid(commentid, userid);
		} catch (Exception e) {
			logger.error("selectCommentLikesByCommentid commentid = {}, userid = {}, msg = {}", commentid, userid, e);
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
			logger.error("deleteCommentLikes id = {}, msg = {}", id, e);
		}
		return reseResp;
	}
	
	private void delete(String id){
		commentLikesMongoDao.deleteCommentLikes(id);;
	}
	
	@Override
	public BaseResp<Object> deleteCommentLikesByCommentidAndUserid(String commentid, String userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteByuserid(commentid, userid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLikesByCommentidAndUserid commentid = {}, userid = {}, msg = {}", commentid, userid, e);
		}
		return reseResp;
	}
	
	private void deleteByuserid(String commentid, String userid){
		commentLikesMongoDao.deleteCommentLikesByCommentidAndUserid(commentid, userid);
	}

	@Override
	public BaseResp<Object> deleteCommentLikesByCommentid(String commentid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteBycommentid(commentid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLikesByCommentid commentid = {}, msg = {}", commentid, e);
		}
		return reseResp;
	}
	
	private void deleteBycommentid(String commentid){
		commentLikesMongoDao.deleteCommentLikesByCommentid(commentid);
	}

}
