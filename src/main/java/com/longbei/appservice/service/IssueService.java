package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.Issue;
import com.longbei.appservice.entity.IssueClassify;


import java.util.List;

public interface IssueService {

	/**
	 * 按条件查询帮助中心问题列表
	 * @title selectIssueList
	 * @param pageNo 页码
	 * @param pageSize 每页显示条数
	 * @author IngaWu
	 * @currentdate:2017年4月25日
	 */
	Page<Issue> selectIssueList(Issue issue, int pageNo, int pageSize);

	/**
	 * 编辑帮助中心问题
	 * @title updateIssueByIssueId
	 * @author IngaWu
	 * @currentdate:2017年4月26日
	 */
	BaseResp<Object> updateIssueByIssueId(Issue issue);

	/**
	 * @Title: deleteIssueByIssuetId
	 * @Description: 删除帮助中心问题
	 * @param  @param issueId 问题id
	 * @auther IngaWu
	 * @currentdate:2017年4月26日
	 */
	BaseResp<Object> deleteIssueByIssuetId(int issuetId);

	/**
	 * @Title: selectIssueByIssueId
	 * @Description: 通过问题id查看问题详情
	 * @param  @param issueId 问题id
	 * @param @param code 0
	 * @auther IngaWu
	 * @currentdate:2017年3月22日
	 */
	BaseResp<Issue> selectIssueByIssueId(int issuetId);

	/**
	 * 添加帮助中心问题
	 * @title insertIssue
	 * @author IngaWu
	 * @currentdate:2017年4月26日
	 */
	BaseResp<Object> insertIssue(Issue issue);

	/**
	 * 获取帮助中心类型列表
	 * @title selectIssueClassifyList
	 * @author IngaWu
	 * @currentdate:2017年4月28日
	 */
	BaseResp<List<IssueClassify>> selectIssueClassifyList();
}
