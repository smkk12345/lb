package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpComplaints;

public interface ImpComplaintsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImpComplaints record);

    int insertSelective(ImpComplaints record);

    ImpComplaints selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImpComplaints record);

    int updateByPrimaryKey(ImpComplaints record);
}