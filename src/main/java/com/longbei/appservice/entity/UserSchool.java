package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class UserSchool implements Serializable {
    private Integer id;

    private Long userid;

    private String schoolname;//学校名称

    private String department;//院系

    private String starttime;

    private Date endtime;

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
     * 学校名称
     * @return schoolname 学校名称
     */
    public String getSchoolname() {
        return schoolname;
    }

    /**
     * 学校名称
     * @param schoolname 学校名称
     */
    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname == null ? null : schoolname.trim();
    }

    /**
     * 院系
     * @return Department 院系
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 院系
     * @param department 院系
     */
    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    /**
     * 
     * @return starttime 
     */
    public String getStarttime() {
        return starttime;
    }

    /**
     * 
     * @param starttime 
     */
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    /**
     * 
     * @return endtime 
     */
    public Date getEndtime() {
        return endtime;
    }

    /**
     * 
     * @param endtime 
     */
    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    /**
     * 
     * @return creatime 
     */
    public Date getCreatetime() {
		return createtime;
	}

    /**
     * 
     * @param creatime 
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
//    public void setUpdatetime(Date updatetime) {
//        this.updatetime = updatetime == null ? null : updatetime.trim();
//    }
    
    public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}


}