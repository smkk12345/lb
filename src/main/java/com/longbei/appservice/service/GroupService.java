package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;

/**
 * Created by wangyongzhi 17/3/8.
 */
public interface GroupService {
    /**
     * 新建群组
     * @param userIds
     * @return
     */
    BaseResp<Object> createGroup(String[] userIds,String mainGroupUserId,Integer type,Long typeId,String groupNam,Boolean needConfirm);
}
