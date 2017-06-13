package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.entity.IssueClassify;

import java.util.List;

public interface IssueClassifyService {

	/**
	 * 获取帮助中心类型列表
	 * @title selectIssueClassifyList
	 * @author IngaWu
	 * @currentdate:2017年6月12日
	 */
	Page<IssueClassify> selectIssueClassifyList(IssueClassify issueClassify, Integer pageNo, Integer pageSize);

	List<IssueClassify> selectAllIssueClassify();

	/**
	 * @Title: selectIssueClassifyByTypeId
	 * @Description: 通过问题类型typeid查看类型详情
	 * @param issueClassifyTypeId 问题类型typeid
	 * @auther IngaWu
	 * @currentdate:2017年6月12日
	 */
	BaseResp<IssueClassify> selectIssueClassifyByTypeId(Long issueClassifyTypeId);

	/**
	 * @Title: deleteByIssueClassifyTypeId
	 * @Description: 删除帮助中心类型
	 * @param issueClassifyTypeId 帮助类型typeid
	 * @auther IngaWu
	 * @currentdate:2017年6月12日
	 */
	BaseResp<Object> deleteByIssueClassifyTypeId(Long issueClassifyTypeId);

	/**
	 * 添加帮助中心类型
	 * @title insertIssueClassify
	 * @author IngaWu
	 * @currentdate:2017年6月12日
	 */
	BaseResp<Object> insertIssueClassify(IssueClassify issueClassify);

	/**
	 * 编辑帮助中心类型
	 * @title updateByIssueClassifyTypeId
	 * @author IngaWu
	 * @currentdate:2017年6月12日
	 */
	BaseResp<Object> updateByIssueClassifyTypeId(IssueClassify issueClassify);
}
