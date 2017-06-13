package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Issue;
import com.longbei.appservice.entity.IssueClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IssueMapper {

    /**
     * 按条件查询帮助中心问题列表
     * @title selectIssuelist
     * @param startNum 页码
     * @param pageSize 每页显示条数
     * @author IngaWu
     * @currentdate:2017年4月25日
     */
    List<Issue> selectIssueList(@Param("issue") Issue issue,
                                   @Param("startNum")Integer startNum,
                                   @Param("pageSize")Integer pageSize);
    /**
     * 获取帮助中心问题列表数量
     * @title selectIssueListCount
     * @author IngaWu
     * @currentdate:2017年4月25日
     */
    int selectIssueListCount(@Param("issue") Issue issue);

    /**
     * 编辑帮助中心问题
     * @title updateIssueByIssueId
     * @author IngaWu
     * @currentdate:2017年4月26日
     */
    int updateIssueByIssueId(Issue data);

    /**
     * @Title: deleteIssueByIssuetId
     * @Description: 删除帮助中心问题
     * @param  @param issueId 问题id
     * @auther IngaWu
     * @currentdate:2017年4月26日
     */
    int deleteIssueByIssuetId(@Param("issuetId") int issuetId);

    /**
     * @Title: selectIssueByIssueId
     * @Description: 通过问题id查看问题详情
     * @param  @param issueId 问题id
     * @param @param code 0
     * @auther IngaWu
     * @currentdate:2017年3月22日
     */
    Issue selectIssueByIssueId(@Param("issuetId")int issuetId);

    /**
     * 添加帮助中心问题
     * @title insertIssue
     * @author IngaWu
     * @currentdate:2017年4月26日
     */
    int insertIssue(Issue data);
}