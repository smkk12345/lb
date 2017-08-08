package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Seminar;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SeminarMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBySeminarid(@Param("seminarid") String seminarid);

    int insert(Seminar record);

    int insertSelective(Seminar record);

    Seminar selectByPrimaryKey(Integer id);

    Seminar selectBySeminarId(Long seminarid);

    List<Seminar> selectList(@Param("seminar") Seminar seminar,
                             @Param("startNo") Integer startNo,
                             @Param("pageSize") Integer pageSize);

    int selectCount(@Param("seminar") Seminar seminar);

    int updateByPrimaryKeySelective(Seminar record);

    int updateByPrimaryKey(Seminar record);

    int updatePageViewBySeminarid(@Param("seminarid") String seminarid);
}