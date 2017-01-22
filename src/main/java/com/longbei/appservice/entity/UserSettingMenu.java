package com.longbei.appservice.entity;

import java.util.Date;

public class UserSettingMenu {
    private Integer id;

    private Integer menuid;//一键直达 菜单id

    private Long userid;

    private Integer sortnum;//排序

    private String isdefault;//系统默认  位置固定

    private Date createtime;

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
     * 一键直达 菜单id
     * @return menuid 一键直达 菜单id
     */
    public Integer getMenuid() {
        return menuid;
    }

    /**
     * 一键直达 菜单id
     * @param menuid 一键直达 菜单id
     */
    public void setMenuid(Integer menuid) {
        this.menuid = menuid;
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
     * 排序
     * @return sortnum 排序
     */
    public Integer getSortnum() {
        return sortnum;
    }

    /**
     * 排序
     * @param sortnum 排序
     */
    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    /**
     * 系统默认  位置固定
     * @return isdefault 系统默认  位置固定
     */
    public String getIsdefault() {
        return isdefault;
    }

    /**
     * 系统默认  位置固定
     * @param isdefault 系统默认  位置固定
     */
    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault == null ? null : isdefault.trim();
    }

    /**
     * 
     * @return createtime 
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 
     * @param createtime 
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