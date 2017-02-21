package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class UserMongoDao extends BaseMongoDao<AppUserMongoEntity> {

	private static Logger logger = LoggerFactory.getLogger(UserMongoDao.class);

	/**--------------------------用户信息----------------------**/
	public void saveAppUserMongoEntity(AppUserMongoEntity user) {
		try {
			save(user);
		} catch (Exception e) {
			logger.error("saveAppUserMongoEntity error and msg={}",e);
		}
	}
	
	public AppUserMongoEntity updateAppUserMongoEntity(UserInfo user) {
		Query query = Query.query(Criteria.where("userid").is(user.getUserid()));
		Update update = new Update();
		if(StringUtils.isBlank(user.getUsername())){
			update.set("username", user.getUsername());
		}
		if(StringUtils.isBlank(user.getNickname())){
			update.set("nickname", user.getNickname());
		}
		if(StringUtils.isBlank(user.getAvatar())){
			update.set("avatar",user.getAvatar());
		}
		if(StringUtils.isBlank(user.getSex())){
			update.set("sex", user.getSex());
		}
		AppUserMongoEntity mongoUser =  updateOne(query,update);
		return mongoUser;
	}
	
	public AppUserMongoEntity getAppUser(String userid){
		Query query = Query.query(Criteria.where("_id").is(userid));
		try {
			AppUserMongoEntity mongoUser = findOne(query);
			return  mongoUser;
		} catch (Exception e) {
			logger.error("findOne error and msg={}",e);
		}
		return null;
	}
	
	public boolean existsUser(String userid){
		Query query = Query.query(Criteria.where("userid").is(userid));
		return mongoTemplate.exists(query, AppUserMongoEntity.class);
	}
	


	
}