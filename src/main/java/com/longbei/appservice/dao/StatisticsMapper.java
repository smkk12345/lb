package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Statistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Statistics record);

    int insertSelective(Statistics record);

    Statistics selectByPrimaryKey(Integer id);

    List<Statistics> listByStartDate(Statistics record);

    int updateByPrimaryKeySelective(Statistics record);

    int updateByPrimaryKey(Statistics record);

    int sumByField(@Param("field") String field);

    int selectListCount();

    List<Statistics> selectListWithPage(@Param("startno") Integer startno,
                                        @Param("pagesize") Integer pagesize);


}