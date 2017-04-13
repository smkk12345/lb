package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserCheckinInfo {
    private Integer id;

    private Long userid;

    private Integer continuedays;//持续时间

    private Date starttime;//持续开始时间

    private Date endtime;//持续结束时间

    private Date createtime;

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
     * 持续时间
     * @return continuedays 持续时间
     */
    public Integer getContinuedays() {
        return continuedays;
    }

    /**
     * 持续时间
     * @param continuedays 持续时间
     */
    public void setContinuedays(Integer continuedays) {
        this.continuedays = continuedays;
    }

    /**
     * 持续开始时间
     * @return starttime 持续开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 持续开始时间
     * @param starttime 持续开始时间
     */
    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    /**
     * 持续结束时间
     * @return endtime 持续结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 持续结束时间
     * @param endtime 持续结束时间
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 
     * @return createtime 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
}