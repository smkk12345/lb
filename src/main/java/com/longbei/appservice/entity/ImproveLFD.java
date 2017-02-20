package com.longbei.appservice.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 进步点赞，送花，送钻
 *
 * @author luye
 * @create 2017-02-20 上午11:13
 **/
@Document(collection = "improvelfd")
public class ImproveLFD implements Serializable{

    private String impid;
    private String userid;
    private String opttype;
    private Date createtime;

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
