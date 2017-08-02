package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveTopic;
import com.longbei.appservice.entity.SuperTopic;
import com.longbei.appservice.entity.TimeLineDetail;
import com.longbei.appservice.service.ImproveTopicService;
import com.longbei.appservice.service.SuperTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/supertopic")
public class SuperTopicApiController {
	@Autowired
	private SuperTopicService superTopicService;
	@Autowired
	private ImproveTopicService improveTopicService;

	private static Logger logger = LoggerFactory.getLogger(SuperTopicApiController.class);

	@RequestMapping(value = "selectSuperTopicList")
	public BaseResp<Page<SuperTopic>> selectSuperTopicList(@RequestBody SuperTopic superTopic, Integer startNum, Integer pageSize){
		logger.info("selectSuperTopicList for adminservice superTopic:{},startNum={},pageSize={}", JSON.toJSONString(superTopic),startNum,pageSize);
		BaseResp<Page<SuperTopic>> baseResp = new BaseResp<>();
		if (null == startNum) {
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (null == pageSize) {
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			Page<SuperTopic> page  = superTopicService.selectSuperTopicList(superTopic,startNum,pageSize);
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (Exception e) {
			logger.error("selectSuperTopicList for adminservice error",e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectSuperTopicByTopicId")
	public BaseResp<SuperTopic> selectSuperTopicByTopicId(String topicId) {
		logger.info("selectSuperTopicByTopicId and topicId={}",topicId);
		BaseResp<SuperTopic> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(topicId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = superTopicService.selectSuperTopicByTopicId(Long.parseLong(topicId));
			return baseResp;
		} catch (Exception e) {
			logger.error("selectSuperTopicByTopicId and topicId={}",topicId,e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteSuperTopicByTopicId")
	public BaseResp<Object> deleteSuperTopicByTopicId(String topicId) {
		logger.info("deleteSuperTopicByTopicId and topicId={}",topicId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(topicId+"")){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			return superTopicService.deleteSuperTopicByTopicId(Long.parseLong(topicId));
		} catch (Exception e) {
			logger.error("deleteSuperTopicByTopicId and topicId={}",topicId,e);
		}
		return baseResp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertSuperTopic")
	public BaseResp<Object> insertSuperTopic(@RequestBody SuperTopic superTopic) {
		logger.info("insertSuperTopic for adminservice superTopic:{}", JSON.toJSONString(superTopic));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(superTopic.getTitle())){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = superTopicService.insertSuperTopic(superTopic);
			return baseResp;
		} catch (Exception e) {
			logger.error("insertSuperTopic for adminservice superTopic:{}", JSON.toJSONString(superTopic),e);
		}
		return baseResp;
	}

	@RequestMapping(value = "/updateSuperTopicByTopicId")
	public BaseResp<Object> updateSuperTopicByTopicId(@RequestBody SuperTopic superTopic) {
		logger.info("updateSuperTopicByTopicId for adminservice superTopic:{}", JSON.toJSONString(superTopic));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(String.valueOf(superTopic.getTopicid()))){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = superTopicService.updateSuperTopicByTopicId(superTopic);
		} catch (Exception e) {
			logger.error("updateSuperTopicByTopicId for adminservice superTopic:{}", JSON.toJSONString(superTopic),e);

		}
		return baseResp;
	}

	//-------------------------------超级话题 end-------------------------------------


	//-------------------------------话题进步 start-----------------------------------

	/**
	 * 获取进步列表 from mongo
	 * @param topicId 话题id
	 * @param businesstype 微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
	 * @param isTopic isTopic 是否为话题 0否 1是
	 * @param brief 微进步内容
	 * @param nickname 用户昵称
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年7月29日
	 */
	@RequestMapping(value = "selectImpTopicList")
	public BaseResp<Page<TimeLineDetail>> selectImpTopicList(String topicId,String businesstype, String isTopic, String brief, String nickname, Integer startNum, Integer pageSize){
		logger.info("topicId={},businesstype={},isTopic={},brief={},nickname={},startNum={},pageSize={}", topicId,businesstype,isTopic,brief,nickname,startNum,pageSize);
		BaseResp<Page<TimeLineDetail>> baseResp = new BaseResp<>();
		if (StringUtils.isBlank(startNum+"")){
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (StringUtils.isBlank(pageSize+"")){
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			baseResp = improveTopicService.selectImpTopicList(topicId,businesstype,isTopic,brief,nickname,startNum,pageSize);
		} catch (Exception e) {
			logger.error("topicId={},businesstype={},isTopic={},brief={},nickname={},startNum={},pageSize={}", topicId,businesstype,isTopic,brief,nickname,startNum,pageSize,e);

		}
		return baseResp;
	}

	/**
	 * 获取话题进步列表 from mysql
	 * @param topicId 话题id
	 * @param businesstype 微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
	 * @param isTopic isTopic 是否为话题 0否 1是
	 * @param brief 微进步内容
	 * @param nickname 用户昵称
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @auther IngaWu
	 * @currentdate:2017年7月29日
	 */
	@RequestMapping(value = "selectListFromImpTopic")
	public BaseResp<Page<ImproveTopic>> selectListFromImpTopic(String topicId,String businesstype, String isTopic, String brief, String nickname, Integer startNum, Integer pageSize){
		logger.info("topicId={},businesstype={},isTopic={},brief={},nickname={},startNum={},pageSize={}", topicId,businesstype,isTopic,brief,nickname,startNum,pageSize);
		BaseResp<Page<ImproveTopic>> baseResp = new BaseResp<>();
		if (null == startNum){
			startNum = Integer.parseInt(Constant.DEFAULT_START_NO);
		}
		if (null == pageSize){
			pageSize = Integer.parseInt(Constant.DEFAULT_PAGE_SIZE);
		}
		try {
			baseResp = improveTopicService.selectListFromImpTopic(topicId,businesstype,isTopic,brief,nickname,startNum,pageSize);
		} catch (Exception e) {
			logger.error("topicId={},businesstype={},isTopic={},brief={},nickname={},startNum={},pageSize={}", topicId,businesstype,isTopic,brief,nickname,startNum,pageSize,e);

		}
		return baseResp;
	}

	/**
	 * 更新进步是否话题状态
	 * @param businesstype  微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
	 * @param impids 进步id
	 * @param isTopic 是否为话题 0否 1是
	 * @auther IngaWu
	 * @currentdate:2017年7月28日
	 */
	@RequestMapping(value = "updateImproveTopicStatus")
	public BaseResp<Object> updateImproveTopicStatus(String topicId,String businesstype, @RequestBody List<Long> impids, String isTopic){
		logger.info("topicId={},businesstype={},impids={},isTopic={}",topicId,businesstype,JSON.toJSONString(impids),isTopic);
		BaseResp<Object> baseResp = new BaseResp<>();
		if (StringUtils.isBlank(businesstype)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
 			baseResp = improveTopicService.updateImproveTopicStatus(topicId,businesstype,impids,isTopic);
		} catch (Exception e) {
			logger.error("topicId={},businesstype={},impids={},isTopic={}",topicId,businesstype,JSON.toJSONString(impids),isTopic,e);
		}
		return baseResp;
	}

	/**
	 * 更新进步话题排序
	 * @param impid 进步id
	 * @param businesstype  微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
	 * @param topicSort 话题排序
	 * @auther IngaWu
	 * @currentdate:2017年7月29日
	 */
	@RequestMapping(value = "updateImproveTopicSort")
	public BaseResp updateImproveTopicSort(Long impid, String businesstype, Integer topicSort) {
		logger.info("impid={},businesstype={},topicSort={}",impid, businesstype,topicSort);
		BaseResp baseResp = new BaseResp();
		try {
			baseResp = improveTopicService.updateImproveTopicSort(impid,businesstype,topicSort);
		} catch (Exception e) {
			logger.error("impid={},businesstype={},topicSort={}",impid, businesstype,topicSort,e);
		}
		return baseResp;
	}
}
