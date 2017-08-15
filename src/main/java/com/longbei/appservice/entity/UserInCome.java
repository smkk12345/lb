package com.longbei.appservice.entity;

import java.util.Date;

public class UserInCome {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id，当前教室受益人id，也是当前老师uid
     */
    private Long userid;

    /**
     * 总收益
     */
    private Integer total;

    /**
     * 总支出
     */
    private Integer outgo;

    /**
     * 
     */
    private Integer left;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 创建时间
     */
    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOutgo() {
        return outgo;
    }

    public void setOutgo(Integer outgo) {
        this.outgo = outgo;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}