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

/**
 * @author yinxc
 * 主评论点赞详情表
 * 2017年2月3日
 * return_type
 * CommentLikes
 */
@Document(collection = "commentlikes")
public class CommentLikes implements Serializable {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String commentid;  //主评论id
	private String userid;     //主评论商户id
	private String friendid;   //点赞者商户id
	private Date createtime; //评论时间
	
	public CommentLikes(){
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonInclude(Include.ALWAYS)
	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@JsonInclude(Include.ALWAYS)
	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
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
