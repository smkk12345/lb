package com.longbei.appservice.entity;

import java.util.Date;

public class UserGoal {
    private Integer id;

    private Long userid;

    private String goaltag;//目标关键字

    private Date createtime;

    private String isdel;//是否删除  0 未删除 1 删除

    private Date updatetime;

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