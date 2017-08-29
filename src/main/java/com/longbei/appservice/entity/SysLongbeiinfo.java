package com.longbei.appservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SysLongbeiinfo implements Serializable {
    private Integer id;

    private String about;//关于龙杯

    private String copyright;//版权

    private String companyname;//公司名称


    private String isdefault;//0 默认  1 非默认

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
     * 关于龙杯
     * @return about 关于龙杯
     */
    public String getAbout() {
        return about;
    }

    /**
     * 关于龙杯
     * @param about 关于龙杯
     */
    public void setAbout(String about) {
        this.about = about == null ? null : about.trim();
    }

    /**
     * 版权
     * @return copyright 版权
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * 版权
     * @param copyright 版权
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright == null ? null : copyright.trim();
    }

    /**
     * 公司名称 
     * @return companyname 公司名称 
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * 公司名称 
     * @param companyname 公司名称 
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    /**
     * 0 默认  1 非默认
     * @return isdefault 0 默认  1 非默认
     */
    public String getIsdefault() {
        return isdefault;
    }

    /**
     * 0 默认  1 非默认
     * @param isdefault 0 默认  1 非默认
     */
    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault == null ? null : isdefault.trim();
    }
}