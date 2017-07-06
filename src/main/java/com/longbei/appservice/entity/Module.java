package com.longbei.appservice.entity;

import java.util.Date;

public class Module{



    /**
     * 
     */
    private Integer id;

    /**
     * 模块id
     */
    private String moduleid;

    /**
     * 模块名字
     */
    private String modulename;

    /**
     * 模块预览图
     */
    private String modulepic;

    /**
     * 排序
     */
    private Integer sortnum;

    /**
     * 
     */
    private String remarker;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    public String getModulepic() {
        return modulepic;
    }

    public void setModulepic(String modulepic) {
        this.modulepic = modulepic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid == null ? null : moduleid.trim();
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename == null ? null : modulename.trim();
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getRemarker() {
        return remarker;
    }

    public void setRemarker(String remarker) {
        this.remarker = remarker == null ? null : remarker.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}