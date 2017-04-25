package com.longbei.appservice.controller;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.entity.Comment;
import com.longbei.appservice.entity.CommentLikes;
import com.longbei.appservice.entity.CommentLower;
import com.longbei.appservice.service.CommentLikesMongoService;
import com.longbei.appservice.service.CommentLowerMongoService;
import com.longbei.appservice.service.CommentMongoService;

/**
 * @author yinxc
 * 评论
 * 2017年1月22日
 * return_type
 * CommentController
 */

@Controller
@RequestMapping(value = "/comment")
public class CommentController extends BaseController {
	
	@Autowired
	private CommentMongoService commentMongoService;
	@Autowired
	private CommentLowerMongoService commentLowerMongoService;
//	@Autowired
//	private CommentCountMongoService commentCountMongoService;
	@Autowired
	private CommentLikesMongoService commentLikesMongoService;
	
	
	private static Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	
	/**
    * @Title: http://ip:port/appservice/comment/decreaseCommentLikes
    * @Description: 给主评论取消点赞
    * @param @param commentid  主评论id
    * @param @param friendid   点赞者商户id
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月4日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/decreaseCommentLikes")
    @ResponseBody
    public BaseResp<Object> decreaseCommentLikes(String commentid, String friendid) {
    	
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(friendid, commentid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = commentLikesMongoService.deleteCommentLikesByCommentidAndFriendid(commentid, friendid);
		} catch (Exception e) {
			logger.error("decreaseCommentLikes commentid = {}, friendid = {}", commentid, friendid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/appservice/comment/addCommentLikes
    * @Description: 给主评论点赞
    * @param @param userid   评论者id
    * @param @param commentid  主评论id
    * @param @param friendid   点赞者商户id
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月3日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/addCommentLikes")
    @ResponseBody
    public BaseResp<Object> addCommentLikes(String userid, String commentid, String friendid) {
    	
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, friendid, commentid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		
  		CommentLikes commentLikes = new CommentLikes();
  		commentLikes.setCommentid(commentid);
  		commentLikes.setFriendid(friendid);
  		commentLikes.setUserid(userid);
  		commentLikes.setCreatetime(DateUtils.formatDateTime1(new Date()));
  		try {
  			baseResp = commentLikesMongoService.insertCommentLikes(commentLikes);
		} catch (Exception e) {
			logger.error("addCommentLikes userid = {}, commentid = {}, friendid = {}", userid, commentid, friendid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/appservice/comment/commentList
    * @Description: 查看最新评论列表
    * @param @param userid   当前访问者商户id
	* @param @param businessid  各类型对应的id
    * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
    * @param @param startNum
    * @param @param pageSize
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年1月22日
	*/
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/commentList")
    @ResponseBody
    public BaseResp<Object> commentList(String userid, String businessid, String businesstype,
    		int startNum, int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(userid, businessid, businesstype)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = commentMongoService.selectCommentListByItypeidAndFriendid(userid, businessid, businesstype, startNum, pageSize);
		} catch (Exception e) {
			logger.error("commentList businessid = {}, businesstype = {}", businessid, businesstype, e);
		}
		return baseResp;
	}
    
    /**
     * @Title: http://ip:port/appservice/comment/commentHotList
     * @Description: 查看热门评论列表(5条)
     * @param @param userid   当前访问者商户id
 	 * @param @param businessid  各类型对应的id
     * @param @param businesstype  类型   0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
    @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/commentHotList")
    @ResponseBody
    public BaseResp<Object> commentHotList(String userid, String businessid, String businesstype) {
    	BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(userid, businessid, businesstype)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentMongoService.selectCommentHotListByItypeidAndFid(userid, businessid, businesstype);
 		} catch (Exception e) {
 			logger.error("commentHotList businessid = {}, businesstype = {}, userid = {}", businessid, businesstype, userid, e);
 		}
 		return baseResp;
 	}
    
    /**
     * @Title: http://ip:port/appservice/comment/addComment
     * @Description: 添加主评论
     * @param @param userid  评论者id
     * @param @param friendid 被评论商户id
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
     * @param @param businessid  各类型对应的id
     * @param @param content
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
     @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/addComment")
     @ResponseBody
     public BaseResp<Object> addComment(String userid, String friendid, 
    		String businessid, String businesstype, String content) {
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(userid, friendid, businessid, businesstype)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			Comment comment = new Comment();
 			comment.setContent(content);
 			comment.setCreatetime(DateUtils.formatDateTime1(new Date()));
 			comment.setBusinesstype(businesstype);
 			comment.setBusinessid(businessid);
 			comment.setUserid(userid);
 			comment.setFriendid(friendid);
 			baseResp = commentMongoService.insertComment(comment);
 		} catch (Exception e) {
 			logger.error("addComment userid = {}, businessid = {}, businesstype = {}", userid, businessid, businesstype, e);
 		}
 		return baseResp;
 	}
     
     /**
      * @Title: http://ip:port/appservice/comment/addCommentLower
      * @Description: 添加子评论
      * @param @param firstuserid
      * @param @param commentid  主评论id
      * @param @param status  0:不显示回复    1:显示回复
      * @param @param content  评论内容
      * @param @param seconduserid
      * 子评论    A回复B   A的id ： firstuserid    B的id：seconduserid
      * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
      * @auther yxc
      * @currentdate:2017年1月22日
  	*/
    @SuppressWarnings("unchecked")
  	@RequestMapping(value = "/addCommentLower")
    @ResponseBody
    public BaseResp<Object> addCommentLower(String firstuserid, String commentid, 
    		String seconduserid, String content, String status) {
    	
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(firstuserid, seconduserid, commentid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		if(StringUtils.hasBlankParams(status)){
  			status = "0";
  		}
  		try {
  			CommentLower commentLower = new CommentLower();
  			commentLower.setContent(content);
  			commentLower.setCreatetime(DateUtils.formatDateTime1(new Date()));
  			commentLower.setCommentid(commentid);
  			commentLower.setSeconduserid(seconduserid);
  			commentLower.setFirstuserid(firstuserid);
  			commentLower.setStatus(status);
  			baseResp = commentLowerMongoService.insertCommentLower(commentLower);
  		} catch (Exception e) {
  			logger.error("addCommentLower firstuserid = {}, commentid = {}, seconduserid = {}", firstuserid, commentid, seconduserid, e);
  		}
  		return baseResp;
    }
    
    /**
     * @Title: http://ip:port/appservice/comment/deleteCommentLower
     * @Description: 删除子评论
     * @param @param id  子评论id
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
   @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/deleteCommentLower")
   @ResponseBody
   public BaseResp<Object> deleteCommentLower(String id) {
   	
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(id)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentLowerMongoService.deleteCommentLower(id);
 		} catch (Exception e) {
 			logger.error("deleteCommentLower id = {}", id, e);
 		}
 		return baseResp;
   }
   
   /**
    * @Title: http://ip:port/appservice/comment/deleteComment
    * @Description: 删除主评论
    * @param @param commentid  主评论id
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年1月22日
	*/
   	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteComment")
	@ResponseBody
	public BaseResp<Object> deleteComment(String commentid) {
	  	
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(commentid)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = commentMongoService.deleteComment(commentid);
		} catch (Exception e) {
			logger.error("deleteComment commentid = {}", commentid, e);
		}
		return baseResp;
	}
   	
   	/**
     * @Title: http://ip:port/appservice/comment/selectCommentCountSum
     * @Description: 查看评论总数
     * @param @param businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜评论  3圈子评论 4 教室评论
     * @param @param businessid  各类型对应的id
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
    @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/selectCommentCountSum")
 	@ResponseBody
 	public BaseResp<Integer> selectCommentCountSum(String businesstype, String businessid) {
 	  	
 		BaseResp<Integer> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(businesstype, businessid)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentMongoService.selectCommentCountSum(businessid, businesstype);
 		} catch (Exception e) {
 			logger.error("selectCommentCountSum businessid = {}, businesstype = {}", businessid, businesstype, e);
 		}
 		return baseResp;
 	}
	
}
