package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankAward;
import com.longbei.appservice.entity.RankAwardRelease;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankAwardReleaseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankAwardRelease record);

    int insertSelective(RankAwardRelease record);

    RankAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankAwardRelease record);

    int insertBatch(List<RankAwardRelease> rankAwards);

    int updateByPrimaryKey(RankAwardRelease record);

    List<RankAwardRelease> selectListByRankid(@Param("rankid") String rankid);

    int deleteByRankid(@Param("rankid") String rankid);
}