package com.longbei.appservice.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.longbei.appservice.entity.SnsFriends;
import org.apache.ibatis.annotations.Param;

public interface SnsFriendsMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(SnsFriends record);

	int insertSelective(SnsFriends record);

	SnsFriends selectByPrimaryKey(Integer id);
	
	/**
	 * @author yinxc
	 * 获取好友信息
	 * 2017年2月4日
	 * return_type
	 * SnsFriendsService
	 */
	SnsFriends selectByUidAndFid(@Param("userid") long userid, @Param("friendid") long friendid,
								 @Param("isdel") String isdel);
	
	Set<String> selectListidByUid(@Param("userid") String userid);

	int updateByPrimaryKeySelective(SnsFriends record);

	int updateByPrimaryKey(SnsFriends record);

	int updateRemarkByUidAndFid(@Param("userid") long userid, @Param("friendid") long friendid,
			@Param("remark") String remark);

	List<SnsFriends> selectListByUsrid(@Param("userid") long userid, @Param("startNum") Integer startNum,
			@Param("endNum") Integer endNum,@Param("updateTime") Date updateTime,@Param("isDel") Integer isDel);
	
	List<SnsFriends> selectListByUid(@Param("userid") long userid);

	int deleteByUidAndFid(@Param("userid") long userid, @Param("friendid") long friendid);

	/**
	 * 更改好友的信息
     * @return
     */
	int updateByUidAndFid(Map<String,Object> map);
	
	/*
	 * 获取好友数量
	 */
	Integer selectFriendsCount(@Param("userid") long userid);

	/**
	 * 根据userid获取好友列表
	 * @param map
	 * @return
	 */
	List<SnsFriends> selectFriendRemarkList(Map<String, Object> map);
}