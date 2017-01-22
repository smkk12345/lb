package com.longbei.appservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.service.CommentCountMongoService;
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
	@Autowired
	private CommentCountMongoService commentCountMongoService;
	
	private static Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	
	/**
    * @Title: http://ip:port/appservice/comment/commentList
    * @Description: 查看评论列表
    * @param @param userid
	* @param @param itypeid  各类型对应的id
    * @param @param itype  类型    0:进步(零散)评论  1：榜评论   2：教室微进步评论   3：圈子评论   4:目标进步评论
    * @param @param startNo
    * @param @param pageSize
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年1月22日
	*/
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/commentList")
    @ResponseBody
    public BaseResp<Object> commentList(@RequestParam("userid") String userid, @RequestParam("itypeid") String itypeid, 
    		@RequestParam("itype") String itype,
    		String startNo, String pageSize) {
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(startNo)){
			startNo = "0";
		}
		if(StringUtils.isBlank(pageSize)){
			pageSize = "10";
		}
		if (StringUtils.hasBlankParams(userid, itypeid, itype)) {
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = commentMongoService.selectCommentListByItypeid(itypeid, itype, Integer.parseInt(startNo), Integer.parseInt(pageSize));
		} catch (Exception e) {
			logger.error("commentList userid = {}, itypeid = {},itype = {},msg={}", userid, itypeid, itype, e);
		}
		return baseResp;
	}
    
    /**
     * @Title: http://ip:port/appservice/comment/commentList
     * @Description: 查看评论列表
     * @param @param request
 	* @param @param response
     * @param @param username password inviteuserid randomCode
     * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
     * @auther yxc
     * @currentdate:2017年1月22日
 	*/
     @SuppressWarnings("unchecked")
 	@RequestMapping(value = "/commentadd")
     @ResponseBody
     public BaseResp<Object> commentadd(@RequestParam("userid") String userid, @RequestParam("itypeid") String itypeid, 
     		@RequestParam("itype") String itype,
     		String startNo, String pageSize) {
 		BaseResp<Object> baseResp = new BaseResp<>();
 		if(StringUtils.isBlank(startNo)){
 			startNo = "0";
 		}
 		if(StringUtils.isBlank(pageSize)){
 			pageSize = "10";
 		}
 		if (StringUtils.hasBlankParams(userid, itypeid, itype)) {
 			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
 		}
 		try {
 			baseResp = commentMongoService.selectCommentListByItypeid(itypeid, itype, Integer.parseInt(startNo), Integer.parseInt(pageSize));
 		} catch (Exception e) {
 			logger.error("commentList userid = {}, itypeid = {},itype = {},msg={}", userid, itypeid, itype, e);
 		}
 		return baseResp;
 	}
	
}
