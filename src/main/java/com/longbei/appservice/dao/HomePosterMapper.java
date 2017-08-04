package com.longbei.appservice.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.HomePoster;

public interface HomePosterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomePoster record);

    int insertSelective(HomePoster record);

    HomePoster selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HomePoster record);

    int updateByPrimaryKey(HomePoster record);
    
    int selectCount(HomePoster homePoster);

    List<HomePoster> selectList(@Param("homePoster") HomePoster homePoster,
                                 @Param("startno")Integer startno,
                                 @Param("pagesize")Integer pagesize);
    
}