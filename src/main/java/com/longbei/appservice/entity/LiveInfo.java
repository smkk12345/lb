package com.longbei.appservice.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*
 * 教室直播课程联查id
 */
@Document(collection = "liveinfo")
public class LiveInfo implements Serializable {
	
	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	
	private long classroomid;//所属教室id
	
	private Long courseid;   //课程id
	
	private long liveid;     //直播id
	
	private long userid;     //用户id
	
	private Date createtime; //评论时间
	
	public LiveInfo(){
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
	public long getClassroomid() {
		return classroomid;
	}

	public void setClassroomid(long classroomid) {
		this.classroomid = classroomid;
	}

	@JsonInclude(Include.ALWAYS)
	public Long getCourseid() {
		return courseid;
	}

	public void setCourseid(Long courseid) {
		this.courseid = courseid;
	}

	@JsonInclude(Include.ALWAYS)
	public long getLiveid() {
		return liveid;
	}

	public void setLiveid(long liveid) {
		this.liveid = liveid;
	}

	@JsonInclude(Include.ALWAYS)
	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getCreatetime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beijingcal = Calendar.getInstance();
        beijingcal.setTimeInMillis(createtime.getTime());
        return sf.format(beijingcal.getTime());
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

}
