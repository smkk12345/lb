package com.longbei.appservice.dao;

import com.longbei.appservice.entity.SysPerfectDefine;
import org.apache.ibatis.annotations.Param;

public interface SysPerfectDefineMapper {

    /**
     * 获取随机的名人名言解释
     * @param ptype
     * @return
     */
    SysPerfectDefine selectRandomByType(@Param("ptype")String ptype);

}