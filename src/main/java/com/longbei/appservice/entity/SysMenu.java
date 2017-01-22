package com.longbei.appservice.entity;

import java.util.Date;

public class SysMenu {
    private Integer id;

    private String menu;//菜单名称

    private String isdefault;//0 系统默认 1 非默认

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

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
     * 菜单名称
     * @return menu 菜单名称
     */
    public String getMenu() {
        return menu;
    }

    /**
     * 菜单名称
     * @param menu 菜单名称
     */
    public void setMenu(String menu) {
        this.menu = menu == null ? null : menu.trim();
    }

    /**
     * 0 系统默认 1 非默认
     * @return isdefault 0 系统默认 1 非默认
     */
    public String getIsdefault() {
        return isdefault;
    }

    /**
     * 0 系统默认 1 非默认
     * @param isdefault 0 系统默认 1 非默认
     */
    public void setIsdefault(String isdefault) {
        this.isdefault = isdefault == null ? null : isdefault.trim();
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
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}