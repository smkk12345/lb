package com.longbei.appservice.entity;

import java.util.Date;

public class UserJob {
    private Integer id;

    private Long userid;

    private String companyname;//所在公司

    private String department;//所在部门

    private String location;//工作所在地

    private Date starttime;//工作起始时间

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
     * 所在公司
     * @return companyname 所在公司
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * 所在公司
     * @param companyname 所在公司
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname == null ? null : companyname.trim();
    }

    /**
     * 所在部门
     * @return department 所在部门
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 所在部门
     * @param department 所在部门
     */
    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    /**
     * 工作所在地
     * @return location 工作所在地
     */
    public String getLocation() {
        return location;
    }

    /**
     * 工作所在地
     * @param location 工作所在地
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * 工作起始时间
     * @return starttime 工作起始时间
     */
    public Date getStarttime() {
        return starttime;
    }

    /**
     * 工作起始时间
     * @param starttime 工作起始时间
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