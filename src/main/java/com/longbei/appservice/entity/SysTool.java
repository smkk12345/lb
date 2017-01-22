package com.longbei.appservice.entity;

import java.util.Date;

public class SysTool {
    private Integer id;

    private String tool;//工具名称

    private String toolclass;//工具类名称

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
     * 工具名称
     * @return tool 工具名称
     */
    public String getTool() {
        return tool;
    }

    /**
     * 工具名称
     * @param tool 工具名称
     */
    public void setTool(String tool) {
        this.tool = tool == null ? null : tool.trim();
    }

    /**
     * 工具类名称
     * @return toolclass 工具类名称
     */
    public String getToolclass() {
        return toolclass;
    }

    /**
     * 工具类名称
     * @param toolclass 工具类名称
     */
    public void setToolclass(String toolclass) {
        this.toolclass = toolclass == null ? null : toolclass.trim();
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