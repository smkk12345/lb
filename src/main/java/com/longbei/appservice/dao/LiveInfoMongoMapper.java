package com.longbei.appservice.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.entity.LiveInfo;

@Repository
public class LiveInfoMongoMapper {

	@Autowired
	private MongoTemplate mongoTemplate1;
	
	private static Logger logger = LoggerFactory.getLogger(LiveInfoMongoMapper.class);
	
	/**
	 * @author yinxc
	 * 添加
	 */
	public void insertLiveInfo(LiveInfo liveInfo){
		try {
			mongoTemplate1.insert(liveInfo);
		} catch (Exception e) {
			logger.error("insertLiveInfo liveInfo = {}", JSON.toJSON(liveInfo).toString(), e);
		}
	}
	
	/**
	 * @author yinxc
	 * 根据liveid查询
	 */
	public LiveInfo selectLiveInfoByLiveid(long liveid){
		Criteria criteria  = Criteria.where("liveid").is(liveid);
		Query query = Query.query(criteria);
		LiveInfo liveInfo = null;
		try {
			liveInfo = mongoTemplate1.findOne(query,LiveInfo.class);
		} catch (Exception e) {
			logger.error("selectLiveInfoByLiveid liveid = {}", liveid, e);
		}
		return liveInfo;
	}
	
}
