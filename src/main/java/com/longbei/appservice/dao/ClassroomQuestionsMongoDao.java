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
	public List<ClassroomQuestions> selectQuestionsListByClassroomid(String classroomid, int startNo,
			int pageSize){
		Criteria criteria  = Criteria.where("classroomid").is(classroomid);
		Query query = Query.query(criteria);
		query.with(new Sort(Direction.DESC, "createdate"));
		query.limit(pageSize);
		List<ClassroomQuestions> list = null;
		try {
			list = mongoTemplate1.find(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectQuestionsListByClassroomid classroomid = {}, startNo = {}, pageSize = {}", 
					classroomid, startNo, pageSize, e);
		}
		return list;
	}
	
	/**
	 * @author yinxc
	 * 获取教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	public ClassroomQuestions selectQuestionsByid(String id){
		ClassroomQuestions classroomQuestions = null;
		try {
			Query query = Query.query(Criteria.where("_id").is(id));
			classroomQuestions = mongoTemplate1.findOne(query, ClassroomQuestions.class);
		} catch (Exception e) {
			logger.error("selectQuestionsByid id = {}", id, e);
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
	
}
