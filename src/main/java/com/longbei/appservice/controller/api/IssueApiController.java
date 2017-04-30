package com.longbei.appservice.controller.api;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.entity.Issue;
import com.longbei.appservice.entity.IssueClassify;
import com.longbei.appservice.service.IssueService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/issue")
public class IssueApiController {
	
	@Autowired
	private IssueService issueService;
	
	private static Logger logger = LoggerFactory.getLogger(IssueApiController.class);


	/**
	 * 获取帮助中心问题列表
	 * @title selectIssuelist
	 * @param pageNo 页码
	 * @param pageSize 每页显示条数
	 * @author IngaWu
	 * @currentdate:2017年4月25日
	 */
	@RequestMapping(value = "selectIssuelist")
	public BaseResp<Page<Issue>> selectIssuelist(@RequestBody Issue issue, String pageNo, String pageSize){
		logger.info("select Issue list for adminservice issue:{},pageNo={},pageSize={}", JSON.toJSONString(issue),pageNo,pageSize);
		Page.initPageNoAndPageSize(pageNo,pageSize);
		BaseResp<Page<Issue>> baseResp = new BaseResp<>();
		if (StringUtils.isBlank(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = Constant.DEFAULT_PAGE_SIZE;
		}
		try {
			Page<Issue> page = issueService.selectIssueList(issue,Integer.parseInt(pageNo),Integer.parseInt(pageSize));
			baseResp = BaseResp.ok();
			baseResp.setData(page);
		} catch (NumberFormatException e) {
			logger.error("select Issue list for adminservice issue:{},pageNo={},pageSize={}", JSON.toJSONString(issue),pageNo,pageSize,e);
		}
		return baseResp;
	}

	/**
	 * 编辑帮助中心问题
	 * @title updateIssueByIssueId
	 * @author IngaWu
	 * @currentdate:2017年4月26日
	 */
	@RequestMapping(value = "/updateIssueByIssueId")
	public BaseResp<Object> updateIssueByIssueId(@RequestBody Issue issue) {
		logger.info("updateIssueByIssueId for adminservice issue:{}", JSON.toJSONString(issue));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(String.valueOf(issue.getId()))){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
				 baseResp = issueService.updateIssueByIssueId(issue);
		} catch (Exception e) {
			logger.error("updateIssueByIssueId for adminservice issue:{}", JSON.toJSONString(issue),e);

		}
		return baseResp;
 	}

	/**
	 * @Title: deleteIssueByIssuetId
	 * @Description: 删除帮助中心问题
	 * @param  @param issueId 问题id
	 * @auther IngaWu
	 * @currentdate:2017年4月26日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteIssueByIssuetId")
	public BaseResp<Object> deleteIssueByIssuetId(String issuetId) {
		logger.info("deleteIssueByIssuetId and issuetId={}",issuetId);
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(issuetId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			return issueService.deleteIssueByIssuetId(Integer.parseInt(issuetId));
		} catch (Exception e) {
			logger.error("deleteIssueByIssuetId and issuetId={}",issuetId,e);
		}
		return baseResp;
	}

	/**
	 * @Title: selectIssueByIssueId
	 * @Description: 通过问题id查看问题详情
	 * @param  @param issueId 问题id
	 * @param @param code 0
	 * @auther IngaWu
	 * @currentdate:2017年3月22日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectIssueByIssueId")
	public BaseResp<Issue> selectIssueByIssueId(String issueId) {
		logger.info("selectIssueByIssueId and issueId={}",issueId);
		BaseResp<Issue> baseResp = new BaseResp<>();
		if(StringUtils.isBlank(issueId)){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = issueService.selectIssueByIssueId(Integer.parseInt(issueId));
			return baseResp;
		} catch (Exception e) {
			logger.error("selectIssueByIssueId and issueId={}",issueId,e);
		}
		return baseResp;
	}

	/**
	 * 添加帮助中心问题
	 * @title insertIssue
	 * @author IngaWu
	 * @currentdate:2017年4月26日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertIssue")
	public BaseResp<Object> insertIssue(@RequestBody Issue issue) {
		logger.info("insertIssue for adminservice issue:{}", JSON.toJSONString(issue));
		BaseResp<Object> baseResp = new BaseResp<>();
		if(StringUtils.hasBlankParams(issue.getContent(),issue.getTitle(),issue.getIshot(),issue.getTypeid())){
			return baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
		}
		try {
			baseResp = issueService.insertIssue(issue);
			return baseResp;
		} catch (Exception e) {
			logger.error("insertIssue for adminservice issue:{}", JSON.toJSONString(issue),e);
		}
		return baseResp;
	}


	@RequestMapping(value = "selectByIssueIdH5")
	public String selectByIssueIdH5(String issueId,HttpServletRequest request){
		BaseResp<Issue> baseResp = new BaseResp<Issue>();
		String callback = request.getParameter("callback");
		if(StringUtils.hasBlankParams(issueId)){
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
			return callback + "("+ JSONObject.fromObject(baseResp).toString()+")";
		}
		baseResp = issueService.selectIssueByIssueId(Integer.parseInt(issueId));
		String jsonObjectStr = JSONObject.fromObject(baseResp).toString();
		return callback + "("+jsonObjectStr+")";
	}

	@RequestMapping(value = "selectIssueTypesH5")
	public String selectIssueTypesH5(HttpServletRequest request){
		BaseResp<List<IssueClassify>> baseResp = new BaseResp<List<IssueClassify>>();
		String callback = request.getParameter("callback");
		baseResp = issueService.selectIssueClassifyList();
		String jsonObjectStr = JSONObject.fromObject(baseResp).toString();
		return callback + "("+jsonObjectStr+")";
	}

	@RequestMapping(value = "selectListByTypeH5")
	public String selectListByTypeH5(String typeId,HttpServletRequest request){
		BaseResp<Page<Issue>> baseResp = new BaseResp<Page<Issue>>();
		String callback = request.getParameter("callback");
		if(StringUtils.hasBlankParams(typeId)){
			baseResp.initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
			return callback + "("+ JSONObject.fromObject(baseResp).toString()+")";
		}
		Issue issue = new Issue();
		issue.setTypeid(typeId);
		Page<Issue> page = issueService.selectIssueList(issue,1,15);
		baseResp.setData(page);
		String jsonObjectStr = JSONObject.fromObject(baseResp).toString();
		return callback + "("+jsonObjectStr+")";

	}

	/**
	 * 获取帮助中心类型列表
	 * @title selectIssueClassifyList
	 * @author IngaWu
	 * @currentdate:2017年4月28日
	 */
	@RequestMapping(value = "selectIssueClassifyList")
	public BaseResp<List<IssueClassify>> selectIssueClassifyList(){
		BaseResp<List<IssueClassify>> baseResp =new BaseResp<List<IssueClassify>>();
		try {
			baseResp = issueService.selectIssueClassifyList();
			return baseResp;
		} catch (NumberFormatException e) {
			logger.error("selectIssueClassifyList for adminservice error",e);
		}
		return baseResp;
	}
}
