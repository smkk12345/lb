package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PubMessage {
    /**
     * 
     */
    private Integer id;

    /**
     * 消息标题
     */
    private String msgtitle = "龙杯";

    /**
     * 关联内容id
     */
    private Long targetid;

    /**
     * 消息内容
     */
    private String msgcontent;

    /**
     * 消息关联内容。0 - 没有 1 - 文章 2 - 专题
     */
    private String msgtarget;

    /**
     * 消息状态。0 - 待发送 1 - 定时发送 2 - 发送中 3 - 已发送 4 - 发送失败
     */
    private String msgstatus;

    /**
     * 创建人id
     */
    private String createuserid;

    /**
     * 是否删除 0 - 未删除 1 - 已删除
     */
    private String isdel;

    /**
     * 发布时间
     */
    private Date publishtime;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 消息关联内容
     */
    private Article article;
    private Seminar seminar;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Seminar getSeminar() {
        return seminar;
    }

    public void setSeminar(Seminar seminar) {
        this.seminar = seminar;
    }

    public Long getTargetid() {
        return targetid;
    }

    public void setTargetid(Long targetid) {
        this.targetid = targetid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgtitle() {
        return msgtitle;
    }

    public void setMsgtitle(String msgtitle) {
        this.msgtitle = msgtitle == null ? null : msgtitle.trim();
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent == null ? null : msgcontent.trim();
    }

    public String getMsgtarget() {
        return msgtarget;
    }

    public void setMsgtarget(String msgtarget) {
        this.msgtarget = msgtarget == null ? null : msgtarget.trim();
    }

    public String getMsgstatus() {
        return msgstatus;
    }

    public void setMsgstatus(String msgstatus) {
        this.msgstatus = msgstatus == null ? null : msgstatus.trim();
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid == null ? null : createuserid.trim();
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel == null ? null : isdel.trim();
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}