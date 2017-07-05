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
import com.longbei.appservice.common.constant.Constant;
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
    
    /**
     * @Description: 获取教室信息
     * @param @param classroomid 教室id
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    @ResponseBody
    @RequestMapping(value = "selectClassroomBycid")
    public BaseResp<Classroom> selectClassroomBycid(String classroomid){
        logger.info("selectClassroomBycid classroomid = {}", classroomid);
        BaseResp<Classroom> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(classroomid)){
        	return baseResp;
        }
  		try {
  			Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
  			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
  			baseResp.setData(classroom);
        } catch (Exception e) {
        	logger.error("selectClassroomBycid classroomid = {}", classroomid, e);
        }
        return baseResp;
    }
    
    /**
     * @Description: 教室发公告
     * @param @param classroomid 教室id
     * @param @param classnotice 公告
     * @param @param noticetype 是否@所有人   0：否  1：是
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    @RequestMapping(value = "/notice")
 	@ResponseBody
    public BaseResp<Object> notice(String classroomid, String classnotice, String noticetype){
 		logger.info("notice classroomid = {}, classnotice = {}, noticetype = {}", 
    			classroomid, classnotice, noticetype);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(classroomid)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.updateClassnoticeByClassroomid(Long.parseLong(classroomid), 
  					Long.parseLong(Constant.SQUARE_USER_ID), classnotice, noticetype);
        } catch (Exception e) {
        	logger.error("notice classroomid = {}, classnotice = {}, noticetype = {}", 
    			classroomid, classnotice, noticetype, e);
        }
        return baseResp;
 	}
    
    /**
     * @Description: 关闭教室
     * @param @param classroomid 教室id
     * @param @param closeremark 关闭原因
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    @RequestMapping(value = "/closeRoom")
 	@ResponseBody
    public BaseResp<Object> closeRoom(String classroomid, String closeremark){
 		logger.info("closeRoom classroomid = {}, closeremark = {}", 
    			classroomid, closeremark);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(classroomid)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.closeRoom(Long.parseLong(classroomid), closeremark);
        } catch (Exception e) {
        	logger.error("closeRoom classroomid = {}, closeremark = {}", 
    			classroomid, closeremark, e);
        }
        return baseResp;
 	}
    
    
    /**
     * @Description: 发布教室
     * @param @param classroomid 教室id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    @RequestMapping(value = "/uproom")
 	@ResponseBody
    public BaseResp<Object> uproom(String classroomid){
 		logger.info("uproom classroomid = {}", classroomid);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(classroomid)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.uproom(Long.parseLong(classroomid));
        } catch (Exception e) {
        	logger.error("uproom classroomid = {}", classroomid, e);
        }
        return baseResp;
 	}
	
}
