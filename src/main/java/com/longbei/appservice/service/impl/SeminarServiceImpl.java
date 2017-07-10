package com.longbei.appservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.IdGenerateService;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.common.utils.ResultUtil;
import com.longbei.appservice.dao.*;
import com.longbei.appservice.entity.*;
import com.longbei.appservice.service.SeminarService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 专题
 *
 * @author luye
 * @create 2017-07-05 上午11:24
 **/
@Service
public class SeminarServiceImpl implements SeminarService{

    private static Logger logger = LoggerFactory.getLogger(SeminarServiceImpl.class);

    @Autowired
    private SeminarMapper seminarMapper;
    @Autowired
    private SeminarModuleMapper seminarModuleMapper;
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private ModuleContentMapper moduleContentMapper;
    @Autowired
    private IdGenerateService idGenerateService;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private VideoMapper videoMapper;


    @Override
    public BaseResp<Object> insertSeminar(Seminar seminar) {
        BaseResp baseResp = new BaseResp();
        try {
            Long seminarid = idGenerateService.getUniqueIdAsLong();
            seminar.setSeminarid(seminarid);
            int res = seminarMapper.insertSelective(seminar);
            if (res > 0){
                baseResp.initCodeAndDesp();
                baseResp.setData(seminarid);
            }
        } catch (Exception e) {
            logger.error("insert seminar:{} is error:", JSON.toJSONString(seminar),e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> insertSeminarModule(List<SeminarModule> seminarModules) {
        BaseResp baseResp = new BaseResp();
        try {
            for (SeminarModule seminarModule : seminarModules){
                Long semmodid = idGenerateService.getUniqueIdAsLong();
                seminarModule.setSemmodid(semmodid);
                int res = seminarModuleMapper.insertSelective(seminarModule);
                if (res > 0){
                    List<ModuleContent> moduleContents = seminarModule.getModuleContents();
                    for (ModuleContent moduleContent : moduleContents){
                        moduleContent.setSemmodid(String.valueOf(semmodid));
                    }
                    if (null != moduleContents && moduleContents.size() > 0){
                        moduleContentMapper.insertBatch(moduleContents);
                    }
                }
            }
            baseResp.initCodeAndDesp();
        } catch (Exception e) {
            logger.error("insert seminarModule:{} is error:", JSON.toJSONString(seminarModules),e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> deleteSeminar(String seminarid) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            int res = seminarMapper.deleteBySeminarid(Integer.parseInt(seminarid));
            if (res > 0){
                List<SeminarModule> seminarModules = seminarModuleMapper.selectList(Long.parseLong(seminarid));
                if (null != seminarModules && seminarModules.size() > 0){
                    for (SeminarModule seminarModule : seminarModules){
                        int isdel = seminarModuleMapper.deleteByPrimaryKey(seminarModule.getId());
                        if (isdel > 0){
                            moduleContentMapper.deleteBySemmodid(seminarModule.getSemmodid());
                        }
                    }
                }
            }
            baseResp.initCodeAndDesp();
        } catch (NumberFormatException e) {
            logger.error("delete seminar:{} is error:",seminarid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateSeminar(Seminar seminar) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            int res = seminarMapper.updateByPrimaryKeySelective(seminar);
            if (res > 0){
                baseResp.initCodeAndDesp();
            }
        } catch (Exception e) {
            logger.error("update seminar{} is error:",JSON.toJSONString(seminar),e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Object> updateSeminarModule(String seminarid, List<SeminarModule> seminarModules) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            baseResp = deleteSeminar(seminarid);
            if (ResultUtil.isSuccess(baseResp)){
                baseResp = insertSeminarModule(seminarModules);
            }
        } catch (Exception e) {
            logger.error("update seminar modules seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Seminar> selectSeminar(String seminarid) {
        BaseResp<Seminar> baseResp = new BaseResp<>();
        try {
            Seminar seminar = seminarMapper.selectBySeminarId(Long.parseLong(seminarid));
            baseResp.initCodeAndDesp();
            baseResp.setData(seminar);
        } catch (NumberFormatException e) {
            logger.error("select seminar seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<SeminarModule>> selectSeminarModules(String seminarid) {
        BaseResp<List<SeminarModule>> baseResp = new BaseResp<>();
        try {
            List<SeminarModule> seminarModules = seminarModuleMapper.selectList(Long.parseLong(seminarid));
            if (null != seminarModules && seminarModules.size() > 0){
                for (SeminarModule seminarModule : seminarModules){
                    List<ModuleContent> moduleContents =
                            moduleContentMapper.selectBySemmodid(String.valueOf(seminarModule.getSemmodid()));
                    if (null != moduleContents && moduleContents.size() > 0){
                        for (ModuleContent moduleContent: moduleContents){
                            switch (moduleContent.getContenttype()){
                                case "0":
                                    Article article = articleMapper.
                                            selectByPrimaryKey(Integer.parseInt(moduleContent.getContentid()));
                                    moduleContent.setArticle(article);
                                    break;
                                case "1":
                                    Rank rank = rankMapper.
                                            selectRankByRankid(Long.parseLong(moduleContent.getContentid()));
                                    moduleContent.setRank(rank);
                                    break;
                                case "2":
                                    UserInfo userInfo = userInfoMapper.
                                            selectByUserid(Long.parseLong(moduleContent.getContentid()));
                                    moduleContent.setUserInfo(userInfo);
                                    break;
                                case "3":
                                    break;
                                case "4":
                                    Map<String,Object> map = new HashedMap();
                                    map.put("videoId",moduleContent.getContentid());
                                    Video video = videoMapper.getVideo(map);
                                    moduleContent.setVideo(video);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }

            baseResp.initCodeAndDesp();
            baseResp.setData(seminarModules);
        } catch (NumberFormatException e) {
            logger.error("select seminar modules seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Page<Seminar>> selectSeminars(Seminar seminar, Integer pageNo, Integer pageSize) {
        BaseResp<Page<Seminar>> baseResp = new BaseResp<>();
        Page<Seminar> page = new Page<>(pageNo,pageSize);
        try {
            int totalcount = seminarMapper.selectCount(seminar);
            List<Seminar> seminars = seminarMapper.selectList(seminar,pageSize*(pageNo - 1),pageSize);
            page.setTotalCount(totalcount);
            page.setList(seminars);
            baseResp.initCodeAndDesp();
            baseResp.setData(page);
        } catch (Exception e) {
            logger.error("select seminars is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<List<Module>> selectModules() {
        BaseResp<List<Module>> baseResp = new BaseResp<>();
        try {
            List<Module> modules = moduleMapper.selectList();
            baseResp.initCodeAndDesp();
            baseResp.setData(modules);
        } catch (Exception e) {
            logger.error("select modules is error:",e);
        }
        return baseResp;
    }

    @Override
    public BaseResp<Seminar> selectSeminarAllDetail(String seminarid) {
        BaseResp<Seminar> baseResp = new BaseResp<>();
        try {
            baseResp = selectSeminar(seminarid);
            if (ResultUtil.isSuccess(baseResp)){
                BaseResp<List<SeminarModule>> base = selectSeminarModules(seminarid);
                baseResp.getData().setSeminarModules(base.getData());
            }
        } catch (Exception e) {
            logger.error("select seminar all detail seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }
}