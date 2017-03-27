package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.AwardClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AwardClassifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AwardClassify record);

    int insertSelective(AwardClassify record);

    AwardClassify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AwardClassify record);

    int updateByPrimaryKey(AwardClassify record);

    List<AwardClassify> selectAwardClassifyList(@Param("startno") Integer startno, @Param("pagesize") Integer pageszie);

    int selectAwardClassifyCount();
}