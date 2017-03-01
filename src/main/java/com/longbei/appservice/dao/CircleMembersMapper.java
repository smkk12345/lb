package com.longbei.appservice.dao;

import com.longbei.appservice.entity.CircleMembers;

import java.util.List;
import java.util.Map;

public interface CircleMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleMembers record);

    int insertSelective(CircleMembers record);

    CircleMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleMembers record);

    int updateByPrimaryKey(CircleMembers record);

    List<CircleMembers> selectCircleMember(Map<String, Object> map);

    CircleMembers findCircleMember(Map<String, Object> map);

    int updateCircleMembers(Map<String, Object> map);

}