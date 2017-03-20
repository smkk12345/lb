package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Award {
    private Integer id;

    private String awardtitle;//奖品标题

    private String awardbrief;//奖品简介

    private Integer awardcateid;//奖品类别id

    private Double awardprice;//奖品价值

    private Integer awardlevel;//奖品对应等级

    private String awardphotos;//奖品图片

    private String awardtype;//奖品类型。0 — 实物。1—虚拟

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private AwardClassify awardClassify; //奖品分类信息 0 - 进步币 1 - 红包 3 - 实物

    private Integer awardcount;//有多少人获奖

    private Long userid;

    public AwardClassify getAwardClassify() {
        return awardClassify;
    }

    public void setAwardClassify(AwardClassify awardClassify) {
        this.awardClassify = awardClassify;
    }

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
     * 奖品标题
     * @return awardtitle 奖品标题
     */
    public String getAwardtitle() {
        return awardtitle;
    }

    /**
     * 奖品标题
     * @param awardtitle 奖品标题
     */
    public void setAwardtitle(String awardtitle) {
        this.awardtitle = awardtitle == null ? null : awardtitle.trim();
    }

    /**
     * 奖品简介
     * @return awardbrief 奖品简介
     */
    public String getAwardbrief() {
        return awardbrief;
    }

    /**
     * 奖品简介
     * @param awardbrief 奖品简介
     */
    public void setAwardbrief(String awardbrief) {
        this.awardbrief = awardbrief == null ? null : awardbrief.trim();
    }

    /**
     * 奖品类别id
     * @return awardcateid 奖品类别id
     */
    public Integer getAwardcateid() {
        return awardcateid;
    }

    /**
     * 奖品类别id
     * @param awardcateid 奖品类别id
     */
    public void setAwardcateid(Integer awardcateid) {
        this.awardcateid = awardcateid;
    }

    /**
     * 奖品价值
     * @return awardprice 奖品价值
     */
    public Double getAwardprice() {
        return awardprice;
    }

    /**
     * 奖品价值
     * @param awardprice 奖品价值
     */
    public void setAwardprice(Double awardprice) {
        this.awardprice = awardprice;
    }

    /**
     * 奖品对应等级
     * @return awardlevel 奖品对应等级
     */
    public Integer getAwardlevel() {
        return awardlevel;
    }

    /**
     * 奖品对应等级
     * @param awardlevel 奖品对应等级
     */
    public void setAwardlevel(Integer awardlevel) {
        this.awardlevel = awardlevel;
    }

    /**
     * 奖品图片
     * @return awardphotos 奖品图片
     */
    public String getAwardphotos() {
        return awardphotos;
    }

    /**
     * 奖品图片
     * @param awardphotos 奖品图片
     */
    public void setAwardphotos(String awardphotos) {
        this.awardphotos = awardphotos == null ? null : awardphotos.trim();
    }

    /**
     * 奖品类型。0 — 实物。1—虚拟
     * @return awardtype 奖品类型。0 — 实物。1—虚拟
     */
    public String getAwardtype() {
        return awardtype;
    }

    /**
     * 奖品类型。0 — 实物。1—虚拟
     * @param awardtype 奖品类型。0 — 实物。1—虚拟
     */
    public void setAwardtype(String awardtype) {
        this.awardtype = awardtype == null ? null : awardtype.trim();
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}