package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.UserAccount;

public interface UserAccountService {

	/**
	 * @Title: selectUserAccountByUserId
	 * @Description: 查看用户账号冻结详情
	 * @param  userId
	 * @auther IngaWu
	 * @currentdate:2017年6月21日
	 */
	BaseResp<UserAccount> selectUserAccountByUserId(Long userId);

	/**
	 * @Title: insertUserAccount
	 * @Description: 添加用户账号冻结
	 * @param  userAccount
	 * @param  strFreezeTime 冻结时长的value值
	 * @auther IngaWu
	 * @currentdate:2017年6月21日
	 */
	BaseResp<Object> insertUserAccount(UserAccount userAccount,String strFreezeTime);

	/**
	 * 编辑用户账号冻结详情
	 * @title updateUserAccountByUserId
     * @param  userAccount
	 * @param  strFreezeTime 冻结时长的value值
	 * @author IngaWu
	 * @currentdate:2017年6月21日
	 */
	BaseResp<Object> updateUserAccountByUserId(UserAccount userAccount,String strFreezeTime);

}
