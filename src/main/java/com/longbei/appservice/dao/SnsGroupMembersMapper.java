package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsGroupMembers;

import java.util.Map;

public interface SnsGroupMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsGroupMembers record);

    int insertSelective(SnsGroupMembers record);

    SnsGroupMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsGroupMembers record);

    int updateByPrimaryKey(SnsGroupMembers record);

    /**
     * 批量插入群组成员
     * @param map
     * @return
     */
    int batchInsertGroupMembers(Map<String, Object> map);
}