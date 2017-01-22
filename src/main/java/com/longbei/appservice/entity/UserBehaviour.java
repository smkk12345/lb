package com.longbei.appservice.entity;

import java.util.Date;

public class UserBehaviour {
    private Integer id;

    private Long userid;

    private String otype;//行为类型

    private String otag;//行为标示

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
     * 
     * @return userid 
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 行为类型
     * @return otype 行为类型
     */
    public String getOtype() {
        return otype;
    }

    /**
     * 行为类型
     * @param otype 行为类型
     */
    public void setOtype(String otype) {
        this.otype = otype == null ? null : otype.trim();
    }

    /**
     * 行为标示
     * @return otag 行为标示
     */
    public String getOtag() {
        return otag;
    }

    /**
     * 行为标示
     * @param otag 行为标示
     */
    public void setOtag(String otag) {
        this.otag = otag == null ? null : otag.trim();
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