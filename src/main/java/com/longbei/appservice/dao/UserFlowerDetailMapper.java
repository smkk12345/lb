package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserFlowerDetail;

public interface UserFlowerDetailMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(UserFlowerDetail record);

    int insertSelective(UserFlowerDetail record);

    UserFlowerDetail selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(UserFlowerDetail record);

    int updateByPrimaryKey(UserFlowerDetail record);
    
    /**
	 * @author yinxc
	 * 获取进步币明细列表
	 * 2017年2月23日
	 * return_type
	 * UserImpCoinDetailMapper
	 */
    List<UserFlowerDetail> selectListByUserid(@Param("userid") long userid, @Param("pageNo") int pageNo, 
    		@Param("pageSize") int pageSize);
    
}