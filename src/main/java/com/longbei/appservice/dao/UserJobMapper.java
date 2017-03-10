package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserJob;

public interface UserJobMapper {
    int insertJob(UserJob data);
    int deleteJob(@Param("id") int id,@Param("userid") long userid);
    UserJob selectJobById(@Param("id")int id);
    List<UserJob> selectJobList(@Param("userid")long userid,@Param("startNum") int startNum,@Param("pageSize")int pageSize);
    int updateJob(UserJob data);
    
    /**
	 * @author yinxc
	 * 获取用户工作经历Count
	 * 2017年3月9日
	 * return_type
	 * UserJobMapper
	 */
    int selectCountJob(@Param("userid")long userid);
    
}