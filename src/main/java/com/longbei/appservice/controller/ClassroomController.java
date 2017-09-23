package com.longbei.appservice.controller;

import com.longbei.appservice.cache.CommonCache;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	@Autowired
	private ClassroomChapterService classroomChapterService;
	@Autowired
	private CommonCache commonCache;

	private static Logger logger = LoggerFactory.getLogger(ClassroomController.class);
	
	
	
	/**
     * @Title: http://ip:port/app_service/classroom/checkInsertTeaching
     * @Description: 判断学员是否能加入直播教室
     * @param @param userid 
     * @param @param classroomid 教室业务id
     * @param @param coursesid   课程id
     * @param @param liveid      直播id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年3月6日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "checkInsertTeaching")
    public BaseResp<Object> checkInsertTeaching(String userid, String classroomid,
													 String coursesid, String liveid) {
		logger.info("checkInsertTeaching userid={},classroomid={},coursesid={},liveid={}",
				userid,classroomid,coursesid,liveid);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid, coursesid, liveid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			BaseResp<ClassroomCourses> resResp = classroomCoursesService.selectCourses(Long.parseLong(classroomid), Integer.parseInt(coursesid));
   			Integer status = 0;
   			if(resResp.getCode() == 0){
   				ClassroomCourses classroomCourses = resResp.getData();
//   				if(classroomCourses.getStatus().equals("1")){
//   					baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
//   				}else if(classroomCourses.getStatus().equals("0")){
//   					baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,"直播未开始");
//   				}else if(classroomCourses.getStatus().equals("2")||classroomCourses.getStatus().equals("3")){
//   					baseResp.initCodeAndDesp(Constant.STATUS_SYS_01,"直播已结束");
//   				}else{
//   					//doNothing
//   				}
   				status = classroomCourses.getStatus();
   			}
   			baseResp.getExpandData().put("status", status);
   			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
   		} catch (Exception e) {
   			logger.error("checkInsertTeaching userid={},classroomid={},coursesid={},liveid={}",
   				userid,classroomid,coursesid,liveid, e);
   		}
   		return baseResp;
    }
	
	/**
     * @Title: http://ip:port/app_service/classroom/selectCroomIsreplyList
     * @Description: 获取教室未批复，已批复的进步
     * @param @param userid 
     * @param @param classroomid 教室业务id
     * @param @param type 0:未批复   1:已批复
     * @param @param startNo   pageSize
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @auther yinxc
     * @currentdate:2017年3月6日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectCroomIsreplyList")
    public BaseResp<Object> selectCroomIsreplyList(String userid, String classroomid,
													 String type, Integer startNo, Integer pageSize) {
		logger.info("selectCroomIsreplyList userid={},classroomid={},type={},startNo={},pageSize={}",
				userid,classroomid,type,startNo,pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid, type)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
   		try {
   			Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
        	if (!userid.equals(classroom.getUserid() + "")){
        		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_1111, Constant.RTNINFO_SYS_1111);
        	}
        	baseResp.initCodeAndDesp();
   			List<Improve> list = improveService.selectCroomImpList(userid, classroomid, type, sNo, sSize);
   			baseResp.setData(list);
   			Map<String,Object> map = new HashedMap();
   			map.put("isteacher",classroomService.isTeacher(userid,classroom));
   			baseResp.setExpandData(map);
   		} catch (Exception e) {
   			logger.error("selectCroomIsreplyList userid = {}, classroomid = {}, type = {}, startNo = {}, pageSize = {}",
   					userid, classroomid, type, startNo, pageSize, e);
   		}
   		return baseResp;
    }
  	
	
//	/**
//     * url: http://ip:port/app_service/classroom/selectCommentLower
//     * 获取教室批复信息---子评论列表(拆分)
//     * @param userid 
//     * @param impid 批复作业进步id---主评论id
//     * @param lastDate 分页数据最后一个的时间
//     * @param pageSize
//     * @return
//     */
//	 @SuppressWarnings("unchecked")
//	@ResponseBody
//    @RequestMapping(value = "selectCommentLower")
//    public BaseResp<List<CommentLower>> selectCommentLower(String userid, String impid,
//			String lastDate, Integer pageSize){
//        logger.info("selectCommentLower impid = {}, lastDate = {}, userid = {}, pageSize = {}", 
//        		impid, lastDate, userid, pageSize);
//        BaseResp<List<CommentLower>> baseResp = new BaseResp<>();
//        if(StringUtils.hasBlankParams(userid, impid)){
//            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
//        }
//        if(null == pageSize){
//            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
//        }
//        try {
//            baseResp = classroomService.selectCommentLower(Long.parseLong(userid), 
//            		impid, 
//            		lastDate == null ? null : DateUtils.parseDate(lastDate), pageSize);
//        } catch (Exception e) {
//            logger.error("selectCommentLower impid = {}, lastDate = {}, userid = {}, pageSize = {}", 
//        		impid, lastDate, userid, pageSize, e);
//        }
//        return baseResp;
//    }
	
	
	/**
     * url: http://ip:port/app_service/classroom/selectImproveReply
     * @ 获取教室批复信息(拆分)
     * @param userid 
     * @param impid 进步id---作业
     * @param businessid 教室id
     * @return
     */
	 @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "selectImproveReply")
    public BaseResp<ReplyImprove> selectImproveReply(String userid, String impid, String businessid){
        logger.info("selectImproveReply impid = {}, businessid = {}, userid = {}", 
        		impid, businessid, userid);
        BaseResp<ReplyImprove> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid, impid, businessid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        try {
            baseResp = classroomService.selectImproveReply(Long.parseLong(userid), 
            		Long.parseLong(impid), Long.parseLong(businessid));
            if(ResultUtil.isSuccess(baseResp)){
            	baseResp.getExpandData().put("shareurl",
                        commonCache.getShortUrl(AppserviceConfig.h5_share_improve_detail
                                + "?impid=" + impid + "&businesstype=5&businessid=" + businessid));
            }
        } catch (Exception e) {
            logger.error("selectImproveReply userid = {}, impid = {}, businessid = {}", 
            		userid, impid, businessid, e);
        }
        return baseResp;
    }
	
	
	/**
     * url: http://ip:port/app_service/classroom/selectClassroomListForApp
     * @ 首页推荐的教室列表
     * @param userid
     * @param startNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "selectClassroomListForApp")
    public BaseResp<List<Classroom>> selectClassroomListForApp(String userid ,Integer startNo, Integer pageSize){
        logger.info("selectClassroomListForApp userid={},startNo={},pageSize={}",userid,startNo,pageSize);
        BaseResp<List<Classroom>> baseResp = new BaseResp<>();
        if(StringUtils.hasBlankParams(userid)){
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if(startNo == null || startNo < 0){
        	startNo = Integer.parseInt(Constant.DEFAULT_START_NO);
        }
        if(null == pageSize){
            pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
        }

        try {
            baseResp = classroomService.selectClassroomListForApp(Long.parseLong(userid),startNo,pageSize);
        } catch (Exception e) {
            logger.error("select Classroom list userid = {}, startNo = {}, pageSize = {}", 
            		userid, startNo, pageSize, e);
        }
        return baseResp;
    }
	

	/**
	* @Title: http://ip:port/app_service/classroom/selectRoomMemberDetail
	* @Description: 查看教室单个用户的信息
	* @param @param classroomid 教室id
	* @param @param userid 用户id
	* @param @param currentUserId 当前登录用户id
	* @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
	* @auther yinxc
	* @currentdate:2017年7月12日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectRoomMemberDetail")
	public BaseResp<Object> selectRoomMemberDetail(String classroomid, String userid, String currentUserId) {
		logger.info("selectRoomMemberDetail classroomid = {}, userid = {}, currentUserId = {}", 
				classroomid, userid, currentUserId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(classroomid, userid, currentUserId)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomMembersService.selectRoomMemberDetail(Long.parseLong(classroomid), 
					Long.parseLong(userid), Long.parseLong(currentUserId));
			if(ResultUtil.isSuccess(baseResp)){
	            baseResp.getExpandData().put("shareurl",
	                    commonCache.getShortUrl(AppserviceConfig.h5_share_rank_improve
	                            + "?rankid=" + classroomid + "&userid=" + userid + "&businesstype=4"));
	        }
		} catch (Exception e) {
			logger.error("selectRoomMemberDetail classroomid = {}, userid = {}, currentUserId = {}", 
				classroomid, userid, currentUserId, e);
		}
		return baseResp;
	}

	
	/**
    * @Title: http://ip:port/app_service/classroom/delQuestionsLower
    * @Description: 老师删除某个教室提问答疑---自己的回复
    * @param @param classroomid  教室id
    * @param @param lowerid  回复id
    * @param @param userid 当前用户id
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "delQuestionsLower")
    public BaseResp<Object> delQuestionsLower(String classroomid, String lowerid, String userid) {
		logger.info("delQuestionsLower classroomid = {}, lowerid={}, userid = {}", classroomid, lowerid, userid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, lowerid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomQuestionsMongoService.deleteQuestionsLower(classroomid, lowerid, userid);
  		} catch (Exception e) {
  			logger.error("delQuestionsLower classroomid = {}, lowerid = {}, userid = {}", classroomid, lowerid, userid, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/delQuestions
    * @Description: 删除单个教室提问答疑
    * @param @param questionsid  教室提问答疑id
    * @param @param userid 当前用户id
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "delQuestions")
    public BaseResp<Object> delQuestions(String questionsid, String userid) {
		logger.info("delQuestions questionsid={}",questionsid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(questionsid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomQuestionsMongoService.deleteQuestions(questionsid, userid);
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
		logger.info("addQuestionsLower questionsid={},userid={},content={},friendid={}",
				questionsid,userid,content,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(questionsid, userid, content, friendid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			ClassroomQuestionsLower classroomQuestionsLower = new ClassroomQuestionsLower();
  			classroomQuestionsLower.setFriendid(friendid);
  			classroomQuestionsLower.setContent(content);
  			classroomQuestionsLower.setQuestionsid(questionsid);
  			classroomQuestionsLower.setCreatetime(new Date());
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
		logger.info("addQuestions classroomid={},userid={},content={}",classroomid,userid,content);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid, content)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			ClassroomQuestions classroomQuestions = new ClassroomQuestions();
  			classroomQuestions.setClassroomid(classroomid);
  			classroomQuestions.setContent(content);
  			classroomQuestions.setCreatetime(new Date());
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
    * @param @param userid  当前访问者id
    * @param @param lastDate 分页数据最后一个的时间
    * @param @param pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月1日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "questionsList")
    public BaseResp<List<ClassroomQuestions>> questionsList(String classroomid, String userid, String lastDate, Integer pageSize) {
		logger.info("questionsList classroomid = {}, userid = {}, lastDate = {}, pageSize = {}", 
				classroomid, userid, lastDate, pageSize);
		BaseResp<List<ClassroomQuestions>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			baseResp = classroomQuestionsMongoService.selectQuestionsListByClassroomid(classroomid, userid, 
  					lastDate == null ? null : DateUtils.parseDate(lastDate), sSize);
  		} catch (Exception e) {
  			logger.error("questionsList classroomid = {}, lastDate = {}, pageSize = {}",
					classroomid, lastDate, pageSize, e);
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
		logger.info("updateIsdel classroomid={},coursesid={}",classroomid,coursesid);
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
		logger.info("updateIsdefault classroomid={},coursesid={}",classroomid,coursesid);
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
    public BaseResp<List<ClassroomCourses>> coursesList(String classroomid, Integer startNo, Integer pageSize) {
		logger.info("coursesList classroomid={},startNo={},pageSize={}",classroomid,startNo,pageSize);
		BaseResp<List<ClassroomCourses>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			baseResp = classroomCoursesService.selectListByClassroomid(Long.parseLong(classroomid), sNo, sSize);
  		} catch (Exception e) {
  			logger.error("coursesList classroomid = {}, startNo = {}, pageSize = {}",
					classroomid, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/updateMembersItype
    * @Description: 教室老师剔除成员---推送消息
    * @param @param classroomid
    * @param @param userid
    * @param @param currentUserId 当前登录用户id
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "updateMembersItype")
    public BaseResp<Object> updateMembersItype(String classroomid, String userid, String currentUserId) {
		logger.info("updateMembersItype classroomid={},userid={}, currentUserId={}",classroomid,userid,currentUserId);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid, currentUserId)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
  			baseResp = classroomMembersService.quitClassroom(Long.parseLong(classroomid),
  					Long.parseLong(userid), Long.parseLong(currentUserId), "1");
  		} catch (Exception e) {
  			logger.error("updateMembersItype classroomid = {}, userid = {}, currentUserId = {}", 
  					classroomid, userid, currentUserId, e);
  		}
  		return baseResp;
    }
	
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
		logger.info("updateClassnotice classroomid={},userid={},classnotice={},ismsg={}",
				classroomid,userid,classnotice,ismsg);
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
	* @Title: http://ip:port/app_service/classroom/quitClassroom
	* @Description: 退出教室
	* @param @param classroomid
	* @param @param userid
	* @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
	* @auther yinxc
	* @currentdate:2017年2月28日
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
			baseResp = classroomMembersService.updateItypeByClassroomidAndUserid(Long.parseLong(classroomid),
					Long.parseLong(userid), "1");
		} catch (Exception e) {
			logger.error("quitClassroom classroomid = {}, userid = {}",
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
		logger.info("insertMembers classroomid={},userid={}",classroomid,userid);
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
  			record.setIcount(0);
  			record.setComplaintotalcount(0);
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
    * @param @param userid 当前登录者id
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectRoomMembers")
    public BaseResp<List<ClassroomMembers>> selectRoomMembers(String classroomid, String userid, Integer startNo, Integer pageSize) {
		logger.info("selectRoomMembers classroomid={}, userid={},startNo={},pageSize={}",
				classroomid,userid,startNo,pageSize);
		BaseResp<List<ClassroomMembers>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(classroomid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		try {
			int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
			int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
			if(null != startNo){
				sNo = startNo.intValue();
			}
			if(null != pageSize){
				sSize = pageSize.intValue();
			}
  			baseResp = classroomMembersService.selectListByClassroomid(Long.parseLong(classroomid), userid, sNo, sSize);
  		} catch (Exception e) {
  			logger.error("selectRoomMembers classroomid = {}, userid = {}, startNo = {}, pageSize = {}",
  					classroomid, userid, startNo, pageSize, e);
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
    public BaseResp<List<Classroom>> selectCreateRoom(String userid, String ptype, Integer startNo, Integer pageSize) {
		logger.info("selectCreateRoom userid={},ptype={},startNo={},pageSize={}",
				userid,ptype,startNo,pageSize);
		BaseResp<List<Classroom>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			baseResp = classroomService.selectListByUserid(Long.parseLong(userid), ptype, sNo, sSize);
  		} catch (Exception e) {
  			logger.error("selectCreateRoom userid = {}, ptype = {}, startNo = {}, pageSize = {}",
  					userid, ptype, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectInsertRoom
    * @Description: 获取我加入, 已关注, 我创建的的教室
    * @param @param userid
    * @param @param type 0:已加入  1：已关注  2:我创建的
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年7月19日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectInsertRoom")
    public BaseResp<List<Classroom>> selectInsertRoom(String userid, String type, Integer startNo, Integer pageSize) {
		logger.info("selectInsertRoom userid={},type={},startNum={},pageSize={}",userid,type,startNo,pageSize);
		BaseResp<List<Classroom>> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, type)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			if("0".equals(type)){
  				baseResp = classroomService.selectInsertByUserid(Long.parseLong(userid), sNo, sSize);
  			}else if("1".equals(type)){
  				baseResp = classroomService.selectFansByUserid(Long.parseLong(userid), sNo, sSize);
  			}else{
  				baseResp = classroomService.selectListByUserid(Long.parseLong(userid), null, sNo, sSize);
  			}
  		} catch (Exception e) {
  			logger.error("selectInsertRoom userid = {}, type = {], startNo = {}, pageSize = {}",
  					userid, type, startNo, pageSize, e);
  		}
  		return baseResp;
    }
	
	/**
    * @Title: http://ip:port/app_service/classroom/selectRoomSearch
    * @Description: 教室标题/教室口令搜索
    * @param @param keyword  搜索标题关键字
	* @param @param ptype 十全十美类型    可为null---教室标题搜索
	* 				0：学习    1：运动   2：社交   3：艺术   4：生活   
	* 				5：公益    6：文学    7：劳动   8：修养   9：健康
    * @param @param startNo   pageSize
	* @param searchByCodeword 根据入榜口令搜索榜单 可不传 传入的值是0/1 1代表按照榜口令搜索榜 0代表按照榜名称搜索榜
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年2月28日
	*/
	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectRoomSearch")
    public BaseResp<Object> selectRoomSearch(String keyword, String ptype,String searchByCodeword, Integer startNo, Integer pageSize) {
		logger.info("selectRoomSearch keyword={},ptype={},startNo={},pageSize={}",
				keyword,ptype,startNo,pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(keyword) || ("1".equals(searchByCodeword) && StringUtils.isEmpty(keyword))) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
		if(StringUtils.isEmpty(searchByCodeword)){
			searchByCodeword = "0";
		}

  		try {
  			baseResp = classroomService.selectListByPtype(ptype, keyword,searchByCodeword, sNo, sSize);
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
	 *@param @param ptype 类型
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年3月3日
	*/
 	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectClassroomList")
    public BaseResp<Object> selectClassroomList(String userid, String ptype, Integer startNo, Integer pageSize) {
		logger.info("selectClassroomList userid = {}, ptype = {}, startNo = {}, pageSize = {}",
				userid, ptype, startNo, pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
  		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
  		try {
  			//ispublic  是否所有人可见。0 所有人可见。1，部分可见
  			baseResp = classroomService.selectClassroomListByIspublic(Long.parseLong(userid),"1", "0", ptype, sNo, sSize);
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
    		 Integer startNo, Integer pageSize,String version) {
		logger.info("classroomMembersList userid = {}, classroomid = {}, sift = {}, startNo = {}, pageSize = {}",
				userid, classroomid, sift, startNo, pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid, sift)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
   		try {
   			baseResp.initCodeAndDesp();
   			List<Improve> list = improveService.selectClassroomImproveListByDate(userid, classroomid, sift, null, sNo, sSize,version);
   			baseResp.setData(list);
   		} catch (Exception e) {
   			logger.error("classroomMembersList userid = {}, classroomid = {}, sift = {}, startNo = {}, pageSize = {}",
   					userid, classroomid, sift, sNo, sSize, e);
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
													 String type, Integer startNo, Integer pageSize,String version) {
		logger.info("classroomMembersDateList userid={},classroomid={},sift={},type={},startNo={},pageSize={}",
				userid,classroomid,sift,type,startNo,pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(userid, classroomid, sift, type)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
   		try {
   			baseResp.initCodeAndDesp();
   			List<Improve> list = improveService.selectClassroomImproveList(userid, classroomid, sift, type, sNo, sSize,version);
   			baseResp.setData(list);
   			Map<String,Object> map = new HashedMap();
      		Classroom classroom = classroomService.selectByClassroomid(Long.parseLong(classroomid));
      		map.put("isteacher",classroomService.isTeacher(userid,classroom));
      		baseResp.setExpandData(map);
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
		logger.info("classroomDetail userid={},classroomid={}",userid,classroomid);
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
     * @Description: 获取教室详情信息---教室课程有关数据(拆分)---暂时不用
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
//  						coursesNum：教室课程总数
//    * 					commentNum:评论总数
//    * 					questionsNum:提问答疑总数
//    * 					coursesDefault:设为默认封面课程   isdefault---1 默认封面  0 非默认
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "coursesDetail")
    public BaseResp<Object> coursesDetail(String classroomid) {
		logger.info("coursesDetail classroomid = {}", classroomid);
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
  	
  	/**
     * @Title: http://ip:port/app_service/classroom/classroomHeadDetail
     * @Description: 获取教室详情信息---教室有关数据(拆分)---教室顶部数据
     * @param @param classroomid 教室业务id
     * @param @param userid 当前访问id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data: 
     *					fileurl：教室课程视频url---转码后---头部显示课程
     * 					pickey:教室课程视频截图---头部显示课程
     * 					coursesort   课程序号---头部显示课程
     * 					coursestarttime 直播开始时间---头部显示课程
     * 					courseendtime   直播结束时间---头部显示课程
     * 					teachingtypes   教学类型 0 录播 1直播---头部显示课程
     * 					coursestatus    直播状态  未开始 0，直播中 1，，直播结束未开启回放 2，直播结束开启回放 3---头部显示课程
     * 					coursedaytime   开始年月日 用于方便查询---头部显示课程
     * 					courseliveid    直播id---头部显示课程
     * 					cardid:创建人id
     * 					isfollow：是否已经关注教室   0：否  1：已关注
     * 					isadd：是否加入教室    0：否  1：已加入
     * 					content：描述
     * 					roomurlshare:分享url
     * 					ptype:类型
     * 					isfree：是否免费。0 免费 1 收费
     * 					charge：课程价格
     * 					coursetype   课程类型.  0 不收费 1 收费
     * 					classphotos:教室图片
     * 					liveCourses  最近一天的直播课程列表，按照时间顺序排列
     * 					daytime      最近一次直播的日期时间
     * @auther yinxc
     * @currentdate:2017年6月14日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "classroomHeadDetail")
    public BaseResp<Object> classroomHeadDetail(String classroomid, String userid) {
		logger.info("classroomHeadDetail classroomid = {}", classroomid);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid, userid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.selectRoomHeadDetail(Long.parseLong(classroomid), Long.parseLong(userid));
   		} catch (Exception e) {
   			logger.error("classroomHeadDetail classroomid = {}", classroomid, e);
   		}
   		return baseResp;
    }
  	
  	/**
     * @Title: http://ip:port/app_service/classroom/croomDetail
     * @Description: 获取教室详情---教室介绍信息
     * @param @param classroomid 教室业务id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data: 
     *					classbrief：教室简介
     * 					syllabus:课程大纲
     * 					crowd:适合人群
     * @auther yinxc
     * @currentdate:2017年7月14日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "croomDetail")
    public BaseResp<Object> croomDetail(String classroomid) {
		logger.info("croomDetail classroomid = {}", classroomid);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.croomDetail(Long.parseLong(classroomid));
   		} catch (Exception e) {
   			logger.error("croomDetail classroomid = {}", classroomid, e);
   		}
   		return baseResp;
    }
  	
  	
  	/**
     * @Title: http://ip:port/app_service/classroom/selectUsercard
     * @Description: 获取老师h5信息
     * @param @param classroomid 教室业务id
     * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
     * @desc data: 
     * 					displayname：老师称呼
     * 					avatar：老师头像
     * 					content：名片信息---老师h5
     * 					brief:简介
     * @auther yinxc
     * @currentdate:2017年7月20日
 	*/
  	@SuppressWarnings("unchecked")
 	@RequestMapping(value = "selectUsercard")
    public BaseResp<Object> selectUsercard(String classroomid) {
		logger.info("selectUsercard classroomid = {}", classroomid);
		BaseResp<Object> baseResp = new BaseResp<>();
   		if (StringUtils.hasBlankParams(classroomid)) {
             return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
        }
   		try {
   			baseResp = classroomService.selectUsercard(Long.parseLong(classroomid));
   		} catch (Exception e) {
   			logger.error("selectUsercard classroomid = {}", classroomid, e);
   		}
   		return baseResp;
    }

	/**
	 * http://ip:port/app_service/classroom/selectUsercardList
	 * 获取教室中老师列表
	 * @param classroomid
	 * @return  BaseResp<List<UserCard>>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "selectUsercardList")
	public BaseResp<List<UserCard>> selectUsercardList(String classroomid) {
		logger.info("selectUsercard classroomid = {}", classroomid);
		BaseResp<List<UserCard>> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(classroomid)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = classroomService.selectUsercardList(Long.parseLong(classroomid));
		} catch (Exception e) {
			logger.error("selectUsercard classroomid = {}", classroomid, e);
		}
		return baseResp;
	}

	/**
	 * @Title: http://ip:port/app_service/classroom/chapterList
	 * @Description: 获取章节列表
	 * @param @param classroomid
	 * @param @param startNo   pageSize
	 * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
	 * @auther yinxc
	 * @currentdate:2017年3月1日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "chapterList")
	public BaseResp<List<ClassroomChapter>> chapterList(String classroomid, Integer startNo, Integer pageSize) {
		logger.info("chapterList classroomid={},startNo={},pageSize={}",classroomid,startNo,pageSize);
		BaseResp<List<ClassroomChapter>> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(classroomid)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		int sNo = Integer.parseInt(Constant.DEFAULT_START_NO);
		int sSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		if(null != startNo){
			sNo = startNo.intValue();
		}
		if(null != pageSize){
			sSize = pageSize.intValue();
		}
		try {
			baseResp = classroomChapterService.selectChapterByCid(Long.parseLong(classroomid), sNo, sSize);
		} catch (Exception e) {
			logger.error("coursesList classroomid = {}, startNo = {}, pageSize = {}",
					classroomid, startNo, pageSize, e);
		}
		return baseResp;
	}


	/**
	 *  http://ip:port/app_service/classroom/ignoreNotice
	 * 忽略公告
	 * @param classroomid
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "ignoreNotice")
	public BaseResp<Object> ignoreNotice(String classroomid, String userid) {
		logger.info("ignoreNotice classroomid={},userid={}",classroomid,userid);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(classroomid,userid)){
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			return baseResp;
		}
		try{
			baseResp = classroomService.ignoreNotice(Long.parseLong(classroomid),Long.parseLong(userid));
		}catch (Exception e){
			logger.error("ignoreNotice classroomid={},userid={}",classroomid,userid,e);
		}
		return baseResp;
	}




}
