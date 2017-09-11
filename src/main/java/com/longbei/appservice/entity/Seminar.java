package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Seminar implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 专题id
     */
    private Long seminarid;

    /**
     * 专题封面图
     */
    private String seminarpic;

    /**
     * 专题标题
     */
    private String seminartitle;

    /**
     * 分享简介
     */
    private String sharebrief;

    /**
     * 专题标签
     */
    private String seminartarget;

    /**
     * 创建人id
     */
    private String createuserid;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    /**
     * css文件
     */
    private String cssfile;

    /**
     * 专题所关联模块
     */
    private List<SeminarModule> seminarModules;

    /**
     * 浏览量
     */
    private Long pageview;

    public String getCssfile() {
        return cssfile;
    }

    public void setCssfile(String cssfile) {
        this.cssfile = cssfile;
    }

    public Long getPageview() {
        return pageview;
    }

    public void setPageview(Long pageview) {
        this.pageview = pageview;
    }

    public List<SeminarModule> getSeminarModules() {
        return seminarModules;
    }

    public void setSeminarModules(List<SeminarModule> seminarModules) {
        this.seminarModules = seminarModules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSeminarid() {
        return seminarid;
    }

    public void setSeminarid(Long seminarid) {
        this.seminarid = seminarid;
    }

    public String getSeminarpic() {
        return seminarpic;
    }

    public void setSeminarpic(String seminarpic) {
        this.seminarpic = seminarpic == null ? null : seminarpic.trim();
    }

    public String getSeminartitle() {
        return seminartitle;
    }

    public void setSeminartitle(String seminartitle) {
        this.seminartitle = seminartitle == null ? null : seminartitle.trim();
    }

    public String getSharebrief() {
        return sharebrief;
    }

    public void setSharebrief(String sharebrief) {
        this.sharebrief = sharebrief == null ? null : sharebrief.trim();
    }

    public String getSeminartarget() {
        return seminartarget;
    }

    public void setSeminartarget(String seminartarget) {
        this.seminartarget = seminartarget == null ? null : seminartarget.trim();
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid == null ? null : createuserid.trim();
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}