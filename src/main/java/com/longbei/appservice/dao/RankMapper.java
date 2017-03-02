package com.longbei.appservice.dao;

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

    List<Rank> selectListWithPage(@Param("rank") Rank rank,
                                       @Param("startno")int startno,
                                       @Param("pagesize")int pagesize);

    int selectListCount(Rank rank);

    int updateSponsornumAndSponsormoney( );
}