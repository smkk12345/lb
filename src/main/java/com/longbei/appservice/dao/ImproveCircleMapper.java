package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveCircle;
import org.apache.ibatis.annotations.Param;

public interface ImproveCircleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveCircle record);

    int insertSelective(ImproveCircle record);

    ImproveCircle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImproveCircle record);

    int updateByPrimaryKey(ImproveCircle record);

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
}