package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AwardClassify {
    /**
     * 
     */
    private Integer id;

    /**
     * 类别名字
     */
    private String classifyname;

    /**
     * 是否删除。0 - 否。1 - 是
     */
    private Integer isdel;

    /**
     * 父级id
     */
    private Integer parentid;

    /**
     * 排序
     */
    private Integer sort;

    private Integer classifytype; //0 - 进步币 1 - 红包 3 - 实物

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

    public String getClassifyname() {
        return classifyname;
    }

    public void setClassifyname(String classifyname) {
        this.classifyname = classifyname == null ? null : classifyname.trim();
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

    public Integer getClassifytype() {
        return classifytype;
    }

    public void setClassifytype(Integer classifytype) {
        this.classifytype = classifytype;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}