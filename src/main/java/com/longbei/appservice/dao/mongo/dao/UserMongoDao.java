package com.longbei.appservice.dao.mongo.dao;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.dao.BaseMongoDao;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.GPSUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.UserInfo;

import com.longbei.appservice.service.api.outernetservice.IRongYunService;
import com.mongodb.*;
import com.mongodb.util.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserMongoDao extends BaseMongoDao<AppUserMongoEntity> {
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private MongoTemplate mongoTemplate1;
	@Autowired
	private IRongYunService rongYunService;

	private static Logger logger = LoggerFactory.getLogger(UserMongoDao.class);

	/**--------------------------用户信息----------------------**/
	public void saveAppUserMongoEntity(AppUserMongoEntity user) {
		try {
			save(user);
		} catch (Exception e) {
			//TODO 这个loggerd的参数应该是有问题的，虽然也能工作
			logger.error("saveAppUserMongoEntity user = {}",
					com.alibaba.fastjson.JSON.toJSON(user).toString(), e);
		}
	}
	
	public AppUserMongoEntity updateAppUserMongoEntity(UserInfo user) {
		Query query = Query.query(Criteria.where("_id").is(user.getUserid().toString()));
		Update update = new Update();
		if(!StringUtils.isBlank(user.getUsername())){
			update.set("username", user.getUsername());
		}
		if(!StringUtils.isBlank(user.getNickname())){
			update.set("nickname", user.getNickname());
		}
		if(!StringUtils.isBlank(user.getAvatar())){
			update.set("avatar",user.getAvatar());
		}
		if(!StringUtils.isBlank(user.getSex())){
			update.set("sex", user.getSex());
		}
		if(!StringUtils.isBlank(user.getVcertification())){
			update.set("vcertification", user.getVcertification());
		}
		try {
			mongoTemplate1.upsert(query, update, AppUserMongoEntity.class);
			if(StringUtils.isNotEmpty(user.getNickname()) || StringUtils.isNotEmpty(user.getAvatar())){
				//如果用户更改了用户昵称或者用户头像,则更新融云的用户昵称和头像
				BaseResp<Object> rongyunUpdateResp = this.rongYunService.refreshUserInfo(user.getUserid().toString(),user.getNickname(),user.getAvatar());
			}
		}catch (Exception e) {
			logger.error("updateAppUserMongoEntity user = {}",
					com.alibaba.fastjson.JSON.toJSON(user).toString(), e);
		}
		AppUserMongoEntity mongoUser =  getAppUser(user.getUserid().toString());
		return mongoUser;
	}
	
	public AppUserMongoEntity getAppUser(String userid){
//		logger.info("getAppUser userid={}",userid);
		userid = userid.trim();
		Query query = Query.query(Criteria.where("_id").is(userid));
		try {
			AppUserMongoEntity mongoUser = findOne(query);
			if(null != mongoUser && StringUtils.isNotEmpty(mongoUser.getNickname())){
				return mongoUser;
			}else{
				UserInfo userInfo = userInfoMapper.selectByUserid(Long.parseLong(userid));
				if(userInfo == null){
					return null;
				}
				if(mongoUser != null){
					return updateAppUserMongoEntity(userInfo);
				}
				if(null != userInfo){
					mongoUser = saveUserInfoToMongo(userInfo);
				}
				return  mongoUser;
			}
		} catch (Exception e) {
			logger.error("findOne error and msg={}",e);
		}
		return null;
	}


	public AppUserMongoEntity getAppUserByUserName(String username){
		username = username.trim();
		Query query = Query.query(Criteria.where("username").is(username));
		return getAppUserByQuery(query);
	}

	public AppUserMongoEntity getAppUserByNickName(String nickname){
		nickname = nickname.trim();
		Query query = Query.query(Criteria.where("nickname").is(nickname));
		return getAppUserByQuery(query);
	}

	public List<AppUserMongoEntity> getAppUserByDeviceIndex(String deviceindex){
		deviceindex = deviceindex.trim();
		Query query = Query.query(Criteria.where("deviceindex").is(deviceindex));
		try {
			return find(query);
		} catch (Exception e) {
			logger.error("findOne error and msg={}",e);
		}
		return null;
	}

	private AppUserMongoEntity getAppUserByQuery(Query query){
		try {
			AppUserMongoEntity mongoUser = findOne(query);
			if(null != mongoUser){
				return mongoUser;
			}
		} catch (Exception e) {
			logger.error("findOne error and msg={}",e);
		}
		return null;
	}

	/**
	 * 根据用户名，昵称 模糊查询
	 * @param appUserMongoEntity
	 * @return
	 */
	public List<AppUserMongoEntity> getAppUsers(AppUserMongoEntity appUserMongoEntity){

		Criteria criteria = null;
		if (null != appUserMongoEntity){
			if (!StringUtils.isBlank(appUserMongoEntity.getUsername())){
				criteria = Criteria.where("username").regex(appUserMongoEntity.getUsername());
			}
			if (!StringUtils.isBlank(appUserMongoEntity.getNickname())){
				if (null == criteria){
					criteria = Criteria.where("nickname").regex(appUserMongoEntity.getNickname());
				} else {
					criteria.and("nickname").regex(appUserMongoEntity.getNickname());
				}
			}
		}
		if (null == criteria){
			return null;
		}
		Query query = new Query(criteria);
		List<AppUserMongoEntity> appUserMongoEntities = mongoTemplate.find(query,AppUserMongoEntity.class);
		return appUserMongoEntities;
	}
	
	/**
	 * 保存用户常用信息到mongouser
	 * @param userInfo
	 * @return AppUserMongoEntity
	 */
	private AppUserMongoEntity saveUserInfoToMongo(UserInfo userInfo){
		AppUserMongoEntity userMongoEntity = new AppUserMongoEntity();
		userMongoEntity.setAvatar(userInfo.getAvatar());
		userMongoEntity.setId(String.valueOf(userInfo.getUserid()));
		userMongoEntity.setUsername(userInfo.getUsername());
		userMongoEntity.setSex(userInfo.getSex());
		userMongoEntity.setNickname(userInfo.getNickname());
		userMongoEntity.setCreatetime(DateUtils.formatDateTime1(userInfo.getCreatetime()));
		saveAppUserMongoEntity(userMongoEntity);
		return userMongoEntity;
	}
	
	public boolean existsUser(String userid){
		Query query = Query.query(Criteria.where("userid").is(userid));
		return mongoTemplate.exists(query, AppUserMongoEntity.class);
	}


	public void updateGps(long userid, double longitude, double latitude, String dateStr) {
		try {
			Query query = Query.query(Criteria.where("_id").is(String.valueOf(userid)));
			Update update = new Update();
			Double[] dArr = {longitude, latitude};
			update.set("gispoint",dArr);
			update.set("updatetime",new Date().getTime());
			AppUserMongoEntity appUserMongoEntity = updateOne(query,update);
			JSONObject.fromObject(appUserMongoEntity).toString();
		}catch (Exception e){
			logger.error("updateGps userid={},longitude={},latitude={}",userid,longitude,latitude);
		}
	}

	/**
	 * 附近的人 带距离 分页不好处理
	 * @param longitude
	 * @param latitude
	 * @param distance
	 * @param gender
	 * @param count
	 * @param limit
	 * @return
	 */
	public List<AppUserMongoEntity> findNear1(double longitude, double latitude,
											 double distance,String gender,int count, int limit){
		BasicDBObject myCmd = new BasicDBObject();
		myCmd.append("geoNear", "appuser");
		double[] loc = {longitude,latitude};
		myCmd.append("near", loc);
		myCmd.append("spherical", true);
		myCmd.append("distanceMultiplier", 6378137);
		if(StringUtils.isBlank(gender)){
		}else
			myCmd.append("sex",gender);
		myCmd.append("maxDistance", distance / Constant.DISTANCE_CONVERT_2D );
		if(limit > 0){
			myCmd.append("limit", limit);
		}
		CommandResult myResults = mongoTemplate.getDb().command(myCmd);
		JSONArray jArray = JSONArray.fromObject(myResults.get("results"));
		List<AppUserMongoEntity> list = new ArrayList<>();
		for (int i = 0; i < jArray.size(); i++) {
			if(i<count){
				continue;
			}
			JSONObject jObject = (JSONObject)jArray.get(i);
			String sDis = jObject.getString("dis");
			BigDecimal bd = new BigDecimal(sDis);
			Double double1 = Double.parseDouble(bd.toPlainString());
			AppUserMongoEntity nearUser = (AppUserMongoEntity)JSONObject.toBean(jObject.getJSONObject("obj"),AppUserMongoEntity.class);
			nearUser.setDistance(double1.intValue());
			list.add(nearUser);
		}
		return list;
	}
	//{gispoint: {$near : [116.302645,40.035655]}}

	/**
	 * 附近的人 分页距离自己计算
	 * @param longitude
	 * @param latitude
	 * @param distance
	 * @param gender
	 * @param count
	 * @param limit
	 * @return
	 */
	public List<AppUserMongoEntity> findNear(double longitude, double latitude,
											 double distance,String gender,int count, int limit){
		List<AppUserMongoEntity> list = new ArrayList<>();
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.append("gispoint", JSON.parse("{$near : ["+longitude+","+latitude+"]}"));

		if(StringUtils.isNotEmpty(gender)){
			basicDBObject.append("sex",gender);
		}
		DBCollection dbCollection = mongoTemplate.getDb().getCollection("appuser");
		DBCursor dbCursor = dbCollection.find(basicDBObject).skip(count).limit(limit);
		while (dbCursor.hasNext()) {
			try {
				DBObject result = dbCursor.next();
				JSONObject jsonObject = JSONObject.fromObject(result);
				AppUserMongoEntity appUserMongoEntity = (AppUserMongoEntity)JSONObject.toBean(jsonObject,AppUserMongoEntity.class);
				appUserMongoEntity.setId(jsonObject.get("_id").toString());
				int d = (int) GPSUtils.computeDistance(longitude, latitude,
						appUserMongoEntity.getGispoint()[0], appUserMongoEntity.getGispoint()[1]);
				appUserMongoEntity.setDistance(d);
				list.add(appUserMongoEntity);
			} catch (Exception e) {
				logger.error("UserMongoDao错误：",e);
			}
		}
		return list;
	}
}