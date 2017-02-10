package com.longbei.appservice.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.CommentCountMongoDao;
import com.longbei.appservice.dao.CommentLowerMongoDao;
import com.longbei.appservice.dao.CommentMongoDao;
import com.longbei.appservice.dao.UserMsgMapper;
import com.longbei.appservice.entity.Comment;
import com.longbei.appservice.entity.CommentCount;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.entity.UserMsg;
import com.longbei.appservice.service.CommentLowerMongoService;

import net.sf.json.JSONObject;

@Service("commentLowerMongoService")
public class CommentLowerMongoServiceImpl implements CommentLowerMongoService {

	@Autowired
	private CommentLowerMongoDao commentLowerMongoDao;
	@Autowired
	private CommentCountMongoDao commentCountMongoDao;
	@Autowired
	private CommentMongoDao commentMongoDao;
	@Autowired
	private UserMsgMapper userMsgMapper;
	
	private static Logger logger = LoggerFactory.getLogger(CommentLowerMongoServiceImpl.class);
	
	@Override
	public BaseResp<Object> insertCommentLower(CommentLower commentLower) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			insert(commentLower);
			//修改主评论条数
			updateCountSizeInsert(commentLower.getCommentid());
			//添加评论消息
			insertMsg(commentLower);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("insertCommentLower commentLower={},msg={}",commentLower,e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 添加评论消息
	 * 2017年2月10日
	 */
	private void insertMsg(CommentLower commentLower){
		UserMsg record = new UserMsg();
		record.setUserid(Long.valueOf(commentLower.getFriendid()));
		record.setCreatetime(new Date());
		record.setFriendid(Long.valueOf(commentLower.getUserid()));
		//itype 类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论  itypeid
		Comment comment = commentMongoDao.selectCommentByid(commentLower.getCommentid());
		if(null != comment){
			record.setGtype(comment.getItype());
			record.setSnsid(Long.valueOf(comment.getItypeid()));
		}
		//0 聊天 1 评论 2 点赞 3 送花 4 送钻石 等等
		record.setMsgtype("1");
		record.setRemark(commentLower.getContent());
		record.setIsdel("0");
		record.setIsread("0");
		// mtype  0 系统消息(通知消息.进步消息等) 1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3
		// 送花 4 送钻石  5:粉丝  等等)
		record.setMtype("1");
		try {
			userMsgMapper.insertSelective(record);
		} catch (Exception e) {
			logger.error("insertMsg record = {}, msg = {}", JSONObject.fromObject(record).toString(), e);
		}
	}
	
	private void insert(CommentLower commentLower){
		commentLowerMongoDao.insertCommentLower(commentLower);
	}
	
	/**
	 * @author yinxc
	 * 修改当前主评论的条数（添加评论）
	 * 2017年1月22日
	 * return_type
	 * CommentLowerMongoServiceImpl
	 */
	private void updateCountSizeInsert(String commentid){
		CommentCount commentCount = commentCountMongoDao.selectCommentCountByCommentid(commentid);
		if(null != commentCount){
			int count = commentCount.getComcount() + 1;
			//点赞先为空，01-22日，点赞需求还不固定
//			String likes = "";
			updateCount(commentid, count+"");
		}
	}
	
	/**
	 * @author yinxc
	 * 修改当前主评论的条数（删除评论）
	 * 2017年1月22日
	 * return_type
	 * CommentLowerMongoServiceImpl
	 */
	private void updateCountSizeDelete(String commentid){
		CommentCount commentCount = commentCountMongoDao.selectCommentCountByCommentid(commentid);
		if(null != commentCount){
			int count = 0;
			if(commentCount.getComcount() >= 1){
				count = commentCount.getComcount() - 1;
			}
			//点赞先为空，01-22日，点赞需求还不固定
//			String likes = "";
			updateCount(commentid, count+"");
		}
	}
	
	private void updateCount(String commentid, String comcount){
		commentCountMongoDao.updateCommentCount(commentid, comcount);
	}

	@Override
	public List<CommentLower> selectCommentLowerListByCommentid(String commentid) {
		List<CommentLower> list = null;
		try {
			list = commentLowerMongoDao.selectCommentLowerListByCommentid(commentid);
		} catch (Exception e) {
			logger.error("selectCommentLowerListByCommentid commentid={},msg={}",commentid,e);
		}
		return list;
	}

	@Override
	public CommentLower selectCommentLowerByid(String id) {
		CommentLower commentLower = null;
		try {
			commentLower = commentLowerMongoDao.selectCommentLowerByid(id);
		} catch (Exception e) {
			logger.error("selectCommentLowerByid id={},msg={}",id,e);
		}
		return commentLower;
	}

	@Override
	public BaseResp<Object> deleteLowerByCommentid(String commentid) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			deleteByCommentid(commentid);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteLowerByCommentid commentid={},msg={}",commentid,e);
		}
		return reseResp;
	}
	
	private void deleteByCommentid(String commentid){
		commentLowerMongoDao.deleteLowerByCommentid(commentid);
	}
	
	@Override
	public BaseResp<Object> deleteCommentLower(String id) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			//修改当前主评论的条数
			CommentLower commentLower = commentLowerMongoDao.selectCommentLowerByid(id);
			if(null != commentLower){
				//修改
				updateCountSizeDelete(commentLower.getCommentid());
			}
			deleteByid(id);
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("deleteCommentLower id={},msg={}",id,e);
		}
		return reseResp;
	}
	
	private void deleteByid(String id){
		commentLowerMongoDao.deleteCommentLower(id);
	}

	@Override
	public long selectCountLowerByCommentid(String commentid) {
		if(StringUtils.hasBlankParams(commentid)){
			return 0;
		}
		return commentLowerMongoDao.selectCountLowerByCommentid(commentid);
	}

}
