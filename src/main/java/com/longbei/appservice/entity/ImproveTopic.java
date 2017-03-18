package com.longbei.appservice.entity;

import java.util.Date;

public class ImproveTopic {
    private Integer id;

    private Long impid;

    private String gtype;//微进步所属类型  0 

    private Long supertopicid;//超级话题id

    private Date createtime;//创建时间

    private Date updatetime;//修改时间

    private String topictitle;//换成一下超级话题的title

    private long businessid;

    private long businesstype;

    public void setBusinessid(long businessid) {
        this.businessid = businessid;
    }

    public void setBusinesstype(long businesstype) {
        this.businesstype = businesstype;
    }

    public long getBusinessid() {
        return businessid;
    }

    public long getBusinesstype() {
        return businesstype;
    }

    public String getTopictitle() {
        return topictitle;
    }

    public void setTopictitle(String topictitle) {
        this.topictitle = topictitle;
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
     * @return impid 
     */
    public Long getImpid() {
        return impid;
    }

    /**
     * 
     * @param impid 
     */
    public void setImpid(Long impid) {
        this.impid = impid;
    }

    /**
     * 微进步所属类型  0 
     * @return gtype 微进步所属类型  0 
     */
    public String getGtype() {
        return gtype;
    }

    /**
     * 微进步所属类型  0 
     * @param gtype 微进步所属类型  0 
     */
    public void setGtype(String gtype) {
        this.gtype = gtype == null ? null : gtype.trim();
    }

    /**
     * 超级话题id
     * @return supertopicid 超级话题id
     */
    public Long getSupertopicid() {
        return supertopicid;
    }

    /**
     * 超级话题id
     * @param supertopicid 超级话题id
     */
    public void setSupertopicid(Long supertopicid) {
        this.supertopicid = supertopicid;
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
     * 修改时间
     * @return updatetime 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 修改时间
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}