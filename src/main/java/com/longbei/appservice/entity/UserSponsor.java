package com.longbei.appservice.entity;

import java.util.Date;

public class UserSponsor {
    private Integer id;

    private Long userid;//赞助人

    private Integer number;//数量

    private Long bid;//业务id  

    private String ptype;//赞助类型  0 龙币 1 进步币

    private Date createtime;//时间

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
     * 赞助人
     * @return userid 赞助人
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 赞助人
     * @param userid 赞助人
     */
    public void setUserid(Long userid) {
        this.userid = userid;
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
     * 业务id  
     * @return bid 业务id  
     */
    public Long getBid() {
        return bid;
    }

    /**
     * 业务id  
     * @param bid 业务id  
     */
    public void setBid(Long bid) {
        this.bid = bid;
    }

    /**
     * 赞助类型  0 龙币 1 进步币
     * @return ptype 赞助类型  0 龙币 1 进步币
     */
    public String getPtype() {
        return ptype;
    }

    /**
     * 赞助类型  0 龙币 1 进步币
     * @param ptype 赞助类型  0 龙币 1 进步币
     */
    public void setPtype(String ptype) {
        this.ptype = ptype == null ? null : ptype.trim();
    }

    /**
     * 时间
     * @return createtime 时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 时间
     * @param createtime 时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}