package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Improve;
import org.apache.ibatis.annotations.Param;

public interface ImproveMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Improve record);

    int insertSelective(Improve record);

    Improve selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Improve record);

    int updateByPrimaryKey(Improve record);

    /**
     * 假删
     * @param userid 用户id
     * @param improveid  进步id
     * @return
     */
    int remove(@Param("userid") String userid,@Param("improveid") String improveid);

}