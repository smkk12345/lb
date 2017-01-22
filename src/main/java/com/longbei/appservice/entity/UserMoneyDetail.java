package com.longbei.appservice.entity;

import java.util.Date;

public class UserMoneyDetail {
    private Integer id;

    private Long userid;

    private String origin;//来源

    private Integer number;//数量

    private Long friendid;

    private Date createtime;//创建时间

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
     * 来源
     * @return origin 来源
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 来源
     * @param origin 来源
     */
    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    /**
     * 数量
     * @return number 数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 数量
     * @param number 数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 
     * @return friendid 
     */
    public Long getFriendid() {
        return friendid;
    }

    /**
     * 
     * @param friendid 
     */
    public void setFriendid(Long friendid) {
        this.friendid = friendid;
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
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