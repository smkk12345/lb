/**   
* @Title: UserRelationServiceImpl.java 
* @Package com.longbei.appservice.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 下午4:23:37 
* @version V1.0   
*/
package com.longbei.appservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.dao.SnsFansMapper;
import com.longbei.appservice.dao.SnsFriendsMapper;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.entity.SnsFriends;
import com.longbei.appservice.service.UserRelationService;

/**
 * @author smkk
 * 用户关系 service
 */
@Service
public class UserRelationServiceImpl implements UserRelationService {
	private static Logger logger = LoggerFactory.getLogger(UserRelationServiceImpl.class);
	
	@Autowired
	private SnsFriendsMapper snsFriendsMapper;
	@Autowired
	private SnsFansMapper snsFansMapper;
	/* smkk
	 * @see com.longbei.appservice.service.UserRelationService#insertFriend(long, long)
	 * 2017年1月20日
	 */
	@Override
	public BaseResp<Object> insertFriend(long userid, long friendid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		SnsFriends snsFriends = new SnsFriends(userid,friendid);
		SnsFriends snsFriends1 = new SnsFriends(friendid,userid);
		try {
			int n = snsFriendsMapper.insert(snsFriends);
			int n1 = snsFriendsMapper.insert(snsFriends1);
			if(n==1&&n1==1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("snsFriendsMapper insert error and msg={}",e);
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserRelationService#selectListByUserId(long, int, int)
	 * 2017年1月20日
	 */
	@Override
	public BaseResp<Object> selectListByUserId(long userid, int startNum, int endNum) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			List<SnsFriends> list = snsFriendsMapper.selectListByUsrid(userid,startNum,endNum);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectListByUserId error and msg = {}",e);
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserRelationService#delete(long, long)
	 * 2017年1月20日
	 */
	@Override
	public BaseResp<Object> delete(long userid, long friendid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			int n = snsFriendsMapper.deleteByUidAndFid(userid, friendid);
			int n1 = snsFriendsMapper.deleteByUidAndFid(friendid, userid);
			if(n == 1&&n1 == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("snsFriendsMapper deleteByUidAndFid error and msg={}",e);
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserRelationService#insertFans(long, long)
	 * 2017年1月20日
	 */
	@Override
	public BaseResp<Object> insertFans(long userid, long likeuserid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			SnsFans snsFans = new SnsFans(userid,likeuserid);
			SnsFans snsFans1 = new SnsFans(likeuserid,userid);
			int n = snsFansMapper.insert(snsFans);
			int n1 = snsFansMapper.insert(snsFans1);
			if(n == 1 && n1 == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error(" snsFansMapper insert error and msg = {}",e);
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserRelationService#deleteFans(long, long)
	 * 2017年1月20日
	 */
	@Override
	public BaseResp<Object> deleteFans(long userid, long likeuserid) {
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			int n = snsFansMapper.deleteByUidAndLid(userid, likeuserid);
			int n1 = snsFansMapper.deleteByUidAndLid(likeuserid, userid);
			if(n == 1&&n1 == 1){
				baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("deleteByUidAndLid error and smg = {}",e);
		}
		return baseResp;
	}

	/* smkk
	 * @see com.longbei.appservice.service.UserRelationService#selectFansListByUserId(long, int, int)
	 * 2017年1月20日
	 */
	@Override
	public BaseResp<Object> selectFansListByUserId(long userid, int startNum, int endNum) {
		// TODO Auto-generated method stub
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			List<SnsFans> list = snsFansMapper.selectFansByUserid(userid, startNum, endNum);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectFansByUserid error and msg = {}",e);
		}
		return baseResp;
	}

}
