package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserCheckinDetail;

public interface UserCheckinDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCheckinDetail record);

    int insertSelective(UserCheckinDetail record);

    UserCheckinDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCheckinDetail record);

    int updateByPrimaryKey(UserCheckinDetail record);
    
    /**
	 * @author yinxc
	 * 获取签到详情记录
	 * 2017年2月22日
	 * return_type
	 * UserCheckinDetailMapper
	 */
    List<UserCheckinDetail> selectDetailList(@Param("userid") long userid);
    
    /**
	 * @author yinxc
	 * 根据年月获取签到详情记录
	 * 2017年2月22日
	 * return_type
	 * UserCheckinDetailMapper
	 */
    List<UserCheckinDetail> selectDetailListByYearmonth(@Param("userid") long userid, @Param("yearmonth") Integer yearmonth);
    
}