package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.service.CommentLowerMongoService;

@Service("commentLowerMongoService")
public class CommentLowerMongoServiceImpl implements CommentLowerMongoService {

	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLowerMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertCommentLower(CommentLower commentLower) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(commentLower);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertCommentLower commentLower={},msg={}",commentLower,e);
		}
		return reseResp;
	}
	
	private void insert(CommentLower commentLower){
		commentLowerMongoDao.insertCommentLower(commentLower);;
	}

	@Override
	public List<CommentLower> selectCommentLowerListByCommentid(String commentid) {
		List<CommentLower> list = null;
		try {
			list = commentLowerMongoDao.selectCommentLowerListByCommentid(commentid);
		} catch (Exception e) {
			logger.error("selectCommentLowerListByCommentid commentid={},msg={}",commentid,e);
		}
		return list;
	}

	@Override
	public CommentLower selectCommentLowerByid(String id) {
		CommentLower commentLower = null;
		try {
			commentLower = commentLowerMongoDao.selectCommentLowerByid(id);
		} catch (Exception e) {
			logger.error("selectCommentLowerByid id={},msg={}",id,e);
		}
		return commentLower;
	}

	@Override
	public BaseResp<Object> deleteLowerByCommentid(String commentid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteByCommentid(commentid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteLowerByCommentid commentid={},msg={}",commentid,e);
		}
		return reseResp;
	}
	
	private void deleteByCommentid(String commentid){
		commentLowerMongoDao.deleteLowerByCommentid(commentid);
	}

	@Override
	public BaseResp<Object> deleteCommentLower(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteByid(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLower id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private void deleteByid(String id){
		commentLowerMongoDao.deleteCommentLower(id);
	}

	@Override
	public long selectCountLowerByCommentid(String commentid) {
		if(StringUtils.hasBlankParams(commentid)){
			return 0;
		}
		return commentLowerMongoDao.selectCountLowerByCommentid(commentid);
	}

}
