package com.longbei.appservice.controller.api;

import java.util.Date;
import java.util.List;

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
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.UserCard;
import com.longbei.appservice.service.ClassroomService;

@RestController
@RequestMapping(value = "/api/classroom")
public class ClassroomApiController {

	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private IdGenerateService idGenerateService;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomApiController.class);
	
	/**
    * @Title: http://ip:port/app_service/api/classroom/selectClassroomList
    * @Description: 获取教室列表
    * @param @param isup  0 - 未发布 。1 --已发布
	* @param @param isdel 0 未删除。1 删除
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年6月17日
	*/
	@RequestMapping(value = "selectClassroomList")
    public BaseResp<Page<Classroom>> selectClassroomList(String isup, String isdel, String startNo, String pageSize) {
		logger.info("selectClassroomList isup = {}, isdel = {}, startNo = {}, pageSize = {}",
				isup, isdel, startNo, pageSize);
		BaseResp<Page<Classroom>> baseResp = new BaseResp<>();
  		try {
  			baseResp = classroomService.selectPcClassroomList(isup, isdel, Integer.parseInt(startNo), Integer.parseInt(pageSize));
  		} catch (Exception e) {
  			logger.error("selectClassroomList isup = {}, isdel = {}, startNo = {}, pageSize = {}",
  					isup, isdel, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/api/classroom/selectUserCardList
    * @Description: 获取教室名片列表
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年6月17日
	*/
	@RequestMapping(value = "selectUserCardList")
    public BaseResp<List<UserCard>> selectUserCardList(String startNo, String pageSize) {
		logger.info("selectUserCardList startNo = {}, pageSize = {}",
				startNo, pageSize);
		BaseResp<List<UserCard>> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(pageSize)){
			pageSize = "0";
		}
  		try {
  			baseResp = classroomService.selectPcUserCardList(Integer.parseInt(startNo), Integer.parseInt(pageSize));
  		} catch (Exception e) {
  			logger.error("selectUserCardList startNo = {}, pageSize = {}",
  					startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	
	/**
     * 添加教室
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insert")
    public BaseResp<Object> insertClassroom(@RequestBody Classroom classroom){
        logger.info("insertClassroom classroom={}",JSON.toJSONString(classroom));
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
        	long classroomid = idGenerateService.getUniqueIdAsLong();
        	classroom.setClassroomid(classroomid);
        	classroom.setCreatetime(new Date());
        	classroom.setUpdatetime(new Date());
        	classroom.setClassinvoloed(0);
        	classroom.setIsdel("0");
        	classroom.setIsrecommend("0");
        	classroom.setIsup("0");
            baseResp = classroomService.insertClassroom(classroom);
        } catch (Exception e) {
            logger.error("insertClassroom is error:{}",e);
        }
        return baseResp;
    }
    
    /**
     * 搜索教室
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "search")
    public BaseResp<Page<Classroom>> search(@RequestBody Classroom classroom, String startNo, String pageSize){
        logger.info("search classroom={},startNo={},pageSize={}", JSON.toJSON(classroom).toString(),startNo,pageSize);
        BaseResp<Page<Classroom>> baseResp = new BaseResp<>();
  		try {
  			baseResp = classroomService.selectPcSearchClassroomList(classroom, Integer.parseInt(startNo), Integer.parseInt(pageSize));
        } catch (Exception e) {
        	logger.error("search classroom={},startNo={},pageSize={}", 
        			JSON.toJSON(classroom).toString(), startNo, pageSize, e);
        }
        return baseResp;
    }
	
}
