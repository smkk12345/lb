package com.longbei.appservice.dao;

import com.longbei.appservice.entity.LiveGift;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lixb on 2017/8/11.
 */
public interface LiveGiftMapper {

    List<LiveGift> selectList(@Param("startNum") Integer startNum,
                              @Param("endNum") Integer endNum);

    LiveGift selectById(@Param("giftId") long giftId);
}
