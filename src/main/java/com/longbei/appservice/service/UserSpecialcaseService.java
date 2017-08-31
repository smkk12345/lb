package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.SysSensitive;
import com.longbei.appservice.entity.UserSpecialcase;

import java.util.Set;

public interface UserSpecialcaseService {

    /**
     * 查询特例用户
     * @title selectUserSpecialcase
     * @author IngaWu
     * @currentdate:2017年8月21日
     */
    BaseResp<UserSpecialcase> selectUserSpecialcase();

    /**
     * 编辑特例用户
     * @title updateUserSpecialcase
     * @author IngaWu
     * @currentdate:2017年8月21日
     */
    BaseResp<Object> updateUserSpecialcase(UserSpecialcase userSpecialcase);

    void updateUserSpecialcase();

}
