package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCollectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCollect record);

    int insertSelective(UserCollect record);

    UserCollect selectByPrimaryKey(Integer id);

    /**
     * 获取收藏列表
     * @param userCollect
     * @return
     * @author luye
     */
    List<UserCollect> selectListByUserCollect(UserCollect userCollect);

    List<String> selectCollectIdsByUser(@Param("userid") String userid);

    int updateByPrimaryKeySelective(UserCollect record);

    int updateByPrimaryKey(UserCollect record);

    int removeCollect(@Param("userid") Long userid,@Param("impid")Long impid, @Param("ctype") String ctype);

    List<UserCollect> selectCollect(@Param("userid")Long uerid,@Param("ctype")String ctype,@Param("startNum")int startNum,@Param("pageSize")int pageSize);

    /**
     * 修改收藏该进步的状态为已删除
     * @param ctype
     * @param improveid
     * @return
     */
    int deleteUserCollectImprove(@Param("ctype")String ctype,@Param("improveid") String improveid);
}