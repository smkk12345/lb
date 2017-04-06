package com.longbei.appservice.entity;

import java.util.Date;

public class UserSettingMenu {
    private Integer id;

    private Integer menutype;//一键直达 菜单id

    private Long userid;

    private Integer sortnum;//排序

    private String isdefault;//系统默认  位置固定

    private String picurl;

    private String displaytitle;

    private String defaultmenu;

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

    public void setDisplaytitle(String displaytitle) {
        this.displaytitle = displaytitle;
    }

    public void setMenutype(Integer menutype) {
        this.menutype = menutype;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public Integer getMenutype() {
        return menutype;
    }

    public String getDisplaytitle() {
        return displaytitle;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setDefaultmenu(String defaultmenu) {
        this.defaultmenu = defaultmenu;
    }

    public String getDefaultmenu() {
        return defaultmenu;
    }

}