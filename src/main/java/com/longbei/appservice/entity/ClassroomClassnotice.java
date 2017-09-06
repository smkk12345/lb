package com.longbei.appservice.entity;

import java.util.Date;

public class ClassroomClassnotice {
    private Integer id;

    private String classnotice;//公告内容

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Long classroomid;//教室id

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
     * 公告内容
     * @return classnotice 公告内容
     */
    public String getClassnotice() {
        return classnotice;
    }

    /**
     * 公告内容
     * @param classnotice 公告内容
     */
    public void setClassnotice(String classnotice) {
        this.classnotice = classnotice == null ? null : classnotice.trim();
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 教室id
     * @return classroomid 教室id
     */
    public Long getClassroomid() {
        return classroomid;
    }

    /**
     * 教室id
     * @param classroomid 教室id
     */
    public void setClassroomid(Long classroomid) {
        this.classroomid = classroomid;
    }
}