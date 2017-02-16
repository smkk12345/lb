package com.longbei.appservice.entity;

import java.util.Date;

public class UserCollect {
    private Integer id;

    private Long userid;

    private Long cid;//收藏内容id

    private Date createtime;

    private Date updatetime;

    private String ctype;//收藏类型  0 微进步 1 其他

    private String businessid;

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

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
     * 收藏内容id
     * @return cid 收藏内容id
     */
    public Long getCid() {
        return cid;
    }

    /**
     * 收藏内容id
     * @param cid 收藏内容id
     */
    public void setCid(Long cid) {
        this.cid = cid;
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

    /**
     * 收藏类型  0 微进步 1 其他
     * @return ctype 收藏类型  0 微进步 1 其他
     */
    public String getCtype() {
        return ctype;
    }

    /**
     * 收藏类型  0 微进步 1 其他
     * @param ctype 收藏类型  0 微进步 1 其他
     */
    public void setCtype(String ctype) {
        this.ctype = ctype == null ? null : ctype.trim();
    }
}