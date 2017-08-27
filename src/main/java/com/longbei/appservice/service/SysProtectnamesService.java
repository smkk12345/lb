package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysProtectnames;

import java.util.Set;

public interface SysProtectnamesService {

    Set<String> selectProtectNamesSet();

    BaseResp<Object> containsProtectNames(String str);
    /**
     * 查询受保护的昵称
     * @author IngaWu
     * @currentdate:2017年8月25日
     */
    BaseResp<SysProtectnames> selectProtectnames();

    /**
     * 编辑受保护的昵称
     * @title updateProtectNames
     * @param  nicknames 昵称集合
     * @author IngaWu
     * @currentdate:2017年8月25日
     */
    BaseResp<Object> updateProtectNames(String nicknames);

}
