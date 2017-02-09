package com.longbei.appservice.entity;

import java.util.Date;

/**
 * 用户十全十美积分明细表
 */
public class UserPointDetail {

    private Long id;//编码

    private Long userid;//用户id

    private Integer point;//积分

    private Date drawdate;//获得时间

    private String ptype;//十全十美id

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
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
     * 积分
     * @return point 积分
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * 积分
     * @param point 积分
     */
    public void setPoint(Integer point) {
        this.point = point;
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