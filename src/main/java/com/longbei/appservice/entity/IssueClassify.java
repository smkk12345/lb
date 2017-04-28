package com.longbei.appservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by IngaWu on 2017/4/25.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class IssueClassify implements Serializable {

    private Integer id;
    private String typeid;//类型id
    private String typeTitle;//类型名称
    private Integer contentCount;//内容条数
    private String createtime;//创建时间
    private String updatetime;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
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

    public void setContentCount(Integer contentCount) {
        this.contentCount = contentCount;
    }

    public Integer getContentCount() {
        return contentCount;
    }

    public IssueClassify() {
    }

    public IssueClassify(Integer id, String typeid, String typeTitle, Integer contentCount, String createtime, String updatetime) {
        this.id = id;
        this.typeid = typeid;
        this.typeTitle = typeTitle;
        this.contentCount = contentCount;
        this.createtime = createtime;
        this.updatetime = updatetime;
    }
}
