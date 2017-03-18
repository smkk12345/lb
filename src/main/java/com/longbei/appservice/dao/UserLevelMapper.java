package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserLevel;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserLevelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLevel record);

    int insertSelective(UserLevel record);

    UserLevel selectByPrimaryKey(Long id);
    
    /**
	 * @author yinxc
	 * 根据等级grade获取信息
	 * 2017年3月9日
	 * grade 等级
	 */
    UserLevel selectByGrade(@Param("grade") Integer grade);

    List<UserLevel> selectAll();

    int updateByPrimaryKeySelective(UserLevel record);

    int updateByPrimaryKey(UserLevel record);
}