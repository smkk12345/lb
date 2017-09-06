package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.ClassroomChapter;

public interface ClassroomChapterService {
	
	BaseResp<ClassroomChapter> selectByPrimaryKey(long chapterid);
	
	BaseResp<Object> updateByPrimaryKeySelective(ClassroomChapter record);
	
	BaseResp<Object> insertSelective(ClassroomChapter record);
	
	/**
	 * @author yinxc
	 * 获取教室章节信息
	 * @param classroomid 
	 * @param startNo   pageSize
	 * 2017年9月5日
	 */
	BaseResp<List<ClassroomChapter>> selectChapterByCid(long classroomid, Integer startNo, Integer pageSize);
	
	/**
	 * @author yinxc
	 * 删除教室章节信息(假删)
	 * 2017年9月5日
	 * @param classroomid
	 * @param chapterid
	 */
	BaseResp<Object> updateIsdel(long classroomid, long chapterid);
	
	/**
	 * @author yinxc
	 * 修改教室章节排序
	 * 2017年9月5日
	 * param chapterid  章节id
	 * param classroomid 教室id
	 * param sort 章节序号
	 */
	BaseResp<Object> updateSortByid(long classroomid, long chapterid, Integer sort);

}
