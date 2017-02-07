package com.longbei.appservice.dao;

import java.util.List;

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
	SnsFriends selectByUidAndFid(@Param("userid") long userid, @Param("friendid") long friendid);
	
	List<String> selectListidByUid(@Param("userid") long userid);

	int updateByPrimaryKeySelective(SnsFriends record);

	int updateByPrimaryKey(SnsFriends record);

	int updateRemarkByUidAndFid(@Param("userid") long userid, @Param("friendid") long friendid,
			@Param("remark") String remark);

	List<SnsFriends> selectListByUsrid(@Param("userid") long userid, @Param("startNum") int startNum,
			@Param("endNum") long endNum);

	int deleteByUidAndFid(@Param("userid") long userid, @Param("friendid") long friendid);

}