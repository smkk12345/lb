package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.ClassroomQuestionsLowerMongoDao;
import com.longbei.appservice.dao.ClassroomQuestionsMongoDao;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomQuestions;
import com.longbei.appservice.entity.ClassroomQuestionsLower;
import com.longbei.appservice.service.ClassroomQuestionsMongoService;
import com.longbei.appservice.service.ClassroomService;

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
	private UserCardMapper userCardMapper;
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
			List<String> list = classroomMembersMapper.selectMidByCid(Long.parseLong(classroomQuestions.getClassroomid()));
			if(null != list && list.size()>0){
				if(!list.contains(classroomQuestions.getUserid())){
					return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1102, Constant.RTNINFO_SYS_1102);
				}
			}else{
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1102, Constant.RTNINFO_SYS_1102);
			}
			insert(classroomQuestions);
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

	@Override
	public BaseResp<Object> selectQuestionsListByClassroomid(String classroomid, String userid, int startNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
	  		List<String> idlist = new ArrayList<>();
	  		if(null != classroom){
	  			idlist = userCardMapper.selectUseridByCardid(classroom.getCardid());
	  		}
			List<ClassroomQuestions> list = classroomQuestionsMongoDao.selectQuestionsListByClassroomid(classroomid, startNo, pageSize);
			if(null != list && list.size()>0){
				for (ClassroomQuestions classroomQuestions : list) {
					initQuestionsUserInfoByUserid(classroomQuestions);
					//获取老师回复
					List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(classroomQuestions.getId());
					String isreply = "0";
					if(null != lowerlist && lowerlist.size()>0){
						initQuestionsLowerUserInfoList(lowerlist);
						//已回答
	  					isreply = "1";
					}
					if(!"1".equals(isreply)){
	  					//判断当前用户是否是老师
	  					if(null != idlist && idlist.size()>0){
	  						if(!idlist.contains(userid)){
	  							isreply = "2";
	  						}
	  					}else{
	  						isreply = "2";
	  					}
	  				}
					classroomQuestions.setIsreply(isreply);
					classroomQuestions.setLowerList(lowerlist);
				}
			}
			reseResp.setData(list);
			if(startNo == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_35);
				return reseResp;
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectQuestionsListByClassroomid classroomid = {}, startNo = {}, pageSize = {}", 
					classroomid, startNo, pageSize, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> selectQuestionsByid(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			ClassroomQuestions classroomQuestions = classroomQuestionsMongoDao.selectQuestionsByid(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(classroomQuestions);
		} catch (Exception e) {
			logger.error("selectQuestionsByid id = {}", id, e);
		}
		return reseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> deleteQuestions(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//先判断老师是否已回复，已回复的学员无法删除
			List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(id);
			if(null != lowerlist && lowerlist.size()>0){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1101, Constant.RTNINFO_SYS_1101);
			}
			//先删除老师回复的信息
//			deleteLowerByQid(id);
			delete(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteQuestions id = {}", id, e);
		}
		return reseResp;
	}
	
//	/**
//	 * @author yinxc
//	 * 删除问题时，根据问题id删除老师回复问题的数据
//	 * 2017年3月1日
//	 * param questionsid 问题id
//	 */
//	private void deleteLowerByQid(String questionsid){
//		try {
//			classroomQuestionsLowerMongoDao.deleteLowerByQuestionsid(questionsid);;
//		} catch (Exception e) {
//			logger.error("deleteLowerByQid questionsid = {}", questionsid, e);
//		}
//	}
	
	private void delete(String id){
		try {
			classroomQuestionsMongoDao.deleteQuestions(id);
		} catch (Exception e) {
			logger.error("delete id = {}", id, e);
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> insertQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//获取老师回复
			List<ClassroomQuestionsLower> lowerlist = classroomQuestionsLowerMongoDao.selectQuestionsLowerListByQuestionsid(classroomQuestionsLower.getQuestionsid());
			if(null != lowerlist && lowerlist.size()>0){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1100, Constant.RTNINFO_SYS_1100);
			}
			insertLower(classroomQuestionsLower);
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
		} catch (Exception e) {
			logger.error("insertLower classroomQuestionsLower = {}", 
					JSONObject.fromObject(classroomQuestionsLower).toString(), e);
		}
	}

	@Override
	public BaseResp<Object> deleteQuestionsLower(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteLower(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteQuestionsLower id = {}", id, e);
		}
		return reseResp;
	}

	private void deleteLower(String id){
		try {
			classroomQuestionsLowerMongoDao.deleteQuestionsLower(id);
		} catch (Exception e) {
			logger.error("deleteLower id = {}", id, e);
		}
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
    
    /**
     * 初始化消息中用户信息 ------Userid
     */
    private void initQuestionsUserInfoByUserid(ClassroomQuestions classroomQuestions){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomQuestions.getUserid()));
        classroomQuestions.setAppUserMongoEntityUserid(appUserMongoEntity);
    }

}
