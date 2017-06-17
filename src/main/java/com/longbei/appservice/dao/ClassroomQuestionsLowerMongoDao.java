package com.longbei.appservice.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.entity.ClassroomQuestionsLower;

@Repository
public class ClassroomQuestionsLowerMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomQuestionsLowerMongoDao.class);
	
	/**
	 * @author yinxc
	 * 添加教室提问答疑老师回复
	 * 2017年3月1日
	 */
	public void insertQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower){
		try {
			mongoTemplate1.insert(classroomQuestionsLower);
		} catch (Exception e) {
			logger.error("insertQuestionsLower classroomQuestionsLower = {}", 
					JSONArray.toJSON(classroomQuestionsLower).toString(), e);
		}
	}
	
	/**
	 * @author yinxc
	 * 获取教室提问答疑回复列表
	 * 2017年3月1日
	 * param questionsid 教室提问答疑id
	 */
	public List<ClassroomQuestionsLower> selectQuestionsLowerListByQuestionsid(String questionsid){
		Criteria criteria  = Criteria.where("questionsid").is(questionsid);
		Query query = Query.query(criteria);
		List<ClassroomQuestionsLower> list = null;
		try {
			list = mongoTemplate1.find(query, ClassroomQuestionsLower.class);
		} catch (Exception e) {
			logger.error("selectQuestionsLowerListByQuestionsid questionsid = {}", questionsid, e);
		}
		return list;
	}
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑回复信息
	 * 2017年3月1日
	 * param id 提问答疑回复id
	 */
	public void deleteQuestionsLower(String id, String userid){
		try {
			Query query = Query.query(Criteria.where("_id").is(id).and("userid").is(userid));
			mongoTemplate1.remove(query, ClassroomQuestionsLower.class);
		} catch (Exception e) {
			logger.error("deleteQuestionsLower id = {}", id, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑回复信息
	 * 2017年3月1日
	 * param questionsid 提问答疑id
	 */
	public void deleteLowerByQuestionsid(String questionsid){
		try {
			Query query = Query.query(Criteria.where("questionsid").is(questionsid));
			mongoTemplate1.remove(query, ClassroomQuestionsLower.class);
		} catch (Exception e) {
			logger.error("deleteLowerByQuestionsid questionsid = {}", questionsid, e);
		}
	}
	
}
