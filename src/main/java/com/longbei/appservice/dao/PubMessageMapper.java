package com.longbei.appservice.dao;

import com.longbei.appservice.entity.PubMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PubMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PubMessage record);

    int insertSelective(PubMessage record);

    PubMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PubMessage record);

    int updateByPrimaryKey(PubMessage record);

    int selectCount(@Param("msg") PubMessage pubMessage);

    List<PubMessage> selectList(@Param("msg") PubMessage pubMessage,
                                @Param("startNo") Integer startNo,
                                @Param("pageSize") Integer pageSize);

    List<PubMessage> selectListByArea(@Param("starttime")String starttime,
                                      @Param("endtime")String endtime);

}