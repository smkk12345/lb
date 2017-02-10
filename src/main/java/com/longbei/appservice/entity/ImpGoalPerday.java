package com.longbei.appservice.entity;

import java.util.Date;

public class ImpGoalPerday {
    private Integer id;

    private String impids;//进步id  逗号隔开

    private String drawdate;//当前日期   年月日

    private Date createtime;

    private Date updatetime;

    private Integer continuedays;//持续天数

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
     * 进步id  逗号隔开
     * @return impids 进步id  逗号隔开
     */
    public String getImpids() {
        return impids;
    }

    /**
     * 进步id  逗号隔开
     * @param impids 进步id  逗号隔开
     */
    public void setImpids(String impids) {
        this.impids = impids == null ? null : impids.trim();
    }

    /**
     * 当前日期   年月日
     * @return drawdate 当前日期   年月日
     */
    public String getDrawdate() {
        return drawdate;
    }

    /**
     * 当前日期   年月日
     * @param drawdate 当前日期   年月日
     */
    public void setDrawdate(String drawdate) {
        this.drawdate = drawdate == null ? null : drawdate.trim();
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
     * 持续天数
     * @return continuedays 持续天数
     */
    public Integer getContinuedays() {
        return continuedays;
    }

    /**
     * 持续天数
     * @param continuedays 持续天数
     */
    public void setContinuedays(Integer continuedays) {
        this.continuedays = continuedays;
    }
}