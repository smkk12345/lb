package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Award;
import com.longbei.appservice.entity.RankAward;
import com.longbei.appservice.entity.RankMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankAward record);

    int insertSelective(RankAward record);

    RankAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankAward record);

    int insertBatch(List<RankAward> rankAwards);

    int updateByPrimaryKey(RankAward record);

    List<RankAward> selectListByRankid(@Param("rankid") String rankid);

    int deleteByRankid(@Param("rankid") String rankid);

    /**
     * 根据rankId和awardId 查询rankAward
     * @param rankId 榜单id
     * @param awardId 奖品id
     * @return
     */
    RankAward selectRankAwardByRankIdAndAwardId(@Param("rankId") Long rankId,@Param("awardId") Long awardId);

}