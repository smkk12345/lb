package com.longbei.appservice.controller.api;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import com.longbei.appservice.common.web.JsonDateValueProcessor;
import com.longbei.appservice.common.web.JsonLongValueProcessor;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/classroom")
public class ClassroomApiController {

	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private IdGenerateService idGenerateService;
	@Autowired
	private ClassroomMembersService classroomMembersService;
	@Autowired
	private ClassroomCoursesService classroomCoursesService;
	@Autowired
	private ImproveService improveService;
	@Autowired
	private ClassroomQuestionsMongoService classroomQuestionsMongoService;
	@Autowired
	private ClassroomChapterService classroomChapterService;
	
//	@Autowired
//	private UserCardService userCardService;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomApiController.class);
	
	
	//----------------------教室成员相关---------------------
	
	/**
    * @Description: 获取教室成员列表
    * @param @param classroomid
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
 	@RequestMapping(value = "membersList")
    public BaseResp<Page<ClassroomMembers>> selectRoomMembers(@RequestBody ClassroomMembers classroomMembers, int startNo, int pageSize) {
		logger.info("selectRoomMembers classroomid={},startNo={},pageSize={}",
				classroomMembers.getClassroomid(),startNo,pageSize);
		BaseResp<Page<ClassroomMembers>> baseResp = new BaseResp<>();
  		try {
  			baseResp = classroomMembersService.selectPcMembersList(classroomMembers, startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("selectRoomMembers classroomid = {}, startNo = {}, pageSize = {}",
  					classroomMembers.getClassroomid(), startNo, pageSize, e);
  		}
  		return baseResp;
    }
 	
 	/**
	* @Description: 教室老师剔除成员---推送消息
	* @param @param classroomid
	* @param @param userid
	* @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
	* @auther yinxc
	* @currentdate:2017年7月7日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "quitClassroom")
	public BaseResp<Object> quitClassroom(String classroomid, String userid) {
		logger.info("quitClassroom classroomid={},userid={}",classroomid,userid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(classroomid, userid)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomMembersService.quitClassroomByPC(Long.parseLong(classroomid),
					Long.parseLong(userid), "1");
		} catch (Exception e) {
			logger.error("quitClassroom classroomid = {}, userid = {}",
					classroomid, userid, e);
		}
		return baseResp;
	}
	
	/**
     * 获取用户在教室中所发的进步列表
     * @param classroomid  教室id
     * @param userid 用户id
     * @param startNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "roomMemberImproves/{startNo}/{pageSize}")
    public BaseResp<Page<Improve>> roomMemberImproves(String classroomid,String userid,String iscomplain,
                                                              @PathVariable("startNo") Integer startNo,
                                                              @PathVariable("pageSize") Integer pageSize){
        logger.info("roomMemberImproves classroomid = {}, userid = {}, iscomplain = {}, startNo = {}, pageSize = {}", 
        		classroomid, userid, iscomplain, startNo, pageSize);
        BaseResp<Page<Improve>> baseResp = new BaseResp<>();

        if (StringUtils.hasBlankParams(classroomid, userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if (StringUtils.isEmpty(startNo.toString())){
        	startNo = 0;
        }
        Page<Improve> page = new Page<>(startNo/pageSize+1,pageSize);
        if (StringUtils.isEmpty(pageSize.toString())){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }
        try {
            BaseResp<List<Improve>> listBaseResp = improveService.selectBusinessImproveList(userid, classroomid, iscomplain,
                    Constant.IMPROVE_CLASSROOM_TYPE, startNo, pageSize, true);
            Integer totalcount = Integer.parseInt(listBaseResp.getExpandData().get("totalcount")+"");
			Page.setPageNo(startNo/pageSize+1,totalcount,pageSize);
			page.setTotalCount(totalcount);
            page.setList(listBaseResp.getData());
            baseResp = BaseResp.ok();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select rank improve list by user userid={} is error:",userid,e);
        }
        return baseResp;
    }
    
    /**
     * @Description: 获取教室成员信息
     * @param @param classroomid 教室id
     * @param @param userid 
     * @auther yinxc
     * @currentdate:2017年7月10日
 	*/
    @ResponseBody
    @RequestMapping(value = "selectMembersDetail")
    public BaseResp<ClassroomMembers> selectMembersDetail(String classroomid, String userid){
        logger.info("selectMembersDetail classroomid = {}, userid = {}", classroomid, userid);
        BaseResp<ClassroomMembers> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(classroomid, userid)){
        	return baseResp;
        }
  		try {
  			ClassroomMembers classroom = classroomMembersService.selectListByClassroomidAndUserid(Long.parseLong(classroomid), 
  					Long.parseLong(userid), "0");
  			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
  			baseResp.setData(classroom);
        } catch (Exception e) {
        	logger.error("selectMembersDetail classroomid = {}, userid = {}", classroomid, userid, e);
        }
        return baseResp;
    }
    
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "updateStatus/{status}/{userid}/{classroomid}/{improveid}")
    public BaseResp<Object> updateStatus(@PathVariable("status")String status,
                                         @PathVariable("userid")String userid,
                                         @PathVariable("classroomid")String classroomid,
                                         @PathVariable("improveid")String improveid){
        logger.info("updateStatus status = {}, userid = {}, classroomid = {}, improveid = {} :", 
        		status, userid, classroomid, improveid);
        BaseResp<Object> baseResp = new BaseResp<>();

        if (StringUtils.hasBlankParams(status,userid,classroomid,improveid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = classroomMembersService.updateStatus(status, userid, classroomid, improveid);
        } catch (Exception e) {
            logger.error("remove classroom improve improveid={} userid={}, classroomid = {}", 
            		improveid, userid, classroomid, e);
        }
        return baseResp;

    }
	
	//----------------------教室相关---------------------
	
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
    * @Title: http://ip:port/app_service/api/classroom/checkClasstitle
    * @Description: 检查教室标题是否重复
    * @param @param classtitle  教室标题
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @return 0:不存在重复   1：存在重复需要更改
    * @auther yinxc
    * @currentdate:2017年7月11日
	*/
	@RequestMapping(value = "checkClasstitle")
    public BaseResp<Object> checkClasstitle(String classtitle) {
		logger.info("checkClasstitle classtitle = {}", classtitle);
		BaseResp<Object> baseResp = new BaseResp<>();
  		try {
  			baseResp = classroomService.checkClasstitle(classtitle);
  		} catch (Exception e) {
  			logger.error("checkClasstitle classtitle = {}", classtitle, e);
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
     * @Description: 修改教室
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年6月17日
 	*/
	@ResponseBody
    @RequestMapping(value = "updroom")
    public BaseResp<Object> updroom(@RequestBody Classroom classroom){
        logger.info("updroom classroom = {}", JSON.toJSONString(classroom));
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            baseResp = classroomService.updateByClassroomid(classroom);
        } catch (Exception e) {
            logger.error("updroom classroom = {}", JSON.toJSONString(classroom), e);
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
        	Date date = new Date();
        	classroom.setCreatetime(date);
        	classroom.setUpdatetime(date);
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
  			Classroom classroom = classroomService.selectByCid(Long.parseLong(classroomid));
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
    @RequestMapping(value = "/insertNotice")
 	@ResponseBody
    public BaseResp<Object> insertNotice(String classroomid, String classnotice, String noticetype){
 		logger.info("notice classroomid = {}, classnotice = {}, noticetype = {}", 
    			classroomid, classnotice, noticetype);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(classroomid)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.updateClassnoticeByPCClassroomid(Long.parseLong(classroomid), 
  					Long.parseLong(Constant.SQUARE_USER_ID), classnotice, noticetype);
        } catch (Exception e) {
        	logger.error("insertNotice classroomid = {}, classnotice = {}, noticetype = {}", 
    			classroomid, classnotice, noticetype, e);
        }
        return baseResp;
 	}
    
    /**
     * 更新教室推荐状态
     * @param classroomid
     * @param isrecommend
     * @return
     */
    @RequestMapping(value = "/updateRoomRecommend")
 	@ResponseBody
    public BaseResp<Object> updateRoomRecommend(String classroomid, String isrecommend){
 		logger.info("updateRoomRecommend classroomid = {}, isrecommend = {}", 
    			classroomid, isrecommend);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(classroomid, isrecommend)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.updateRoomRecommend(Long.parseLong(classroomid), isrecommend);
        } catch (Exception e) {
        	logger.error("updateRoomRecommend classroomid = {}, isrecommend = {}", 
    			classroomid, isrecommend, e);
        }
        return baseResp;
 	}
    
    /**
     * 更新教室推荐权重
     * @param classroomid
     * @param weight
     * @return
     */
    @RequestMapping(value = "/updateRoomRecommendSort")
 	@ResponseBody
    public BaseResp<Object> updateRoomRecommendSort(String classroomid, String weight){
 		logger.info("updateRoomRecommendSort classroomid = {}, weight = {}", 
    			classroomid, weight);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(classroomid, weight)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.updateRoomRecommendSort(Long.parseLong(classroomid), weight);
        } catch (Exception e) {
        	logger.error("updateRoomRecommendSort classroomid = {}, weight = {}", 
    			classroomid, weight, e);
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
     * @Description: 删除教室
     * @param @param classroomid 教室id
     * @param @param closeremark 关闭原因
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月5日
 	*/
    @RequestMapping(value = "/delRoom")
 	@ResponseBody
    public BaseResp<Object> delRoom(String classroomid){
 		logger.info("closeRoom classroomid = {}", classroomid);
 		BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.isBlank(classroomid)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomService.delRoom(Long.parseLong(classroomid));
        } catch (Exception e) {
        	logger.error("delRoom classroomid = {}", classroomid, e);
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
    
    
    
    
    /**
     * @Description: 获取课程列表
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年6月17日
 	*/
    @ResponseBody
    @RequestMapping(value = "searchCourses")
    public BaseResp<Page<ClassroomCourses>> searchCourses(@RequestBody ClassroomCourses classroomCourses, String startNo, String pageSize){
        logger.info("search classroomCourses={},startNo={},pageSize={}", JSON.toJSON(classroomCourses).toString(),startNo,pageSize);
        BaseResp<Page<ClassroomCourses>> baseResp = new BaseResp<>();
  		try {
  			baseResp = classroomCoursesService.selectPcSearchCroomCoursesList(classroomCourses, Integer.parseInt(startNo), Integer.parseInt(pageSize));
        } catch (Exception e) {
        	logger.error("search classroomCourses={},startNo={},pageSize={}", 
        			JSON.toJSON(classroomCourses).toString(), startNo, pageSize, e);
        }
        return baseResp;
    }
    
    /**
     * @Description: 修改课程序号
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年6月17日
 	*/
    @ResponseBody
    @RequestMapping(value = "updCoursesSort")
    public BaseResp<Object> updCoursesSort(String classroomid, String id, String coursesort){
        logger.info("updCoursesSort classroomid = {}, id = {}, coursesort = {}", classroomid, id, coursesort);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(classroomid, id, coursesort)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomCoursesService.updateSortByid(Integer.parseInt(id), 
  					Long.parseLong(classroomid), Integer.parseInt(coursesort));
        } catch (Exception e) {
        	logger.error("updCoursesSort classroomid = {}, id = {}, coursesort = {}", 
        			classroomid, id, coursesort, e);
        }
        return baseResp;
    }
    
    /**
     * @Description: 发布教室课程
     * @param @param classroomid 教室id
     * @param @param id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月25日
 	*/
    @ResponseBody
    @RequestMapping(value = "updateIsup")
    public BaseResp<Object> updateIsup(String classroomid, String id){
        logger.info("updateIsup classroomid = {}, id = {}", classroomid, id);
        BaseResp<Object> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(classroomid, id)){
        	return baseResp;
        }
  		try {
  			baseResp = classroomCoursesService.updateIsup(Integer.parseInt(id), 
  					Long.parseLong(classroomid));
        } catch (Exception e) {
        	logger.error("updateIsup classroomid = {}, id = {}", 
        			classroomid, id, e);
        }
        return baseResp;
    }
    
    /**
     * @Description: 删除教室课程
     * @param @param classroomid 教室id
     * @param @param id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月12日
 	*/
    @RequestMapping(value = "/delCourses")
 	@ResponseBody
    public BaseResp<Object> delCourses(String classroomid, String id){
    	logger.info("delCourses classroomid = {}, id = {}", classroomid, id);
    	BaseResp<Object> baseResp = new BaseResp<Object>();
    	if(StringUtils.hasBlankParams(classroomid, id)){
        	return baseResp;
        }
		try {
			baseResp = classroomCoursesService.updateIsdel(Long.parseLong(classroomid), Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("delCourses classroomid = {}, id = {}", 
					classroomid, id, e);
		}
		return baseResp;
	}
    
    /**
     * @Description: 添加教室课程
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月12日
 	*/
    @RequestMapping(value = "/saveCourses")
 	@ResponseBody
    public BaseResp<Object> saveCourses(@RequestBody ClassroomCourses classroomCourses){
    	logger.info("saveCourses classroomCourses = {}", JSON.toJSONString(classroomCourses));
    	BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			//teachingtypes教学类型 0 录播 1直播
			if("1".equals(classroomCourses.getTeachingtypes())){
				classroomCourses.setDaytime(DateUtils.formatDateString(classroomCourses.getStarttime(), "yyyy-MM-dd"));
			}
			baseResp = classroomCoursesService.saveCourses(classroomCourses);
		} catch (Exception e) {
			logger.error("saveCourses classroomCourses = {}", JSON.toJSONString(classroomCourses), e);
		}
		return baseResp;
	}
    
    /**
     * @Description: 修改教室课程
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月12日
 	*/
    @RequestMapping(value = "/editCourses")
 	@ResponseBody
    public BaseResp<Object> editCourses(@RequestBody ClassroomCourses classroomCourses){
    	logger.info("editCourses classroomCourses = {}", JSON.toJSONString(classroomCourses));
    	BaseResp<Object> baseResp = new BaseResp<Object>();
		try {
			baseResp = classroomCoursesService.editCourses(classroomCourses);
		} catch (Exception e) {
			logger.error("editCourses classroomCourses = {}", JSON.toJSONString(classroomCourses), e);
		}
		return baseResp;
	}
    
    /**
     * @Description: 获取教室课程
     * @param @param classroomid 教室id
     * @param @param id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月12日
 	*/
    @RequestMapping(value = "/selectCourses")
 	@ResponseBody
    public BaseResp<ClassroomCourses> selectCourses(String classroomid, String id){
    	logger.info("selectCourses classroomid = {}, id = {}", classroomid, id);
    	BaseResp<ClassroomCourses> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(classroomid, id)){
        	return baseResp;
        }
		try {
			baseResp = classroomCoursesService.selectCourses(Long.parseLong(classroomid), Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("selectCourses classroomid = {}, id = {}", classroomid, id, e);
		}
		return baseResp;
	}
    
    
    
    
    
    //----------------------------------------share分享------------------------------------------------
    
    /**
     * @Title: http://ip:port/app_service/api/classroom/classroomHeadDetail
     * @Description: 获取教室详情信息---教室有关数据(拆分)---教室顶部数据
     * @param @param classroomid 教室业务id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data: 
     *					fileurl：教室课程视频url---转码后
     * 					pickey:教室课程视频截图
     * 					classphotos:教室图片
     * 					cardid:创建人id
     * 					isfollow：是否已经关注教室   0：否  1：已关注
     * 					isadd：是否加入教室    0：否  1：已加入
     * 					content：名片信息---老师h5
     * 					roomurlshare:分享url
     * @auther yinxc
     * @currentdate:2017年7月15日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomHeadDetail")
    public BaseResp<Object> classroomHeadDetail(String classroomid) {
		logger.info("classroomHeadDetail classroomid = {}", classroomid);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.selectRoomHeadDetail(Long.parseLong(classroomid), null);
   		} catch (Exception e) {
   			logger.error("classroomHeadDetail classroomid = {}", classroomid, e);
   		}
   		return baseResp;
    }
  	
  	
  	/**
     * @Title: http://ip:port/app_service/api/classroom/classroomDetail
     * @Description: 获取教室详情信息---教室有关数据(拆分)
     * @param @param classroomid 教室业务id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data:Classroom
     * 		 Map结果集：     impNum:当前用户在教室发作业的总数
     * 					classroomMembers:当前用户花赞钻石总数
     * @auther yinxc
     * @currentdate:2017年7月15日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomDetail")
    public BaseResp<Object> classroomDetail(String classroomid) {
		logger.info("classroomDetail classroomid={}",classroomid);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.selectRoomDetailAll(Long.parseLong(classroomid), null);
   		} catch (Exception e) {
   			logger.error("classroomDetail classroomid = {}", classroomid, e);
   		}
   		return baseResp;
    }
  	
  	/**
     * @Title: http://ip:port/app_service/api/classroom/classroomMembersDateList
     * @Description: 获取教室按时间排序以及热度
     * @param @param userid 
     * @param @param classroomid 教室业务id
     * @param @param type 0:按时间排序   1:热度
     * @param @param startNo   pageSize
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月15日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomMembersDateList")
    public BaseResp<Object> classroomMembersDateList(String classroomid,
													 String type) {
		logger.info("classroomMembersDateList classroomid={},type={}",
				classroomid,type);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid, type)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp.initCodeAndDesp();
   			List<Improve> list = improveService.selectClassroomImproveList(null, classroomid, "0", type, 0, 15,null);
   			baseResp.setData(list);
   		} catch (Exception e) {
   			logger.error("classroomMembersDateList classroomid = {}, type = {}",
   					classroomid, type, e);
   		}
   		return baseResp;
    }
  	
  	/**
     * @Title: http://ip:port/app_service/api/classroom/coursesList
     * @Description: 获取课程列表
     * @param @param classroomid
     * @param @param startNo   pageSize
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月15日
 	*/
 	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "coursesList")
     public BaseResp<List<ClassroomCourses>> coursesList(String classroomid) {
 		logger.info("coursesList classroomid={}",classroomid);
 		BaseResp<List<ClassroomCourses>> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
              return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
         }
   		try {
   			baseResp = classroomCoursesService.selectListByClassroomid(Long.parseLong(classroomid), 0, 15);
//   			baseResp = classroomChapterService.selectChapterByCid(Long.parseLong(classroomid), 0, 15);
   		} catch (Exception e) {
   			logger.error("coursesList classroomid = {}", classroomid, e);
   		}
   		return baseResp;
     }
 	
 	/**
     * @Title: http://ip:port/app_service/api/classroom/questionsList
     * @Description: 获取教室提问答疑列表
     * @param @param classroomid  教室id
     * @param @param userid  当前访问者id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年7月15日
 	*/
 	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "questionsList")
     public BaseResp<List<ClassroomQuestions>> questionsList(String classroomid) {
 		logger.info("questionsList classroomid = {}",
 				classroomid);
 		BaseResp<List<ClassroomQuestions>> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
              return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
         }
   		try {
   			baseResp = classroomQuestionsMongoService.selectQuestionsListByClassroomid(classroomid, null, null, 15);
   		} catch (Exception e) {
   			logger.error("questionsList classroomid = {}", classroomid, e);
   		}
   		return baseResp;
     }

	//---------------------------提问答疑列表 start------------------------

	/**
	 * @Title: selectQuestionsList
	 * @Description: 获取教室提问答疑列表 for PC端
	 * @param classroomId 教室id
	 * @param dealStatus 处理状态 0未回复 1已忽略 2已回复 3未忽略 4已处理(已忽略+已回复)
	 * @param nickname 用户昵称
	 * @param startCreatetime 查询提问时间开始时间
	 * @param endCreatetime 查询提问时间结束时间
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @author IngaWu
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectQuestionsList")
	public BaseResp<Page<ClassroomQuestions>> selectQuestionsList(String classroomId,String dealStatus,String nickname, String startCreatetime, String endCreatetime, Integer startNum, Integer pageSize){
		logger.info("selectQuestionsList and classroomId={},dealStatus={},nickname={},startCreatetime={},endCreatetime={},startNum={},pageSize={}",classroomId,dealStatus, nickname,startCreatetime,endCreatetime,startNum,pageSize);
		BaseResp<Page<ClassroomQuestions>> baseResp = new BaseResp<>();
		if (null == startNum) {
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (null == pageSize) {
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			Page<ClassroomQuestions> page = classroomQuestionsMongoService.selectQuestionsList(classroomId,dealStatus,nickname, startCreatetime, endCreatetime, startNum, pageSize);
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("selectQuestionsList and classroomId={},dealStatus={}, nickname={},startCreatetime={},endCreatetime={},pageNo={},pageSize={}", classroomId,dealStatus,nickname,startCreatetime,endCreatetime,startNum,pageSize, e);
		}
		return baseResp;
	}

	/**
	 * @Title: selectQuestionsListCount
	 * @Description: 获取教室提问答疑列表数量 for PC端
	 * @param classroomId 教室id
	 * @param dealStatus 处理状态 0未回复 1已忽略 2已回复 3未忽略 4已处理(已忽略+已回复)
	 * @param nickname 用户昵称
	 * @param startCreatetime 查询提问时间开始时间
	 * @param endCreatetime 查询提问时间结束时间
	 * @author IngaWu
	 * @currentdate:2017年7月26日
	 */
	@RequestMapping(value = "selectQuestionsListCount")
	public BaseResp<Integer> selectQuestionsListCount(String classroomId,String dealStatus,String nickname, String startCreatetime, String endCreatetime){
		logger.info("selectQuestionsListCount and classroomId={},dealStatus={},nickname={},startCreatetime={},endCreatetime={}",classroomId,dealStatus, nickname,startCreatetime,endCreatetime);
		BaseResp<Integer> baseResp = new BaseResp<>();
		try {
			baseResp = classroomQuestionsMongoService.selectQuestionsListCount(classroomId,dealStatus,nickname, startCreatetime, endCreatetime);
		} catch (Exception e) {
			logger.error("selectQuestionsListCount and classroomId={},dealStatus={}, nickname={},startCreatetime={},endCreatetime={}", classroomId,dealStatus,nickname,startCreatetime,endCreatetime, e);
		}
		return baseResp;
	}

	/**
	 * @Title: updateQuestionsIsIgnore
	 * @Description: 更改问题忽略状态
	 * @param questionsId 问题id
	 * @param isIgnore  0：未忽略  1：已忽略
	 * @auther IngaWu
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateQuestionsIsIgnore")
	public BaseResp<Object> updateQuestionsIsIgnore(String questionsId ,String isIgnore) {
		logger.info("updateQuestionsIsIgnore for adminservice questionsId={},isIgnore={}", questionsId,isIgnore);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(questionsId,isIgnore)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomQuestionsMongoService.updateQuestionsIsIgnore(questionsId,isIgnore);
		} catch (Exception e) {
			logger.error("updateQuestionsIsIgnore for adminservice questionsId={},isIgnore={}", questionsId,isIgnore,e);

		}
		return baseResp;
	}

	/**
	 * @Title: deleteQuestionsByquestionsId
	 * @Description:删除教室问题
	 * @param questionsId 问题id
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "deleteQuestionsByquestionsId")
	public BaseResp<Object> deleteQuestionsByquestionsId(String questionsId) {
		logger.info("deleteQuestionsByquestionsId and  questionsId={}",questionsId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(questionsId)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomQuestionsMongoService.deleteQuestionsByQuestionsId(questionsId);
		} catch (Exception e) {
			logger.error("deleteQuestionsByquestionsId for adminservice and anquestionsId = {}", questionsId, e);
		}
		return baseResp;
	}

	/**
	 * @Title: deleteLowerByQuestionsId
	 * @Description: 删除教室答疑
	 * @param  questionsId  问题id
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "deleteLowerByQuestionsId")
	public BaseResp<Object> deleteLowerByQuestionsId(String questionsId) {
		logger.info("deleteLowerByQuestionsId and questionsId = {}", questionsId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.isBlank(questionsId)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomQuestionsMongoService.deleteLowerByQuestionsId(questionsId);
		} catch (Exception e) {
			logger.error("deleteLowerByQuestionsId and questionsId = {}", questionsId, e);
		}
		return baseResp;
	}

	/**
	 * @Title: insertQuestionsLower
	 * @Description: 添加教室答疑
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "insertQuestionsLower")
	public BaseResp<Object> insertQuestionsLower(@RequestBody ClassroomQuestionsLower classroomQuestionsLower,String classroomId) {
		logger.info("insertQuestionsLower and classroomQuestionsLower={},classroomId={}",JSON.toJSONString(classroomQuestionsLower),classroomId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(classroomQuestionsLower.getQuestionsid(),classroomQuestionsLower.getContent())) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomQuestionsMongoService.insertQuestionsLower(classroomQuestionsLower);
		} catch (Exception e) {
			logger.error("insertQuestionsLower and classroomQuestionsLower={}",JSON.toJSONString(classroomQuestionsLower),e);
		}
		return baseResp;
	}

	/**
	 * @Title: selectQuestionsByQuestionsId
	 * @Description: 查询教室答疑详情
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectQuestionsByQuestionsId")
	public BaseResp<ClassroomQuestions> selectQuestionsByQuestionsId(String questionsId) {
		logger.info("selectQuestionsByQuestionsId and questionsId={}",questionsId);
		BaseResp<ClassroomQuestions> baseResp = new BaseResp<>();
		if (StringUtils.isBlank(questionsId)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomQuestionsMongoService.selectQuestionsByQuestionsId(questionsId);
		} catch (Exception e) {
			logger.error("selectQuestionsByQuestionsId and questionsId={}",questionsId,e);
		}
		return baseResp;
	}

	/**
	 * @Title: updateQuestionsLower
	 * @Description: 修改教室答疑
	 * @auther IngaWu
	 * @currentdate:2017年7月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "updateQuestionsLower")
	public BaseResp<Object> updateQuestionsLower(@RequestBody ClassroomQuestionsLower classroomQuestionsLower) {
		classroomQuestionsLower.setCreatetime(new Date());
		logger.info("updateQuestionsLower and classroomQuestionsLower={}",JSON.toJSONString(classroomQuestionsLower));
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(classroomQuestionsLower.getQuestionsid(), classroomQuestionsLower.getContent())) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomQuestionsMongoService.updateQuestionsLower(classroomQuestionsLower);
		} catch (Exception e) {
			logger.error("updateQuestionsLower and classroomQuestionsLower={}",JSON.toJSONString(classroomQuestionsLower),e);
		}
		return baseResp;
	}

	//--------------------------提问答疑列表 end-------------------------
	
	
	
	
	/**
     * 将直播教室置为  结束未开启回放
     * @param currentTime
     */
	@SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value="endClassroom")
    public BaseResp<Object> endClassroom(Long currentTime){
        logger.info("endClassroom currentTime={}", currentTime);
        BaseResp<Object> baseResp = new BaseResp<Object>();
        if(currentTime == null){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
//        Date currentDateTime= new Date(currentTime);
        baseResp = this.classroomService.endClassroom(currentTime);
        return baseResp;
    }

	/**
	 * http://localhost:9090/app_service//api/classroom/roomListByIds?roomids=654165112&callback=jsonp
	 * @param roomids id逗号隔开
	 * @param response
	 * @param callback
	 */
	@RequestMapping(value="roomListByIds")
	public void roomListByIds(String roomids, HttpServletResponse response, String callback){
		logger.info("roomListByIds roomids = {}", roomids);
		BaseResp<List<Classroom>> baseResp = new BaseResp<>();
		if(StringUtils.isEmpty(roomids)){
			try{
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
				PrintWriter out = response.getWriter();
				response.setContentType("text/javascript;charset=UTF-8");
				out.print(callback+"("+ JSONObject.fromObject(baseResp).toString()+")");
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			return;
		}

		baseResp = classroomService.roomListByIds(roomids);

		try{
			PrintWriter out = response.getWriter();
			response.setContentType("text/javascript;charset=UTF-8");
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Long.class,new JsonLongValueProcessor());
			jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
			JSONObject jsonObject = JSONObject.fromObject(baseResp,jsonConfig);
			out.print(callback+"("+jsonObject.toString()+")");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
//        return baseResp;
	}

}
