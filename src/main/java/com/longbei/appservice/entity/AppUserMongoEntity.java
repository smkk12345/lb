package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "appusermongo")
public class AppUserMongoEntity {
	@Id
	private Long id; //用户id
	private String username;//手机号
	private String nickname;//昵称
	private String avatar;//头像

	private String sex;//性别

	private Double[] gispoint;
	
	public Double[] getGispoint() {
		return gispoint;
	}
	public void setGispoint(Double[] gispoint) {
		this.gispoint = gispoint;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

}
