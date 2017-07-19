package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveLikes;
import org.apache.ibatis.annotations.Param;

public interface ImproveLikesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImproveLikes record);

    int insertSelective(ImproveLikes record);

    ImproveLikes selectByPrimaryKey(Integer id);

    int updateByImpidSelective(ImproveLikes record);

    int updateByPrimaryKey(ImproveLikes record);

    ImproveLikes selectByimpid(@Param("impid") String impid);
}