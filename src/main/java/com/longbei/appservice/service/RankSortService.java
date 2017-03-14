package com.longbei.appservice.service;

import com.longbei.appservice.common.constant.Constant;

/**
 * Created by wangyongzhi 17/3/14.
 */
public interface RankSortService extends BaseService{

    /**
     * 榜中点赞,送花时,更新用户在榜中的排名分值
     * @param rankId 榜id
     * @param userId 用户id
     * @param operationType 操作类型 点赞/送花/取消点赞
     * @param num 送花的数量,默认是1
     * @return
     */
    boolean updateRankSortScore(Long rankId, Long userId, Constant.OperationType operationType,Integer num);

}
