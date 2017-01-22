package com.longbei.appservice.entity;

import java.util.Date;

public class Circle {
    private Integer id;

    private String circletitle;//兴趣圈标题

    private String circlephotos;//兴趣圈图片

    private Integer circlecateid;//兴趣圈类别id

    private String circlebrief;//兴趣圈简介

    private Integer circleinvoloed;//兴趣圈参与人数

    private Long createuserid;//兴趣圈创建人id

    private Date createtime;//兴趣圈创建时间

    private Date updatetime;//兴趣圈更新时间

    private String ptype;//十全十美分类

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
     * 兴趣圈类别id
     * @return circlecateid 兴趣圈类别id
     */
    public Integer getCirclecateid() {
        return circlecateid;
    }

    /**
     * 兴趣圈类别id
     * @param circlecateid 兴趣圈类别id
     */
    public void setCirclecateid(Integer circlecateid) {
        this.circlecateid = circlecateid;
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
}