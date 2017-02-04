package com.longbei.appservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.CommentCountMongoDao;
import com.longbei.appservice.entity.CommentCount;
import com.longbei.appservice.service.CommentCountMongoService;

@Service("commentCountMongoService")
public class CommentCountMongoServiceImpl implements CommentCountMongoService {

	@Autowired
	private CommentCountMongoDao commentCountMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLowerMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertCommentCount(CommentCount commentCount) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(commentCount);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertCommentCount commentCount={},msg={}",commentCount,e);
		}
		return reseResp;
	}
	
	private void insert(CommentCount commentCount){
		commentCountMongoDao.insertCommentCount(commentCount);;
	}

	@Override
	public CommentCount selectCommentCountByCommentid(String commentid) {
		CommentCount commentCount = null;
		try {
			commentCount = commentCountMongoDao.selectCommentCountByCommentid(commentid);
		} catch (Exception e) {
			logger.error("selectCommentCountByCommentid commentid = {}, msg = {}", commentid, e);
		}
		return commentCount;
	}

	@Override
	public BaseResp<Object> deleteCommentCount(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			delete(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentCount id = {}, msg = {}", id, e);
		}
		return reseResp;
	}
	
	private void delete(String id){
		commentCountMongoDao.deleteCommentCount(id);;
	}

	@Override
	public void updateCommentCount(String commentid, String comcount) {
		commentCountMongoDao.updateCommentCount(commentid, comcount);
	}

	@Override
	public void deleteCommentCountByCommentid(String commentid) {
		try {
			commentCountMongoDao.deleteCommentCountByCommentid(commentid);
		} catch (Exception e) {
			logger.error("deleteCommentCountByCommentid commentid = {}, msg = {}", commentid, e);
		}
	}

}
