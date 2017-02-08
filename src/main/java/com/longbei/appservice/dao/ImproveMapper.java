package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Improve;
import com.longbei.appservice.entity.ImproveGoal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImproveMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Improve record);

    int insertSelective(@Param("improve") Improve improve,@Param("tablename") String tablename);

    Improve selectByPrimaryKey(@Param("impid")Long impid,@Param("tablename")String tablename);

    /**
     * 获取进步列表
     *
     * @return
     */
    List<Improve> selectListByBusinessid(@Param("businessid")String businessid,
                                         @Param("tablename")String tablename,
                                         @Param("ismainimp")String ismainimp,
                                         @Param("startno")int startno,
                                         @Param("pagesize")int pagesize);

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