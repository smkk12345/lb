package com.longbei.appservice.entity;

import java.util.Date;

public class SysRuleLp {
    private Integer id;

    private Integer level;

    private String privilege;//特权   按照分类处理

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
     * @return level 
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 
     * @param level 
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 特权   按照分类处理
     * @return privilege 特权   按照分类处理
     */
    public String getPrivilege() {
        return privilege;
    }

    /**
     * 特权   按照分类处理
     * @param privilege 特权   按照分类处理
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege == null ? null : privilege.trim();
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