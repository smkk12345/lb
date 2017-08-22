package com.longbei.appservice.entity;

import java.util.Date;

public class UserSpecialcase {
    /**
     * 
     */
    private Integer id;

    /**
     * 切换登陆的手机号 以逗号隔开
     */
    private String noSwitchLogin;

    /**
     * 注册的手机号 以逗号隔开，标示该手机号注册不受手机设备号限制
     */
    private String noRegisterLimit;

    /**
     * 操作人uid 
     */
    private Long createuid;

    /**
     * 操作时间
     */
    private Date operatedate;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoSwitchLogin() {
        return noSwitchLogin;
    }

    public void setNoSwitchLogin(String noSwitchLogin) {
        this.noSwitchLogin = noSwitchLogin == null ? null : noSwitchLogin.trim();
    }

    public String getNoRegisterLimit() {
        return noRegisterLimit;
    }

    public void setNoRegisterLimit(String noRegisterLimit) {
        this.noRegisterLimit = noRegisterLimit == null ? null : noRegisterLimit.trim();
    }

    public Long getCreateuid() {
        return createuid;
    }

    public void setCreateuid(Long createuid) {
        this.createuid = createuid;
    }

    public Date getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(Date operatedate) {
        this.operatedate = operatedate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}