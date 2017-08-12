package com.longbei.appservice.dao;

import com.longbei.appservice.entity.LiveGift;

import java.util.List;

/**
 * Created by lixb on 2017/8/11.
 */
public interface LiveGiftMapper {

    List<LiveGift> selectList(Integer startNum, Integer endNum);

    LiveGift selectById(long giftId);
}
