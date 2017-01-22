package com.longbei.appservice.entity;

import java.util.Date;

public class UserInterests {
    private Integer id;

    private String userid;

    private String perfectid;

    private String perfectname;//十全十美  名

    private Date createtime;

    private Date updatetime;

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
    public String getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 
     * @return perfectid 
     */
    public String getPerfectid() {
        return perfectid;
    }

    /**
     * 
     * @param perfectid 
     */
    public void setPerfectid(String perfectid) {
        this.perfectid = perfectid == null ? null : perfectid.trim();
    }

    /**
     * 十全十美  名
     * @return perfectname 十全十美  名
     */
    public String getPerfectname() {
        return perfectname;
    }

    /**
     * 十全十美  名
     * @param perfectname 十全十美  名
     */
    public void setPerfectname(String perfectname) {
        this.perfectname = perfectname == null ? null : perfectname.trim();
    }

    /**
     * 
     * @return createtime 
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 
     * @return updatetime 
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}