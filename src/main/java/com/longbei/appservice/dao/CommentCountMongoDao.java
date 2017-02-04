package com.longbei.appservice.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	
	private static Logger logger = LoggerFactory.getLogger(CommentCountMongoDao.class);
	
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
	 * 根据主评论id集合获取热门评论5个
	 * 2017年2月4日
	 * return_type
	 * CommentCountMongoDao
	 */
	public List<CommentCount> selectCommentCountListByCommentids(String commentids){
		List<CommentCount> list = null;
		try {
			Criteria criteria  = Criteria.where("commentid").in(commentids);
			Query query = Query.query(criteria);
			query.with(new Sort(Direction.DESC, "likes"));
			query.limit(5);
			list = mongoTemplate1.find(query,CommentCount.class);
		} catch (Exception e) {
			logger.error("selectCommentCountListByCommentids commentids = {}, msg = {}", commentids, e);
		}
		return list;
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
			logger.error("deleteCommentCount id = {}, msg = {}", id, e);
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
	 * 修改评论总数
	 * 2017年1月21日
	 * return_type
	 * CommentCountMongoDao
	 */
	public void updateCommentCount(String commentid, String comcount){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			
			Update update = new Update();
			update.set("comcount", Integer.parseInt(comcount));
//			if(!StringUtils.isBlank(likes)){
//				update.set("likes", Integer.parseInt(likes));
//			}
			mongoTemplate1.updateMulti(query, update, CommentCount.class);
		} catch (Exception e) {
			logger.error("updateCommentCount comcount = {}, msg = {}", comcount, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 修改评论点赞  -----点赞总数+1  递增
	 * 2017年2月3日
	 * return_type
	 * CommentCountMongoDao
	 */
	public void updateCommentAddLikes(String commentid){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			Update update = new Update();
			//递增+1
			update.inc("likes", 1);
			mongoTemplate1.updateMulti(query, update, CommentCount.class);
		} catch (Exception e) {
			logger.error("updateCommentAddLikes msg = {}", e);
		}
	}
	
	/**
	 * @author yinxc
	 * 修改评论点赞  -----点赞总数-1  递减
	 * 2017年2月3日
	 * return_type
	 * CommentCountMongoDao
	 */
	public void updateCommentDecreaseLikes(String commentid){
		try {
			Query query = Query.query(Criteria.where("commentid").is(commentid));
			Update update = new Update();
			//递增-1
			update.inc("likes", -1);
			mongoTemplate1.updateMulti(query, update, CommentCount.class);
		} catch (Exception e) {
			logger.error("updateCommentDecreaseLikes msg = {}", e);
		}
	}
	
}
