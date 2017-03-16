package com.longbei.appservice.entity;

import java.util.Date;

public class SnsGroup {

    public static final Integer groupIdLength = 7;
    public static final String groupIdSet = "Sns_Group_Id_Set";

    public static Integer maxNum = 1000;//一个群组中最大的成员数量

    private Integer id;

    private Long groupid;//群id

    private String groupname;//群名字

    private Integer grouptype;//群类型

    private Long relateid;//关联id（榜，圈子，教室的id）

    private Date createtime;//ch

    private Date updatetime;

    private boolean needconfirm;//加群是否需要审核

    private String notice;//群公告

    private Long mainuserid;//群主id

    private Integer maxnum;//最大人数

    private Integer currentnum;//当前人数

    private String[] avatarArray;//群组成员的头像数组

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
     * 群名字
     * @return groupname 群名字
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * 群名字
     * @param groupname 群名字
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }

    /**
     * 群类型
     * @return grouptype 群类型
     */
    public Integer getGrouptype() {
        return grouptype;
    }

    /**
     * 群类型
     * @param grouptype 群类型
     */
    public void setGrouptype(Integer grouptype) {
        this.grouptype = grouptype == null ? null : grouptype;
    }

    /**
     * 关联id（榜，圈子，教室的id）
     * @return relateid 关联id（榜，圈子，教室的id）
     */
    public Long getRelateid() {
        return relateid;
    }

    /**
     * 关联id（榜，圈子，教室的id）
     * @param relateid 关联id（榜，圈子，教室的id）
     */
    public void setRelateid(Long relateid) {
        this.relateid = relateid;
    }

    /**
     * ch
     * @return createtime ch
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * ch
     * @param createtime ch
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean getNeedconfirm() {
        return needconfirm;
    }

    /**
     * 设置是否需要群主验证
     * @param needconfirm
     */
    public void setNeedconfirm(boolean needconfirm) {
        this.needconfirm = needconfirm;
    }

    /**
     * 获取群主的用户id
     * @return
     */
    public Long getMainuserid() {
        return mainuserid;
    }

    /**
     * 设置群主的用户id
     * @param mainuserid
     */
    public void setMainuserid(Long mainuserid) {
        this.mainuserid = mainuserid;
    }

    /**
     * 获取群最大人数
     * @return
     */
    public Integer getMaxnum() {
        return maxnum;
    }

    /**
     * 设置群最大人数
     * @param maxnum
     */
    public void setMaxnum(Integer maxnum) {
        this.maxnum = maxnum;
    }

    /**
     * 获取当前人数
     * @return
     */
    public Integer getCurrentnum() {
        return currentnum;
    }

    /**
     * 设置当前人数
     * @param currentnum
     */
    public void setCurrentnum(Integer currentnum) {
        this.currentnum = currentnum;
    }

    public String[] getAvatarArray() {
        return avatarArray;
    }

    public void setAvatarArray(String[] avatarArray) {
        this.avatarArray = avatarArray;
    }
}