package com.longbei.appservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by IngaWu on 2017/4/25.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class IssueClassify implements Serializable {

    private Integer id;
    private Long typeid;//类型id
    private String typetitle;//类型名称
    private Integer contentcount;//内容条数
    private Integer sortno;//排序
    private String createtime;//创建时间
    private String updatetime;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContentcount(Integer contentcount) {
        this.contentcount = contentcount;
    }

    public Integer getContentcount() {
        return contentcount;
    }

    public void setTypetitle(String typetitle) {
        this.typetitle = typetitle;
    }

    public String getTypetitle() {
        return typetitle;
    }

    public Long getTypeid() {
        return typeid;
    }

    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }
}
