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
import com.longbei.appservice.service.UserSettingCommonService;


/**
 * @author yinxc
 * 设置
 * 2017年1月19日
 * return_type
 */
@Controller
@RequestMapping(value = "/userSetting")
public class UserSettingController extends BaseController {

	@Autowired
	private UserSettingCommonService userSettingCommonService;
	
	private static Logger logger = LoggerFactory.getLogger(UserSettingController.class);
	
	
	/**
    * @Title: http://ip:port/appservice/userSetting/commonlist
    * @Description: 用户 设置  消息提醒设置 隐私设置等
    * @Description: key 键名称     显示首页工具栏:is_page_tool 新消息提醒    
    * @Description: (新粉丝：is_new_fans
    * @Description: 点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment)
    * @Description: 隐私设置  (允许通过昵称搜到我:is_nick_search   允许通过此手机号搜到我:is_phone_search
    * @Description: 允许熟人看我的微进步:is_acquaintance_look
	* @Description: 评论设置(我同意接收到这些人的评论通知):is_comment_msg)
    * @Description: value;//值   0:关闭  1：开启   评论设置:0:关闭  1：与我相关（好友、Like、熟人） 2：所有人
    * @param @param userid
    * @param @param 正确返回 code 0，Map<String, Object>  参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月19日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/commonlist")
    @ResponseBody
    public BaseResp<Object> commonlist(@RequestParam("userid") String userid) {
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(userid)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
    	}
    	try {
    		baseResp = userSettingCommonService.selectByUserid(userid);
        } catch (Exception e) {
            logger.error("commonlist userid={},msg={}", userid, e);
        }
    	return baseResp;
    }
	
	/**
    * @Title: http://ip:port/appservice/userSetting/updateCommonBykey
    * @Description: 修改用户 设置  消息提醒设置 隐私等设置
    * @Description: key 键名称     显示首页工具栏:is_page_tool 新消息提醒    
    * @Description: (新粉丝：is_new_fans
    * @Description: 点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment)
    * @Description: 隐私设置  (允许通过昵称搜到我:is_nick_search   允许通过此手机号搜到我:is_phone_search
    * @Description: 允许熟人看我的微进步:is_acquaintance_look
    * @Description: 评论设置(我同意接收到这些人的评论通知):is_comment_msg)
    * @Description: value;//值   0:关闭  1：开启   评论设置:0:关闭  1：与我相关（好友、Like、熟人） 2：所有人
    * @param @param userid
	* @param @param key
    * @param @param value
    * @param @param 正确返回 code 0，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月19日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateCommonBykey")
    @ResponseBody
    public BaseResp<Object> updateCommonBykey(@RequestParam("userid") String userid, @RequestParam("key") String key, 
    		@RequestParam("value") String value) {
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(userid,key,value)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
    	}
    	try {
    		baseResp = userSettingCommonService.updateByUseridKey(userid, key, value);
        } catch (Exception e) {
            logger.error("updateCommonBykey userid={}, key={}, value={}, msg={}", userid, key, value, e);
        }
    	return baseResp;
    }
	
	/**
    * @Title: http://ip:port/appservice/userSetting/updatekeys
    * @Description: 消息提醒设置---一键关闭或一键打开
    * @Description: key 键名称     显示首页工具栏:is_page_tool 新消息提醒    
    * @Description: (新粉丝：is_new_fans
    * @Description: 点赞:is_like  献花:is_flower  钻石:is_diamond  评论设置:is_comment)
    * @Description: 隐私设置  (允许通过昵称搜到我:is_nick_search   允许通过此手机号搜到我:is_phone_search
    * @Description: 允许熟人看我的微进步:is_acquaintance_look
    * @Description: 评论设置(我同意接收到这些人的评论通知):is_comment_msg)
    * @param @param userid
	* @param @param value--- 0:关闭   1:打开
    * @param @param 正确返回 code 0，参数错误，未知错误返回相应状态码
    * @auther yinxc
    * @currentdate:2017年1月19日
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updatekeys")
    @ResponseBody
    public BaseResp<Object> updatekeys(@RequestParam("userid") String userid, @RequestParam("value") String value) {
    	BaseResp<Object> baseResp = new BaseResp<>();
    	if(StringUtils.hasBlankParams(userid, value)){
    		return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
    	}
    	try {
    		baseResp = userSettingCommonService.updateByUseridMap(userid, value);
        } catch (Exception e) {
            logger.error("updatekeys userid={}, msg={}", userid, e);
        }
    	return baseResp;
    }
	
}
