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


}
