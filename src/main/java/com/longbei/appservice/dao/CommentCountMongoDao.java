package com.longbei.appservice.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.longbei.appservice.entity.CommentCount;

@Repository
public class CommentCountMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(CommentMongoDao.class);
	
	/**
	 * @author yinxc
	 * 添加评论总数
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public void insertCommentCount(CommentCount commentCount){
		try {
			mongoTemplate1.insert(commentCount);
		} catch (Exception e) {
			logger.error("insertCommentCount commentCount = {}, msg = {}", commentCount, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 获取评论总数信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public CommentCount selectCommentCountByCommentid(String commentid){
		CommentCount commentCount = null;
		try {
			Criteria criteria  = Criteria.where("commentid").is(commentid);
			Query query = Query.query(criteria);
			commentCount = mongoTemplate1.findOne(query,CommentCount.class);
		} catch (Exception e) {
			logger.error("selectCommentCountByCommentid commentid = {}, msg = {}", commentid, e);
		}
		return commentCount;
	}
	
	/**
	 * @author yinxc
	 * 删除评论总数
	 * 2017年1月21日
	 * return_type
	 * CommentCountMongoDao
	 */
	public void deleteCommentCount(String id){
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			mongoTemplate1.remove(query, CommentCount.class);
		} catch (Exception e) {
			logger.error("deleteComment id = {}, msg = {}", id, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 根据主评论id批量删除评论总数
	 * 2017年1月21日
	 * return_type
	 * CommentCountMongoDao
	 */
	public void deleteCommentCountByCommentid(String commentid){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			mongoTemplate1.remove(query, CommentCount.class);
		} catch (Exception e) {
			logger.error("deleteCommentCountByCommentid commentid = {}, msg = {}", commentid, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 修改评论总数及点赞总数
	 * 2017年1月21日
	 * return_type
	 * CommentCountMongoDao
	 */
	public void updateCommentCount(String commentid, String comcount, String likes){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			
			Update update = new Update();
			update.set("comcount", Integer.parseInt(comcount));
			if(!StringUtils.isBlank(likes)){
				update.set("likes", Integer.parseInt(likes));
			}
			mongoTemplate1.updateMulti(query, update, CommentCount.class);
		} catch (Exception e) {
			logger.error("updateCommentCount comcount = {}, msg = {}", comcount, e);
		}
	}
	
}
