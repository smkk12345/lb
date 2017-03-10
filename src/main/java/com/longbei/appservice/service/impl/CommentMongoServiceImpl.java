package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CommentCountMongoDao;
import com.longbei.appservice.dao.CommentLikesMongoDao;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.CommentMongoDao;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.Comment;
import com.longbei.appservice.entity.CommentCount;
import com.longbei.appservice.entity.CommentLikes;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.service.CommentMongoService;
import com.longbei.appservice.service.UserBehaviourService;

import net.sf.json.JSONObject;

@Service("commentMongoService")
public class CommentMongoServiceImpl implements CommentMongoService {

	@Autowired
	private CommentMongoDao commentMongoDao;
	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	@Autowired
	private CommentCountMongoDao commentCountMongoDao;
	@Autowired
	private CommentLikesMongoDao commentLikesMongoDao;
	@Autowired
	private UserMsgMapper userMsgMapper;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserBehaviourService userBehaviourService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	private static Logger logger = LoggerFactory.getLogger(CommentMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertComment(Comment comment) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(comment);
//			comment = commentMongoDao.selectCommentByItypeid(comment.getItypeid(), comment.getItype());
			//添加评论总数
			insertCount(comment);
			//添加评论消息
			insertMsg(comment);
			
			//添加评论---    +积分
			//获取十全十美类型---社交
			String pType = SysRulesCache.perfectTenMap.get(2);
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(comment.getUserid()));//此处通过id获取用户信息
			reseResp.setData(comment);
			userBehaviourService.pointChange(userInfo, "DAILY_COMMENT", pType, null,0,0);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertComment comment = {}", JSONArray.toJSON(comment).toString(), e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 添加评论消息
	 * 2017年2月10日
	 * return_type
	 * CommentMongoServiceImpl
	 */
	private void insertMsg(Comment comment){
		UserMsg record = new UserMsg();
		record.setUserid(Long.valueOf(comment.getFriendid()));
		record.setCreatetime(new Date());
		record.setFriendid(Long.valueOf(comment.getUserid()));
		record.setGtype(comment.getBusinesstype());
		//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
		record.setMsgtype("1");
		record.setSnsid(Long.valueOf(comment.getBusinessid()));
		record.setRemark(comment.getContent());
		record.setIsdel("0");
		record.setIsread("0");
		// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
		// 送花 4 送钻石  5:粉丝  等等)
		record.setMtype("1");
		try {
			userMsgMapper.insertSelective(record);
		} catch (Exception e) {
			logger.error("insertMsg record = {}", JSONObject.fromObject(record).toString(), e);
		}
	}
	
	private void insert(Comment comment){
		try {
			commentMongoDao.insertComment(comment);
		} catch (Exception e) {
			logger.error("insert comment = {}", JSONObject.fromObject(comment).toString(), e);
		}
	}
	
	private void insertCount(Comment comment){
		CommentCount commentCount = new CommentCount();
		commentCount.setComcount(1);
		commentCount.setCreatetime(DateUtils.formatDateTime1(new Date()));
		commentCount.setLikes(0);
		commentCount.setCommentid(comment.getId());
		try {
			commentCountMongoDao.insertCommentCount(commentCount);
		} catch (Exception e) {
			logger.error("insertCount commentCount = {}", JSONObject.fromObject(commentCount).toString(), e);
		}
	}

	@Override
	public BaseResp<Object> selectCommentListByItypeid(String businessid, String businesstype, int startNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(businessid, businesstype, startNo, pageSize);
//			String commentids = "";
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					initCommentUserInfoByUserid(comment);
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					comment.setLowerList(lowers);
//					commentids += comment.getId() + ",";
				}
				//热门评论     点赞和热门评论暂时不做
//					//获取热门评论   点赞数最高的5个
//					if(commentids.length()>0){
//						commentids = commentids.substring(0, commentids.length()-1);
//						List<CommentCount> countList = commentCountMongoDao.selectCommentCountListByCommentids(commentids);
//						expandData.put("hotComment", countList);
//					}
				reseResp.setData(list);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_21, Constant.RTNINFO_SYS_21);
			}
		} catch (Exception e) {
			logger.error("selectCommentListByItypeid businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> selectCommentListByItypeidAndFriendid(String friendid, String businessid, String businesstype,
			int startNo, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(businessid, businesstype, startNo, pageSize);
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					//初始化用户信息
					initCommentUserInfoByUserid(comment);
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					//初始化用户信息
					initCommentLowerUserInfoList(lowers);
					comment.setLowerList(lowers);
					//判断是否点赞
//					CommentLikes commentLikes = commentLikesMongoDao.selectCommentLikesByCommentid(comment.getId(), friendid);
//					if(null != commentLikes){
//						comment.setIsaddlike("1");
//					}
				}
				reseResp.setData(list);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_21, Constant.RTNINFO_SYS_21);
			}
		} catch (Exception e) {
			logger.error("selectCommentListByItypeidAndFriendid businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> selectCommentHotListByItypeidAndFid(String friendid, String businessid, String businesstype) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentByItypeid(businessid, businesstype);
			String commentids = "";
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					comment.setLowerList(lowers);
					commentids += comment.getId() + ",";
				}
				//获取热门评论   点赞数最高的5个
				//只要圈子评论才有热门评论     点赞和热门评论暂时不做  02-04
				List<CommentCount> countList = null;
				List<Comment> commentList = new ArrayList<Comment>();
				if(commentids.length()>0){
					commentids = commentids.substring(0, commentids.length()-1);
					countList = commentCountMongoDao.selectCommentCountListByCommentids(commentids);
				}
				//获取5条热门主评论信息
				if(null != countList && countList.size()>0){
					for (CommentCount commentCount : countList) {
						Comment comment = commentMongoDao.selectCommentByid(commentCount.getCommentid());
						List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(commentCount.getCommentid());
						comment.setLowerList(lowers);
						//判断是否点赞
						CommentLikes commentLikes = commentLikesMongoDao.selectCommentLikesByCommentid(comment.getId(), friendid);
						if(null != commentLikes){
							comment.setIsaddlike("1");
						}
						commentList.add(comment);
					}
				}
				reseResp.setData(commentList);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_21, Constant.RTNINFO_SYS_21);
			}
		} catch (Exception e) {
			logger.error("selectCommentListByItypeidAndFriendid businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return reseResp;
	}

	@Override
	public Comment selectCommentByid(String id) {
		Comment comment = null;
		try {
			comment = commentMongoDao.selectCommentByid(id);
		} catch (Exception e) {
			logger.error("selectCommentByid id = {}", id, e);
		}
		return comment;
	}

	@Override
	public BaseResp<Object> deleteComment(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//删除其他的信息，评论总数及子评论信息
			deleteCommentOther(id);
			//后删主评论信息
			commentMongoDao.deleteComment(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteComment id = {}", id, e);
		}
		return reseResp;
	}
	
	private void deleteCommentOther(String commentid){
		//删除子评论信息
		commentLowerMongoDao.deleteLowerByCommentid(commentid);
		//删除主评论条数
		commentCountMongoDao.deleteCommentCountByCommentid(commentid);
	}

	@Override
	public BaseResp<Integer> selectCommentCountSum(String businessid, String businesstype) {
		BaseResp<Integer> reseResp = new BaseResp<Integer>();
		try {
			List<Comment> list = commentMongoDao.selectCommentByItypeid(businessid, businesstype);
			//获取评论总数
			int zong = 0;
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					CommentCount commentCount = commentCountMongoDao.selectCommentCountByCommentid(comment.getId());
					zong = zong + commentCount.getComcount();
				}
			}
			reseResp.setData(zong);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectCommentCountSum businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return reseResp;
	}
	
	//------------------------公用方法，初始化消息中用户信息------------------------------------------
    /**
     * 初始化消息中用户信息 ------List
     */
    private void initCommentLowerUserInfoList(List<CommentLower> lowers){
    	if(null != lowers && lowers.size()>0){
    		for (CommentLower commentLower : lowers) {
    			if(!StringUtils.hasBlankParams(commentLower.getFriendid())){
    				AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(commentLower.getFriendid()));
        	        commentLower.setAppUserMongoEntityFriendid(appUserMongoEntity);
    			}
    	        AppUserMongoEntity appUserMongo = userMongoDao.getAppUser(String.valueOf(commentLower.getUserid()));
    	        commentLower.setAppUserMongoEntityUserid(appUserMongo);
			}
    	}
        
    }
    
    /**
     * 初始化消息中用户信息 ------Userid
     */
    private void initCommentUserInfoByUserid(Comment comment){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(comment.getUserid()));
        comment.setAppUserMongoEntityUserid(appUserMongoEntity);
    }

}
