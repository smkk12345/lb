package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpComplaints;
import com.longbei.appservice.entity.ImpComplaintsKey;

public interface ImpComplaintsMapper {
    int deleteByPrimaryKey(ImpComplaintsKey key);

    int insert(ImpComplaints record);

    int insertSelective(ImpComplaints record);

    ImpComplaints selectByPrimaryKey(ImpComplaintsKey key);

    int updateByPrimaryKeySelective(ImpComplaints record);

    int updateByPrimaryKey(ImpComplaints record);
}