package com.longbei.appservice.controller;

import java.util.HashMap;
import java.util.Map;

import com.longbei.appservice.entity.UserSettingCommon;
import com.longbei.appservice.service.UserSettingCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.service.UserMsgService;

@RestController
@RequestMapping(value = "/userMsg")
public class UserMsgController extends BaseController {

	@Autowired
	private UserMsgService userMsgService;
	@Autowired
	private UserSettingCommonService userSettingCommonService;
	
	private static Logger logger = LoggerFactory.getLogger(UserMsgController.class);
	
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgSystemList
    * @Description: 获取系统消息---(分页)
    * @param @param userid  
    * @param @param startNum   pageSize
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgSystemList")
    public BaseResp<Object> msgSystemList(String userid, int startNum, int pageSize) {
		logger.info("userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectByUserid(Long.parseLong(userid), startNum, pageSize);
		} catch (Exception e) {
			logger.error("msgSystemList userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgOtherList
    * @Description: 获取@我消息---(分页)
    * @param @param userid  
    * @param @param startNum   pageSize
    * @param @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	*										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	*										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	*										27:工作认证审核结果      28：学历认证审核结果   
	*										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	*										32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙V认证)
	* 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	* 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	* 		 					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  44: 榜中成员下榜)
    * @param @param msgtype 可为null 获取@我消息
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgOtherList")
    public BaseResp<Object> msgOtherList(String userid, String mtype, String msgtype, int startNum, int pageSize) {
		logger.info("userid = {}, mtype = {}, msgtype = {}, startNum = {}, pageSize = {}", userid, mtype, msgtype, startNum, pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, mtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectOtherList(Long.parseLong(userid), mtype, msgtype, startNum, pageSize);
		} catch (Exception e) {
			logger.error("msgOtherList userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgLikeList
    * @Description: 获取对话---点赞和粉丝消息列表(分页)(对话消息)
    * @param @param userid  
    * @param @param startNum   pageSize
    * @param @param msgtype 2:点赞   5:粉丝  
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgLikeList")
    public BaseResp<Object> msgLikeList(String userid, String msgtype, int startNum, int pageSize) {
		logger.info("userid = {},  msgtype = {}, startNum = {}, pageSize = {}", userid, msgtype, startNum, pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectLikeList(Long.parseLong(userid), msgtype, startNum, pageSize);
		} catch (Exception e) {
			logger.error("msgLikeList userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDialogueList
    * @Description: 获取对话消息列表(分页)(对话消息-----除赞消息,粉丝消息)
    * @param @param userid  
    * @param @param startNum   pageSize
    * @param @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	*										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	*										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	*										27:工作认证审核结果      28：学历认证审核结果   
	*										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	*										32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙V认证)
	* 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	* 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	* 		 					              14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDialogueList")
    public BaseResp<Object> msgDialogueList(String userid, int startNum, int pageSize) {
		logger.info("userid = {}, startNum = {}, pageSize = {}", userid, startNum, pageSize);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.selectExceptList(Long.parseLong(userid), startNum, pageSize);
		} catch (Exception e) {
			logger.error("msgDialogueList userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgList
    * @Description: 获取系统消息列表(分页)(系统消息)
    * @param @param userid  
    * @param @param startNum   pageSize
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
//	@SuppressWarnings("unchecked")
//  	@RequestMapping(value = "/msgList")
//    @ResponseBody
//    public BaseResp<Object> msgList(@RequestParam("userid") String userid, 
//    		int startNum, int endNum) {
//		BaseResp<Object> baseResp = new BaseResp<>();
//  		if (StringUtils.hasBlankParams(userid)) {
//  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
//  		}
//  		try {
//  			baseResp = userMsgService.selectByUserid(Long.parseLong(userid), startNum, endNum);
//		} catch (Exception e) {
//			logger.error("msgList userid = {}", userid, e);
//		}
//  		return baseResp;
//	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDel
    * @Description: 删除消息
    * @param @param id  
    * @param @param userid 
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDel")
    public BaseResp<Object> msgDel(String id, String userid) {
		logger.info("id = {}, userid = {}", id, userid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(id, userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByid(Integer.parseInt(id), Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("msgDel id = {}", id, e);
		}
  		return baseResp;
	}

	/**
    * @Title: http://ip:port/app_service/userMsg/msgDelOther
    * @Description: 清空用户类型消息(mtype,msgtype) 
    * @param @param userid  
    * @param @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	*										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	*										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	*										27:工作认证审核结果      28：学历认证审核结果   
	*										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	*										32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙V认证)
	* 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	* 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	* 		 					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDelOther")
    public BaseResp<Object> msgDelOther(String userid, String mtype, String msgtype) {
		logger.info("userid = {}, mtype = {}, msgtype = {}", userid, mtype, msgtype);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, mtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByMtypeAndMsgtype(Long.parseLong(userid), mtype, msgtype);
		} catch (Exception e) {
			logger.error("msgDelMtype userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgDelMtype
    * @Description: 删除用户类型消息
    * @param @param userid  
    * @param @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	*										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	*										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	*										27:工作认证审核结果      28：学历认证审核结果   
	*										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	*										32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙V认证)
	* 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	* 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	* 		 					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgDelMtype")
    public BaseResp<Object> msgDelMtype(String userid, String mtype) {
		logger.info("userid = {}, mtype = {}", userid, mtype);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, mtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByUserid(Long.parseLong(userid), mtype);
		} catch (Exception e) {
			logger.error("msgDelMtype userid = {}", userid, e);
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
    public BaseResp<Object> msgDelLike(String userid, String msgtype) {
		logger.info("userid = {}, msgtype = {}", userid, msgtype);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, msgtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.deleteByLikeUserid(Long.parseLong(userid), msgtype);
		} catch (Exception e) {
			logger.error("msgDelLike userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgUpdIsread
    * @Description: 修改消息状态 isread 是否已读 0：未读 1：已读
    * @param @param id  
    * @param @param userid
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgUpdIsread")
    public BaseResp<Object> msgUpdIsread(String id, String userid) {
		logger.info("id = {}, userid = {}", id, userid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(id)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.updateIsreadByid(Integer.parseInt(id), Long.parseLong(userid));
		} catch (Exception e) {
			logger.error("msgUpdIsread id = {}", id, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/msgUpdAllIsread
    * @Description: 修改用户消息状态 mtype,msgtype
    * @param @param userid  
    * @param @param mtype 0 系统消息(msgtype  18:升龙级   19：十全十美升级   20:榜关注开榜通知    21：榜关注结榜通知 
	*										22:加入的榜结榜未获奖   23：加入的教室有新课通知    24：订单已发货
	*										25:订单发货N天后自动确认收货    26：实名认证审核结果   
	*										27:工作认证审核结果      28：学历认证审核结果   
	*										29：被PC选为热门话题    30：被选为达人   31：微进步被推荐
	*										32：创建的龙榜/教室/圈子被选中推荐 53：被授予龙V认证)
	* 		 				1 对话消息(msgtype 0 聊天 1 评论 2 点赞 3 送花 4 送钻石 5:粉丝  等等)
	* 		 				2:@我消息(msgtype  10:邀请   11:申请加入特定圈子   12:老师批复作业  13:老师回复提问  
	* 		 					14:发布新公告   15:获奖   16:剔除   17:加入请求审批结果  )
    * @param @param msgtype 是对话消息时，传值     msgtype 为null 修改各类型消息状态
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/msgUpdAllIsread")
    public BaseResp<Object> msgUpdAllIsread(String userid, String mtype, String msgtype) {
		logger.info("userid = {}, mtype = {}, msgtype = {}", userid, mtype, msgtype);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid, mtype)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
  			baseResp = userMsgService.updateIsreadByUserid(Long.parseLong(userid), mtype, msgtype);
		} catch (Exception e) {
			logger.error("msgUpdAllIsread userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	/**
    * @Title: http://ip:port/app_service/userMsg/isMsgRed
    * @Description: 获取消息是否显示红点
    * @param @param userid  
	* return_type  0:不显示   1：显示
    * @param @param 正确返回 code 0 参数错误，未知错误返回相应状态码
    * @auther yxc
    * @currentdate:2017年2月8日
	*/
	@SuppressWarnings("unchecked")
  	@RequestMapping(value = "/isMsgRed")
    public BaseResp<Object> isMsgRed(String userid) {
		logger.info("userid = {}", userid);
		BaseResp<Object> baseResp = new BaseResp<>();
  		if (StringUtils.hasBlankParams(userid)) {
  			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
  		}
  		try {
			//type 0 通知消息(系统消息)页面是否显示红点
			//	   1  获取"我的"页面是否显示红点
			// 	   2:@我消息页面是否显示红点
			//	   3: 添加好友申请 是否显示红点
//			Map<String,Object> resultMap = new HashMap<String,Object>();
//  			int temp = 0;
//  			if("1".equals(type)){
  				//type 1   获取"我的"页面是否显示红点
//			Map<String,Object> myMap = userMsgService.selectShowMyByMtype(Long.parseLong(userid));
//  			}else if("3".equals(type)){
				//获取添加好友的申请 消息数量和最大的createtime
//			Map<String,Object> friendMap = userMsgService.selectAddFriendAskMsg(Long.parseLong(userid));
//			}else{
//  			Map<String,Object> friendMap = userMsgService.selectAddFriendAskMsg(Long.parseLong(userid));

			//获取新消息通知的开关
			Map<String, Object> userMsgSwitch = userSettingCommonService.selectMapByUserid(userid+"");

			Map<String,Object> myMap = userMsgService.selectShowMy(Long.parseLong(userid));
			Map<String,Object> friendMap = new HashMap<String,Object>();
			if("1".equals(userMsgSwitch.get("is_newfriendask").toString())){
				friendMap = userMsgService.selectAddFriendAskMsgDate(Long.parseLong(userid));
			}else{
				friendMap.put("friendAskcount", 0);
			}

			Map<String,Object> informMap = userMsgService.selectCountByType(Long.parseLong(userid), "0", null, "0");
			Map<String,Object> rankMap = userMsgService.selectCountByType(Long.parseLong(userid), "2", null, "0");
//  			}
			myMap.putAll(friendMap);
			myMap.putAll(informMap);
			myMap.putAll(rankMap);
			baseResp.setExpandData(myMap);
  			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("isMsgRed userid = {}", userid, e);
		}
  		return baseResp;
	}
	
	
}
