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
            baseResp = classroomChapterService.insertSelective(classroomChapter);
        } catch (Exception e) {
            logger.error("insertClassroomChapter classroomChapter = {}", 
            		JSON.toJSONString(classroomChapter).toString(),e);
        }
        return baseResp;
    }
	
}
