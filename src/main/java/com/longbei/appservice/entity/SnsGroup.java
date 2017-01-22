package com.longbei.appservice.entity;

import java.util.Date;

public class SnsGroup {
    private Integer id;

    private Long groupid;//群id

    private String groupname;//群名字

    private String grouptype;//群类型

    private Long relateid;//关联id（榜，圈子，教室的id）

    private Date createtime;//ch

    private Date updatetime;

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
    public String getGrouptype() {
        return grouptype;
    }

    /**
     * 群类型
     * @param grouptype 群类型
     */
    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype == null ? null : grouptype.trim();
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
}