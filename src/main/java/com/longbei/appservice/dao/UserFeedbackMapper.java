package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserFeedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFeedbackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFeedback record);

    int insertSelective(UserFeedback record);

    int updateByPrimaryKey(UserFeedback record);


    UserFeedback selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFeedback record);

    int selectFeedbackCount(@Param("userFeedback") UserFeedback userFeedback);

//    int selectHasFeedbackCount(@Param("userFeedback") UserFeedback userFeedback);

    List<UserFeedback> selectFeedbackList(@Param("userFeedback") UserFeedback userFeedback, @Param("startno") Integer startno, @Param("pagesize") Integer pageszie);

//    List<UserFeedback> selectHasFeedbackList(@Param("userFeedback") UserFeedback userFeedback, @Param("startno") Integer startno, @Param("pagesize") Integer pageszie);

}