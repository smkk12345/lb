package com.longbei.appservice.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "questionslower")
public class ClassroomQuestionsLower implements Serializable {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;     //回复者id---老师
	private String content;    //回复内容
	private String friendid;   //问题创建者id
	private Date createtime; //回复时间
	private String questionsid;  //问题id
	@Transient
	private AppUserMongoEntity appUserMongoEntityUserid; //回复者用户信息--老师--Userid
	
	public ClassroomQuestionsLower(){
		super();
	}

	@JsonInclude(Include.ALWAYS)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonInclude(Include.ALWAYS)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonInclude(Include.ALWAYS)
	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public String getCreatetime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beijingcal = Calendar.getInstance();
        beijingcal.setTimeInMillis(createtime.getTime());
        return sf.format(beijingcal.getTime());
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@JsonInclude(Include.ALWAYS)
	public String getQuestionsid() {
		return questionsid;
	}

	public void setQuestionsid(String questionsid) {
		this.questionsid = questionsid;
	}

	public AppUserMongoEntity getAppUserMongoEntityUserid() {
		return appUserMongoEntityUserid;
	}

	public void setAppUserMongoEntityUserid(AppUserMongoEntity appUserMongoEntityUserid) {
		this.appUserMongoEntityUserid = appUserMongoEntityUserid;
	}
	
}
