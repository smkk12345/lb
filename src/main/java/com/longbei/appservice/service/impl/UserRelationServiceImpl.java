/**   
* @Title: UserRelationServiceImpl.java 
* @Package com.longbei.appservice.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 下午4:23:37 
* @version V1.0   
*/
package com.longbei.appservice.service.impl;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SnsFansMapper;
import com.longbei.appservice.dao.SnsFriendsMapper;
import com.longbei.appservice.dao.UserInfoMapper;
import com.longbei.appservice.dao.mongo.dao.UserMongoDao;
import com.longbei.appservice.entity.AppUserMongoEntity;
import com.longbei.appservice.entity.SnsFans;
import com.longbei.appservice.entity.SnsFriends;
import com.longbei.appservice.entity.UserInfo;
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
	@Autowired
	private UserMongoDao userMongoDao;
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
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
			if(null != list && list.size()>0){
				for (SnsFriends snsFriends : list) {
					UserInfo userInfo = userInfoMapper.selectByPrimaryKey(snsFriends.getFriendid());
					if(null != userInfo){
						snsFriends.setFriendNickname(userInfo.getNickname());
						snsFriends.setUsername(userInfo.getUsername());
						snsFriends.setFriendAvatar(userInfo.getAvatar());
						//判断该好友是否已关注
						SnsFans snsFans = snsFansMapper.selectByUidAndLikeid(userid, snsFriends.getFriendid());
						if(null != snsFans){
							snsFriends.setIslike("1");
						}
					}
				}
			}
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
		BaseResp<Object> baseResp = new BaseResp<>();
		try {
			List<SnsFans> list = snsFansMapper.selectFansByUserid(userid, startNum, endNum);
			if(null != list && list.size()>0){
				for (SnsFans snsFans : list) {
					initMsgUserInfoByLikeuserid(snsFans);
//					if(null != userInfo){
//						snsFans.setLikeNickname(userInfo.getNickname());
//						snsFans.setLiekAvatar(userInfo.getAvatar());
						//判断已关注者是否是好友关系
						SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userid, snsFans.getLikeuserid());
						if(null != snsFriends){
							snsFans.setIsfriend("1");
						}
//					}
				}
			}
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			baseResp.setData(list);
		} catch (Exception e) {
			logger.error("selectFansByUserid error and msg = {}",e);
		}
		return baseResp;
	}

	@Override
	public SnsFriends selectByUidAndFid(long userid, long friendid) {
		SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userid, friendid);
		return snsFriends;
	}

	@Override
	public BaseResp<Object> updateRemarkByUidAndFid(long userid, long friendid, String remark) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			boolean temp = updateRemark(userid, friendid, remark);
			if (temp) {
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}
		} catch (Exception e) {
			logger.error("updateRemarkByUidAndFid userid = {}, friendid = {}, remark = {}, msg = {}", userid, friendid, remark, e);
		}
		return reseResp;
	}
	
	private boolean updateRemark(long userid, long friendid, String remark){
		int temp = snsFriendsMapper.updateRemarkByUidAndFid(userid, friendid, remark);
		return temp > 0 ? true : false;
	}

	/**
	 * @author yinxc
	 * 通讯录本地搜索(手机号和昵称搜索)
	 * 2017年2月6日
	 * return_type
	 * UserRelationService
	 */
	public BaseResp<Object> selectLocalListByUnameAndNname(long userid, String nickname, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
//			List<String> friendList = snsFriendsMapper.selectListidByUid(userid);
//			List<String> fansList = snsFansMapper.selectListidByUid(userid);
//			//读取拼接ids
//			String ids = selectids(userid, friendList, fansList);
//			//type 0：本地 1：远程
//			List<UserInfo> list = userInfoMapper.selectLikeListByUnameAndNname(nickname, ids, "0", startNum, endNum);
//			if(null != list && list.size()>0){
//				for (UserInfo userInfo : list) {
//					if(friendList.contains(userInfo.getUserid())){
//						//是好友
//						userInfo.setIsfriend("1");
//					}
//					if(fansList.contains(userInfo.getUserid())){
//						//已关注
//						userInfo.setIslike("1");
//					}
//				}
//			}
			reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
		} catch (Exception e) {
			logger.error("selectLocalListByUnameAndNname userid = {}, nickname = {}, msg = {}", userid, nickname, e);
		}
		return reseResp;
	}

	/**
	 * @author yinxc
	 * 通讯录远程搜索(手机号和昵称搜索)
	 * 2017年2月6日
	 * 搜索屏蔽当前访问userid
	 */
	public BaseResp<Object> selectLongRangeListByUnameAndNname(long userid, String nickname, int startNum, int endNum) {
		BaseResp<Object> reseResp = new BaseResp<>();
		try {
			List<String> friendList = snsFriendsMapper.selectListidByUid(userid);
			List<String> fansList = snsFansMapper.selectListidByUid(userid);
			//读取拼接ids
			String ids = selectids(userid, friendList, fansList);
			//type 0：本地 1：远程
			List<UserInfo> list = userInfoMapper.selectLikeListByUnameAndNname(userid, nickname, ids, "1", startNum, endNum);
			if(null != list && list.size()>0){
				for (UserInfo userInfo : list) {
					if(friendList.contains(userInfo.getId())){
						//是好友
						userInfo.setIsfriend("1");
					}
					if(fansList.contains(userInfo.getId())){
						//已关注
						userInfo.setIslike("1");
					}
				}
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			}else{
				reseResp.initCodeAndDesp(Constant.STATUS_SYS_27, Constant.RTNINFO_SYS_27);
			}
			reseResp.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("selectLongRangeListByUnameAndNname userid = {}, nickname = {}", userid, nickname, e);
		}
		return reseResp;
	}
	
	/**
	 * @author yinxc
	 * 读取拼接ids
	 * 2017年2月6日
	 * return_type
	 * UserRelationServiceImpl
	 */
	private String selectids(long userid, List<String> friendList, List<String> fansList){
		friendList.addAll(fansList);
		//通过HashSet剔除     删除ArrayList中重复元素
		HashSet<String> h = new HashSet<String>(friendList);
		friendList.clear();
		friendList.addAll(h);
		//读取拼接ids
		String ids = "";
		ids = StringUtils.joinArr(h.toArray(), ",", 0, h.size());
//		if(null != friendList && friendList.size()>0){
//			for (String string : friendList) {
//				ids += string + ",";
//			}
//		}
//		if(ids.length()>0){
//			ids = ids.substring(0, ids.length()-1);
//		}
		return ids;
	}
	
	/**
     * 初始化消息中用户信息 ------Friendid
     * @param snsFans
     * @author:luye
     */
    private void initMsgUserInfoByLikeuserid(SnsFans snsFans){
        AppUserMongoEntity appUserMongoEntity = userMongoDao.findById(String.valueOf(snsFans.getLikeuserid()));
        snsFans.setAppUserMongoEntityLikeuserid(appUserMongoEntity);
    }

}
