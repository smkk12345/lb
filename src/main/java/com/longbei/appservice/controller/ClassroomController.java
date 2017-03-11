package com.longbei.appservice.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.entity.ClassroomQuestions;
import com.longbei.appservice.entity.ClassroomQuestionsLower;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.service.ClassroomCoursesService;
import com.longbei.appservice.service.ClassroomMembersService;
import com.longbei.appservice.service.ClassroomQuestionsMongoService;
import com.longbei.appservice.service.ClassroomService;
import com.longbei.appservice.service.ImproveService;

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
	@Autowired
	private ImproveService improveService;

	private static Logger logger = LoggerFactory.getLogger(ClassroomController.class);
	
	
	
	
	/**
    * @Title: http://ip:port/app_service/classroom/updateClassnotice
    * @Description: 修改教室公告
    * @param @param classroomid  教室业务id
    * @param @param userid 当前用户--老师id
    * @param @param classnotice 公告信息
    * @param @param ismsg 是否@全体成员   0：否   1：是
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "updateClassnotice")
    public BaseResp<Object> updateClassnotice(String classroomid, String userid, String classnotice, String ismsg) {
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
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "delQuestionsLower")
    public BaseResp<Object> delQuestionsLower(String lowerid) {
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
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "delQuestions")
    public BaseResp<Object> delQuestions(String questionsid) {
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
    * @param @param questionsid  提问答疑id
    * @param @param userid  当前回复者id---老师
    * @param @param content 老师回复内容
    * @param @param friendid 问题创建者id
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "addQuestionsLower")
    public BaseResp<Object> addQuestionsLower(String questionsid, String userid, String content, String friendid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(questionsid, userid, content, friendid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			ClassroomQuestionsLower classroomQuestionsLower = new ClassroomQuestionsLower();
  			classroomQuestionsLower.setFriendid(friendid);
  			classroomQuestionsLower.setContent(content);
  			classroomQuestionsLower.setQuestionsid(questionsid);
  			classroomQuestionsLower.setCreatetime(DateUtils.formatDateTime1(new Date()));
  			classroomQuestionsLower.setUserid(userid);
  			baseResp = classroomQuestionsMongoService.insertQuestionsLower(classroomQuestionsLower);
  		} catch (Exception e) {
  			logger.error("addQuestionsLower questionsid = {}, userid = {}, content = {}, friendid = {}", 
  					questionsid, userid, content, friendid, e);
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
    public BaseResp<Object> addQuestions(String classroomid, String userid, String content) {
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
    public BaseResp<Object> questionsList(String classroomid, int startNo, int pageSize) {
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
    public BaseResp<Object> updateIsdel(String classroomid, String coursesid) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, coursesid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomCoursesService.updateIsdel(Long.parseLong(classroomid), Integer.parseInt(coursesid));
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
    public BaseResp<Object> updateIsdefault(String classroomid, String coursesid) {
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
    public BaseResp<Object> coursesList(String classroomid, int startNo, int pageSize) {
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
    public BaseResp<Object> updateMembersItype(String classroomid, String userid) {
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
    public BaseResp<Object> insertMembers(String classroomid, String userid) {
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
    public BaseResp<Object> selectRoomMembers(String classroomid, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomMembersService.selectListByClassroomid(Long.parseLong(classroomid), startNo, pageSize);
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
    public BaseResp<Object> selectCreateRoom(String userid, String ptype, int startNo, int pageSize) {
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
    public BaseResp<Object> selectInsertRoom(String userid, int startNo, int pageSize) {
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
    public BaseResp<Object> selectRoomSearch(String keyword, String ptype, int startNo, int pageSize) {
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
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectClassroomList
    * @Description: 获取教室列表(所有人可见)
    * @param @param userid
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月3日
	*/
 	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectClassroomList")
    public BaseResp<Object> selectClassroomList(String userid, int startNo, int pageSize) {
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			//ispublic  是否所有人可见。0 所有人可见。1，部分可见
  			baseResp = classroomService.selectClassroomListByIspublic(Long.parseLong(userid), "0", startNo, pageSize);
  		} catch (Exception e) {
  			logger.error("selectClassroomList userid = {}, startNo = {}, pageSize = {}", 
  					userid, startNo, pageSize, e);
  		}
  		return baseResp;
    }
 	
 	/**
     * @Title: http://ip:port/app_service/classroom/classroomMembersList
     * @Description: 获取教室学员动态列表
     * @param @param userid 
     * @param @param classroomid 教室业务id
     * @param @param sift  筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param @param startNo   pageSize
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年3月6日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomMembersList")
    public BaseResp<Object> classroomMembersList(String userid, String classroomid, String sift, 
    		 int startNo, int pageSize) {
   		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid, sift)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp.initCodeAndDesp();
   			List<Improve> list = improveService.selectClassroomImproveListByDate(userid, classroomid, sift, null, startNo, pageSize);
   			baseResp.setData(list);
   		} catch (Exception e) {
   			logger.error("classroomMembersList userid = {}, classroomid = {}, sift = {}, startNo = {}, pageSize = {}", 
   					userid, classroomid, sift, startNo, pageSize, e);
   		}
   		return baseResp;
    }
  	
  	/**
     * @Title: http://ip:port/app_service/classroom/classroomMembersDateList
     * @Description: 获取教室按时间排序以及热度
     * @param @param userid 
     * @param @param classroomid 教室业务id
     * @param @param sift  筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
     * @param @param type 0:按时间排序   1:热度
     * @param @param startNo   pageSize
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年3月6日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomMembersDateList")
    public BaseResp<Object> classroomMembersDateList(String userid, String classroomid, String sift, 
    		 String type, 
    		 int startNo, int pageSize) {
   		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid, sift, type)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp.initCodeAndDesp();
   			List<Improve> list = improveService.selectClassroomImproveList(userid, classroomid, sift, type, startNo, pageSize);
   			baseResp.setData(list);
   		} catch (Exception e) {
   			logger.error("classroomMembersDateList userid = {}, classroomid = {}, sift = {}, type = {}, startNo = {}, pageSize = {}", 
   					userid, classroomid, sift, type, startNo, pageSize, e);
   		}
   		return baseResp;
    }
  	
//  	/**
//     * @Title: http://ip:port/app_service/classroom/classroomMembersDateList
//     * @Description: 获取教室按热度排序
//     * @param @param userid 
//     * @param @param classroomid 教室业务id
//     * @param @param sift  筛选类型 （ 0 - 全部 1 - 关注 2 - 好友 3 - 熟人）
//     * @param @param startNo   pageSize
//     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
//     * @auther yinxc
//     * @currentdate:2017年3月6日
// 	*/
//  	@SuppressWarnings("unchecked")
// 	@RequestMapping(value = "classroomMembersHotList")
//    public BaseResp<Object> classroomMembersHotList(String userid, String classroomid, String sift, 
//    		 int startNo, int pageSize) {
//   		BaseResp<Object> baseResp = new BaseResp<>();
//   		if (StringUtils.hasBlankParams(userid, classroomid, sift)) {
//             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
//        }
//   		try {
//   			baseResp.initCodeAndDesp();
//   			List<Improve> list = improveService.selectClassroomImproveList(userid, classroomid, sift, "1", startNo, pageSize);
//   			baseResp.setData(list);
//   		} catch (Exception e) {
//   			logger.error("classroomMembersHotList userid = {}, classroomid = {}, sift = {}, startNo = {}, pageSize = {}", 
//   					userid, classroomid, sift, startNo, pageSize, e);
//   		}
//   		return baseResp;
//    }
  	
  	/**
     * @Title: http://ip:port/app_service/classroom/classroomDetail
     * @Description: 获取教室详情信息---教室有关数据(拆分)
     * @param @param userid 当前访问id
     * @param @param classroomid 教室业务id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data:Classroom
     * 		 Map结果集：     impNum:当前用户在教室发作业的总数
     * 					classroomMembers:当前用户花赞钻石总数
     * @auther yinxc
     * @currentdate:2017年3月6日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomDetail")
    public BaseResp<Object> classroomDetail(String userid, String classroomid) {
   		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.selectRoomDetail(Long.parseLong(classroomid), Long.parseLong(userid));
   		} catch (Exception e) {
   			logger.error("classroomDetail userid = {}, classroomid = {}", 
   					userid, classroomid, e);
   		}
   		return baseResp;
    }
  	
  	/**
     * @Title: http://ip:port/app_service/classroom/coursesDetail
     * @Description: 获取教室详情信息---教室课程有关数据(拆分)
     * @param @param classroomid 教室业务id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data: courseList 
     * 		 Map结果集：     coursesNum：教室课程总数
     * 					commentNum:评论总数
     * 					questionsNum:提问答疑总数
     * 					coursesDefault:设为默认封面课程   isdefault---1 默认封面  0 非默认
     * @auther yinxc
     * @currentdate:2017年3月6日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "coursesDetail")
    public BaseResp<Object> coursesDetail(String classroomid) {
   		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.selectCoursesDetail(Long.parseLong(classroomid));
   		} catch (Exception e) {
   			logger.error("coursesDetail classroomid = {}", classroomid, e);
   		}
   		return baseResp;
    }
  	
  	
  	
}
