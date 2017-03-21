package com.longbei.appservice.entity;

import java.util.Date;

public class UserFlowerDetail {
    private Integer pid;

    private Long id;//编码

    private Long userid;//用户id

    private Long improveid;//如果origin为0，那么此字段非空

    private Long friendid;//如果origin为1或2，那么此字段非空

    private Integer number;//鲜花数量

    private String ftype;//0:花朵;1:花束;2:花篮

    private String origin;//来源，0:购买;1:捐赠;2:被赠与;3:增值

    private Date drawdate;//获得时间

    /**
     * 
     * @return pid 
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 
     * @param pid 
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 编码
     * @return id 编码
     */
    public Long getId() {
        return id;
    }

    /**
     * 编码
     * @param id 编码
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 如果origin为0，那么此字段非空
     * @return improveid 如果origin为0，那么此字段非空
     */
    public Long getImproveid() {
        return improveid;
    }

    /**
     * 如果origin为0，那么此字段非空
     * @param improveid 如果origin为0，那么此字段非空
     */
    public void setImproveid(Long improveid) {
        this.improveid = improveid;
    }

    /**
     * 如果origin为1或2，那么此字段非空
     * @return friendid 如果origin为1或2，那么此字段非空
     */
    public Long getFriendid() {
        return friendid;
    }

    /**
     * 如果origin为1或2，那么此字段非空
     * @param friendid 如果origin为1或2，那么此字段非空
     */
    public void setFriendid(Long friendid) {
        this.friendid = friendid;
    }

    /**
     * 鲜花数量
     * @return number 鲜花数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 鲜花数量
     * @param number 鲜花数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 0:花朵;1:花束;2:花篮
     * @return ftype 0:花朵;1:花束;2:花篮
     */
    public String getFtype() {
        return ftype;
    }

    /**
     * 0:花朵;1:花束;2:花篮
     * @param ftype 0:花朵;1:花束;2:花篮
     */
    public void setFtype(String ftype) {
        this.ftype = ftype == null ? null : ftype.trim();
    }

    /**
     * 来源，0:购买;1:捐赠;2:被赠与;3:增值
     * @return origin 来源，0:购买;1:捐赠;2:被赠与;3:增值
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 来源，0:购买;1:捐赠;2:被赠与;3:增值
     * @param origin 来源，0:购买;1:捐赠;2:被赠与;3:增值
     */
    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
    }

    /**
     * 获得时间
     * @return drawdate 获得时间
     */
    public Date getDrawdate() {
        return drawdate;
    }

    /**
     * 获得时间
     * @param drawdate 获得时间
     */
    public void setDrawdate(Date drawdate) {
        this.drawdate = drawdate;
    }
}