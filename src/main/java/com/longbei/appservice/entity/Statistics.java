package com.longbei.appservice.entity;

import java.util.Date;

public class Statistics {
    /**
     * 
     */
    private Integer id;

    /**
     * 注册数
     */
    private Integer registernum;

    /**
     * 签到数
     */
    private Integer checknum;

    /**
     * 点赞数
     */
    private Integer likenum;

    /**
     * 鲜花数
     */
    private Integer flowernum;

    /**
     * 进步数
     */
    private Integer improvenum;

    /**
     * 龙榜数
     */
    private Integer ranknum;

    /**
     * 目标数
     */
    private Integer goalnum;

    /**
     * 订单数
     */
    private Integer ordernum;

    /**
     * 龙币充值数
     */
    private Integer moneynum;

    /**
     * 进步币发放数
     */
    private Integer iconnum;

    /**
     * 龙币使用数
     */
    private Integer moneyusenum;

    /**
     * 进步币使用数
     */
    private Integer iconusenum;

    /**
     * 奖品发放人次
     */
    private Integer awardpeoplenum;

    /**
     * 奖品发放价值
     */
    private Double awardpricenum;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegisternum() {
        return registernum;
    }

    public void setRegisternum(Integer registernum) {
        this.registernum = registernum;
    }

    public Integer getChecknum() {
        return checknum;
    }

    public void setChecknum(Integer checknum) {
        this.checknum = checknum;
    }

    public Integer getLikenum() {
        return likenum;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }

    public Integer getFlowernum() {
        return flowernum;
    }

    public void setFlowernum(Integer flowernum) {
        this.flowernum = flowernum;
    }

    public Integer getImprovenum() {
        return improvenum;
    }

    public void setImprovenum(Integer improvenum) {
        this.improvenum = improvenum;
    }

    public Integer getRanknum() {
        return ranknum;
    }

    public void setRanknum(Integer ranknum) {
        this.ranknum = ranknum;
    }

    public Integer getGoalnum() {
        return goalnum;
    }

    public void setGoalnum(Integer goalnum) {
        this.goalnum = goalnum;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public Integer getMoneynum() {
        return moneynum;
    }

    public void setMoneynum(Integer moneynum) {
        this.moneynum = moneynum;
    }

    public Integer getIconnum() {
        return iconnum;
    }

    public void setIconnum(Integer iconnum) {
        this.iconnum = iconnum;
    }

    public Integer getMoneyusenum() {
        return moneyusenum;
    }

    public void setMoneyusenum(Integer moneyusenum) {
        this.moneyusenum = moneyusenum;
    }

    public Integer getIconusenum() {
        return iconusenum;
    }

    public void setIconusenum(Integer iconusenum) {
        this.iconusenum = iconusenum;
    }

    public Integer getAwardpeoplenum() {
        return awardpeoplenum;
    }

    public void setAwardpeoplenum(Integer awardpeoplenum) {
        this.awardpeoplenum = awardpeoplenum;
    }

    public Double getAwardpricenum() {
        return awardpricenum;
    }

    public void setAwardpricenum(Double awardpricenum) {
        this.awardpricenum = awardpricenum;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}