package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SysAppupdate {
    private Integer id;

    private String ttype;//终端类别 0：android；1：ios

    private String version;//当前版本

    private Date updatetime;//更新时间

    private String enforced;//是否强制更新 0：不强制；1：强制

    private String url;//下载地址

    private String remark;//备注

    private String updateexplain;//版本更新描述

    private String enforceversion;

    public void setEnforceversion(String enforceversion) {
        this.enforceversion = enforceversion;
    }

    public String getEnforceversion() {
        return enforceversion;
    }
    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 终端类别 0：android；1：ios
     * @return ttype 终端类别 0：android；1：ios
     */
    public String getTtype() {
        return ttype;
    }

    /**
     * 终端类别 0：android；1：ios
     * @param ttype 终端类别 0：android；1：ios
     */
    public void setTtype(String ttype) {
        this.ttype = ttype == null ? null : ttype.trim();
    }

    /**
     * 当前版本
     * @return version 当前版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 当前版本
     * @param version 当前版本
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 是否强制更新 0：不强制；1：强制
     * @return enforced 是否强制更新 0：不强制；1：强制
     */
    public String getEnforced() {
        return enforced;
    }

    /**
     * 是否强制更新 0：不强制；1：强制
     * @param enforced 是否强制更新 0：不强制；1：强制
     */
    public void setEnforced(String enforced) {
        this.enforced = enforced == null ? null : enforced.trim();
    }

    /**
     * 下载地址
     * @return url 下载地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 下载地址
     * @param url 下载地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 版本更新描述
     * @return updateexplain 版本更新描述
     */
    public String getUpdateexplain() {
        return updateexplain;
    }

    /**
     * 版本更新描述
     * @param updateexplain 版本更新描述
     */
    public void setUpdateexplain(String updateexplain) {
        this.updateexplain = updateexplain == null ? null : updateexplain.trim();
    }
}