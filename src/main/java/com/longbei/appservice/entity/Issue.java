package com.longbei.appservice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wu on 2017/4/25.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Issue  implements Serializable {

    private Integer id;
    private String title;//问题标题
    private String typeid;//类型id
    private String content;//问题内容
    private String ishot;//是否热点 0普通文章 1热点
    private String createtime;//创建时间
    private String updatetime;//修改时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getCreatetime() {
        return createtime;
    }
}
