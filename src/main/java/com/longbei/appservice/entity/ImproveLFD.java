package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 进步点赞，送花，送钻(每一个人只有一条记录)
 *
 * @author luye
 * @create 2017-02-20 上午11:13
 **/
@Document(collection = "improvelfd")
public class ImproveLFD implements Serializable{
    @Id
    private String id;
    private String impid;
    private String userid;
    private String avatar="";//0309号同于总确认  头像不改 所以不存储appuser id信息了
    private String opttype;
    private String businesstype;
    private Long businessid;

    private Date createtime;

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getImpid() {
        return impid;
    }

    public void setImpid(String impid) {
        this.impid = impid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpttype() {
        return opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
