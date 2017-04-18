package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

	private String updatetime;
	private String createtime;
	@JsonSerialize(using=ToStringSerializer.class)
	private Long userid;
	private String isfriend="0";
	private String isfans="0";

	private int distance;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public AppUserMongoEntity(){}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public Long getUserid() {
		return this.id != null?Long.parseLong(id):null;
	}

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

	public void setUserid(String userid){
		this.userid = this.id != null? Long.parseLong(this.id):null;
	}
}
