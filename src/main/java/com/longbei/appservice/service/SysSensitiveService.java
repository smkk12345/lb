package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysSensitive;

import java.util.Set;

/**
 * Created by lixb on 2017/3/22.
 */
public interface SysSensitiveService {

    Set<String> selectSensitiveWord();

    BaseResp<Object> getSensitiveWordSet(String str);

    /**
     * 查询敏感词
     * @title selectSensitive
     * @author IngaWu
     * @currentdate:2017年6月15日
     */
    BaseResp<SysSensitive> selectSensitive();

    /**
     * 编辑敏感词
     * @title updateSensitiveWords
     * @author IngaWu
     * @currentdate:2017年6月15日
     */
    BaseResp<Object> updateSensitiveWords(String words);

}
