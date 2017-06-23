package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.longbei.appservice.common.utils.ShortUrlUtils;
import com.longbei.appservice.config.AppserviceConfig;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String createuserid;

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

    /** 榜主名片的地址 */
    private String rankCardUrl;

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

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
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

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getRankCardUrl() {
        return rankCardUrl;
    }

    public void setRankCardUrl(String rankCardId) {
        this.rankCardUrl = ShortUrlUtils.getShortUrl(AppserviceConfig.h5_rankcard+"?rankCardId="+rankCardId);
    }
}