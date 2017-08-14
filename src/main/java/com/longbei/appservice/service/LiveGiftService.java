package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.entity.LiveGift;
import com.longbei.appservice.entity.LiveGiftDetail;

import java.util.List;

/**
 * Created by lixb on 2017/8/11.
 */
public interface LiveGiftService {

    /**
     * 获取礼物列表
     * @param startNum
     * @param endNum
     * @return
     */
    BaseResp<List<LiveGift>> selectList(Integer startNum, Integer endNum);

    /**
     * 送礼物
     * @param giftId
     * @param fromUid
     * @param num
     * @param toUId
     * @return
     */
    BaseResp<Object> giveGift(long giftId, long fromUid, int num, long toUId,long businessid,String businesstype);


    BaseResp<List<LiveGiftDetail>> selectOwnGiftList(long userid,Integer startNum,Integer endNum);

    BaseResp<List<LiveGiftDetail>> selectGiftList(long userid,Integer startNum,Integer endNum);


}
