package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankAcceptAward;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RankAcceptAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankAcceptAward record);

    int insertSelective(RankAcceptAward record);

    RankAcceptAward selectByPrimaryKey(Integer id);

    int updateByRankidAndUseridSelective(RankAcceptAward record);

    int updateByPrimaryKey(RankAcceptAward record);

    int selectCount(RankAcceptAward rankAcceptAward);

    List<RankAcceptAward> selectList(@Param("rankAcceptAward")RankAcceptAward rankAcceptAward,@Param("startno")Integer startno,
                                     @Param("pagesize")Integer pagesize);

    RankAcceptAward selectByRankIdAndUserid(@Param("rankid") String rankid,@Param("userid") String userid);

    /**
     * 查询需要自动确认收货的订单
     * @return
     */
    List<RankAcceptAward> selectAutoConfirmReceiptRankAward(Map<String,Object> map);

    /**
     * 修改rankAcceptAward的状态 自动确认收货
     * @param map
     * @return
     */
    int updateRankAwardStatus(Map<String, Object> map);

    /**
     * 查询用户的榜单获奖列表
     * @param userid
     * @param startNum
     * @param pageSize
     * @return
     */
    List<RankAcceptAward> userRankAcceptAwardList(@Param("userid")Long userid,@Param("ispublic") Integer ispublic,@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

    /**
     * 获取用户的榜单获奖数量
     * @param userid
     * @return
     */
    int userRankAcceptAwardCount(Long userid);
}