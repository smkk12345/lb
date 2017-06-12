package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.longbei.appservice.common.utils.StringUtils;

@Document(collection = "questions")
public class ClassroomQuestions {
	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;     //问题创建者id
	private String content;    //问题内容
	private String classroomid;    //教室id
	private String createtime; //创建时间
	@Transient
	private List<ClassroomQuestionsLower> lowerList = new ArrayList<ClassroomQuestionsLower>();
	@Transient
	private AppUserMongoEntity appUserMongoEntityUserid; //问题用户信息----Userid
	@Transient
	private String isreply; //是否已回答  0：未回答  1：已回答  2:页面不显示天津唉回答(只有教室老师有回答权限)
	
	
	@JsonInclude(Include.ALWAYS)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getIsreply() {
		return isreply;
	}

	public void setIsreply(String isreply) {
		this.isreply = isreply;
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
	public String getClassroomid() {
		return classroomid;
	}
	
	public void setClassroomid(String classroomid) {
		this.classroomid = classroomid;
	}
	
	@JsonInclude(Include.ALWAYS)
	public List<ClassroomQuestionsLower> getLowerList() {
		return lowerList;
	}
	
	public void setLowerList(List<ClassroomQuestionsLower> lowerList) {
		this.lowerList = lowerList;
	}
	
	public AppUserMongoEntity getAppUserMongoEntityUserid() {
		return appUserMongoEntityUserid;
	}
	
	public void setAppUserMongoEntityUserid(AppUserMongoEntity appUserMongoEntityUserid) {
		this.appUserMongoEntityUserid = appUserMongoEntityUserid;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		if (!StringUtils.isBlank(createtime) && createtime.indexOf(".0") > -1) {
			this.createtime = createtime.substring(0, createtime.length() - 2);
		}else{
			this.createtime = createtime;
		}
	}
	
}