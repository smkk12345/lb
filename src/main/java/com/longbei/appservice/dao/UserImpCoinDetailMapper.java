package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserImpCoinDetail;

public interface UserImpCoinDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserImpCoinDetail record);

    int insertSelective(UserImpCoinDetail record);

    UserImpCoinDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserImpCoinDetail record);

    int updateByPrimaryKey(UserImpCoinDetail record);
    
    /**
	 * @author yinxc
	 * 获取进步币明细列表
	 * 2017年2月23日
	 * return_type
	 * UserImpCoinDetailMapper
	 */
    List<UserImpCoinDetail> selectListByUserid(@Param("userid") long userid, @Param("pageNo") int pageNo, 
    		@Param("pageSize") int pageSize);

}