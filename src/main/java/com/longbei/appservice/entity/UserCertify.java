package com.longbei.appservice.entity;

import java.util.Date;

public class UserCertify {
    private Integer id;

    private Long userid;

    private String ctype;

    private String photes;

    private String result;

    private Long checkuid;

    private Date checkdate;

    private Date createtime;

    private Date updatetime;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCtype() {
        return ctype;
    }

    public String getPhotes() {
        return photes;
    }

    public void setPhotes(String photes) {
        this.photes = photes;
    }

    public String getResult() {

        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getCheckuid() {
        return checkuid;
    }

    public void setCheckuid(Long checkuid) {
        this.checkuid = checkuid;
    }

    public Date getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}