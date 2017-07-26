package com.longbei.appservice.dao;

import java.util.Date;
import java.util.List;

import com.longbei.appservice.common.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
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

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.entity.ClassroomQuestions;

/**
 * @author yinxc
 * 教室问题
 * 2017年3月1日
 */
@Repository
public class ClassroomQuestionsMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomQuestionsMongoDao.class);
	
	/**
	 * @author yinxc
	 * 添加教室提问答疑
	 * 2017年3月1日
	 */
	public void insertQuestions(ClassroomQuestions classroomQuestions){
		try {
			mongoTemplate1.insert(classroomQuestions);
		} catch (Exception e) {
			logger.error("insertQuestions classroomQuestions = {}", 
					JSONArray.toJSON(classroomQuestions).toString(), e);
		}
	}
	
	/**
	 * @author yinxc
	 * 获取教室提问答疑列表
	 * 2017年3月1日
	 * param classroomid 教室id
	 * param startNo  pageSize
	 */
	public List<ClassroomQuestions> selectQuestionsListByClassroomid(String classroomid, Date lastDate,
			int pageSize){
		Criteria criteria  = Criteria.where("classroomid").is(classroomid);
		if (lastDate != null) {
			criteria = criteria.and("createtime").lt(lastDate);
		}
		Query query = Query.query(criteria);
		query.with(new Sort(Direction.DESC, "createtime"));
		if(pageSize != 0){
			query.limit(pageSize);
		}
		List<ClassroomQuestions> list = null;
		try {
			list = mongoTemplate1.find(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectQuestionsListByClassroomid classroomid = {}, lastDate = {}, pageSize = {}", 
					classroomid, lastDate, pageSize, e);
		}
		return list;
	}

	public List<ClassroomQuestions>selectQuestionsList(String classroomId,String dealStatus, String userid, String startCreatetime, String endCreatetime, Integer startNum, Integer pageSize){
		logger.info("selectQuestionsList and classroomId={},dealStatus={},userid={},startCreatetime={},endCreatetime={},startNum={},pageSize={}",classroomId,dealStatus,userid,startCreatetime,startCreatetime,startNum,pageSize);
		Criteria criteria  = Criteria.where("classroomid").is(classroomId);
		initSelectListCondition(criteria,dealStatus,userid,startCreatetime,endCreatetime);
		Query query = Query.query(criteria);
		query.with(new Sort(Direction.DESC, "createtime"));
		if(startNum != 0 || startNum!= null){
			query.skip(startNum);
		}
		if(pageSize != 0 || pageSize !=null){
			query.limit(pageSize);
		}
		List<ClassroomQuestions> list = null;
		try {
			list = mongoTemplate1.find(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectQuestionsList and classroomId={},userid={},startCreatetime={},endCreatetime={},startNum={},pageSize={}",classroomId,userid,startCreatetime,endCreatetime,startNum,pageSize,e);
		}
		return list;
	}

	public Long selectQuestionsListCount(String classroomId, String dealStatus,String userid, String startCreatetime, String endCreatetime){
		Criteria criteria  = Criteria.where("classroomid").is(classroomId);
		initSelectListCondition(criteria,dealStatus,userid,startCreatetime,endCreatetime);
		Query query = Query.query(criteria);
		query.with(new Sort(Direction.DESC, "createtime"));
		List<ClassroomQuestions> list = null;
		Long count = 0L;
		try {
			count= mongoTemplate1.count(query,ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectQuestionsListCount and classroomid={}, userid={}, startCreatetime={}, endCreatetime={}",
					classroomId, userid, startCreatetime, endCreatetime, e);
		}
		return count;
	}

    //查询提问答疑列表条件
	private  void initSelectListCondition(Criteria criteria,String dealStatus, String userid, String startCreatetime, String endCreatetime){
		if (!StringUtils.isBlank(dealStatus)) {	 //dealStatus 处理状态 0未回复 1已忽略 2已回复 3未忽略 4已处理(已忽略+已回复)
			if("0".equals(dealStatus)) {
				criteria = criteria.and("isignore").ne("1").and("isreply").ne("1");
			}
			else if("1".equals(dealStatus)) {
				criteria = criteria.and("isignore").is("1");
			}
			else if("2".equals(dealStatus)) {
				criteria = criteria.and("isreply").is("1");
			}
			else if("3".equals(dealStatus)) {
				criteria.orOperator(Criteria.where("isignore").is("0"),Criteria.where("isignore").is(null));
			}
			else if("4".equals(dealStatus)) {
				criteria.orOperator(Criteria.where("isignore").is("1"),Criteria.where("isreply").is("1"));
			}
		}
		if (!StringUtils.isBlank(userid) &&  !"null".equals(userid)) {
			criteria = criteria.and("userid").is(userid);
		}
		if (!StringUtils.isBlank(startCreatetime) && StringUtils.isBlank(endCreatetime)) {
			Date startDate =DateUtils.parseDate(startCreatetime);
			criteria = criteria.and("createtime").gte(startDate);
		}
		if (!StringUtils.isBlank(endCreatetime) && StringUtils.isBlank(startCreatetime)) {
			Date endDate =DateUtils.parseDate(endCreatetime);
			criteria = criteria.and("createtime").lt(endDate);
		}
		if (!StringUtils.isBlank(endCreatetime) && !StringUtils.isBlank(startCreatetime)) {
			Date startDate =DateUtils.parseDate(startCreatetime);
			Date endDate =DateUtils.parseDate(endCreatetime);
			criteria = criteria.and("createtime").gte(startDate).lt(endDate);
		}
	}
	/**
	 * @author yinxc
	 * 获取教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	public ClassroomQuestions selectQuestionsByQuestionsId(String questionsId){
		ClassroomQuestions classroomQuestions = null;
		try {
			Query query = Query.query(Criteria.where("_id").is(questionsId));
			classroomQuestions = mongoTemplate1.findOne(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectQuestionsByQuestionsId and questionsId = {}", questionsId, e);
		}
		return classroomQuestions;
	}
	
	/**
	 * @author yinxc
	 * 获取当前教室提问答疑总数
	 * 2017年3月1日
	 * param classroomid 教室id
	 */
	public long selectCountQuestions(String classroomid){
		Criteria criteria  = Criteria.where("classroomid").is(classroomid);
		Query query = Query.query(criteria);
		long totalcount = 0;
		try {
			totalcount = mongoTemplate1.count(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectCountQuestions classroomid = {}", classroomid, e);
		}
		return totalcount;
	}
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	public void deleteQuestions(String id, String userid){
		try {
			Query query = Query.query(Criteria.where("_id").is(id).and("userid").is(userid));
			mongoTemplate1.remove(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("deleteQuestions id = {}", id, e);
		}
	}

	public void deleteQuestionsByQuestionsid(String questionsId){
		try {
			Query query = Query.query(Criteria.where("_id").is(questionsId));
			mongoTemplate1.remove(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("deleteQuestionsByQuestionsid and questionsId = {}", questionsId, e);
		}
	}

	public void updateQuestionsIsIgnore(String questionsId, String isIgnore){
		try {
			Update update = new Update();
			Query query = Query.query(Criteria.where("_id").is(questionsId));
			ClassroomQuestions classroomQuestions = mongoTemplate1.findOne(query,ClassroomQuestions.class);
			if (null != classroomQuestions){
				update.set("isignore",isIgnore);
			}
			mongoTemplate1.upsert(query,update,ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("updateQuestionsIsIgnore and questionsId = {},isIgnore={}",questionsId,isIgnore,e);
		}
	}

	public void updateQuestionsIsReply(String questionsId, String isReply){
		try {
			Update update = new Update();
			Query query = Query.query(Criteria.where("_id").is(questionsId));
			ClassroomQuestions classroomQuestions = mongoTemplate1.findOne(query,ClassroomQuestions.class);
			if (null != classroomQuestions){
				update.set("isreply",isReply);
			}
			mongoTemplate1.upsert(query,update,ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("updateQuestionsIsIgnore and questionsId = {},isReply={}",questionsId,isReply,e);
		}
	}
}
