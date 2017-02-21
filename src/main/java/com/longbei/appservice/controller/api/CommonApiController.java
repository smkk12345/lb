package com.longbei.appservice.controller.api;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.entity.PerfectTen;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
