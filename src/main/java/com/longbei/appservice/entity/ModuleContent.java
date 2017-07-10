package com.longbei.appservice.entity;

import java.util.Date;

public class ModuleContent {
    /**
     * 
     */
    private Integer id;

    /**
     * 专题模块id
     */
    private String semmodid;

    /**
     * 内容id
     */
    private String contentid;

    /**
     * 内容类型 0-文章 1-榜单 2-达人 3-图片 4-视频
     */
    private String contenttype;

    /**
     * 排序
     */
    private Integer sortnum;

    /**
     * 图片
     */
    private String pickey;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 具体内容
     */
    private Rank rank;

    private Article article;

    private UserInfo userInfo;

    private Video video;

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSemmodid() {
        return semmodid;
    }

    public void setSemmodid(String semmodid) {
        this.semmodid = semmodid == null ? null : semmodid.trim();
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid == null ? null : contentid.trim();
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype == null ? null : contenttype.trim();
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public String getPickey() {
        return pickey;
    }

    public void setPickey(String pickey) {
        this.pickey = pickey == null ? null : pickey.trim();
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