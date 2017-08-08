package com.longbei.appservice.entity;

import java.math.BigDecimal;

/**
 * Created by lixb on 2017/8/8.
 */
public class UserInComeDetail {

    private Integer id;

    private Long userid;

    private String itype;  //明细类型  0 教室学费，1 送礼物，2 提现 3 提现失败扣除，4 退学费，5转入钱包

    private Integer num;   //体现龙杯数量

    private String businesstype;//业务类型

    private Long businessid;//id

    private String btitle;//冗余的业务title

    private BigDecimal commission;//提成占比

    private String createtime;

    private String updatetime;


    public String getUpdatetime() {
        return updatetime;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public Long getUserid() {
        return userid;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNum() {
        return num;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public String getBtitle() {
        return btitle;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public String getItype() {
        return itype;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setItype(String itype) {
        this.itype = itype;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
