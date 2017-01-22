package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "appusermongo")
public class AppUserMongoEntity {
	@Id
	private String id; //用户id
	private String username;//手机号
	private String nickname;//昵称
	private String avatar;//头像
	private String gender;//性别
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	private Double[] gispoint;
	
	public Double[] getGispoint() {
		return gispoint;
	}
	public void setGispoint(Double[] gispoint) {
		this.gispoint = gispoint;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
}
