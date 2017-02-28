package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.service.ClassroomMembersService;

@Service("classroomMembersService")
public class ClassroomMembersServiceImpl implements ClassroomMembersService {
	
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomMembersServiceImpl.class);

	@Override
	public BaseResp<Object> insertClassroomMembers(ClassroomMembers record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertClassroomMembers record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean insert(ClassroomMembers record){
		int temp = classroomMembersMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectListByClassroomid(Integer classroomid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<ClassroomMembers> list = classroomMembersMapper.selectListByClassroomid(classroomid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					initMsgUserInfoByUserid(classroomMembers);
					//获取赞，花，钻石总数，教室所发的微进步总数
					
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			reseResp.setData(list);
			if(startNum == 0 && null == list){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_32, Constant.RTNINFO_SYS_32);
			}
		} catch (Exception e) {
			logger.error("selectListByClassroomid classroomid = {}, startNum = {}, endNum = {}", 
					classroomid, startNum, endNum, e);
		}
		return reseResp;
	}

	@Override
	public List<ClassroomMembers> selectInsertByUserid(long userid, int startNum, int endNum) {
		try {
			List<ClassroomMembers> list = classroomMembersMapper.selectInsertByUserid(userid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					initMsgUserInfoByUserid(classroomMembers);
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("selectInsertByUserid userid = {}, startNum = {}, endNum = {}", 
					userid, startNum, endNum, e);
		}
		return null;
	}

	@Override
	public ClassroomMembers selectListByClassroomidAndUserid(Integer classroomid, long userid, String itype) {
		try {
			ClassroomMembers classroomMembers = classroomMembersMapper.selectListByClassroomidAndUserid(classroomid, userid, itype);
			return classroomMembers;
		} catch (Exception e) {
			logger.error("selectListByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return null;
	}

	@Override
	public BaseResp<Object> updateItypeByClassroomidAndUserid(Integer classroomid, long userid, String itype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(classroomid, userid, itype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateItypeByClassroomidAndUserid classroomid = {}, userid = {}, itype = {}", 
					classroomid, userid, itype, e);
		}
		return reseResp;
	}
	
	private boolean update(Integer classroomid, long userid, String itype){
		int temp = classroomMembersMapper.updateItypeByClassroomidAndUserid(classroomid, userid, itype);
		return temp > 0 ? true : false;
	}
	
	
	//------------------------公用方法，初始化教室成员用户信息------------------------------------------
	/**
     * 初始化教室成员用户信息 ------Userid
     */
    private void initMsgUserInfoByUserid(ClassroomMembers classroomMembers){
    	if(!StringUtils.hasBlankParams(classroomMembers.getUserid().toString())){
    		AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(classroomMembers.getUserid()));
    		classroomMembers.setAppUserMongoEntityUserid(appUserMongoEntity);;
    	}
    }

}
