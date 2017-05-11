package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "userrelationchange")
public class UserRelationChange {
	@Id
	private String id = UUID.randomUUID().toString();
	private String uid;//用户id
	private String changeuid;
	private Date updatetime;

	public void setChangeuid(String changeuid) {
		this.changeuid = changeuid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public String getChangeuid() {
		return changeuid;
	}

	public String getId() {
		return id;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

}
