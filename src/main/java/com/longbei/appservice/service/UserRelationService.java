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
import com.longbei.appservice.entity.SnsFriends;

/**
 * @author smkk
 * 用户关系接口
 */
public interface UserRelationService {
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
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> selectListByUserId(long userid,int startNum,int endNum);
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
	* @param @return
	* @auther smkk
	* @currentdate:2017年1月20日
	 */
	public BaseResp<Object> selectFansListByUserId(long userid,int startNum,int endNum);

	/**
	 * @author yinxc
	 * 通讯录本地搜索(手机号和昵称搜索)
	 * 2017年2月6日
	 * return_type
	 * UserRelationService
	 */
	public BaseResp<Object> selectLocalListByUnameAndNname(long userid, String nickname);
	
	/**
	 * @author yinxc
	 * 通讯录远程搜索(手机号和昵称搜索)
	 * 2017年2月6日
	 * return_type
	 * UserRelationService
	 */
	public BaseResp<Object> selectLongRangeListByUnameAndNname(long userid, String nickname);

}
