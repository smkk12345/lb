package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.ClassroomMembers;

public interface ClassroomMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomMembers record);

    int insertSelective(ClassroomMembers record);

    ClassroomMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClassroomMembers record);

    int updateByPrimaryKey(ClassroomMembers record);
    
    /**
	 * @author yinxc
	 * 获取教室成员列表---(剔除   退出教室的)
	 * 2017年2月28日
	 * param classroomid 教室id
	 */
    List<ClassroomMembers> selectListByClassroomid(@Param("classroomid") long classroomid, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取教室成员列表---(剔除   退出教室的)
	 * 2017年7月7日
	 * param classroom
	 */
    List<ClassroomMembers> selectSearchList(@Param("classroomMembers") ClassroomMembers classroomMembers, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /*
     * 获取Count
     */
    Integer selectSearchCount(@Param("classroomMembers") ClassroomMembers classroomMembers);
    
    /**
	 * @author yinxc
	 * 获取我加入的教室成员列表---(剔除   退出教室的)
	 * 2017年2月28日
	 * param classroomid 教室id
	 */
    List<ClassroomMembers> selectInsertByUserid(@Param("userid") long userid, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
     * 获取教室成员id集合
     */
    List<String> selectMidByCid(@Param("classroomid") long classroomid);
    
    /**
	 * @author yinxc
	 * 获取教室成员信息
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室     为null查全部
	 */
    ClassroomMembers selectByClassroomidAndUserid(@Param("classroomid") long classroomid, 
    		@Param("userid") long userid, @Param("itype") String itype);
    
    /**
	 * @author yinxc
	 * 教室剔除成员
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室 
	 */
    int updateItypeByClassroomidAndUserid(@Param("classroomid") long classroomid, 
    		@Param("userid") long userid, @Param("itype") String itype);
    
    /*
     * 递增教室成员发进步总数
     */
    int updateIcountByCidAndUid(@Param("classroomid") long classroomid, 
    		@Param("userid") long userid, @Param("icount") int icount);
    
    /*
     * 递增教室成员投诉总数
     */
    int updateCompcountByCidAndUid(@Param("classroomid") long classroomid, 
    		@Param("userid") long userid, @Param("complaintotalcount") int complaintotalcount);
    
    /*
     * 递增教室成员总赞，总花
     */
    int updateLFByCidAndUid(@Param("classroomid") long classroomid, 
    		@Param("userid") long userid, @Param("likes") int likes, @Param("flowers") int flowers);
    
    /**
	 * @author yinxc
	 * 获取当前教室成员总数
	 * 2017年3月6日
	 * param classroomid 教室id
	 */
    int selectCountMembers(@Param("classroomid") long classroomid);
    
}