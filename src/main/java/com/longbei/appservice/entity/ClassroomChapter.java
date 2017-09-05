package com.longbei.appservice.entity;

import java.util.Date;

public class ClassroomChapter {
    private Integer id;

    private Long chapterid;

    private String title;//章节名称

    private Integer sort;//章节顺序


    private Long classroomid;//教室id

    private String isdel;//是否删除


    private Date createtime;//创建时间

    private Date updatetime;//更新时间


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
}