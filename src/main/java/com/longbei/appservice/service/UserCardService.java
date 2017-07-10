package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.UserCard;

public interface UserCardService {

	/**
	 * 按条件查询用户名片列表
	 * @title selectUserCardList
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @author IngaWu
	 * @currentdate:2017年7月10日
	 */
	Page<UserCard> selectUserCardList(UserCard userCard, Integer startNum, Integer pageSize);

	/**
	 * @Title: selectUserCardByUserCardId
	 * @Description: 通过用户名片id查看名片详情
	 * @param  userCardId 用户名片id
	 * @auther IngaWu
	 * @currentdate:2017年7月10日
	 */
	BaseResp<UserCard> selectUserCardByUserCardId(Long userCardId);

	/**
	 * @Title: deleteByUserCardId
	 * @Description: 删除用户名片(假删)
	 * @param userCardId 用户名片id
	 * @auther IngaWu
	 * @currentdate:2017年7月10日
	 */
	BaseResp<Object> deleteByUserCardId(Long userCardId);

	/**
	 * 添加用户名片
	 * @title insertUserCard
	 * @author IngaWu
	 * @currentdate:2017年7月10日
	 */
	BaseResp<Object> insertUserCard(UserCard userCard);

	/**
	 * 编辑用户名片
	 * @title updateByUserCardId
	 * @author IngaWu
	 * @currentdate:2017年7月10日
	 */
	BaseResp<Object> updateByUserCardId(UserCard userCard);
}
