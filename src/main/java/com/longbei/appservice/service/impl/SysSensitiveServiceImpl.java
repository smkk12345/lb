package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.security.SensitiveWord;
import com.longbei.appservice.common.utils.DateUtils;
import com.longbei.appservice.dao.SysSensitiveMapper;
import com.longbei.appservice.entity.SysSensitive;
import com.longbei.appservice.service.SysSensitiveService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lixb on 2017/3/22.
 */
@Service
public class SysSensitiveServiceImpl implements SysSensitiveService {
    private Logger logger = LoggerFactory.getLogger(SysSensitiveServiceImpl.class);


    @Autowired
    private SysSensitiveMapper sysSensitiveMapper;

    @Override
    public Set<String> selectSensitiveWord() {
        SysSensitive sysSensitive = sysSensitiveMapper.selectSensitive();
        if(null == sysSensitive){
            return null;
        }
        Set<String> set = new HashSet<>();
        String words = "";
        words = sysSensitive.getWords();
        words = words.replaceAll( "\\s", "" );
        words = words.replaceAll("　"," ");
        words = words.replaceAll("，",",");
        String[] sArr = words.split(",");
        CollectionUtils.addAll(set,sArr);
        return set;
    }

    /**
     * 敏感词
     * @param str
     * @return
     */
    @Override
    public BaseResp<Object> getSensitiveWordSet(String str) {
        BaseResp<Object> baseResp = new BaseResp<>();
        Set<String> set = SensitiveWord.getSensitiveWord(str,2);
        if(set.isEmpty()){
            return baseResp.initCodeAndDesp();
        }else{
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_17,Constant.RTNINFO_SYS_17);
            baseResp.setData(set);
            return baseResp;
        }
    }

    @Override
    public BaseResp<SysSensitive> selectSensitive() {
        BaseResp<SysSensitive> baseResp = new BaseResp<SysSensitive>();
        try {
            SysSensitive sysSensitive = sysSensitiveMapper.selectSensitive();
            baseResp.setData(sysSensitive);
            baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
        }
        catch (Exception e) {
            logger.error("selectSensitive error and msg={}",e);
        }
        return baseResp;
    }

    @Override
    public 	BaseResp<Object> updateSensitiveWords(String words,String sensitiveId) {
        BaseResp<Object> baseResp = new BaseResp<Object>();
        SysSensitive sensitive = new SysSensitive();
        sensitive.setId(Integer.parseInt(sensitiveId));
        sensitive.setWords(words);
        sensitive.setUpdatetime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
        try {
            int n = sysSensitiveMapper.updateSensitiveWords(sensitive);
            if(n >= 1){
                baseResp.initCodeAndDesp(Constant.STATUS_SYS_00, Constant.RTNINFO_SYS_00);
            }
        } catch (Exception e) {
            logger.error("updateSensitiveWords error and msg={}",e);
        }
        return baseResp;
    }

}
