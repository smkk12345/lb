package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "appuser")
public class AppUserMongoEntity {
	@Id
	private String  id; //用户id
	private String username;//手机号
	private String nickname;//昵称
	private String avatar;//头像

	private String sex;//性别

	private Double[] gispoint;
	
	@Transient
	private String islike = "0";//是否关注   0：未关注   1：已关注
    
	@Transient
    private String isfriend = "0"; //是否是好友    0：不是   1：是

	private long userid;

	
	@JsonInclude(Include.ALWAYS)
	public Double[] getGispoint() {
		return gispoint;
	}
	public void setGispoint(Double[] gispoint) {
		this.gispoint = gispoint;
	}

	@JsonInclude(Include.ALWAYS)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.userid = Long.parseLong(id);
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getIslike() {
		return islike;
	}
	public void setIslike(String islike) {
		this.islike = islike;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getIsfriend() {
		return isfriend;
	}
	public void setIsfriend(String isfriend) {
		this.isfriend = isfriend;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getUserid() {
		return userid;
	}
}
