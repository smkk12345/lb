package com.longbei.appservice.entity;

import java.util.Date;

public class RankMembers {
    private Integer id;

    private Long rankid;//榜单id

    private Long userid;//用户id

    private Integer itype;//0—上榜。1—下榜

    private Date createtime;//入榜时间

    private Date updatetime;//下榜时间

    private Integer sortnum;//榜单内排名 榜单结束之后存入

    private String iswinning;//中奖 0 未中间 1 中奖

    private Integer improvecount;//子进步条数

    private Integer likes;//榜单点赞数

    private Integer flowers;

    private String isup;//是否下榜。0-未下榜 1-已下榜

    private String acceptaward;//0 未领奖 1 领奖 2 发货 3签收

    private String isfashionman;

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
     * 榜单id
     * @return rankid 榜单id
     */
    public Long getRankid() {
        return rankid;
    }

    /**
     * 榜单id
     * @param rankid 榜单id
     */
    public void setRankid(Long rankid) {
        this.rankid = rankid;
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
     * 0—上榜。1—下榜
     * @return itype 0—上榜。1—下榜
     */
    public Integer getItype() {
        return itype;
    }

    /**
     * 0—上榜。1—下榜
     * @param itype 0—上榜。1—下榜
     */
    public void setItype(Integer itype) {
        this.itype = itype;
    }

    /**
     * 入榜时间
     * @return createtime 入榜时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 入榜时间
     * @param createtime 入榜时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 下榜时间
     * @return updatetime 下榜时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 下榜时间
     * @param updatetime 下榜时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 榜单内排名 榜单结束之后存入
     * @return sortnum 榜单内排名 榜单结束之后存入
     */
    public Integer getSortnum() {
        return sortnum;
    }

    /**
     * 榜单内排名 榜单结束之后存入
     * @param sortnum 榜单内排名 榜单结束之后存入
     */
    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    /**
     * 中奖 0 未中间 1 中奖
     * @return iswinning 中奖 0 未中间 1 中奖
     */
    public String getIswinning() {
        return iswinning;
    }

    /**
     * 中奖 0 未中间 1 中奖
     * @param iswinning 中奖 0 未中间 1 中奖
     */
    public void setIswinning(String iswinning) {
        this.iswinning = iswinning == null ? null : iswinning.trim();
    }

    /**
     * 子进步条数
     * @return improvecount 子进步条数
     */
    public Integer getImprovecount() {
        return improvecount;
    }

    /**
     * 子进步条数
     * @param improvecount 子进步条数
     */
    public void setImprovecount(Integer improvecount) {
        this.improvecount = improvecount;
    }

    /**
     * 榜单点赞数
     * @return likes 榜单点赞数
     */
    public Integer getLikes() {
        return likes;
    }

    /**
     * 榜单点赞数
     * @param likes 榜单点赞数
     */
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    /**
     * 
     * @return flowers 
     */
    public Integer getFlowers() {
        return flowers;
    }

    /**
     * 
     * @param flowers 
     */
    public void setFlowers(Integer flowers) {
        this.flowers = flowers;
    }

    /**
     * 是否下榜。0-未下榜 1-已下榜
     * @return isup 是否下榜。0-未下榜 1-已下榜
     */
    public String getIsup() {
        return isup;
    }

    /**
     * 是否下榜。0-未下榜 1-已下榜
     * @param isup 是否下榜。0-未下榜 1-已下榜
     */
    public void setIsup(String isup) {
        this.isup = isup == null ? null : isup.trim();
    }

    /**
     * 0 未领奖 1 领奖 2 发货 3签收
     * @return acceptaward 0 未领奖 1 领奖 2 发货 3签收
     */
    public String getAcceptaward() {
        return acceptaward;
    }

    /**
     * 0 未领奖 1 领奖 2 发货 3签收
     * @param acceptaward 0 未领奖 1 领奖 2 发货 3签收
     */
    public void setAcceptaward(String acceptaward) {
        this.acceptaward = acceptaward == null ? null : acceptaward.trim();
    }

    /**
     * 
     * @return isfashionman 
     */
    public String getIsfashionman() {
        return isfashionman;
    }

    /**
     * 
     * @param isfashionman 
     */
    public void setIsfashionman(String isfashionman) {
        this.isfashionman = isfashionman == null ? null : isfashionman.trim();
    }
}