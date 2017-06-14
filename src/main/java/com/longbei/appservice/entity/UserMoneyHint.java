package com.longbei.appservice.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 进步币消息提示类
 */
@Document(collection = "userloginhint")
public class UserMoneyHint {

	@Id
	private String id;
	private String userid;
	private String money;
	private String drawtime;
	
	public UserMoneyHint(){
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

	@JsonInclude(Include.ALWAYS)
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getDrawtime() {
		return drawtime;
	}

	public void setDrawtime(String drawtime) {
		this.drawtime = drawtime;
	}
	
	
}
