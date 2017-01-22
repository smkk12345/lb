package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserFeedback;

public interface UserFeedbackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFeedback record);

    int insertSelective(UserFeedback record);

    UserFeedback selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFeedback record);

    int updateByPrimaryKey(UserFeedback record);
}