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
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.service.ClassroomService;

@Service("classroomService")
public class ClassroomServiceImpl implements ClassroomService {
	
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);

	@Override
	public BaseResp<Object> insertClassroom(Classroom record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertClassroom record = {}, msg = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean insert(Classroom record){
		int temp = classroomMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public Classroom selectByClassroomid(long classroomid) {
		Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
		return classroom;
	}

	@Override
	public BaseResp<Object> updateByClassroomid(Classroom record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByClassroomid record = {}, msg = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean update(Classroom record){
		int temp = classroomMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectListByUserid(long userid, String ptype, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Classroom> list = classroomMapper.selectListByUserid(userid, ptype, startNum, endNum);
			reseResp.setData(list);
			if(null != list && list.size()>0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_33, Constant.RTNINFO_SYS_33);
			}
		} catch (Exception e) {
			logger.error("selectListByUserid ptype = {}, userid = {}, startNum = {}, endNum = {}, msg = {}", 
					ptype, userid, startNum, endNum, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 获取我加入的教室信息List
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	@Override
	public BaseResp<Object> selectInsertByUserid(long userid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Classroom> roomlist = new ArrayList<Classroom>();
			List<ClassroomMembers> list = classroomMembersMapper.selectInsertByUserid(userid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					Classroom classroom = classroomMapper.selectByPrimaryKey(classroomMembers.getClassroomid());
					roomlist.add(classroom);
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			if(startNum == 0 && roomlist.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_33, Constant.RTNINFO_SYS_33);
			}
			reseResp.setData(roomlist);
		} catch (Exception e) {
			logger.error("selectInsertByUserid userid = {}, startNum = {}, endNum = {}, msg = {}", 
					userid, startNum, endNum, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> selectListByPtype(String ptype, String keyword, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Classroom> list = classroomMapper.selectListByPtype(ptype, keyword, startNum, endNum);
			reseResp.setData(list);
			if(null != list && list.size()>0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_33, Constant.RTNINFO_SYS_33);
			}
		} catch (Exception e) {
			logger.error("selectListByPtype ptype = {}, keyword = {}, startNum = {}, endNum = {}, msg = {}", 
					ptype, keyword, startNum, endNum, e);
		}
		return reseResp;
	}

}
