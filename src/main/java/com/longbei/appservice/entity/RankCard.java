package com.longbei.appservice.entity;

import java.util.Date;

public class RankCard {
    /**
     * 
     */
    private Integer id;

    /**
     * 榜主名称
     */
    private String adminname;

    /**
     * 简介
     */
    private String adminbrief;

    /**
     * 创建人id
     */
    private Integer createuserid;

    /**
     * 头像
     */
    private String adminpic;

    /**
     * 图文详情
     */
    private String admindetail;

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

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname == null ? null : adminname.trim();
    }

    public String getAdminbrief() {
        return adminbrief;
    }

    public void setAdminbrief(String adminbrief) {
        this.adminbrief = adminbrief == null ? null : adminbrief.trim();
    }

    public Integer getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(Integer createuserid) {
        this.createuserid = createuserid;
    }

    public String getAdminpic() {
        return adminpic;
    }

    public void setAdminpic(String adminpic) {
        this.adminpic = adminpic == null ? null : adminpic.trim();
    }

    public String getAdmindetail() {
        return admindetail;
    }

    public void setAdmindetail(String admindetail) {
        this.admindetail = admindetail == null ? null : admindetail.trim();
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