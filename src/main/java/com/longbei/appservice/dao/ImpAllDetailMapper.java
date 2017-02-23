package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpAllDetail;
import com.longbei.appservice.entity.Improve;

import java.util.Date;
import java.util.List;

public interface ImpAllDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByImpAllDetail(ImpAllDetail impAllDetail);

    int insert(ImpAllDetail record);

    int insertSelective(ImpAllDetail record);

    ImpAllDetail selectByPrimaryKey(Integer id);

    List<ImpAllDetail> selectList(String impid, String detailtype, int pagesize, Date lastdate);

    /**
     * 用户判断记录是否存在
     * @param impAllDetail
     * @return
     * @author luye
     */
    List<ImpAllDetail> selectOneDetail(ImpAllDetail impAllDetail);

    int updateByPrimaryKeySelective(ImpAllDetail record);

    int updateByPrimaryKey(ImpAllDetail record);
}