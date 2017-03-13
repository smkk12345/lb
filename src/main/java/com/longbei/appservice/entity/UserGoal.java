package com.longbei.appservice.entity;

import java.util.Date;

public class UserGoal {
    private Integer id;

    private Long userid;

    private String goaltag;//目标关键字

    private Date createtime;

    private String isdel;//是否删除  0 未删除 1 删除

    private Date updatetime;

    private String ispublic;

    private String ptype;

    private String needwarn;

    private String warntime;

    private String week;

    private Integer icount;//进步更新条数

    public void setIcount(Integer icount) {
        this.icount = icount;
    }

    public Integer getIcount() {
        return icount;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getNeedwarn() {
        return needwarn;
    }

    public void setNeedwarn(String needwarn) {
        this.needwarn = needwarn;
    }

    public String getWarntime() {
        return warntime;
    }

    public void setWarntime(String warntime) {
        this.warntime = warntime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getIspublic() {
        return ispublic;
    }

    public void setIspublic(String ispublic) {
        this.ispublic = ispublic;
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
     * 
     * @return userid 
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 目标关键字
     * @return goaltag 目标关键字
     */
    public String getGoaltag() {
        return goaltag;
    }

    /**
     * 目标关键字
     * @param goaltag 目标关键字
     */
    public void setGoaltag(String goaltag) {
        this.goaltag = goaltag == null ? null : goaltag.trim();
    }

    /**
     * 
     * @return createtime 
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 是否删除  0 未删除 1 删除
     * @return isdel 是否删除  0 未删除 1 删除
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * 是否删除  0 未删除 1 删除
     * @param isdel 是否删除  0 未删除 1 删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

    /**
     * 
     * @return updatetime 
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}