package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.ClassroomChapter;

public interface ClassroomChapterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassroomChapter record);

    int insertSelective(ClassroomChapter record);

    ClassroomChapter selectByPrimaryKey(@Param("chapterid") long chapterid);

    int updateByPrimaryKeySelective(ClassroomChapter record);

    int updateByPrimaryKey(ClassroomChapter record);
    
    /**
	 * @author yinxc
	 * 获取教室章节信息
	 * @param classroomid 
	 * @param startNo   pageSize
	 * 2017年9月5日
	 */
    List<ClassroomChapter> selectChapterByCid(@Param("classroomid") long classroomid, 
    		@Param("startNo") Integer startNo, @Param("pageSize") Integer pageSize);
    
    /**
	 * @author yinxc
	 * 删除教室章节信息(假删)
	 * 2017年9月5日
	 * @param classroomid
	 * @param chapterid
	 */
    Integer updateIsdel(@Param("classroomid") long classroomid, @Param("chapterid") long chapterid);
    
    /**
	 * @author yinxc
	 * 修改教室章节排序
	 * 2017年9月5日
	 * param chapterid  章节id
	 * param classroomid 教室id
	 * param sort 章节序号
	 */
    Integer updateSortByid(@Param("classroomid") long classroomid, 
    		@Param("chapterid") long chapterid, @Param("sort") Integer sort);
    
}