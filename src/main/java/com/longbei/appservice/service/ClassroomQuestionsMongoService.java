package com.longbei.appservice.service;

import java.util.Date;
import java.util.List;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.ClassroomQuestions;
import com.longbei.appservice.entity.ClassroomQuestionsLower;

public interface ClassroomQuestionsMongoService {
	
	/**
	 * @author yinxc
	 * 获取当前教室提问答疑总数
	 * 2017年3月7日
	 * param classroomid 教室id
	 */
	long selectCountQuestions(String classroomid);

	/**
	 * @author yinxc
	 * 添加教室提问答疑
	 * 2017年3月1日
	 */
	BaseResp<Object> insertQuestions(ClassroomQuestions classroomQuestions);
	
	/**
	 * @author yinxc
	 * 添加教室提问答疑回复---老师回复
	 * 2017年3月1日
	 */
	BaseResp<Object> insertQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower);


	/**
	 * @despripciton 更改教室答疑
	 * @author IngaWu
	 * 2017年7月21日
	 */
	BaseResp<Object> updateQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower);
	
	/**
	 * @author yinxc
	 * 获取教室提问答疑列表
	 * 2017年3月1日
	 * param classroomid 教室id
	 * param startNo  pageSize
	 */
	BaseResp<List<ClassroomQuestions>> selectQuestionsListByClassroomid(String classroomid, String userid, Date lastDate, 
			int pageSize);

	/**
	 * @Title: selectQuestionsList
	 * @Description: 获取教室提问答疑列表
	 * @param classroomId 教室id
	 * @param dealStatus 处理状态 0未回复 1已忽略 2已回复 3未忽略 4已处理(已忽略+已回复)
	 * @param nickname 用户昵称
	 * @param startCreatetime 查询提问时间开始时间
	 * @param endCreatetime 查询提问时间结束时间
	 * @param startNum 分页起始值
	 * @param pageSize 每页显示条数
	 * @author IngaWu
	 * @currentdate:2017年7月21日
	 */
	Page<ClassroomQuestions> selectQuestionsList(String classroomId,String dealStatus,String nickname,String startCreatetime, String endCreatetime, Integer startNum, Integer pageSize);

	/**
	 * @Title: selectQuestionsListCount
	 * @Description: 获取教室提问答疑列表数量
	 * @param classroomId 教室id
	 * @param dealStatus 处理状态 0未回复 1已忽略 2已回复 3未忽略 4已处理(已忽略+已回复)
	 * @param nickname 用户昵称
	 * @param startCreatetime 查询提问时间开始时间
	 * @param endCreatetime 查询提问时间结束时间
	 * @author IngaWu
	 * @currentdate:2017年7月26日
	 */
	BaseResp<Integer> selectQuestionsListCount(String classroomId,String dealStatus,String nickname,String startCreatetime, String endCreatetime);
	/**
	 * @author yinxc
	 * 获取教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	BaseResp<ClassroomQuestions> selectQuestionsByQuestionsId(String questionsId);
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	BaseResp<Object> deleteQuestions(String id, String userid);

	/**
	 * 删除教室问题
	 * @param questionsId 提问答疑id
	 */
	BaseResp<Object> deleteQuestionsByQuestionsId(String questionsId);
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑单个回复(老师)信息
	 * 2017年3月1日
	 * param id 提问答疑回复id
	 */
	BaseResp<Object> deleteQuestionsLower(String classroomid, String id, String userid);

	/**
	 * @Title: updateQuestionsIsIgnore
	 * @Description: 更改问题忽略状态
	 * @param questionsId 问题id
	 * @param isIgnore  0：未忽略  1：已忽略
	 * @auther IngaWu
	 * @currentdate:2017年7月21日
	 */
	BaseResp<Object> updateQuestionsIsIgnore(String questionsId,String isIgnore);

	/**
	 * @Title: updateQuestionsIsIgnore
	 * @Description: 更改问题答疑回复状态
	 * @param questionsId 问题id
	 * @param isReply  0：未回复  1：已回复
	 * @auther IngaWu
	 * @currentdate:2017年7月26日
	 */
	BaseResp<Object> updateQuestionsIsReply(String questionsId,String isReply);

		/**
         * @author yinxc
         * 删除教室提问答疑回复
         * 2017年3月1日
         * ClassroomQuestionsMongoService
         */
	BaseResp<Object> deleteLowerByQuestionsId(String questionsId);
	
}
