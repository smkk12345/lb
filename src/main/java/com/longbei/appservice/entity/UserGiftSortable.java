package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class UserGiftSortable implements Serializable {
    private Integer id;

    private Long userid;

    private Long improveid;

    private Date updatetime;

    private String nickname;//冗余昵称

    private String username;//冗余手机号

    private String avatar;//冗余头像

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
     * 
     * @return userid 
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 
     * @param userid 
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 
     * @return improveid 
     */
    public Long getImproveid() {
        return improveid;
    }

    /**
     * 
     * @param improveid 
     */
    public void setImproveid(Long improveid) {
        this.improveid = improveid;
    }

    /**
     * 
     * @return updatetime 
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 
     * @param updatetime 
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 冗余昵称
     * @return nickname 冗余昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 冗余昵称
     * @param nickname 冗余昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 冗余手机号
     * @return username 冗余手机号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 冗余手机号
     * @param username 冗余手机号
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 冗余头像
     * @return avatar 冗余头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 冗余头像
     * @param avatar 冗余头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }
}