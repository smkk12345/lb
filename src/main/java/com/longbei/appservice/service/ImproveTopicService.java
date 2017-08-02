package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveTopic;
import com.longbei.appservice.entity.TimeLineDetail;

import java.util.Date;
import java.util.List;

public interface ImproveTopicService {

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
	BaseResp<Page<TimeLineDetail>> selectImpTopicList(String topicId,String businesstype,String isTopic, String brief, String nickname, Integer startNum, Integer pageSize);

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
	BaseResp<Page<ImproveTopic>> selectListFromImpTopic(String topicId,String businesstype,String isTopic, String brief, String nickname, Integer startNum, Integer pageSize);

	/**
	 * 更新进步是否话题状态
	 * @param businesstype  微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
	 * @param impids 进步id
	 * @param isTopic 是否为话题 0否 1是
	 * @auther IngaWu
	 * @currentdate:2017年7月28日
	 */
	BaseResp<Object> updateImproveTopicStatus(String topicId,String businesstype, List<Long> impids, String isTopic);

	/**
	 * 更新进步话题排序
	 * @param impid 进步id
	 * @param businesstype  微进步关联的业务类型 0未关联 1目标 2榜 3圈子 4教室 5教室批复作业
	 * @param topicSort 话题排序
	 * @auther IngaWu
	 * @currentdate:2017年7月29日
	 */
	BaseResp updateImproveTopicSort(Long impid,String businesstype,Integer topicSort);

}
