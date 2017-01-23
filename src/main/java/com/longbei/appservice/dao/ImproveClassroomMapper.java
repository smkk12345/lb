package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveClassroom;
import com.longbei.appservice.entity.ImproveRank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveClassroomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveClassroom record);

    int insertSelective(ImproveClassroom record);

    ImproveClassroom selectByPrimaryKey(Long impid);

    int updateByPrimaryKeySelective(ImproveClassroom record);

    int updateByPrimaryKey(ImproveClassroom record);


    /**
     * 根据classroomid查询进步列表
     * @param classroomid 教室id
     * @param ismainimp  最新进步 0 普通微进步  1 最新微进步
     * @return
     */
    List<ImproveClassroom> selectByClassroomId(String classroomid, String ismainimp);



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