package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsGroupMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SnsGroupMembersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsGroupMembers record);

    int insertSelective(SnsGroupMembers record);

    SnsGroupMembers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsGroupMembers record);

    int updateByPrimaryKey(SnsGroupMembers record);

    /**
     * 批量插入群组成员
     * @param map
     * @return
     */
    int batchInsertGroupMembers(Map<String, Object> map);

    /**
     * 根据群组id和用户id 查询群成员
     * @param userId
     * @param groupId
     * @return
     */
    SnsGroupMembers findByUserIdAndGroupId(@Param("userId") Long userId,@Param("groupId") String groupId);

    /**
     * 修改群组成员信息
     * @param userId
     * @param groupId
     * @param nickName 昵称
     * @param status 状态
     * @return
     */
    int updateSnsGroupMemberInfo(@Param("userId")Long userId,@Param("groupId") String groupId,
                                 @Param("nickName")String nickName,@Param("status") Integer status);

    /**
     * 根据用户id和群组id查询用户
     * @param parameterMap
     * @return
     */
    List<SnsGroupMembers> selectSnsGroupMembersList(Map<String, Object> parameterMap);

    /**
     * 批量更新用户在群里的状态
     * @param updateMap
     * @return
     */
    int batchUpdateSnsGroupMemberStatus(Map<String, Object> updateMap);

    /**
     * 批量删除群成员,直接从数据库删除
     * @param deleteMap
     * @return
     */
    int batchDeleteGroupMember(Map<String, Object> deleteMap);

    /**
     * 查询群组成员信息列表
     * @param groupId
     * @param status
     * @return
     */
    List<SnsGroupMembers> groupMemberList(@Param("groupId") String groupId,@Param("status") Integer status);
}