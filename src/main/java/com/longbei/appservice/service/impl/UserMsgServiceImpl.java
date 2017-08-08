package com.longbei.appservice.service.impl;

import java.util.*;

import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.mongo.dao.FriendMongoDao;
import com.longbei.appservice.dao.mongo.dao.MsgRedMongDao;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.ImproveService;
import com.longbei.appservice.service.UserRelationService;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CircleMapper;
import com.longbei.appservice.dao.ClassroomMapper;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.HotLineMongoDao;
import com.longbei.appservice.dao.RankMapper;
import com.longbei.appservice.dao.UserCardMapper;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.service.UserMsgService;
import com.longbei.appservice.service.UserSettingCommonService;

@Service("userMsgService")
public class UserMsgServiceImpl implements UserMsgService {

	@Autowired
	private UserMsgMapper userMsgMapper;
	@Autowired
	private ImproveService improveService;
	@Autowired
	private UserMongoDao userMongoDao;
//	@Autowired
//	private SnsFansMapper snsFansMapper;
//	@Autowired
//	private SnsFriendsMapper snsFriendsMapper;
	@Autowired
	private RankMapper rankMapper;
	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private ClassroomMapper classroomMapper;
	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	@Autowired
	private FriendMongoDao friendMongoDao;
	@Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private UserSettingCommonService userSettingCommonService;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private HotLineMongoDao hotLineMongoDao;
	@Autowired
	private UserCardMapper userCardMapper;
	@Autowired
	private MsgRedMongDao msgRedMongDao;
	
	private static Logger logger = LoggerFactory.getLogger(UserMsgServiceImpl.class);
	
	
	
	/**
	 * @author yinxc
	 * 添加消息封装
	 * @param userid 消息推送者id
	 * @param friendid 消息接受者id
	 * @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知
	 *									22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	 *									25:订单发货N天后自动确认收货    26：实名认证审核结果
	 *									27:工作认证审核结果      28：学历认证审核结果
	 *									29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	 *									32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙杯名人认证 54: 教室成员退出教室 55：转让群主权限 56：被授予龙杯Star认证
	 *									40：订单已取消 41 榜中进步下榜)
	 *				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3  送花 4 送钻石  5:粉丝  等等)
	 *				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
	 *						14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
	 * 2017年4月26日
	 */
	@Override
	public BaseResp<Object> insertMsg(String userid, String friendid, String impid, String businesstype,
			String businessid, String remark, String mtype, String msgtype, String title, int num, 
			String commentid, String commentlowerid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		if (StringUtils.isBlank(friendid)){
			return reseResp;
		}
		try {
			UserMsg record = new UserMsg();
			if(!StringUtils.isBlank(friendid)){
				record.setUserid(Long.valueOf(friendid));
			}
			record.setCreatetime(new Date());
			record.setFriendid(Long.valueOf(userid));
			record.setGtype(businesstype);
			//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
			record.setMsgtype(msgtype);
			if(!StringUtils.isBlank(businessid)){
				record.setGtypeid(Long.valueOf(businessid));
			}else{
				record.setGtypeid(0l);
			}
			if(!StringUtils.isBlank(impid)){
				record.setSnsid(Long.valueOf(impid));
			}else{
				record.setSnsid(0l);
			}
			record.setRemark(remark);
			record.setIsdel("0");
			record.setIsread("0");
			record.setNum(num);
			record.setTitle(title);
			record.setCommentid(commentid);
			record.setCommentlowerid(commentlowerid);
			// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
			// 送花 4 送钻石  5:粉丝  等等)
			record.setMtype(mtype);
			try {
				userMsgMapper.insertSelective(record);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			} catch (Exception e) {
				logger.error("insertMsg record = {}", JSONObject.fromObject(record).toString(), e);
			}
		} catch (Exception e) {
			logger.error("insertMsg userid = {}", userid, e);
		}
		return reseResp;
	}

	@Override
	public BaseResp<Object> sendMessagesBatch(String friendid, String[] userids, String businesstype,
									  String businessid, String remark, String title) {
		BaseResp<Object> reseResp = new BaseResp<>();
		if(null == userids){
			return reseResp;
		}
		try {
			List<UserMsg> userMsgs = new ArrayList<>();
			for(int i=0;i<userids.length;i++){
				UserMsg record = new UserMsg();
				if(!StringUtils.isBlank(userids[i])){
					record.setUserid(Long.valueOf(userids[i]));//消息接收者
				}
				record.setFriendid(Long.valueOf(friendid));//消息发送者
				record.setGtype("10");//榜中
				if(!StringUtils.isBlank(businessid)){
					record.setGtypeid(Long.parseLong(businessid));
				}
				record.setRemark(remark);
				record.setTitle(title);
				record.setMtype("2");//mtype:@我消息-2
				record.setMsgtype(businesstype);//msgtype
				record.setIsdel("0");//未删除-0
				record.setIsread("0");//未读-0
				record.setCreatetime(new Date());
				userMsgs.add(record);
			}

			try {
				userMsgMapper.insertSelectiveBatch(userMsgs);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			} catch (Exception e) {
				logger.error("insertSelectiveBatch", e);
			}
		} catch (Exception e) {
			logger.error("sendMessagesBatch error ", e);
		}
		return reseResp;
	}


	@Override
	public BaseResp<Object> insertMsg(String userid, String friendid, String impid, String businesstype,
									  String businessid, String remark, String mtype, String msgtype, String title, int num,
									  String commentid, String commentlowerid,String href, String notice) {
		BaseResp<Object> reseResp = new BaseResp<>();
		if (StringUtils.isBlank(friendid)){
			return reseResp;
		}
		try {
			UserMsg record = new UserMsg();
			if(!StringUtils.isBlank(friendid)){
				record.setUserid(Long.valueOf(friendid));
			}
			record.setCreatetime(new Date());
			record.setFriendid(Long.valueOf(userid));
			record.setGtype(businesstype);
			//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
			record.setMsgtype(msgtype);
			if(!StringUtils.isBlank(businessid)){
				record.setGtypeid(Long.valueOf(businessid));
			}else{
				record.setGtypeid(0l);
			}
			if(!StringUtils.isBlank(impid)){
				record.setSnsid(Long.valueOf(impid));
			}else{
				record.setSnsid(0l);
			}
			record.setRemark(remark);
			record.setIsdel("0");
			record.setIsread("0");
			record.setNum(num);
			record.setTitle(title);
			record.setCommentid(commentid);
			record.setHref(href);
			record.setCommentlowerid(commentlowerid);
			// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
			// 送花 4 送钻石  5:粉丝  等等)
			record.setMtype(mtype);
			record.setNotice(notice);
			try {
				userMsgMapper.insertSelective(record);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			} catch (Exception e) {
				logger.error("insertMsg record = {}", JSONObject.fromObject(record).toString(), e);
			}
		} catch (Exception e) {
			logger.error("insertMsg userid = {}", userid, e);
		}
		return reseResp;
	}

	@Override
	public int deleteCommentMsg(String impid, String businesstype, String businessid,
			String commentid, String commentlowerid) {
		int temp = userMsgMapper.deleteCommentMsg(impid, businesstype, businessid, commentid, commentlowerid);
		return temp;
	}

	@Override
	public int deleteCommentMsgLike(String userid, String friendid) {
		int temp = userMsgMapper.deleteCommentMsgLike(userid, friendid);
		return temp;
	}
	
	/**
	 * 删除赞信息
	 */
	@Override
	public int deleteLikeCommentMsg(String impid, String businesstype, String businessid, String userid) {
		int temp = userMsgMapper.deleteLikeCommentMsg(impid, businesstype, businessid, userid);
		return temp;
	}


	/**
	 * 获取是否显示红点 0.不显示 1.显示红点
	 * @param userid
	 * @return
	 */
	@Override
	public int selectCountShowMyByMtype(long userid){
		try{
			Map<String,Object> resultMap = selectShowMyByMtype(userid);
			String res = resultMap.get("mycount").toString();
			if("true".equals(res)){
				return 1;
			}
			//判断邀请所获收益是否显示红点    0:不显示   1：显示
			MsgRed msgRed = msgRedMongDao.getMsgRed(String.valueOf(userid),"0","62");
			if (null != msgRed){
				return 1;
			}
		}catch (Exception e){
			logger.error("userid={}",userid,e);
		}
		return 0;
	}
	
	private Date getShowCommentDate(long userid, Date mymaxtime){
		//获取好友   粉丝ids
		Set<String> friendList = this.userRelationService.getFriendIds(userid);
		Set<String> fansList = this.userRelationService.getFansIds(userid);
		Set<String> slist = selectListid(userid, friendList, fansList);
		//获取评论消息List    对比是否有好友   粉丝的未读评论消息
		List<UserMsg> list = userMsgMapper.selectListByMtypeAndMsgtype(userid,
				Constant.MSG_DIALOGUE_TYPE, Constant.MSG_COMMENT_TYPE, "0");
		if(null != list && list.size()>0){
			for (UserMsg userMsg : list) {
				if(mymaxtime != null){
					if(userMsg.getCreatetime().getTime() > mymaxtime.getTime()){
						if(slist.contains(userMsg.getFriendid().toString())){
							//好友   粉丝    评论含有未读消息
							return userMsg.getCreatetime();
						}
					}
				}else{
					if(slist.contains(userMsg.getFriendid().toString())){
						//好友   粉丝    评论含有未读消息
						return userMsg.getCreatetime();
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @author yinxc
	 * 获取"我的"页面对话消息---红点是否显示
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type  0:不显示   1：显示
	 */
	@Override
	public Map<String,Object> selectShowMy(long userid) {
		//点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment(我同意接收到这些人的评论通知))
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			Map<String, Object> expandData = userSettingCommonService.selectMapByUserid(userid+"");
			List<String> msgTypeList = new ArrayList<String>();
			if(expandData.get("is_new_fans").toString().equals("1")){
				msgTypeList.add(Constant.MSG_FANS_TYPE);
			}
			if(expandData.get("is_like").toString().equals("1")){
				//点赞   打开提醒
				msgTypeList.add(Constant.MSG_LIKE_TYPE);
			}
			if(expandData.get("is_flower").toString().equals("1")){
				//献花   打开提醒
				msgTypeList.add(Constant.MSG_FLOWER_TYPE);
			}
			//判断邀请所获收益是否显示红点    0:不显示   1：显示
			MsgRed msgRed = msgRedMongDao.getMsgRed(String.valueOf(userid),"0","62");
			if (null != msgRed){
				resultMap.put("inviteMsg",1);
			} else {
				resultMap.put("inviteMsg",0);
			}
			Date mymaxDate = null;
			Date commentMaxDate = null;
			HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");
			if(null != hotLine && hotLine.getMymaxtime() != null){
				mymaxDate = hotLine.getMymaxtime();
			}
			if(expandData.get("is_comment").toString().equals("2")){
				//评论设置   打开提醒   ---所有人
				msgTypeList.add(Constant.MSG_COMMENT_TYPE);
			}else if(expandData.get("is_comment").toString().equals("1")){
				commentMaxDate = getShowCommentDate(userid, mymaxDate);
			}
			if(msgTypeList.size() == 0 && commentMaxDate == null){
				resultMap.put("mycount", 0);
				return resultMap;
			}else if(msgTypeList.size() == 0 && commentMaxDate != null){
				resultMap.put("mycount", 1);
				return resultMap;
			}

			Map<String,Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("userid",userid);
			parameterMap.put("mtype",Constant.MSG_DIALOGUE_TYPE);
			parameterMap.put("msgtypelist",msgTypeList);
			parameterMap.put("isread","0");
			parameterMap.put("mymaxdate", mymaxDate);
			resultMap = this.userMsgMapper.selectUserMsgCountByMsgTypeList(parameterMap);

			int count = resultMap.containsKey("count")?Integer.parseInt(resultMap.get("count").toString()):0;
			if(count > 0){
				resultMap.put("mycount", 1);
				return resultMap;
			}
			if(commentMaxDate == null){
				resultMap.remove("count");
				resultMap.remove("maxtime");
				if(count < 1){
					resultMap.put("mycount", 0);
				}else{
					resultMap.put("mycount", 1);
				}
				return resultMap;
			}else{
				resultMap.put("mycount", 1);
				return resultMap;
			}
			
		}catch(Exception e){
			logger.error("selectMapByUserid userid = {}", userid, e);
		}
		resultMap.put("mycount", 0);
		return resultMap;
	}
	
	/**
	 * @author yinxc
	 * 获取"我的"页面对话消息---红点是否显示------不用
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type  0:不显示   1：显示
	 */
	@Override
	public Map<String,Object> selectShowMyByMtype(long userid) {
		//键名称     新消息提醒    (新粉丝：is_new_fans
		//点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment(我同意接收到这些人的评论通知))
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			Map<String, Object> expandData = userSettingCommonService.selectMapByUserid(userid+"");
			List<String> msgTypeList = new ArrayList<String>();
			if(expandData.get("is_new_fans").toString().equals("1")){
				//粉丝   打开提醒
				msgTypeList.add(Constant.MSG_FANS_TYPE);
//				int temp = selectPublicList(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_FANS_TYPE, "0");
//				if(temp > 0){
//					return temp;
//				}
			}
			if(expandData.get("is_like").toString().equals("1")){
				//点赞   打开提醒
				msgTypeList.add(Constant.MSG_LIKE_TYPE);
//				int temp = selectPublicList(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_LIKE_TYPE, "0");
//				if(temp > 0){
//					return temp;
//				}
			}
			if(expandData.get("is_flower").toString().equals("1")){
				//献花   打开提醒
				msgTypeList.add(Constant.MSG_FLOWER_TYPE);
//				int temp = selectPublicList(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_FLOWER_TYPE, "0");
//				if(temp > 0){
//					return temp;
//				}
			}
//			if(expandData.get("is_diamond").toString().equals("1")){
//				//钻石   打开提醒
//				msgTypeList.add(Constant.MSG_DIAMOND_TYPE);
//				int temp = selectPublicList(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_DIAMOND_TYPE, "0");
//				if(temp > 0){
//					return temp;
//				}
//			}
			
			HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");

			Date commentMaxDate = null;//评论的最后消息时间
			// 评论设置:0:关闭  1：与我相关（好友、Like、熟人） 2：所有人
			if(expandData.get("is_comment").toString().equals("2")){
				//评论设置   打开提醒   ---所有人
				msgTypeList.add(Constant.MSG_COMMENT_TYPE);
//				int temp = selectPublicList(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_COMMENT_TYPE, "0");
//				if(temp > 0){
//					return temp;
//				}
			}else if(expandData.get("is_comment").toString().equals("1")){
				commentMaxDate = getShowComment(userid);
			}
			if(msgTypeList.size() == 0 && commentMaxDate == null){
				resultMap.put("mycount", false);
//				resultMap.put("mymaxtime",0);
				return resultMap;
			}else if(msgTypeList.size() == 0 && commentMaxDate != null){
				if(null != hotLine && hotLine.getMymaxtime() != null){
					if(hotLine.getMymaxtime().getTime() >= commentMaxDate.getTime()){
						resultMap.put("mycount", false);
					}else{
						resultMap.put("mycount", true);
					}
				}else{
					resultMap.put("mycount", true);
				}
//				resultMap.put("mymaxtime",commentMaxDate);
				return resultMap;
			}

			Map<String,Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("userid",userid);
			parameterMap.put("mtype",Constant.MSG_DIALOGUE_TYPE);
			parameterMap.put("msgtypelist",msgTypeList);
			parameterMap.put("isread","0");
			resultMap = this.userMsgMapper.selectUserMsgCountByMsgTypeList(parameterMap);

			int count = resultMap.containsKey("count")?Integer.parseInt(resultMap.get("count").toString()):0;
			if(commentMaxDate == null){
				resultMap.remove("count");
				resultMap.remove("maxtime");
				if(count < 1){
//					resultMap.put("mymaxtime",0);
					resultMap.put("mycount", false);
					return resultMap;
				}else{
					resultMap.put("mycount", true);
					return resultMap;
				}
			}else{
				if(null != hotLine && hotLine.getMymaxtime() != null){
					if(hotLine.getMymaxtime().getTime() >= commentMaxDate.getTime()){
						resultMap.put("mycount", false);
					}else{
						resultMap.put("mycount", true);
					}
				}else{
					resultMap.put("mycount", true);
				}
				
				if(count < 1){
					
					resultMap.put("mycount", true);
//					resultMap.put("mymaxtime", commentMaxDate.getTime());
					return resultMap;
				}
				
			}
			
			Date maxtime = DateUtils.formatDate(resultMap.get("mymaxtime").toString(),null);
			count ++;
			if(maxtime.getTime() > commentMaxDate.getTime()){
				resultMap.put("mymaxtime", commentMaxDate.getTime());
			}else{
				resultMap.put("mymaxtime", commentMaxDate.getTime());
			}
			resultMap.put("mycount",count);
			return resultMap;
		}catch (Exception e){
			logger.error("selectMapByUserid userid = {}", userid, e);
		}
		resultMap.put("mycount",0);
		resultMap.put("mymaxtime", 0);
		return resultMap;
	}
	
//	private int selectPublicList(long userid, String mtype, String msgtype, String isread){
//		List<UserMsg> list = userMsgMapper.selectListByMtypeAndMsgtype(userid, mtype, msgtype, isread);
//		if(null != list && list.size()>0){
//			return 1;
//		}
//		return 0;
//	}
	
	private Date getShowComment(long userid){
		//获取好友   粉丝ids
		Set<String> friendList = this.userRelationService.getFriendIds(userid);
		Set<String> fansList = this.userRelationService.getFansIds(userid);
		Set<String> slist = selectListid(userid, friendList, fansList);
		//获取评论消息List    对比是否有好友   粉丝的未读评论消息
		List<UserMsg> list = userMsgMapper.selectListByMtypeAndMsgtype(userid,
				Constant.MSG_DIALOGUE_TYPE, Constant.MSG_COMMENT_TYPE, "0");
		if(null != list && list.size()>0){
			for (UserMsg userMsg : list) {
				if(slist.contains(userMsg.getFriendid().toString())){
					//好友   粉丝    评论含有未读消息
					return userMsg.getCreatetime();
				}
			}
		}
		return null;
	}
	
	/**
	 * @author yinxc
	 * 读取拼接id List
	 * 2017年2月6日
	 * return_type list
	 */
	private Set<String> selectListid(long userid, Set<String> friendList, Set<String> fansList){
		friendList.addAll(fansList);
		return friendList;
//		friendList.addAll(fansList);
//		//通过HashSet剔除     删除ArrayList中重复元素
//		HashSet<String> h = new HashSet<String>(friendList);
//		friendList.clear();
//		friendList.addAll(h);
//		return friendList;
	}
	
	@Override
	public BaseResp<Object> deleteByid(Integer id, long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = delete(id, userid);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByid id = {}", id, e);
		}
		return reseResp;
	}
	
	private boolean delete(Integer id, long userid){
		int temp = userMsgMapper.deleteByPrimaryKey(id, userid);
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
			logger.error("deleteByUserid userid = {}", userid, e);
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
			logger.error("deleteByMtypeAndMsgtype userid = {}", userid, e);
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
			logger.error("deleteByUserid userid = {}", userid, e);
		}
		return reseResp;
	}
    
	private boolean deleteLikeUserid(long userid, String msgtype){
		int temp = userMsgMapper.deleteByLikeUserid(userid, msgtype);
		return temp > 0 ? true : false;
	}
	
	private boolean deleteUserid(long userid, String mtype){
		int temp = userMsgMapper.deleteByUserid(userid, mtype);
		return temp >= 0 ? true : false;
	}

	private BaseResp<Object> insertSelective(UserMsg record) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = insert(record);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("insertSelective record = {}", JSONArray.toJSON(record).toString(), e);
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
			//是否显示红点      mongo中当前数据修改---informmaxtime
			HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");
			HotLine line = new HotLine();
			line.setUserid(userid + "");
			line.setInformmaxtime(new Date());
			if(null != hotLine){
				//修改
				hotLineMongoDao.updateHotLine(line);
			}else{
				//添加
				hotLineMongoDao.insertHotLine(line);
			}
			
			
			List<UserMsg> list = userMsgMapper.selectByUserid(userid, startNum, endNum);
			if (startNum == 0 && list.size() == 0) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_28);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectByUserid userid = {}", userid, e);
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
			logger.error("updateByid record = {}", JSONArray.toJSON(record).toString(), e);
		}
		return reseResp;
	}
	
	private boolean update(UserMsg record){
		int temp = userMsgMapper.updateByPrimaryKeySelective(record);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsreadByid(Integer id, long userid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateId(id, userid);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsreadByid id = {}", id, e);
		}
		return reseResp;
	}
	
	private boolean updateId(Integer id, long userid){
		int temp = userMsgMapper.updateIsreadByid(id, userid);
		return temp > 0 ? true : false;
	}

	@Override
	public BaseResp<Object> updateIsreadByUserid(long userid, String mtype, String msgtype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<UserMsg> list = userMsgMapper.selectOtherList(userid, mtype, msgtype, 0, 1);
			if(null != list && list.size()>0){
				boolean temp = updateUserid(userid, mtype, msgtype);
				if (temp) {
					reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
				}
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateIsreadByUserid userid = {}", userid, e);
		}
		return reseResp;
	}

	/**
	 * 批量插入用户信息
	 * @param userIdList 接收人的用户Id
	 * @param userMsg 消息实体
     * @return
     */
	@Override
	public boolean batchInsertUserMsg(List<Long> userIdList, UserMsg userMsg) {
		if(userIdList == null || userIdList.size() == 0 || userMsg == null){
			return false;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userIdList",userIdList);
		map.put("userMsg",userMsg);
		int row = this.userMsgMapper.batchInsertUserMsg(map);
		if(row > 0){
			return true;
		}
		return false;
	}

	/**
	 * 批量插入消息
	 * @param userMsgList
	 * @return
     */
	@Override
	public int batchInsertUserMsg(List<UserMsg> userMsgList) {
		if(userMsgList == null || userMsgList.size() == 0){
			return 0;
		}
		return this.userMsgMapper.batchInsertUserMsgList(userMsgList);
	}

	@Override
	public UserMsg findCircleNoticeMsg(Long circleId, Long userId) {
		return this.userMsgMapper.findCircleNoticeMsg(circleId,userId);
	}

	/**
	 * 查看是否有同一个类型的信息
	 * @param userId 接收消息的用户id
	 * @param msgType 消息类型
	 * @param snsId 业务id
	 * @param gType
	 * @return
	 */
	@Override
	public int findSameTypeMessage(Long userId, String msgType, Long snsId, String gType) {
		return this.userMsgMapper.findSameTypeMessage(userId,msgType,snsId,gType);
	}

	/**
	 * 更改消息的已读状态
	 * @param userId 接受消息的用户id
	 * @param msgType 消息类型
	 * @param snsId 业务id
	 * @param gType
	 * @param remark 备注
	 * @param updateCreatetime 是否更新创建时间
	 * @return
	 */
	@Override
	public int updateUserMsgStatus(Long userId, String msgType, Long snsId, String gType,String remark,Boolean updateCreatetime) {
		return this.userMsgMapper.updateUserMsgStatus(userId,msgType,snsId,gType,remark,updateCreatetime);
	}

	/**
	 * 发送消息
	 * @param isOnly 消息是否要求唯一
	 * @param userId 接受消息 用户id
	 * @param friendId 发送消息
	 * @param mType 消息类型 0.系统消息 1.对话消息 2.@我消息
	 * @param msgType
	 * @param snsId 业务id
	 * @param remark 备注
	 * @param gType 0 零散 1 目标中 2 榜中 3圈子中 4 教室中 5.龙群
	 * @param updateCreatetime 如果存在该类型的消息,是否更新创建时间
     * @return
     */
	@Override
	public boolean sendMessage(boolean isOnly, Long userId, Long friendId,
							   String mType, String msgType, Long snsId, String remark, String gType,Boolean updateCreatetime) {
		if(isOnly){
			//先查询是否有该类型的 消息 根据接收人userid, msytype 业务id snsId
			int count = this.findSameTypeMessage(userId,msgType,snsId,gType);
			if(count > 0){
				//直接更改未读状态
				int updateRow = this.updateUserMsgStatus(userId,msgType,snsId,gType,remark,updateCreatetime);
				if(updateRow > 0){
					return true;
				}
			}
		}
		UserMsg userMsg = new UserMsg();
		userMsg.setUserid(userId);
		if(friendId != null){
			userMsg.setFriendid(friendId);
		}
		userMsg.setMsgtype(msgType);
		userMsg.setGtypeid(snsId);
		userMsg.setRemark(remark);
		userMsg.setGtype(gType);
		userMsg.setMtype(mType);
		userMsg.setCreatetime(new Date());
		userMsg.setIsdel("0");
		userMsg.setIsread("0");

		BaseResp<Object> insertResult = this.insertSelective(userMsg);
		if(insertResult.getCode() == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取添加好友的申请 消息数量和最大的createtime
	 * @param userid
	 * @return
     */
	@Override
	public Map<String, Object> selectAddFriendAskMsgDate(long userid) {
		Date friendAskmaxtime = null;
		HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");
		if(null != hotLine && hotLine.getFriendAskmaxtime() != null){
			friendAskmaxtime = hotLine.getFriendAskmaxtime();
		}
		List<FriendAddAsk> friendAddAskList = this.friendMongoDao.friendAddAskDateList(userid, friendAskmaxtime, false, null, 1);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int count = friendAddAskList != null?friendAddAskList.size():0;
		if(count > 0){
			resultMap.put("friendAskcount", 1);
		}else{
			resultMap.put("friendAskcount", 0);
		}
		return resultMap;
	}

	/**
	 * 获取添加好友的申请 消息数量和最大的createtime
	 * @param userid
	 * @return
     */
	@Override
	public Map<String, Object> selectAddFriendAskMsg(long userid) {
		List<FriendAddAsk> friendAddAskList = this.friendMongoDao.friendAddAskList(userid,false,null,null);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int count = friendAddAskList != null?friendAddAskList.size():0;
		Date maxtime = (friendAddAskList != null && friendAddAskList.size() > 0)?friendAddAskList.get(0).getCreateDate():null;
		resultMap.put("friendAskcount",count);
		if(null != maxtime){
//			resultMap.put("friendAskmaxtime",maxtime.getTime()/1000);
			resultMap.put("friendAskmaxtime",maxtime.getTime());
		}else{
			resultMap.put("friendAskmaxtime", 0);
		}
		return resultMap;
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
	 * 		 	14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  44: 榜中成员下榜)
	 */
	@Override
	public BaseResp<Object> selectOtherList(long userid, String mtype, String msgtype, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//是否显示红点      mongo中当前数据修改---rankmaxtime
			HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");
			HotLine line = new HotLine();
			line.setUserid(userid + "");
			line.setRankmaxtime(new Date());
			if(null != hotLine){
				//修改
				hotLineMongoDao.updateHotLine(line);
			}else{
				//添加
				hotLineMongoDao.insertHotLine(line);
			}
			
			List<UserMsg> list = userMsgMapper.selectOtherList(userid, mtype, msgtype, startNum, endNum);
			if (null != list && list.size()>0) {
				//拼接获取   对话消息---除赞消息,粉丝消息  消息记录展示字段List
				//2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问
				//					14:发布新公告   15:获奖   16:剔除  42.榜单公告更新   17:加入请求审批结果 
				//					34.加入的榜获奖通知---获奖消息  44: 榜中成员下榜   49:发布榜单审核未通过 50:个人定制榜邀请
				// 					51.入榜审核通过 52.入榜审核不通过  54: 教室成员退出教室  57:教室关闭    60:教室发布新公告
				//					61:教室疑答回复
				for (UserMsg userMsg : list) {
					if(!"15".equals(userMsg.getMsgtype()) && !"16".equals(userMsg.getMsgtype())
							&& !"17".equals(userMsg.getMsgtype()) && !"34".equals(userMsg.getMsgtype())){
						if("61".equals(userMsg.getMsgtype())){
							if(!StringUtils.isBlank(userMsg.getFriendid().toString())){
								//教室疑答回复---显示教室老师信息
								UserCard userCard = userCardMapper.selectByCardid(userMsg.getFriendid());
								AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
								appUserMongoEntity.setAvatar(userCard.getAvatar());
								appUserMongoEntity.setId(userCard.getUserid().toString());
								appUserMongoEntity.setUserid(userCard.getUserid().toString());
								appUserMongoEntity.setNickname(userCard.getDisplayname());
								userMsg.setAppUserMongoEntityFriendid(appUserMongoEntity);
							}
							
						}else{
							initMsgUserInfoByFriendid(userMsg, userid);
						}
					}else{
						AppUserMongoEntity appUserMongoEntity = new AppUserMongoEntity();
						if("44".equals(userMsg.getMsgtype())){
							//44: 榜中成员下榜     
							if(!"0".equals(userMsg.getFriendid()+"")){
								//如果是定制榜---显示榜主的信息
								initMsgUserInfoByFriendid(userMsg, userid);
							}else{
								appUserMongoEntity.setId(Constant.SQUARE_USER_ID);
								appUserMongoEntity.setUserid(Constant.SQUARE_USER_ID);
								appUserMongoEntity.setNickname(Constant.MSG_LONGBEI_NICKNAME);
								appUserMongoEntity.setAvatar(Constant.MSG_LONGBEI_DIFAULT_AVATAR);
								userMsg.setAppUserMongoEntityFriendid(appUserMongoEntity);
							}
						}else{
							//15:获奖   16:剔除   17:加入请求审批结果,通过或拒绝-----统一为龙杯公司推送的消息
							appUserMongoEntity.setId(Constant.SQUARE_USER_ID);
							appUserMongoEntity.setUserid(Constant.SQUARE_USER_ID);
							appUserMongoEntity.setNickname(Constant.MSG_LONGBEI_NICKNAME);
							appUserMongoEntity.setAvatar(Constant.MSG_LONGBEI_DIFAULT_AVATAR);
							userMsg.setAppUserMongoEntityFriendid(appUserMongoEntity);
						}
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_28);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectOtherList userid = {}, mtype = {}", userid, mtype, e);
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
	public BaseResp<Object> selectExceptList(final long userid, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		logger.info("select except list: userid={} startnum={}",userid,startNum);
		try {
			//是否显示红点      mongo中当前数据修改---mymaxtime
			HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");
			HotLine line = new HotLine();
			line.setUserid(userid + "");
			line.setMymaxtime(new Date());
			if(null != hotLine){
				//修改
				hotLineMongoDao.updateHotLine(line);
			}else{
				//添加
				hotLineMongoDao.insertHotLine(line);
			}
			
			final List<UserMsg> list = userMsgMapper.selectExceptList(userid, startNum, endNum);
			//key 新粉丝：is_new_fans  点赞:is_like
			Map<String, Object> expandData = userSettingCommonService.selectMapByUserid(userid+"");
			//0:关闭  1：开启
			int fansCount = 0;
			int likeCount = 0;
			if("1".equals(expandData.get("is_new_fans").toString())){
				//获取新粉丝count
				fansCount = userMsgMapper.selectCountByType(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_FANS_TYPE, "0");
			}
			if("1".equals(expandData.get("is_like").toString())){
				//获取点赞count
				likeCount = userMsgMapper.selectCountByType(userid, Constant.MSG_DIALOGUE_TYPE, Constant.MSG_LIKE_TYPE, "0");
			}
			expandData.put("fansCount", fansCount);
			expandData.put("likeCount", likeCount);
			
			if (null != list && list.size()>0) {
				//拼接获取   对话消息---除赞消息,粉丝消息  消息记录展示字段List
				//msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等
				for (UserMsg userMsg : list) {
					if(userMsg.getMsgtype().equals(Constant.MSG_COMMENT_TYPE)){
						//拼接 1 评论消息List
						msgComment(userid, userMsg);
						if(StringUtils.isBlank(userMsg.getRemark())){
							//获取评论内容
							CommentLower commentLower = commentLowerMongoDao.selectCommentLowerByid(Long.toString(userMsg.getSnsid()));
							if(null != commentLower){
								userMsg.setRemark(commentLower.getContent());
							}
						}
					}else if(userMsg.getMsgtype().equals(Constant.MSG_FLOWER_TYPE)){
						//拼接 3 送花消息List
						msgFlowerAndDiamond(userMsg);
//						String remark = Constant.MSG_FLOWER_MODEL.replace("n", userMsg.getNum().toString());
//						userMsg.setRemark(remark);
					}else{
						//拼接  5:送钻石消息List
						msgFlowerAndDiamond(userMsg);
//						String remark = Constant.MSG_DIAMOND_MODEL.replace("n", userMsg.getNum().toString());
//						userMsg.setRemark(remark);
					}
					//初始化消息中用户信息----friendid
					initMsgUserInfoByFriendid(userMsg, userid);
					
					//异步线程修改list消息为已读
					threadPoolTaskExecutor.execute(
	                    new Runnable() {
	                        @Override
	                        public void run() {
	                            for (UserMsg userMsg : list) {
	                            	userMsgMapper.updateIsreadByid(userMsg.getId(), userid);
								}
	                        }
	                    });
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
			else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_28);
			}
			reseResp.setExpandData(expandData);
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectExceptList userid = {}", userid, e);
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
	private void msgFlowerAndDiamond(UserMsg userMsg){
		//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步送花消息
		likeMsg(userMsg);
	}
	
	/**
	 * @author yinxc
	 * 拼接获取点赞消息记录展示字段List
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void msgComment(long userid, UserMsg userMsg){
		//gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步点赞消息
		commentMsg(userMsg);
	}
	
	/**
	 * @author yinxc
	 * gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对评论消息
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void commentMsg(UserMsg userMsg){
		//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
		if("0".equals(userMsg.getGtype())){
			Improve improve = improveService.selectImproveByImpidMuc(userMsg.getSnsid(),String.valueOf(userMsg.getUserid()),userMsg.getGtype(),String.valueOf(userMsg.getGtypeid()));
			if (null == improve){
				return;
			}
			userMsg.setImpPicFilekey(improveService.getFirstPhotos(improve));
			userMsg.setImpItype(improve.getItype());
		}else if("1".equals(userMsg.getGtype())){
			//1 目标中  进步评论消息
			Improve improve = improveService.selectImproveByImpidMuc(userMsg.getSnsid(),String.valueOf(userMsg.getUserid()),userMsg.getGtype(),String.valueOf(userMsg.getGtypeid()));
			userMsg.setImpPicFilekey(improveService.getFirstPhotos(improve));
			userMsg.setImpItype(improve.getItype());
		}else if("2".equals(userMsg.getGtype())){
			//2 榜中   进步点赞消息
			Improve improve = improveService.selectImproveByImpidMuc(userMsg.getSnsid(),String.valueOf(userMsg.getUserid()),userMsg.getGtype(),String.valueOf(userMsg.getGtypeid()));
			if(improve != null){
				userMsg.setImpPicFilekey(improveService.getFirstPhotos(improve));
				userMsg.setImpItype(improve.getItype());
			}
		}else if("3".equals(userMsg.getGtype())){
			//3圈子中      进步点赞消息
			Improve improve = improveService.selectImproveByImpidMuc(userMsg.getSnsid(),String.valueOf(userMsg.getUserid()),userMsg.getGtype(),String.valueOf(userMsg.getGtypeid()));
			if(improve != null){
				userMsg.setImpPicFilekey(improveService.getFirstPhotos(improve));
				userMsg.setImpItype(improve.getItype());
			}
		}else if("4".equals(userMsg.getGtype())){
			//4 教室中   进步点赞消息
			Improve improve = improveService.selectImproveByImpidMuc(userMsg.getSnsid(),String.valueOf(userMsg.getUserid()),userMsg.getGtype(),String.valueOf(userMsg.getGtypeid()));
			userMsg.setImpPicFilekey(improveService.getFirstPhotos(improve));
			userMsg.setImpItype(improve.getItype());
		}else if("10".equals(userMsg.getGtype())){
			//10 榜中   评论消息   获取榜图片
			userMsg = impRankItype(userMsg, userMsg.getGtype());
		}else if("11".equals(userMsg.getGtype())){
			//11 圈子中      评论消息   获取圈子图片
			userMsg = impRankItype(userMsg, userMsg.getGtype());
		}else{
			//12 教室中   评论消息    获取教室图片
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
		if("10".equals(gtype)){
			//榜评论图片
			Rank rank = rankMapper.selectByPrimaryKey(userMsg.getGtypeid());
			if(null != rank){
				userMsg.setImpPicFilekey(rank.getRankphotos());
			}
		}
		if("11".equals(gtype)){
			//圈子评论图片
			Circle circle = circleMapper.selectByPrimaryKey(userMsg.getGtypeid());
			if(null != circle){
				userMsg.setImpPicFilekey(circle.getCirclephotos());
			}
		}
		if("12".equals(gtype)){
			//教室评论图片
			Classroom classroom = classroomMapper.selectByPrimaryKey(userMsg.getGtypeid());
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
					msgLike(userid, list);
				}
				if(msgtype.equals(Constant.MSG_FANS_TYPE)){
					//5:粉丝   拼接获取粉丝消息记录展示字段List
					//获取粉丝列表
//					List<SnsFans> fanslist = snsFansMapper.selectFansByLikeUserid(userid, startNum, endNum);
//					fanslists(fanslist, userid, msgtype);
					for (UserMsg userMsg : list) {
						userMsg.setRemark("关注了您");
						//初始化消息中用户信息----friendid
						initMsgUserInfoByFriendid(userMsg, userid);
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_28);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectLikeList userid = {}", userid, e);
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
//	private List<SnsFans> fanslists(List<SnsFans> fanslist, long userid, String msgtype){
//		//拼接id  List集合-----未读的粉丝消息
//		List<String> idList = userMsgMapper.selectIdByMsgtypeList(userid, msgtype);
//		if(null != fanslist && fanslist.size()>0){
//			for (SnsFans snsFans : fanslist) {
//				if(idList.contains(snsFans.getLikeuserid())){
//					//含有    未读
//					snsFans.setIsread("0");
//				}
////				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(snsFans.getLikeuserid()));
////				if(null != appUserMongoEntity){
////
////				}
//				//判断当前用户是否已关注
//				snsFans.setIsfocus(this.userRelationService.checkIsFans(snsFans.getLikeuserid(),userid)?"1":"0");
//				//是否是好友
//				snsFans.setIsfriend(this.userRelationService.checkIsFriend(userid,snsFans.getLikeuserid())?"1":"0");
//
//			}
//		}
//		return fanslist;
//	}
	
	/**
	 * @author yinxc
	 * 拼接获取点赞消息记录展示字段List
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void msgLike(long userid, List<UserMsg> list){
		for (UserMsg userMsg : list) {
			//针对进步点赞消息
			//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
			//10：榜中  11 圈子中  12 教室中  13:教室批复作业
			likeMsg(userMsg);
			//初始化消息中用户信息----friendid
			initMsgUserInfoByFriendid(userMsg, userid);
			userMsg.setRemark(Constant.MSG_LIKE_MODEL);
		}
	}
	
	/**
	 * @author yinxc
	 * gtype 0 零散 1 目标中 2 榜中 3圈子中 4 教室中        针对进步点赞消息
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
	private void likeMsg(UserMsg userMsg){
		//针对进步点赞消息
		//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统
		//10：榜中  11 圈子中  12 教室中  13:教室批复作业
		Improve improve = improveService.selectImproveByImpid(userMsg.getSnsid(),String.valueOf(userMsg.getUserid()),userMsg.getGtype(),String.valueOf(userMsg.getGtypeid()));
		if(null != improve){
			userMsg.setImpPicFilekey(improveService.getFirstPhotos(improve));
			userMsg.setImpItype(improve.getItype());
		}
	}
	
	/**
	 * @author yinxc
	 * itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
	 * 2017年2月9日
	 * return_type
	 * UserMsgServiceImpl
	 */
//	private void impItype(Improve improve, UserMsg userMsg){
//		if(null != improve){
//			//itype类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
//			if("0".equals(improve.getItype())){
//				//0 文字进步   brief --- 说明
//				userMsg.setImpPicFilekey(improve.getBrief());
//			}else if("1".equals(improve.getItype())){
//				//1 图片进步   pickey --- 图片的key
//				userMsg.setImpPicFilekey(improve.getPickey());
//			}else{
//				//2 视频进步 3 音频进步 4 文件    filekey --- 文件key  视频文件  音频文件 普通文件
//				userMsg.setImpPicFilekey(improve.getFilekey());
//			}
//			userMsg.setImpItype(improve.getItype());
//		}
//	}

	/**
	 * @author yinxc
	 * 根据mtype,msgtype获取不同类型消息Count(对话消息-----已读，未读消息)
	 * 2017年2月8日
	 * mtype 0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	 * isread 可为null  查全部
	 * return_type
	 */
	@Override
	public Map<String,Object> selectCountByType(long userid, String mtype, String msgtype, String isread) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			HotLine hotLine = hotLineMongoDao.selectHotLineByUid(userid+"");
			if("0".equals(mtype)){
				Date informmaxtime = null;
				if(null != hotLine && hotLine.getInformmaxtime() != null){
					informmaxtime = hotLine.getInformmaxtime();
				}
				Map<String,Object> resultmap = userMsgMapper.selectCountAndMaxDatetimeByType(userid, mtype, msgtype, isread, informmaxtime);
				long count = 0;
				if(!resultmap.isEmpty()){
					count = (long)resultmap.get("count");
				}
				//通知消息
				if(count > 0){
					map.put("informcount", 1);
				}else{
					map.put("informcount", 0);
				}
			}
			if("2".equals(mtype)){
				//@我消息
				Date rankmaxtime = null;
				if(null != hotLine && hotLine.getRankmaxtime() != null){
					rankmaxtime = hotLine.getRankmaxtime();
				}
				Map<String,Object> resultmap = userMsgMapper.selectCountAndMaxDatetimeByType(userid, mtype, msgtype, isread, rankmaxtime);
				long count = 0;
				if(!resultmap.isEmpty()){
					count = (long)resultmap.get("count");
				}
				//通知消息
				if(count > 0){
					map.put("rankcount", 1);
				}else{
					map.put("rankcount", 0);
				}
//				map.put("rankcount", count);
//				if(resultmap.containsKey("maxtime")){
//					map.put("rankmaxtime", resultmap.get("maxtime"));
//				}else{
//					map.put("rankmaxtime", 0);
//				}
			}
		} catch (Exception e) {
			logger.error("selectCountByType userid = {}, mtype = {}", userid, mtype, e);
		}
		return map;
	}
	
	

	@Override
	public List<String> selectIdByMsgtypeList(long userid, String msgtype) {
		List<String> list = userMsgMapper.selectIdByMsgtypeList(userid, msgtype);
		return list;
	}
	

	@Override
	public int updateByPrimaryKeySelective(UserMsg record) {
		int temp = userMsgMapper.updateByPrimaryKeySelective(record);
		return temp;
	}

	//------------------------公用方法，初始化消息中用户信息------------------------------------------
	/**
     * 初始化消息中用户信息 ------Friendid
     */
    private void initMsgUserInfoByFriendid(UserMsg userMsg, long userid){
    	if(!StringUtils.hasBlankParams(userMsg.getFriendid().toString())){

    		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userMsg.getFriendid()));
			if(null != appUserMongoEntity){
				this.userRelationService.updateFriendRemark(userid,appUserMongoEntity);
				userMsg.setAppUserMongoEntityFriendid(appUserMongoEntity);
			}else{
				userMsg.setAppUserMongoEntityFriendid(new AppUserMongoEntity());
			}
    	}
    }

    /**
     * 初始化消息中用户信息 ------Userid
     */
//    private void initMsgUserInfoByUserid(UserMsg userMsg){
//        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(userMsg.getUserid()));
//        userMsg.setAppUserMongoEntityUserid(appUserMongoEntity);
//    }

}
