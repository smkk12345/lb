package com.longbei.appservice.dao;

import com.longbei.appservice.entity.DeviceRegister;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lixb on 2017/6/27.
 */
public interface DeviceIndexMapper {

    List<DeviceRegister> selectRegisterCountByDevice(@Param("deviceindex") String deviceIndex);

    int updateRegisterCount(@Param("deviceindex") String deviceIndex,
                            @Param("lastregistertime") String lastregistertime,
                            @Param("lastusername") String lastusername);

    int insert(DeviceRegister deviceRegister);

}
