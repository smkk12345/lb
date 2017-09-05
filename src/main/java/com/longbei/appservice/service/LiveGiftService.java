package com.longbei.appservice.service;

import com.longbei.appservice.common.BaseResp;
import com.longbei.appservice.common.Page;
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


    /**
     * 查询用户收到的礼物列表
     * @param userid 当前登录者id
     * @param startNum
     * @param endNUm
     * @return
     */
    BaseResp<List<LiveGiftDetail>> selectOwnGiftList(Long userid,Integer startNum,Integer endNum);
    
    /**
     * 查询用户收到的各礼物类型总数
     * @param userid
     */
    BaseResp<List<LiveGiftDetail>> selectGiftSumList(long userid);

    BaseResp<List<LiveGiftDetail>> selectGiftList(long userid,Integer startNum,Integer endNum);

    /**
     * 获取直播礼物列表
     * @title selectLiveGiftList
     * @author IngaWu
     * @currentdate:2017年8月18日
     */
    Page<LiveGift> selectLiveGiftList(LiveGift liveGift, Integer startNum, Integer pageSize);

    /**
     * @Title: selectLiveGiftByGiftId
     * @Description: 通过礼物id查看直播礼物详情
     * @param giftId 礼物id
     * @auther IngaWu
     * @currentdate:2017年8月18日
     */
    BaseResp<LiveGift> selectLiveGiftByGiftId(Long giftId);

    /**
     * @Title: removeLiveGiftByGiftId
     * @Description: 删除直播礼物
     * @param giftId 礼物id
     * @auther IngaWu
     * @currentdate:2017年8月18日
     */
    BaseResp<Object> removeLiveGiftByGiftId(Long giftId);

    /**
     * 添加直播礼物
     * @title insertLiveGift
     * @author IngaWu
     * @currentdate:2017年8月18日
     */
    BaseResp<Object> insertLiveGift(LiveGift liveGift);

    /**
     * 编辑直播礼物
     * @title updateLiveGiftByGiftId
     * @author IngaWu
     * @currentdate:2017年8月18日
     */
    BaseResp<Object> updateLiveGiftByGiftId(LiveGift liveGift);
}
