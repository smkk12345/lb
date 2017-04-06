package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserPointDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPointDetailMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(UserPointDetail record);

    int insertSelective(UserPointDetail record);

    UserPointDetail selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(UserPointDetail record);

    int updateByPrimaryKey(UserPointDetail record);

    /**
     * @Title: selectPointListByUseridAndPointtype
     * @Description: 获取用户发进步积分列表
     * @param @param userid 用户id
     * @param @param pointtype 积分类型
     * @param @param startNum分页起始值
     * @param @param pageSize每页显示条数
     * @auther IngaWu
     * @currentdate:2017年4月5日
     */
    List<UserPointDetail> selectPointListByUseridAndPointtype(@Param("userid")long userid, @Param("pointtype")String pointtype,@Param("startNum")int startNum,@Param("pageSize")int pageSize);
}