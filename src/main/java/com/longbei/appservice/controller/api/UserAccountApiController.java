package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserAccount;
import com.longbei.appservice.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/useraccount")
public class UserAccountApiController {
	
	@Autowired
	private UserAccountService userAccountService;
	
	private static Logger logger = LoggerFactory.getLogger(UserAccountApiController.class);

	/**
	 * @Title: selectUserAccountByUserId
	 * @Description: 查看用户账号冻结详情
	 * @param  userId
	 * @auther IngaWu
	 * @currentdate:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectUserAccountByUserId")
	public BaseResp<UserAccount> selectUserAccountByUserId(String userId) {
		logger.info("selectUserAccountByUserId and userId={}",userId);
		BaseResp<UserAccount> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userAccountService.selectUserAccountByUserId(Long.parseLong(userId));
			return baseResp;
		} catch (Exception e) {
			logger.error("selectUserAccountByUserId and userId={}",userId,e);
		}
		return baseResp;
	}

	/**
	 * @Title: insertUserAccount
	 * @Description: 添加用户账号冻结
	 * @param  userAccount
	 * @param  strFreezeTime 冻结时长的value值
	 * @auther IngaWu
	 * @currentdate:2017年6月21日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertUserAccount")
	public BaseResp<Object> insertUserAccount(@RequestBody UserAccount userAccount,String strFreezeTime) {
		logger.info("insertUserAccount for adminservice and userAccount:{},strFreezeTime={}", JSON.toJSONString(userAccount),strFreezeTime);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(userAccount.getUserid()+"",userAccount.getRemark(),strFreezeTime)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userAccountService.insertUserAccount(userAccount,strFreezeTime);
			return baseResp;
		} catch (Exception e) {
			logger.error("insertUserAccount for adminservice and userAccount:{},strFreezeTime={}", JSON.toJSONString(userAccount),strFreezeTime,e);
		}
		return baseResp;
	}

	/**
	 * 编辑用户账号冻结详情
	 * @title updateUserAccountByUserId
	 * @param  userAccount
	 * @param  strFreezeTime 冻结时长的value值
	 * @author IngaWu
	 * @currentdate:2017年6月21日
	 */
	@RequestMapping(value = "/updateUserAccountByUserId")
	public BaseResp<Object> updateUserAccountByUserId(@RequestBody UserAccount userAccount,String strFreezeTime) {
		logger.info("updateUserAccountByUserId for adminservice and userAccount:{},strFreezeTime={}", JSON.toJSONString(userAccount),strFreezeTime);
		BaseResp<Object> baseResp = new BaseResp<>();
		if("0".equals(userAccount.getStatus())){
			if(StringUtils.isBlank(userAccount.getUserid()+"")){
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
			}
		}else {
			if (StringUtils.hasBlankParams(userAccount.getUserid() + "", strFreezeTime,userAccount.getRemark())) {
				return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07, Constant.RTNINFO_SYS_07);
			}
		}
		try {
			baseResp = userAccountService.updateUserAccountByUserId(userAccount,strFreezeTime);
		} catch (Exception e) {
			logger.error("updateUserAccountByUserId for adminservice and userAccount:{},strFreezeTime={}", JSON.toJSONString(userAccount),strFreezeTime,e);

		}
		return baseResp;
	}
}

