package com.longbei.appservice.controller.api;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.entity.DictArea;
import com.longbei.appservice.entity.PerfectTen;
import com.longbei.appservice.service.DictAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 通用接口
 *
 * @author luye
 * @create 2017-02-17 下午2:34
 **/
@RestController
@RequestMapping(value = "api/common")
public class CommonApiController {

    private static Logger logger = LoggerFactory.getLogger(CommonApiController.class);


    @Autowired
    private DictAreaService dictAreaService;



    /**
     * 获取十全十美列表
     * @return
     */
    @RequestMapping(value = "/perfectlist")
    public BaseResp<List<PerfectTen>> getPerfectList(){
        BaseResp<List<PerfectTen>> baseResp = BaseResp.ok();
        baseResp.setData(SysRulesCache.perfectTenList);
        return baseResp;
    }

    @RequestMapping(value = "/selectCityList")
    @ResponseBody
    public BaseResp<List<DictArea>> selectCityList(String pid) {
        logger.info("selectCityList and pid={}",pid);
        BaseResp<List<DictArea>> baseResp = new BaseResp<>();
        try {
            baseResp = dictAreaService.selectCityList(pid,null,null);
            return baseResp;
        } catch (Exception e) {
            logger.error("selectCityList and pid={} is error:",pid,e);
        }
        return baseResp;
    }

}
