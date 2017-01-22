package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.CommentMongoDao;
import com.longbei.appservice.entity.Comment;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.service.CommentMongoService;

@Service("commentMongoService")
public class CommentMongoServiceImpl implements CommentMongoService {

	@Autowired
	private CommentMongoDao commentMongoDao;
	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(CommentMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertComment(Comment comment) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(comment);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertComment comment={},msg={}",comment,e);
		}
		return reseResp;
	}
	
	private void insert(Comment comment){
		commentMongoDao.insertComment(comment);;
	}

	@Override
	public BaseResp<Object> selectCommentListByItypeid(String itypeid, String itype, int startNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(itypeid, itype, startNo, pageSize);
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					comment.setLowerList(lowers);
				}
				reseResp.setData(list);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_21, Constant.RTNINFO_SYS_21);
			}
		} catch (Exception e) {
			logger.error("selectCommentListByItypeid itypeid = {}, itype = {}, msg = {}", itypeid, itype, e);
		}
		return reseResp;
	}

	@Override
	public Comment selectCommentByid(String id) {
		Comment comment = null;
		try {
			comment = commentMongoDao.selectCommentByid(id);
		} catch (Exception e) {
			logger.error("selectCommentByid id = {}, msg = {}", id, e);
		}
		return comment;
	}

	@Override
	public BaseResp<Object> deleteComment(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			commentMongoDao.deleteComment(id);
			//删除其他的信息，评论总数及子评论信息
			deleteother(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteComment id = {}, msg = {}", id, e);
		}
		return reseResp;
	}
	
	private void deleteother(String id){
		
	}

}
