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
	 * 获取我加入的教室成员列表---(剔除   退出教室的)
	 * 2017年2月28日
	 * param classroomid 教室id
	 */
    List<ClassroomMembers> selectInsertByUserid(@Param("userid") long userid, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 获取教室成员信息
	 * 2017年2月28日
	 * param classroomid 教室id
	 * param userid 成员id
	 * param itype 0—加入教室 1—退出教室     为null查全部
	 */
    ClassroomMembers selectListByClassroomidAndUserid(@Param("classroomid") long classroomid, 
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
    
}