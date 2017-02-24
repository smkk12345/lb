package com.longbei.appservice.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserCheckinInfo;

public interface UserCheckinInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCheckinInfo record);

    int insertSelective(UserCheckinInfo record);

    UserCheckinInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCheckinInfo record);

    int updateByPrimaryKey(UserCheckinInfo record);
    
    /**
	 * @author yinxc
	 * 根据时间，userid获取持续签到信息
	 * 2017年2月22日
	 * return_type
	 * UserCheckinInfoMapper
	 */
    UserCheckinInfo selectByStarttimeAndUserid(@Param("userid") long userid, @Param("starttime") String starttime);
    
    /**
	 * @author yinxc
	 * 根据id修改持续签到continuedays，endtime
	 * 2017年2月22日
	 * return_type
	 * UserCheckinInfoMapper
	 */
    int updateContinuedaysByid(@Param("id") Integer id, @Param("continuedays") Integer continuedays, 
    		@Param("endtime") Date endtime);
    
}