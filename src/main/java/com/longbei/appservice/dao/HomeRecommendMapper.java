package com.longbei.appservice.dao;

import com.longbei.appservice.entity.HomeRecommend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomeRecommendMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomeRecommend record);

    int insertSelective(HomeRecommend record);

    HomeRecommend selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HomeRecommend record);

    int updateByPrimaryKey(HomeRecommend record);

    /**
     * 利用businessids批量插入
     * @param homeRecommend
     * @return
     */
    int insertBantch(HomeRecommend homeRecommend);


    int selectCount(HomeRecommend homeRecommend);


    List<HomeRecommend> selectList(@Param("homeRecommend") HomeRecommend homeRecommend,
                                   @Param("startno") Integer startno,
                                   @Param("pagesize") Integer pagesize);
}