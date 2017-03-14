package com.longbei.appservice.dao;
import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
                                       @Param("startno")int startno,
                                       @Param("pagesize")int pagesize);

    int selectListCount(Rank rank);

    int updateSponsornumAndSponsormoney( );

    int updateSymbolByRankId(Rank rank);

    /**
     * 更新榜单中的用户数量
     * @param rankId
     * @param count
     * @return
     */
    int updateRankMemberCount(Long rankId, int count);
}