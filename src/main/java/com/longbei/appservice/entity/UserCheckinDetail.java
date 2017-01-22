package com.longbei.appservice.entity;

import java.util.Date;

public class UserCheckinDetail {
    private Integer id;

    private Long userid;

    private Date checkindate;//签到日期

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
     * 签到日期
     * @return checkindate 签到日期
     */
    public Date getCheckindate() {
        return checkindate;
    }

    /**
     * 签到日期
     * @param checkindate 签到日期
     */
    public void setCheckindate(Date checkindate) {
        this.checkindate = checkindate;
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
}