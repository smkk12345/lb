package com.longbei.appservice.entity;

import java.util.Date;

public class ClassroomCourses {
    private Integer id;

    private String coursetitle;//课程标题

    private Integer coursesort;//课程序号

    private String coursetype;//课程类型

    private String coursebrief;//课程简介

    private String coursephotos;//课程图片

    private String coursecontent;//课程内容

    private Integer classroomid;//所属教室id

    private Long uploaduserid;//上传人id

    private Long createuserid;//创建人id

    private String isdel;//0 — 未删除。1 —删除

    private Date createtime;//创建时间

    private Date udpatetime;//更新时间

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
     * 课程标题
     * @return coursetitle 课程标题
     */
    public String getCoursetitle() {
        return coursetitle;
    }

    /**
     * 课程标题
     * @param coursetitle 课程标题
     */
    public void setCoursetitle(String coursetitle) {
        this.coursetitle = coursetitle == null ? null : coursetitle.trim();
    }

    /**
     * 课程序号
     * @return coursesort 课程序号
     */
    public Integer getCoursesort() {
        return coursesort;
    }

    /**
     * 课程序号
     * @param coursesort 课程序号
     */
    public void setCoursesort(Integer coursesort) {
        this.coursesort = coursesort;
    }

    /**
     * 课程类型
     * @return coursetype 课程类型
     */
    public String getCoursetype() {
        return coursetype;
    }

    /**
     * 课程类型
     * @param coursetype 课程类型
     */
    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype == null ? null : coursetype.trim();
    }

    /**
     * 课程简介
     * @return coursebrief 课程简介
     */
    public String getCoursebrief() {
        return coursebrief;
    }

    /**
     * 课程简介
     * @param coursebrief 课程简介
     */
    public void setCoursebrief(String coursebrief) {
        this.coursebrief = coursebrief == null ? null : coursebrief.trim();
    }

    /**
     * 课程图片
     * @return coursephotos 课程图片
     */
    public String getCoursephotos() {
        return coursephotos;
    }

    /**
     * 课程图片
     * @param coursephotos 课程图片
     */
    public void setCoursephotos(String coursephotos) {
        this.coursephotos = coursephotos == null ? null : coursephotos.trim();
    }

    /**
     * 课程内容
     * @return coursecontent 课程内容
     */
    public String getCoursecontent() {
        return coursecontent;
    }

    /**
     * 课程内容
     * @param coursecontent 课程内容
     */
    public void setCoursecontent(String coursecontent) {
        this.coursecontent = coursecontent == null ? null : coursecontent.trim();
    }

    /**
     * 所属教室id
     * @return classroomid 所属教室id
     */
    public Integer getClassroomid() {
        return classroomid;
    }

    /**
     * 所属教室id
     * @param classroomid 所属教室id
     */
    public void setClassroomid(Integer classroomid) {
        this.classroomid = classroomid;
    }

    /**
     * 上传人id
     * @return uploaduserid 上传人id
     */
    public Long getUploaduserid() {
        return uploaduserid;
    }

    /**
     * 上传人id
     * @param uploaduserid 上传人id
     */
    public void setUploaduserid(Long uploaduserid) {
        this.uploaduserid = uploaduserid;
    }

    /**
     * 创建人id
     * @return createuserid 创建人id
     */
    public Long getCreateuserid() {
        return createuserid;
    }

    /**
     * 创建人id
     * @param createuserid 创建人id
     */
    public void setCreateuserid(Long createuserid) {
        this.createuserid = createuserid;
    }

    /**
     * 0 — 未删除。1 —删除
     * @return isdel 0 — 未删除。1 —删除
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * 0 — 未删除。1 —删除
     * @param isdel 0 — 未删除。1 —删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
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
     * @return udpatetime 更新时间
     */
    public Date getUdpatetime() {
        return udpatetime;
    }

    /**
     * 更新时间
     * @param udpatetime 更新时间
     */
    public void setUdpatetime(Date udpatetime) {
        this.udpatetime = udpatetime;
    }
}