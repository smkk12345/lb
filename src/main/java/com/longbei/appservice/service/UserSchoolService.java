package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserSchool;

public interface UserSchoolService {
	BaseResp<Object> insertSchool(long userid,String schoolname,String Department,Date starttime,Date endtime);
	BaseResp<Object> deleteSchool(int id,long userid);
	BaseResp<Object> selectSchoolById(int id);
	BaseResp<Object> selectSchoolList(long userid);
	BaseResp<Object> updateSchool(int id,String schoolname,String Department,Date starttime,Date endtime);
}
