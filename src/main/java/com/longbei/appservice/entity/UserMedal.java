package com.longbei.appservice.entity;

import java.util.Date;

public class UserMedal {
    private Integer id;

    private Long userid;

    private Long medalid;//荣誉勋章id  

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
     * 荣誉勋章id  
     * @return medalid 荣誉勋章id  
     */
    public Long getMedalid() {
        return medalid;
    }

    /**
     * 荣誉勋章id  
     * @param medalid 荣誉勋章id  
     */
    public void setMedalid(Long medalid) {
        this.medalid = medalid;
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