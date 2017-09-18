package com.longbei.appservice.service.impl;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.syscache.SysRulesCache;

import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.CodeDao;
import com.longbei.appservice.entity.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.cache.CommonCache;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.syscache.SysRulesCache;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.dao.mongo.dao.CodeDao;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("classroomService")
public class ClassroomServiceImpl implements ClassroomService {
	
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private ClassroomMembersMapper classroomMembersMapper;
	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private UserCardMapper userCardMapper;
	@Autowired
	private ClassroomCoursesMapper classroomCoursesMapper;
	@Autowired
	private ClassroomQuestionsMongoService classroomQuestionsMongoService;
	@Autowired
	private CommentMongoService commentMongoService;
	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	@Autowired
	private ImproveClassroomMapper improveClassroomMapper;
	@Autowired
	private UserBusinessConcernMapper userBusinessConcernMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private HomeRecommendMapper homeRecommendMapper;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserLevelMapper userLevelMapper;
	@Autowired
	private LiveInfoMongoMapper liveInfoMongoMapper;
	@Autowired
	private ClassroomClassnoticeMapper classroomClassnoticeMapper;
	@Autowired
	private ClassroomChapterMapper classroomChapterMapper;
	@Autowired
	private CommonCache commonCache;
	@Autowired
	private CodeDao codeDao;

	private static Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);
	
	
	
	
	/**
     * 获取教室批复信息---子评论列表(拆分)
     * @param impid 进步id---作业
     * @param lastdate 分页数据最后一个的时间
     * @param pageSize
     * @return
     */
	@Override
	public BaseResp<List<CommentLower>> selectCommentLower(Long userid, String impid, Date lastdate, int pageSize) {
		BaseResp<List<CommentLower>> baseResp = new BaseResp<>();
		try{
			List<CommentLower> lowerList = commentLowerMongoDao.selectCommentLowerListByCommentid(impid);
			//初始化用户信息
			initCommentLowerUserInfoList(lowerList, userid.toString());
			baseResp.setData(lowerList);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		}catch(Exception e){
			logger.error("selectCommentLower impid = {}, lastDate = {}, userid = {}, pageSize = {}", 
	        		impid, DateUtils.formatDateTime1(lastdate), userid, pageSize, e);
        }
    	return baseResp;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<ReplyImprove> selectImproveReply(Long userid, Long impid, Long classroomid) {
		BaseResp<ReplyImprove> baseResp = new BaseResp<>();
		Map<String,Object> map = new HashedMap();
		try{
			Classroom classroom = selectByClassroomid(classroomid);
            UserCard userCard = null;
            if(null != classroom && !StringUtils.isBlank(classroom.getCardid() + "")){
                userCard = userCardMapper.selectByCardid(classroom.getCardid());
            }
            map.put("isteacher", isTeacher(userid.toString(), classroom));
			//获取教室微进步批复作业列表
            ImproveClassroom improveClassroom = improveClassroomMapper.selectByPrimaryKey(impid);
            ReplyImprove replyImprove = null;
            if(null != improveClassroom){
            	replyImprove = new ReplyImprove(improveClassroom.getImpid(),
						improveClassroom.getItype(),
                		improveClassroom.getBrief(),
						improveClassroom.getPickey(),
                		improveClassroom.getUserid(),
						classroomid,
						Constant.IMPROVE_CLASSROOM_REPLY_TYPE,
						improveClassroom.getCreatetime());
				replyImprove.setFilekey(improveClassroom.getFilekey());
				replyImprove.setDuration(improveClassroom.getDuration());
				replyImprove.setPicattribute(improveClassroom.getPicattribute());

				AppUserMongoEntity appUserMongo = new AppUserMongoEntity();
            	appUserMongo.setId(classroom.getUserid().toString());
            	appUserMongo.setUserid(classroom.getUserid().toString());
                appUserMongo.setNickname(userCard.getDisplayname());
                appUserMongo.setAvatar(userCard.getAvatar());
                replyImprove.setAppUserMongoEntity(appUserMongo);
            }
	    	baseResp.setData(replyImprove);
	    	baseResp.setExpandData(map);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
		}catch(Exception e){
            logger.error("selectImproveReply impid = {} classroomid = {}", impid, classroomid, e);
        }
    	return baseResp;
	}



	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<List<Classroom>> selectClassroomListForApp(long userid, Integer startNo, Integer pageSize) {
		BaseResp<List<Classroom>> baseResp = new BaseResp<>();
        try{
            List<Classroom> list = new ArrayList<Classroom>();
            HomeRecommend homeRecommend = new HomeRecommend();
            homeRecommend.setIsdel("0");
            homeRecommend.setRecommendtype(1);
            List<HomeRecommend> homeRecommendList = homeRecommendMapper.selectList(homeRecommend,startNo,pageSize);
            if(homeRecommendList == null){
                homeRecommendList = new ArrayList<HomeRecommend>();
            }

            for(HomeRecommend homeRecommend1: homeRecommendList){
            	Classroom classroom = classroomMapper.selectByPrimaryKey(homeRecommend1.getBusinessid());
                if(classroom == null || "1".equals(classroom.getIsdel())){
                    continue;
                }
                String isadd = "0";
                //获取老师名片信息
    			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
    			classroom.setUserCard(userCard);
    			//获取最新课程视频截图key
				List<ClassroomCourses> courseList = classroomCoursesMapper.selectCroomidOrderByCtime(classroom.getClassroomid(), "1", 0, 1);
				if(null != courseList && courseList.size()>0){
					classroom.setPickey(courseList.get(0).getPickey());
				}
    			
                if(!Constant.VISITOR_UID.equals(String.valueOf(userid))){
                	//itype 0—加入教室 1—退出教室     为null查全部
        			ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroom.getClassroomid(), 
        					userid, "0");
        			if(null != classroomMembers){
        				isadd = "1";
        			}
                }
                classroom.setIsadd(isadd);
                list.add(classroom);
            }
            baseResp.setData(list);
            return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch(Exception e){
            logger.error("select classroom list for app error startNo:{} pageSize:{}",startNo,pageSize);
        }
        return baseResp;
	}



	/**
	 * @author yinxc
	 * 获取教室详情信息---教室课程有关数据(拆分)
	 * 2017年3月7日
	 * @param classroomid 教室业务id
	 */
	@Override
	public BaseResp<Object> selectCoursesDetail(long classroomid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		Map<String, Object> expandData = new HashMap<String, Object>();
		try {
			//教室课程总数
			Integer coursesNum = classroomCoursesMapper.selectCountCourses(classroomid, "1");
			expandData.put("coursesNum", coursesNum);
			//获取评论总数
			int commentNum = 0;
			BaseResp<Integer> baseResp = commentMongoService.selectCommentCountSum
					(String.valueOf(classroomid), Constant.COMMENT_CLASSROOM_TYPE, "");
			if (ResultUtil.isSuccess(baseResp)){
				commentNum = baseResp.getData();
	        }
			expandData.put("commentNum", commentNum);
			//获取提问答疑总数
			long questionsNum = classroomQuestionsMongoService.selectCountQuestions(String.valueOf(classroomid));
			expandData.put("questionsNum", questionsNum);
			//获取课程目录
			List<ClassroomCourses> courseList = classroomCoursesMapper.selectListByClassroomid(classroomid, "1", 0, 5);
			//获取默认封面课程
			ClassroomCourses classroomCourses = classroomCoursesMapper.selectIsdefaultByClassroomid(classroomid);
			expandData.put("coursesDefault", classroomCourses);
			reseResp.setData(courseList);
			reseResp.setExpandData(expandData);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectCoursesDetail classroomid = {}", classroomid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 获取教室详情信息---教室有关数据(拆分)
	 * 2017年6月14日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	@Override
	public BaseResp<Object> selectRoomDetail(Long classroomid, Long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			if(null != classroom){
				String isadd = "0";
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				//老师称呼
//				String cardNickname = initUserInfo(classroom.getCardid());
				String displayname = userCard.getDisplayname();
				map.put("cardid", userCard.getUserid());
				map.put("displayname", displayname);
				map.put("ptype", classroom.getPtype()); //十全十美类型
				map.put("crowd",classroom.getCrowd());//使用人群
				map.put("classbrief", classroom.getClassbrief());
				map.put("syllabus", classroom.getSyllabus());
				map.put("classtitle", classroom.getClasstitle()); //教室标题
				map.put("charge", classroom.getCharge()); //课程价格
				map.put("isfree", classroom.getIsfree()); //是否免费。0 免费 1 收费
				map.put("classinvoloed", classroom.getClassinvoloed()); //教室参与人数
				map.put("classnotice", classroom.getClassnotice()); //教室公告
				map.put("updatetime", DateUtils.formatDateTime1(classroom.getUpdatetime())); //教室公告更新时间
				map.put("classbrief", classroom.getClassbrief()); //教室简介
				int isTeacher = isTeacher(String.valueOf(userid),classroom);
				map.put("isteacher",isTeacher);


				if(userid != null&&!userid.toString().equals(Constant.VISITOR_UID)){
					//获取当前用户在教室发作业的总数
//					Integer impNum = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid + "", userid + "");
//					if(null == impNum){
//						impNum = 0;
//					}
//					map.put("impNum", impNum);
					ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
					if(isTeacher == 1){
//						map.put("classroomMembers", null);
						map.put("shownotice", "1"); //教室公告 1 显示  0 不显示
					}else{
						if(null != classroomMembers){
							if(null != classroomMembers.getNoticeid()&&null != classroom.getNoticeid()){
								if(classroomMembers.getNoticeid() < classroom.getNoticeid()){
									map.put("shownotice", "1"); //教室公告
								}else{
									map.put("shownotice", "0"); //教室公告
								}
							}else{
								map.put("shownotice", "1"); //教室公告
							}
//							map.put("classroomMembers", classroomMembers);
						}else{
//							map.put("classroomMembers", null);
							map.put("shownotice", "1"); //教室公告
						}
					}
					//itype 0—加入教室 1—退出教室     为null查全部
//					ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
					if(null != classroomMembers){
						isadd = "1";
					}
				}else{
//					map.put("classroomMembers", null);
					map.put("shownotice", "1"); //教室公告
				}
				
				map.put("isadd", isadd);
				//获取最新加入成员头像5个
//				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 5);
//				initUserInfoString(memberList);
//				map.put("membersImageList", memberList); //成员头像列表
			}
			reseResp.setData(map);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectRoomDetail classroomid = {}, userid = {}", classroomid, userid, e);
		}
		return reseResp;
	}
	
	/**
    * 初始化消息中用户信息 ------userid
    */
//	private String initUserInfo(long userid){
//		//获取好友昵称
////		String remark = userRelationService.selectRemark(userid, userFeedback.getUserid(), "0");
//		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userid));
//		if(null != appUserMongoEntity){
////			if(!StringUtils.isBlank(remark)){
////				appUserMongoEntity.setNickname(remark);
////			}
//			return appUserMongoEntity.getNickname();
//		}
//		return "";
//  	}
	
	
	/**
    * 初始化消息中用户信息 ------userid
    */
	private void initUserInfoString(List<ClassroomMembers> memberList){
		if(null != memberList && memberList.size()>0){
			for (ClassroomMembers classroomMembers : memberList) {
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(classroomMembers.getUserid()));
				if(null != appUserMongoEntity){
					classroomMembers.setAppUserMongoEntityUserid(appUserMongoEntity);
				}else{
					classroomMembers.setAppUserMongoEntityUserid(new AppUserMongoEntity());
				}
			}
		}
		//获取好友昵称
//			String remark = userRelationService.selectRemark(userid, userFeedback.getUserid(), "0");
  	}
	
	/**
	 * @author yinxc
	 * 获取教室详情信息---教室有关数据(拆分)---教室顶部数据
	 * 2017年3月6日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	@Override
	public BaseResp<Object> selectRoomHeadDetail(Long classroomid, Long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			String isadd = "0";
			if(null != classroom){
				//获取最新课程视频截图key
				List<ClassroomCourses> courseList = classroomCoursesMapper.selectCroomidOrderByCtime(classroom.getClassroomid(), "1", 0, 1);
				//获取视频url---转码后
//				String fileurl = "";
				//isfree 是否免费。0 免费 1 收费
//				if("0".equals(classroom.getIsfree())){
//					if(null != courseList && courseList.size()>0){
//						map.put("fileurl", courseList.get(0).getFileurl());
//						map.put("coursesort", courseList.get(0).getCoursesort());
//					}
//				}else{
//					//若收费，获取第一条免费课程
//					ClassroomCourses classroomCourses = classroomCoursesMapper.selectSortByCid(classroomid, 1);
//					if(null != classroomCourses){
//						map.put("fileurl", classroomCourses.getFileurl());
//						map.put("coursesort", classroomCourses.getCoursesort());
//					}
//				}
				if(null != courseList && courseList.size()>0){
					ClassroomCourses classroomCourses = courseList.get(0);
					map.put("pickey", classroomCourses.getPickey());
					map.put("fileurl", classroomCourses.getFileurl());
					map.put("coursesort", classroomCourses.getCoursesort());
					if(null != classroomCourses.getChapterid()){
						ClassroomChapter classroomChapter = classroomChapterMapper.selectByPrimaryKey(classroomCourses.getChapterid());
						int n = classroomChapterMapper.selectChapterCountByTime(classroomChapter.getClassroomid(),classroomChapter.getCreatetime());
						if(n > 0){
							map.put("chaptersort", 2);
						}else {
							map.put("chaptersort", 1);
						}
					}else{
						map.put("chaptersort", 1);
					}

					if(!StringUtils.isBlank(classroomCourses.getStarttime())){
						map.put("coursestarttime", DateUtils.formatDateString(classroomCourses.getStarttime(), "yyyy-MM-dd HH:mm:ss"));
					}else{
						map.put("coursestarttime", null);
					}
					if(!StringUtils.isBlank(classroomCourses.getEndtime())){
						map.put("courseendtime", DateUtils.formatDateString(classroomCourses.getEndtime(), "yyyy-MM-dd HH:mm:ss"));
					}else{
						map.put("courseendtime", null);
					}
					map.put("teachingtypes", classroomCourses.getTeachingtypes());
					map.put("coursestatus", classroomCourses.getStatus());
					map.put("coursedaytime", classroomCourses.getDaytime());
					map.put("courseliveid", classroomCourses.getLiveid());
					map.put("coursesid", classroomCourses.getId());
				}
				map.put("courseCount", classroom.getAllcourses());
				map.put("classphotos", classroom.getClassphotos());
				map.put("classtitle", classroom.getClasstitle());
				map.put("isfree", classroom.getIsfree());
				map.put("charge", classroom.getCharge());
				map.put("ptype", classroom.getPtype());
				map.put("ispublic", classroom.getIspublic());
				map.put("audiotime", classroom.getAudiotime());
				map.put("videotime", classroom.getVideotime());
				map.put("classnotice", classroom.getClassnotice()); //教室公告
				map.put("updatetime", DateUtils.formatDateTime1(classroom.getUpdatetime())); //教室公告更新时间
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				map.put("cardid", userCard.getUserid());
				map.put("classroomid", classroomid);
				int isTeacher = isTeacher(String.valueOf(userid),classroom);
				map.put("isteacher",isTeacher);
				//是否已经关注教室
				if(userid != null&&!userid.toString().equals(Constant.VISITOR_UID)){
					Map<String,Object> usermap = new HashMap<String,Object>();
					usermap.put("businessType", "4");
					usermap.put("businessId", classroomid);
					usermap.put("userId", userid);
					List<UserBusinessConcern> userBusinessConcern = userBusinessConcernMapper.findUserBusinessConcernList(usermap);

					if(userBusinessConcern.size() > 0){
						map.put("isfollow", "1");
					}else{
						map.put("isfollow", "0");
					}
					if(isTeacher == 1){
						map.put("shownotice", "1"); //教室公告 1 显示  0 不显示
					}else{
						//itype 0—加入教室 1—退出教室     为null查全部
						ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
						if(null != members){
							if(null != members.getNoticeid()&&null != classroom.getNoticeid()){
								if(members.getNoticeid() < classroom.getNoticeid()){
									map.put("shownotice", "1"); //教室公告
								}else{
									map.put("shownotice", "0"); //教室公告
								}
							}else{
								map.put("shownotice", "1"); //教室公告
							}
							isadd = "1";
						}else{
							map.put("shownotice", "1"); //教室公告
						}
					}
					
				}else{
					map.put("isfollow", "0");
					map.put("shownotice", "1"); //教室公告 1 显示  0 不显示
				}

				map.put("isadd", isadd);
				//描述
				map.put("content", classroom.getClassbrief());
//				map.put("isteacher",isTeacher(String.valueOf(userid),classroom));
				
				//最近一次直播的日期时间      提前5分钟可进入     延迟5分钟直播结束
				Date startdate = selectDate(-Constant.LIVE_START);
				
				Date enddate = selectDate(-Constant.LIVE_END);
				
				ClassroomCourses classroomCourses =  classroomCoursesMapper.selectTeachingCoursesListByCid(classroomid, 
						startdate, enddate);
				if(null != classroomCourses){
					List<ClassroomCourses> liveCourses = classroomCoursesMapper.selectDaytimeCoursesListByCid(classroomid,  
							classroomCourses.getDaytime(), startdate, enddate, 0, 0);
					map.put("liveCourses", liveCourses);
					map.put("daytime", classroomCourses.getDaytime());
				}else{
					map.put("liveCourses", new ArrayList<ClassroomCourses>());
					map.put("daytime", null);
				}
				
				//分享url
				map.put("roomurlshare", 
						commonCache.getShortUrl(AppserviceConfig.h5_share_classroom_detail + "?classroomid=" + classroomid));
			}
			reseResp.setData(map);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectRoomDetail classroomid = {}, userid = {}", classroomid, userid, e);
		}
		return reseResp;
	}
	
	private Date selectDate(int horse){
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.MINUTE, horse);
		Date date = calendar.getTime();
		return date;
	}

	@Override
	public BaseResp<Object> selectUsercard(long classroomid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			if(null != classroom){
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				map.put("cardid", userCard.getUserid());
				map.put("classroomid", classroomid);
				//名片信息---老师h5
				map.put("content", userCard.getContent());
				map.put("displayname", userCard.getDisplayname());
				map.put("brief", userCard.getBrief());
				map.put("avatar", userCard.getAvatar());
			}
			reseResp.setData(map);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectUsercard classroomid = {}", classroomid, e);
		}
		return reseResp;
	}

	

	@Override
	public BaseResp<Object> croomDetail(long classroomid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			if(null != classroom){
				map.put("classbrief", classroom.getClassbrief());
				map.put("syllabus", classroom.getSyllabus());
				map.put("crowd", classroom.getCrowd());
			}
			reseResp.setData(map);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("croomDetail classroomid = {}", classroomid, e);
		}
		return reseResp;
	}
	
	
//	/**
//	 * @author yinxc
//	 * 教室学员作业列表
//	 * 2017年3月6日
//	 * @param userId 当前登录用户id
//	 * @param classroomid 教室业务id
//	 * @param type 0.学员动态   1.按热度排列    2.按时间倒序排列
//	 * @param
//	 */
//	@Override
//	public BaseResp<Object> selectImprove(long classroomid, long userid, String type, int startNum, int endNum) {
//		BaseResp<Object> reseResp = new BaseResp<>();
//		try {
//			List<Improve> improves = null;
//			//type 0.学员动态   1.按热度排列    2.按时间倒序排列 
//			if("0".equals(type)){
//	            improves = improveMapper.selectListByBusinessid
//	            		(Long.toString(classroomid), Constant_table.IMPROVE_CLASSROOM, "1", startNum, endNum);
//	            replyImp(improves);
////	            initImproveListOtherInfo(userid,improves);
//			}else if("1".equals(type)){
//				
//				
//				
//			}else{
//				//2.按时间倒序排列 
//				improves = improveMapper.selectListByBusinessid
//	                    (Long.toString(classroomid), Constant_table.IMPROVE_CLASSROOM, null, startNum, endNum);
//				replyImp(improves);
////				initImproveListOtherInfo(userid,improves);
//			}
//			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
//		} catch (Exception e) {
//			logger.error("selectImprove classroomid = {}, userid = {}, type = {}, startNum = {}, endNum = {}", 
//					classroomid, userid, type, startNum, endNum, e);
//		}
//		return reseResp;
//	}

//	//批复信息
//	private void replyImp(List<Improve> improves){
//		if(null != improves && improves.size()>0){
//			for (Improve improve : improves) {
//				//获取教室微进步批复作业列表
//				List<ImproveClassroom> replyList = improveClassroomMapper.selectListByBusinessid(improve.getBusinessid(), improve.getImpid());
//				improve.setReplyList(replyList);
//			}
//		}
//	}

	@Override
	public BaseResp<Object> insertClassroom(Classroom record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//设置默认音频、视频时长
			setClassroomAudioAndVideoTime(record);
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertClassroom record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}

	/**
	 * 设置默认音频、视频时长
	 * @param record
	 */
	private void setClassroomAudioAndVideoTime(Classroom record) {
		if (record.getAudiotime() == null) {
			record.setAudiotime(SysRulesCache.behaviorRule.getCroomimpaudiotime());
		}
		if (record.getVideotime() == null) {
			record.setVideotime(SysRulesCache.behaviorRule.getCroomimpvideotime());
		}
	}
	
	private boolean insert(Classroom record){
		int temp = classroomMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public Classroom selectByClassroomid(long classroomid) {
		Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
		if(null != classroom){
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			classroom.setUserCard(userCard);
			//获取创建人信息
			String nickname = "";
			//sourcetype  0:运营  1:app  2:商户
			if("1".equals(classroom.getSourcetype())){
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(classroom.getUserid() + "");
				classroom.setAppUserMongoEntity(appUserMongoEntity);
				nickname = appUserMongoEntity.getNickname();
			}
			classroom.setNickname(nickname);
		}
		return classroom;
	}
	

	@Override
	public Classroom selectByCid(long classroomid) {
		Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
		UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
		classroom.setUserCard(userCard);
		return classroom;
	}


	@Override
	public BaseResp<Object> updateByClassroomid(Classroom record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(record.getClassroomid());
			//isup 0 - 未发布 。1 --已发布    默认0
			if("1".equals(record.getIsup())){
				record.setIsfree(classroom.getIsfree());
				record.setIspublic(classroom.getIspublic());
				//已发布－私密教室，更新口令
				if ("1".equals(classroom.getIspublic())) {
					record.setJoincode(this.getClassroomJoincode());
				}
			}
			//设置默认音频、视频时长
			setClassroomAudioAndVideoTime(record);
			boolean temp = update(record);
			if (temp) {
				if(!classroom.getIsfree().equals(record.getIsfree())){
					//修改教室是否免费收费     修改课程收费状态
					if("1".equals(record.getIsfree())){
						classroomCoursesMapper.updateCoursetypeByClassroomid(record.getClassroomid(), "1");
						ClassroomCourses classroomCourses = classroomCoursesMapper.selectSortByCid(record.getClassroomid(), 1);
						if(null != classroomCourses){
							classroomCoursesMapper.updateCoursetypeByid(record.getClassroomid(), classroomCourses.getId(), "0");
						}
					}else{
						//coursetype 课程类型.  0 不收费 1 收费
						classroomCoursesMapper.updateCoursetypeByClassroomid(record.getClassroomid(), "0");
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByClassroomid record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}

	private String getClassroomJoincode() {
		String joincode = codeDao.getCode(CodeDao.CodeType.classroom.toString());
		return joincode;
	}
	
	private boolean update(Classroom record){
		//教室收费修改为免费时，将charge置为0
		if ("0".equals(record.getIsfree())) {
			record.setCharge(0);
		}
		int temp = classroomMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<List<Classroom>> selectListByUserid(long userid, String ptype, int startNum, int endNum) {
		BaseResp<List<Classroom>> reseResp = new BaseResp<>();
		try {
			if(StringUtils.isBlank(ptype)){
				ptype = null;
			}
			List<Classroom> list = classroomMapper.selectListByUserid(userid, ptype, startNum, endNum);
			if(null != list && list.size()>0){
				//操作
				list = selectList(list);
			}
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectListByUserid ptype = {}, userid = {}, startNum = {}, endNum = {}", 
					ptype, userid, startNum, endNum, e);
		}
		return reseResp;
	}

//	@Cacheable(cacheNames = RedisCacheNames._ROOM_LIST,key = "#userid +'&'+ #startNum +'&'+ #endNum +'&' +#ptype "
//			,condition="#ispublic == '1'")
	@Override
	public BaseResp<Object> selectClassroomListByIspublic(long userid,String isup, String ispublic, String ptype, int startNum, int endNum) {
		logger.info("selectClassroomListByIspublic ispublic = {}, startNum = {}, endNum = {}",
				ispublic, startNum, endNum);
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Classroom> list = new ArrayList<>();
			if("-1".equals(ptype)){
				//isrecommend 是否推荐。0 - 没有推荐 1 - 推荐
				list = classroomMapper.selectClassroomListByIspublic(ispublic,isup, "", "1", startNum, endNum);
			}else{
				list = classroomMapper.selectClassroomListByIspublic(ispublic,isup, ptype, "", startNum, endNum);
			}
			if(null != list && list.size()>0){
				//操作
				list = selectUserList(list, userid);
			}
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			reseResp.setData(list);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectClassroomListByIspublic ispublic = {}, startNum = {}, endNum = {}", 
					ispublic, startNum, endNum, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 获取我加入的教室信息List
	 * param userid
	 * param startNum   endNum
	 * 2017年7月21日
	 */
	@Override
	public BaseResp<List<Classroom>> selectInsertByUserid(long userid, int startNum, int endNum) {
		BaseResp<List<Classroom>> reseResp = new BaseResp<>();
		try {
			List<Classroom> roomlist = classroomMapper.selectInsertByUserid(userid, startNum, endNum);
			if(startNum == 0 && roomlist.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			if(null != roomlist && roomlist.size()>0){
				//操作
				roomlist = selectUserList(roomlist, userid);
			}
			reseResp.setData(roomlist);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectInsertByUserid userid = {}, startNum = {}, endNum = {}", 
					userid, startNum, endNum, e);
		}
		return reseResp;
	}
	
	private List<Classroom> selectUserList(List<Classroom> list, long userid){
		//把教室没有课程视频的去掉
		//isadd 访问用户是否已加入教室  0：未加入  1：加入
		for (int i = 0; i < list.size(); i++) {
			String isadd = "0";
			Classroom classroom = list.get(i);
			//获取老师名片信息
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			classroom.setUserCard(userCard);
			//获取教室课程默认封面，把教室没有课程视频的去掉
//			Integer res = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
//			if(res == 0){
//				list.remove(classroom);
//			} else {
//				ClassroomCourses classroomCourses = classroomCoursesMapper.selectIsdefaultByClassroomid(classroom.getClassroomid());
//				if(null != classroomCourses){
//					//课程默认封面    fileurl  视频文件url（转码后）
//					classroom.setFileurl(classroomCourses.getFileurl());
//				}
				//获取最新课程视频截图key
				List<ClassroomCourses> courseList = classroomCoursesMapper.selectCroomidOrderByCtime(classroom.getClassroomid(), "1", 0, 1);
				if(null != courseList && courseList.size()>0){
					classroom.setPickey(courseList.get(0).getPickey());
					classroom.setCoursesort(courseList.get(0).getCoursesort());
				}
//			}
			//itype 0—加入教室 1—退出教室     为null查全部
			ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroom.getClassroomid(), 
					userid, "0");
			if(null != classroomMembers){
				isadd = "1";
			}
			classroom.setIsadd(isadd);
		}
		return list;
	}
	
	
	@Override
	public BaseResp<List<Classroom>> selectFansByUserid(long userid, int startNum, int endNum) {
		BaseResp<List<Classroom>> reseResp = new BaseResp<>();
		try {
			List<Classroom> roomlist = new ArrayList<Classroom>();
			Map<String,Object> map = new HashMap<String,Object>();
            map.put("userId",userid);
            map.put("businessType","4");
            map.put("orderType","idDesc");
            map.put("startNum",startNum);
            map.put("pageSize",endNum);
            List<UserBusinessConcern> concernList = this.userBusinessConcernMapper.findUserBusinessConcernList(map);
			if(null != concernList && concernList.size()>0){
				for (UserBusinessConcern userBusinessConcern : concernList) {
					Classroom classroom = classroomMapper.selectByPrimaryKey(userBusinessConcern.getBusinessid());
					roomlist.add(classroom);
				}
			}
			if(startNum == 0 && roomlist.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			if(null != roomlist && roomlist.size()>0){
				//操作
				roomlist = selectUserList(roomlist, userid);
			}
			reseResp.setData(roomlist);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectFansByUserid userid = {}, startNum = {}, endNum = {}", 
					userid, startNum, endNum, e);
		}
		return reseResp;
	}
	

	@Override
	public BaseResp<Object> selectListByPtype(String ptype, String keyword,String searchByCodeword, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			if(StringUtils.isBlank(ptype)){
				ptype = null;
			}
			List<Classroom> list = classroomMapper.selectListByPtype(ptype, keyword,searchByCodeword, startNum, endNum);
			if(null != list && list.size()>0){
				//操作
				list = selectList(list);
			}
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			reseResp.setData(list);
			Integer roomsize = classroomMapper.selectCountByPtype(ptype, keyword,searchByCodeword);
			Map<String, Object> expandData = new HashMap<String, Object>();
			expandData.put("roomsize", roomsize);
			reseResp.setExpandData(expandData);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectListByPtype ptype = {}, keyword = {}, startNum = {}, endNum = {}", 
					ptype, keyword, startNum, endNum, e);
		}
		return reseResp;
	}
	
	private List<Classroom> selectList(List<Classroom> list){
		//把教室没有课程视频的去掉
		//isadd 访问用户是否已加入教室  0：未加入  1：加入
		for (int i = 0; i < list.size(); i++) {
			Classroom classroom = list.get(i);
			//获取老师名片信息
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			classroom.setUserCard(userCard);
			//获取教室课程默认封面，把教室没有课程视频的去掉
//			Integer res = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
//			if(res == 0){
//				list.remove(classroom);
//			} else {
//				ClassroomCourses classroomCourses = classroomCoursesMapper.selectIsdefaultByClassroomid(classroom.getClassroomid());
//				if(null != classroomCourses){
//					//课程默认封面    fileurl  视频文件url（转码后）
//					classroom.setFileurl(classroomCourses.getFileurl());
//				}
				//获取最新课程视频截图key
				List<ClassroomCourses> courseList = classroomCoursesMapper.selectCroomidOrderByCtime(classroom.getClassroomid(), "1", 0, 1);
				if(null != courseList && courseList.size()>0){
					classroom.setPickey(courseList.get(0).getPickey());
					classroom.setCoursesort(courseList.get(0).getCoursesort());
				}
//			}
			String isadd = "0";
			//itype 0—加入教室 1—退出教室     为null查全部
			ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroom.getClassroomid(), 
					classroom.getUserid(), "0");
			if(null != classroomMembers){
				isadd = "1";
			}
//			classroom.setAllcourses(res);
			classroom.setIsadd(isadd);
		}
		return list;
	}

	/**
	 * @author yinxc
	 * 修改教室公告---classnotice
	 * 2017年3月2日
	 * param classnotice 公告
	 * param userid 老师id
	 * param classroomid 教室业务id
	 * param ismsg 是否@全体成员   0：否   1：是
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> updateClassnoticeByClassroomid(long classroomid, long userid, 
			String classnotice, String ismsg) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(null == classroom){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
			//判断当前用户是否是老师
			int isteacher = isTeacher(userid + "", classroom);
			if(isteacher != 1){
				return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1107, Constant.RTNINFO_SYS_1107);
			}
			boolean temp = updateClassnotice(classroomid, classnotice);
			if (temp) {
				if("1".equals(ismsg)){
					//修改完公告后，给教室每个成员推送消息
					List<ClassroomMembers> list = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 0);
					if(null != list && list.size()>0){
						for (ClassroomMembers classroomMembers : list) {
							String remark = "教室《" + classroom.getClasstitle() + "》更新了公告:" + classnotice;
							//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//							//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
//							//snsid---教室业务id
//							//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
							//10：榜中  11 圈子中  12 教室中  13:教室批复作业
//							//mtype 0 系统消息  1 对话消息   2:@我消息
							userMsgService.insertMsg(userid + "", classroomMembers.getUserid().toString(), 
									"", "12", classroomid + "", remark, "2", "60", "教室更新公告", 0, "", "");
//							addMsg(classroomid, userid, classnotice, classroomMembers.getUserid());
						}
					}
				}
				reseResp.setData(classroom);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateClassnoticeByClassroomid classroomid = {}, classnotice = {}", 
					classroomid, classnotice, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> closeRoom(long classroomid, String closeremark){
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = classroomMapper.updateIsdel(classroomid, closeremark, DateUtils.formatDateTime1(new Date()));
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(temp > 0){
				//关闭教室，教室成员下教室
				List<ClassroomMembers> list = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 0);
				if(null != list && list.size()>0){
					for (ClassroomMembers classroomMembers : list) {
						//退出教室
						classroomMembersMapper.updateItypeByClassroomidAndUserid(classroomid, classroomMembers.getUserid(), "1");
						//发@我消息
						String remark = "很遗憾,您加入的教室《"+classroom.getClasstitle()+"》因违反龙杯相关规定，已被关闭";
                        userMsgService.insertMsg(Constant.SQUARE_USER_ID, classroomMembers.getUserid().toString(),
                                "", "12",
                                classroomid + "", remark, "2", "57", "教室关闭", 0, "", "");
					}
				}
				//推送给老师@我消息
				//sourcetype 0:运营  1:app  2:商户
				if("1".equals(classroom.getSourcetype())){
					String remark = "您的教室《"+classroom.getClasstitle()+"》因违反龙杯相关规定，已被关闭";
	                userMsgService.insertMsg(Constant.SQUARE_USER_ID, classroom.getUserid().toString(),
	                        "", "12",
	                        classroomid + "", remark, "2", "57", "教室关闭", 0, "", "");
				}
				
				//取消关注该教室
				userBusinessConcernMapper.deleteBusinessConcern(4, classroomid);
				//关闭教室中的直播
				classroomMapper.updateLiveStatus(classroomid, "2");
				classroomCoursesMapper.updateTeachingLiveStatus(classroomid, "2");
				//调用直播平台---关闭教室
				
				
				
				
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("closeRoom classroomid = {}, closeremark = {}", 
					classroomid, closeremark, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> delRoom(long classroomid){
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = classroomMapper.deleteByPrimaryKey(classroomid);
			if(temp > 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("delRoom classroomid = {}", classroomid, e);
		}
		return reseResp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Object> uproom(long classroomid){
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(classroom.getAllcourses() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_1108, Constant.RTNINFO_SYS_1108);
				return reseResp;
			}
			//sourcetype 0:运营  1:app  2:商户
			if("1".equals(classroom.getSourcetype())){
				//判断是否达到个人发教室的限制
				Integer roomCount = classroomMapper.selectCountByUserid(classroom.getUserid());
				UserInfo userInfo = userInfoMapper.selectInfoMore(classroom.getUserid());
				UserLevel userLevel = userLevelMapper.selectByGrade(userInfo.getGrade());
				if(roomCount >= userLevel.getClassroomnum()){
					return reseResp.initCodeAndDesp(Constant.STATUS_SYS_1114, Constant.RTNINFO_SYS_1114);
				}
			}
			int temp = classroomMapper.updateIsup(classroomid);
			if(temp > 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				//教室发布成功,添加"提成"
				Double commission = SysRulesCache.behaviorRule.getClassroomcommission();
				Classroom updateRoom = new Classroom();
				updateRoom.setCommission(commission);

				//私密教室，添加口令
				if ("1".equals(classroom.getIspublic())) {
					String joincode = codeDao.getCode(CodeDao.CodeType.classroom.toString());
					updateRoom.setJoincode(joincode);
				}
				updateRoom.setClassroomid(classroomid);
				classroomMapper.updateByPrimaryKeySelective(updateRoom);
			}
		} catch (Exception e) {
			logger.error("uproom classroomid = {}", classroomid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 每个成员添加消息
	 * 2017年3月2日
	 */
//	private void addMsg(long classroomid, long userid, String classnotice, long friendid){
//		//添加消息
//		UserMsg record = new UserMsg();
//		record.setFriendid(userid);
//		record.setUserid(friendid);
//		//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//		//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
//		record.setMsgtype("14");
//		//snsid---教室业务id
//		record.setSnsid(classroomid);
//		record.setRemark(classnotice);
//		//isdel 消息假删  0 未删 1 假删
//		record.setIsdel("0");
//		//isread 0 未读  1 已读
//		record.setIsread("0");
//		record.setCreatetime(new Date());
//		// gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中
//		record.setGtype("4");
//		//mtype 0 系统消息  1 对话消息   2:@我消息
//		record.setMtype("2");
//		record.setNum(0);
//		userMsgMapper.insertSelective(record);
//	}
	
	private boolean updateClassnotice(long classroomid, String classnotice){
		ClassroomClassnotice classroomClassnotice = new ClassroomClassnotice();
		classroomClassnotice.setClassroomid(classroomid);
		classroomClassnotice.setClassnotice(classnotice);
		Date date = new Date();
		classroomClassnotice.setUpdatetime(date);
		classroomClassnotice.setCreatetime(date);
		classroomClassnoticeMapper.insert(classroomClassnotice);
		int temp = classroomMapper.updateClassnoticeByClassroomid(classroomid, 
				classnotice, DateUtils.formatDateTime1(new Date()),classroomClassnotice.getId());
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateClassinvoloedByClassroomid(long classroomid, long userid, Integer num) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateClassinvoloed(classroomid, userid, num);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateClassnoticeByClassroomid classroomid = {}, userid = {}, num = {}", 
					classroomid, userid, num, e);
		}
		return reseResp;
	}
	
	private boolean updateClassinvoloed(long classroomid, long userid, Integer num){
		int temp = classroomMapper.updateClassinvoloedByClassroomid(classroomid, num);
		return temp > 0 ? true : false;
	}

	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	//--------------------------admin调用方法------------------------------------
	/**
	 * @author yinxc
	 * 获取教室信息
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	@Override
	public BaseResp<Page<Classroom>> selectPcClassroomList(String isup, String isdel, int startNum, int endNum){
		BaseResp<Page<Classroom>> baseResp = new BaseResp<>();
		Page<Classroom> page = new Page<>(startNum,endNum);
        try {
            int totalcount = classroomMapper.selectCount(isup, isdel);
//            startNum = Page.setPageNo(startNum,totalcount,endNum);
            List<Classroom> list = classroomMapper.selectClassroomList(isup, isdel, startNum, endNum);
            if(null != list && list.size()>0){
            	for (Classroom classroom : list) {
					UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
					classroom.setUserCard(userCard);
					//教室总进步数量
					Integer allimp = classroomMembersMapper.selectRoomImproveCount(classroom.getClassroomid());
					if(allimp == null){
						allimp = 0;
					}
					classroom.setAllimp(allimp);
					//教室课程数量
//					Integer allcourses = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
//					classroom.setAllcourses(allcourses);
					//获取创建人信息
					String nickname = "";
					//sourcetype  0:运营  1:app  2:商户
					if("1".equals(classroom.getSourcetype())){
						AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(classroom.getUserid() + "");
						nickname = appUserMongoEntity.getNickname();
					}
					classroom.setNickname(nickname);
					//获取评论总数
					int commentNum = 0;
					BaseResp<Integer> resResp = commentMongoService.selectCommentCountSum
							(String.valueOf(classroom.getClassroomid()), "12", "");
					if (ResultUtil.isSuccess(resResp)){
						commentNum = resResp.getData();
			        }
					classroom.setCommentNum(commentNum);
					//获取提问答疑总数
					Long questionsNum = classroomQuestionsMongoService.selectCountQuestions(String.valueOf(classroom.getClassroomid()));
					classroom.setQuestionsNum(questionsNum);
            	}
            }
            page.setTotalCount(totalcount);
            page.setList(list);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        	baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectPcClassroomList for adminservice isup = {}, isdel = {}, startNum = {}, pageSize = {}",
  					isup, isdel, startNum, endNum, e);
        }
        return baseResp;
	}
	
	/**
	 * @author yinxc
	 * 获取教室信息
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<Page<Classroom>> selectPcSearchClassroomList(Classroom classrooms, int startNum, int endNum){
		BaseResp<Page<Classroom>> baseResp = new BaseResp<>();
		Page<Classroom> page = new Page<>(startNum/endNum+1,endNum);
		Map<String,Object> map = new HashedMap();
        try {
            int totalcount = classroomMapper.selectSearchCount(classrooms);
//            startNum = Page.setPageNo(startNum,totalcount,endNum);
            List<Classroom> list = classroomMapper.selectSearchList(classrooms, startNum, endNum);
            if(null != list && list.size()>0){
            	for (Classroom classroom : list) {
					UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
					classroom.setUserCard(userCard);
					//教室总进步数量
					Integer allimp = classroomMembersMapper.selectRoomImproveCount(classroom.getClassroomid());
					if(allimp == null){
						allimp = 0;
					}
					classroom.setAllimp(allimp);
					//教室课程数量
//					Integer allcourses = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
//					classroom.setAllcourses(allcourses);
					//获取创建人信息
					String nickname = "";
					//sourcetype  0:运营  1:app  2:商户
					if("1".equals(classroom.getSourcetype())){
						AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(classroom.getUserid() + "");
						nickname = appUserMongoEntity.getNickname();
					}
					classroom.setNickname(nickname);
					//获取评论总数
					int commentNum = 0;
					BaseResp<Integer> resResp = commentMongoService.selectCommentCountSum
							(String.valueOf(classroom.getClassroomid()), "12", "");
					if (ResultUtil.isSuccess(resResp)){
						commentNum = resResp.getData();
			        }
					classroom.setCommentNum(commentNum);
					//获取提问答疑总数
					Long questionsNum = classroomQuestionsMongoService.selectCountQuestions(String.valueOf(classroom.getClassroomid()));
					classroom.setQuestionsNum(questionsNum);
            	}
            }
            page.setTotalCount(totalcount);
            page.setList(list);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        	baseResp.setData(page);
        } catch (Exception e) {
            logger.error("selectPcSearchClassroomList for adminservice classroom, startNum = {}, pageSize = {}",
  					JSON.toJSON(classrooms).toString(), startNum, endNum, e);
        }
        Classroom cr = new Classroom();
		cr.setUserid(classrooms.getUserid());
		cr.setIsdel("2");
		int res = classroomMapper.selectSearchCount(cr);
		if (res > 0){
			map.put("hasclose",1);
		} else {
			map.put("hasclose",0);
		}
		baseResp.setExpandData(map);
        return baseResp;
	}
	
	/**
    * @Description: 获取教室名片列表
    * @param @param startNo   pageSize
    * @param @param 正确返回 code 0 ，验证码不对，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年6月17日
	*/
	@Override
	public BaseResp<List<UserCard>> selectPcUserCardList(int startNum, int endNum){
		BaseResp<List<UserCard>> baseResp = new BaseResp<>();
        try {
        	UserCard userCard = new UserCard();
        	userCard.setSourcetype("0");
        	List<UserCard> list = userCardMapper.selectUserCardList(userCard, startNum, endNum);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        	baseResp.setData(list);
        } catch (Exception e) {
            logger.error("selectPcUserCardList for adminservice startNum = {}, pageSize = {}",
  					startNum, endNum, e);
        }
        return baseResp;
	}

	@Override
	public BaseResp<Object> checkClasstitle(String classtitle) {
		BaseResp<Object> baseResp = new BaseResp<>();
        try {
        	List<Classroom> list = classroomMapper.checkClasstitle(classtitle);
        	if(null != list && list.size()>0){
        		baseResp.initCodeAndDesp(Constant.STATUS_SYS_1105, Constant.RTNINFO_SYS_1105);
        	}else{
        		baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        	}
        } catch (Exception e) {
            logger.error("checkClasstitle for adminservice classtitle = {}", classtitle, e);
        }
        return baseResp;
	}

	@Override
	public BaseResp<Object> updateRoomRecommend(long classroomid, String isrecommend) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Integer temp = classroomMapper.updateIsrecommend(classroomid, isrecommend);
			if (temp > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateRoomRecommend classroomid = {}, isrecommend = {}", 
					classroomid, isrecommend, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> updateRoomRecommendSort(long classroomid, String weight) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Integer temp = classroomMapper.updateRoomRecommendSort(classroomid, weight);
			if (temp > 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateRoomRecommendSort classroomid = {}, weight = {}", 
					classroomid, weight, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> updateClassnoticeByPCClassroomid(long classroomid, long userid, String classnotice,
			String ismsg) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			boolean temp = updateClassnotice(classroomid, classnotice);
			if (temp) {
				if("1".equals(ismsg)){
					//修改完公告后，给教室每个成员推送消息
					List<ClassroomMembers> list = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 0);
					if(null != list && list.size()>0){
						for (ClassroomMembers classroomMembers : list) {
							String remark = "教室《" + classroom.getClasstitle() + "》更新了公告:" + classnotice;
							//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//							//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
//							//snsid---教室业务id
//							//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
							//10：榜中  11 圈子中  12 教室中  13:教室批复作业
//							//mtype 0 系统消息  1 对话消息   2:@我消息
							userMsgService.insertMsg(userid + "", classroomMembers.getUserid().toString(), 
									"", "12", classroomid + "", remark, "2", "60", "教室更新公告", 0, "", "", "", classnotice);
						}
					}
				}
				reseResp.setData(classroom);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateClassnoticeByClassroomid classroomid = {}, classnotice = {}", 
					classroomid, classnotice, e);
		}
		return reseResp;
	}
	
	
	
	
	//----------------------------------share调用---------------------------------------------

	@Override
	public BaseResp<Object> selectRoomDetailAll(Long classroomid, Long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			if(null != classroom){
				String isadd = "0";
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				//老师称呼
//				String cardNickname = initUserInfo(classroom.getCardid());
				String displayname = userCard.getDisplayname();
				map.put("cardid", userCard.getUserid());
				map.put("cardavatar", userCard.getAvatar());
				map.put("cardbrief", userCard.getBrief());
				//名片信息---老师h5
				map.put("content", userCard.getContent());
				map.put("displayname", displayname);
				map.put("ptype", classroom.getPtype()); //十全十美类型
				map.put("classtitle", classroom.getClasstitle()); //教室标题
				map.put("charge", classroom.getCharge()); //课程价格
				map.put("isfree", classroom.getIsfree()); //是否免费。0 免费 1 收费
				map.put("classinvoloed", classroom.getClassinvoloed()); //教室参与人数
				map.put("classnotice", classroom.getClassnotice()); //教室公告
				map.put("updatetime", classroom.getUpdatetime()); //教室公告更新时间
				map.put("classbrief", classroom.getClassbrief()); //教室简介
				if(userid != null){
					//获取当前用户在教室发作业的总数
					Integer impNum = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid + "", userid + "");
					map.put("impNum", impNum);
					ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
					if(null != classroomMembers){
						map.put("classroomMembers", classroomMembers);
					}else{
						map.put("classroomMembers", null);
					}
					//itype 0—加入教室 1—退出教室     为null查全部
					ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
					if(null != members){
						isadd = "1";
					}
				}else{
					map.put("classroomMembers", null);
				}
				map.put("isadd", isadd);
				//获取最新加入成员头像5个
				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 5);
				initUserInfoString(memberList);
				map.put("membersImageList", memberList); //成员头像列表
				//获取最新课程视频截图key  --- 分享获取第一个视频
				ClassroomCourses classroomCourses = classroomCoursesMapper.selectSortByCid(classroomid, 1);
				//获取视频url---转码后
//				if(null != courselist && courselist.size()>0){
					map.put("fileurl", classroomCourses.getFileurl());
//				}
				map.put("classphotos", classroom.getClassphotos());
				map.put("classroomid", classroomid);
				//是否已经关注教室
				Map<String,Object> usermap = new HashMap<String,Object>();
				usermap.put("businessType", "4");
				usermap.put("businessId", classroomid);
				usermap.put("userId", userid);
                List<UserBusinessConcern> userBusinessConcern = userBusinessConcernMapper.findUserBusinessConcernList(usermap);
                if(userBusinessConcern.size() > 0){
                	map.put("isfollow", "1");
                }else{
                	map.put("isfollow", "0");
                }
				if(userid != null){
					//itype 0—加入教室 1—退出教室     为null查全部
					ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
					if(null != members){
						isadd = "1";
					}
				}
				map.put("isadd", isadd);
			}
			reseResp.setData(map);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectRoomDetail classroomid = {}, userid = {}", classroomid, userid, e);
		}
		return reseResp;
	}

	@Override
	public int isTeacher(String userid,Classroom classroom){
		//游客
		if(StringUtils.isBlank(userid)){
			return 0;
		}
		if (userid.equals(classroom.getUserid() + ""))
			return 1;
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResp updateOnlineStatus(String roomid, String courseid, String userid,String status) {
		classroomMapper.updateLiveStatus(Long.parseLong(roomid),status);
		classroomCoursesMapper.updateLiveStatus(Long.parseLong(roomid), Integer.parseInt(courseid),status);
		return BaseResp.ok();
	}


	//------------------------公用方法，初始化消息中用户信息------------------------------------------
    /**
     * 初始化消息中用户信息 ------List
     */
    private void initCommentLowerUserInfoList(List<CommentLower> lowers, String friendid){
    	if(null != lowers && lowers.size()>0){
			Map<String,String> friendRemark = this.userRelationService.selectFriendRemarkList(friendid);
    		for (CommentLower commentLower : lowers) {
    			if(!StringUtils.hasBlankParams(commentLower.getSeconduserid())){
					if(StringUtils.isEmpty(friendid)){
						AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(commentLower.getSeconduserid()));
						commentLower.setSecondNickname(appUserMongoEntity.getNickname());
					}else{
						if(friendRemark.containsKey(commentLower.getSeconduserid())){
							commentLower.setSecondNickname(friendRemark.get(commentLower.getSeconduserid()));
						}else{
							AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(commentLower.getSeconduserid()));
							commentLower.setSecondNickname(appUserMongoEntity.getNickname());
						}
					}
    			}
				if(!StringUtils.hasBlankParams(commentLower.getFirstuserid())){
					if(StringUtils.isEmpty(friendid)){
						AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(commentLower.getFirstuserid()));
						commentLower.setFirstNickname(appUserMongo.getNickname());
					}else{
						if(friendRemark.containsKey(commentLower.getFirstuserid())){
							commentLower.setFirstNickname(friendRemark.get(commentLower.getFirstuserid()));
						}else{
							AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(commentLower.getFirstuserid()));
							commentLower.setFirstNickname(appUserMongo.getNickname());
						}
					}
				}
			}
    	}
        
    }


    /**
     * 将直播教室置为  结束未开启回放
     * @param currentTime
     */
	@Override
	public BaseResp<Object> endClassroom(Long currentTime) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
        try{
        	//获取直播中的教室列表---mongo   liveinfo存放的直播课程
        	List<LiveInfo> list = liveInfoMongoMapper.selectLiveInfoList("0");
        	if(null != list && list.size()>0){
        		for (LiveInfo liveInfo : list) {
        			Classroom classroom = classroomMapper.selectByPrimaryKey(liveInfo.getClassroomid());
    				if(null == classroom){
    					continue;
    				}
    				ClassroomCourses classroomCourses = classroomCoursesMapper.select(liveInfo.getClassroomid(), 
    						liveInfo.getCourseid().intValue());
    				if(null == classroomCourses){
    					continue;
    				}
        			//判断currentTime   及课程结束时间延迟30分钟   是否结束
        			Date currentDateTime= new Date(currentTime);
        			//返回两个时间的误差 单位是秒
        			Long end = DateUtils.getTimeDifference(liveInfo.getEndtime(), currentDateTime);
        			//status 直播状态  未开始 0，直播中 1，，直播结束未开启回放 2，直播结束开启回放 3
        			//未直播的课程    结束时间到了，直接关闭      2017-09-01  
        			if(classroomCourses.getStatus() == 0){
        				if(end>0){
        					//直播结束
            				updateOnlineStatus(liveInfo.getClassroomid() + "", liveInfo.getCourseid() + "", liveInfo.getUserid() + "", "2");
            				//删除mongo数据
            				liveInfoMongoMapper.deleteLiveInfo(liveInfo.getClassroomid(), liveInfo.getCourseid());
            				continue;
        				}
        			}
        			// 开启直播的课程延迟分钟关闭   2017-09-01
        			if(classroomCourses.getStatus() == 1){
        				int liveend = Constant.LIVE_END;
        				if(end>liveend*60){
            				//直播结束
            				updateOnlineStatus(liveInfo.getClassroomid() + "", liveInfo.getCourseid() + "", liveInfo.getUserid() + "", "2");
            				//删除mongo数据
            				liveInfoMongoMapper.deleteLiveInfo(liveInfo.getClassroomid(), liveInfo.getCourseid());
            				continue;
            			}
        			}
        			
        			//教室直播课程开始前10分钟推送消息
        			Long teaching = DateUtils.getTimeDifference(currentDateTime, liveInfo.getStarttime());
        			if(teaching <= 10*60 && teaching > 0){
        				
        				//判断消息是否已发送    
        				List<UserMsg> msgList = userMsgService.selectList("72", liveInfo.getLiveid() + "",
								liveInfo.getClassroomid(), 0, 1);
						if(null != msgList && msgList.size()>0){
							//消息已发送
							continue;
						}
						HashSet<String> set = new HashSet<String>();

        				//推送消息---已加入该教室的人员
        				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroom.getClassroomid(),0,0);
        				String insertremark = "您加入的教室《" + classroom.getClasstitle() + "》直播还有十分钟开始,赶快去看看吧";
        				if(null != memberList && memberList.size()>0){
        					for (int i=0;i<memberList.size();i++) {
        						set.add(memberList.get(i).getUserid() + "");
        						userMsgService.insertMsg(Constant.SQUARE_USER_ID, memberList.get(i).getUserid()+"",
        								"", "12", classroom.getClassroomid() + "", insertremark, "2", "72", "直播开始提示", 0, 
        								liveInfo.getLiveid() + "", "");
        					}
        				}
        				
        				//推送消息---已关注该教室的人员
        				Map<String,Object> map = new HashMap<String,Object>();
        	            map.put("businessType","4");
        	            map.put("businessId", classroom.getClassroomid());
        	            List<UserBusinessConcern> concernList = this.userBusinessConcernMapper.findConcernUserList(map);
        	            String remark = "您关注的教室《" + classroom.getClasstitle() + "》直播还有十分钟开始,赶快去看看吧";
        				if(null != concernList && concernList.size()>0){
        					for (UserBusinessConcern userBusinessConcern : concernList) {
        						if(set.contains(userBusinessConcern.getUserid().toString())){
        							continue;
        						}
                				userMsgService.insertMsg(Constant.SQUARE_USER_ID, userBusinessConcern.getUserid().toString(), 
        								"", "12", liveInfo.getClassroomid() + "", remark, "0", "72", "直播开始提示", 0, 
        								liveInfo.getLiveid() + "", "");
        					}
        				}
        				
        			}
				}
        	}
        }catch(Exception e){
            logger.error("endClassroom currentTime;{}", currentTime, e);
        }
        return baseResp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseResp<List<UserCard>> selectUsercardList(Long classroonid) {
		BaseResp<List<UserCard>> baseResp = new BaseResp<>();
		List<UserCard> list = new ArrayList<>();
		try{
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroonid);
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			list.add(userCard);
			baseResp.setData(list);
		}catch (Exception e){
			logger.error("selectUsercardList error and msg={}",e);
		}
		return baseResp.initCodeAndDesp();
	}

	@Override
	public BaseResp<List<Classroom>> roomListByIds(String roomids) {
		BaseResp<List<Classroom>> baseResp = new BaseResp<>();
		try{
			List<Classroom> list = new ArrayList<>();
			String[] roomidArr = roomids.split(",");
			for (int i = 0; i < roomidArr.length; i++) {
				Classroom room = classroomMapper.selectByPrimaryKey(Long.parseLong(roomidArr[i]));
				if(null != room){
					list.add(room);
				}
			}
			baseResp.setData(list);
			baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("roomListByIds error roomids = {}",roomids,e);
		}
		return baseResp;
	}

	@Override
	public BaseResp<Object> ignoreNotice(long classroomid, long userid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try{
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			if(null != classroom.getNoticeid()){
				classroomMembersMapper.updateNoticeId(classroom.getClassroomid(),
						classroom.getNoticeid(),userid);
			}
			baseResp.initCodeAndDesp();
		}catch (Exception e){
			logger.error("ignoreNotice error and classroomid={},userid={}",classroomid,userid,e);
		}
		return baseResp;
	}


}
