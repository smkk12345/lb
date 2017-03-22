package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

import java.util.Set;

/**
 * Created by lixb on 2017/3/22.
 */
public interface SysSensitiveService {

    Set<String> selectSensitiveWord();

    BaseResp<Object> getSensitiveWordSet(String str);

}
