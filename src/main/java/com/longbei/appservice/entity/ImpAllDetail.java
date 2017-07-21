package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ImpAllDetail {
    private Long id;

    private Long userid;//用户id

    private Long impid;//微进步id

    private String status;//点赞，还是已经取消  0 赞。1 取消赞

    private Integer times;//点击次数

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private String businesstype;//0 零散 1 目标中 2 榜中 3 圈中 4教室中

    private String detailtype; //明细类型 0 - 赞  1 - 花  2 - 钻

    private String giftnum;  //礼物数量 如 花，钻

    private AppUserMongoEntity appUser;

    private Integer startno;

    private Integer pagesize;

    private Integer businessid;

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public Integer getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Integer businessid) {
        this.businessid = businessid;
    }

    public AppUserMongoEntity getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserMongoEntity appUser) {
        this.appUser = appUser;
    }

    public Integer getStartno() {
        return startno;
    }

    public void setStartno(Integer startno) {
        this.startno = startno;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public String getDetailtype() {
        return detailtype;
    }

    public void setDetailtype(String detailtype) {
        this.detailtype = detailtype;
    }

    public String getGiftnum() {
        return giftnum;
    }

    public void setGiftnum(String giftnum) {
        this.giftnum = giftnum;
    }

    /**
     * 
     * @return id 
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 微进步id
     * @return impid 微进步id
     */
    public Long getImpid() {
        return impid;
    }

    /**
     * 微进步id
     * @param impid 微进步id
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 点赞，还是已经取消
     * @return status 点赞，还是已经取消
     */
    public String getStatus() {
        return status;
    }

    /**
     * 点赞，还是已经取消
     * @param status 点赞，还是已经取消
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 点击次数
     * @return times 点击次数
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * 点击次数
     * @param times 点击次数
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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


}