package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class PutFashionman implements Serializable {
    private Integer id;

    private Long userid;//用户id

    private String isfashionman;//是否是达人 0.不是达人 1.是达人

    private Long rankid;//达人对应的榜单id

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

    private Integer sort;//达人排序

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
     * 用户id
     * @return userid 用户id
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 用户id
     * @param userid 用户id
     */
    public void setUserid(Long userid) {
        this.userid = userid;
    }

    /**
     * 是否是达人
     * @return isfashionman 是否是达人
     */
    public String getIsfashionman() {
        return isfashionman;
    }

    /**
     * 是否是达人
     * @param isfashionman 是否是达人
     */
    public void setIsfashionman(String isfashionman) {
        this.isfashionman = isfashionman == null ? null : isfashionman.trim();
    }

    /**
     * 达人对应的榜单id
     * @return rankid 达人对应的榜单id
     */
    public Long getRankid() {
        return rankid;
    }

    /**
     * 达人对应的榜单id
     * @param rankid 达人对应的榜单id
     */
    public void setRankid(Long rankid) {
        this.rankid = rankid;
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
     * 更新时间
     * @return updatetime 更新时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     * @param updatetime 更新时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 达人排序
     * @return sort 达人排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 达人排序
     * @param sort 达人排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}