package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserSettingMenu;

import java.util.List;

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
	BaseResp<UserInfo> selectInfoMore(long userid,long lookid);
	
	/**
	 * @author yinxc
	 * 获取个人资料    屏蔽无用的字段
	 * 2017年3月8日
	 */
	BaseResp<Object> selectByUserid(long userid);

	UserInfo selectJustInfo(long userid);
	
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
	 * 校验验证码
	 * @param mobile
	 * @param random
	 * @return
	 */
	BaseResp<Object> checkSms(String mobile, String random);
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
	BaseResp<Object> userlevel(long userid,int grade);

	/**
	 * 获取用户等级特权
	 * @param userid
	 * @return
	 */
	BaseResp<Object> selectUserlevel(long userid);

	/**
	 * 更改用户信息
	 * @param userid
	 * @param longitude
	 * @param latitude
	 * @param dateStr
	 * @return
	 */
	BaseResp<Object> gps(long userid,double longitude,double latitude,String dateStr);

	/**
	 * 感兴趣的标签
	 * @return
	 */
	BaseResp<Object> selectRandomTagList();

	BaseResp<Object> perfectInfo(String ptype);

	/**
	 * 获取用户列表
	 * @param userInfo
	 * @param order
	 * @param ordersc
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	BaseResp<Page<UserInfo>> selectUserList(UserInfo userInfo,String order,String ordersc,Integer pageno,Integer pagesize);

	/**
	 * 更新用户状态 达人，封号等
	 * @param userInfo
	 * @return
	 */
	BaseResp<Object> updateUserStatus(UserInfo userInfo);

	/**
	 * 选择用户首页菜单
	 * @param userid
	 * @return
	 */
	BaseResp<List<UserSettingMenu>> selectMenuByUid(long userid);

	BaseResp<Object> updateBg(long userid,String bg);
}
