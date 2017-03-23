package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class HomePicture {
    /**
     * 
     */
    private Integer id;

    /**
     * 名称
     */
    private String picname;

    /**
     * 图片
     */
    private String photos;

    /**
     * 关联 内容类型 0 - 图文 1 - 专题  2 - 连接
     */
    private String contenttype;

    /**
     * L浏览量
     */
    private Integer scannum;

    /**
     * 是否上线 0 - 未上线 1 - 上线
     */
    private String isup;

    /**
     * 上线时间
     */
    private Date update;

    /**
     * 下线时间
     */
    private Date downdate;

    /**
     * 排序
     */
    private String sort;

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

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname == null ? null : picname.trim();
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos == null ? null : photos.trim();
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype == null ? null : contenttype.trim();
    }

    public Integer getScannum() {
        return scannum;
    }

    public void setScannum(Integer scannum) {
        this.scannum = scannum;
    }

    public String getIsup() {
        return isup;
    }

    public void setIsup(String isup) {
        this.isup = isup == null ? null : isup.trim();
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDowndate() {
        return downdate;
    }

    public void setDowndate(Date downdate) {
        this.downdate = downdate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
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