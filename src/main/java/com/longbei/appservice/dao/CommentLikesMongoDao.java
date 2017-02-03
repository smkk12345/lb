package com.longbei.appservice.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.longbei.appservice.entity.CommentLikes;

/**
 * @author yinxc
 * 主评论点赞详情表
 * 2017年2月3日
 * return_type
 * CommentLikesMongoDao
 */
@Repository
public class CommentLikesMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLikesMongoDao.class);
	
	/**
	 * @author yinxc
	 * 添加主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	public void insertCommentLikes(CommentLikes commentLikes){
		try {
			mongoTemplate1.insert(commentLikes);
		} catch (Exception e) {
			logger.error("insertCommentLikes commentLikes = {}, msg = {}", commentLikes, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 查询主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	public CommentLikes selectCommentLikesByCommentid(String commentid, String userid){
		CommentLikes commentLikes = null;
		try {
			Criteria criteria  = Criteria.where("commentid").is(commentid).and("userid").is(userid);
			Query query = Query.query(criteria);
			commentLikes = mongoTemplate1.findOne(query,CommentLikes.class);
		} catch (Exception e) {
			logger.error("selectCommentLikesByCommentid commentid = {}, msg = {}", commentid, e);
		}
		return commentLikes;
	}
	
	/**
	 * @author yinxc
	 * 删除主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	public void deleteCommentLikes(String id){
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			mongoTemplate1.remove(query, CommentLikes.class);
		} catch (Exception e) {
			logger.error("deleteCommentLikes id = {}, msg = {}", id, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 删除主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	public void deleteCommentLikesByCommentidAndUserid(String commentid, String userid){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid).and("userid").is(userid));
			mongoTemplate1.remove(query, CommentLikes.class);
		} catch (Exception e) {
			logger.error("deleteCommentLikesByCommentidAndUserid commentid = {}, userid = {}, msg = {}", commentid, userid, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 删除主评论点赞详情
	 * 2017年2月3日
	 * return_type
	 * CommentLikesMongoDao
	 */
	public void deleteCommentLikesByCommentid(String commentid){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			mongoTemplate1.remove(query, CommentLikes.class);
		} catch (Exception e) {
			logger.error("deleteCommentLikesByCommentid commentid = {}, msg = {}", commentid, e);
		}
	}
	
}
