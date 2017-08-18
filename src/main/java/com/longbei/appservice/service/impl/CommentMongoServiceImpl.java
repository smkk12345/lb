package com.longbei.appservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant_Perfect;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CommentCountMongoDao;
import com.longbei.appservice.dao.CommentLikesMongoDao;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.CommentMongoDao;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;

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
	private UserMsgService userMsgService;
	@Autowired
	private UserMongoDao userMongoDao;
	@Autowired
	private UserBehaviourService userBehaviourService;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserRelationService userRelationService;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private ImproveService improveService;
	
	private static Logger logger = LoggerFactory.getLogger(CommentMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertComment(final Comment comment) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(comment);
//			comment = commentMongoDao.selectCommentByItypeid(comment.getItypeid(), comment.getItype());
			//添加评论总数
			insertCount(comment);
			//businesstype 类型    0 零散进步评论   1 目标进步评论    2 榜中微进步评论  3圈子中微进步评论 4 教室中微进步评论
			// 					10：榜评论  11 圈子评论  12 教室评论
			if(!"10".equals(comment.getBusinesstype()) && !"11".equals(comment.getBusinesstype()) 
					&& !"12".equals(comment.getBusinesstype())){
				//添加评论消息
				//msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
				//gtype 0:零散 1:目标中 2:榜中微进步  3:圈子中微进步 4.教室中微进步  5:龙群  6:龙级  7:订单  8:认证 9：系统 
				//10：榜中  11 圈子中  12 教室中  13:教室批复作业
				if(!comment.getUserid().equals(comment.getFriendid())){
					userMsgService.insertMsg(comment.getUserid(), comment.getFriendid(), 
							comment.getImpid(), comment.getBusinesstype(), 
							comment.getBusinessid(), comment.getContent(), "1", "1", "评论", 0, comment.getId(), "");
				}

				final String impid = comment.getImpid();
				//24小时热门进步
				if (!StringUtils.isBlank(impid)){
					threadPoolTaskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							Improve improve = improveService.selectImproveByImpid(Long.parseLong(impid),null,
									comment.getBusinesstype(),comment.getBusinessid());
							Comment comment1 = commentMongoDao.selectByUseridAndImpid(impid,comment.getUserid());
							if (null == comment1){
								improveService.toDoHotImprove(improve,comment.getBusinessid(),comment.getBusinesstype(),
										1 * SysRulesCache.behaviorRule.getCommentscore());
							}
						}
					});
				}



//				insertMsg(comment);
			}

			//添加评论---    +积分
			//获取十全十美类型---社交
//			String pType = SysRulesCache.perfectTenMap.get(2);
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(Long.parseLong(comment.getUserid()));//此处通过id获取用户信息
			initCommentUserInfoByUserid(comment, comment.getFriendid());
			reseResp.setData(comment);
			userBehaviourService.pointChange(userInfo, "DAILY_COMMENT",Constant_Perfect.PERFECT_GAM, null,0,0);
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
//	private void insertMsg(Comment comment){
//		UserMsg record = new UserMsg();
//		if(!StringUtils.isBlank(comment.getFriendid())){
//			record.setUserid(Long.valueOf(comment.getFriendid()));
//		}
//		record.setCreatetime(new Date());
//		record.setFriendid(Long.valueOf(comment.getUserid()));
//		record.setGtype(comment.getBusinesstype());
//		//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
//		record.setMsgtype("1");
//		record.setSnsid(Long.valueOf(comment.getBusinessid()));
//		record.setRemark(comment.getContent());
//		record.setIsdel("0");
//		record.setIsread("0");
//		// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
//		// 送花 4 送钻石  5:粉丝  等等)
//		record.setMtype("1");
//		try {
//			userMsgMapper.insertSelective(record);
//		} catch (Exception e) {
//			logger.error("insertMsg record = {}", JSONObject.fromObject(record).toString(), e);
//		}
//	}
	
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
		commentCount.setCreatetime(new Date());
		commentCount.setLikes(0);
		commentCount.setCommentid(comment.getId());
		try {
			commentCountMongoDao.insertCommentCount(commentCount);
		} catch (Exception e) {
			logger.error("insertCount commentCount = {}", JSONObject.fromObject(commentCount).toString(), e);
		}
	}

	@Override
	public BaseResp<Object> selectCommentListByItypeid(String businessid, String businesstype, Date lastdate, int pageSize) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(null,businessid, businesstype, lastdate, pageSize);
//			String commentids = "";
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					initCommentUserInfoByUserids(comment);
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
	public BaseResp<List<Comment>> selectCommentListByItypeidAndFriendid(String friendid, String businessid, String businesstype, 
			String impid,
			Date lastdate, int pageSize) {
		BaseResp<List<Comment>> reseResp = new BaseResp<>();
		Map<String, Object> expandData = new HashMap<>();
		int commentNum = 0;
		try {
			List<Comment> list = commentMongoDao.selectCommentListByItypeid(impid,businessid, businesstype, lastdate, pageSize);
			if(null != list && list.size()>0){
				for (Comment comment : list) {
					//初始化用户信息
					initCommentUserInfoByUserid(comment, friendid);
					List<CommentLower> lowers = commentLowerMongoDao.selectCommentLowerListByCommentid(comment.getId());
					//初始化用户信息
					initCommentLowerUserInfoList(lowers, friendid);
					comment.setLowerList(lowers);
					
					//判断是否点赞
//					CommentLikes commentLikes = commentLikesMongoDao.selectCommentLikesByCommentid(comment.getId(), friendid);
//					if(null != commentLikes){
//						comment.setIsaddlike("1");
//					}
				}
				BaseResp<Integer> resp = selectCommentCountSum(businessid, businesstype, impid);
				//获取评论总数
				if (ResultUtil.isSuccess(resp)){
					commentNum = resp.getData();
		        }
				reseResp.setData(list);
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.setData(new ArrayList<Comment>());
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_21);
			}
			expandData.put("commentNum", commentNum);
			reseResp.setExpandData(expandData);
		} catch (Exception e) {
			logger.error("selectCommentListByItypeidAndFriendid businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return reseResp;
	}
	
	@Override
	public BaseResp<Object> selectCommentHotListByItypeidAndFid(String friendid, String businessid, 
			String businesstype, String impid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<Comment> list = commentMongoDao.selectCommentByItypeid(businessid, businesstype, impid);
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
				reseResp.setData(new ArrayList<Comment>());
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_21);
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
			Comment comment = commentMongoDao.selectCommentByid(id);
			if(null != comment){
				//删除评论消息
				userMsgService.deleteCommentMsg(comment.getImpid(), comment.getBusinesstype(), 
						comment.getBusinessid(), comment.getId(), null);
			}
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
	public BaseResp<Integer> selectCommentCountSum(String businessid, String businesstype, String impid) {
		BaseResp<Integer> reseResp = new BaseResp<Integer>();
		try {
			List<Comment> list = commentMongoDao.selectCommentByItypeid(businessid, businesstype, impid);
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
     * 初始化消息中用户信息 ------Userid
     */
    private void initCommentUserInfoByUserid(Comment comment, String friendid){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(comment.getUserid()));
        if(null != appUserMongoEntity){
			if(StringUtils.isNotEmpty(friendid)){
				this.userRelationService.updateFriendRemark(friendid,appUserMongoEntity);
			}
        	comment.setAppUserMongoEntityUserid(appUserMongoEntity);
        }else{
        	comment.setAppUserMongoEntityUserid(new AppUserMongoEntity());
        }
    }
    
    private void initCommentUserInfoByUserids(Comment comment){
    	AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(comment.getUserid()));
    	comment.setAppUserMongoEntityUserid(appUserMongoEntity);
    }

}
