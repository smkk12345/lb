package com.longbei.appservice.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.longbei.appservice.entity.ImpComplaints;

/** 
* @ClassName: ImpComplaintsMapper 
* @Description: 投诉举报
* @author yinxc
* @date 2017年3月30日 上午10:32:06 
*  
*/
public interface ImpComplaintsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ImpComplaints record);

    int insertSelective(ImpComplaints record);

    ImpComplaints selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ImpComplaints record);

    int updateByPrimaryKey(ImpComplaints record);
    
    /**
     * @Title: selectByStatus 
     * @Description: 根据status获取投诉信息
     * @param @param status 处理状态   0：未处理   1：已处理
     * @param @param pageNo
     * @param @param pageSize
     * @param @return    设定文件 
     * @return List<ImpComplaints>    返回类型
     */
    List<ImpComplaints> selectListByStatus(@Param("status") String status, 
    		@Param("pageNo") int pageNo, @Param("pageSize") int pageSize);
    
    /**
    * @Title: selectCountByStatus 
    * @Description: 获取投诉类型Count
    * @param @param status
    * @param @return    设定文件 
    * @return int    返回类型
     */
    int selectCountByStatus(@Param("status") String status);
    
    /**
    * @Title: searchList 
    * @Description: 搜索 
    * @param @param status
    * @param @param username
    * @param @param businesstype
    * @param @param sdealtime
    * @param @param edealtime
    * @param @param pageNo
    * @param @param pageSize
    * @param @return    设定文件 
    * @return List<ImpComplaints>    返回类型
     */
    List<ImpComplaints> searchList(@Param("status") String status, 
    		@Param("username") String username, @Param("businesstype") String businesstype, 
    		@Param("sdealtime") String sdealtime, @Param("edealtime") String edealtime, 
    		@Param("pageNo") int pageNo, @Param("pageSize") int pageSize);
    
    /**
    * @Title: searchCount 
    * @Description: 获取搜索Count 
    * @param @param status
    * @param @param username
    * @param @param businesstype
    * @param @param sdealtime
    * @param @param edealtime
    * @param @return    设定文件 
    * @return int    返回类型
     */
    int searchCount(@Param("status") String status, 
    		@Param("username") String username, @Param("businesstype") String businesstype, 
    		@Param("sdealtime") String sdealtime, @Param("edealtime") String edealtime);
    
    /**
    * @Title: selectCountByImpid 
    * @Description: 获取businesstype类型进步被投诉次数
    * @param @param status
    * @param @param impid 进步id
    * @param @param businessid 类型业务id
    * @param @param businesstype 类型    0 零散进步   1 目标进步    2 榜中  3圈子中进步 4 教室
    * @param @return    设定文件 
    * @return int    返回类型
     */
    int selectCountByImpid(@Param("status") String status, @Param("impid") long impid, 
    		@Param("businessid") long businessid, @Param("businesstype") String businesstype);
    
    /**
    * @Title: updateImpComplaintsByStatus 
    * @Description: 投诉处理
    * @param @param status
    * @param @param dealtime
    * @param @param checkoption
    * @param @param impid
    * @param @param businessid
    * @param @param businesstype
    * @param @return    设定文件 
    * @return int    返回类型
     */
    int updateImpComplaintsByStatus(@Param("status") String status, @Param("dealtime") Date dealtime, 
    		@Param("dealuser") String dealuser, 
    		@Param("checkoption") String checkoption, @Param("impid") long impid, 
    		@Param("businessid") long businessid, @Param("businesstype") String businesstype);
    
}