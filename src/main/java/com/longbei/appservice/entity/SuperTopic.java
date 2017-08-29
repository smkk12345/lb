package com.longbei.appservice.entity;

import java.io.Serializable;

public class SuperTopic implements Serializable {
    private Integer id;

    private Long topicid;//话题id

    private String photos;//图片key

    private String title;//话题标题

    private String content;//小编内容

    private String isdel;//是否删除 0未删除  1假删

    private String createtime;//创建时间

    private String updatetime;//更新时间

    private Integer sort;//排序

    private String ishot;//是否热点 0否 1是

    private String uptime;//上线时间

    private String downtime;//下线时间

    private Integer impcount;//微进步数量

    private Integer scancount;//浏览量

    private String isup;//上下线  0下线 1上线

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
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * 更新时间
     * @return updatetime 更新时间
     */
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(String updatetime) {
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

    public Long getTopicid() {
        return topicid;
    }

    public void setTopicid(Long topicid) {
        this.topicid = topicid;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDowntime() {
        return downtime;
    }

    public void setDowntime(String downtime) {
        this.downtime = downtime;
    }

    public Integer getImpcount() {
        return impcount;
    }

    public void setImpcount(Integer impcount) {
        this.impcount = impcount;
    }

    public Integer getScancount() {
        return scancount;
    }

    public void setScancount(Integer scancount) {
        this.scancount = scancount;
    }

    public String getIsup() {
        return isup;
    }

    public void setIsup(String isup) {
        this.isup = isup;
    }
}