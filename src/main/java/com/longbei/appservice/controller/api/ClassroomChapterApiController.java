package com.longbei.appservice.controller.api;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.ClassroomChapter;
import com.longbei.appservice.service.ClassroomChapterService;

@RestController
@RequestMapping(value = "/api/classroomChapter")
public class ClassroomChapterApiController {
	
	@Autowired
	private ClassroomChapterService classroomChapterService;
	@Autowired
	private IdGenerateService idGenerateService;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomChapterApiController.class);
	
	
	/**
     * 搜索教室章节
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "search")
    public BaseResp<Page<ClassroomChapter>> search(@RequestBody ClassroomChapter classroomChapter, 
    		String startNo, String pageSize){
        logger.info("search classroomChapter={},startNo={},pageSize={}", 
        		JSON.toJSON(classroomChapter).toString(), startNo, pageSize);
        BaseResp<Page<ClassroomChapter>> baseResp = new BaseResp<>();
  		try {
  			baseResp = classroomChapterService.selectPcSearchChapterList(classroomChapter, 
  					Integer.parseInt(startNo), Integer.parseInt(pageSize));
        } catch (Exception e) {
        	logger.error("search classroomChapter={},startNo={},pageSize={}", 
        			JSON.toJSON(classroomChapter).toString(), startNo, pageSize, e);
        }
        return baseResp;
    }

	/**
     * 添加教室章节
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insert")
    public BaseResp<Object> insertClassroomChapter(@RequestBody ClassroomChapter classroomChapter){
        logger.info("insertClassroom classroomChapter = {}", JSON.toJSONString(classroomChapter).toString());
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
        	long chapterid = idGenerateService.getUniqueIdAsLong();
        	classroomChapter.setChapterid(chapterid);;
        	classroomChapter.setCreatetime(new Date());
        	classroomChapter.setUpdatetime(new Date());
        	classroomChapter.setIsdel("0");
        	classroomChapter.setSort(0);
            baseResp = classroomChapterService.insertSelective(classroomChapter);
        } catch (Exception e) {
            logger.error("insertClassroomChapter classroomChapter = {}", 
            		JSON.toJSONString(classroomChapter).toString(),e);
        }
        return baseResp;
    }
    
    /**
     * 获取教室章节信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "selectChapter")
    public BaseResp<ClassroomChapter> selectChapter(Long chapterid){
        logger.info("selectChapter chapterid = {}", chapterid);
        BaseResp<ClassroomChapter> baseResp = new BaseResp<ClassroomChapter>();
        try {
            baseResp = classroomChapterService.selectByPrimaryKey(chapterid);
        } catch (Exception e) {
            logger.error("selectChapter chapterid = {}", chapterid,e);
        }
        return baseResp;
    }
    
    /**
     * 编辑教室章节
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update")
    public BaseResp<Object> updateClassroomChapter(@RequestBody ClassroomChapter classroomChapter){
        logger.info("updateClassroomChapter classroomChapter = {}", JSON.toJSONString(classroomChapter).toString());
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = classroomChapterService.updateByPrimaryKeySelective(classroomChapter);
        } catch (Exception e) {
            logger.error("updateClassroomChapter classroomChapter = {}", 
            		JSON.toJSONString(classroomChapter).toString(),e);
        }
        return baseResp;
    }
    
    /**
     * 删除教室章节
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public BaseResp<Object> delete(Long classroomid, Long chapterid){
        logger.info("delete classroomid = {}, chapterid = {}", 
        		classroomid, chapterid);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = classroomChapterService.updateIsdel(classroomid, chapterid);
        } catch (Exception e) {
            logger.error("delete classroomid = {}, chapterid = {}", 
            		classroomid, chapterid,e);
        }
        return baseResp;
    }
    
    /**
	 * @author yinxc
	 * 修改教室章节是否显示
	 * 2017年9月6日
	 * param chapterid  章节id
	 * param classroomid 教室id
	 * param display 0 显示 1 隐藏
	 */
    @ResponseBody
    @RequestMapping(value = "updateDisplayByid")
    public BaseResp<Object> updateDisplayByid(Long classroomid, Long chapterid, String display){
        logger.info("updateDisplayByid classroomid = {}, chapterid = {}, display = {}", 
        		classroomid, chapterid, display);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = classroomChapterService.updateDisplayByid(classroomid, chapterid, display);
        } catch (Exception e) {
            logger.error("updateDisplayByid classroomid = {}, chapterid = {}, display = {}", 
            		classroomid, chapterid, display,e);
        }
        return baseResp;
    }
	
}
