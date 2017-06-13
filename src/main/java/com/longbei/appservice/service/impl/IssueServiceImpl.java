package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.IssueMapper;
import com.longbei.appservice.dao.IssueClassifyMapper;
import com.longbei.appservice.entity.Issue;
import com.longbei.appservice.entity.IssueClassify;
import com.longbei.appservice.service.IssueService;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("issueService")
public class IssueServiceImpl implements IssueService{

    private static Logger logger = LoggerFactory.getLogger(IssueServiceImpl.class);

    @Autowired
    private IssueMapper issueMapper;
    @Autowired
    private IssueClassifyMapper issueClassifyMapper;
    @Autowired
    private IssueService issueService;

    @Override
    public Page<Issue> selectIssueList(Issue issue, int pageNo, int pageSize) {
        Page<Issue> page = new Page<>(pageNo,pageSize);
        try {
            int totalcount = issueMapper.selectIssueListCount(issue);
            pageNo = Page.setPageNo(pageNo,totalcount,pageSize);
            List<Issue> issues = issueMapper.selectIssueList(issue,(pageNo-1)*pageSize,pageSize);
            if (issues.size()!=0 || issues != null){
                for (int i = 0; i < issues.size(); i++) {
                    IssueClassify issueClassify = issueClassifyMapper.selectIssueClassifyByTypeId(Long.parseLong(issues.get(i).getTypeid()));
                    if(issueClassify != null) {
                        issues.get(i).setTypetitle(issueClassify.getTypetitle());
                    }
                }
            }
            page.setTotalCount(totalcount);
            page.setList(issues);
        } catch (Exception e) {
            logger.error("select Issue list for adminservice error and msg={}",e);
        }
        return page;
    }

    @Override
    public 	BaseResp<Object> updateIssueByIssueId(Issue issue) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        issue.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        try {
            int n = issueMapper.updateIssueByIssueId(issue);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("update issue error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> deleteIssueByIssuetId(int issuetId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            int m = issueMapper.deleteIssueByIssuetId(issuetId);
            if(m == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteIssueByIssuetId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Issue> selectIssueByIssueId(int issuetId) {
        BaseResp<Issue> baseResp = new BaseResp<Issue>();
        try {
            Issue issue = issueMapper.selectIssueByIssueId(issuetId);
            baseResp.setData(issue);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectIssueByIssueId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertIssue(Issue issue){
        BaseResp<Object> baseResp = new BaseResp<Object>();

        issue.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        try {
            int n = issueMapper.insertIssue(issue);
            Integer m = issueClassifyMapper.updateContentCount(Long.parseLong(issue.getTypeid()));
            if(n == 1 && m > 0){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertIssue error and msg={}",e);
        }
        return baseResp;
    }

    public BaseResp<Object> selectIssueTypesH5() {
        BaseResp baseResp = new BaseResp();
        List<Map<String,String>> list = new ArrayList<Map<String,String>>(){{
            add(new HashedMap(){{put("id","1");put("title","关于微进步");}});
            add(new HashedMap(){{put("id","2");put("title","关于教室");}});
            add(new HashedMap(){{put("id","3");put("title","关于进步圈");}});
            add(new HashedMap(){{put("id","4");put("title","关于目标");}});
            add(new HashedMap(){{put("id","5");put("title","关于进步花");}});
            add(new HashedMap(){{put("id","6");put("title","关于个人认证");}});
        }};
        baseResp.initCodeAndDesp();
        baseResp.setData(list);
        return baseResp;
    }
}
