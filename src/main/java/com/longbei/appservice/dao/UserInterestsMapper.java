package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserInterests;

public interface UserInterestsMapper {

    int updateInterests(UserInterests record);

    /**
     * @Title: insertInterests
     * @Description: 添加兴趣信息
     * @auther IngaWu
     * @currentdate:2017年2月24日
     */
    int insert(UserInterests record);

    /**
     * @Title: selectInterests
     * @Description: 获取用户兴趣标签
     * @auther IngaWu
     * @currentdate:2017年5月9日
     */
    UserInterests selectInterests(@Param("userid") long userid);
    
}