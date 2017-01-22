package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.Gson;
import com.longbei.appservice.common.constant.Constant;
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
	private String type;
	private String photos;


	private Long rankId;
	private Long awardId;
	private String awardname;
	private String awardlevel;
	private String itype; // 进步类型
	private int sortnum;
	private double price;
	private int indexnum = 0;

	private String ispublic;
	private int likes = 0; // 进步赞数
	private int flowers = 0; // 进步花数
	private int commentgeos = 0; // 进步评论数
	private int likeAndFlowerCount; // 点赞送花总数
	
	private String hasAddLike;
	private String hasGiveFlower;
	private boolean isupdate;//是否可编辑

	private Date createdate;



	public int getIndexnum() {
		return indexnum;
	}

	public void setIndexnum(int indexnum) {
		this.indexnum = indexnum;
	}

	public boolean isIsupdate() {
		return isupdate;
	}

	public void setIsupdate(boolean isupdate) {
		this.isupdate = isupdate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getImproveId() {
		return improveId;
	}

	public void setImproveId(Long improveId) {
		this.improveId = improveId;
	}

	public Long getRankId() {
		return rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

	public Long getAwardId() {
		return awardId;
	}

	public void setAwardId(Long awardId) {
		this.awardId = awardId;
	}

	public String getAwardname() {
		return awardname;
	}

	public void setAwardname(String awardname) {
		this.awardname = awardname;
	}

	public String getAwardlevel() {
		return awardlevel;
	}

	public void setAwardlevel(String awardlevel) {
		this.awardlevel = awardlevel;
	}

	public int getSortnum() {
		return sortnum;
	}

	public void setSortnum(int sortnum) {
		this.sortnum = sortnum;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}

	@JsonInclude(Include.ALWAYS)
	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	@JsonInclude(Include.ALWAYS)
	public int getFlowers() {
		return flowers;
	}

	public void setFlowers(int flowers) {
		this.flowers = flowers;
	}

	@JsonInclude(Include.ALWAYS)
	public int getCommentgeos() {
		return commentgeos;
	}

	public void setCommentgeos(int commentgeos) {
		this.commentgeos = commentgeos;
	}

	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
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

}
