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

    /**
     * @Description: 查看用户等级列表
     * @param startNum 分页起始值
     * @param pageSize 每页显示条数
     */
    List<UserLevel> selectAll(@Param("startNum") Integer startNum,@Param("pageSize") Integer pageSize);

    /**
     * @Description: 查看用户等级列表数量
     * @auther IngaWu
     * @currentdate:2017年6月1日
     */
    Integer selectUserLevelListCount();

    int updateByPrimaryKeySelective(UserLevel record);

    int updateByPrimaryKey(UserLevel record);
}