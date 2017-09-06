package com.longbei.appservice.service;

import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
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
	
	/**
	 * @author yinxc
	 * 修改教室章节是否显示
	 * 2017年9月6日
	 * param chapterid  章节id
	 * param classroomid 教室id
	 * param display 0 显示 1 隐藏
	 */
	BaseResp<Object> updateDisplayByid(long classroomid, long chapterid, String display);
	
	
	
	
	
	//-----------------------admin调用--------------------------
	
	
	/**
    * @Description: 获取教室章节列表
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年9月6日
	*/
	BaseResp<Page<ClassroomChapter>> selectPcSearchChapterList(ClassroomChapter classroomChapter, 
			int startNo, int pageSize);
		

}
