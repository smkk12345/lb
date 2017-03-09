package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
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
	
	private long userid;
	private String isfriend;
	private String isfans;

	public void setIsfriend(String isfriend) {
		this.isfriend = isfriend;
	}

	public void setIsfans(String isfans) {
		this.isfans = isfans;
	}

	public String getIsfriend() {
		return isfriend;
	}

	public String getIsfans() {
		return isfans;
	}

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
	
	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getUserid() {
		return userid;
	}
}
