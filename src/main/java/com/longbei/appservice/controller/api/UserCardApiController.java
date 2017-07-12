package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.UserCard;
import com.longbei.appservice.service.UserCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户名片
 *
 * @author IngaWu
 * @create 2017-07-10
 **/
@RestController
@RequestMapping(value = "api/usercard")
public class UserCardApiController {

	private Logger logger = LoggerFactory.getLogger(UserCardApiController.class);

	@Autowired
	private UserCardService userCardService;

	/**
	 * 按条件查询用户名片列表
	 * @title selectUserCardList
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @author IngaWu
	 * @currentdate:2017年7月10日
	 */
	@RequestMapping(value = "selectUserCardList")
	public BaseResp<Page<UserCard>> selectUserCardList(@RequestBody UserCard userCard, Integer startNum, Integer pageSize){
		logger.info("selectUserCardList for adminservice userCard:{},pageNo={},pageSize={}", JSON.toJSONString(userCard),startNum,pageSize);
		BaseResp<Page<UserCard>> baseResp = new BaseResp<>();
		if (StringUtils.isBlank(startNum+"")){
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (StringUtils.isBlank(pageSize+"")){
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			Page<UserCard> page= userCardService.selectUserCardList(userCard,startNum,pageSize);
			baseResp.setData(page);
			baseResp.initCodeAndDesp();
		} catch (Exception e) {
			logger.error("selectUserCardList for adminservice error",e);
		}
		return baseResp;
	}

	/**
	 * @Title: selectUserCardByUserCardId
	 * @Description: 通过用户名片id查看名片详情
	 * @param  userCardId 用户名片id
	 * @auther IngaWu
	 * @currentdate:2017年7月10日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectUserCardByUserCardId")
	public BaseResp<UserCard> selectUserCardByUserCardId(String userCardId) {
		logger.info("selectUserCardByUserCardId and userCardId={}",userCardId);
		BaseResp<UserCard> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userCardId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userCardService.selectUserCardByUserCardId(Long.parseLong(userCardId));
			return baseResp;
		} catch (Exception e) {
			logger.error("selectUserCardByUserCardId and userCardId={}",userCardId,e);
		}
		return baseResp;
	}

	/**
	 * @Title: deleteByUserCardId
	 * @Description: 删除用户名片(假删)
	 * @param userCardId 用户名片id
	 * @auther IngaWu
	 * @currentdate:2017年7月10日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteByUserCardId")
	public BaseResp<Object> deleteByUserCardId(String userCardId) {
		logger.info("deleteByUserCardId and userCardId={}",userCardId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(userCardId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			return userCardService.deleteByUserCardId(Long.parseLong(userCardId));
		} catch (Exception e) {
			logger.error("deleteByUserCardId and userCardId={}",userCardId,e);
		}
		return baseResp;
	}

	/**
	 * 添加用户名片
	 * @title insertUserCard
	 * @author IngaWu
	 * @currentdate:2017年7月10日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertUserCard")
	public BaseResp<Object> insertUserCard(@RequestBody UserCard userCard) {
		logger.info("insertUserCard for adminservice userCard:{}", JSON.toJSONString(userCard));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(userCard.getAvatar(),userCard.getDisplayname(),userCard.getBrief(),userCard.getContent())){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userCardService.insertUserCard(userCard);
			return baseResp;
		} catch (Exception e) {
			logger.error("insertUserCard for adminservice userCard:{}", JSON.toJSONString(userCard),e);
		}
		return baseResp;
	}

	/**
	 * 编辑用户名片
	 * @title updateByUserCardId
	 * @author IngaWu
	 * @currentdate:2017年7月10日
	 */
	@RequestMapping(value = "/updateByUserCardId")
	public BaseResp<Object> updateByUserCardId(@RequestBody UserCard userCard) {
		logger.info("updateByUserCardId for adminservice userCard:{}", JSON.toJSONString(userCard));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(String.valueOf(userCard.getCardid()))){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = userCardService.updateByUserCardId(userCard);
		} catch (Exception e) {
			logger.error("updateByUserCardId for adminservice userCard:{}", JSON.toJSONString(userCard),e);

		}
		return baseResp;
	}
}
