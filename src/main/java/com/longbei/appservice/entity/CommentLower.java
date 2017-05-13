package com.longbei.appservice.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private Date createtime; //评论时间
	private String commentid;  //主评论id
	private String status;     //0:不显示回复    1:显示回复
	@Transient
	private String firstNickname;
//	private AppUserMongoEntity appUserMongoEntityUserid; //评论用户信息----Userid
	@Transient
	private String secondNickname;
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

	public String getCreatetime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beijingcal = Calendar.getInstance();
        beijingcal.setTimeInMillis(createtime.getTime());
        return sf.format(beijingcal.getTime());
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
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

	public void setFirstNickname(String firstNickname) {
		this.firstNickname = firstNickname;
	}

	public void setSecondNickname(String secondNickname) {
		this.secondNickname = secondNickname;
	}

	public String getFirstNickname() {

		return firstNickname;
	}

	public String getSecondNickname() {
		return secondNickname;
	}
}
