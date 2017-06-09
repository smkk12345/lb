package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.ClassroomCoursesMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.ClassroomMembersMapper;
import com.longbei.appservice.dao.ImproveClassroomMapper;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.ClassroomCourses;
import com.longbei.appservice.entity.ClassroomMembers;
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
			int coursesNum = classroomCoursesMapper.selectCountCourses(classroomid);
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
	 * 2017年3月6日
	 * @param classroomid 教室业务id
	 * @param userid 当前访问者id
	 */
	@Override
	public BaseResp<Object> selectRoomDetail(long classroomid, long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			Classroom classroom = classroomMapper.selectByPrimaryKey(classroomid);
			Map<String, Object> expandData = new HashMap<String, Object>();
			String isadd = "0";
			if(null != classroom){
				UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
				//名片信息
				classroom.setUserCard(userCard);
				//获取成员列表
//				List<ClassroomMembers> memberList = classroomMembersMapper.selectListByClassroomid(classroomid, 0, 10);
//				expandData.put("memberList", memberList);
				
				//获取当前用户在教室发作业的总数
				int impNum = improveClassroomMapper.selectCountByClassroomidAndUserid(classroomid, userid);
				expandData.put("impNum", impNum);
				ClassroomMembers classroomMembers = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
				if(null == classroomMembers){
					classroomMembers = new ClassroomMembers();
					classroomMembers.setLikes(0);
					classroomMembers.setFlowers(0);
					classroomMembers.setDiamonds(0);
				}
				expandData.put("classroomMembers", classroomMembers);
				//itype 0—加入教室 1—退出教室     为null查全部
				ClassroomMembers members = classroomMembersMapper.selectByClassroomidAndUserid(classroomid, userid, "0");
				if(null != members){
					isadd = "1";
				}
				classroom.setIsadd(isadd);
			}
			reseResp.setData(classroom);
			reseResp.setExpandData(expandData);
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
		for (Classroom classroom : list) {
			//获取老师名片信息
			UserCard userCard = userCardMapper.selectByCardid(classroom.getCardid());
			classroom.setUserCard(userCard);
			//获取教室课程默认封面，把教室没有课程视频的去掉
			int res = classroomCoursesMapper.selectCountCourses(classroom.getClassroomid());
			if(res == 0){
				list.remove(classroom);
			} else {
				ClassroomCourses classroomCourses = classroomCoursesMapper.selectIsdefaultByClassroomid(classroom.getClassroomid());
				if(null != classroomCourses){
					//课程默认封面    fileurl  视频文件url（转码后）
					classroom.setFileurl(classroomCourses.getFileurl());
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
									"", "12", classroomid + "", remark, "2", "14", "教室发布最新公告", 0, "", "");
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
		int temp = classroomMapper.updateClassnoticeByClassroomid(classroomid, classnotice);
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

}
