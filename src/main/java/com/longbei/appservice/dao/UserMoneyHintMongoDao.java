package com.longbei.appservice.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.entity.UserMoneyHint;

@Repository
public class UserMoneyHintMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(UserMoneyHintMongoDao.class);
	
	
	/**
	 * @author yinxc
	 * 添加
	 * 2017年6月13日
	 */
	public void insertUserMoneyHint(UserMoneyHint userMoneyHint){
		try {
			mongoTemplate1.insert(userMoneyHint);
		} catch (Exception e) {
			logger.error("insertUserMoneyHint userMoneyHint = {}", JSON.toJSON(userMoneyHint).toString(), e);
		}
	}
	
	/**
	 * @author yinxc
	 * 查询
	 * 2017年6月13日
	 */
	public UserMoneyHint selectUserMoneyHintByUid(String userid){
		UserMoneyHint userMoneyHint = null;
		try {
			Criteria criteria  = Criteria.where("userid").is(userid);
			Query query = Query.query(criteria);
			userMoneyHint = mongoTemplate1.findOne(query,UserMoneyHint.class);
		} catch (Exception e) {
			logger.error("selectUserMoneyHintByUid userid = {}", userid, e);
		}
		return userMoneyHint;
	}
	
	/**
	 * @author yinxc
	 * 查询
	 * 2017年6月13日
	 */
	public void updateHintAddMoney(String userid, Integer num){
		try {
			Query query = Query.query(Criteria.where("userid").is(userid));
			Update update = new Update();
			update.set("drawtime", DateUtils.formatDateTime1(new Date()));
			//递增+1
			update.inc("money", num);
			mongoTemplate1.updateMulti(query, update, UserMoneyHint.class);
		} catch (Exception e) {
			logger.error("updateHintAddMoney userid = {}, num = {}", userid, num, e);
		}
	}
	
}
