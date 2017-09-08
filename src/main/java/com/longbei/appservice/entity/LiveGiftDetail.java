package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by lixb on 2017/8/12.
 */
public class LiveGiftDetail implements Serializable {

    private Integer id;

    private Long fromuid; //赠送者id

    private Long touid; //接受人id

    private Long giftid; //giftid 礼物类型id

    private Integer num;

    private String gifttitle;

    private Long businessid;

    private String businesstype;

    private String status; 

    private Date createtime;

    private Date updatetime;
    
    
    //----------------扩展字段------------------
    
    private AppUserMongoEntity appUserMongoEntity;
    
    private String picurl;//礼物访问图片路径
    
    public LiveGiftDetail(){
    	super();
    }

    public LiveGiftDetail(Long fromuid,
                          Long touid,
                          Long giftid,
                          String gifttitle,
                          Integer num,
                          Long businessid,
                          String busenesstype){
        this.fromuid = fromuid;
        this.touid = touid;
        this.businesstype = busenesstype;
        this.giftid = giftid;
        this.gifttitle = gifttitle;
        this.num = num;
        this.businessid = businessid;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public String getGifttitle() {
        return gifttitle;
    }

    public void setGifttitle(String gifttitle) {
        this.gifttitle = gifttitle;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getGiftid() {
        return giftid;
    }

    public void setGiftid(Long giftid) {
        this.giftid = giftid;
    }

    public Long getTouid() {
        return touid;
    }

    public void setTouid(Long touid) {
        this.touid = touid;
    }

    public Long getFromuid() {
        return fromuid;
    }

    public void setFromuid(Long fromuid) {
        this.fromuid = fromuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getBusinesstype() {
        return businesstype;
    }

	public AppUserMongoEntity getAppUserMongoEntity() {
		return appUserMongoEntity;
	}

	public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
		this.appUserMongoEntity = appUserMongoEntity;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

}
