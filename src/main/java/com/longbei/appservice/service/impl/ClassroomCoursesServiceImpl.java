package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ClassroomCoursesMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.UserBusinessConcernMapper;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomCourses;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.entity.UserBusinessConcern;
import com.longbei.appservice.service.ClassroomCoursesService;
import com.longbei.appservice.service.UserMsgService;

@Service("classroomCoursesService")
public class ClassroomCoursesServiceImpl implements ClassroomCoursesService {
	
	@Autowired
	private ClassroomCoursesMapper classroomCoursesMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private UserBusinessConcernMapper userBusinessConcernMapper;
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	
	
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomCoursesServiceImpl.class);

	@Override
	public BaseResp<List<ClassroomCourses>> selectListByClassroomid(long classroomid, int startNum, int endNum) {
		Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
		BaseResp<List<ClassroomCourses>> reseResp = new BaseResp<>();
		try {
			List<ClassroomCourses> list = classroomCoursesMapper.selectListByClassroomid(classroomid, "1", startNum, endNum);
			//教室课程总数
			reseResp.getExpandData().put("coursesNum", classroom.getAllcourses());
			reseResp.setData(list);
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_34);
				return reseResp;
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByClassroomid classroomid = {}, startNum = {}, endNum = {}", 
					classroomid, startNum, endNum, e);
		}
		return reseResp;
	}

	@Override
	public ClassroomCourses selectByid(Integer id) {
		try {
			ClassroomCourses classroomCourses = classroomCoursesMapper.selectByPrimaryKey(id);
			return classroomCourses;
		} catch (Exception e) {
			logger.error("selectByid id = {}", id, e);
		}
		return null;
	}

	@Override
	public ClassroomCourses selectIsdefaultByClassroomid(long classroomid) {
		try {
			ClassroomCourses classroomCourses = classroomCoursesMapper.selectIsdefaultByClassroomid(classroomid);
			return classroomCourses;
		} catch (Exception e) {
			logger.error("selectIsdefaultByClassroomid classroomid = {}", classroomid, e);
		}
		return null;
	}

	@Override
	public BaseResp<Object> updateIsdefaultByid(Integer id, long classroomid, String isdefault) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//先修改所有的课程为非默认
			classroomCoursesMapper.updateIsdefaultByClassroomid(classroomid);
			boolean temp = updateIsdefault(id, isdefault);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsdefaultByid id = {}, classroomid = {}, isdefault = {}", 
					id, classroomid, isdefault, e);
		}
		return reseResp;
	}
	
	private boolean updateIsdefault(Integer id, String isdefault){
		int temp = classroomCoursesMapper.updateIsdefaultByid(id, isdefault);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsdel(long classroomid, Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if("1".equals(classroom.getIsup()) && classroom.getAllcourses() == 1){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_1109, Constant.RTNINFO_SYS_1109);
				return reseResp;
			}
			ClassroomCourses classroomCourses = classroomCoursesMapper.select(classroomid, id);
			boolean temp = updatedel(classroomid, id);
			if (temp) {
				//isup是否上架   0：未上架    1：已上架
				if("1".equals(classroomCourses.getIsup())){
					//修改课程数量
					classroomMapper.updateAllcoursesByClassroomid(classroomid, -1);
				}
				//教室课程第一课改为免费
				if("1".equals(classroom.getIsfree())){
					classroomCoursesMapper.updateCoursetypeByClassroomid(classroomid, "1");
					List<ClassroomCourses> courselist = classroomCoursesMapper.selectListByClassroomid(classroomid, "1", 0, 1);
					if(null != courselist && courselist.size()>0){
						classroomCoursesMapper.updateCoursetypeByid(classroomid, courselist.get(0).getId(), "0");
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsdel classroomid = {}, id = {}", classroomid, id, e);
		}
		return reseResp;
	}
	
	private boolean updatedel(long classroomid, Integer id){
		int temp = classroomCoursesMapper.updateIsdel(classroomid, id);
		return temp > 0 ? true : false;
	}
	

	@Override
	public BaseResp<Page<ClassroomCourses>> selectPcSearchCroomCoursesList(ClassroomCourses classroomCourses,
			int startNum, int endNum) {
		BaseResp<Page<ClassroomCourses>> reseResp = new BaseResp<>();
		Page<ClassroomCourses> page = new Page<>(startNum/endNum+1, endNum);
		try {
			int totalcount = classroomCoursesMapper.selectSearchCount(classroomCourses);
			List<ClassroomCourses> list = classroomCoursesMapper.selectSearchList(classroomCourses, startNum, endNum);
			page.setTotalCount(totalcount);
            page.setList(list);
			reseResp.setData(page);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectPcSearchCroomCoursesList classroomid = {}, startNum = {}, endNum = {}", 
					classroomCourses.getClassroomid(), startNum, endNum, e);
		}
		return reseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateSortByid(Integer id, long classroomid, Integer coursesort) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(null == classroom){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
			int temp = classroomCoursesMapper.updateSortByid(classroomid, id, coursesort);
			if (temp > 0) {
				//教室课程第一课改为免费
				if("1".equals(classroom.getIsfree())){
					classroomCoursesMapper.updateCoursetypeByClassroomid(classroomid, "1");
					List<ClassroomCourses> courselist = classroomCoursesMapper.selectListByClassroomid(classroomid, "1", 0, 1);
					if(null != courselist && courselist.size()>0){
						classroomCoursesMapper.updateCoursetypeByid(classroomid, courselist.get(0).getId(), "0");
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateSortByid classroomid = {}, id = {}", classroomid, id, e);
		}
		return reseResp;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateIsup(Integer id, long classroomid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(null == classroom){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
			List<ClassroomCourses> list = classroomCoursesMapper.selectListByClassroomid(classroomid, "1", 0, 1);
			if(null != list && list.size()>0){
				//修改课程排序
				Integer coursesort = classroomCoursesMapper.selectMaxSort(classroomid) + 1;
				classroomCoursesMapper.updateSortByid(classroomid, id, coursesort);
			}else{
				if("1".equals(classroom.getIsfree())){
					classroomCoursesMapper.updateCoursetypeByid(classroomid, id, "0");
				}
				//修改课程排序
				classroomCoursesMapper.updateSortByid(classroomid, id, 1);
			}
			int temp = classroomCoursesMapper.updateIsupByid("1", id, classroomid);
			if (temp > 0) {
				//修改课程数量
				classroomMapper.updateAllcoursesByClassroomid(classroomid, 1);
				
				//推送消息---已关注该教室的人员
				String remark = Constant.MSG_CLASSROOMCOURSES_FANS_MODEL;
				remark = remark.replace("n", classroom.getClasstitle());
				Map<String,Object> map = new HashMap<String,Object>();
	            map.put("businessType","4");
	            map.put("businessId", classroom.getClassroomid());
	            List<UserBusinessConcern> concernList = this.userBusinessConcernMapper.findConcernUserList(map);
				if(null != concernList && concernList.size()>0){
					for (UserBusinessConcern userBusinessConcern : concernList) {
						userMsgService.insertMsg(Constant.SQUARE_USER_ID, userBusinessConcern.getUserid().toString(), 
								"", "12", classroom.getClassroomid() + "", remark, "0", "58", "教室添加新课程", 0, "", "");
					}
				}
				
				//推送消息---已加入该教室的人员
				String insertRemark = Constant.MSG_CLASSROOMCOURSES_INSERT_MODEL;
				insertRemark = insertRemark.replace("n", classroom.getClasstitle());
				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroom.getClassroomid(),0,0);
				if(null != memberList && memberList.size()>0){
					for (int i=0;i<memberList.size();i++) {
						userMsgService.insertMsg(Constant.SQUARE_USER_ID, memberList.get(i).getUserid()+"",
								"", "12", classroom.getClassroomid() + "", insertRemark, "0", "58", "教室添加新课程", 0, "", "");
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsup classroomid = {}, id = {}", classroomid, id, e);
		}
		return reseResp;
	}


	@Override
	public BaseResp<Object> saveCourses(ClassroomCourses classroomCourses) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomCourses.getClassroomid());
			if(null == classroom){
				return reseResp;
			}
			//获取排序值
//			int coursesort = 10000;
			//coursetype 课程类型.  0 不收费 1 收费
			String coursetype = "0";
			//isdefault是否 默认   1 默认封面  0 非默认
//			String isdefault = "1";
//			List<ClassroomCourses> list = classroomCoursesMapper.selectListByClassroomid(classroomCourses.getClassroomid(), "1", 0, 1);
//			if(null != list && list.size()>0){
////				coursesort = classroomCoursesMapper.selectMaxSort(classroomCourses.getClassroomid()) - 1;
//				isdefault = "0";
//			}
			//isfree是否免费。0 免费 1 收费
			if("1".equals(classroom.getIsfree())){
				coursetype = "1";
			}
			classroomCourses.setCoursesort(0);
			classroomCourses.setCoursetype(coursetype);
			classroomCourses.setCreatetime(new Date());
			classroomCourses.setIsdefault("0");
			classroomCourses.setIsdel("0");
			classroomCourses.setUdpatetime(new Date());
			int temp = classroomCoursesMapper.insertSelective(classroomCourses);
			if (temp > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("saveCourses classroomCourses = {}", JSON.toJSONString(classroomCourses), e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> editCourses(ClassroomCourses classroomCourses) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = classroomCoursesMapper.updateByPrimaryKeySelective(classroomCourses);
			if (temp > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("editCourses classroomCourses = {}", JSON.toJSONString(classroomCourses), e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<ClassroomCourses> selectCourses(long classroomid, Integer id) {
		BaseResp<ClassroomCourses> reseResp = new BaseResp<>();
		try {
			ClassroomCourses classroomCourses = classroomCoursesMapper.select(classroomid, id);
			reseResp.setData(classroomCourses);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectCourses classroomid = {}, id = {}", classroomid, id, e);
		}
		return reseResp;
	}

	
}
