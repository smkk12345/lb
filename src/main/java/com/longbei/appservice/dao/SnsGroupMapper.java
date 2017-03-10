package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SnsGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface SnsGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SnsGroup record);

    int insertSelective(SnsGroup record);

    SnsGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SnsGroup record);

    int updateByPrimaryKey(SnsGroup record);

    /**
     * 根据groupId和userId 查询群组 但userId可不传
     * @param groupId 群组id 必传
     * @param userId 用户id 非必传
     * @return
     */
    SnsGroup selectByGroupIdAndMainUserId(@Param("groupId") String groupId, @Param("userId") Long userId);

    /**
     * 更新群组名称
     * @param groupId 群组id
     * @param groupName 新的群组名称
     * @param updateDate 时间
     * @return
     */
    int updateGroupInfo(@Param("groupId") String groupId,@Param("groupName") String groupName,
                        @Param("needConfirm") Boolean needConfirm,@Param("notice") String notice,
                        @Param("updateDate") Date updateDate);

    /**
     * 更新群组新增人数
     * @param groupId
     * @param addNum
     * @return
     */
    int updateGroupCurrentNum(@Param("groupId") String groupId,@Param("addNum") String addNum);

    /**
     * 删除群组
     * @param groupId
     * @return
     */
    int deleteByGroupId(String groupId);

    /**
     * 更新群组 群主id
     * @param userId
     * @param groupId
     * @return
     */
    int updateGroupMainUser(@Param("userId") String userId,@Param("groupId") String groupId);
}