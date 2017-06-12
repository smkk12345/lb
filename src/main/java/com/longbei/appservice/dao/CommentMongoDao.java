package com.longbei.appservice.dao;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.longbei.appservice.entity.Comment;

/**
 * @author yinxc
 * 主评论Dao
 * 2017年1月21日
 * return_type
 * CommentMongoDao
 */
@Repository
public class CommentMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(CommentMongoDao.class);
	
	/**
	 * @author yinxc
	 * 添加主评论
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public void insertComment(Comment comment){
		try {
			mongoTemplate1.insert(comment);
		} catch (Exception e) {
			logger.error("insertComment comment = {}", comment, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 根据评论类型及类型id获取当前类型的评论列表(分页)
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public List<Comment> selectCommentListByItypeid(String impid,String businessid, String businesstype, Date lastdate,
			int pageSize){
		Criteria criteria  = Criteria.where("businesstype").is(businesstype);
		if (StringUtils.isNotEmpty(businessid)) {
			criteria = criteria.and("businessid").is(businessid);
		}
		if(StringUtils.isNotEmpty(impid)){
			criteria = criteria.and("impid").is(impid);
		}
		if (lastdate != null) {
			criteria = criteria.and("createtime").lt(lastdate);
		}
		Query query = Query.query(criteria);
		query.with(new Sort(Sort.Direction.DESC, "createtime"));
//		query.with(new Sort(Direction.DESC, "createdate"));
		if(pageSize != 0){
			query.limit(pageSize);
		}
		//long totalcount = mongoTemplate1.count(query, Comment.class);
		List<Comment> comments = null;
		try {
			comments = mongoTemplate1.find(query, Comment.class);
		} catch (Exception e) {
			logger.error("getCommentListByItypeid businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return comments;
	}
	
	/**
	 * @author yinxc
	 * 根据id获取主评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public List<Comment> selectCommentByItypeid(String businessid, String businesstype, String impid){
		List<Comment> list = null;
		try {
			Criteria criteria  = Criteria.where("businesstype").is(businesstype);
			if (StringUtils.isNotBlank(impid)) {
				criteria = criteria.and("impid").is(impid);
			}
			if (StringUtils.isNotBlank(businessid)) {
				criteria = criteria.and("businessid").is(businessid);
			}
			Query query = Query.query(criteria);
			list = mongoTemplate1.find(query,Comment.class);
		} catch (Exception e) {
			logger.error("selectCommentByItypeid businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return list;
	}
	
	/**
	 * @author yinxc
	 * 根据id获取主评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public Comment selectCommentByid(String id){
		Comment comment = null;
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			comment = mongoTemplate1.findOne(query,Comment.class);
		} catch (Exception e) {
			logger.error("selectCommentByid id = {}", id, e);
		}
		return comment;
	}
	
	/**
	 * @author yinxc
	 * 删除主评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public void deleteComment(String id){
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			mongoTemplate1.remove(query, Comment.class);
		} catch (Exception e) {
			logger.error("deleteComment id = {}", id, e);
		}
	}
	
}
