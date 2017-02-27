package com.longbei.appservice.dao;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.UserIdcard;

public interface UserIdcardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserIdcard record);

    int insertSelective(UserIdcard record);

    UserIdcard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserIdcard record);

    int updateByPrimaryKey(UserIdcard record);
    
    /**
	 * @author yinxc
	 * 查看身份证认证信息
	 * 2017年1月20日
	 */
    UserIdcard selectByUserid(@Param("userid") long userid);
    
}