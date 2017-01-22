package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.longbei.appservice.common.utils.StringUtils;

@Document(collection = "maincomment")
public class Comment {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;
	private String content;    //评论内容
	private String createtime; //评论时间
	private String itype;      //类型    0:进步(零散)评论  1：榜评论   2：教室微进步评论   3：圈子评论   4:目标进步评论
	private String itypeid;    //各类型对应的id
	@Transient
	private List<CommentLower> lowerList = new ArrayList<>();
	
	public Comment(){
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	
	@JsonInclude(Include.ALWAYS)
	public String getItype() {
		return itype;
	}
	public void setItype(String itype) {
		this.itype = itype;
	}
	
	@JsonInclude(Include.ALWAYS)
	public String getItypeid() {
		return itypeid;
	}
	public void setItypeid(String itypeid) {
		this.itypeid = itypeid;
	}

	@JsonInclude(Include.ALWAYS)
	public List<CommentLower> getLowerList() {
		return lowerList;
	}

	public void setLowerList(List<CommentLower> lowerList) {
		this.lowerList = lowerList;
	}
}
