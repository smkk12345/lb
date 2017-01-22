package com.longbei.appservice.entity;

import java.util.Date;

public class UserSettingTool {
    private Integer id;

    private Long userid;

    private Integer toolid;//工具id

    private Integer sortnum;//排序

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
     * 工具id
     * @return toolid 工具id
     */
    public Integer getToolid() {
        return toolid;
    }

    /**
     * 工具id
     * @param toolid 工具id
     */
    public void setToolid(Integer toolid) {
        this.toolid = toolid;
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