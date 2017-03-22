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
	
	/**
	 * @author yinxc
	 * 个人中心页面
	 * 2017年3月9日
	 */
	UserInfo selectInfoMore(@Param("userid") long userid);
	
	/**
	 * @author yinxc
	 * 获取个人资料    屏蔽无用的字段
	 * 2017年3月8日
	 */
	UserInfo selectByUserid(@Param("userid") long userid);

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
	
	/**
	 * @author yinxc
	 * 修改用户总花，总龙币数(龙币兑换花)
	 * 2017年3月1日
	 */
	int updateMoneyAndFlowerByUserid(@Param("userid") long userid, @Param("totalmoney") Integer totalmoney, 
			@Param("totalflower") Integer totalflower);

	/**
	 * 查询系统推荐的达人
	 * @param startNum
	 * @param pageSize
     * @return
     */
	List<UserInfo> selectFashionManUser(@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

	/**
	 * 更改用户的进步币 在用户的数据基础上直接进行加减
	 * @param userid 用户id
	 * @param coin 进步币
	 * @return
	 */
	int updateUserCoin(@Param("userid") long userid,@Param("coin") int coin);

	/**
	 * 异步更新统计信息
	 * @param userid
	 * @param totalimp
	 * @param totallikes
	 * @param totalfans
	 * @param totalflower
	 * @return
	 */
	int updateUserSumInfo(@Param("userid")long userid,
						  @Param("totalimp") int totalimp,
						  @Param("totallikes") int totallikes,
						  @Param("totalfans") int totalfans,
						  @Param("totalflower") int totalflower);
}