package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.RankImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    int updateSymbolByRankId(RankImage rankImage);

    int selectListCount(RankImage rankImage);

    int selectPCListCount(RankImage rankImage);

    /**
     * 查询需要发布的榜单
     * @param map
     * @return
     */
    List<RankImage> selectPublishRank(Map<String, Object> map);
}