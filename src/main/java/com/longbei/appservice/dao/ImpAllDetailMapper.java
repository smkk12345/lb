package com.longbei.appservice.dao;

import com.longbei.appservice.entity.ImpAllDetail;
import com.longbei.appservice.entity.Improve;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ImpAllDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByImpAllDetail(ImpAllDetail impAllDetail);

    int insert(ImpAllDetail record);

    int insertSelective(ImpAllDetail record);

    ImpAllDetail selectByPrimaryKey(Integer id);

    List<ImpAllDetail> selectList(@Param("impid") String impid, @Param("detailtype")String detailtype,
                                  @Param("pagesize")int pagesize,@Param("lastdate") Date lastdate);

    /**
     * 用户判断记录是否存在
     * @param impAllDetail
     * @return
     * @author luye
     */
    List<ImpAllDetail> selectOneDetail(ImpAllDetail impAllDetail);

    int updateByPrimaryKeySelective(ImpAllDetail record);

    int updateByPrimaryKey(ImpAllDetail record);

    /**
     * 查询用户从startDate 到endDate 的点赞和送花列表
     * @param startDate
     * @param endDate
     * @return
     */
    List<ImpAllDetail> userStatistic(@Param("startDate")Date startDate,@Param("endDate") Date endDate);

    /**
     * 查询用户对进步献花的总数
     */
    Integer selectSumByImp(@Param("userid") long userid, @Param("impid") String impid);
}