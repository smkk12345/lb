package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by wangyongzhi 17/6/28.
 */
public class Video {

    private Integer id;
    private Date createtime;
    private String title;//视频标题
    private String brief;//视频 简介
    private String screenpthoto;//视频 截图
    private String videourl;//视频地址
    private Boolean isshow;//是否前台 显示
    private Integer sortnum;//排序
    private Integer likes;//点赞数量
    private Integer videoclassifyid;//所属视频分类id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public String getScreenpthoto() {
        return screenpthoto;
    }

    public void setScreenpthoto(String screenpthoto) {
        this.screenpthoto = screenpthoto;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public Boolean getIsshow() {
        return isshow;
    }

    public void setIsshow(Boolean isshow) {
        this.isshow = isshow;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Integer getVideoclassifyid() {
        return videoclassifyid;
    }

    public void setVideoclassifyid(Integer videoclassifyid) {
        this.videoclassifyid = videoclassifyid;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
