package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.DictArea;

public interface DictAreaMapper {

    List<DictArea> selectCityList(@Param("pid") String pid,@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

    /**
     * 根据id 查询 地区
     * @param areaIdList
     * @return
     */
    List<DictArea> findAreaListById(@Param("areaIdList") List<Long> areaIdList);
}