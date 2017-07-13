package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

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
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomCourses;
import com.longbei.appservice.service.ClassroomCoursesService;

@Service("classroomCoursesService")
public class ClassroomCoursesServiceImpl implements ClassroomCoursesService {
	
	@Autowired
	private ClassroomCoursesMapper classroomCoursesMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomCoursesServiceImpl.class);

	@Override
	public BaseResp<Object> selectListByClassroomid(long classroomid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<ClassroomCourses> list = classroomCoursesMapper.selectListByClassroomid(classroomid, startNum, endNum);
			//教室课程总数
			Integer coursesNum = classroomCoursesMapper.selectCountCourses(classroomid);
			reseResp.getExpandData().put("coursesNum", coursesNum);
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
			boolean temp = updatedel(classroomid, id);
			if (temp) {
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
		Page<ClassroomCourses> page = new Page<>(startNum, endNum);
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

	@Override
	public BaseResp<Object> updateSortByid(Integer id, long classroomid, Integer coursesort) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = classroomCoursesMapper.updateSortByid(classroomid, id, coursesort);
			if (temp > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateSortByid classroomid = {}, id = {}", classroomid, id, e);
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
			int coursesort = 10000;
			String coursetype = "0";
			//isdefault是否 默认   1 默认封面  0 非默认
			String isdefault = "1";
			List<ClassroomCourses> list = classroomCoursesMapper.selectListByClassroomid(classroomCourses.getClassroomid(), 0, 1);
			if(null != list && list.size()>0){
				coursesort = classroomCoursesMapper.selectMinSort(classroomCourses.getClassroomid()) - 1;
				//isfree是否免费。0 免费 1 收费
				if("1".equals(classroom.getIsfree())){
					coursetype = "1";
				}
				isdefault = "0";
			}
			classroomCourses.setCoursesort(coursesort);
			classroomCourses.setCoursetype(coursetype);
			classroomCourses.setCreatetime(new Date());
			classroomCourses.setIsdefault(isdefault);
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
