package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
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
	 * @author yinxc
	 * 获取教室提问答疑列表
	 * 2017年3月1日
	 * param classroomid 教室id
	 * param startNo  pageSize
	 */
	BaseResp<Object> selectQuestionsListByClassroomid(String classroomid, String userid, int startNo,
			int pageSize);
	
	/**
	 * @author yinxc
	 * 获取教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	BaseResp<Object> selectQuestionsByid(String id);
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑信息
	 * 2017年3月1日
	 * param id 提问答疑id
	 */
	BaseResp<Object> deleteQuestions(String id);
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑单个回复(老师)信息
	 * 2017年3月1日
	 * param id 提问答疑回复id
	 */
	BaseResp<Object> deleteQuestionsLower(String id);
	
	/**
	 * @author yinxc
	 * 删除教室提问答疑回复
	 * 2017年3月1日
	 * return_type
	 * ClassroomQuestionsMongoService
	 */
//	BaseResp<Object> deleteLowerByQuestionsid(String questionsid);
	
}
