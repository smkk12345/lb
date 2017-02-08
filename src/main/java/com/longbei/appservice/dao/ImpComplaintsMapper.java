package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpComplaints;

public interface ImpComplaintsMapper {
    int deleteByPrimaryKey(ImpComplaints record);

    int insert(ImpComplaints record);

    int insertSelective(ImpComplaints record);

    ImpComplaints selectByPrimaryKey(ImpComplaints record);

    int updateByPrimaryKeySelective(ImpComplaints record);

    int updateByPrimaryKey(ImpComplaints record);
}