package com.longbei.appservice.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.longbei.appservice.common.utils.StringUtils;

@Document(collection = "commentlower")
public class CommentLower {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;     //商户id
	private String content;    //评论内容
	private String friendid;   //被评论商户id
	private String createtime; //评论时间
	private String commentid;  //主评论id
	private String status;     //0:不显示回复    1:显示回复

	public CommentLower(){
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

	@JsonInclude(Include.ALWAYS)
	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
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
	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
