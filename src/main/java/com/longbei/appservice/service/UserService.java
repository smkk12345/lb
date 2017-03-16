package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserInfo;

/**
 * insert
 * @author smkk
 *
 */
public interface UserService {
	
	/**
	 * @author yinxc
	 * 个人中心
	 * 2017年3月9日
	 */
	BaseResp<UserInfo> selectInfoMore(long userid);
	
	/**
	 * @author yinxc
	 * 获取个人资料    屏蔽无用的字段
	 * 2017年3月8日
	 */
	BaseResp<Object> selectByUserid(long userid);
	
	/**
	 * 注册
	 * @param username
	 * @param nickname
	 * @param password
	 * @return
	 */
//	BaseResp<Object> register(Long userid,String username,String nickname,String  inviteuserid);
	/**
	* @Title: registerbasic
	* @Description:注册用户中心
	* @param @param username
	* @param @param password
	* @param @return    参数
	* @return BaseResp<UserInfo>    返回类型
	* @throws
	*/
	BaseResp<Object> registerbasic(String username, String password,String inviteuserid
			,String deviceindex,String devicetype,String avatar);
	
	
	/**
	* @Title: getByNickName
	* @Description: 通过昵称获取用户信息
	* @param @param nickname
	* @param @return    参数
	* @return UserInfo    返回类型
	* @throws
	 */
	UserInfo getByNickName(String nickname);
	
	BaseResp<Object> sms(String mobile,String operateType);
	/**
	* @Title: checkSms
	* @Description: 校验验证码
	* @param @param mobile
	* @param @param random
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月16日
	*/
	BaseResp<Object> checkSms(String mobile, String random,String deviceindex,String devicetype);
	/**
	* @Title: findPassword
	* @Description: TODO
	* @param @param username
	* @param @param newpwd
	* @param @param randomCode
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月16日
	*/
	BaseResp<Object> findPassword(String username, String newpwd, String randomCode);
	/**
	* @Title: login
	* @Description: TODO
	* @param @param username
	* @param @param password
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月16日
	*/
	BaseResp<UserInfo> login(String username, String password,String deviceindex);
	/**
	* @Title: registerthird
	* @Description: TODO
	* @param @param username
	* @param @param password
	* @param @param utype
	* @param @param openid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月17日
	*/
	BaseResp<Object> registerthird(String username, String password, String utype, 
			String openid,String inviteuserid,String deviceindex,
			String devicetype,String randomcode,String avatar);
	
	/**
	* @Title: updatethird
	* @Description: 第三方注册后，修改推荐人手机号及昵称，昵称数据库去重
	* @param @param username
	* @param @param password
	* @param @param utype
	* @param @param openid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月17日
	*/
	BaseResp<Object> updateNickName(String userid, String nickname, String invitecode,String sex,String pl);
	
	/**
	* @Title: thirdlogin
	* @Description: TODO
	* @param @param utype
	* @param @param openid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月17日
	*/
	BaseResp<Object> thirdlogin(String utype, String openid,String deviceindex);
	/**
	* @Title: updateUserInfo
	* @Description: 更新相关信息
	* @param @param userInfo
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月17日
	 */
	BaseResp<Object> updateUserInfo(UserInfo userInfo);

	/**
	 * 修改密码
	 * @param userid
	 * @param pwd
	 * @param newpwd
     * @return
     */
	BaseResp<Object> changePassword(long userid,String pwd,String newpwd);

	/**
	 * 获取用户等级特权
	 * @param grade
	 * @return
	 */
	BaseResp<Object> userlevel(int grade);

}
