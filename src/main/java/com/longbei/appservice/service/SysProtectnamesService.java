package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysProtectnames;

public interface SysProtectnamesService {

    /**
     * 查询受保护的昵称
     * @author IngaWu
     * @currentdate:2017年8月25日
     */
    BaseResp<SysProtectnames> selectProtectnames();

    /**
     * 编辑受保护的昵称
     * @title updateProtectNames
     * @param  nicknames 名称集合
     * @param  protectNamesId
     * @author IngaWu
     * @currentdate:2017年8月25日
     */
    BaseResp<Object> updateProtectNames(String nicknames, String protectNamesId);

}
