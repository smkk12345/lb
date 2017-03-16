package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImproveGoal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveGoalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImproveGoal record);

    int insertSelective(ImproveGoal record);

    ImproveGoal selectByPrimaryKey(Long impid);

    int updateByPrimaryKeySelective(ImproveGoal record);

    int updateByPrimaryKey(ImproveGoal record);

    /**
     * 根据目标id 获取进步列表
     * @param goalid 目标id
     * @return
     */
    List<ImproveGoal> selectByGoalId(String goalid);

    /**
     * 假删
     * @param userid 用户id
     * @param topicid 目标ID
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,
               @Param("topicid") String topicid,
               @Param("improveid") String improveid);

    /**
     * 选择主进步
     * @param goalid
     * @param userid
     * @return
     */
    int chooseGoalMainImprove(@Param("goalid")long goalid,
                              @Param("userid")long userid);


}