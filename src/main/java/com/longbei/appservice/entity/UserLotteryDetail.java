package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryDetail implements Serializable {
    private Integer id;

    private Long userid;

    private String ltype;//抽奖类型  0榜单内抽奖  1公益抽奖

    private Integer money;//龙币数量

    private Long rankid;//榜单id

    private Integer impcoin;//进步币

    private Date createtime;

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
     * 抽奖类型  0榜单内抽奖  1公益抽奖
     * @return ltype 抽奖类型  0榜单内抽奖  1公益抽奖
     */
    public String getLtype() {
        return ltype;
    }

    /**
     * 抽奖类型  0榜单内抽奖  1公益抽奖
     * @param ltype 抽奖类型  0榜单内抽奖  1公益抽奖
     */
    public void setLtype(String ltype) {
        this.ltype = ltype == null ? null : ltype.trim();
    }

    /**
     * 龙币数量
     * @return money 龙币数量
     */
    public Integer getMoney() {
        return money;
    }

    /**
     * 龙币数量
     * @param money 龙币数量
     */
    public void setMoney(Integer money) {
        this.money = money;
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
     * 进步币
     * @return impcoin 进步币
     */
    public Integer getImpcoin() {
        return impcoin;
    }

    /**
     * 进步币
     * @param impcoin 进步币
     */
    public void setImpcoin(Integer impcoin) {
        this.impcoin = impcoin;
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
}