/**   
* @Title: UserRelationServiceImpl.java 
* @Package com.longbei.appservice.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月20日 下午4:23:37 
* @version V1.0   
*/
package com.longbei.appservice.service.impl;

import java.util.*;

import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.service.FriendService;
import com.longbei.appservice.service.UserBehaviourService;
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
	@Autowired
	private FriendService friendService;
	@Autowired
	private UserBehaviourService userBehaviourService;
	
	
	/**
	* @Title: selectRemark 
	* @Description: 获取好友备注信息
	* @param @param userid
	* @param @param friendid
	* @param @return    设定文件 
	* @return String    返回类型
	 */
	@Override
	public String selectRemark(Long userid, Long friendid) {
		if(userid == null || friendid == null){
			return null;
		}
		//判断已关注者是否是好友关系
		SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userid, friendid);
		if(null != snsFriends){
			if(!StringUtils.isBlank(snsFriends.getRemark())){
				//好友备注
				return snsFriends.getRemark();
			}
		}
		return "";
	}
	
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
	public BaseResp<Object> selectListByUserId(long userid, Integer startNum, Integer endNum,Date updateTime) {
		BaseResp<Object> baseResp = new BaseResp<>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			Integer isDel = null;
			if(updateTime == null){
				isDel = 0;
			}

			List<SnsFriends> list = snsFriendsMapper.selectListByUsrid(userid,startNum,endNum,updateTime,isDel);
			if(list != null && list.size()>0){
				for (SnsFriends snsFriends : list) {
					AppUserMongoEntity appUserMongoEntit =this.userMongoDao.getAppUser(snsFriends.getFriendid()+"");

					if(appUserMongoEntit == null){
						continue;
					}
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("userid",snsFriends.getFriendid());
					map.put("nickname",StringUtils.formatMobileOrUsername(appUserMongoEntit.getNickname()));
					map.put("avatar",appUserMongoEntit.getAvatar());
					map.put("username",appUserMongoEntit.getUsername());

					if(StringUtils.isNotEmpty(snsFriends.getRemark())){
						map.put("remark",snsFriends.getRemark());
					}else{
						map.put("remark",appUserMongoEntit.getNickname());
					}

					if(!"1".equals(snsFriends.getIsdel())){
						//判断该好友是否已关注
						SnsFans snsFans = snsFansMapper.selectByUidAndLikeid(userid, snsFriends.getFriendid());
						if(null != snsFans){
							map.put("isfans","1");
						}else{
							map.put("isfans","0");
						}
					}else{
						map.put("isfans","0");
					}

					//判断是怎么操作
					if(updateTime != null){
						if("1".equals(snsFriends.getIsdel())){
							map.put("operationType","delete");
						}else if(DateUtils.compare(snsFriends.getCreatetime(),updateTime)){
							map.put("operationType","insert");
						}else{
							map.put("operationType","update");
						}
					}
					resultList.add(map);
				}
			}
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
			resultMap.put("friendList",resultList);
			resultMap.put("updateTime",DateUtils.getDateTime());
			baseResp.setData(resultMap);
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
			//查看该用户是否已经关注此用户
			SnsFans tempSnsFans = this.snsFansMapper.selectByUidAndLikeid(userid,likeuserid);
			if(tempSnsFans != null){
				return baseResp.fail("您已关注了该用户,不用再次关注!");
			}

			SnsFans snsFans = new SnsFans(userid,likeuserid);
			int n = snsFansMapper.insert(snsFans);
			if(n > 0){
				userBehaviourService.userSumInfo(Constant.UserSumType.addedFans,
						userid,null,0);
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
			if(n > 0){
				userBehaviourService.userSumInfo(Constant.UserSumType.removedFans,userid,null,0);
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
	 * @param ftype 0:查询关注列表   1：粉丝列表
	 */
	@Override
	public BaseResp<List<SnsFans>> selectFansListByUserId(long userid, String ftype, Integer startNum, Integer endNum) {
		BaseResp<List<SnsFans>> baseResp = new BaseResp<>();
		try {
			List<SnsFans> list = snsFansMapper.selectFansList(userid, Integer.parseInt(ftype), startNum, endNum);
			if(null != list && list.size()>0){
				for (SnsFans snsFans : list) {
					if("1".equals(ftype)){
						//1：粉丝列表
						initMsgUserInfoByUserid(snsFans);
						//判断是否已经关注
						SnsFans fans = snsFansMapper.selectByUidAndLikeid(userid, snsFans.getUserid());
						if(null != fans){
							snsFans.getAppUserMongoEntityLikeuserid().setIsfans("1");
							snsFans.setIsfocus("1");
						}
					}else{
						initMsgUserInfoByLikeuserid(snsFans);
						//判断是否已经是粉丝
						SnsFans fans = snsFansMapper.selectByUidAndLikeid(userid, snsFans.getLikeuserid());
						if(null != fans){
							snsFans.getAppUserMongoEntityLikeuserid().setIsfans("1");
							snsFans.setIsfans("1");
						}
					}
					//判断已关注者是否是好友关系
					SnsFriends snsFriends = snsFriendsMapper.selectByUidAndFid(userid, snsFans.getLikeuserid());
					if(null != snsFriends){
						if(!StringUtils.isBlank(snsFriends.getRemark())){
							//好友备注
							snsFans.getAppUserMongoEntityLikeuserid().setNickname(snsFriends.getRemark());
						}
						snsFans.getAppUserMongoEntityLikeuserid().setIsfriend("1");
						snsFans.setIsfriend("1");
					}
				}
			}
			baseResp.setData(list);
			Map<String, Object> expandData = new HashMap<>();
			int ftypeCount = snsFansMapper.selectFansFtypeCount(userid, Integer.parseInt(ftype));
			expandData.put("ftypeCount", ftypeCount);
			baseResp.setExpandData(expandData);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
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
	public SnsFans selectByUidAndFanid(long userid, long fansid) {
		SnsFans snsFans = snsFansMapper.selectByUidAndLikeid(userid,fansid);
		return snsFans;
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
					SnsFans fans = snsFansMapper.selectByUidAndLikeid(userid, userInfo.getUserid());
					if(null != fans){
						userInfo.setIsfans("1");
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
	 * 查询系统推荐的达人
	 * @param startNum
	 * @param pageSize
     * @return
     */
	@Override
	public BaseResp<Object> selectFashionManUser(Long userId,Integer startNum, Integer pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try{
			List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			List<UserInfo> fashionManUserList = this.userInfoMapper.selectFashionManUser(startNum,pageSize);
			if(fashionManUserList != null && fashionManUserList.size() > 0){
				for(UserInfo userInfo:fashionManUserList){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("usernickname",userInfo.getNickname());
					map.put("avatar",userInfo.getAvatar());
					map.put("userid",userInfo.getUserid());

					if(userId.equals(userInfo.getUserid())){
						map.put("isfans","1");
						resultMap.add(map);
						continue;
					}
					SnsFans snsFans = this.snsFansMapper.selectByUidAndLikeid(userId,userInfo.getUserid());
					if(snsFans != null){
						map.put("isfans","1");
					}else{
						map.put("isfans","0");
					}
					SnsFriends snsFriends = this.snsFriendsMapper.selectByUidAndFid(userId,userInfo.getUserid());
					if(null == snsFriends){
						map.put("isfriend","0");
					}else{
						map.put("isfriend","1");
					}
					map.put("ptype",0);
					map.put("plevel",3);
					resultMap.add(map);
				}
			}
			baseResp.setData(resultMap);
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
		}catch(Exception e){
			logger.error("select fashionMan user errror userId:{} startNum:{} pageSize:{} msg:{}",userId,startNum,pageSize,e);
		}
		return baseResp;
	}

	/**
	 * 查询关注了likeuserId的用户列表
	 * @param likeuserId 被关注用户id
	 * @param queryUserInfo 是否查询关注者的用户信息
	 * @param startNum 开始下标
	 * @param pageSize 每页数量
     * @return
     */
	@Override
	public BaseResp<Object> selectFansListByLikeUserid(Long likeuserId,Boolean queryUserInfo, Integer startNum, Integer pageSize) {
		BaseResp<Object> baseResp = new BaseResp<Object>();
		try{
			if(queryUserInfo == null){
				queryUserInfo = false;
			}
			List<SnsFans> fansList = this.snsFansMapper.selectFansByLikeUserid(likeuserId,startNum,pageSize);
			if(queryUserInfo && fansList != null && fansList.size() > 0){
				for(SnsFans snsFans :fansList){
					snsFans.setAppUserMongoEntityLikeuserid(this.userMongoDao.getAppUser(snsFans.getUserid()+""));
				}
			}
			baseResp.setData(fansList);
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
		}catch(Exception e){
			logger.error("select fans list by likeUserid error likeuserId:{} queryUserInfo:{} msg:{}",likeuserId,queryUserInfo,e);
		}
		return baseResp;
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
        AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(snsFans.getLikeuserid()));
        snsFans.setAppUserMongoEntityLikeuserid(appUserMongoEntity);
    }

	private void initMsgUserInfoByUserid(SnsFans snsFans){
		AppUserMongoEntity appUserMongoEntity = userMongoDao.getAppUser(String.valueOf(snsFans.getUserid()));
		snsFans.setAppUserMongoEntityLikeuserid(appUserMongoEntity);
	}


}
