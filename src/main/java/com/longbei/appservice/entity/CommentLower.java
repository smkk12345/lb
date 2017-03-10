package com.longbei.appservice.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.longbei.appservice.common.utils.StringUtils;

@Document(collection = "commentlower")
public class CommentLower {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	/**
	 * @author yinxc
	 * 子评论    A回复B   A的id ： firstuserid    B的id：seconduserid
	 * 2017年3月10日
	 */
	private String firstuserid;     //商户id
	private String content;    //评论内容
	private String seconduserid;   //被评论商户id
	private String createtime; //评论时间
	private String commentid;  //主评论id
	private String status;     //0:不显示回复    1:显示回复
	@Transient
	private String nickname;  
//	private AppUserMongoEntity appUserMongoEntityUserid; //评论用户信息----Userid
	@Transient
	private String friendNickname;
//	private AppUserMongoEntity appUserMongoEntityFriendid; //评论用户信息----Friendid

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

	public String getContent() {
		return content;
	}

	@JsonInclude(Include.ALWAYS)
	public String getFirstuserid() {
		return firstuserid;
	}

	public void setFirstuserid(String firstuserid) {
		this.firstuserid = firstuserid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getSeconduserid() {
		return seconduserid;
	}

	public void setSeconduserid(String seconduserid) {
		this.seconduserid = seconduserid;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFriendNickname() {
		return friendNickname;
	}

	public void setFriendNickname(String friendNickname) {
		this.friendNickname = friendNickname;
	}

}
