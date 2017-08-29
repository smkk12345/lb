package com.longbei.appservice.entity;

import java.io.Serializable;
import java.util.Date;

public class SysRuleCheckin implements Serializable {
    private Integer id;

    private Integer continues;//持续天数

    private Integer awardmoney;//签到奖励

    private Date createtime;//创建时间

    private Date updatetime;//更新时间

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
     * 持续天数
     * @return continues 持续天数
     */
    public Integer getContinues() {
        return continues;
    }

    /**
     * 持续天数
     * @param continues 持续天数
     */
    public void setContinues(Integer continues) {
        this.continues = continues;
    }

    /**
     * 签到奖励
     * @return awardmoney 签到奖励
     */
    public Integer getAwardmoney() {
        return awardmoney;
    }

    /**
     * 签到奖励
     * @param awardmoney 签到奖励
     */
    public void setAwardmoney(Integer awardmoney) {
        this.awardmoney = awardmoney;
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
}