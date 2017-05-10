package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysPerfectTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPerfectTagMapper {



    /**
     * @Title: selectUserTagList
     * @Description: 获取用户兴趣标签
     */
    List<SysPerfectTag> selectUserTagList(@Param("ptypes")String[] ptypes);

    List<SysPerfectTag> selectAll();

}