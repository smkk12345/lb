package com.longbei.appservice.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ProductCategory {
    private Integer id;//类别id

    private String catename;//类别名字

    private String parentid;//类别父级id
    
    private String parentids; //父类集合id

    private String sort;//排序

    private Date createtime;//创建时间

    private Date updatetime;//分型时间

    /**
     * 类别id
     * @return id 类别id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 类别id
     * @param id 类别id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 类别名字
     * @return catename 类别名字
     */
    public String getCatename() {
        return catename;
    }

    /**
     * 类别名字
     * @param catename 类别名字
     */
    public void setCatename(String catename) {
        this.catename = catename == null ? null : catename.trim();
    }

    /**
     * 类别父级id
     * @return parentid 类别父级id
     */
    @JsonInclude(Include.ALWAYS)
    public String getParentid() {
        return parentid;
    }

    /**
     * 类别父级id
     * @param parentid 类别父级id
     */
    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    /**
     * 排序
     * @return sort 排序
     */
    @JsonInclude(Include.ALWAYS)
    public String getSort() {
        return sort;
    }

    /**
     * 排序
     * @param sort 排序
     */
    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
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
     * 分型时间
     * @return updatetime 分型时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 分型时间
     * @param updatetime 分型时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    @JsonInclude(Include.ALWAYS)
	public String getParentids() {
		return parentids;
	}

	public void setParentids(String parentids) {
		this.parentids = parentids;
	}
}