package com.longbei.appservice.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by lixb on 2017/8/11.
 */
public class LiveGift {

    private int id;

    private Long giftid;//礼物id】

    private String title ;//礼物名称

    private int price;//价格 龙币

    private String fileurl;//礼物访问路径

    private String doublehit;//是否可以连击 0 不可以 1可以

    private Date createetime;//创建时间

    private Date updatetime;//更新时间

    private String isdel;//是否删除  0 未删除 1删除


    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateetime() {
        return createetime;
    }

    public void setCreateetime(Date createetime) {
        this.createetime = createetime;
    }

    public String getDoublehit() {
        return doublehit;
    }

    public void setDoublehit(String doublehit) {
        this.doublehit = doublehit;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getGiftid() {
        return giftid;
    }

    public void setGiftid(Long giftid) {
        this.giftid = giftid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
