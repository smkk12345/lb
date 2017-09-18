package com.longbei.appservice.controller.api;

import com.longbei.appservice.cache.CommonCache;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.Module;
import com.longbei.appservice.entity.Seminar;
import com.longbei.appservice.entity.SeminarModule;
import com.longbei.appservice.service.SeminarService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 专题
 *
 * @author luye
 * @create 2017-07-05 上午10:54
 **/
@RestController
@RequestMapping("api/seminar")
public class SeminarApiController {

    private static Logger logger = LoggerFactory.getLogger(SeminarApiController.class);

    @Autowired
    private SeminarService seminarService;
    @Autowired
    private CommonCache commonCache;

    /**
     * 获取专题列表
     * @param seminar
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public BaseResp<Page<Seminar>> selectSerminars(@RequestBody Seminar seminar,
                                                   @RequestParam("pageNo") String pageNo,
                                                   @RequestParam("pageSize") String pageSize){
        BaseResp<Page<Seminar>> baseResp = new BaseResp();
        try {
            baseResp = seminarService.selectSeminars(seminar,Integer.parseInt(pageNo)
                    ,Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error("select serminar list is error:",e);
        }
        return baseResp;
    }

    /**
     * 获取模块列表
     * @return
     */
    @RequestMapping(value = "modulelist",method = RequestMethod.POST)
    public BaseResp<List<Module>> selectModules(){
        BaseResp<List<Module>> baseResp = new BaseResp();
        try {
            baseResp = seminarService.selectModules();
        } catch (Exception e) {
            logger.error("select module list is error:",e);
        }
        return baseResp;
    }


    /**
     * 获取专题详情
     * @param seminarid
     * @return
     */
    @RequestMapping(value = "info",method = RequestMethod.POST)
    public BaseResp<Seminar> selectSerminar(String seminarid){
        BaseResp<Seminar> baseResp = new BaseResp();
        try {
            baseResp = seminarService.selectSeminar(seminarid);
        } catch (Exception e) {
            logger.error("select seminar info seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }

    /**
     * 获取专题详情包括模块详情
     * @param seminarid
     * @return
     */
    @RequestMapping(value = "allinfo",method = RequestMethod.POST)
    public BaseResp<Seminar> selectSerminarAllDetail(String seminarid){
        BaseResp<Seminar> baseResp = new BaseResp();
        try {
            String shareurl = AppserviceConfig.seminarurl + "?seminarid=" + seminarid + "&ref=share";
            String shorturl = commonCache.getShortUrl(shareurl);
            baseResp = seminarService.selectSeminarAllDetail(seminarid);
            Map<String,Object> map = new HashedMap();
            map.put("shorturl",shorturl);
            baseResp.setExpandData(map);
        } catch (Exception e) {
            logger.error("select seminar all info seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }



    /**
     * 获取专题模块详情
     * @param seminarid
     * @return
     */
    @RequestMapping(value = "moduleinfo",method = RequestMethod.POST)
    public BaseResp<List<SeminarModule>> selectSerminarModules(String seminarid){
        BaseResp<List<SeminarModule>> baseResp = new BaseResp();
        try {
            baseResp = seminarService.selectSeminarModules(seminarid);
        } catch (Exception e) {
            logger.error("select seminar info seminarid:{} is error:",seminarid,e);
        }
        return baseResp;
    }


    /**
     * 添加专题
     * @param seminar
     * @return
     */
    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public BaseResp<String> insertSerminar(@RequestBody Seminar seminar){
        BaseResp<String> baseResp = new BaseResp();
        try {
            baseResp = seminarService.insertSeminar(seminar);
        } catch (Exception e) {
            logger.error("insert serminar is error:",e);
        }
        return baseResp;
    }


    /**
     * 添加专题内容
     * @param seminarModules
     * @return
     */
    @RequestMapping(value = "insertsm",method = RequestMethod.POST)
    public BaseResp<Object> insertSeminarModule(@RequestBody List<SeminarModule> seminarModules){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = seminarService.insertSeminarModule(seminarModules);
        } catch (Exception e) {
            logger.error("insert serminar module is error:",e);
        }
        return baseResp;
    }


    /**
     * 编辑专题基本信息
     * @param seminar
     * @return
     */
    @RequestMapping(value = "edit",method = RequestMethod.POST)
    public BaseResp<Object> editSerminar(@RequestBody Seminar seminar){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = seminarService.updateSeminar(seminar);
        } catch (Exception e) {
            logger.error("update serminar is error:",e);
        }
        return baseResp;
    }


    /**
     * 编辑专题模块内容
     * @param seminarModules
     * @return
     */
    @RequestMapping(value = "editsm",method = RequestMethod.POST)
    public BaseResp<Object> editSeminarModule(@RequestBody List<SeminarModule> seminarModules,
                                              String semiarid){
        BaseResp baseResp = new BaseResp();
        try {
            baseResp = seminarService.updateSeminarModule(semiarid,seminarModules);
        } catch (Exception e) {
            logger.error("update seminar modules ",e);
        }
        return baseResp;
    }


    /**
     * 删除专题
     * @param seminarid
     * @return
     */
    @RequestMapping(value = "delete")
    public BaseResp<Object> deleteSeminar(String seminarid){
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
             baseResp = seminarService.deleteSeminar(seminarid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResp;
    }




}
