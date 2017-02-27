package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserMoneyDetail;

public interface UserMoneyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMoneyDetail record);

    int insertSelective(UserMoneyDetail record);

    UserMoneyDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMoneyDetail record);

    int updateByPrimaryKey(UserMoneyDetail record);
    
    /**
	 * @author yinxc
	 * 获取龙币明细列表
	 * 2017年2月27日
	 * return_type
	 */
    List<UserMoneyDetail> selectListByUserid(@Param("userid") long userid, @Param("pageNo") int pageNo, 
    		@Param("pageSize") int pageSize);
    
}