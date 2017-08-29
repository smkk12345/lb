package com.longbei.appservice.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "hotline")
public class HotLine implements Serializable {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;     //评论商户id
	private Date mymaxtime;    //获取"我的"页面---日期
	private Date rankmaxtime;  //获取@我消息---日期
	private Date informmaxtime;  //获取通知消息---日期
	private Date friendAskmaxtime; //获取添加好友的申请---日期
	
	public HotLine(){
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

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getMymaxtime() {
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar beijingcal = Calendar.getInstance();
//        beijingcal.setTimeInMillis(mymaxtime.getTime());
//        return sf.format(beijingcal.getTime());
		return mymaxtime;
	}

	public void setMymaxtime(Date mymaxtime) {
		this.mymaxtime = mymaxtime;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getRankmaxtime() {
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar beijingcal = Calendar.getInstance();
//        beijingcal.setTimeInMillis(rankmaxtime.getTime());
//        return sf.format(beijingcal.getTime());
		return rankmaxtime;
	}

	public void setRankmaxtime(Date rankmaxtime) {
		this.rankmaxtime = rankmaxtime;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getInformmaxtime() {
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar beijingcal = Calendar.getInstance();
//        beijingcal.setTimeInMillis(informmaxtime.getTime());
//        return sf.format(beijingcal.getTime());
		return informmaxtime;
	}

	public void setInformmaxtime(Date informmaxtime) {
		this.informmaxtime = informmaxtime;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getFriendAskmaxtime() {
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar beijingcal = Calendar.getInstance();
//        beijingcal.setTimeInMillis(friendAskmaxtime.getTime());
//        return sf.format(beijingcal.getTime());
		return friendAskmaxtime;
	}

	public void setFriendAskmaxtime(Date friendAskmaxtime) {
		this.friendAskmaxtime = friendAskmaxtime;
	}
	
}
