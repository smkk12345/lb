package com.longbei.appservice.entity;

import java.util.Date;

public class UserSchool {
    private Integer id;

    private Long userid;

    private String schoolname;//学校名称

    private String department;//院系

    private Date starttime;

    private Date endtime;

    private Date creatime;

    private String updatetime;

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
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 
     * @param starttime 
     */
    public void setStarttime(Date starttime) {
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
    public Date getCreatime() {
        return creatime;
    }

    /**
     * 
     * @param creatime 
     */
    public void setCreatime(Date creatime) {
        this.creatime = creatime;
    }

    /**
     * 
     * @return updatetime 
     */
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }
}