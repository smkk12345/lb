package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankImageMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByRankImageId(@Param("rankimageid") String rankimageid);

    int insert(RankImage record);

    int insertSelective(RankImage record);

    Rank selectByPrimaryKey(Integer id);

    RankImage selectByRankImageId(@Param("rankimageid") String rankimageid);

    int updateByPrimaryKeySelective(RankImage record);

    int updateByPrimaryKey(RankImage record);

    List<RankImage> selectListWithPage(@Param("rankimage") RankImage rankImage,
                                       @Param("startno")int startno,
                                       @Param("pagesize")int pagesize);
}