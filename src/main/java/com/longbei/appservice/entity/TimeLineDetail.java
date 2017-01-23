package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.*;

@Document(collection = "timelinedetail")
public class TimeLineDetail {
	@Id
	private String id = UUID.randomUUID().toString().replace("-", "_");
	@DBRef
	private AppUserMongoEntity user;
	private Long improveId;
	private String title;
	private String brief;
	private String photos;
	private String itype; // 进步类型
	private int sortnum;
	private int indexnum = 0;
	private String ispublic;
	private int likes = 0; // 进步赞数
	private int flowers = 0; // 进步花数
	private int commentgeos = 0; // 进步评论数
	private int likeAndFlowerCount; // 点赞送花总数
	private String hasAddLike;
	private String hasGiveFlower;
	private Date createdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppUserMongoEntity getUser() {
		return user;
	}

	public void setUser(AppUserMongoEntity user) {
		this.user = user;
	}

	public Long getImproveId() {
		return improveId;
	}

	public void setImproveId(Long improveId) {
		this.improveId = improveId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}

	public int getSortnum() {
		return sortnum;
	}

	public void setSortnum(int sortnum) {
		this.sortnum = sortnum;
	}

	public int getIndexnum() {
		return indexnum;
	}

	public void setIndexnum(int indexnum) {
		this.indexnum = indexnum;
	}

	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getFlowers() {
		return flowers;
	}

	public void setFlowers(int flowers) {
		this.flowers = flowers;
	}

	public int getCommentgeos() {
		return commentgeos;
	}

	public void setCommentgeos(int commentgeos) {
		this.commentgeos = commentgeos;
	}

	public int getLikeAndFlowerCount() {
		return likeAndFlowerCount;
	}

	public void setLikeAndFlowerCount(int likeAndFlowerCount) {
		this.likeAndFlowerCount = likeAndFlowerCount;
	}

	public String getHasAddLike() {
		return hasAddLike;
	}

	public void setHasAddLike(String hasAddLike) {
		this.hasAddLike = hasAddLike;
	}

	public String getHasGiveFlower() {
		return hasGiveFlower;
	}

	public void setHasGiveFlower(String hasGiveFlower) {
		this.hasGiveFlower = hasGiveFlower;
	}

	public String getCreatedate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beijingcal = Calendar.getInstance();
        beijingcal.setTimeInMillis(createdate.getTime());
        return sf.format(beijingcal.getTime());
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


}
