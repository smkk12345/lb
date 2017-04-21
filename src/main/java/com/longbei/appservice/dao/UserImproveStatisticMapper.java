package com.longbei.appservice.dao;

import com.longbei.appservice.entity.UserImproveStatistic;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyongzhi 17/4/17.
 */
public interface UserImproveStatisticMapper {

    /**
     * 批量插入用户的进步统计
     * @param userImproveList
     * @return
     */
    int batchInsertUserImproveStatistic(@Param("userImproveList") List<UserImproveStatistic> userImproveList);

    /**
     * 更新用户的请求数据
     * @param map
     * @return
     */
    int updateUserImproveStatic(Map<String, Object> map);
    
    UserImproveStatistic selectByUseridAndCurrentday(@Param("userid") long userid, 
    		@Param("currentday") String currentday);
}
