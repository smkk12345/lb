package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.IssueClassifyMapper;
import com.longbei.appservice.entity.IssueClassify;
import com.longbei.appservice.service.IssueClassifyService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("issueClassifyService")
public class IssueClassifyServiceImpl implements IssueClassifyService{

    private static Logger logger = LoggerFactory.getLogger(IssueClassifyServiceImpl.class);

    @Autowired
    private IssueClassifyMapper issueClassifyMapper;
    @Autowired
    private IssueClassifyService issueClassifyService;
    @Autowired
    private IdGenerateService idGenerateService;

    public  Page<IssueClassify> selectIssueClassifyList(IssueClassify issueClassify, Integer pageNo, Integer pageSize){
        Page<IssueClassify> page = new Page<>(pageNo,pageSize);
        try {
            int totalcount = issueClassifyMapper.selectIssueClassifyListCount(issueClassify);
            pageNo = Page.setPageNo(pageNo,totalcount,pageSize);
            List<IssueClassify> classifyList = new ArrayList<IssueClassify>();
            classifyList = issueClassifyMapper.selectIssueClassifyList(issueClassify,(pageNo-1)*pageSize,pageSize);
            page.setTotalCount(totalcount);
            page.setList(classifyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    public  List<IssueClassify> selectAllIssueClassify(){
        List<IssueClassify> classifyList = new ArrayList<IssueClassify>();
        try {
            classifyList = issueClassifyMapper.selectAllIssueClassify();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classifyList;
    }

    @Override
    public BaseResp<IssueClassify> selectIssueClassifyByTypeId(Long issueClassifyTypeId) {
        BaseResp<IssueClassify> baseResp = new BaseResp<IssueClassify>();
        try {
            IssueClassify issueClassify = issueClassifyMapper.selectIssueClassifyByTypeId(issueClassifyTypeId);
            baseResp.setData(issueClassify);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectIssueClassifyByTypeId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> deleteByIssueClassifyTypeId(Long issueClassifyTypeId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        try {
            int m = issueClassifyMapper.deleteByIssueClassifyTypeId(issueClassifyTypeId);
            if(m == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("deleteByIssueClassifyTypeId error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertIssueClassify(IssueClassify issueClassify){
        BaseResp<Object> baseResp = new BaseResp<Object>();
        issueClassify.setTypeid(idGenerateService.getUniqueIdAsLong());
        issueClassify.setCreatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        issueClassify.setContentcount(0);
        try {
            int n = issueClassifyMapper.insertIssueClassify(issueClassify);
            if(n == 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("insertIssueClassify error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateByIssueClassifyTypeId(IssueClassify issueClassify) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        issueClassify.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        try {
            int n = issueClassifyMapper.updateByIssueClassifyTypeId(issueClassify);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateByIssueClassifyTypeId error and msg={}",e);
        }
        return baseResp;
    }
}
