package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.ClassroomCoursesMapper;
import com.longbei.appservice.entity.ClassroomCourses;
import com.longbei.appservice.service.ClassroomCoursesService;

@Service("classroomCoursesService")
public class ClassroomCoursesServiceImpl implements ClassroomCoursesService {
	
	@Autowired
	private ClassroomCoursesMapper classroomCoursesMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomCoursesServiceImpl.class);

	@Override
	public BaseResp<Object> selectListByClassroomid(long classroomid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<ClassroomCourses> list = classroomCoursesMapper.selectListByClassroomid(classroomid, startNum, endNum);
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
			logger.error("updateIsdefaultByid id = {}, isdefault = {}", 
					id, isdefault, e);
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
			logger.error("updateIsdel id = {}", id, e);
		}
		return reseResp;
	}
	
	private boolean updatedel(long classroomid, Integer id){
		int temp = classroomCoursesMapper.updateIsdel(classroomid, id);
		return temp > 0 ? true : false;
	}

}
