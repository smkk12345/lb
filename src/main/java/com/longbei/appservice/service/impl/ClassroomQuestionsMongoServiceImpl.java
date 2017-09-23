package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.ClassroomQuestionsLowerMongoDao;
import com.longbei.appservice.dao.ClassroomQuestionsMongoDao;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.service.ClassroomQuestionsMongoService;
import com.longbei.appservice.service.ClassroomService;
import com.longbei.appservice.service.UserMsgService;
import com.longbei.appservice.service.UserRelationService;

import net.sf.json.JSONObject;

@Service("classroomQuestionsMongoService")
public class ClassroomQuestionsMongoServiceImpl implements ClassroomQuestionsMongoService {
	
	@Autowired
	private ClassroomQuestionsMongoDao classroomQuestionsMongoDao;
	@Autowired
	private ClassroomQuestionsLowerMongoDao classroomQuestionsLowerMongoDao;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private ClassroomQuestionsMongoService classroomQuestionsMongoService;
	@Autowired
	private UserCardMapper userCardMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	
	
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomQuestionsMongoServiceImpl.class);
	
	

	@Override
	public long selectCountQuestions(String classroomid) {
		try {
			long totalcount = classroomQuestionsMongoDao.selectCountQuestions(classroomid);
			return totalcount;
		} catch (Exception e) {
			logger.error("selectCountQuestions classroomid = {}", classroomid, e);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertQuestions(ClassroomQuestions classroomQuestions) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//判断用户是否已加入教室，未加入教室的无法提交问题
			ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(Long.parseLong(classroomQuestions.getClassroomid()), 
					Long.parseLong(classroomQuestions.getUserid()), "0");
			if(null == classroomMembers){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1102, Constant.RTNINFO_SYS_1102);
			}
			insert(classroomQuestions);
			Classroom classroom = classroomMapper.selectByPrimaryKey(Long.parseLong(classroomQuestions.getClassroomid()));
			if(null != classroom){
				reseResp.getExpandData().put("isteacher", classroomService.isTeacher(classroomQuestions.getUserid().toString(), classroom.getUserid()));
//				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				initQuestionsUserInfoByUserid(classroomQuestions, null);
//				classroomQuestions.setIsreply(isreply);
				//sourcetype 0:运营  1:app  2:商户
				if("1".equals(classroom.getSourcetype())){
					//推送@我消息
					String remark = "您的教室《" + classroom.getClasstitle() + "》中收到新的提问:" + classroomQuestions.getContent();
					userMsgService.insertMsg(Constant.SQUARE_USER_ID, classroom.getUserid().toString(), 
							"", "12", classroomQuestions.getClassroomid() + "", remark, "2", "61", "教室收到新的提问", 0, "", "");
				}
			}
			reseResp.setData(classroomQuestions);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertQuestions classroomQuestions = {}", JSONArray.toJSON(classroomQuestions).toString(), e);
		}
		return reseResp;
	}
	
	private void insert(ClassroomQuestions classroomQuestions){
		try {
			classroomQuestionsMongoDao.insertQuestions(classroomQuestions);
		} catch (Exception e) {
			logger.error("insert classroomQuestions = {}", JSONObject.fromObject(classroomQuestions).toString(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<List<ClassroomQuestions>> selectQuestionsListByClassroomid(String classroomid, String userid, Date lastDate, int pageSize) {
		BaseResp<List<ClassroomQuestions>> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
			if(null == classroom){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
//	  		List<String> idlist = new ArrayList<>();
//	  		if(null != classroom){
//	  			idlist = userCardMapper.selectUseridByCardid(classroom.getCardid());
//	  		}
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			if(null == userCard){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
			List<ClassroomQuestions> list = classroomQuestionsMongoDao.selectQuestionsListByClassroomid(classroomid, lastDate, pageSize);
			if(null != list && list.size()>0){
				for (ClassroomQuestions classroomQuestions : list) {
					initQuestionsUserInfoByUserid(classroomQuestions, userid);
					
					//获取老师回复
					List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(classroomQuestions.getId());
					String isreply = "0";
					if(null != lowerlist && lowerlist.size()>0){
//						initQuestionsLowerUserInfoList(lowerlist);
						ClassroomQuestionsLower classroomQuestionsLower = lowerlist.get(0);
						AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
						appUserMongoEntity.setAvatar(userCard.getAvatar());
						appUserMongoEntity.setId(classroomQuestionsLower.getUserid().toString());
						appUserMongoEntity.setUserid(classroomQuestionsLower.getUserid().toString());
						appUserMongoEntity.setNickname(userCard.getDisplayname());
						classroomQuestionsLower.setAppUserMongoEntityUserid(appUserMongoEntity);
						//已回答
	  					isreply = "1";
	  					classroomQuestions.setClassroomQuestionsLower(classroomQuestionsLower);
					}
					classroomQuestions.setIsreply(isreply);
				}
			}
			reseResp.setData(list);
			Map<String, Object> expandData = new HashMap<>();
			expandData.put("cardid", userCard.getUserid());
			//是否为老师
			expandData.put("isteacher",classroomService.isTeacher(userid,classroom.getUserid()));
			reseResp.setExpandData(expandData);
			if(list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_35);
				return reseResp;
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectQuestionsListByClassroomid classroomid = {}, lastDate = {}, pageSize = {}", 
					classroomid, lastDate, pageSize, e);
		}
		return reseResp;
	}

	@Override
	public Page<ClassroomQuestions> selectQuestionsList(String classroomId, String dealStatus, String nickname, String startCreatetime, String endCreatetime, Integer startNum, Integer pageSize) {

		Page<ClassroomQuestions> page = new Page<>(startNum / pageSize + 1, pageSize);
		try {
			AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
			if(StringUtils.isNotBlank(nickname)){
				appUserMongoEntity = userMongoDao.getAppUserByNickName(nickname);
			}
			List<ClassroomQuestions> list = classroomQuestionsMongoDao.selectQuestionsList(classroomId, dealStatus,appUserMongoEntity.getUserid()+"", startCreatetime, endCreatetime, startNum, pageSize);
			long totalcount = classroomQuestionsMongoDao.selectQuestionsListCount(classroomId, dealStatus, appUserMongoEntity.getUserid()+"", startCreatetime,endCreatetime);
			if (null != list && list.size() > 0) {
				for (ClassroomQuestions classroomQuestions : list) {
					initQuestionsUserInfoByUserid(classroomQuestions, null);
					List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(classroomQuestions.getId());
					String isreply = "0";
					if (null != lowerlist && lowerlist.size() > 0) {
						initQuestionsLowerUserInfoList(lowerlist);
						isreply = "1";
						classroomQuestions.setClassroomQuestionsLower(lowerlist.get(0));
					}
					classroomQuestionsMongoService.updateQuestionsIsReply(classroomQuestions.getId(),isreply);
					classroomQuestions.setIsreply(isreply);
				}
			}
			page.setTotalCount((int)totalcount);
			page.setList(list);
		} catch (Exception e) {
			logger.error("selectQuestionsList for adminservice error and msg={}", e);
		}
		return page;
	 }

	@Override
	public BaseResp<Integer> selectQuestionsListCount(String classroomId,String dealStatus,String nickname,String startCreatetime,String endCreatetime) {
		BaseResp<Integer> reseResp = new BaseResp<>();
		try {
			AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
			if(StringUtils.isNotBlank(nickname)){
				appUserMongoEntity = userMongoDao.getAppUserByNickName(nickname);
			}
			long totalcount = classroomQuestionsMongoDao.selectQuestionsListCount(classroomId, dealStatus, appUserMongoEntity.getUserid()+"", startCreatetime,endCreatetime);
			reseResp.setData((int)totalcount);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectQuestionsListCount for adminservice error and msg={}", e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<ClassroomQuestions> selectQuestionsByQuestionsId(String questionsId) {
		BaseResp<ClassroomQuestions> reseResp = new BaseResp<>();
		try {
			ClassroomQuestions classroomQuestions = classroomQuestionsMongoDao.selectQuestionsByQuestionsId(questionsId);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(classroomQuestions);
		} catch (Exception e) {
			logger.error("selectQuestionsByQuestionsId questionsId = {}", questionsId, e);
		}
		return reseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> deleteQuestions(String id, String userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//先判断老师是否已回复，已回复的学员无法删除
			List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(id);
			if(null != lowerlist && lowerlist.size()>0){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1101, Constant.RTNINFO_SYS_1101);
			}
			//先删除老师回复的信息
//			deleteLowerByQid(id);
			delete(id, userid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteQuestions id = {}", id, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> deleteQuestionsByQuestionsId(String questionsId) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			classroomQuestionsMongoDao.deleteQuestionsByQuestionsid(questionsId);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteQuestions questionsId = {}", questionsId, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 删除问题时，根据问题id删除老师回复问题的数据
	 * 2017年3月1日
	 * param questionsId 问题id
	 */
	@Override
	public BaseResp<Object> deleteLowerByQuestionsId(String questionsId){
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			classroomQuestionsLowerMongoDao.deleteLowerByQuestionsid(questionsId);
			classroomQuestionsMongoService.updateQuestionsIsReply(questionsId,"1");
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteLowerByQid and questionsId = {}", questionsId, e);
		}
		return reseResp;
	}
	
	private void delete(String id, String userid){
		try {
			classroomQuestionsMongoDao.deleteQuestions(id, userid);
		} catch (Exception e) {
			logger.error("delete id = {}", id, e);
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			ClassroomQuestions classroomQuestions = classroomQuestionsMongoDao.selectQuestionsByQuestionsId(classroomQuestionsLower.getQuestionsid());
			Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomQuestions.getClassroomid()));
			if(null == classroom){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			if(null == userCard){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
//			classroomQuestionsLower.setUserid(classroom.getCardid()+"");
//			classroomQuestionsLower.setFriendid(classroomQuestions.getUserid());
			//获取老师回复,已回复的无法忽略
			List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(classroomQuestionsLower.getQuestionsid());
			if(null != lowerlist && lowerlist.size()>0){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1100, Constant.RTNINFO_SYS_1100);
			}
			insertLower(classroomQuestionsLower);
			String remark = "您在教室《" + classroom.getClasstitle() + "》中的提问已被回复："+ classroomQuestionsLower.getContent();
			//sourcetype 0:运营  1:app  2:商户
			if("0".equals(classroom.getSourcetype())){
				//推送@我消息
				userMsgService.insertMsg(Constant.SQUARE_USER_ID, classroomQuestionsLower.getFriendid(), 
						"", "12", classroomQuestions.getClassroomid() + "", remark, "2", "13", "教室老师回复问题", 0, "", "");
			}else if("1".equals(classroom.getSourcetype())){
				//推送@我消息
				userMsgService.insertMsg(classroom.getUserid().toString(), classroomQuestionsLower.getFriendid(), 
						"", "12", classroomQuestions.getClassroomid() + "", remark, "2", "13", "教室老师回复问题", 0, "", "");
			}
			
			
//			initQuestionsLower(classroomQuestionsLower);
			AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
			appUserMongoEntity.setAvatar(userCard.getAvatar());
			appUserMongoEntity.setId(classroomQuestionsLower.getUserid().toString());
			appUserMongoEntity.setUserid(classroomQuestionsLower.getUserid().toString());
			appUserMongoEntity.setNickname(userCard.getDisplayname());
			classroomQuestionsLower.setAppUserMongoEntityUserid(appUserMongoEntity);
			
			reseResp.setData(classroomQuestionsLower);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertQuestionsLower classroomQuestionsLower = {}", 
					JSONArray.toJSON(classroomQuestionsLower).toString(), e);
		}
		return reseResp;
	}

	private void insertLower(ClassroomQuestionsLower classroomQuestionsLower){
		try {
			classroomQuestionsLowerMongoDao.insertQuestionsLower(classroomQuestionsLower);
			classroomQuestionsMongoService.updateQuestionsIsReply(classroomQuestionsLower.getQuestionsid(),"1");
		} catch (Exception e) {
			logger.error("insertLower classroomQuestionsLower = {}", 
					JSONObject.fromObject(classroomQuestionsLower).toString(), e);
		}
	}

	@Override
	public BaseResp<Object> updateQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			classroomQuestionsLowerMongoDao.updateQuestionsLower(classroomQuestionsLower);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("updateQuestionsLower and classroomQuestionsLower = {}",
					JSONObject.fromObject(classroomQuestionsLower).toString(), e);
		}
		return reseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> deleteQuestionsLower(String classroomid, String id, String userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			if(Long.parseLong(userid) != userCard.getUserid()){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1103, Constant.RTNINFO_SYS_1103);
			}
			deleteLower(id, userid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteQuestionsLower id = {}", id, e);
		}
		return reseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateQuestionsIsIgnore(String questionsId,String isIgnore) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//先判断老师是否已回复，已回复无法忽略
			List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(questionsId);
			if(null != lowerlist && lowerlist.size()>0){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1110, Constant.RTNINFO_SYS_1110);
			}
			classroomQuestionsMongoDao.updateQuestionsIsIgnore(questionsId,isIgnore);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("updateQuestionsIsIgnore and questionsId = {}", questionsId, e);
		}
		return reseResp;
	}

	private void deleteLower(String id, String userid){
		try {
			classroomQuestionsLowerMongoDao.deleteQuestionsLower(id, userid);
			classroomQuestionsMongoService.updateQuestionsIsReply(id,"0");
		} catch (Exception e) {
			logger.error("deleteLower id = {}", id, e);
		}
	}


	@Override
	public BaseResp<Object> updateQuestionsIsReply(String questionsId,String isReply) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			classroomQuestionsMongoDao.updateQuestionsIsReply(questionsId,isReply);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("updateQuestionsIsIgnore and questionsId = {}", questionsId, e);
		}
		return reseResp;
	}
	
	//------------------------公用方法，初始化用户信息------------------------------------------
    /**
     * 初始化用户信息 ------List
     */
    private void initQuestionsLowerUserInfoList(List<ClassroomQuestionsLower> lowerlist){
    	if(null != lowerlist && lowerlist.size()>0){
    		for (ClassroomQuestionsLower classroomQuestionsLower : lowerlist) {
    	        AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(classroomQuestionsLower.getUserid()));
    	        classroomQuestionsLower.setAppUserMongoEntityUserid(appUserMongo);
			}
    	}
        
    }
    
//    /**
//     * 初始化用户信息 ------
//     */
//    private void initQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower){
//    	if(null != classroomQuestionsLower){
//	        AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(classroomQuestionsLower.getUserid()));
//	        classroomQuestionsLower.setAppUserMongoEntityUserid(appUserMongo);
//    	}
//        
//    }
    
    /**
     * 初始化消息中用户信息 ------Userid
     */
    private void initQuestionsUserInfoByUserid(ClassroomQuestions classroomQuestions, String userid){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomQuestions.getUserid()));
        if(null != appUserMongoEntity){
			if(StringUtils.isNotEmpty(userid) && !userid.toString().equals(Constant.VISITOR_UID)){
				this.userRelationService.updateFriendRemark(userid,appUserMongoEntity);
			}
			classroomQuestions.setAppUserMongoEntityUserid(appUserMongoEntity);
        }else{
        	classroomQuestions.setAppUserMongoEntityUserid(new AppUserMongoEntity());
        }
    }

}
