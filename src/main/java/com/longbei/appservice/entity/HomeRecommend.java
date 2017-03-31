package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class HomeRecommend {
    /**
     * 
     */
    private Integer id;

    /**
     *  0 - 榜单 1 - 教室 2 - 圈子
     */
    private Integer recommendtype;

    /**
     * 榜id 教室id 圈子id
     */
    private Long businessid;

    /**
     * 数据操作使用
     */
    private List<Long> businessids;

    /**
     * 显示用
     */
    private Rank rank;


    /**
     * 排序
     */
    private Integer sort;

    /**
     * 
     */
    private String newTablecol;

    /**
     * 创建日期  推荐日期
     */
    private Date createtime;

    /**
     * 推荐人id
     */
    private String createuserid;

    /**
     * 删除 0 - 未删除。1 - 已删除
     */
    private String isdel;

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public List<Long> getBusinessids() {
        return businessids;
    }

    public void setBusinessids(List<Long> businessids) {
        this.businessids = businessids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecommendtype() {
        return recommendtype;
    }

    public void setRecommendtype(Integer recommendtype) {
        this.recommendtype = recommendtype;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getNewTablecol() {
        return newTablecol;
    }

    public void setNewTablecol(String newTablecol) {
        this.newTablecol = newTablecol == null ? null : newTablecol.trim();
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid == null ? null : createuserid.trim();
    }
}