package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_table;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.ImproveMapper;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.SnsFansMapper;
import com.longbei.appservice.dao.SnsFriendsMapper;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.dao.UserSettingCommonMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Circle;
import com.longbei.appservice.entity.Classroom;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.Rank;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.entity.SnsFriends;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.entity.UserSettingCommon;
import com.longbei.appservice.service.UserMsgService;

@Service("userMsgService")
public class UserMsgServiceImpl implements UserMsgService {

	@Autowired
	private UserMsgMapper userMsgMapper;
	@Autowired
	private ImproveMapper improveMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private SnsFansMapper snsFansMapper;
	@Autowired
	private SnsFriendsMapper snsFriendsMapper;
	@Autowired
	private RankMapper rankMapper;
	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	
	@Autowired
	private UserSettingCommonMapper userSettingCommonMapper;
	
	private static Logger logger = LoggerFactory.getLogger(UserMsgServiceImpl.class);
	
	
	@Override
	public BaseResp<Object> deleteByid(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = delete(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByid id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private boolean delete(Integer id){
		int temp = userMsgMapper.deleteByPrimaryKey(id);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> deleteByUserid(long userid, String mtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = deleteUserid(userid, mtype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByUserid userid={},msg={}",userid,e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> deleteByMtypeAndMsgtype(long userid, String mtype, String msgtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = deleteUseridAndMsgtype(userid, mtype, msgtype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByMtypeAndMsgtype userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}

	private boolean deleteUseridAndMsgtype(long userid, String mtype, String msgtype){
		int temp = userMsgMapper.deleteByMtypeAndMsgtype(userid, mtype, msgtype);
		return temp > 0 ? true : false;
	}
	
	@Override
	public BaseResp<Object> deleteByLikeUserid(long userid, String msgtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = deleteLikeUserid(userid, msgtype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByUserid userid={},msg={}",userid,e);
		}
		return reseResp;
	}
    
	private boolean deleteLikeUserid(long userid, String msgtype){
		int temp = userMsgMapper.deleteByLikeUserid(userid, msgtype);
		return temp > 0 ? true : false;
	}
	
	private boolean deleteUserid(long userid, String mtype){
		int temp = userMsgMapper.deleteByUserid(userid, mtype);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> insertSelective(UserMsg record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean insert(UserMsg record){
		int temp = userMsgMapper.insertSelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public UserMsg selectByid(Integer id) {
		UserMsg userMsg = userMsgMapper.selectByPrimaryKey(id);
		return userMsg;
	}

	@Override
	public BaseResp<Object> selectByUserid(long userid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMsg> list = userMsgMapper.selectByUserid(userid, startNum, endNum);
			if (null != list && list.size()>0) {
//				AppUserMongoEntity mongoEntity = userMongoDao.getAppUser(userid+"");
//				if(null != mongoEntity){
//					
//				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_28, Constant.RTNINFO_SYS_28);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectByUserid userid={},msg={}",userid,e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> updateByid(UserMsg record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = update(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateByid record={},msg={}",record,e);
		}
		return reseResp;
	}
	
	private boolean update(UserMsg record){
		int temp = userMsgMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsreadByid(Integer id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateId(id);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsreadByid id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private boolean updateId(Integer id){
		int temp = userMsgMapper.updateIsreadByid(id);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsreadByUserid(long userid, String mtype, String msgtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateUserid(userid, mtype, msgtype);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsreadByUserid userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}
	
	private boolean updateUserid(long userid, String mtype, String msgtype){
		if(StringUtils.isBlank(msgtype)){
			msgtype = null;
		}
		int temp = userMsgMapper.updateIsreadByUserid(userid, mtype, msgtype);
		return temp > 0 ? true : false;
	}

	/**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同mtype类型消息列表信息
	 * 2017年2月13日
	 * mtype 0 系统消息(通知消息.进步消息等) 
	 * 		 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * 		 2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 */
	@Override
	public BaseResp<Object> selectOtherList(long userid, String mtype, String msgtype, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMsg> list = userMsgMapper.selectOtherList(userid, mtype, msgtype, startNum, endNum);
			if (null != list && list.size()>0) {
				//拼接获取   对话消息---除赞消息,粉丝消息  消息记录展示字段List
				//@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
				//					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果,通过或拒绝  )
				for (UserMsg userMsg : list) {
					if(!"15".equals(userMsg.getMsgtype()) && !"16".equals(userMsg.getMsgtype()) && !"17".equals(userMsg.getMsgtype()) ){
						initMsgUserInfoByFriendid(userMsg);
					}else{
						//15:获奖   16:剔除   17:加入请求审批结果,通过或拒绝-----统一为龙杯公司推送的消息
						AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
						appUserMongoEntity.setNickname(Constant.MSG_LONGBEI_NICKNAME);
						appUserMongoEntity.setAvatar(Constant.MSG_LONGBEI_DIFAULT_AVATAR);
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_28, Constant.RTNINFO_SYS_28);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectOtherList userid = {}, mtype = {}, msg = {}", userid, mtype, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc  <暂时没有用到----扩展方法>
	 * 消息--@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	 *					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 2017年2月13日
	 * UserMsg
	 */
//	private UserMsg selectUserMsgOther(UserMsg userMsg){
//		if(userMsg.getMsgtype().equals(Constant.MSG_INVITE_TYPE)){
//			//拼接10:邀请消息字段
//			userMsg = otherItype(userMsg);
//		}
//		return userMsg;
//	}
	
	/**
	 * @author yinxc
	 * 拼接---获取榜,圈子,教室@我消息各类型所需要字段     <暂时没有用到----扩展方法>
	 * gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对@我消息
	 * 2017年2月13日
	 */
//	private UserMsg otherItype(UserMsg userMsg){
//		if("2".equals(userMsg.getGtype())){
//			//榜评论图片
//			Rank rank = rankMapper.selectByPrimaryKey(userMsg.getSnsid());
//			if(null != rank){
//				userMsg.setImpPicFilekey(rank.getRankphotos());
//			}
//		}
//		if("3".equals(userMsg.getGtype())){
//			//圈子评论图片
//			Circle circle = circleMapper.selectByPrimaryKey(userMsg.getSnsid());
//			if(null != circle){
//				userMsg.setImpPicFilekey(circle.getCirclephotos());
//			}
//		}
//		if("4".equals(userMsg.getGtype())){
//			//教室评论图片
//			Classroom classroom = classroomMapper.selectByPrimaryKey(userMsg.getSnsid());
//			if(null != classroom){
//				userMsg.setImpPicFilekey(classroom.getClassphotos());
//			}
//		}
//		return userMsg;
//	}

	/**
	 * @author yinxc
	 * 获取消息列表信息(对话消息---除赞消息,粉丝消息)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * return_type
	 */
	@Override
	public BaseResp<Object> selectExceptList(long userid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMsg> list = userMsgMapper.selectExceptList(userid, startNum, endNum);
			//key 新粉丝：is_new_fans  点赞:is_like
			List<UserSettingCommon> setlist = userSettingCommonMapper.selectByUserid(userid+"");
			Map<String, Object> expandData = new HashMap<>();
			for (UserSettingCommon userSettingCommon : setlist) {
				expandData.put(userSettingCommon.getUkey(), userSettingCommon.getUvalue());
			}
			//获取新粉丝count
			int fansCount = userMsgMapper.selectCountByType(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_FANS_TYPE, "0");
			expandData.put("fansCount", fansCount);
			//获取点赞count
			int likeCount = userMsgMapper.selectCountByType(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_LIKE_TYPE, "0");
			expandData.put("likeCount", likeCount);
			
			if (null != list && list.size()>0) {
				//拼接获取   对话消息---除赞消息,粉丝消息  消息记录展示字段List
				//msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等
				for (UserMsg userMsg : list) {
					if(userMsg.getMsgtype().equals(Constant.MSG_COMMENT_TYPE)){
						//拼接 1 评论消息List
						userMsg = msgComment(userid, userMsg);
						if(StringUtils.isBlank(userMsg.getRemark())){
							//获取评论内容
							CommentLower commentLower = commentLowerMongoDao.selectCommentLowerByid(Long.toString(userMsg.getSnsid()));
							if(null != commentLower){
								userMsg.setRemark(commentLower.getContent());
							}
						}
					}else if(userMsg.getMsgtype().equals(Constant.MSG_FLOWER_TYPE)){
						//拼接 3 送花消息List
						userMsg = msgFlowerAndDiamond(userMsg);
						String remark = Constant.MSG_FLOWER_MODEL.replace("n", userMsg.getNum().toString());
						userMsg.setRemark(remark);
					}else{
						//拼接  5:送钻石消息List
						userMsg = msgFlowerAndDiamond(userMsg);
						String remark = Constant.MSG_DIAMOND_MODEL.replace("n", userMsg.getNum().toString());
						userMsg.setRemark(remark);
					}
					//初始化消息中用户信息----friendid
					initMsgUserInfoByFriendid(userMsg);
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_28, Constant.RTNINFO_SYS_28);
			}
			reseResp.setExpandData(expandData);
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectExceptList userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 拼接获取点赞消息记录展示字段List
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private UserMsg msgFlowerAndDiamond(UserMsg userMsg){
		//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步送花消息
		likeMsg(userMsg);
		return userMsg;
	}
	
	/**
	 * @author yinxc
	 * 拼接获取点赞消息记录展示字段List
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private UserMsg msgComment(long userid, UserMsg userMsg){
		//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步点赞消息
		commentMsg(userMsg);
		return userMsg;
	}
	
	/**
	 * @author yinxc
	 * gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对评论消息
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void commentMsg(UserMsg userMsg){
		//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中      
		if("0".equals(userMsg.getGtype())){
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE,null,null);
			impItype(improve, userMsg);
		}else if("1".equals(userMsg.getGtype())){
			//1 目标中  进步评论消息
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE_GOAL,null,null);
			impItype(improve, userMsg);
		}else if("2".equals(userMsg.getGtype())){
			//2 榜中   评论消息   获取榜图片
			userMsg = impRankItype(userMsg, userMsg.getGtype());
		}else if("3".equals(userMsg.getGtype())){
			//3圈子中      评论消息   获取圈子图片
			userMsg = impRankItype(userMsg, userMsg.getGtype());
		}else{
			//4 教室中   评论消息    获取教室图片
			userMsg = impRankItype(userMsg, userMsg.getGtype());
		}
	}
	
	/**
	 * @author yinxc
	 * 拼接---获取榜,圈子,教室评论图片
	 * gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中 
	 * 2017年2月10日
	 * void
	 * UserMsgServiceImpl
	 */
	private UserMsg impRankItype(UserMsg userMsg, String gtype){
		if("2".equals(gtype)){
			//榜评论图片
			Rank rank = rankMapper.selectByPrimaryKey(userMsg.getSnsid());
			if(null != rank){
				userMsg.setImpPicFilekey(rank.getRankphotos());
			}
		}
		if("3".equals(gtype)){
			//圈子评论图片
			Circle circle = circleMapper.selectByPrimaryKey(userMsg.getSnsid());
			if(null != circle){
				userMsg.setImpPicFilekey(circle.getCirclephotos());
			}
		}
		if("4".equals(gtype)){
			//教室评论图片
			Classroom classroom = classroomMapper.selectByPrimaryKey(userMsg.getSnsid());
			if(null != classroom){
				userMsg.setImpPicFilekey(classroom.getClassphotos());
			}
		}
		return userMsg;
	}

	/**
	 * @author yinxc
	 * 根据msgtype获取消息列表信息(对话消息)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * return_type
	 */
	@Override
	public BaseResp<Object> selectLikeList(long userid, String msgtype, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMsg> list = userMsgMapper.selectLikeList(userid, msgtype, startNum, endNum);
			if (null != list && list.size()>0) {
				if(msgtype.equals(Constant.MSG_LIKE_TYPE)){
					//2 点赞    拼接获取点赞消息记录展示字段List
					list = msgLike(userid, list);
				}
				if(msgtype.equals(Constant.MSG_FANS_TYPE)){
					//5:粉丝   拼接获取粉丝消息记录展示字段List
					//获取粉丝列表
					List<SnsFans> fanslist = snsFansMapper.selectFansByUserid(userid, startNum, endNum);
					fanslists(fanslist, userid, msgtype);
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_28, Constant.RTNINFO_SYS_28);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectLikeList userid = {}, msg = {}", userid, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 5:粉丝   拼接获取粉丝消息记录展示字段List
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private List<SnsFans> fanslists(List<SnsFans> fanslist, long userid, String msgtype){
		//拼接id  List集合-----未读的粉丝消息
		List<String> idList = userMsgMapper.selectIdByMsgtypeList(userid, msgtype);
		if(null != fanslist && fanslist.size()>0){
			for (SnsFans snsFans : fanslist) {
				if(idList.contains(snsFans.getLikeuserid())){
					//含有    未读
					snsFans.setIsread("0");
				}
				AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(snsFans.getLikeuserid()));
				if(null != appUserMongoEntity){
					
				}
				//判断当前用户是否已关注
				SnsFans fans = snsFansMapper.selectByUidAndLikeid(snsFans.getLikeuserid(), userid);
				if(null != fans){
					snsFans.setIsfriend("1");
				}
				//判断已关注者是否是好友关系
				SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userid, snsFans.getLikeuserid());
				if(null != snsFriends){
					snsFans.setIsfriend("1");
				}
			}
		}
		return fanslist;
	}
	
	/**
	 * @author yinxc
	 * 拼接获取点赞消息记录展示字段List
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private List<UserMsg> msgLike(long userid, List<UserMsg> list){
		List<UserMsg> msgList = new ArrayList<UserMsg>();
		for (UserMsg userMsg : list) {
			//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步点赞消息
			likeMsg(userMsg);
			//初始化消息中用户信息----friendid
			initMsgUserInfoByFriendid(userMsg);
			userMsg.setRemark(Constant.MSG_LIKE_MODEL);
		}
		return msgList;
	}
	
	/**
	 * @author yinxc
	 * gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步点赞消息
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void likeMsg(UserMsg userMsg){
		//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步点赞消息
		if("0".equals(userMsg.getGtype())){
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE,null,null);
			impItype(improve, userMsg);
		}else if("1".equals(userMsg.getGtype())){
			//1 目标中  进步点赞消息
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE_GOAL,null,null);
			impItype(improve, userMsg);
		}else if("2".equals(userMsg.getGtype())){
			//2 榜中   进步点赞消息
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE_RANK,null,null);
			impItype(improve, userMsg);
		}else if("3".equals(userMsg.getGtype())){
			//3圈子中      进步点赞消息
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE_CIRCLE,null,null);
			impItype(improve, userMsg);
		}else{
			//4 教室中   进步点赞消息
			Improve improve = improveMapper.selectByPrimaryKey(userMsg.getSnsid(), Constant_table.IMPROVE_CLASSROOM,null,null);
			impItype(improve, userMsg);
		}
	}
	
	/**
	 * @author yinxc
	 * itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void impItype(Improve improve, UserMsg userMsg){
		if(null != improve){
			//itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
			if("0".equals(improve.getItype())){
				//0 文字进步   brief --- 说明
				userMsg.setImpPicFilekey(improve.getBrief());
			}else if("1".equals(improve.getItype())){
				//1 图片进步   pickey --- 图片的key
				userMsg.setImpPicFilekey(improve.getPickey());
			}else{
				//2 视频进步 3 音频进步 4 文件    filekey --- 文件key  视频文件  音频文件 普通文件
				userMsg.setImpPicFilekey(improve.getFilekey());
			}
			userMsg.setImpItype(improve.getItype());
		}
	}

	/**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同类型消息Count(对话消息-----已读，未读消息)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type
	 */
	@Override
	public int selectCountByType(long userid, String mtype, String msgtype, String isread) {
		int temp = 0;
		try {
			temp = userMsgMapper.selectCountByType(userid, mtype, msgtype, isread);
		} catch (Exception e) {
			logger.error("selectCountByType userid = {}, mtype = {}, msg = {}", userid, mtype, e);
		}
		return temp;
	}

	@Override
	public List<String> selectIdByMsgtypeList(long userid, String msgtype) {
		List<String> list = userMsgMapper.selectIdByMsgtypeList(userid, msgtype);
		return list;
	}

	//------------------------公用方法，初始化消息中用户信息------------------------------------------
	/**
     * 初始化消息中用户信息 ------Friendid
     */
    private void initMsgUserInfoByFriendid(UserMsg userMsg){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(userMsg.getFriendid()));
        userMsg.setAppUserMongoEntityFriendid(appUserMongoEntity);
    }

    /**
     * 初始化消息中用户信息 ------Userid
     */
//    private void initMsgUserInfoByUserid(UserMsg userMsg){
//        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(userMsg.getUserid()));
//        userMsg.setAppUserMongoEntityUserid(appUserMongoEntity);
//    }

}
