package com.longbei.appservice.entity;

import java.util.Date;

public class ClassroomMembers {
    private Integer id;

    private Integer classroomid;//教室id

    private Long userid;//用户id

    private Integer itype;//0—加入教室 1—退出教室

    private String usetstatus;//用户在教室中的身份。0 — 普通学员 1—助教

    private Date createtime;//加入教室时间

    private Date updatetime;//退出教室时间

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
     * 教室id
     * @return classroomid 教室id
     */
    public Integer getClassroomid() {
        return classroomid;
    }

    /**
     * 教室id
     * @param classroomid 教室id
     */
    public void setClassroomid(Integer classroomid) {
        this.classroomid = classroomid;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 0—加入教室 1—退出教室
     * @return itype 0—加入教室 1—退出教室
     */
    public Integer getItype() {
        return itype;
    }

    /**
     * 0—加入教室 1—退出教室
     * @param itype 0—加入教室 1—退出教室
     */
    public void setItype(Integer itype) {
        this.itype = itype;
    }

    /**
     * 用户在教室中的身份。0 — 普通学员 1—助教
     * @return usetstatus 用户在教室中的身份。0 — 普通学员 1—助教
     */
    public String getUsetstatus() {
        return usetstatus;
    }

    /**
     * 用户在教室中的身份。0 — 普通学员 1—助教
     * @param usetstatus 用户在教室中的身份。0 — 普通学员 1—助教
     */
    public void setUsetstatus(String usetstatus) {
        this.usetstatus = usetstatus == null ? null : usetstatus.trim();
    }

    /**
     * 加入教室时间
     * @return createtime 加入教室时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 加入教室时间
     * @param createtime 加入教室时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 退出教室时间
     * @return updatetime 退出教室时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 退出教室时间
     * @param updatetime 退出教室时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}