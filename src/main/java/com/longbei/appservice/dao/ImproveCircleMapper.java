package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveCircle;
import com.longbei.appservice.entity.ImproveRank;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ImproveCircleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveCircle record);

    int insertSelective(ImproveCircle record);

    ImproveCircle selectByPrimaryKey(Long impid);

    int updateByPrimaryKeySelective(ImproveCircle record);

    int updateByPrimaryKey(ImproveCircle record);


    /**
     * 根据circleid查询进步列表
     * @param circleid 圈子id
     * @param ismainimp  最新进步 0 普通微进步  1 最新微进步
     * @return
     */
    List<ImproveCircle> selectByCircleId(String circleid, String ismainimp);

    /**
     * 假删
     * @param userid 用户id
     * @param circleid 兴趣圈ID
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,
               @Param("circleid") String circleid,
               @Param("improveid") String improveid);

    /**
     * 更改圈子中用户的进步状态
     * @param userId
     * @param circleId
     * @param isDel 1 代表删除
     * @param isBusinessDel 1 代表删除
     * @return
     */
    int updateImproveCircleStatus(@Param("userId")String userId,@Param("circleId") String circleId,
                                  @Param("isDel")String isDel,@Param("isBusinessDel") String isBusinessDel);
}