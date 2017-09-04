package com.longbei.appservice.dao;

import java.util.Date;
import java.util.List;

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
		Criteria criteria  = Criteria.where("liveid").is(liveid).and("isdel").is("0");
		Query query = Query.query(criteria);
		LiveInfo liveInfo = null;
		try {
			liveInfo = mongoTemplate1.findOne(query,LiveInfo.class);
		} catch (Exception e) {
			logger.error("selectLiveInfoByLiveid liveid = {}", liveid, e);
		}
		return liveInfo;
	}
	
	/**
	 * @author yinxc
	 * 获取所有的直播信息
	 * @param isdel 是否删除    0:未删除   1：删除
	 */
	public List<LiveInfo> selectLiveInfoList(String isdel){
		Criteria criteria  = Criteria.where("isdel").is(isdel);
		Query query = Query.query(criteria);
		List<LiveInfo> list = null;
		try {
			list = mongoTemplate1.find(query,LiveInfo.class);
		} catch (Exception e) {
			logger.error("selectLiveInfoList isdel = {}", isdel, e);
		}
		return list;
	}
	
	/**
	 * @author yinxc
	 * 修改
	 */
	public void updateLiveInfo(long classroomid, Long courseid, Date starttime, Date endtime){
		try {
			Criteria criteria  = Criteria.where("classroomid").is(classroomid).and("courseid").is(courseid);
			Query query = Query.query(criteria);
			Update update = new Update();
			if (starttime != null) {
				update.set("starttime", starttime);
			}
			if (endtime != null) {
				update.set("endtime", endtime);
			}
			mongoTemplate1.updateMulti(query, update, LiveInfo.class);
		} catch (Exception e) {
			logger.error("updateLiveInfo classroomid = {}, courseid = {}, starttime = {}, endtime = {}", 
					classroomid, courseid, DateUtils.formatDateTime1(starttime), DateUtils.formatDateTime1(endtime), e);
		}
	}
	
	/**
	 * @author yinxc
	 * 删除
	 */
	public void deleteLiveInfo(long classroomid, Long courseid){
		try {
			Criteria criteria  = Criteria.where("classroomid").is(classroomid).and("courseid").is(courseid);
			Query query = Query.query(criteria);
			Update update = new Update();
			update.set("isdel", "1");
			mongoTemplate1.updateMulti(query, update, LiveInfo.class);
		} catch (Exception e) {
			logger.error("deleteLiveInfo classroomid = {}, courseid = {}", 
					classroomid, courseid, e);
		}
	}
	
}
