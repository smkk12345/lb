package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ClassroomChapter implements Serializable {
    private Integer id;

    private Long chapterid;

    private String title;//章节名称

    private Integer sort;//章节顺序

    private Long classroomid;//教室id

    private String isdel;//isdel 是否删除  0：未删除   1：已删除
    
    private String display; //display 0 显示 1 隐藏

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private List<ClassroomCourses> coursesList;//课程列表
    
    public ClassroomChapter(){
    	super();
    }


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
     * @return chapterid 
     */
    @JsonInclude(Include.ALWAYS)
    public Long getChapterid() {
        return chapterid;
    }

    /**
     * 
     * @param chapterid 
     */
    public void setChapterid(Long chapterid) {
        this.chapterid = chapterid;
    }

    /**
     * 章节名称
     * @return title 章节名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 章节名称
     * @param title 章节名称
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 章节顺序 
     * @return sort 章节顺序 
     */
    @JsonInclude(Include.ALWAYS)
    public Integer getSort() {
        return sort;
    }

    /**
     * 章节顺序 
     * @param sort 章节顺序 
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 教室id
     * @return classroomid 教室id
     */
    @JsonInclude(Include.ALWAYS)
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

    /**
     * 是否删除 
     * @return isdel 是否删除 
     */
    @JsonInclude(Include.ALWAYS)
    public String getIsdel() {
        return isdel;
    }

    /**
     * 是否删除 
     * @param isdel 是否删除 
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }

    /**
     * 创建时间
     * @return createtime 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    @JsonInclude(Include.ALWAYS)
	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

    public void setCoursesList(List<ClassroomCourses> coursesList) {
        this.coursesList = coursesList;
    }

    public List<ClassroomCourses> getCoursesList() {
        return coursesList;
    }
}