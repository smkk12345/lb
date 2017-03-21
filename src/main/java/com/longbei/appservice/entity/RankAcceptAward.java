package com.longbei.appservice.entity;

import java.util.Date;

public class RankAcceptAward {
    /**
     * 
     */
    private Integer id;

    /**
     * 榜单id
     */
    private Long rankid;

    /**
     * 用户id
     */
    private Long userid;

    /**
     * 获奖码
     */
    private String receivecode;

    /**
     * 领奖日期
     */
    private Date acceptdate;

    /**
     * 确认领奖时间
     */
    private Date confirmacceptdate;

    /**
     * 奖品等级
     */
    private Integer awardlevel;

    /**
     * 奖品id
     */
    private Integer awardid;

    /**
     * 状态 0 - 未领取 1 - 已领取 2 - 已经发货 3 - 待确认（实物 物流） 4 - 已完成
     */
    private Integer status;

    /**
     * 收货人
     */
    private String reciverusername;

    /**
     * 收货人手机号
     */
    private String reciverusertel;

    /**
     * 收货人地址
     */
    private String reciveruseraddr;

    /**
     * 物流公司
     */
    private String logisticscompany;

    /**
     * 物流单号
     */
    private String logisticsnum;

    /**
     * 发货日期
     */
    private Date dispatchdate;

    /**
     * 备注
     */
    private String remarker;

    /**
     * 处理人id
     */
    private Integer handleuserid;

    /**
     * 创建日期
     */
    private Date createdate;

    public String getReceivecode() {
        return receivecode;
    }

    public void setReceivecode(String receivecode) {
        this.receivecode = receivecode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRankid() {
        return rankid;
    }

    public void setRankid(Long rankid) {
        this.rankid = rankid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Date getAcceptdate() {
        return acceptdate;
    }

    public void setAcceptdate(Date acceptdate) {
        this.acceptdate = acceptdate;
    }

    public Date getConfirmacceptdate() {
        return confirmacceptdate;
    }

    public void setConfirmacceptdate(Date confirmacceptdate) {
        this.confirmacceptdate = confirmacceptdate;
    }

    public Integer getAwardlevel() {
        return awardlevel;
    }

    public void setAwardlevel(Integer awardlevel) {
        this.awardlevel = awardlevel;
    }

    public Integer getAwardid() {
        return awardid;
    }

    public void setAwardid(Integer awardid) {
        this.awardid = awardid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReciverusername() {
        return reciverusername;
    }

    public void setReciverusername(String reciverusername) {
        this.reciverusername = reciverusername == null ? null : reciverusername.trim();
    }

    public String getReciverusertel() {
        return reciverusertel;
    }

    public void setReciverusertel(String reciverusertel) {
        this.reciverusertel = reciverusertel == null ? null : reciverusertel.trim();
    }

    public String getReciveruseraddr() {
        return reciveruseraddr;
    }

    public void setReciveruseraddr(String reciveruseraddr) {
        this.reciveruseraddr = reciveruseraddr == null ? null : reciveruseraddr.trim();
    }

    public String getLogisticscompany() {
        return logisticscompany;
    }

    public void setLogisticscompany(String logisticscompany) {
        this.logisticscompany = logisticscompany == null ? null : logisticscompany.trim();
    }

    public String getLogisticsnum() {
        return logisticsnum;
    }

    public void setLogisticsnum(String logisticsnum) {
        this.logisticsnum = logisticsnum == null ? null : logisticsnum.trim();
    }

    public Date getDispatchdate() {
        return dispatchdate;
    }

    public void setDispatchdate(Date dispatchdate) {
        this.dispatchdate = dispatchdate;
    }

    public String getRemarker() {
        return remarker;
    }

    public void setRemarker(String remarker) {
        this.remarker = remarker == null ? null : remarker.trim();
    }

    public Integer getHandleuserid() {
        return handleuserid;
    }

    public void setHandleuserid(Integer handleuserid) {
        this.handleuserid = handleuserid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}