package com.longbei.appservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

public class UserCard implements Serializable {
    private Integer id;

    private Long userid;//用户id

    private String ctype;//类型。0 榜主名片。1 圈主名片  2 教师名片

    private String avatar;//头像

    private String displayname;//称呼

    private String brief;

    private String content;//图文介绍

    private String createtime;

    private String updatetime;

    private Long cardid;//名片id
    
    private String isdel; //0 没删 1 - 删除   默认0

    private String sourcetype; //0:运营  1:app  2:商户

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
     * 用户id
     * @return userid 用户id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 类型。0 榜主名片。1 圈主名片
     * @return ctype 类型。0 榜主名片。1 圈主名片
     */
    @JsonInclude(Include.ALWAYS)
    public String getCtype() {
        return ctype;
    }

    /**
     * 类型。0 榜主名片。1 圈主名片  2 教师名片
     * @param ctype 类型。0 榜主名片。1 圈主名片  2 教师名片
     */
    public void setCtype(String ctype) {
        this.ctype = ctype == null ? null : ctype.trim();
    }

    /**
     * 头像
     * @return avatar 头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 头像
     * @param avatar 头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * 称呼
     * @return displayname 称呼
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * 称呼
     * @param displayname 称呼
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname == null ? null : displayname.trim();
    }

    /**
     * 
     * @return brief 
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 
     * @param brief 
     */
    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    /**
     * 图文介绍
     * @return content 图文介绍
     */
    public String getContent() {
        return content;
    }

    /**
     * 图文介绍
     * @param content 图文介绍
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 
     * @return createtime 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * 
     * @return updatetime 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 名片id
     * @return cardid 名片id
     */
    @JsonInclude(Include.ALWAYS)
    public Long getCardid() {
        return cardid;
    }

    /**
     * 名片id
     * @param cardid 名片id
     */
    public void setCardid(Long cardid) {
        this.cardid = cardid;
    }

    @JsonInclude(Include.ALWAYS)
	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }
}