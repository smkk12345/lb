package com.longbei.appservice.controller.api;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.utils.ShortUrlUtils;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.config.AppserviceConfig;
import com.longbei.appservice.entity.DictArea;
import com.longbei.appservice.entity.PerfectTen;
import com.longbei.appservice.entity.SysAppupdate;
import com.longbei.appservice.service.DictAreaService;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    @Autowired
    private SysSettingService sysSettingService;


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

    /**
     * 获取龙币、进步币、人民币、花之间的兑换比例
     * @return
     */
    @RequestMapping(value = "getMoneyAndCoinExchange")
    @ResponseBody
    public BaseResp<Object> getMoneyAndCoinExchange(){
        BaseResp<Object> baseResp = new BaseResp<>();
        try {
            Map<String, Object> expandData = new HashMap<>();
            expandData.put("yuantomoney", AppserviceConfig.yuantomoney);
            expandData.put("moneytocoin", AppserviceConfig.moneytocoin);
            expandData.put("flowertocoin", AppserviceConfig.flowertocoin);
            expandData.put("flowertomoney", AppserviceConfig.flowertomoney);
            baseResp.setExpandData(expandData);
            baseResp.initCodeAndDesp();
        } catch (Exception e) {
            logger.error("getYuanToMoney is error:{}", e);
        }
        return baseResp;
    }

    @RequestMapping(value="findSysAppUpdateList")
    @ResponseBody
    public BaseResp<List<SysAppupdate>> findSysAppUpdateList(){
        logger.info("find SysAppUpdate List");
        return this.sysSettingService.findSysAppUpdateList();
    }

    @RequestMapping(value="shortUrl")
    @ResponseBody
    public String shortUrl(String url){
        logger.info("shortUrl url={}",url);
        return ShortUrlUtils.getShortUrl(url);
    }

    /**
     * 获取版本更新详情
     * @param id
     * @return
     */
    @RequestMapping(value="getSysAppUpdateDetail")
    @ResponseBody
    public BaseResp<SysAppupdate> getSysAppUpdateDetail(Integer id){
        logger.info("get sysAppUpdate detail id:{}",id);
        if(id == null){
            return new BaseResp<SysAppupdate>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.sysSettingService.getSysAppUpdateDetail(id);
    }

    /**
     * 删除版本更新
     * @param id
     * @return
     */
    @RequestMapping(value="deleteSysAppUpdate")
    public BaseResp<Object> deleteSysAppUpdate(Integer id){
        logger.info("delete sysAppUpdate id:{}",id);
        if(id == null){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        return this.sysSettingService.deleteSysAppUpdate(id);
    }

    /**
     * 添加版本更新
     * @param ttype
     * @param version
     * @param enforced
     * @param url
     * @param remark
     * @param updateexplain
     * @return
     */
    @RequestMapping(value="addSysAppUpdate")
    @ResponseBody
    public BaseResp<Object> addSysAppUpdate(String ttype,String version,String enforced,String url,String remark,String updateexplain){
        logger.info("ttype={},version={},enforced={},url={},remark={},updateexplain={}",ttype,version,enforced,url,remark,updateexplain);
        if(StringUtils.hasBlankParams(ttype,version,enforced)){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if("0".equals(ttype) && StringUtils.isEmpty(url)){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,"请上传更新包!");
        }
        return this.sysSettingService.addSysAppUpdate(ttype,version,enforced,url,remark,updateexplain);
    }

    /**
     * 编辑版本更新
     * @param ttype
     * @param version
     * @param enforced
     * @param url
     * @param remark
     * @param updateexplain
     * @return
     */
    @RequestMapping(value="updateSysAppUpdate")
    @ResponseBody
    public BaseResp<Object> updateSysAppUpdate(Integer id,String ttype,String version,String enforced,String url,String remark,String updateexplain){
        logger.info("id={},ttype={},version={},enforced={},url={},remark={},updateexplain={}",id,ttype,version,enforced,url,remark,updateexplain);
        if(id == null || StringUtils.hasBlankParams(ttype,version,enforced)){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,Constant.RTNINFO_SYS_07);
        }
        if("0".equals(ttype) && StringUtils.isEmpty(url)){
            return new BaseResp<Object>().initCodeAndDesp(Constant.STATUS_SYS_07,"请上传更新包!");
        }
        return this.sysSettingService.updateSysAppUpdate(id,ttype,version,enforced,url,remark,updateexplain);
    }
}
