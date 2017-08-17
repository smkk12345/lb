package com.longbei.appservice.dao;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.CommentLower;

/**
 * @author yinxc
 * 子评论
 * 2017年1月21日
 * return_type
 * CommentLowerMongoDao
 */
@Repository
public class CommentLowerMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLowerMongoDao.class);
	
	/**
	 * @author yinxc
	 * 添加子评论
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public void insertCommentLower(CommentLower commentLower){
		try {
			mongoTemplate1.insert(commentLower);
		} catch (Exception e) {
			logger.error("insertCommentLower commentLower = {}, msg = {}", commentLower, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 根据主评论id获取子评论列表
	 * 2017年1月21日
	 */
	public List<CommentLower> selectCommentLowerListByCommentid(String commentid){
		Criteria criteria  = Criteria.where("commentid").is(commentid);
		Query query = Query.query(criteria);
		List<CommentLower> commentLowerList = null;
		try {
			commentLowerList = mongoTemplate1.find(query, CommentLower.class);
		} catch (Exception e) {
			logger.error("getCommentLowerListByCommentid commentid = {}, msg = {}", commentid, e);
		}
		return commentLowerList;
	}
	
	
	/**
	 * @author yinxc
	 * 根据主评论id获取子评论列表---分页
	 * 2017年8月17日
	 */
	public List<CommentLower> selectLowerListByCid(String commentid, Date lastdate, int pageSize){
		Criteria criteria  = Criteria.where("commentid").is(commentid);
		if (lastdate != null) {
			criteria = criteria.and("createtime").lt(lastdate);
		}
		Query query = Query.query(criteria);
		query.with(new Sort(Sort.Direction.DESC, "createtime"));
		if(pageSize != 0){
			query.limit(pageSize);
		}
		List<CommentLower> commentLowerList = null;
		try {
			commentLowerList = mongoTemplate1.find(query, CommentLower.class);
		} catch (Exception e) {
			logger.error("selectLowerListByCid commentid = {}, lastdate = {}, pageSize = {}", 
					commentid, DateUtils.formatDateTime1(lastdate), pageSize, e);
		}
		return commentLowerList;
	}
	
	/**
	 * @author yinxc
	 * 根据id获取子评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public CommentLower selectCommentLowerByid(String id){
		CommentLower commentLower = null;
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			commentLower = mongoTemplate1.findOne(query,CommentLower.class);
		} catch (Exception e) {
			logger.error("selectCommentLowerByid id = {}, msg = {}", id, e);
		}
		return commentLower;
	}
	
	/**
	 * @author yinxc
	 * 删除子评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public void deleteLowerByCommentid(String commentid){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			mongoTemplate1.remove(query, CommentLower.class);
		} catch (Exception e) {
			logger.error("deleteLowerByCommentid commentid = {}, msg = {}", commentid, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 删除子评论信息
	 * 2017年1月21日
	 * return_type
	 * CommentMongoDao
	 */
	public void deleteCommentLower(String id){
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			mongoTemplate1.remove(query, CommentLower.class);
		} catch (Exception e) {
			logger.error("deleteCommentLower id = {}, msg = {}", id, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 获取子评论条数
	 * 2017年1月22日
	 * return_type
	 * CommentLowerMongoDao
	 */
	public long selectCountLowerByCommentid(String commentid){
		Query query = Query.query(Criteria.where("commentid").is(commentid));
		long temp = mongoTemplate1.count(query, CommentLower.class);
		return temp;
	}
	
}
