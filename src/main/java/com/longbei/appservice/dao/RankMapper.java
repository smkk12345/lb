package com.longbei.appservice.dao;
import com.longbei.appservice.entity.Award;
import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RankMapper {
    int deleteByPrimaryKey(Integer rankid);

    int insert(Rank record);

    int insertSelective(Rank record);

    Rank selectByPrimaryKey(Long rankid);

    int updateByPrimaryKeySelective(Rank record);

    int updateByPrimaryKey(Rank record);


    int updateSponsornumAndSponsormoney(@Param("rankid")Long rankid);

    Rank selectRankByRankid(@Param("rankid")Long rankid);

    List<Rank> selectListWithPage(@Param("rank") Rank rank,
                                       @Param("startno")Integer startno,
                                       @Param("pagesize")Integer pagesize);

    int selectListCount(Rank rank);

    int updateSponsornumAndSponsormoney( );

    int updateSymbolByRankId(Rank rank);

    /**
     * 更新榜单中的用户数量
     * @param rankId 榜单id
     * @param count 新增的榜单人数
     * @return
     */
    int updateRankMemberCount(@Param("rankId") Long rankId,@Param("count") int count);

    /**
     * 查询榜单列表
     * @param parameterMap
     * @return
     */
    List<Rank> selectRankList(Map<String, Object> parameterMap);

    /**
     * 查询五分钟前刚刚开始的榜单
     * @param map
     * @return
     */
    List<Rank> selectStartRank(Map<String, Object> map);

    /**
     * 将已开始的榜单置为已开始
     * @param map
     * @return
     */
    int handleStartRank(Map<String, Object> map);
}