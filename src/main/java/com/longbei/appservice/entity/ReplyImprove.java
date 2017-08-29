package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * 进步---教室相关
 */
public class ReplyImprove implements Serializable {

	private Long impid;//微进步id
	private String itype;//类型  0 文字进步 1 图片进步 2 视频进步 3 音频进步 4 文件
	private String brief;//说明
	private String pickey;//图片的key
	private String filekey;//文件key  视频文件  音频文件 普通文件
	private Long userid;//用户id
	private Date createtime;//创建时间
	private String duration;

	private String picattribute; //图片属性
	
	protected Long businessid;//业务id  榜单id  圈子id 教室id
	
	protected String businesstype;//微进步关联的业务类型 0 未关联 1 目标  2 榜 3 圈子 4教室
	
	private AppUserMongoEntity appUserMongoEntity; //进步用户信息
	//评论
	private List<Comment> list = new ArrayList<Comment>();
		

	public ReplyImprove(){
		super();
	}
	
	public ReplyImprove(Long impid, String itype, String brief, String pickey, Long userid, 
			Long businessid, String businesstype, Date createtime){
		this.impid = impid;
		this.itype = itype;
		this.brief = brief;
		this.pickey = pickey;
		this.userid = userid;
		this.businessid = businessid;
		this.businesstype = businesstype;
		this.createtime = createtime;
	}
	
	public Long getImpid() {
		return impid;
	}
	public void setImpid(Long impid) {
		this.impid = impid;
	}
	public String getItype() {
		return itype;
	}
	public void setItype(String itype) {
		this.itype = itype;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getPickey() {
		return pickey;
	}
	public void setPickey(String pickey) {
		this.pickey = pickey;
	}
	public String getFilekey() {
		return filekey;
	}
	public void setFilekey(String filekey) {
		this.filekey = filekey;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public AppUserMongoEntity getAppUserMongoEntity() {
		return appUserMongoEntity;
	}
	public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
		this.appUserMongoEntity = appUserMongoEntity;
	}
	
	public List<Comment> getList() {
		return list;
	}

	public void setList(List<Comment> list) {
		this.list = list;
	}

	public Long getBusinessid() {
		return businessid;
	}

	public void setBusinessid(Long businessid) {
		this.businessid = businessid;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setPicattribute(String picattribute) {
		this.picattribute = picattribute;
	}

	public String getDuration() {
		return duration;
	}

	public String getPicattribute() {
		return picattribute;
	}
}
