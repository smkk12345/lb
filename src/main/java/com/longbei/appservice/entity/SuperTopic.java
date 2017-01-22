package com.longbei.appservice.entity;

import java.util.Date;

public class SuperTopic {
    private Integer id;

    private String photos;//图片key

    private String title;//话题标题

    private String content;//小编内容

    private String isdel;//0 未删除  1 假删

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Integer sort;//排序

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
     * 图片key
     * @return photos 图片key
     */
    public String getPhotos() {
        return photos;
    }

    /**
     * 图片key
     * @param photos 图片key
     */
    public void setPhotos(String photos) {
        this.photos = photos == null ? null : photos.trim();
    }

    /**
     * 话题标题
     * @return title 话题标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 话题标题
     * @param title 话题标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 小编内容
     * @return content 小编内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 小编内容
     * @param content 小编内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 0 未删除  1 假删
     * @return isdel 0 未删除  1 假删
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * 0 未删除  1 假删
     * @param isdel 0 未删除  1 假删
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

    /**
     * 排序
     * @return sort 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 排序
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}