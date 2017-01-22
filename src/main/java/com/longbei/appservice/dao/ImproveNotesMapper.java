package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveNotes;

public interface ImproveNotesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveNotes record);

    int insertSelective(ImproveNotes record);

    ImproveNotes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImproveNotes record);

    int updateByPrimaryKey(ImproveNotes record);
}