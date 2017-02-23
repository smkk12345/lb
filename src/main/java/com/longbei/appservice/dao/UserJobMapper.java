package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserJob;
import com.longbei.appservice.entity.UserSchool;

public interface UserJobMapper {
    int insertJob(UserJob data);
    int deleteJob(@Param("id") int id,@Param("userid") long userid);
    UserJob selectJobById(@Param("id")int id);
    List<UserJob> selectJobList(@Param("userid")long userid);
    int updateJob(UserJob data);
}