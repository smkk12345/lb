package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UserCheckinDetail {
    private Integer id;

    private Long userid;

    private Date checkindate;//签到日期

    private Date createtime;
    
    private Integer yearmonth; //年月
    
    //---------------扩展字段-------------------
    
    private String isimprove = "0"; //签到是否已发进步状态  isimprove 0:没有发进步   1：已发进步
    
    private String day;  //dd  签到---天

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
    @JsonInclude(Include.ALWAYS)
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    @JsonInclude(Include.ALWAYS)
	public Integer getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(Integer yearmonth) {
		this.yearmonth = yearmonth;
	}

	@JsonInclude(Include.ALWAYS)
	public String getIsimprove() {
		return isimprove;
	}

	public void setIsimprove(String isimprove) {
		this.isimprove = isimprove;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
    
}