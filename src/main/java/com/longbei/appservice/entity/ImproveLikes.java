package com.longbei.appservice.entity;

import java.util.Date;

public class ImproveLikes {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Long impid;

    /**
     * 
     */
    private Long businessid;

    /**
     * 
     */
    private String businesstype;

    /**
     * 
     */
    private Integer likes;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getImpid() {
        return impid;
    }

    public void setImpid(Long impid) {
        this.impid = impid;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype == null ? null : businesstype.trim();
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
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