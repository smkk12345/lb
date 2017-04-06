package com.longbei.appservice.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserFeedback {
    private Long id;

    private Long userid;//用户id
    
    private String username; //用户手机号

    private String content;//反馈内容

    private String photos;//图片

    private Date createtime;//反馈时间

    private String status;//0--未处理 1---无用  2---有用

    private Date dealtime;//处理时间

    private Long dealuser;//处理人

    private String checkoption;//回复内容

    private AppUserMongoEntity appUserMongoEntity; //用户信息

    private UserInfo userInfo;//用户信息

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public UserFeedback(){
    	super();
    }
    
    public UserFeedback(Long userid, String content, String photos, Date createtime, String status) {
		super();
		this.userid = userid;
		this.content = content;
		this.photos = photos;
		this.createtime = createtime;
		this.status = status;
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
    @JsonInclude(Include.ALWAYS)
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

    @JsonInclude(Include.ALWAYS)
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    @JsonInclude(Include.ALWAYS)
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * 反馈内容
     * @return content 反馈内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 反馈内容
     * @param content 反馈内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 图片
     * @return photos 图片
     */
    public String getPhotos() {
        return photos;
    }

    /**
     * 图片
     * @param photos 图片
     */
    public void setPhotos(String photos) {
        this.photos = photos == null ? null : photos.trim();
    }

    /**
     * 反馈时间
     * @return createtime 反馈时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 反馈时间
     * @param createtime 反馈时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 0--未处理 1---无用  2---有用
     * @return status 0--未处理 1---无用  2---有用
     */
    @JsonInclude(Include.ALWAYS)
    public String getStatus() {
        return status;
    }

    /**
     * 0--未处理 1---无用  2---有用
     * @param status 0--未处理 1---无用  2---有用
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 处理时间
     * @return dealtime 处理时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getDealtime() {
        return dealtime;
    }

    /**
     * 处理时间
     * @param dealtime 处理时间
     */
    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }

    /**
     * 处理人
     * @return dealuser 处理人
     */
    @JsonInclude(Include.ALWAYS)
    public Long getDealuser() {
        return dealuser;
    }

    /**
     * 处理人
     * @param dealuser 处理人
     */
    public void setDealuser(Long dealuser) {
        this.dealuser = dealuser;
    }

    /**
     * 
     * @return checkoption 
     */
    public String getCheckoption() {
        return checkoption;
    }

    /**
     * 
     * @param checkoption 
     */
    public void setCheckoption(String checkoption) {
        this.checkoption = checkoption == null ? null : checkoption.trim();
    }
}