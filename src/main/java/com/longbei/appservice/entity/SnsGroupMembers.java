package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class SnsGroupMembers implements Serializable {
    private Integer id;

    private Long groupid;//群id

    private Long userid;//用户id

    private String remark;//备注

    private int status;//状态 0.待处理 1.已入群 2.拒绝入群 4.已不在群中

    private String nickname;//群昵称

    private String avatar;//头像

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Long inviteuserid;//邀请加入群组的用户id

    private Boolean topstatus;//是否置顶

    private Boolean disturbstatus;//是否开启消息免打扰

    private Integer identity;//身份标识/在群中的身份 0-普通 20 — 群主

    private String vcertification;//龙杯官方认证 0无认证 1名人认证 2Star认证

    protected AppUserMongoEntity appUserMongoEntity; //进步用户信息

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
     * 群id
     * @return groupid 群id
     */
    public Long getGroupid() {
        return groupid;
    }

    /**
     * 群id
     * @param groupid 群id
     */
    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    /**
     * 用户id
     * @return userid 用户id
     */
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
     * 备注
     * @return remark 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public int getStatus() {
        return status;
    }

    /**
     * 设置成员在群中的状态
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取群成员昵称
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置群成员昵称
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    /**
     * 获取头像
     * @return
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头像
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getInviteuserid() {
        return inviteuserid;
    }

    public void setInviteuserid(Long inviteuserid) {
        this.inviteuserid = inviteuserid;
    }

    public Boolean getTopstatus() {
        return topstatus;
    }

    public void setTopstatus(Boolean topstatus) {
        this.topstatus = topstatus;
    }

    public Boolean getDisturbstatus() {
        return disturbstatus;
    }

    public void setDisturbstatus(Boolean disturbstatus) {
        this.disturbstatus = disturbstatus;
    }

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public String getVcertification() {
        return vcertification;
    }

    public void setVcertification(String vcertification) {
        this.vcertification = vcertification;
    }
}