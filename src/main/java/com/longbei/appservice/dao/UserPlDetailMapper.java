package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserPlDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserPlDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPlDetail record);

    int insertSelective(UserPlDetail record);

    UserPlDetail selectUserPlDetailById(Integer id);

    UserPlDetail selectByUserIdAndType(@Param("userid") long userid, @Param("ptype") String ptype);

    List<UserPlDetail> selectUserPerfectListByUserId(@Param("userid") long userid,@Param("startNum") int startNum,@Param("pageSize")int pageSize);

    int updateByPrimaryKeySelective(UserPlDetail record);

    int updateByPrimaryKey(UserPlDetail record);

    int updateScorce(UserPlDetail record);



}