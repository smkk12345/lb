package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class SnsAcquaintances implements Serializable {
    private Integer id;

    private Long userid;

    private String mobile;//手机号

    private String hasregister;//0 未注册龙杯  1 已经注册龙杯

    private String isfriend;//0 不是好友 1 是好友

    private String nickname;//熟人昵称

    private Date createtime;//创建时间

    private String updatetime;//更新时间

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
     * 手机号
     * @return mobile 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 手机号
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 0 未注册龙杯  1 已经注册龙杯
     * @return hasregister 0 未注册龙杯  1 已经注册龙杯
     */
    public String getHasregister() {
        return hasregister;
    }

    /**
     * 0 未注册龙杯  1 已经注册龙杯
     * @param hasregister 0 未注册龙杯  1 已经注册龙杯
     */
    public void setHasregister(String hasregister) {
        this.hasregister = hasregister == null ? null : hasregister.trim();
    }

    /**
     * 0 不是好友 1 是好友
     * @return isfriend 0 不是好友 1 是好友
     */
    public String getIsfriend() {
        return isfriend;
    }

    /**
     * 0 不是好友 1 是好友
     * @param isfriend 0 不是好友 1 是好友
     */
    public void setIsfriend(String isfriend) {
        this.isfriend = isfriend == null ? null : isfriend.trim();
    }

    /**
     * 熟人昵称
     * @return nickname 熟人昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 熟人昵称
     * @param nickname 熟人昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
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
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }
}