package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Circle;
import com.longbei.appservice.entity.CircleMembers;
import com.longbei.appservice.entity.ImproveCircle;
import org.apache.ibatis.annotations.Param;

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

    int updateCircleInfo(Map<String, Object> map);

    int updateCircleInvoloed(Map<String, Object> map);

    int test(@Param("list") List<ImproveCircle> list);
}