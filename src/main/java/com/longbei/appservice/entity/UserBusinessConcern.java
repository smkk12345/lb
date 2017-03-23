package com.longbei.appservice.entity;

import java.util.Date;

/**
 * 关注相关
 * Created by wangyongzhi 17/3/21.
 */
public class UserBusinessConcern {
    private Long id;
    private Long userid;//用户id
    private Integer businesstype;//关注的类型 1 目标 2 榜单 3 圈子 4 教室
    private Long businessid;//关注的类型id
    private Date createtime;//创建时间

    public UserBusinessConcern(){}
    public UserBusinessConcern(Long userid,Integer businesstype,Long businessid){
        super();
        this.userid = userid;
        this.businesstype = businesstype;
        this.businessid = businessid;
        this.createtime = new Date();
    }

    @Override
    public String toString() {
        return "UserBusinessConcern{" +
                "id=" + id +
                ", userid=" + userid +
                ", businesstype=" + businesstype +
                ", businessid=" + businessid +
                ", createtime=" + createtime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(Integer businesstype) {
        this.businesstype = businesstype;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}
