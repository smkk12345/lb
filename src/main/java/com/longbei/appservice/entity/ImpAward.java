package com.longbei.appservice.entity;

import java.util.Date;

public class ImpAward {
    private Integer id;

    private Long improveid;//进步id 

    private Long userid;//用户id

    private String logisticscom;//物流公司

    private String logremark;//备注

    private String address;//地址

    private Date drawtime;//领奖时间

    private String logisticscode;

    private String region;//区域

    private String receiver;//接收人

    private String mobile;//手机号

    private Date shipdate;//发货日期

    private String awardid;

    private Integer awardlevel;

    private String awardname;

    private Date accepttime;//领奖时间

    private Integer awardprice;


    public void setAwardprice(Integer awardprice) {
        this.awardprice = awardprice;
    }

    public Integer getAwardprice() {
        return awardprice;
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
     * 进步id 
     * @return improveid 进步id 
     */
    public Long getImproveid() {
        return improveid;
    }

    /**
     * 进步id 
     * @param improveid 进步id 
     */
    public void setImproveid(Long improveid) {
        this.improveid = improveid;
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
     * 物流公司
     * @return logisticscom 物流公司
     */
    public String getLogisticscom() {
        return logisticscom;
    }

    /**
     * 物流公司
     * @param logisticscom 物流公司
     */
    public void setLogisticscom(String logisticscom) {
        this.logisticscom = logisticscom == null ? null : logisticscom.trim();
    }

    /**
     * 备注
     * @return logremark 备注
     */
    public String getLogremark() {
        return logremark;
    }

    /**
     * 备注
     * @param logremark 备注
     */
    public void setLogremark(String logremark) {
        this.logremark = logremark == null ? null : logremark.trim();
    }

    /**
     * 地址
     * @return address 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 地址
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 领奖时间
     * @return drawtime 领奖时间
     */
    public Date getDrawtime() {
        return drawtime;
    }

    /**
     * 领奖时间
     * @param drawtime 领奖时间
     */
    public void setDrawtime(Date drawtime) {
        this.drawtime = drawtime;
    }

    /**
     * 
     * @return logisticsCode 
     */
    public String getLogisticscode() {
        return logisticscode;
    }

    /**
     * 
     * @param logisticscode 
     */
    public void setLogisticscode(String logisticscode) {
        this.logisticscode = logisticscode == null ? null : logisticscode.trim();
    }

    /**
     * 区域
     * @return region 区域
     */
    public String getRegion() {
        return region;
    }

    /**
     * 区域
     * @param region 区域
     */
    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    /**
     * 接收人
     * @return receiver 接收人
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 接收人
     * @param receiver 接收人
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    /**
     * 手机号
     * @return mobile 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 手机号
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 发货日期
     * @return shipdate 发货日期
     */
    public Date getShipdate() {
        return shipdate;
    }

    /**
     * 发货日期
     * @param shipdate 发货日期
     */
    public void setShipdate(Date shipdate) {
        this.shipdate = shipdate;
    }

    /**
     * 
     * @return awardid 
     */
    public String getAwardid() {
        return awardid;
    }

    /**
     * 
     * @param awardid 
     */
    public void setAwardid(String awardid) {
        this.awardid = awardid == null ? null : awardid.trim();
    }

    /**
     * 
     * @return awardlevel 
     */
    public Integer getAwardlevel() {
        return awardlevel;
    }

    /**
     * 
     * @param awardlevel 
     */
    public void setAwardlevel(Integer awardlevel) {
        this.awardlevel = awardlevel;
    }

    /**
     * 
     * @return awardname 
     */
    public String getAwardname() {
        return awardname;
    }

    /**
     * 
     * @param awardname 
     */
    public void setAwardname(String awardname) {
        this.awardname = awardname == null ? null : awardname.trim();
    }

    /**
     * 领奖时间
     * @return accepttime 领奖时间
     */
    public Date getAccepttime() {
        return accepttime;
    }

    /**
     * 领奖时间
     * @param accepttime 领奖时间
     */
    public void setAccepttime(Date accepttime) {
        this.accepttime = accepttime;
    }
}