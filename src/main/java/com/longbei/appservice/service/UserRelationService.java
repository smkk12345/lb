/**   
* @Title: UserRelationService.java 
* @Package com.longbei.appservice.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 下午4:15:03 
* @version V1.0   
*/
package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author smkk
 * 用户关系接口
 */
public interface UserRelationService {
	
	/**
	* @Title: selectRemark 
	* @Description: 获取好友备注信息
	* @param @param userid
	* @param @param friendid
	* @param @return    设定文件 
	* @return String    返回类型
	 */
	String selectRemark(Long userid, Long friendid, String isdel);
	
	/**
	* @Title: insertFriend
	* @Description: 添加好友
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> insertFriend(long userid,long friendid);
	/**
	* @Title: selectListByUserId
	* @Description: 查询好友列表
	* @param @param userid
	* @param updateTime 更新时间
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> selectListByUserId(long userid,Integer startNum,Integer endNum,Date updateTime);
	/**
	* @Title: delete
	* @Description: 删除好友
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> delete(long userid,long friendid);
	
	/**
	 * @author yinxc
	 * 获取好友信息
	 * 2017年2月4日
	 * return_type
	 * SnsFriendsService
	 */
	SnsFriends selectByUidAndFid(long userid, long friendid);

	/*
	 * 获取好友数量
	 */
	Integer selectFriendsCount(long userid);

	/**
	 * 获取粉丝信息
	 * @param userid
	 * @param fansid
	 * @return
	 */
	SnsFans selectByUidAndFanid(long userid,long fansid);
	/**
	 * @author yinxc
	 * 修改好友备注信息
	 * 2017年2月4日
	 * return_type
	 * SnsFriendsService
	 */
	BaseResp<Object> updateRemarkByUidAndFid(long userid, long friendid, String remark);
	
	/**
	* @Title: insertFans
	* @Description: 关注
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> insertFans(long userid,long friendid);
	/**
	* @Title: deleteFans
	* @Description: 删除关注
	* @param @param userid
	* @param @param friendid
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> deleteFans(long userid,long friendid);
	/**
	* @Title: selectFansListByUserId
	* @Description: 获取关注列表
	* @param @param userid
	 * @param ftype 0:查询关注列表   1：粉丝列表
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	BaseResp<List<SnsFans>> selectFansListByUserId(long userid, long friendid, String ftype, Integer startNum, Integer endNum);

	/**
	 * @author yinxc
	 * 通讯录本地搜索(手机号和昵称搜索)
	 * 2017年2月6日
	 * return_type
	 * UserRelationService
	 */
	BaseResp<Object> selectLocalListByUnameAndNname(long userid, String nickname, int startNum, int endNum);
	
	/**
	 * @author yinxc
	 * 通讯录远程搜索(手机号和昵称搜索)
	 * 2017年2月6日
	 * return_type
	 * UserRelationService
	 */
	public BaseResp<Object> selectLongRangeListByUnameAndNname(long userid, String nickname, int startNum, int endNum,Integer searchFashinMan);

	/**
	 * 查询系统推荐的达人
	 * @param startNum
	 * @param pageSize
     * @return
     */
	BaseResp<Object> selectFashionManUser(long userId,Integer startNum, Integer pageSize);

	/**
	 * 查询关注了likeuserId的用户列表
	 * @param likeuserId 被关注用户id
	 * @param queryUserInfo 是否查询关注者的用户信息
	 * @param startNum 开始下标
	 * @param pageSize 每页数量
     * @return
     */
	BaseResp<Object> selectFansListByLikeUserid(Long likeuserId,Boolean queryUserInfo, Integer startNum, Integer pageSize);

	boolean syncUserRelationInfo(String uid);

	boolean syncUserRelationInfo(String uid,String otheruid);

	BaseResp<List<AppUserMongoEntity>> selectRelationList(String userid, String dataStr);

	/**
	 * 获取用户的好友用户id列表
	 * @param userid
	 * @return
	 */
	Set<String> getFriendIds(String userid);
	//功能同上面的方法
	Set<String> getFriendIds(Long userid);

	/**
	 * redis缓存的好友id列表中 添加好友id
	 * @desc 往userid中插入friendid时 也同时会往friendid的好友id列表中插入userid
	 * @param userid 当前登录用户id
	 * @param friendId 好友id
     */
	void addFriendId(Long userid,Long friendId);

	/**
	 * 刪除redis中緩存的用戶id
	 * @desc 删除userid中的friendid时,同时也会删除friendid中缓存的userid
	 * @param userid 当前登录用户id
	 * @param friendid 好友的id
     */
	void deleteFriendId(Long userid,Long friendid);

	/**
	 * 判断是否是好友
	 * @param userid 代表当前登录用户id
	 * @param friendid
	 * @return
	 */
	boolean checkIsFriend(Long userid,Long friendid);
	//功能同以上方法
	boolean checkIsFriend(String userid,String friendid);

	/**
	 * 获取用户的关注用户id列表
	 * @param userid
	 * @return
	 */
	Set<String> getFansIds(Long userid);

	Set<String> getFansIds(String userid);

	/**
	 * 获取粉丝id列表
	 */
	Set<String> getBeFansedIds(String userid);

	/**
	 * redis中 添加关注的用户id
	 * @param userid 当前登录用户id
	 * @param likeFansId 被关注的用户id
     */
	void addFansId(Long userid,Long likeFansId);

	/**
	 * redis中 删除用户 关注的用户id
	 * @param userid
	 * @param likeUserId
     */
	void deleteFansId(Long userid,Long likeUserId);

	/**
	 * 判断是否已经关注该用户
	 * @param userid 当前登录用户id
	 * @param fansid
     * @return
     */
	boolean checkIsFans(Long userid,Long fansid);

	/**
	 * @Title: 获取用户备注的好友 列表
	 * @Description: 只会获取用户已经备注的好友
	 * @param @param userid
	 * @return Map<String1,String2>    返回类型 String1:好友的id String2备注的昵称
	 */
	Map<String,String> selectFriendRemarkList(String userid);
	//功能同上面方法,传入的参数数据格式不一样 只是为了兼容更多接口
	Map<String,String> selectFriendRemarkList(Long userid);

	/**
	 * 更改用户的备注 昵称
	 * @param currentUserId 当前登录用户id
	 * @param appUserMongoEntity 用户信息
     * @return
     */
	void updateFriendRemark(String currentUserId, AppUserMongoEntity appUserMongoEntity);
	//功能同上面接口
	void updateFriendRemark(Long currentUserId, AppUserMongoEntity appUserMongoEntity);

	/**
	 * 获取好友的备注昵称
	 * @param currentUserId
	 * @param friendId
     * @return
     */
	String getUserRemark(String currentUserId,String friendId);
	//功能同以上方法
	String getUserRemark(Long currentUserId,Long friendId);

	/**
	 * 添加/更改 用户 在redis中缓存的昵称
	 * @param currentUserId 当前登录用户id
	 * @param friendId 好友id
	 * @param remark 备注名字
     * @return
     */
	void addOrUpdateUserRemark(String currentUserId,String friendId,String remark);
	//功能同以上方法
	void addOrUpdateUserRemark(Long currentUserId,Long friendId,String remark);

	/**
	 * 刪除好友备注
	 * @param currentUserId 当前登录用户id
	 * @param friendId 好友id
     */
	void deleteUserRemark(String currentUserId,String friendId);
	//同以上方法
	void deleteUserRemark(Long currentUserId,Long friendId);
}
