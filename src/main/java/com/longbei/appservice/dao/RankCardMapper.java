package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankCard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankCardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankCard record);

    int insertSelective(RankCard record);

    RankCard selectByPrimaryKey(Integer id);

    List<RankCard> selectList(@Param("rankcard") RankCard rankCard,
                              @Param("startno") Integer startno,@Param("pagesize") Integer pagesize);

    int selectCount(RankCard rankCard);

    int updateByPrimaryKeySelective(RankCard record);

    int updateByPrimaryKey(RankCard record);
}