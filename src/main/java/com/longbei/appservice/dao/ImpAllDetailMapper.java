package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpAllDetail;
import com.longbei.appservice.entity.Improve;

import java.util.List;

public interface ImpAllDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByImpAllDetail(ImpAllDetail impAllDetail);

    int insert(ImpAllDetail record);

    int insertSelective(ImpAllDetail record);

    ImpAllDetail selectByPrimaryKey(Integer id);

    List<ImpAllDetail> selectByImpAllDetail(ImpAllDetail impAllDetail);

    int updateByPrimaryKeySelective(ImpAllDetail record);

    int updateByPrimaryKey(ImpAllDetail record);
}