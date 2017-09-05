package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.LiveGift;
//import com.longbei.appservice.service.LiveGift2Service;
import com.longbei.appservice.service.LiveGiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/livegift")
public class LiveGiftApiController {
	@Autowired
	private LiveGiftService liveGiftService;

	private static Logger logger = LoggerFactory.getLogger(LiveGiftApiController.class);

	@RequestMapping(value = "selectLiveGiftList")
	public BaseResp<Page<LiveGift>> selectLiveGiftList(@RequestBody LiveGift liveGift, Integer startNum, Integer pageSize){
		logger.info("selectLiveGiftList for adminservice liveGift:{},startNum={},pageSize={}", JSON.toJSONString(liveGift),startNum,pageSize);
		BaseResp<Page<LiveGift>> baseResp = new BaseResp<>();
		if (null == startNum) {
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (null == pageSize) {
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			Page<LiveGift> page  = liveGiftService.selectLiveGiftList(liveGift,startNum,pageSize);
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("selectLiveGiftList for adminservice liveGift:{},startNum={},pageSize={}", JSON.toJSONString(liveGift),startNum,pageSize,e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectLiveGiftByGiftId")
	public BaseResp<LiveGift> selectLiveGiftByGiftId(String giftId) {
		logger.info("selectLiveGiftByGiftId and giftId={}",giftId);
		BaseResp<LiveGift> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(giftId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = liveGiftService.selectLiveGiftByGiftId(Long.parseLong(giftId));
		} catch (Exception e) {
			logger.error("selectLiveGiftByGiftId and giftId={}",giftId,e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/removeLiveGiftByGiftId")
	public BaseResp<Object> removeLiveGiftByGiftId(String giftId) {
		logger.info("removeLiveGiftByGiftId and giftId={}",giftId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(giftId+"")){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = liveGiftService.removeLiveGiftByGiftId(Long.parseLong(giftId));
		} catch (Exception e) {
			logger.error("removeLiveGiftByGiftId and giftId={}",giftId,e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertLiveGift")
	public BaseResp<Object> insertLiveGift(@RequestBody LiveGift liveGift) {
		logger.info("insertLiveGift for adminservice and liveGift:{}", JSON.toJSONString(liveGift));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(liveGift.getTitle(),liveGift.getPicurl(),liveGift.getDoublehit(),liveGift.getPrice()+"")){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = liveGiftService.insertLiveGift(liveGift);
		} catch (Exception e) {
			logger.error("insertLiveGift for adminservice and liveGift:{}", JSON.toJSONString(liveGift),e);
		}
		return baseResp;
	}

	@RequestMapping(value = "/updateLiveGiftByGiftId")
	public BaseResp<Object> updateLiveGiftByGiftId(@RequestBody LiveGift liveGift) {
		logger.info("updateLiveGiftByGiftId for adminservice and liveGift:{}", JSON.toJSONString(liveGift));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(liveGift.getGiftid()+"",liveGift.getTitle(),liveGift.getPicurl(),liveGift.getDoublehit(),liveGift.getPrice()+"")){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = liveGiftService.updateLiveGiftByGiftId(liveGift);
		} catch (Exception e) {
			logger.error("updateLiveGiftByGiftId for adminservice and liveGift:{}", JSON.toJSONString(liveGift),e);

		}
		return baseResp;
	}
}
