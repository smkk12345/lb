package com.longbei.appservice.entity;

import java.io.Serializable;

public class ImproveTopic implements Serializable {
    private Integer id;

    private Long impid;

    private String gtype;//微进步所属类型  0 

    private Long supertopicid;//超级话题id

    private String createtime;//创建时间

    private String updatetime;//修改时间

    private String topictitle;//缓存超级话题的title
    
    private String isdel;//是否为话题 0否 1是
    
    private String ispublic;//是否公开 0公开 1未公开

    private Long businessid;

    private Long businesstype;

    private Integer sort;//话题排序

    private Improve improve;

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public void setBusinesstype(Long businesstype) {
        this.businesstype = businesstype;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public Long getBusinesstype() {
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
    public String getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     * @param createtime 创建时间
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * 修改时间
     * @return updatetime 修改时间
     */
    public String getUpdatetime() {
        return updatetime;
    }

    /**
     * 修改时间
     * @param updatetime 修改时间
     */
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getIspublic() {
        return ispublic;
    }

    public void setIspublic(String ispublic) {
        this.ispublic = ispublic;
    }

    public Improve getImprove() {
        return improve;
    }

    public void setImprove(Improve improve) {
        this.improve = improve;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }
}