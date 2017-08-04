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
    
    
    /**
     * 启动页上下线修改
     * @author yinxc
     */
    Integer updateIsup(@Param("isup") String isup, @Param("id") String id, 
    		@Param("uptime") String uptime, @Param("downtime") String downtime);
    
    /**
     * 启动页上线修改为下线状态
     * @author yinxc
     */
    Integer updateIsdown(@Param("downtime") String downtime);
    
}