package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveRank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveRankMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveRank record);

    int insertSelective(ImproveRank record);

    ImproveRank selectByPrimaryKey(Long impid);

    int updateByPrimaryKeySelective(ImproveRank record);

    int updateByPrimaryKey(ImproveRank record);


    /**
     * 根据rankid查询进步列表
     * @param rankid 榜单id
     * @param ismainimp  最新进步 0 普通微进步  1 最新微进步
     * @return
     */
    List<ImproveRank> selectByRankId(String rankid,String ismainimp);



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