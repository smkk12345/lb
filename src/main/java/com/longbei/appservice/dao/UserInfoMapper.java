package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserInfo;

public interface UserInfoMapper {
	int deleteByPrimaryKey(long id);

	int insert(UserInfo record);

	int insertSelective(UserInfo record);

	UserInfo selectByPrimaryKey(@Param("userid") long userid);

	/**
	 * @Title: getByUserName
	 * @Description: 通过手机号获取用户基本信息
	 * @param @param
	 *            username
	 * @param @return
	 * @auther smkk
	 * @currentdate:2017年1月17日
	 */
	UserInfo getByUserName(String username);

	int updateByUseridSelective(UserInfo userInfo);

	int updateByPrimaryKey(UserInfo record);

	UserInfo getByNickName(String nickname);

	/**
	 * @author yinxc 通讯录本地及远程搜索(手机号和昵称搜索) 
	 * type 0：本地 1：远程 2017年2月6日 
	 * 搜索屏蔽当前访问userid
	 */
	List<UserInfo> selectLikeListByUnameAndNname(@Param("userid") long userid, @Param("nickname") String nickname, @Param("ids") String ids,
			@Param("type") String type, @Param("startNum") int startNum, @Param("endNum") int endNum);

	/**
	 * 更新设备号和设备类型
	 * @param userInfo
	 * @return
     */
	int updateDeviceIndexByUserName(UserInfo userInfo);

	int updatePointByUserid(UserInfo record);
	
	/**
	 * @author yinxc
	 * 修改用户进步币数量
	 * 2017年3月1日
	 */
	int updateTotalcoinByUserid(@Param("userid") long userid, @Param("totalcoin") Integer totalcoin);
	
	/**
	 * @author yinxc
	 * 修改用户龙币数量
	 * 2017年3月1日
	 */
	int updateTotalmoneyByUserid(@Param("userid") long userid, @Param("totalmoney") Integer totalmoney);
	
}