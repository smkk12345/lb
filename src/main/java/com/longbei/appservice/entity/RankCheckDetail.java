package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RankCheckDetail {
    /**
     * 
     */
    private Integer id;

    /**
     * 榜单id
     */
    private Long rankid;

    /**
     * 审核结果 2 - 不通过 3 - 不通过 不可修改 4 - 审核通过
     */
    private String checkstatus;

    /**
     * 审核人ID
     */
    private Integer checkuserid;

    /**
     * 审核详情
     */
    private String checkideadetail;

    /**
     * 审核意见
     */
    private String checkidea;

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

    public Long getRankid() {
        return rankid;
    }

    public void setRankid(Long rankid) {
        this.rankid = rankid;
    }

    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus == null ? null : checkstatus.trim();
    }

    public Integer getCheckuserid() {
        return checkuserid;
    }

    public void setCheckuserid(Integer checkuserid) {
        this.checkuserid = checkuserid;
    }

    public String getCheckideadetail() {
        return checkideadetail;
    }

    public void setCheckideadetail(String checkideadetail) {
        this.checkideadetail = checkideadetail == null ? null : checkideadetail.trim();
    }

    public String getCheckidea() {
        return checkidea;
    }

    public void setCheckidea(String checkidea) {
        this.checkidea = checkidea == null ? null : checkidea.trim();
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
}