package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.CommentCountMongoDao;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.CommentMongoDao;
import com.longbei.appservice.entity.Comment;
import com.longbei.appservice.entity.CommentCount;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.service.CommentMongoService;

@Service("commentMongoService")
public class CommentMongoServiceImpl implements CommentMongoService {

	@Autowired
	private CommentMongoDao commentMongoDao;
	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	@Autowired
	private CommentCountMongoDao commentCountMongoDao;
//	@Autowired
//	private CommentLikesMongoDao commentLikesMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(CommentMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertComment(Comment comment) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(comment);
//			comment = commentMongoDao.selectCommentByItypeid(comment.getItypeid(), comment.getItype());
			//添加评论总数
			insertOther(comment);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertComment comment={},msg={}",comment,e);
		}
		return reseResp;
	}
	
	private void insert(Comment comment){
		commentMongoDao.insertComment(comment);;
	}
	
	private void insertOther(Comment comment){
		CommentCount commentCount = new CommentCount();
		commentCount.setComcount(1);
		commentCount.setCreatetime(DateUtils.formatDateTime1(new Date()));
		commentCount.setLikes(0);
		commentCount.setCommentid(comment.getId());
		commentCountMongoDao.insertCommentCount(commentCount);
	}

	@Override
	public BaseResp<Object> selectCommentListByItypeid(String itypeid, String itype, int startNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(itypeid, itype, startNo, pageSize);
//			String commentids = "";
			Map<String, Object> expandData = new HashMap<String, Object>();
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					comment.setLowerList(lowers);
//					commentids += comment.getId() + ",";
				}
				expandData.put("commentList", list);
				//只要圈子评论才有热门评论     点赞和热门评论暂时不做
//				if("3".equals(itype)){
//					//获取热门评论   点赞数最高的5个
//					if(commentids.length()>0){
//						commentids = commentids.substring(0, commentids.length()-1);
//						List<CommentCount> countList = commentCountMongoDao.selectCommentCountListByCommentids(commentids);
//						expandData.put("hotComment", countList);
//					}
//				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_21, Constant.RTNINFO_SYS_21);
			}
			reseResp.setExpandData(expandData);
		} catch (Exception e) {
			logger.error("selectCommentListByItypeid itypeid = {}, itype = {}, msg = {}", itypeid, itype, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> selectCommentListByItypeidAndFriendid(String friendid, String itypeid, String itype,
			int startNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(itypeid, itype, startNo, pageSize);
//			String commentids = "";
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					comment.setLowerList(lowers);
//					commentids += comment.getId() + ",";
					//判断是否点赞
//					CommentLikes commentLikes = commentLikesMongoDao.selectCommentLikesByCommentid(comment.getId(), friendid);
//					if(null != commentLikes){
//						comment.setIsaddlike("1");
//					}
				}
				//获取热门评论   点赞数最高的5个
				//只要圈子评论才有热门评论     点赞和热门评论暂时不做  02-04
//				if(commentids.length()>0){
//					commentids = commentids.substring(0, commentids.length()-1);
//					List<CommentCount> countList = commentCountMongoDao.selectCommentCountListByCommentids(commentids);
//					expandData.put("hotComment", countList);
//				}
				reseResp.setData(list);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_21, Constant.RTNINFO_SYS_21);
			}
//			reseResp.setExpandData(expandData);
		} catch (Exception e) {
			logger.error("selectCommentListByItypeidAndFriendid itypeid = {}, itype = {}, msg = {}", itypeid, itype, e);
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
			//删除其他的信息，评论总数及子评论信息
			deleteCommentOther(id);
			//后删主评论信息
			commentMongoDao.deleteComment(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteComment id = {}, msg = {}", id, e);
		}
		return reseResp;
	}
	
	private void deleteCommentOther(String commentid){
		//删除子评论信息
		commentLowerMongoDao.deleteLowerByCommentid(commentid);
		//删除主评论条数
		commentCountMongoDao.deleteCommentCountByCommentid(commentid);
	}

	@Override
	public BaseResp<Object> selectCommentCountSum(String itypeid, String itype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentByItypeid(itypeid, itype);
			//获取评论总数
			int zong = 0;
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					CommentCount commentCount = commentCountMongoDao.selectCommentCountByCommentid(comment.getId());
					zong = zong + commentCount.getComcount();
				}
			}
			reseResp.setData(zong);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectCommentCountSum itypeid = {}, msg = {}", itypeid, e);
		}
		return reseResp;
	}

}
