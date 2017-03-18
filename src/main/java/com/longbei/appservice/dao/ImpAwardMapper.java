package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpAward;
import org.apache.ibatis.annotations.Param;

/**
 * 进步获奖表
 */
public interface ImpAwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImpAward record);

    int insertSelective(ImpAward record);

    ImpAward selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImpAward record);

    int updateByPrimaryKey(ImpAward record);

    //通过榜单id和用户id获取用户获奖信息
    ImpAward  selectByRankIdAndUserId(@Param("rankid") Long rankId,@Param("userid") Long userId);

}