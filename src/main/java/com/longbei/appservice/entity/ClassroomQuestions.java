package com.longbei.appservice.entity;

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

@Document(collection = "questions")
public class ClassroomQuestions {
	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;     //问题创建者id
	private String content;    //问题内容
	private String classroomid;    //教室id
	private Date createtime; //创建时间
	private String isignore = "0"; //是否已忽略  0：未忽略  1：已忽略
	@Transient
	private ClassroomQuestionsLower classroomQuestionsLower;
	@Transient
	private AppUserMongoEntity appUserMongoEntityUserid; //问题用户信息----Userid
	private String isreply = "0"; //是否已回答  0：未回答  1：已回答  2:页面不显示天津唉回答(只有教室老师有回答权限)

	
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
	
	public ClassroomQuestionsLower getClassroomQuestionsLower() {
		return classroomQuestionsLower;
	}

	public void setClassroomQuestionsLower(ClassroomQuestionsLower classroomQuestionsLower) {
		this.classroomQuestionsLower = classroomQuestionsLower;
	}
	
	public AppUserMongoEntity getAppUserMongoEntityUserid() {
		return appUserMongoEntityUserid;
	}
	
	public void setAppUserMongoEntityUserid(AppUserMongoEntity appUserMongoEntityUserid) {
		this.appUserMongoEntityUserid = appUserMongoEntityUserid;
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

	public String getIsignore() {
		return isignore;
	}

	public void setIsignore(String isignore) {
		this.isignore = isignore;
	}
}