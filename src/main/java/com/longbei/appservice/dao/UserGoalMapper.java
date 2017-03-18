package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserGoal;
import org.apache.ibatis.annotations.Param;

public interface UserGoalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGoal record);

    int insertSelective(UserGoal record);

    UserGoal selectByGoalId(@Param("goalid") Long goalId);

    int updateByPrimaryKeySelective(UserGoal record);

    int updateByPrimaryKey(UserGoal record);

    int updateTitle(@Param("goalid")long goalId,@Param("title")String title);

    int delGoal(@Param("goalid")long goalId,@Param("userid")long userid);

    /**
     *
     * @param goalId
     * @param userid
     * @param otype  0删除进步  1 增加进步
     * @return
     */
    int updateIcount(@Param("goalid")long goalId,@Param("userid")long userid,@Param("otype") String otype);

}