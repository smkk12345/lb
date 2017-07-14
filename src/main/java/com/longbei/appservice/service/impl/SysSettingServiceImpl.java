package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Cache.SysRulesCache;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.constant.Constant_Imp_Icon;
import com.longbei.appservice.common.constant.Constant_point;
import com.longbei.appservice.common.utils.StringUtils;
import com.longbei.appservice.dao.SysAppupdateMapper;
import com.longbei.appservice.dao.SysCommonMapper;
import com.longbei.appservice.dao.SysLongbeiinfoMapper;
import com.longbei.appservice.entity.SysAppupdate;
import com.longbei.appservice.entity.SysCommon;
import com.longbei.appservice.entity.SysLongbeiinfo;
import com.longbei.appservice.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smkk on 17/2/17.
 */
@Service
public class SysSettingServiceImpl implements SysSettingService {
    @Autowired
    private SysLongbeiinfoMapper sysLongbeiinfoMapper;
    @Autowired
    private SysAppupdateMapper sysAppupdateMapper;
    @Autowired
    private SysCommonMapper sysCommonMapper;

    private static Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);

    @Override
    public BaseResp<Object> selectCompanyInfo() {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            SysLongbeiinfo sysLongbeiinfo = sysLongbeiinfoMapper.selectDefault();
            baseResp.setData(sysLongbeiinfo);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
        }catch (Exception e){
            logger.error("selectCompanyInfo error and msg={}", e);
        }
        return baseResp;
    }

    private int getMinSize(int ia,int ib){
        if(ia>ib){
            return ib;
        }else {
            return ia;
        }
    }

    /**
     * 获取版本更新的列表
     * @return
     */
    @Override
    public BaseResp<List<SysAppupdate>> findSysAppUpdateList() {
        BaseResp<List<SysAppupdate>> baseResp = new BaseResp<>();
        try{
            List<SysAppupdate> list = this.sysAppupdateMapper.findSysAppUpdateList();
            baseResp.setData(list);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("find sysAppupdate list error errorMsg:{}",e);
        }
        return baseResp;
    }

    /**
     * 获取版本更新的详情
     * @param id
     * @return
     */
    @Override
    public BaseResp<SysAppupdate> getSysAppUpdateDetail(Integer id) {
        BaseResp<SysAppupdate> baseResp = new BaseResp<>();
        try{
            SysAppupdate sysAppupdate = this.sysAppupdateMapper.selectByPrimaryKey(id);
            baseResp.setData(sysAppupdate);
            return baseResp.initCodeAndDesp();
        }catch(Exception e){
            logger.error("get sysAppUpdate detail error errorMsg:{}",e);
        }
        return baseResp;
    }

    /**
     * 删除版本更新
     * @param id
     * @return
     */
    @Override
    public BaseResp<Object> deleteSysAppUpdate(Integer id) {
        BaseResp<Object> baseResp = new BaseResp<>();
        try{
            int row = this.sysAppupdateMapper.deleteByPrimaryKey(id);
            if(row > 0){
                initSysAppUpdateMap();
                return baseResp.initCodeAndDesp();
            }
        }catch(Exception e){
            logger.error("delete sysAppUpdate error id:{}",id);
        }
        return baseResp;
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
    @Override
    public BaseResp<Object> addSysAppUpdate(String ttype, String version, String enforced, String url, String remark, String updateexplain) {
        SysAppupdate sysAppupdate = new SysAppupdate();
        sysAppupdate.setTtype(ttype);
        sysAppupdate.setVersion(version);
        sysAppupdate.setEnforced(enforced);
        sysAppupdate.setUrl(url);
        sysAppupdate.setRemark(remark);
        sysAppupdate.setUpdateexplain(updateexplain);
        sysAppupdate.setUpdatetime(new Date());

        if("0".equals(enforced)){
            //获取最近的一次强制更新的版本
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ttype",ttype);
            SysAppupdate sysAppupdate1 = this.sysAppupdateMapper.getEnforcedVersion(map);
            if(sysAppupdate1 != null){
                sysAppupdate.setEnforceversion(sysAppupdate1.getVersion());
            }
        }else{
            sysAppupdate.setEnforceversion(version);
        }

        int row = this.sysAppupdateMapper.insertSelective(sysAppupdate);
        if(row > 0){
            initSysAppUpdateMap();
            return new BaseResp<>().ok();
        }
        return new BaseResp<>().fail();
    }

    /**
     * 编辑版本更新
     * @param id
     * @param ttype
     * @param version
     * @param enforced
     * @param url
     * @param remark
     * @param updateexplain
     * @return
     */
    @Override
    public BaseResp<Object> updateSysAppUpdate(Integer id, String ttype, String version, String enforced, String url, String remark, String updateexplain) {
        SysAppupdate sysAppupdate = new SysAppupdate();
        sysAppupdate.setId(id);
        sysAppupdate.setTtype(ttype);
        sysAppupdate.setVersion(version);
        sysAppupdate.setEnforced(enforced);
        sysAppupdate.setUrl(url);
        sysAppupdate.setRemark(remark);
        sysAppupdate.setUpdateexplain(updateexplain);
        sysAppupdate.setUpdatetime(new Date());

        if("0".equals(enforced)){
            //获取最近的一次强制更新的版本
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ttype",ttype);
            map.put("id",id);
            SysAppupdate sysAppupdate1 = this.sysAppupdateMapper.getEnforcedVersion(map);
            if(sysAppupdate1 != null){
                sysAppupdate.setEnforceversion(sysAppupdate1.getVersion());
            }
        }else{
            sysAppupdate.setEnforceversion(version);
        }

        int row = this.sysAppupdateMapper.updateByPrimaryKeySelective(sysAppupdate);
        if(row > 0){
            initSysAppUpdateMap();
            return new BaseResp<>().ok();
        }
        return new BaseResp<>().fail();
    }

    @Override
    public BaseResp<Object> upGrade(String ttype, String version) {
        BaseResp<Object> baseResp = new BaseResp<>();
        SysAppupdate app = sysAppupdateMapper.selectRecentByKey(ttype);
        if(null == app){
            return baseResp.initCodeAndDesp();
        }
        String newVersion = app.getVersion().replaceAll("\\.", "");
        String cversion = version.replaceAll("\\.","");
        String enforceversion = app.getEnforceversion();//是否强制更新
        int size = 0;
        if(!StringUtils.isBlank(enforceversion)){
            enforceversion = enforceversion.replaceAll("\\.","");
            size = getMinSize(newVersion.length(),enforceversion.length());
            size = getMinSize(size,cversion.length());
        }else{
            size = getMinSize(newVersion.length(),cversion.length());
        }

        boolean isNew = false;
        boolean isC = false;
        boolean isFor = false;
        boolean isforC = false;
        for (int i = 0; i < size; i++) {
            int newi = newVersion.charAt(i);

            int versioni = cversion.charAt(i);
            if(!isNew&&!isC){
                if(versioni < newi){
                    isNew = true;
                }else if(versioni > newi){
                    isC = true;
                }
            }
            if(!StringUtils.isBlank(enforceversion)){
                int enforceveri = enforceversion.charAt(i);
                if(!isFor&&!isforC){
                    if(versioni < enforceveri){
                        isFor = true;
                    }else if(versioni > enforceveri){
                        isforC = true;
                    }
                }
            }

        }

        if(isNew){
            if(app.getEnforced().equals("1")){
            }else{
                if(isFor){
                    app.setEnforced("1");
                }
            }
            baseResp.setData(app);
        }else {
            baseResp.setData(null);
        }
        baseResp.initCodeAndDesp();
        return baseResp;
    }

    /**
     * 获取版本号相关
     * @param ttype
     * @return
     */
    @Override
    public SysAppupdate selectRecentByKey(String ttype) {
        SysAppupdate app = sysAppupdateMapper.selectRecentByKey(ttype);
        return app;
    }

    /**
     * 获取分享app的相关信息
     * @return
     */
    @Override
    public BaseResp<Object> getShareInfo() {
        BaseResp<Object> baseResp = new BaseResp<Object>();

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("point", Constant_point.INVITE_LEVEL1);//龙分
        map.put("impIcon", Constant_Imp_Icon.INVITE_LEVEL1);//进步币
        map.put("downloadUrl","http://www.baidu.com");

        baseResp.setData(map);
        return baseResp.initCodeAndDesp(Constant.STATUS_SYS_00,Constant.RTNINFO_SYS_00);
    }

    @Override
    public boolean insertSysCommon(String key, String info, String remark) {
        SysCommon sysCommon = sysCommonMapper.selectByKey(key);
        SysCommon sysCommon1 = new SysCommon();
        sysCommon1.setKey(Constant.SYS_COMMON_KEYS.regprotocol.toString());
        sysCommon1.setInfo(info);
        sysCommon1.setRemark(remark);
        int res = 0;
        if (null == sysCommon){
            res = sysCommonMapper.insertSelective(sysCommon1);
        } else {
            res = sysCommonMapper.updateByKey(sysCommon1);
        }
        if(res == 0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public SysCommon getSysCommonByKey(String key) {
        SysCommon sysCommon = sysCommonMapper.selectByKey(key);
        return sysCommon;
    }

    @Override
    public List<SysCommon> getSysCommons() {
        return sysCommonMapper.getSysCommons();
    }

    @Override
    public void initSysAppUpdateMap(){
        //1 安卓 2 ios
        SysAppupdate sysAppupdate = selectRecentByKey("0");
        if (null != sysAppupdate){
            SysRulesCache.sysAppupdateMap.put("0",sysAppupdate);
        }
        SysAppupdate sysAppupdate1 = selectRecentByKey("1");
        if (null != sysAppupdate1){
            SysRulesCache.sysAppupdateMap.put("1",sysAppupdate1);
        }
    }


}
