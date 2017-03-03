package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.Classroom;

public interface ClassroomMapper {
    int deleteByPrimaryKey(@Param("classroomid") long classroomid);

    int insert(Classroom record);

    int insertSelective(Classroom record);

    Classroom selectByPrimaryKey(@Param("classroomid") long classroomid);

    int updateByPrimaryKeySelective(Classroom record);

    int updateByPrimaryKey(Classroom record);
    
    /**
	 * @author yinxc
	 * 获取教室信息
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月27日
	 */
    List<Classroom> selectListByUserid(@Param("userid") long userid, @Param("ptype") String ptype, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 根据ptype获取教室信息(通用方法)
	 * param ptype:十全十美类型
	 * param keyword:关键字搜索    可为null
	 * param pageNo   pageSize
	 * 2017年2月27日
	 */
    List<Classroom> selectListByPtype(@Param("ptype") String ptype, @Param("keyword") String keyword, 
    		@Param("startNum") int startNum, @Param("endNum") int endNum);
    
    /**
	 * @author yinxc
	 * 修改教室公告---classnotice
	 * 2017年3月2日
	 */
    int updateClassnoticeByClassroomid(@Param("classroomid") long classroomid, 
    		@Param("userid") long userid, @Param("classnotice") String classnotice); 
    
}