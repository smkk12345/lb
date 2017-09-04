package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.UserInfo;
import com.longbei.appservice.entity.UserLevel;
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
	 * 提示用户进步币是否更改
	 */
	BaseResp<Integer> isUserMoneyHint(String userid);
	
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
			,String deviceindex,String devicetype,String avatar,String nickname);

	/**
	 * admin明星粉丝会员批量注册
	 * @param userList
	 * @return
	 */
	BaseResp<Object> fansRegisterBatch(List<UserInfo> userList, String isRandom);
	
	
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
	 * @Description: 批量发送短信
	 * @param mobiles 手机号码列表
     * @param template 短信模版id
	 * @auther IngaWu
	 * @currentdate:2017年7月18日
	 */
	BaseResp<Object> smsBatch(List<String> mobiles,String template);
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

	BaseResp<Object> checkSmsAndLogin(String mobile, String random,String deviceindex,String devicetype);

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
	 * login for pc
	 * @param username
	 * @param password
	 * @return
	 * @auther luye
	 */
	BaseResp<UserInfo> login(String username, String password);
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
			String devicetype,String randomcode,String avatar,String nickname);
	
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
	BaseResp<Object> selectRandomTagList(String userid);

	BaseResp<Object> perfectInfo(String ptype);

	/**
	 * 获取用户列表
	 * @param userInfo
	 * @param validateidcard //是否验证了身份证号码 0是未提交信息 1是验证中 2验证通过 3验证不通过
	 * @param order
	 * @param ordersc
	 * @param pageno
	 * @param pagesize
	 * @return
	 */
	BaseResp<Page<UserInfo>> selectUserList(UserInfo userInfo,String validateidcard,String order,String ordersc,Integer pageno,Integer pagesize);

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

	/**
	 * 第三方帐号绑定
	 * @param userid
	 * @param utype
	 * @param opendid
	 * @return
	 */
	BaseResp<Object> thirdbinding(String userid,String utype,String opendid);

	/**
	 * 给用户发消息
	 * @param friendid //消息发送者
	 * @param userids //消息接收者
	 * @param businesstype
	 * @param businessid
	 * @param remark
	 * @param title
	 * @return
	 */
	BaseResp<Object> sendMessagesBatch(String friendid, String[] userids , String businesstype,
								 String businessid, String remark,String title);

	/**
	 * 修改用户龙币数
	 * @param userid
	 * @param totalPrice  消耗的龙币数量
	 * @return
	 */
	BaseResp updateTotalmoneyByUserid(long userid, Integer totalPrice);

	/**
	 * @Description: 分享成功后送分送币等操作
	 * @param userid  用户id
	 * @param sharePlatform  分享平台 0龙群 1龙杯好友 2QQ空间 3QQ好友 4微信朋友圈 5微信好友 6新浪微博
	 * @auther IngaWu
	 * @currentdate:2017年6月22日
	 */
	BaseResp<Object> afterShareSuccess(Long userid, String sharePlatform);

	/**
	 * 用户通过邀请所获得的进步币
	 * @param userid
	 * @return
	 */
	BaseResp selectInviteCoinsDetail(String userid);


	BaseResp insertInviteCode(String userid,String invitecode);

	/**
	 * 获取一个随机库中的用户昵称
	 * @return
	 */
	String getRandomNickName();

	BaseResp isMoneyEnough(int money,long userid);
}
