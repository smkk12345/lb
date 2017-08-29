package com.longbei.appservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 进步点赞，送花，送钻 简略信息（mongo）
 *
 * @author luye
 * @create 2017-02-21 上午11:06
 **/
@Document(collection = "improveLFDDetail")
public class ImproveLFDDetail implements Serializable {

    @Id
    private String id;
    private String impid;
    private String userid;
    private String opttype;
    private Date createtime;
    private String businessid;
    private String businesstype;

    @DBRef
    private AppUserMongoEntity appUser;

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public AppUserMongoEntity getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserMongoEntity appUser) {
        this.appUser = appUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
