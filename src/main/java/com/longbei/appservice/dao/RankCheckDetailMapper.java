package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankCheckDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankCheckDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankCheckDetail record);

    int insertSelective(RankCheckDetail record);

    RankCheckDetail selectByPrimaryKey(Integer id);

    List<RankCheckDetail> selectList(@Param("rankid") String rankid);

    int updateByPrimaryKeySelective(RankCheckDetail record);

    int updateByPrimaryKey(RankCheckDetail record);
}