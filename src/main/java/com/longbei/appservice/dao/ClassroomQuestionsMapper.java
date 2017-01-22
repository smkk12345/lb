package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ClassroomQuestions;

public interface ClassroomQuestionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomQuestions record);

    int insertSelective(ClassroomQuestions record);
}