package com.longbei.appservice.dao;

import com.longbei.appservice.entity.HomePicture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomePictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomePicture record);

    int insertSelective(HomePicture record);

    HomePicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HomePicture record);

    int updateByPrimaryKey(HomePicture record);

    int selectCount(HomePicture homePicture);

    List<HomePicture> selectList(@Param("homePicture") HomePicture homePicture,
                                 @Param("startno")Integer startno,
                                 @Param("pagesize")Integer pagesize);
}