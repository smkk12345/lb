package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveClassroom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveClassroomMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveClassroom record);

    int insertSelective(Improve record);

    ImproveClassroom selectByPrimaryKey(Long impid);

    int updateByPrimaryKeySelective(ImproveClassroom record);

    int updateByPrimaryKey(ImproveClassroom record);


    /**
     * 根据classroomid查询进步列表
     * @param classroomid 教室id
     * @param ismainimp  最新进步 0 普通微进步  1 最新微进步
     * @return
     */
    List<ImproveClassroom> selectByClassroomId(String businessid, String ismainimp);
    
    /**
	 * @author yinxc
	 * 获取教室微进步批复作业列表
	 * 2017年3月6日
	 * @param businessid 教室id
	 * @param impid 微进步id
	 */
    List<ImproveClassroom> selectListByBusinessid(@Param("businessid") long businessid, @Param("impid") long impid);
    
    /**
	 * @author yinxc
	 * 根据classroomid，userid获取用户在教室里上传作业的总数
	 * 2017年3月3日
	 * @param classroomid 教室id
	 * @param userid 用户id
	 */
    Integer selectCountByClassroomidAndUserid(@Param("businessid") long businessid, @Param("userid") long userid);


    /**
     * 假删
     * @param userid 用户id
     * @param classroomid 教室ID
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,
               @Param("businessid") String businessid,
               @Param("improveid") String improveid);
}