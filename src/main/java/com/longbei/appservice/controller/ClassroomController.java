package com.longbei.appservice.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.entity.ClassroomQuestions;
import com.longbei.appservice.service.ClassroomCoursesService;
import com.longbei.appservice.service.ClassroomMembersService;
import com.longbei.appservice.service.ClassroomQuestionsMongoService;
import com.longbei.appservice.service.ClassroomService;

@RestController
@RequestMapping(value = "/classroom")
public class ClassroomController {
	
	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private ClassroomMembersService classroomMembersService;
	@Autowired
	private ClassroomCoursesService classroomCoursesService;
	@Autowired
	private ClassroomQuestionsMongoService classroomQuestionsMongoService;

	private static Logger logger = LoggerFactory.getLogger(ClassroomController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/classroom/updateClassnotice
    * @Description: 修改教室公告
    * @param @param classroomid  教室业务id
    * @param @param userid 老师id
    * @param @param classnotice 公告信息
    * @param @param ismsg 是否@全体成员   0：否   1：是
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "updateClassnotice")
    public BaseResp<Object> updateClassnotice(@RequestParam("classroomid") String classroomid, 
    		@RequestParam("userid") String userid, String classnotice, String ismsg) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid, classnotice, ismsg)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomService.updateClassnoticeByClassroomid(Long.parseLong(classroomid), 
  					Long.parseLong(userid), classnotice, ismsg);
  		} catch (Exception e) {
  			logger.error("updateClassnotice classroomid = {}, userid = {}, classnotice = {}", 
  					classroomid, userid, classnotice, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/delQuestionsLower
    * @Description: 老师删除某个教室提问答疑---自己的回复
    * @param @param lowerid  回复id
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "delQuestionsLower")
    public BaseResp<Object> delQuestionsLower(@RequestParam("lowerid") String lowerid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(lowerid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomQuestionsMongoService.deleteQuestionsLower(lowerid);
  		} catch (Exception e) {
  			logger.error("delQuestionsLower lowerid = {}", lowerid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/delQuestions
    * @Description: 老师删除单个教室提问答疑
    * @param @param questionsid  教室提问答疑id
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "delQuestions")
    public BaseResp<Object> delQuestions(@RequestParam("questionsid") String questionsid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(questionsid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomQuestionsMongoService.deleteQuestions(questionsid);
  		} catch (Exception e) {
  			logger.error("delQuestions questionsid = {}", questionsid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/addQuestionsLower
    * @Description: 添加教室提问答疑回复---老师回复
    * @param @param classroomid  教室id
    * @param @param userid  问题创建者id
    * @param @param content 问题内容
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "addQuestionsLower")
    public BaseResp<Object> addQuestionsLower(@RequestParam("classroomid") String classroomid, 
    		@RequestParam("userid") String userid, @RequestParam("content") String content) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid, content)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			ClassroomQuestions classroomQuestions = new ClassroomQuestions();
  			classroomQuestions.setClassroomid(classroomid);
  			classroomQuestions.setContent(content);
  			classroomQuestions.setCreatetime(DateUtils.formatDateTime1(new Date()));
  			classroomQuestions.setUserid(userid);
  			baseResp = classroomQuestionsMongoService.insertQuestions(classroomQuestions);
  		} catch (Exception e) {
  			logger.error("addQuestions classroomid = {}, userid = {}, content = {}", classroomid, userid, content, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/addQuestions
    * @Description: 添加教室提问答疑
    * @param @param classroomid  教室id
    * @param @param userid  问题创建者id
    * @param @param content 问题内容
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "addQuestions")
    public BaseResp<Object> addQuestions(@RequestParam("classroomid") String classroomid, 
    		@RequestParam("userid") String userid, @RequestParam("content") String content) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid, content)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			ClassroomQuestions classroomQuestions = new ClassroomQuestions();
  			classroomQuestions.setClassroomid(classroomid);
  			classroomQuestions.setContent(content);
  			classroomQuestions.setCreatetime(DateUtils.formatDateTime1(new Date()));
  			classroomQuestions.setUserid(userid);
  			baseResp = classroomQuestionsMongoService.insertQuestions(classroomQuestions);
  		} catch (Exception e) {
  			logger.error("addQuestions classroomid = {}, userid = {}, content = {}", classroomid, userid, content, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/questionsList
    * @Description: 获取教室提问答疑列表
    * @param @param classroomid  教室id
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "questionsList")
    public BaseResp<Object> questionsList(@RequestParam("classroomid") String classroomid, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomQuestionsMongoService.selectQuestionsListByClassroomid(classroomid, startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("questionsList classroomid = {}, startNo = {}, pageSize = {}", classroomid, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/updateIsdel
    * @Description: 删除单个课程
    * @param @param coursesid  课程id
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "updateIsdel")
    public BaseResp<Object> updateIsdel(@RequestParam("coursesid") String coursesid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(coursesid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomCoursesService.updateIsdel(Integer.parseInt(coursesid));
  		} catch (Exception e) {
  			logger.error("updateIsdel coursesid = {}", coursesid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/updateIsdefault
    * @Description: 修改课程为默认封面
    * @param @param classroomid  教室id
    * @param @param coursesid  课程id
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "updateIsdefault")
    public BaseResp<Object> updateIsdefault(@RequestParam("classroomid") String classroomid, 
    		@RequestParam("coursesid") String coursesid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, coursesid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			//isdefault 是否 默认   1 默认封面  0 非默认
  			baseResp = classroomCoursesService.updateIsdefaultByid(Integer.parseInt(coursesid), Long.parseLong(classroomid), "1");
  		} catch (Exception e) {
  			logger.error("updateIsdefault classroomid = {}", classroomid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/coursesList
    * @Description: 获取课程列表
    * @param @param classroomid
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "coursesList")
    public BaseResp<Object> coursesList(@RequestParam("classroomid") String classroomid, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomCoursesService.selectListByClassroomid(Long.parseLong(classroomid), startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("coursesList classroomid = {}, startNo = {}, pageSize = {}", classroomid, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/updateMembersItype
    * @Description: 教室剔除成员
    * @param @param classroomid
    * @param @param userid
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "updateMembersItype")
    public BaseResp<Object> updateMembersItype(@RequestParam("classroomid") String classroomid, @RequestParam("userid") String userid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomMembersService.updateItypeByClassroomidAndUserid(Integer.parseInt(classroomid), 
  					Long.parseLong(userid), "1");
  		} catch (Exception e) {
  			logger.error("updateMembersItype classroomid = {}, userid = {}", 
  					classroomid, userid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/insertMembers
    * @Description: 添加教室成员
    * @param @param classroomid 教室id 
    * @param @param userid
    * 成员在加入教室之前，如果该教室收费，需先交费后才可加入
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "insertMembers")
    public BaseResp<Object> insertMembers(@RequestParam("classroomid") String classroomid, 
    		@RequestParam("userid") String userid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			ClassroomMembers record = new ClassroomMembers();
  			record.setClassroomid(Long.parseLong(classroomid));
  			record.setCreatetime(new Date());
  			record.setFlowers(0);
  			//hascharge 是否已经付费。0 未付费 1 付费
  			record.setHascharge("1");
  			//itype 0—加入教室 1—退出教室
  			record.setItype(0);
  			record.setLikes(0);
  			record.setUpdatetime(new Date());
  			record.setUserid(Long.parseLong(userid));
  			//userstatus 用户在教室中的身份。0 — 普通学员 1—助教
  			record.setUserstatus("0");
  			baseResp = classroomMembersService.insertClassroomMembers(record);
  		} catch (Exception e) {
  			logger.error("insertMembers classroomid = {}, userid = {}", 
  					classroomid, userid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectRoomMembers
    * @Description: 获取教室成员列表
    * @param @param classroomid
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectRoomMembers")
    public BaseResp<Object> selectRoomMembers(@RequestParam("classroomid") String classroomid, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomMembersService.selectListByClassroomid(Integer.parseInt(classroomid), startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("selectRoomMembers classroomid = {}, startNo = {}, pageSize = {}", 
  					classroomid, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectCreateRoom
    * @Description: 获取我创建的教室,十全十美类型搜索
    * @param @param userid
	* @param @param ptype 十全十美类型    可为null---获取我创建的教室
	* 				0：学习    1：运动   2：社交   3：艺术   4：生活   
	* 				5：公益    6：文学    7：劳动   8：修养   9：健康
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectCreateRoom")
    public BaseResp<Object> selectCreateRoom(@RequestParam("userid") String userid, 
    		@RequestParam("ptype") String ptype, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomService.selectListByUserid(Long.parseLong(userid), ptype, startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("selectCreateRoom userid = {}, ptype = {}, startNo = {}, pageSize = {}", 
  					userid, ptype, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectInsertRoom
    * @Description: 获取我加入的教室
    * @param @param userid
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectInsertRoom")
    public BaseResp<Object> selectInsertRoom(@RequestParam("userid") String userid, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomService.selectInsertByUserid(Long.parseLong(userid), startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("selectInsertRoom userid = {}, startNo = {}, pageSize = {}", 
  					userid, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectRoomSearch
    * @Description: 教室标题搜索
    * @param @param keyword  搜索标题关键字
	* @param @param ptype 十全十美类型    可为null---教室标题搜索
	* 				0：学习    1：运动   2：社交   3：艺术   4：生活   
	* 				5：公益    6：文学    7：劳动   8：修养   9：健康
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectRoomSearch")
    public BaseResp<Object> selectRoomSearch(@RequestParam("keyword") String keyword, 
    		@RequestParam("ptype") String ptype, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(keyword)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomService.selectListByPtype(ptype, keyword, startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("selectRoomSearch keyword = {}, ptype = {}, startNo = {}, pageSize = {}", 
  					keyword, ptype, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
}
