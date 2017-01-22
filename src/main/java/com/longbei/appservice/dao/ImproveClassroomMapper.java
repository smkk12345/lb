package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveClassroom;
import org.apache.ibatis.annotations.Param;

public interface ImproveClassroomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveClassroom record);

    int insertSelective(ImproveClassroom record);

    ImproveClassroom selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImproveClassroom record);

    int updateByPrimaryKey(ImproveClassroom record);

    /**
     * 假删
     * @param userid 用户id
     * @param classroomid 教室ID
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,
               @Param("classroomid") String classroomid,
               @Param("improveid") String improveid);
}