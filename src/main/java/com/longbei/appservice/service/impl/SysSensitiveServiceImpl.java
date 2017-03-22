package com.longbei.appservice.service.impl;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.constant.Constant;
import com.longbei.appservice.common.security.SensitiveWord;
import com.longbei.appservice.dao.SysSensitiveMapper;
import com.longbei.appservice.entity.SysSensitive;
import com.longbei.appservice.service.SysSensitiveService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lixb on 2017/3/22.
 */
@Service
public class SysSensitiveServiceImpl implements SysSensitiveService {

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

}
