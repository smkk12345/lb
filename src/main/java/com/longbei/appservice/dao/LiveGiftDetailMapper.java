package com.longbei.appservice.dao;

import com.longbei.appservice.entity.LiveGiftDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lixb on 2017/8/12.
 */
public interface LiveGiftDetailMapper {
    /**
     * 查询用户收到的礼物列表
     * @param userid
     * @param startNum
     * @param endNUm
     * @return
     */
    List<LiveGiftDetail> selectOwnGiftList(@Param("userid") Long userid,
                                           @Param("startNum") Integer startNum,
                                           @Param("endNum") Integer endNUm);

    /**
     * 查询用户送出的礼物列表
     * @param userid
     * @param startNum
     * @param endNUm
     * @return
     */
    List<LiveGiftDetail> selectGiftList(@Param("userid") Long userid,
                                           @Param("startNum") Integer startNum,
                                           @Param("endNum") Integer endNUm);

    /**
     * 插入礼物明细
     * @param liveGiftDetail
     * @return
     */
    int insertGiftDetail(@Param("liveGiftDetail") LiveGiftDetail liveGiftDetail);

}
