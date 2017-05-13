package com.longbei.appservice.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "commentcount")
public class CommentCount {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
//	private String itype;      //类型    0:进步(零散)评论  1：榜评论   2：教室微进步评论   3：圈子评论   4:目标进步评论
//	private String itypeid;    //各类型对应的id
	private String commentid;  //主评论id
	private int comcount;        //评论总数
	private int likes;         //点赞数
	private Date createtime; //添加时间
	
	public CommentCount(){
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
	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	@JsonInclude(Include.ALWAYS)
	public int getComcount() {
		return comcount;
	}

	public void setComcount(int comcount) {
		this.comcount = comcount;
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
