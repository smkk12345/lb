package com.longbei.appservice.dao;

import java.util.Date;
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
    
    /**
	 * @author yinxc
	 * 修改教室章节是否显示
	 * 2017年9月6日
	 * param chapterid  章节id
	 * param classroomid 教室id
	 * param display 0 显示 1 隐藏
	 */
    Integer updateDisplayByid(@Param("classroomid") long classroomid, 
    		@Param("chapterid") long chapterid, @Param("display") String display);
    
    
    
    
    
    //------------------------admin调用------------------------------
    
    /**
     * @Description: 获取教室章节列表---pc
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年9月6日
 	*/
    List<ClassroomChapter> selectPcSearchChapterList(@Param("classroomChapter") ClassroomChapter classroomChapter, 
    		@Param("startNo") Integer startNo, @Param("pageSize") Integer pageSize);
    
    /*
     * 获取Count
     */
    Integer selectSearchCount(@Param("classroomChapter") ClassroomChapter classroomChapter);

	Integer selectChapterCountByTime(@Param("classroomid") long classroomid,
									 @Param("createtime")Date createtime);
    
}