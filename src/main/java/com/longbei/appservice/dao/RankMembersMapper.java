package com.longbei.appservice.dao;

import com.longbei.appservice.entity.RankMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 更改rankMe
     * @param updateMap
     * @return
     */
    int updateRank(Map<String, Object> updateMap);

    /**
     * @param rankid
     * @param userid
     * @param otype  0删除进步  1 增加进步
     * @return
     */
    int updateRankImproveCount(@Param("rankid")long rankid,@Param("userid")long userid,@Param("otype") String otype);

    /**
     * 查询用户排名的集合
     * @param rankId 榜单id
     * @param sortType 排序字段
     * @param startNum
     * @param pageSize
     * @return
     */
    List<RankMembers> selectUserSort(@Param("rankId")Long rankId,@Param("sortType") String sortType,@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);
}