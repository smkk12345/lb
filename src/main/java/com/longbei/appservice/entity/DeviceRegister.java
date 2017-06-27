package com.longbei.appservice.entity;

import java.util.Date;

/**
 * Created by lixb on 2017/6/27.
 */
public class DeviceRegister {
    private String deviceindex;//设备编号
    private int registercount;//注册数量
    private Date lastregistertime;//最后注册时间
    private String lastusername;//最后手机号

    public void setDeviceindex(String deviceindex) {
        this.deviceindex = deviceindex;
    }

    public void setLastregistertime(Date lastregistertime) {
        this.lastregistertime = lastregistertime;
    }

    public void setLastusername(String lastusername) {
        this.lastusername = lastusername;
    }

    public void setRegistercount(int registercount) {
        this.registercount = registercount;
    }

    public Date getLastregistertime() {
        return lastregistertime;
    }

    public int getRegistercount() {
        return registercount;
    }

    public String getDeviceindex() {
        return deviceindex;
    }

    public String getLastusername() {
        return lastusername;
    }


}
