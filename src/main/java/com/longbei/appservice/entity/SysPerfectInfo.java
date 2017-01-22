package com.longbei.appservice.entity;

import java.util.Date;

public class SysPerfectInfo {
    private Integer id;

    private String name;//十全十美  分类名字

    private String photos;//图片

    private String title;//简介标题

    private String brief;//简介内容

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private String ptype;//十全十美类型  0 全部 1学习 等等等

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
     * 十全十美  分类名字
     * @return name 十全十美  分类名字
     */
    public String getName() {
        return name;
    }

    /**
     * 十全十美  分类名字
     * @param name 十全十美  分类名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 图片
     * @return photos 图片
     */
    public String getPhotos() {
        return photos;
    }

    /**
     * 图片
     * @param photos 图片
     */
    public void setPhotos(String photos) {
        this.photos = photos == null ? null : photos.trim();
    }

    /**
     * 简介标题
     * @return title 简介标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 简介标题
     * @param title 简介标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 简介内容
     * @return brief 简介内容
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 简介内容
     * @param brief 简介内容
     */
    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
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
     * 十全十美类型 0 全部 1学习 等等等
     * @return ptype 十全十美类型 0 全部 1学习 等等等
     */
    public String getPtype() {
        return ptype;
    }

    /**
     * 十全十美类型 0 全部 1学习 等等等
     * @param ptype 十全十美类型 0 全部 1学习 等等等
     */
    public void setPtype(String ptype) {
        this.ptype = ptype == null ? null : ptype.trim();
    }
}