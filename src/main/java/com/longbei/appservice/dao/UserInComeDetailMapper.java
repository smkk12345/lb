package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserInComeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInComeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInComeDetail record);

    int insertSelective(UserInComeDetail record);

    UserInComeDetail selectByPrimaryKey(Integer id);

    /**
     * 查询收入明细
     * @param detailid
     * @return
     */
    UserInComeDetail selectUserInComeInDetail(@Param("detailid") String detailid);


    /**
     * 查询支出明细
     * @param detailid
     * @return
     */
    UserInComeDetail selectUserInComeOutDetail(@Param("detailid") String detailid);

    int selectCount(@Param("userincomedetail") UserInComeDetail userInComeDetail);

    List<UserInComeDetail> selectList(@Param("userincomedetail") UserInComeDetail userInComeDetail,
                                      @Param("startNo") Integer startNo,
                                      @Param("pageSize") Integer pageSize);

    int updateByPrimaryKeySelective(UserInComeDetail record);

    int updateByPrimaryKey(UserInComeDetail record);
}