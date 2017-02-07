package com.longbei.appservice.controller;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public BaseResp<Object> decreaseCommentLikes(@RequestParam("commentid") String commentid, 
    		@RequestParam("friendid") String friendid) {
    	
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(friendid, commentid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = commentLikesMongoService.deleteCommentLikesByCommentidAndFriendid(commentid, friendid);
		} catch (Exception e) {
			logger.error("decreaseCommentLikes commentid = {}, friendid = {},msg={}", commentid, friendid, e);
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
    public BaseResp<Object> addCommentLikes(@RequestParam("userid") String userid, 
    		@RequestParam("commentid") String commentid, 
    		@RequestParam("friendid") String friendid) {
    	
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
			logger.error("addCommentLikes userid = {}, commentid = {}, friendid = {},msg={}", userid, commentid, friendid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/appservice/comment/commentList
    * @Description: 查看最新评论列表
    * @param @param friendid   当前访问者商户id
	* @param @param itypeid  各类型对应的id
    * @param @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
    * @param @param startNo
    * @param @param pageSize
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年1月22日
	*/
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/commentList")
    @ResponseBody
    public BaseResp<Object> commentList(@RequestParam("friendid") String friendid, 
    		@RequestParam("itypeid") String itypeid, 
    		@RequestParam("itype") String itype,
    		int startNo, int pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(friendid, itypeid, itype)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = commentMongoService.selectCommentListByItypeidAndFriendid(friendid, itypeid, itype, startNo, pageSize);
		} catch (Exception e) {
			logger.error("commentList itypeid = {},itype = {},msg={}", itypeid, itype, e);
		}
		return baseResp;
	}
    
    /**
     * @Title: http://ip:port/appservice/comment/commentHotList
     * @Description: 查看热门评论列表(5条)
     * @param @param friendid   当前访问者商户id
 	 * @param @param itypeid  各类型对应的id
     * @param @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
    @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/commentHotList")
    @ResponseBody
    public BaseResp<Object> commentHotList(@RequestParam("friendid") String friendid, 
     	@RequestParam("itypeid") String itypeid, 
     	@RequestParam("itype") String itype) {
    	BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(friendid, itypeid, itype)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentMongoService.selectCommentHotListByItypeidAndFid(friendid, itypeid, itype);
 		} catch (Exception e) {
 			logger.error("commentHotList itypeid = {},itype = {},msg={}", itypeid, itype, e);
 		}
 		return baseResp;
 	}
    
    /**
     * @Title: http://ip:port/appservice/comment/addComment
     * @Description: 添加主评论
     * @param @param userid
     * @param @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param @param itypeid  各类型对应的id
     * @param @param content
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
     @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/addComment")
     @ResponseBody
     public BaseResp<Object> addComment(@RequestParam("userid") String userid, @RequestParam("itypeid") String itypeid, 
     		@RequestParam("itype") String itype, @RequestParam("content") String content) {
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(userid, itypeid, itype)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			Comment comment = new Comment();
 			comment.setContent(content);
 			comment.setCreatetime(DateUtils.formatDateTime1(new Date()));
 			comment.setItype(itype);
 			comment.setItypeid(itypeid);
 			comment.setUserid(userid);
 			baseResp = commentMongoService.insertComment(comment);
 		} catch (Exception e) {
 			logger.error("addComment userid = {}, itypeid = {},itype = {},msg={}", userid, itypeid, itype, e);
 		}
 		return baseResp;
 	}
     
     /**
      * @Title: http://ip:port/appservice/comment/addCommentLower
      * @Description: 添加子评论
      * @param @param userid   评论者id
      * @param @param commentid  主评论id
      * @param @param status  0:不显示回复    1:显示回复
      * @param @param content  评论内容
      * @param @param friendid   被评论商户id
      * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
      * @auther yxc
      * @currentdate:2017年1月22日
  	*/
    @SuppressWarnings("unchecked")
  	@RequestMapping(value = "/addCommentLower")
    @ResponseBody
    public BaseResp<Object> addCommentLower(@RequestParam("userid") String userid, @RequestParam("commentid") String commentid, 
    		@RequestParam("friendid") String friendid, 
      		@RequestParam("content") String content, String status) {
    	
  		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, friendid, commentid)) {
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
  			commentLower.setFriendid(friendid);
  			commentLower.setUserid(userid);
  			commentLower.setStatus(status);
  			baseResp = commentLowerMongoService.insertCommentLower(commentLower);
  		} catch (Exception e) {
  			logger.error("addCommentLower userid = {}, commentid = {},friendid = {},msg={}", userid, commentid, friendid, e);
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
   public BaseResp<Object> deleteCommentLower(@RequestParam("id") String id) {
   	
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(id)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentLowerMongoService.deleteCommentLower(id);
 		} catch (Exception e) {
 			logger.error("deleteCommentLower id = {},msg={}", id, e);
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
	public BaseResp<Object> deleteComment(@RequestParam("commentid") String commentid) {
	  	
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.hasBlankParams(commentid)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = commentMongoService.deleteComment(commentid);
		} catch (Exception e) {
			logger.error("deleteComment commentid = {},msg={}", commentid, e);
		}
		return baseResp;
	}
   	
   	/**
     * @Title: http://ip:port/appservice/comment/selectCommentCountSum
     * @Description: 查看评论总数
     * @param @param itype  类型    0:进步(零散)评论  1:目标进步评论  2：榜评论    3：圈子评论     4：教室微进步评论
     * @param @param itypeid  各类型对应的id
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
    @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/selectCommentCountSum")
 	@ResponseBody
 	public BaseResp<Object> selectCommentCountSum(@RequestParam("itype") String itype, @RequestParam("itypeid") String itypeid) {
 	  	
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if (StringUtils.hasBlankParams(itype, itypeid)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentMongoService.selectCommentCountSum(itypeid, itypeid);
 		} catch (Exception e) {
 			logger.error("selectCommentCountSum itypeid = {},msg={}", itypeid, e);
 		}
 		return baseResp;
 	}
	
}
