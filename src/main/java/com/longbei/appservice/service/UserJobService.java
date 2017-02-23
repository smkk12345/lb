package com.longbei.appservice.service;

import java.util.Date;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserJob;

public interface UserJobService {
	BaseResp<Object> insertJob(long userid,String companyname,String department,String location,Date starttime,Date endtime);
	BaseResp<Object> deleteJob(int id,long userid);
	BaseResp<Object> selectJobById(int id);
	BaseResp<Object> selectJobList(long userid);
	BaseResp<Object> updateJob(int id,String companyname,String department,String location,Date starttime,Date endtime);
}
