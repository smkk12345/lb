package com.longbei.appservice.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.longbei.appservice.common.utils.StringUtils;

@Document(collection = "maincomment")
public class Comment {

	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	private String userid;     //评论商户id
	@Transient
	private String friendid;   //被评论商户id
	private String content;    //评论内容
	private String createtime; //评论时间
	private String businesstype;      //businesstype  类型    0 零散进步评论   1 目标进步评论    2 榜中微进步评论  3圈子中微进步评论 4 教室中微进步评论
	 									//10：榜评论  11 圈子评论  12 教室评论
	private String businessid;    //各类型对应的id
	
	private String impid; //进步id
	@Transient
	private List<CommentLower> lowerList = new ArrayList<>();
	@Transient
	private String isaddlike = "0";  //是否已点赞     0:未点赞  1：已点赞
	@Transient
	private AppUserMongoEntity appUserMongoEntityUserid; //评论用户信息----Userid
	
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
	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	@JsonInclude(Include.ALWAYS)
	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	@JsonInclude(Include.ALWAYS)
	public List<CommentLower> getLowerList() {
		return lowerList;
	}

	public void setLowerList(List<CommentLower> lowerList) {
		this.lowerList = lowerList;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIsaddlike() {
		return isaddlike;
	}

	public void setIsaddlike(String isaddlike) {
		this.isaddlike = isaddlike;
	}

	@JsonInclude(Include.ALWAYS)
	public String getFriendid() {
		return friendid;
	}

	public void setFriendid(String friendid) {
		this.friendid = friendid;
	}

	public AppUserMongoEntity getAppUserMongoEntityUserid() {
		return appUserMongoEntityUserid;
	}

	public void setAppUserMongoEntityUserid(AppUserMongoEntity appUserMongoEntityUserid) {
		this.appUserMongoEntityUserid = appUserMongoEntityUserid;
	}

	public String getImpid() {
		return impid;
	}

	public void setImpid(String impid) {
		this.impid = impid;
	}
	
}
