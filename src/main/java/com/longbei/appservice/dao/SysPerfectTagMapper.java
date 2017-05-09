package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysPerfectTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPerfectTagMapper {

    List<SysPerfectTag> selectRandomTagList();

    List<SysPerfectTag> selectUserTagList(@Param("ptypes")String ptypes []);

}