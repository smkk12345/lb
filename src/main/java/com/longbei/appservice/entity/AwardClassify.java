package com.longbei.appservice.entity;

import java.util.Date;

public class AwardClassify {
    /**
     * 
     */
    private Integer id;

    /**
     * 类别名字
     */
    private String classifynamev;

    /**
     * 父级id
     */
    private Integer parentid;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 
     */
    private Date updatetime;

    /**
     * 
     */
    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassifynamev() {
        return classifynamev;
    }

    public void setClassifynamev(String classifynamev) {
        this.classifynamev = classifynamev == null ? null : classifynamev.trim();
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}