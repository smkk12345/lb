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
	String selectRemark(Long userid, Long friendid);
	
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
	public BaseResp<Object> selectLongRangeListByUnameAndNname(long userid, String nickname, int startNum, int endNum);

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

}
