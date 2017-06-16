package com.longbei.appservice.dao;

import com.longbei.appservice.entity.CircleMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CircleMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleMembers record);

    int insertSelective(CircleMembers record);

    CircleMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleMembers record);

    int updateByPrimaryKey(CircleMembers record);

    List<CircleMembers> selectCircleMember(Map<String, Object> map);

    CircleMembers findCircleMember(Map<String, Object> map);

    int updateCircleMembers(Map<String, Object> map);

    /**
     * 更新用户在圈主中的信息 加赞 加花 加钻石
     * @param map
     * @return
     */
    int updateCircleMemberInfo(Map<String,Object> map);

    /**
     * 查询兴趣圈中的所有用户id
     * @param circleId
     * @return
     */
    List<Long> findCircleMembersId(Long circleId);

    /**
     * 更新用户在圈子中的进步数量
     * @param userid
     * @param circleId
     * @param num
     * @return
     */
    int updateCircleMembersIcount(@Param("userid")Long userid,@Param("circleId") Long circleId,@Param("num")int num);
}