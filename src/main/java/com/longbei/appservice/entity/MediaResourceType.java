package com.longbei.appservice.entity;

import java.util.Date;

/**
 * Created by wangyongzhi 17/7/27.
 */
public class MediaResourceType {
    public static final Integer userMaxMediaResourceTypeCount = 3;

    private Integer id;
    private Date createtime;
    private String userid;//用户id
    private String typename;//类型名称
    private Integer sort;//排序 0代表排在第一个
    private Integer icount;//当前分类下的资源数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIcount() {
        return icount;
    }

    public void setIcount(Integer icount) {
        this.icount = icount;
    }
}
