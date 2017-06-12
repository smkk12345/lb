package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Issue;
import com.longbei.appservice.entity.IssueClassify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IssueClassifyMapper {

    /**
     * 获取帮助中心类型列表
     * @title selectIssueClassifyList
     * @param startNum 分页起始值
     * @param pageSize 每页显示条数
     * @author IngaWu
     * @currentdate:2017年6月12日
     */
    List<IssueClassify> selectIssueClassifyList(@Param("issueClassify") IssueClassify issueClassify,
                                                @Param("startNum") Integer startNum,
                                                @Param("pageSize") Integer pageSize);

    List<IssueClassify> selectAllIssueClassify();

    /**
     * 获取帮助中心问题类型列表数量
     * @title selectIssueClassifyListCount
     * @author IngaWu
     * @currentdate:2017年6月12日
     */
    int selectIssueClassifyListCount(@Param("issueClassify") IssueClassify issueClassify);

    /**
     * @Title: selectIssueClassifyByTypeId
     * @Description: 通过问题类型typeid查看类型详情
     * @param issueClassifyTypeId 问题类型typeid
     * @auther IngaWu
     * @currentdate:2017年6月12日
     */
    IssueClassify selectIssueClassifyByTypeId(@Param("issueClassifyTypeId")Long issueClassifyTypeId);

    /**
     * @Title: deleteByIssueClassifyTypeId
     * @Description: 删除帮助中心类型
     * @param issueClassifyTypeId 帮助类型typeid
     * @auther IngaWu
     * @currentdate:2017年6月12日
     */
    Integer deleteByIssueClassifyTypeId(@Param("issueClassifyTypeId") Long issueClassifyTypeId);

    /**
     * 添加帮助中心类型
     * @title insertIssueClassify
     * @author IngaWu
     * @currentdate:2017年6月12日
     */
    Integer insertIssueClassify(IssueClassify data);

    /**
     * 编辑帮助中心类型
     * @title updateByIssueClassifyTypeId
     * @author IngaWu
     * @currentdate:2017年6月12日
     */
    Integer updateByIssueClassifyTypeId(IssueClassify data);
}