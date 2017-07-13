package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ClassroomCoursesMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.ImproveClassroomMapper;
import com.longbei.appservice.dao.UserBusinessConcernMapper;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomCourses;
import com.longbei.appservice.entity.ClassroomMembers;
import com.longbei.appservice.entity.UserBusinessConcern;
import com.longbei.appservice.entity.UserCard;
import com.longbei.appservice.service.ClassroomQuestionsMongoService;
import com.longbei.appservice.service.ClassroomService;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.UserMsgService;

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
//	@Autowired
//	private ImproveMapper improveMapper;
	@Autowired
	private ImproveClassroomMapper improveClassroomMapper;
	@Autowired
	private UserBusinessConcernMapper userBusinessConcernMapper;
	@Autowired
	private UserMongoDao userMongoDao;
//	@Autowired
//	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ClassroomServiceImpl.class);
	


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
			Integer coursesNum = classroomCoursesMapper.selectCountCourses(classroomid);
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
			List<ClassroomCourses> courseList = classroomCoursesMapper.selectListByClassroomid(classroomid, 0, 5);
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
	public BaseResp<Object> selectRoomDetail(long classroomid, long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			String isadd = "0";
			if(null != classroom){
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				//老师称呼
//				String cardNickname = initUserInfo(classroom.getCardid());
				String displayname = userCard.getDisplayname();
				map.put("cardid", userCard.getUserid());
				map.put("displayname", displayname);
				map.put("ptype", classroom.getPtype()); //十全十美类型
				map.put("classtitle", classroom.getClasstitle()); //教室标题
				map.put("charge", classroom.getCharge()); //课程价格
				map.put("isfree", classroom.getIsfree()); //是否免费。0 免费 1 收费
				map.put("classinvoloed", classroom.getClassinvoloed()); //教室参与人数
				map.put("classnotice", classroom.getClassnotice()); //教室公告
				map.put("updatetime", classroom.getUpdatetime()); //教室公告更新时间
				map.put("classbrief", classroom.getClassbrief()); //教室简介
				
				//获取当前用户在教室发作业的总数
				Integer impNum = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid + "", userid + "");
				map.put("impNum", impNum);
				ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
				if(null != classroomMembers){
					map.put("classroomMembers", classroomMembers);
				}else{
					map.put("classroomMembers", new ClassroomMembers());
				}
				//itype 0—加入教室 1—退出教室     为null查全部
				ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
				if(null != members){
					isadd = "1";
				}
				map.put("isadd", isadd);
				//获取最新加入成员头像5个
				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 5);
				initUserInfoString(memberList);
				map.put("membersImageList", memberList); //成员头像列表
				
				
				//名片信息
//				classroom.setUserCard(userCard);
				//获取成员列表
//				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 5);
//				reseResp.getExpandData().put("memberList", memberList);
//				
//				reseResp.getExpandData().put("impNum", impNum);
//				ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
//				if(null == classroomMembers){
//					classroomMembers = new ClassroomMembers();
//					classroomMembers.setLikes(0);
//					classroomMembers.setFlowers(0);
//					classroomMembers.setDiamonds(0);
//				}
//				reseResp.getExpandData().put("classroomMembers", classroomMembers);
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
	public BaseResp<Object> selectRoomHeadDetail(long classroomid, long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> map = new HashMap<String, Object>();
			String isadd = "0";
			if(null != classroom){
				//获取最新课程视频截图key
				List<ClassroomCourses> courseList = classroomCoursesMapper.selectCroomidOrderByCtime(classroom.getClassroomid(), 0, 1);
				//获取视频url---转码后
//				String fileurl = "";
				//isfree 是否免费。0 免费 1 收费
				if("0".equals(classroom.getIsfree())){
					if(null != courseList && courseList.size()>0){
						map.put("fileurl", courseList.get(0).getFileurl());
					}
				}else{
					//若收费，获取第一条免费课程
					List<ClassroomCourses> courselist = classroomCoursesMapper.selectListByClassroomid(classroomid, 0, 1);
					if(null != courselist && courselist.size()>0){
						map.put("fileurl", courselist.get(0).getFileurl());
					}
				}
				if(null != courseList && courseList.size()>0){
					map.put("pickey", courseList.get(0).getPickey());
				}
				map.put("classphotos", classroom.getClassphotos());
				map.put("classtitle", classroom.getClasstitle());
				map.put("ptype", classroom.getPtype());
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				map.put("cardid", userCard.getUserid());
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
				
				//itype 0—加入教室 1—退出教室     为null查全部
				ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
				if(null != members){
					isadd = "1";
				}
				map.put("isadd", isadd);
				//名片信息---老师h5
				map.put("content", userCard.getContent());
				
				//分享url
				map.put("roomurlshare", "");
				
				//获取成员列表
//				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 10);
//				expandData.put("memberList", memberList);
				
				//获取当前用户在教室发作业的总数
//				int impNum = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid, userid);
//				reseResp.getExpandData().put("impNum", impNum);
//				ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
//				if(null == classroomMembers){
//					classroomMembers = new ClassroomMembers();
//					classroomMembers.setLikes(0);
//					classroomMembers.setFlowers(0);
//					classroomMembers.setDiamonds(0);
//				}
//				reseResp.getExpandData().put("classroomMembers", classroomMembers);
				
				
//				classroom.setIsadd(isadd);
			}
			reseResp.setData(map);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectRoomDetail classroomid = {}, userid = {}", classroomid, userid, e);
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
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertClassroom record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
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
			//教室课程数量
			Integer allcourses = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
			classroom.setAllcourses(allcourses);
			//获取创建人信息
			String nickname = "";
			//sourcetype  0:运营  1:app  2:商户
			if("1".equals(classroom.getSourcetype())){
				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(classroom.getUserid() + "");
				nickname = appUserMongoEntity.getNickname();
			}
			classroom.setNickname(nickname);
		}
		return classroom;
	}

	@Override
	public BaseResp<Object> updateByClassroomid(Classroom record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByClassroomid record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean update(Classroom record){
		int temp = classroomMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> selectListByUserid(long userid, String ptype, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
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

	@Override
	public BaseResp<Object> selectClassroomListByIspublic(long userid, String ispublic, String ptype, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Classroom> list = classroomMapper.selectClassroomListByIspublic(ispublic, ptype, startNum, endNum);
			if(null != list && list.size()>0){
				//操作
				list = selectList(list);
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
	 * param ptype:十全十美类型    可为null
	 * param userid
	 * param pageNo   pageSize
	 * 2017年2月28日
	 */
	@Override
	public BaseResp<Object> selectInsertByUserid(long userid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Classroom> roomlist = new ArrayList<Classroom>();
			List<ClassroomMembers> list = classroomMembersMapper.selectInsertByUserid(userid, startNum, endNum);
			if(null != list && list.size()>0){
				for (ClassroomMembers classroomMembers : list) {
					Classroom classroom = classroomMapper.selectByPrimaryKey(classroomMembers.getClassroomid());
					roomlist.add(classroom);
				}
			}
			if(startNum == 0 && roomlist.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			//操作
			roomlist = selectList(roomlist);
			reseResp.setData(roomlist);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectInsertByUserid userid = {}, startNum = {}, endNum = {}", 
					userid, startNum, endNum, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> selectListByPtype(String ptype, String keyword, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			if(StringUtils.isBlank(ptype)){
				ptype = null;
			}
			List<Classroom> list = classroomMapper.selectListByPtype(ptype, keyword, startNum, endNum);
			if(null != list && list.size()>0){
				//操作
				list = selectList(list);
			}
			if(startNum == 0 && list.size() == 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_33);
				return reseResp;
			}
			reseResp.setData(list);
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
		String isadd = "0";
		for (int i = 0; i < list.size(); i++) {
			Classroom classroom = list.get(i);
			//获取老师名片信息
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			classroom.setUserCard(userCard);
			//获取教室课程默认封面，把教室没有课程视频的去掉
			Integer res = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
			if(res == 0){
				list.remove(classroom);
			} else {
//				ClassroomCourses classroomCourses = classroomCoursesMapper.selectIsdefaultByClassroomid(classroom.getClassroomid());
//				if(null != classroomCourses){
//					//课程默认封面    fileurl  视频文件url（转码后）
//					classroom.setFileurl(classroomCourses.getFileurl());
//				}
				//获取最新课程视频截图key
				List<ClassroomCourses> courseList = classroomCoursesMapper.selectCroomidOrderByCtime(classroom.getClassroomid(), 0, 1);
				if(null != courseList && courseList.size()>0){
					classroom.setPickey(courseList.get(0).getPickey());
				}
			}
			//itype 0—加入教室 1—退出教室     为null查全部
			ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroom.getClassroomid(), 
					classroom.getUserid(), "0");
			if(null != classroomMembers){
				isadd = "1";
			}
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
	@Override
	public BaseResp<Object> updateClassnoticeByClassroomid(long classroomid, long userid, String classnotice, String ismsg) {
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
							String remark = "在教室'" + classroom.getClasstitle() + "'中发布了最新公告,并@了您";
							//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问   
//							//14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
//							//snsid---教室业务id
//							//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
							//10：榜中  11 圈子中  12 教室中  13:教室批复作业
//							//mtype 0 系统消息  1 对话消息   2:@我消息
							userMsgService.insertMsg(userid + "", classroomMembers.getUserid().toString(), 
									"", "12", classroomid + "", remark, "0", "14", "教室发布最新公告", 0, "", "");
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
						//发通知消息
						String remark = "您参加的教室'"+classroom.getClasstitle()+"'因违反龙杯相关规定，已被关闭";
                        userMsgService.insertMsg(Constant.SQUARE_USER_ID, classroomMembers.getUserid().toString(),
                                "", "12",
                                classroomid + "", remark, "0", "54", "教室关闭", 0, "", "");
					}
				}
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
	
	@Override
	public BaseResp<Object> uproom(long classroomid){
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			int temp = classroomMapper.updateIsup(classroomid);
			if(temp > 0){
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
		int temp = classroomMapper.updateClassnoticeByClassroomid(classroomid, 
				classnotice, DateUtils.formatDateTime1(new Date()));
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
					Integer allimp = improveClassroomMapper.selectCountByClassroomidAndUserid(classroom.getClassroomid().toString(), null);
					classroom.setAllimp(allimp);
					//教室课程数量
					Integer allcourses = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
					classroom.setAllcourses(allcourses);
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
	@Override
	public BaseResp<Page<Classroom>> selectPcSearchClassroomList(Classroom classrooms, int startNum, int endNum){
		BaseResp<Page<Classroom>> baseResp = new BaseResp<>();
		Page<Classroom> page = new Page<>(startNum,endNum);
        try {
            int totalcount = classroomMapper.selectSearchCount(classrooms);
//            startNum = Page.setPageNo(startNum,totalcount,endNum);
            List<Classroom> list = classroomMapper.selectSearchList(classrooms, startNum, endNum);
            if(null != list && list.size()>0){
            	for (Classroom classroom : list) {
					UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
					classroom.setUserCard(userCard);
					//教室总进步数量
					Integer allimp = improveClassroomMapper.selectCountByClassroomidAndUserid(classroom.getClassroomid().toString(), null);
					classroom.setAllimp(allimp);
					//教室课程数量
					Integer allcourses = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
					classroom.setAllcourses(allcourses);
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
	
}
