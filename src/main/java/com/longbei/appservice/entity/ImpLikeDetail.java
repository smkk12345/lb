package com.longbei.appservice.entity;

import java.util.Date;

public class ImpLikeDetail {
    private Integer id;

    private Long userid;//用户id

    private Long impid;//微进步id

    private String status;//点赞，还是已经取消

    private Integer times;//点击次数

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private String gtype;//0 零散 1 目标中 2 榜中 3 圈中 4教室中

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
     * 微进步id
     * @return impid 微进步id
     */
    public Long getImpid() {
        return impid;
    }

    /**
     * 微进步id
     * @param impid 微进步id
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 点赞，还是已经取消
     * @return status 点赞，还是已经取消
     */
    public String getStatus() {
        return status;
    }

    /**
     * 点赞，还是已经取消
     * @param status 点赞，还是已经取消
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 点击次数
     * @return times 点击次数
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * 点击次数
     * @param times 点击次数
     */
    public void setTimes(Integer times) {
        this.times = times;
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
     * 更新时间
     * @return updatetime 更新时间
     */
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

    /**
     * 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     * @return gtype 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     */
    public String getGtype() {
        return gtype;
    }

    /**
     * 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     * @param gtype 0 零散 1 目标中 2 榜中 3 圈中 4教室中
     */
    public void setGtype(String gtype) {
        this.gtype = gtype == null ? null : gtype.trim();
    }
}