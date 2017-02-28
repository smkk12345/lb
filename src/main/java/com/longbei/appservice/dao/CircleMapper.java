package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Circle;

import java.util.List;
import java.util.Map;

public interface CircleMapper {
    int deleteByPrimaryKey(long circleid);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(long circleid);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);

    List<Circle> findRelevantCircle(Map<String,Object> map);

    Circle findCircleByCircleTitle(String circleTitle);
}