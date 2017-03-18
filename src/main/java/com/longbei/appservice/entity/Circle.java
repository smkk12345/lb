package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Circle {
    private Integer id;

    private Long circleid;
    
    private String circletitle;//兴趣圈标题

    private String circlephotos;//兴趣圈图片

    private String circlebrief;//兴趣圈简介

    private Integer circleinvoloed;//兴趣圈参与人数

    private Long createuserid;//兴趣圈创建人id

    private Date createtime;//兴趣圈创建时间

    private Date updatetime;//兴趣圈更新时间

    private String ptype;//十全十美分类

    private Boolean ispublic;//是否所有人可见

    private Boolean needconfirm;//加圈子是否需要圈住审核

    private Boolean creategoup;//是否同时简历龙信群

    private Integer commentCount;//圈子的评论总数

    private String notice;//公告

    private AppUserMongoEntity appUserMongoEntity;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 兴趣圈标题
     * @return circletitle 兴趣圈标题
     */
    public String getCircletitle() {
        return circletitle;
    }

    /**
     * 兴趣圈标题
     * @param circletitle 兴趣圈标题
     */
    public void setCircletitle(String circletitle) {
        this.circletitle = circletitle == null ? null : circletitle.trim();
    }

    /**
     * 兴趣圈图片
     * @return circlephotos 兴趣圈图片
     */
    public String getCirclephotos() {
        return circlephotos;
    }

    /**
     * 兴趣圈图片
     * @param circlephotos 兴趣圈图片
     */
    public void setCirclephotos(String circlephotos) {
        this.circlephotos = circlephotos == null ? null : circlephotos.trim();
    }

    /**
     * 兴趣圈简介
     * @return circlebrief 兴趣圈简介
     */
    public String getCirclebrief() {
        return circlebrief;
    }

    /**
     * 兴趣圈简介
     * @param circlebrief 兴趣圈简介
     */
    public void setCirclebrief(String circlebrief) {
        this.circlebrief = circlebrief == null ? null : circlebrief.trim();
    }

    /**
     * 兴趣圈参与人数
     * @return circleinvoloed 兴趣圈参与人数
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getCircleinvoloed() {
        return circleinvoloed;
    }

    /**
     * 兴趣圈参与人数
     * @param circleinvoloed 兴趣圈参与人数
     */
    public void setCircleinvoloed(Integer circleinvoloed) {
        this.circleinvoloed = circleinvoloed;
    }

    /**
     * 兴趣圈创建人id
     * @return createuserid 兴趣圈创建人id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getCreateuserid() {
        return createuserid;
    }

    /**
     * 兴趣圈创建人id
     * @param createuserid 兴趣圈创建人id
     */
    public void setCreateuserid(Long createuserid) {
        this.createuserid = createuserid;
    }

    /**
     * 兴趣圈创建时间
     * @return createtime 兴趣圈创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 兴趣圈创建时间
     * @param createtime 兴趣圈创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 兴趣圈更新时间
     * @return updatetime 兴趣圈更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 兴趣圈更新时间
     * @param updatetime 兴趣圈更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 十全十美分类
     * @return ptype 十全十美分类
     */
    @JsonInclude(Include.ALWAYS)
    public String getPtype() {
        return ptype;
    }

    /**
     * 十全十美分类
     * @param ptype 十全十美分类
     */
    public void setPtype(String ptype) {
        this.ptype = ptype == null ? null : ptype.trim();
    }

    @JsonInclude(Include.ALWAYS)
	public Long getCircleid() {
		return circleid;
	}

	public void setCircleid(Long circleid) {
		this.circleid = circleid;
	}

    /**
     * 圈子是否所有人可见
     * @return
     */
    public Boolean getIspublic() {
        return ispublic;
    }

    /**
     * 圈子是否所有人可见
     * @param ispublic
     */
    public void setIspublic(Boolean ispublic) {
        this.ispublic = ispublic;
    }

    /**
     * 加圈子是否需要圈主同意
     * @return
     */
    public Boolean getNeedconfirm() {
        return needconfirm;
    }

    /**
     * 加圈子是否需要圈主同意
     * @return
     */
    public void setNeedconfirm(Boolean needconfirm) {
        this.needconfirm = needconfirm;
    }

    /**
     * 是否同时简历龙信群
     * @return
     */
    public Boolean getCreategoup() {
        return creategoup;
    }

    /**
     * 是否同时简历龙信群
     * @param creategoup
     */
    public void setCreategoup(Boolean creategoup) {
        this.creategoup = creategoup;
    }

    /**
     * 获取公告
     * @return
     */
    public String getNotice() {
        return notice;
    }

    /**
     * 设置公告
     * @param notice
     */
    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     * 设置用户信息
     * @return
     */
    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    /**
     * 获取用户信息
     * @param userInfo
     */
    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}