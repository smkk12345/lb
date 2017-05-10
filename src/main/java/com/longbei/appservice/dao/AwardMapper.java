package com.longbei.appservice.dao;

import com.longbei.appservice.entity.Award;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AwardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Award record);

    int insertSelective(Award record);

    Award selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Award record);

    int updateByPrimaryKey(Award record);

    List<Award> selectAwardList(@Param("award") Award award,
                                @Param("startno") Integer startno, @Param("pagesize") Integer pageszie);

    int selectAwardCount(@Param("award") Award award);

    int awardCountsUnderClassify(@Param("classifyid") Integer classifyid);

    /**
     * 查询奖品 和 奖品分类
     * @param awardId
     * @return
     */
    Award selectAwardAndAwardClassify(@Param("awardId") Integer awardId);
}