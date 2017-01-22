package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveRank;
import org.apache.ibatis.annotations.Param;

public interface ImproveRankMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveRank record);

    int insertSelective(ImproveRank record);

    ImproveRank selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImproveRank record);

    int updateByPrimaryKey(ImproveRank record);

    /**
     * 假删
     * @param userid 用户id
     * @param rankid 榜单ID
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,
               @Param("rankid") String rankid,
               @Param("improveid") String improveid);
}