package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class SysAppupdateerror implements Serializable {
    private Integer id;

    private String ttype;

    private String appversion;

    private String erromessage;

    private Date drawdate;

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
     * 
     * @return ttype 
     */
    public String getTtype() {
        return ttype;
    }

    /**
     * 
     * @param ttype 
     */
    public void setTtype(String ttype) {
        this.ttype = ttype == null ? null : ttype.trim();
    }

    /**
     * 
     * @return appversion 
     */
    public String getAppversion() {
        return appversion;
    }

    /**
     * 
     * @param appversion 
     */
    public void setAppversion(String appversion) {
        this.appversion = appversion == null ? null : appversion.trim();
    }

    /**
     * 
     * @return erromessage 
     */
    public String getErromessage() {
        return erromessage;
    }

    /**
     * 
     * @param erromessage 
     */
    public void setErromessage(String erromessage) {
        this.erromessage = erromessage == null ? null : erromessage.trim();
    }

    /**
     * 
     * @return drawdate 
     */
    public Date getDrawdate() {
        return drawdate;
    }

    /**
     * 
     * @param drawdate 
     */
    public void setDrawdate(Date drawdate) {
        this.drawdate = drawdate;
    }
}