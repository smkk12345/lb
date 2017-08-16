package com.longbei.appservice.entity;

import com.longbei.appservice.config.AppserviceConfig;

import java.util.Date;
import java.util.List;

public class UserInComeOrder {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户收益结算单id
     */
    private Long uioid;

    /**
     * 
     */
    private Long userid;

    /**
     * 
     */
    private Long detailid;

    /**
     * 结算单状态 0 - 申请结算。1 - 运营处理同意 2 - 育婴处理不同意 3 - 财务处理不同意 4 - 财务出来同意，完成处理
     */
    private Integer uiostatus;

    /**
     * 结算龙币数量
     */
    private String num;

    /**
     * 结算金额
     */
    private Integer settlenum;

    /**
     * 收款银行 
     */
    private String receiptBank;

    /**
     * 收款的号码
     */
    private String receiptNum;

    /**
     * 收款人
     */
    private String receiptUser;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Date updatetime;

    /**
     * 
     */
    private String dealoption;


    /**
     * 显示用
     */
    private AppUserMongoEntity appUserMongoEntity;

    /**
     * 搜索用
     */
    private List<String> userids;

    public Integer getSettlenum() {
        return this.num==null?0:Integer.parseInt(
                String.valueOf(Math.ceil(Double.parseDouble(num) / AppserviceConfig.yuantomoney)));
    }

    public void setSettlenum(Integer settlenum) {
        this.settlenum = settlenum;
    }

    public AppUserMongoEntity getAppUserMongoEntity() {
        return appUserMongoEntity;
    }

    public void setAppUserMongoEntity(AppUserMongoEntity appUserMongoEntity) {
        this.appUserMongoEntity = appUserMongoEntity;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUioid() {
        return uioid;
    }

    public void setUioid(Long uioid) {
        this.uioid = uioid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getDetailid() {
        return detailid;
    }

    public void setDetailid(Long detailid) {
        this.detailid = detailid;
    }

    public Integer getUiostatus() {
        return uiostatus;
    }

    public void setUiostatus(Integer uiostatus) {
        this.uiostatus = uiostatus;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }

    public String getReceiptBank() {
        return receiptBank;
    }

    public void setReceiptBank(String receiptBank) {
        this.receiptBank = receiptBank == null ? null : receiptBank.trim();
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum == null ? null : receiptNum.trim();
    }

    public String getReceiptUser() {
        return receiptUser;
    }

    public void setReceiptUser(String receiptUser) {
        this.receiptUser = receiptUser == null ? null : receiptUser.trim();
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

    public String getDealoption() {
        return dealoption;
    }

    public void setDealoption(String dealoption) {
        this.dealoption = dealoption == null ? null : dealoption.trim();
    }
}