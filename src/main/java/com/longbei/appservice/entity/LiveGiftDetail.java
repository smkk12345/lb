package com.longbei.appservice.entity;

import java.util.Date;

/**
 * Created by lixb on 2017/8/12.
 */
public class LiveGiftDetail {

    private Integer id;

    private Long fromuid;

    private Long touid;

    private Long giftid;

    private Integer num;

    private String gifttitle;

    private Long businessid;

    private String busenesstype;

    private String status;

    private Date createtime;

    private Date updatetime;

    public LiveGiftDetail(Long fromuid,
                          Long touid,
                          Long giftid,
                          String gifttitle,
                          Integer num,
                          Long businessid,
                          String busenesstype){
        this.fromuid = fromuid;
        this.touid = touid;
        this.busenesstype = busenesstype;
        this.giftid = giftid;
        this.gifttitle = gifttitle;
        this.num = num;
        this.businessid = businessid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusenesstype() {
        return busenesstype;
    }

    public void setBusenesstype(String busenesstype) {
        this.busenesstype = busenesstype;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public String getGifttitle() {
        return gifttitle;
    }

    public void setGifttitle(String gifttitle) {
        this.gifttitle = gifttitle;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getGiftid() {
        return giftid;
    }

    public void setGiftid(Long giftid) {
        this.giftid = giftid;
    }

    public Long getTouid() {
        return touid;
    }

    public void setTouid(Long touid) {
        this.touid = touid;
    }

    public Long getFromuid() {
        return fromuid;
    }

    public void setFromuid(Long fromuid) {
        this.fromuid = fromuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
