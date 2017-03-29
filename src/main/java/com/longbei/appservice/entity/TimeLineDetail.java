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
	private String fileKey;
	private String sourcekey;
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
	private String businesstype;
	private Long businessid;
	private String isrecommend; //是否被推荐 0 - 否 1 - 是
	private Date recommendtime; //推荐时间
	private int sort; //推荐排序

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public Long getBusinessid() {
		return businessid;
	}

	public void setBusinessid(Long businessid) {
		this.businessid = businessid;
	}

	public String getIsrecommend() {
		return isrecommend;
	}

	public void setIsrecommend(String isrecommend) {
		this.isrecommend = isrecommend;
	}

	public Date getRecommendtime() {
		return recommendtime;
	}

	public void setRecommendtime(Date recommendtime) {
		this.recommendtime = recommendtime;
	}

	public String getSourcekey() {
		return sourcekey;
	}

	public void setSourcekey(String sourcekey) {
		this.sourcekey = sourcekey;
	}

	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}

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
