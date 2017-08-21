package com.longbei.appservice.dao;

import com.longbei.appservice.entity.LiveGift;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lixb on 2017/8/11.
 */
public interface LiveGiftMapper {

    List<LiveGift> selectList(@Param("startNum") Integer startNum,
                              @Param("pageSize") Integer pageSize);

    List<LiveGift> selectLiveGiftList(@Param("liveGift") LiveGift liveGift,
                                      @Param("startNum") Integer startNum,
                                      @Param("pageSize") Integer pageSize);

    int selectLiveGiftListCount(@Param("liveGift") LiveGift liveGift);

    LiveGift selectLiveGiftByGiftId(@Param("giftId")Long giftId);

    int removeLiveGiftByGiftId(@Param("giftId") Long giftId);

    int insertLiveGift(LiveGift record);

    int updateLiveGiftByGiftId(LiveGift record);
}
