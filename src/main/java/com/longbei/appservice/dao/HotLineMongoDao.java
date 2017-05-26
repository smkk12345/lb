package com.longbei.appservice.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.entity.HotLine;

@Repository
public class HotLineMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(HotLine.class);
	
	/**
	 * @author yinxc
	 * 添加
	 */
	public void insertHotLine(HotLine hotLine){
		try {
			mongoTemplate1.insert(hotLine);
		} catch (Exception e) {
			logger.error("insertHotLine hotLine = {}", JSON.toJSON(hotLine).toString(), e);
		}
	}
	
	/**
	 * @author yinxc
	 * 获取
	 */
	public HotLine selectHotLineByUid(String userid){
		HotLine hotLine = null;
		try {
			Query query = Query.query(Criteria.where("userid").is(userid));
			hotLine = mongoTemplate1.findOne(query, HotLine.class);
		} catch (Exception e) {
			logger.error("selectHotLineByUid userid = {}", userid, e);
		}
		return hotLine;
	}
	
	/**
	 * @author yinxc
	 * 删除
	 */
	public void deleteHotLine(String userid){
		try {
			Query query = Query.query(Criteria.where("userid").is(userid));
			mongoTemplate1.remove(query, HotLine.class);
		} catch (Exception e) {
			logger.error("deleteHotLine userid = {}", userid, e);
		}
	}
	
	/**
	 * @author yinxc
	 * 修改
	 */
	public void updateHotLine(HotLine hotLine){
		try {
			Criteria criteria  = Criteria.where("userid").is(hotLine.getUserid());
			Query query = Query.query(criteria);
			Update update = new Update();
			if (hotLine.getFriendAskmaxtime() != null) {
				update.set("friendAskmaxtime", hotLine.getFriendAskmaxtime());
			}
			if (hotLine.getInformmaxtime() != null) {
				update.set("informmaxtime", hotLine.getInformmaxtime());
			}
			if (hotLine.getMymaxtime() != null) {
				update.set("mymaxtime", hotLine.getMymaxtime());
			}
			if (hotLine.getRankmaxtime() != null) {
				update.set("rankmaxtime", hotLine.getRankmaxtime());
			}
			mongoTemplate1.upsert(query, update, HotLine.class);
		} catch (Exception e) {
			logger.error("updateHotLine hotLine = {}", JSON.toJSON(hotLine).toString(), e);
		}
	}
	
}
