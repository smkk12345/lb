package com.longbei.appservice.entity;

import java.util.Date;

public class UserGiftDetail {
    private Integer id;

    private Long userid;

    private String gtype;//类型  0 花 1 钻石

    private Integer number;//数量

    private Long friendid;//送礼物人的id

    private Date createtime;

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
     * 类型  0 花 1 钻石
     * @return gtype 类型  0 花 1 钻石
     */
    public String getGtype() {
        return gtype;
    }

    /**
     * 类型  0 花 1 钻石
     * @param gtype 类型  0 花 1 钻石
     */
    public void setGtype(String gtype) {
        this.gtype = gtype == null ? null : gtype.trim();
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
     * 送礼物人的id
     * @return friendid 送礼物人的id
     */
    public Long getFriendid() {
        return friendid;
    }

    /**
     * 送礼物人的id
     * @param friendid 送礼物人的id
     */
    public void setFriendid(Long friendid) {
        this.friendid = friendid;
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