package com.longbei.appservice.entity;

import java.util.Date;
import java.util.List;

public class SeminarModule{
    /**
     * 
     */
    private Integer id;

    /**
     * 专题模块id
     */
    private Long semmodid;

    /**
     * 专题id
     */
    private Long seminarid;

    /**
     * 模块id
     */
    private String moduleid;

    /**
     * 模块名字
     */
    private String modulename;

    /**
     * 是否显示 0 - 不显示 1 - 显示
     */
    private String isshow;

    /**
     * 排序
     */
    private Integer sortnum;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;


    /**
     * 后端配置
     */
    private String config;



    /**
     * 模块所关联内容
     */
    private List<ModuleContent> moduleContents;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<ModuleContent> getModuleContents() {
        return moduleContents;
    }

    public void setModuleContents(List<ModuleContent> moduleContents) {
        this.moduleContents = moduleContents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSemmodid() {
        return semmodid;
    }

    public void setSemmodid(Long semmodid) {
        this.semmodid = semmodid;
    }

    public Long getSeminarid() {
        return seminarid;
    }

    public void setSeminarid(Long seminarid) {
        this.seminarid = seminarid;
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

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow == null ? null : isshow.trim();
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
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