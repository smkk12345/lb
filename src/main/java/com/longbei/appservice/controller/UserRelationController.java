/**   
* @Title: UserRelationController.java 
* @Package com.longbei.appservice.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 下午3:46:47 
* @version V1.0   
*/
package com.longbei.appservice.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.web.BaseController;
import com.longbei.appservice.service.UserRelationService;
/**
 * @author smkk
 * 关系控制器，好友  熟人  关注的人
 */
@Controller
@RequestMapping(value = "/user")
public class UserRelationController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(UserRelationController.class);
	
	 @Autowired
	 private UserRelationService userRelationService;
	
	//－－－－－－－－－－－sns_frined－start－－－－－－－－－－－－
	/**
	* @Title: insertFriend
	* @Description: 添加好友
	* @param @param userid
	* @param @param friendid
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@ResponseBody
    @RequestMapping(value = "insert")
	public BaseResp<Object> insertFriend(long userid,long friendid){
		logger.info("insertfriend params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			return userRelationService.insertFriend(userid, friendid);
		} catch (Exception e) {
			logger.error("insertfriend error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: selectListByUserId
	* @Description: 查询好友列表 通过好友id
	* @param @param userid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@ResponseBody
    @RequestMapping(value = "selectListByUserId")
	public BaseResp<Object> selectListByUserId(long userid,int startNum,int endNum){
		logger.info("selectListByUserId params userid={}",userid);
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			return userRelationService.selectListByUserId(userid, startNum, endNum);
		} catch (Exception e) {
			logger.error("selectListByUserId error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: delete
	* @Description: 删除好友
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@ResponseBody
    @RequestMapping(value = "delete")
	public BaseResp<Object> delete(long userid,long friendid){
		logger.info("delete params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			return userRelationService.delete(userid, friendid);
		} catch (Exception e) {
			logger.error("delete error and msg={}",e);
		}
		return baseResp;
	}
	//－－－－－－－－－－－sns_frined－end－－－－－－－－－－－－－-
	//－－－－－－－－－－－sns_fans－start－－－－－－－－－－－－
	/**
	* @Title: insertFans
	* @Description: 添加关注
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@ResponseBody
    @RequestMapping(value = "insertFans")
	public BaseResp<Object> insertFans(long userid,long friendid){
		logger.info("insertFans params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			return userRelationService.insertFans(userid, friendid);
		} catch (Exception e) {
			logger.error("insertFans error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: deleteFans
	* @Description: 删除粉丝
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@ResponseBody
    @RequestMapping(value = "deleteFans")
	public BaseResp<Object> deleteFans(long userid,long friendid){
		logger.info("deleteFans params userid={},friendid={}",userid,friendid);
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			return userRelationService.deleteFans(userid, friendid);
		} catch (Exception e) {
			logger.error("deleteFans error and msg={}",e);
		}
		return baseResp;
	}
	/**
	* @Title: selectFansListByUserId
	* @Description: 查询关注的人员列表  查询被关注的成员列表
	* @param @param userid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	@ResponseBody
    @RequestMapping(value = "selectFansListByUserId")
	public BaseResp<Object> selectFansListByUserId(long userid,int startNum,int endNum){
		logger.info("selectFansListByUserId params userid={}",userid);
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			return userRelationService.selectFansListByUserId(userid, startNum, endNum);
		} catch (Exception e) {
			logger.error("selectFansListByUserId error and msg={}",e);
		}
		return baseResp;
	}
	//－－－－－－－－－－－sns_fans－end－－－－－－－－－－－－－-
	//－－－－－－－－－－－sns_frined－start－－－－－－－－－－－－
	
	//－－－－－－－－－－－sns_frined－end－－－－－－－－－－－－－-

	
	
}
