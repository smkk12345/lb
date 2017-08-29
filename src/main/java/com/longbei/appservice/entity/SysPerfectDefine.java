package com.longbei.appservice.entity;

import java.io.Serializable;

public class SysPerfectDefine implements Serializable {
    private Integer id;
    private String ptype;
    private String author;
    private String comment;
    private String createtime;
    private String updatetime;

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

    public String getPtype() {
        return ptype;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

}