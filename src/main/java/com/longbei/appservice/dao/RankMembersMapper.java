package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RankMembers record);

    int insertSelective(RankMembers record);

    RankMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RankMembers record);

    int selectCount(RankMembers rankMembers);

    List<RankMembers> selectList(@Param("rankmember") RankMembers rankMembers,
                                 @Param("startno") Integer startno,
                                 @Param("pagesize") Integer pagesize);

    int updateByPrimaryKey(RankMembers record);

    RankMembers selectByRankIdAndUserId(@Param("rankid") long rankid, @Param("userid") long userid);
}