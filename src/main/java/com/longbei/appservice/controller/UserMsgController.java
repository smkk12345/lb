package com.longbei.appservice.controller;

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
import com.longbei.appservice.service.UserMsgService;

@Controller
@RequestMapping(value = "/userMsg")
public class UserMsgController extends BaseController {

	@Autowired
	private UserMsgService userMsgService;
	
	private static Logger logger = LoggerFactory.getLogger(UserMsgController.class);
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgLikeList
    * @Description: 获取对话---点赞和粉丝消息列表(分页)(对话消息)
    * @param @param userid  
    * @param @param startNum   endNum
    * @param @param mtype 0 系统消息(通知消息.进步消息等) 
    * 					  1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝  等等)
    * @param @param msgtype 2:点赞   5:粉丝  
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgLikeList")
    @ResponseBody
    public BaseResp<Object> msgLikeList(@RequestParam("userid") String userid, String msgtype, 
    		int startNum, int endNum) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectLikeList(Long.parseLong(userid), msgtype, startNum, endNum);
		} catch (Exception e) {
			logger.error("msgLikeList userid = {}, msg = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDialogueList
    * @Description: 获取对话消息列表(分页)(对话消息-----除赞消息,粉丝消息)
    * @param @param userid  
    * @param @param startNum   endNum
    * @param @param mtype 0 系统消息(通知消息.进步消息等) 
    * 					  1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝  等等)
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDialogueList")
    @ResponseBody
    public BaseResp<Object> msgDialogueList(@RequestParam("userid") String userid, 
    		int startNum, int endNum) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectExceptList(Long.parseLong(userid), startNum, endNum);
		} catch (Exception e) {
			logger.error("msgDialogueList userid = {}, msg = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgList
    * @Description: 获取系统消息列表(分页)(系统消息)
    * @param @param userid  
    * @param @param startNum   endNum
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgList")
    @ResponseBody
    public BaseResp<Object> msgList(@RequestParam("userid") String userid, 
    		int startNum, int endNum) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectByUserid(Long.parseLong(userid), startNum, endNum);
		} catch (Exception e) {
			logger.error("msgList userid = {}, msg = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDel
    * @Description: 删除消息
    * @param @param id  
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDel")
    @ResponseBody
    public BaseResp<Object> msgDel(@RequestParam("id") String id) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(id)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByid(Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("msgDel id = {}, msg = {}", id, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDelMtype
    * @Description: 删除用户类型消息
    * @param @param userid  
    * @param @param mtype 0 系统消息(通知消息.进步消息等) 
    * 					  1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝  等等)
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDelMtype")
    @ResponseBody
    public BaseResp<Object> msgDelMtype(@RequestParam("userid") String userid, String mtype) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, mtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByUserid(Long.parseLong(userid), mtype);
		} catch (Exception e) {
			logger.error("msgDelMtype userid = {}, msg = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDelLike
    * @Description: 清空消息---点赞等对话消息
    * @param @param userid  
    * @param @param msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝  等等
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * mtype 0 系统消息(通知消息.进步消息等) 
    * 		  1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝  等等)
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDelLike")
    @ResponseBody
    public BaseResp<Object> msgDelLike(@RequestParam("userid") String userid, String msgtype) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, msgtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByLikeUserid(Long.parseLong(userid), msgtype);
		} catch (Exception e) {
			logger.error("msgDelLike userid = {}, msg = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgUpdIsread
    * @Description: 修改消息状态 isread 是否已读 0：未读 1：已读
    * @param @param id  
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgUpdIsread")
    @ResponseBody
    public BaseResp<Object> msgUpdIsread(@RequestParam("id") String id) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(id)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.updateIsreadByid(Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("msgUpdIsread id = {}, msg = {}", id, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgUpdAllIsread
    * @Description: 修改用户所有消息状态 isread 是否已读 0：未读 1：已读
    * @param @param userid  
    * @param @param mtype 0 系统消息(通知消息.进步消息等) 
    * 					  1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石  5:粉丝   等等)
    * @param @param msgtype 是对话消息时，传值
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgUpdAllIsread")
    @ResponseBody
    public BaseResp<Object> msgUpdAllIsread(@RequestParam("userid") String userid, String mtype, String msgtype) {
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, mtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.updateIsreadByUserid(Long.parseLong(userid), mtype, msgtype);
		} catch (Exception e) {
			logger.error("msgUpdAllIsread userid = {}, msg = {}", userid, e);
		}
  		return baseResp;
	}
	
}
